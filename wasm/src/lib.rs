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

use gaoya::minhash::{MinHasher32, MinHashIndex};
use wasm_bindgen::prelude::*;
use whichlicense_detection::detecting::fuzzy_implementation::fuzzy_implementation::FuzzyDetection;
use whichlicense_detection::detecting::gaoya_implementation::gaoya_implementation::GaoyaDetection;
use whichlicense_detection::LicenseListActions;

#[wasm_bindgen]
pub struct FuzzyHashingConfig {
    licenses_json: String,
    confidence_threshold: u8,
    exit_on_exact_match: bool,
}

#[wasm_bindgen]
impl FuzzyHashingConfig {
    #[wasm_bindgen(constructor)]
    pub fn new(licenses_json: String, confidence_threshold: u8, exit_on_exact_match: bool) -> FuzzyHashingConfig {
        FuzzyHashingConfig { licenses_json, confidence_threshold, exit_on_exact_match }
    }
}

#[wasm_bindgen]
pub fn fuzzy_compute_hash(config: &FuzzyHashingConfig, license: String) -> String {
    let fuzzy = FuzzyDetection {
        licenses: vec![],
        min_confidence: config.confidence_threshold,
        exit_on_exact_match: config.exit_on_exact_match,
    };
    return fuzzy.hash_from_inline_string(license);
}

#[wasm_bindgen]
pub fn fuzzy_detect_license(config: &FuzzyHashingConfig, license: String) -> String {
    let mut fuzzy = FuzzyDetection {
        licenses: vec![],
        min_confidence: config.confidence_threshold,
        exit_on_exact_match: config.exit_on_exact_match,
    };

    fuzzy.load_from_inline_string(config.licenses_json.clone());

    fuzzy.match_by_plain_text(license).iter()
        .map(|m| format!("{}: {}", m.name, m.confidence))
        .collect::<Vec<String>>().join(";")
}

#[wasm_bindgen]
pub struct GaoyaHashingConfig {
    licenses_json: String,
    band_count: usize,
    band_width: usize,
    shingle_size: usize,
}

#[wasm_bindgen]
impl GaoyaHashingConfig {
    #[wasm_bindgen(constructor)]
    pub fn new(licenses_json: String, band_count: usize, band_width: usize, shingle_size: usize) -> GaoyaHashingConfig {
        GaoyaHashingConfig { licenses_json, band_count, band_width, shingle_size }
    }
}

#[wasm_bindgen]
pub fn gaoya_compute_hash(config: &GaoyaHashingConfig, license: String) -> String {
    let gaoya = GaoyaDetection {
        index: MinHashIndex::new(config.band_count, config.band_width, 0.5),
        min_hasher: MinHasher32::new(config.band_count * config.band_width),
        shingle_text_size: config.shingle_size,
    };

    format!("[{}]", gaoya.hash_from_inline_string(license).iter()
        .map(|n| n.to_string()).collect::<Vec<String>>().join(","))
}

#[wasm_bindgen]
pub fn gaoya_detect_license(config: &GaoyaHashingConfig, license: String) -> String {
    let mut gaoya = GaoyaDetection {
        index: MinHashIndex::new(config.band_count, config.band_width, 0.5),
        min_hasher: MinHasher32::new(config.band_count * config.band_width),
        shingle_text_size: config.shingle_size,
    };

    gaoya.load_from_inline_string(config.licenses_json.clone());

    gaoya.match_by_plain_text(license).iter()
        .map(|m| format!("{}: {}", m.name, m.confidence))
        .collect::<Vec<String>>().join(";")
}
