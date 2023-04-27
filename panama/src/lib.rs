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

use std::ffi::{CStr, CString};
use std::os::raw::c_char;

use gaoya::minhash::{MinHasher32, MinHashIndex};
use whichlicense_detection::detecting::fuzzy_implementation::fuzzy_implementation::FuzzyDetection;
use whichlicense_detection::detecting::gaoya_implementation::gaoya_implementation::GaoyaDetection;
use whichlicense_detection::LicenseListActions;

#[repr(C)]
pub struct FuzzyHashingConfig {
    licenses_json: *const c_char,
    confidence_threshold: u8,
    exit_on_exact_match: bool,
}

#[no_mangle]
pub extern "C" fn fuzzy_compute_hash<'jvm>(config: &'jvm FuzzyHashingConfig, license: &'jvm c_char) -> *const c_char {
    let fuzzy = FuzzyDetection {
        licenses: vec![],
        min_confidence: config.confidence_threshold,
        exit_on_exact_match: config.exit_on_exact_match,
    };

    let raw_license = unsafe { CStr::from_ptr(license) }.to_str().unwrap();
    let hash = fuzzy.hash_from_inline_string(raw_license.to_string());

    CString::new(hash).unwrap().into_raw()
}

#[no_mangle]
pub extern "C" fn fuzzy_detect_license<'jvm>(config: &'jvm FuzzyHashingConfig, license: &'jvm c_char) -> *const c_char {
    let mut fuzzy = FuzzyDetection {
        licenses: vec![],
        min_confidence: config.confidence_threshold,
        exit_on_exact_match: config.exit_on_exact_match,
    };

    let licenses = unsafe { CStr::from_ptr(config.licenses_json) }.to_str().unwrap();
    fuzzy.load_from_inline_string(licenses.to_string());

    let raw_license_text = unsafe { CStr::from_ptr(license) }.to_str().unwrap();
    let matches = fuzzy.match_by_plain_text(raw_license_text.to_string()).iter()
        .map(|m| format!("{}: {}", m.name, m.confidence))
        .collect::<Vec<String>>().join(";");

    CString::new(matches).unwrap().into_raw()
}

#[repr(C)]
pub struct GaoyaHashingConfig {
    licenses_json: *const c_char,
    band_count: usize,
    band_width: usize,
    shingle_size: usize,
}

#[no_mangle]
pub extern "C" fn gaoya_compute_hash<'jvm>(config: &'jvm GaoyaHashingConfig, license: &'jvm c_char) -> *const c_char {
    let gaoya = GaoyaDetection {
        index: MinHashIndex::new(config.band_count, config.band_width, 0.5),
        min_hasher: MinHasher32::new(config.band_count * config.band_width),
        shingle_text_size: config.shingle_size,
    };

    let raw_license = unsafe { CStr::from_ptr(license) }.to_str().unwrap();
    let hash = gaoya.hash_from_inline_string(raw_license.to_string()).iter()
        .map(|n| n.to_string()).collect::<Vec<String>>().join(",");

    CString::new(format!("[{}]", hash)).unwrap().into_raw()
}

#[no_mangle]
pub extern "C" fn gaoya_detect_license<'jvm>(config: &'jvm GaoyaHashingConfig, license: &'jvm c_char) -> *const c_char {
    let mut gaoya = GaoyaDetection {
        index: MinHashIndex::new(config.band_count, config.band_width, 0.5),
        min_hasher: MinHasher32::new(config.band_count * config.band_width),
        shingle_text_size: config.shingle_size,
    };

    let licenses = unsafe { CStr::from_ptr(config.licenses_json) }.to_str().unwrap();
    gaoya.load_from_inline_string(licenses.to_string());

    let raw_license_text = unsafe { CStr::from_ptr(license) }.to_str().unwrap();
    let matches = gaoya.match_by_plain_text(raw_license_text.to_string()).iter()
        .map(|m| format!("{}: {}", m.name, m.confidence))
        .collect::<Vec<String>>().join(";");

    CString::new(matches).unwrap().into_raw()
}
