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

use std::ffi::c_void;
use std::os::raw::c_char;
use regex::Regex;
use std::string::ToString;

use gaoya::minhash::{MinHasher32, MinHashIndex};
use rayon::iter::{IntoParallelRefIterator, ParallelIterator};
use serde::ser::Serialize;
use whichlicense_detection::{DEFAULT_NORMALIZATION_FN, DiskData, LicenseEntry, LicenseListActions, Pipeline, Segment, Using};
use whichlicense_detection::detecting::fuzzy_implementation::fuzzy_implementation::FuzzyDetection;
use whichlicense_detection::detecting::gaoya_implementation::gaoya_implementation::GaoyaDetection;

use crate::rustic::ffm::{c_box, c_string, rustic_normalize, rustic_string, rustic_vec, unsafe_rustic_vec};
use crate::rustic::pipeline::{PipelineConfig, PipelineLicenseMatches, segment_to_raw_ptr};
use crate::rustic::repr::{FuzzyHashingConfig, LicenseIndex, GaoyaHashingConfig, LicenseMatchEntry, LicenseMatches};

mod rustic;

static SKIP_NORMALIZATION_FN: fn(&str) -> String = |l| l.to_string();

#[no_mangle]
pub extern "C" fn construct_fuzzy_index<'jvm>(entries: *const u8, size: usize) -> LicenseIndex {
    let mut fuzzy = FuzzyDetection {
        licenses: vec![],
        min_confidence: 50,
        exit_on_exact_match: false,
        normalization_fn: SKIP_NORMALIZATION_FN,
    };
    fuzzy.load_from_memory(&rustic_vec(entries, size));
    return Box::into_raw(Box::new(fuzzy.licenses)) as *mut c_void
}

#[inline(always)]
fn configure_fuzzy_detection(config: &FuzzyHashingConfig, normalization_fn: fn(&str) -> String) -> FuzzyDetection {
    FuzzyDetection {
        licenses: unsafe { &(*(config.index as *const Vec<LicenseEntry<String>>)) }.clone(),
        min_confidence: 50,
        exit_on_exact_match: config.exit_on_exact_match,
        normalization_fn,
    }
}

#[no_mangle]
pub extern "C" fn fuzzy_compute_hash<'jvm>(config: &'jvm FuzzyHashingConfig, license: &'jvm c_char) -> *const c_char {
    let rustic_license = rustic_string(license, "failed to obtain the license text");

    let (license, normalization_fn) = match config.normalization_fn {
        None => (rustic_license, DEFAULT_NORMALIZATION_FN),
        _ => (rustic_normalize(config.normalization_fn, rustic_license), SKIP_NORMALIZATION_FN)
    };

    let fuzzy = configure_fuzzy_detection(config, normalization_fn);
    let hash = fuzzy.hash_from_inline_string(license);

    c_string(hash, "failed to convert the hash into a CString")
}

#[no_mangle]
pub extern "C" fn fuzzy_detect_license<'jvm>(config: &'jvm FuzzyHashingConfig, license: &'jvm c_char) -> LicenseMatches {
    let rustic_license = rustic_string(license, "failed to obtain the license text");

    let (license, normalization_fn) = match config.normalization_fn {
        None => (rustic_license, DEFAULT_NORMALIZATION_FN),
        _ => (rustic_normalize(config.normalization_fn, rustic_license), SKIP_NORMALIZATION_FN)
    };

    let fuzzy = configure_fuzzy_detection(config, normalization_fn);

    let matches = fuzzy.match_by_plain_text(license).iter().map(|m| {
        LicenseMatchEntry {
            name: c_string(m.name.clone(), "failed to clone the license name"),
            confidence: m.confidence,
        }
    }).collect::<Box<[LicenseMatchEntry]>>();

    LicenseMatches {
        length: matches.len(),
        matches: c_box(matches),
    }
}

#[no_mangle]
pub extern "C" fn construct_gaoya_index<'jvm>(entries: *const u8, size: usize) -> LicenseIndex {
    Box::into_raw(Box::new(
        bincode::deserialize(&&rustic_vec(entries, size)[..]).unwrap_or(DiskData::<Vec<u32>> {
            licenses: Vec::new(),
        }).licenses.par_iter()
            .map(|l| (l.name.clone(), l.hash.clone()))
            .collect::<Vec<(String, Vec<u32>)>>()
    )) as *mut c_void
}

#[inline(always)]
fn configure_gaoya_detection(config: &GaoyaHashingConfig, normalization_fn: fn(&str) -> String) -> GaoyaDetection {
    let signatures = unsafe { &(*(config.index as *const Vec<(String, Vec<u32>)>)) }.clone();
    let mut idx = MinHashIndex::new_with_capacity(config.band_count, config.band_width, 0.5, signatures.len());
    idx.par_bulk_insert_pairs(signatures);

    GaoyaDetection {
        index: idx,
        min_hasher: MinHasher32::new(config.band_count * config.band_width),
        shingle_text_size: config.shingle_size,
        normalization_fn,
    }
}

#[no_mangle]
pub extern "C" fn gaoya_compute_hash<'jvm>(config: &'jvm GaoyaHashingConfig, license: &'jvm c_char) -> *const c_char {
    let rustic_license = rustic_string(license, "failed to obtain the license text");

    let (license, normalization_fn) = match config.normalization_fn {
        None => (rustic_license, DEFAULT_NORMALIZATION_FN),
        _ => (rustic_normalize(config.normalization_fn, rustic_license), SKIP_NORMALIZATION_FN)
    };

    let gaoya = configure_gaoya_detection(config, normalization_fn);
    let hash = gaoya.hash_from_inline_string(license).iter()
        .map(|n| n.to_string()).collect::<Vec<String>>().join(",");

    c_string(format!("[{}]", hash), "failed to convert the hash into a CString")
}

#[no_mangle]
pub extern "C" fn gaoya_detect_license<'jvm>(config: &'jvm GaoyaHashingConfig, license: &'jvm c_char) -> LicenseMatches {
    let rustic_license = rustic_string(license, "failed to obtain the license text");

    let (license, normalization_fn) = match config.normalization_fn {
        None => (rustic_license, DEFAULT_NORMALIZATION_FN),
        _ => (rustic_normalize(config.normalization_fn, rustic_license), SKIP_NORMALIZATION_FN)
    };

    let gaoya = configure_gaoya_detection(config, normalization_fn);

    let matches = gaoya.match_by_plain_text(license).iter().map(|m| {
        LicenseMatchEntry {
            name: c_string(m.name.clone(), "failed to clone the license name"),
            confidence: m.confidence,
        }
    }).collect::<Box<[LicenseMatchEntry]>>();

    LicenseMatches {
        length: matches.len(),
        matches: c_box(matches),
    }
}

#[no_mangle]
pub extern "C" fn pipeline_remove_text_step(str: *const c_char) -> *mut c_void {
    segment_to_raw_ptr(Segment::Remove(Using::Text(
        rustic_string(str, "failed to obtain text").to_string())))
}

#[no_mangle]
pub extern "C" fn pipeline_remove_regex_step(pattern: *const c_char) -> *mut c_void {
    segment_to_raw_ptr(Segment::Remove(Using::Regex(Regex::new(
        rustic_string(pattern, "failed to obtain the regex"))
        .expect("failed to create regex pattern from c string"))))
}

#[no_mangle]
pub extern "C" fn pipeline_replace_text_step(target: *const c_char, replacement: *const c_char) -> *mut c_void {
    segment_to_raw_ptr(Segment::Replace(Using::Text(
        rustic_string(target, "failed to obtain text").to_string()
    ), rustic_string(replacement, "failed to obtain replacement text").to_string()))
}

#[no_mangle]
pub extern "C" fn pipeline_replace_regex_step(pattern: *const c_char, replacement: *const c_char) -> *mut c_void {
    segment_to_raw_ptr(Segment::Replace(Using::Regex(Regex::new(
        rustic_string(pattern, "failed to obtain the regex"))
        .expect("failed to create regex pattern from c string")
    ), rustic_string(replacement, "failed to obtain replacement text").to_string()))
}

/*#[no_mangle]
pub extern "C" fn pipeline_custom_step() -> *mut c_void {
    Box::into_raw(Box::new("")) as *mut c_void
}*/

#[no_mangle]
pub extern "C" fn pipeline_batch_steps(steps: *const c_void, length: usize) -> *mut c_void {
    let steps = unsafe { unsafe_rustic_vec(steps as *const Segment, length) };
    segment_to_raw_ptr(Segment::Batch(steps))
}

#[inline(always)]
fn rustic_pipeline_detect_license<'jvm, T: Serialize>(algorithm: &dyn LicenseListActions<T>, pipeline: &'jvm PipelineConfig, license: &str) -> PipelineLicenseMatches {
    println!("4");
    let steps = unsafe { unsafe_rustic_vec(pipeline.steps as *mut Segment, pipeline.length) };
    println!("5");
    let res = Pipeline::new(steps).run(algorithm, license, pipeline.threshold);

    println!("6");
    let matches = res.iter().map(|ms| {
        LicenseMatches {
            length: ms.len(),
            matches: c_box(ms.iter().map(|m| {
                LicenseMatchEntry {
                    name: c_string(m.name.clone(), "failed to clone the license name"),
                    confidence: m.confidence,
                }
            }).collect::<Box<[LicenseMatchEntry]>>()),
        }
    }).collect::<Box<[LicenseMatches]>>();
    println!("7");
    PipelineLicenseMatches {
        length: matches.len(),
        step_matches: c_box(matches),
    }
}

//TODO add support for custom normalization functions
#[no_mangle]
pub extern "C" fn fuzzy_pipeline_detect_license<'jvm>(config: &'jvm FuzzyHashingConfig, pipeline: &'jvm PipelineConfig, license: &'jvm c_char) -> PipelineLicenseMatches {
    let raw_license = rustic_string(license, "failed to obtain the license text");
    let fuzzy = configure_fuzzy_detection(config, DEFAULT_NORMALIZATION_FN);
    rustic_pipeline_detect_license(&fuzzy, pipeline, raw_license)
}

//TODO add support for custom normalization functions
#[no_mangle]
pub extern "C" fn gaoya_pipeline_detect_license<'jvm>(config: &'jvm GaoyaHashingConfig, pipeline: &'jvm PipelineConfig, license: &'jvm c_char) -> PipelineLicenseMatches {
    println!("1");
    let raw_license = rustic_string(license, "failed to obtain the license text");
    println!("2");
    let gaoya = configure_gaoya_detection(config, DEFAULT_NORMALIZATION_FN);
    println!("3");
    rustic_pipeline_detect_license(&gaoya, pipeline, raw_license)
}
