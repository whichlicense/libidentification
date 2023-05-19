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
use whichlicense_detection::{DEFAULT_NORMALIZATION_FN, LicenseListActions};

#[repr(C)]
pub struct FuzzyHashingConfig {
    licenses_json: *const c_char,
    confidence_threshold: u8,
    exit_on_exact_match: bool,
    normalization_fn: extern "C" fn(*const c_char) -> *const c_char,
}

#[no_mangle]
pub extern "C" fn fuzzy_compute_hash<'jvm>(config: &'jvm FuzzyHashingConfig, license: &'jvm c_char) -> *const c_char {
    let fuzzy = FuzzyDetection {
        licenses: vec![],
        min_confidence: config.confidence_threshold,
        exit_on_exact_match: config.exit_on_exact_match,
        normalization_fn: DEFAULT_NORMALIZATION_FN,
    };

    let raw_license = unsafe { CStr::from_ptr(license) }.to_str().expect("failed to obtain the license text");
    let hash = fuzzy.hash_from_inline_string(raw_license);

    CString::new(hash).expect("failed to convert the hash into a CString").into_raw()
}

#[no_mangle]
pub extern "C" fn fuzzy_detect_license<'jvm>(config: &'jvm FuzzyHashingConfig, license: &'jvm c_char) -> *const c_char {
    let mut fuzzy = FuzzyDetection {
        licenses: vec![],
        min_confidence: config.confidence_threshold,
        exit_on_exact_match: config.exit_on_exact_match,
        normalization_fn: DEFAULT_NORMALIZATION_FN,
    };

    let licenses = unsafe { CStr::from_ptr(config.licenses_json) }.to_str().expect("failed to obtain the license index");
    fuzzy.load_from_inline_string(licenses);

    let raw_license = unsafe { CStr::from_ptr(license) }.to_str().expect("failed to obtain the license text");
    let matches = fuzzy.match_by_plain_text(raw_license).iter()
        .map(|m| format!("{}: {}", m.name, m.confidence))
        .collect::<Vec<String>>().join(";");

    CString::new(matches).expect("failed to convert the matches into a CString").into_raw()
}

#[repr(C)]
pub struct GaoyaHashingConfig {
    licenses_json: *const c_char,
    band_count: usize,
    band_width: usize,
    shingle_size: usize,
    normalization_fn: extern "C" fn(*const c_char) -> *const c_char,
}

#[no_mangle]
pub extern "C" fn gaoya_compute_hash<'jvm>(config: &'jvm GaoyaHashingConfig, license: &'jvm c_char) -> *const c_char {
    let gaoya = GaoyaDetection {
        index: MinHashIndex::new(config.band_count, config.band_width, 0.5),
        min_hasher: MinHasher32::new(config.band_count * config.band_width),
        shingle_text_size: config.shingle_size,
        normalization_fn: DEFAULT_NORMALIZATION_FN,
    };

    let raw_license = unsafe { CStr::from_ptr(license) }.to_str().expect("failed to obtain the license text");
    let hash = gaoya.hash_from_inline_string(raw_license).iter()
        .map(|n| n.to_string()).collect::<Vec<String>>().join(",");

    CString::new(format!("[{}]", hash)).expect("failed to convert the hash into a CString").into_raw()
}

#[no_mangle]
pub extern "C" fn gaoya_detect_license<'jvm>(config: &'jvm GaoyaHashingConfig, license: &'jvm c_char) -> *const c_char {
    let mut gaoya = GaoyaDetection {
        index: MinHashIndex::new(config.band_count, config.band_width, 0.5),
        min_hasher: MinHasher32::new(config.band_count * config.band_width),
        shingle_text_size: config.shingle_size,
        normalization_fn: DEFAULT_NORMALIZATION_FN,
    };

    let licenses = unsafe { CStr::from_ptr(config.licenses_json) }.to_str().expect("failed to obtain the license index");
    gaoya.load_from_inline_string(licenses);

    let raw_license = unsafe { CStr::from_ptr(license) }.to_str().expect("failed to obtain the license text");
    let matches = gaoya.match_by_plain_text(raw_license).iter()
        .map(|m| format!("{}: {}", m.name, m.confidence))
        .collect::<Vec<String>>().join(";");

    CString::new(matches).expect("failed to convert the matches into a CString").into_raw()
}

/*#[repr(C)]
pub enum PipelineStepKind { FUNC, REGEX, IMPL }

#[repr(C)]
pub union PipelineStepProcessor {
    function: extern "C" fn(*const c_char) -> *const c_char,
    regex: *const c_char,
    implementation: *const c_char,
}

#[repr(C)]
pub struct PipelineStep {
    kind: PipelineStepKind,
    processor: PipelineStepProcessor,
}

#[repr(C)]
pub struct Pipeline {
    threshold: f32,
    steps: [PipelineStepProcessor],
}

#[no_mangle]
pub extern "C" fn fuzzy_pipeline_detect_license<'jvm>(config: &'jvm FuzzyHashingConfig, pipeline: &'jvm Pipeline, license: &'jvm c_char) -> *const c_char {
    CString::new("").expect("placeholder").into_raw()
}

#[no_mangle]
pub extern "C" fn gaoya_pipeline_detect_license<'jvm>(config: &'jvm GaoyaHashingConfig, pipeline: &'jvm Pipeline, license: &'jvm c_char) -> *const c_char {
    CString::new("").expect("placeholder").into_raw()
}*/