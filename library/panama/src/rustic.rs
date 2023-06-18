/*
*   Copyright (c) 2023 David Greven
*   All rights reserved.
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

pub mod ffm {
    use std::ffi::{CStr, CString};
    use std::os::raw::c_char;
    use std::ptr;
    use std::slice;

    pub type CNormalizationFn = Option<extern "C" fn(*const c_char) -> *const c_char>;

    #[inline(always)]
    pub fn rustic_normalize(normalization: CNormalizationFn, license: &str) -> &str {
        let c_license = CString::new(license)
            .expect("failed to convert the license into a CString");
        match normalization {
            Some(normalization_fn) => unsafe {
                CStr::from_ptr((normalization_fn)(c_license.into_raw()))
            }.to_str().expect("failed to obtain the normalized license text"),
            None => license
        }
    }

    #[inline(always)]
    pub fn rustic_string(ptr: *const c_char, msg: &str) -> &str {
        unsafe { CStr::from_ptr(ptr) }.to_str().expect(msg)
    }

    #[inline(always)]
    pub fn c_string(str: String, msg: &str) -> *const c_char {
        CString::new(str).expect(msg).into_raw()
    }

    #[inline(always)]
    pub fn c_box<T>(rustic_box: Box<[T]>) -> *const T {
        if rustic_box.is_empty() { return ptr::null(); }
        Box::into_raw(rustic_box) as *const T
    }

    #[inline(always)]
    pub fn rustic_vec<T>(ptr: *const T, size: usize) -> Vec<T> where T: Clone {
        if size == 0 { return Vec::new(); }
        unsafe { slice::from_raw_parts(ptr, size) }.to_vec()
    }

    #[inline(always)]
    pub unsafe fn unsafe_rustic_vec<T>(ptr: *const T, size: usize) -> Vec<T> {
        let mut vec = Vec::with_capacity(size) as Vec<T>;
        vec.set_len(size);
        ptr::copy_nonoverlapping(ptr, vec.as_mut_ptr(), size);
        vec
    }
}

pub mod repr {
    use std::ffi::c_void;
    use std::os::raw::c_char;
    use crate::rustic::ffm::CNormalizationFn;

    pub type LicenseIndex = *mut c_void;

    #[repr(C)]
    pub struct LicenseMatchEntry {
        pub name: *const c_char,
        pub confidence: f32,
    }

    #[repr(C)]
    pub struct LicenseMatches {
        pub matches: *const LicenseMatchEntry,
        pub length: usize,
    }

    #[repr(C)]
    pub struct FuzzyHashingConfig {
        pub index: LicenseIndex,
        pub exit_on_exact_match: bool,
        pub normalization_fn: CNormalizationFn,
    }

    #[repr(C)]
    pub struct GaoyaHashingConfig {
        pub index: LicenseIndex,
        pub band_count: usize,
        pub band_width: usize,
        pub shingle_size: usize,
        pub normalization_fn: CNormalizationFn,
    }
}

pub mod pipeline {
    use std::ffi::c_void;
    use whichlicense_detection::Segment;
    use crate::rustic::repr::LicenseMatches;

    #[repr(C)]
    pub struct PipelineLicenseMatches {
        pub step_matches: *const LicenseMatches,
        pub length: usize,
    }

    #[repr(C)]
    pub struct PipelineConfig {
        pub steps: *mut c_void,
        pub length: usize,
        pub threshold: f32,
    }

    #[inline(always)]
    pub fn segment_to_raw_ptr(seg: Segment) -> *mut c_void {
        Box::into_raw(Box::new(seg)) as *mut c_void
    }
}