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
use std::ptr;
use std::slice;
use std::string::ToString;

use gaoya::minhash::{MinHasher32, MinHashIndex};
use regex::Regex;
use serde::ser::Serialize;
use whichlicense_detection::{DEFAULT_NORMALIZATION_FN, LicenseListActions, Pipeline, Segment, Using};
use whichlicense_detection::detecting::fuzzy_implementation::fuzzy_implementation::FuzzyDetection;
use whichlicense_detection::detecting::gaoya_implementation::gaoya_implementation::GaoyaDetection;

type CNormalizationFn = extern "C" fn(*const c_char) -> *const c_char;

static SKIP_NORMALIZATION_FN: fn(&str) -> String = |l| l.to_string(); //TODO explain reason

#[inline(always)]
fn rustic_normalize(normalization_fn: CNormalizationFn, license: &str) -> &str {
    let c_license = CString::new(license).expect("failed to convert the license into a CString");
    let c_result = (normalization_fn)(c_license.into_raw());
    unsafe { CStr::from_ptr(c_result) }.to_str().expect("failed to obtain the normalized license text")
}

#[inline(always)]
fn rustic_string(ptr: *const c_char, msg: &str) -> &str {
    unsafe { CStr::from_ptr(ptr) }.to_str().expect(msg)
}

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

#[inline(always)]
fn c_string(str: String, msg: &str) -> *const c_char {
    CString::new(str).expect(msg).into_raw()
}

#[inline(always)]
fn c_box<T>(rustic_box: Box<[T]>) -> *const T {
    if rustic_box.is_empty() { return ptr::null(); }
    Box::into_raw(rustic_box) as *const T
}

#[inline(always)]
fn rustic_vec<T>(ptr: *const T, size: usize) -> Vec<T> where T: Clone {
    if size == 0 { return Vec::new(); }
    unsafe { slice::from_raw_parts(ptr, size) }.to_vec()
}

#[repr(C)]
pub struct FuzzyHashingConfig {
    license_index: *const u8,
    license_index_size: usize,
    max_license_count: usize,
    confidence_threshold: u8,
    exit_on_exact_match: bool,
    normalization_fn: CNormalizationFn,
}

#[inline(always)]
fn configure_fuzzy_detection(config: &FuzzyHashingConfig, normalization_fn: fn(&str) -> String) -> FuzzyDetection {
    FuzzyDetection {
        licenses: vec![],
        min_confidence: config.confidence_threshold,
        exit_on_exact_match: config.exit_on_exact_match,
        normalization_fn,
    }
}

#[no_mangle]
pub extern "C" fn fuzzy_compute_hash_default_normalization<'jvm>(config: &'jvm FuzzyHashingConfig, license: &'jvm c_char) -> *const c_char {
    let fuzzy = configure_fuzzy_detection(config, DEFAULT_NORMALIZATION_FN);

    let raw_license = rustic_string(license, "failed to obtain the license text");
    let hash = fuzzy.hash_from_inline_string(raw_license);

    c_string(hash, "failed to convert the hash into a CString")
}

#[no_mangle]
pub extern "C" fn fuzzy_compute_hash<'jvm>(config: &'jvm FuzzyHashingConfig, license: &'jvm c_char) -> *const c_char {
    let fuzzy = configure_fuzzy_detection(config, SKIP_NORMALIZATION_FN);

    let raw_license = rustic_string(license, "failed to obtain the license text");
    let hash = fuzzy.hash_from_inline_string(rustic_normalize(config.normalization_fn, raw_license));

    c_string(hash, "failed to convert the hash into a CString")
}

#[no_mangle]
pub extern "C" fn fuzzy_detect_license_default_normalization<'jvm>(config: &'jvm FuzzyHashingConfig, license: &'jvm c_char) -> LicenseMatches {
    let mut fuzzy = configure_fuzzy_detection(config, DEFAULT_NORMALIZATION_FN);
    fuzzy.load_from_memory(rustic_vec(config.license_index, config.license_index_size));

    let raw_license = rustic_string(license, "failed to obtain the license text");
    let matches = fuzzy.match_by_plain_text(raw_license).iter().map(|m| {
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
pub extern "C" fn fuzzy_detect_license<'jvm>(config: &'jvm FuzzyHashingConfig, license: &'jvm c_char) -> LicenseMatches {
    let mut fuzzy = configure_fuzzy_detection(config, SKIP_NORMALIZATION_FN);
    fuzzy.load_from_memory(rustic_vec(config.license_index, config.license_index_size));

    let raw_license = rustic_string(license, "failed to obtain the license text");
    let normalized_license = rustic_normalize(config.normalization_fn, raw_license);
    let matches = fuzzy.match_by_plain_text(normalized_license).iter().map(|m| {
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

#[repr(C)]
pub struct GaoyaHashingConfig {
    license_index: *const u8,
    license_index_size: usize,
    max_license_count: usize,
    band_count: usize,
    band_width: usize,
    shingle_size: usize,
    normalization_fn: CNormalizationFn,
}

#[inline(always)]
fn configure_gaoya_detection(config: &GaoyaHashingConfig, normalization_fn: fn(&str) -> String) -> GaoyaDetection {
    GaoyaDetection {
        index: MinHashIndex::new(config.band_count, config.band_width, 0.5),
        min_hasher: MinHasher32::new(config.band_count * config.band_width),
        shingle_text_size: config.shingle_size,
        normalization_fn,
    }
}

#[no_mangle]
pub extern "C" fn gaoya_compute_hash_default_normalization<'jvm>(config: &'jvm GaoyaHashingConfig, license: &'jvm c_char) -> *const c_char {
    let gaoya = configure_gaoya_detection(config, DEFAULT_NORMALIZATION_FN);

    let raw_license = rustic_string(license, "failed to obtain the license text");
    let hash = gaoya.hash_from_inline_string(raw_license).iter()
        .map(|n| n.to_string()).collect::<Vec<String>>().join(",");

    c_string(format!("[{}]", hash), "failed to convert the hash into a CString")
}

#[no_mangle]
pub extern "C" fn gaoya_compute_hash<'jvm>(config: &'jvm GaoyaHashingConfig, license: &'jvm c_char) -> *const c_char {
    let gaoya = configure_gaoya_detection(config, SKIP_NORMALIZATION_FN);

    let raw_license = rustic_string(license, "failed to obtain the license text");
    let hash = gaoya.hash_from_inline_string(rustic_normalize(config.normalization_fn, raw_license)).iter()
        .map(|n| n.to_string()).collect::<Vec<String>>().join(",");

    c_string(format!("[{}]", hash), "failed to convert the hash into a CString")
}

#[no_mangle]
pub extern "C" fn gaoya_detect_license_default_normalization<'jvm>(config: &'jvm GaoyaHashingConfig, license: &'jvm c_char) -> LicenseMatches {
    let mut gaoya = configure_gaoya_detection(config, DEFAULT_NORMALIZATION_FN);
    gaoya.load_from_memory(rustic_vec(config.license_index, config.license_index_size));

    let raw_license = rustic_string(license, "failed to obtain the license text");
    let matches = gaoya.match_by_plain_text(raw_license).iter().map(|m| {
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
pub extern "C" fn gaoya_detect_license<'jvm>(config: &'jvm GaoyaHashingConfig, license: &'jvm c_char) -> LicenseMatches {
    let mut gaoya = configure_gaoya_detection(config, SKIP_NORMALIZATION_FN);
    gaoya.load_from_memory(rustic_vec(config.license_index, config.license_index_size));

    let raw_license = rustic_string(license, "failed to obtain the license text");
    let normalized_license = rustic_normalize(config.normalization_fn, raw_license);
    let matches = gaoya.match_by_plain_text(normalized_license).iter().map(|m| {
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

#[repr(C)]
pub struct PipelineLicenseMatches {
    pub matches: *const LicenseMatches,
    pub length: usize,
}

#[repr(C)]
#[derive(Clone)]
pub enum PipelineStepOperation { REMOVE, REPLACE, BATCH }

#[repr(C)]
#[derive(Clone)]
pub enum PipelineStepArgumentKind { TEXT, REGEX }

#[repr(C)]
pub union PipelineStepArguments {
    text: *const c_char,
    regex: *const c_char,
    replacement: *const ReplacementPipelineStep,
    batch: *const BatchPipelineStep,
}

#[repr(C)]
pub struct ReplacementPipelineStep {
    kind: PipelineStepArgumentKind,
    arguments: *const PipelineStepArguments,
    text: *const c_char,
}

#[repr(C)]
pub struct BatchPipelineStep {
    steps: *const PipelineStep,
    size: usize,
}

#[repr(C)]
#[derive(Clone)]
pub struct PipelineStep {
    kind: PipelineStepArgumentKind,
    operation: PipelineStepOperation,
    arguments: *const PipelineStepArguments,
}

#[repr(C)]
pub struct PipelineConfig {
    steps: *const PipelineStep,
    length: usize,
    threshold: f32,
}

#[inline(always)]
unsafe fn rustic_step_operation(kind: &PipelineStepArgumentKind, arguments: &PipelineStepArguments) -> Using {
    match kind {
        PipelineStepArgumentKind::TEXT => Using::Text(rustic_string(arguments.text, "failed to obtain text").to_string()),
        PipelineStepArgumentKind::REGEX => Using::Regex(Regex::new(rustic_string(arguments.regex, "failed to obtain the regex"))
            .expect("failed to create regex pattern from c string"))
    }
}

#[inline(always)]
unsafe fn rustic_replacement_step(step: &ReplacementPipelineStep) -> Segment {
    let args = step.arguments.as_ref().expect("failed to obtain operation arguments");
    Segment::Replace(rustic_step_operation(&args.replacement
        .as_ref().expect("failed to obtain operation replacement").kind, args),
                     rustic_string(args.replacement.as_ref().expect("failed to obtain operation arguments")
                                       .text, "failed to obtain replacement text").to_string())
}

#[inline(always)]
unsafe fn rustic_step(step: &PipelineStep) -> Segment {
    let args = step.arguments.as_ref().expect("failed to obtain operation arguments");
    match step.operation {
        PipelineStepOperation::REMOVE => Segment::Remove(rustic_step_operation(&step.kind, args)),
        PipelineStepOperation::REPLACE => rustic_replacement_step(&args.replacement
            .as_ref().expect("failed to obtain operation replacement arguments")),
        PipelineStepOperation::BATCH => {
            let batch = args.batch.as_ref().expect("failed to obtain batch");
            Segment::Batch(rustic_vec(batch.steps, batch.size).iter()
                .map(|s| unsafe { rustic_step(s) }).collect::<Vec<Segment>>())
        }
    }
}

#[inline(always)]
fn rustic_pipeline_detect_license<'jvm, T: Serialize>(algorithm: &dyn LicenseListActions<T>, pipeline: &'jvm PipelineConfig, license: &str) -> PipelineLicenseMatches {
    let steps = rustic_vec(pipeline.steps, pipeline.length).iter()
        .map(|s| unsafe { rustic_step(s) }).collect::<Vec<Segment>>();
    let res = Pipeline::new(steps).run(algorithm, license, pipeline.threshold);

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

    PipelineLicenseMatches {
        length: matches.len(),
        matches: c_box(matches),
    }
}

#[no_mangle]
pub extern "C" fn fuzzy_pipeline_detect_license_default_normalization<'jvm>(config: &'jvm FuzzyHashingConfig, pipeline: &'jvm PipelineConfig, license: &'jvm c_char) -> PipelineLicenseMatches {
    let mut fuzzy = configure_fuzzy_detection(config, DEFAULT_NORMALIZATION_FN);
    fuzzy.load_from_memory(rustic_vec(config.license_index, config.license_index_size));
    let raw_license = rustic_string(license, "failed to obtain the license text");
    rustic_pipeline_detect_license(&fuzzy, pipeline, raw_license)
}

//TODO fix normalization
#[no_mangle]
pub extern "C" fn fuzzy_pipeline_detect_license<'jvm>(config: &'jvm FuzzyHashingConfig, pipeline: &'jvm PipelineConfig, license: &'jvm c_char) -> PipelineLicenseMatches {
    let mut fuzzy = configure_fuzzy_detection(config, SKIP_NORMALIZATION_FN);
    fuzzy.load_from_memory(rustic_vec(config.license_index, config.license_index_size));
    let raw_license = rustic_string(license, "failed to obtain the license text");
    let normalized_license = rustic_normalize(config.normalization_fn, raw_license);
    rustic_pipeline_detect_license(&fuzzy, pipeline, normalized_license)
}

#[no_mangle]
pub extern "C" fn gaoya_pipeline_detect_license_default_normalization<'jvm>(config: &'jvm GaoyaHashingConfig, pipeline: &'jvm PipelineConfig, license: &'jvm c_char) -> PipelineLicenseMatches {
    let mut gaoya = configure_gaoya_detection(config, DEFAULT_NORMALIZATION_FN);
    gaoya.load_from_memory(rustic_vec(config.license_index, config.license_index_size));
    let raw_license = rustic_string(license, "failed to obtain the license text");
    rustic_pipeline_detect_license(&gaoya, pipeline, raw_license)
}

//TODO fix normalization
#[no_mangle]
pub extern "C" fn gaoya_pipeline_detect_license_default<'jvm>(config: &'jvm GaoyaHashingConfig, pipeline: &'jvm PipelineConfig, license: &'jvm c_char) -> PipelineLicenseMatches {
    let mut gaoya = configure_gaoya_detection(config, SKIP_NORMALIZATION_FN);
    gaoya.load_from_memory(rustic_vec(config.license_index, config.license_index_size));
    let raw_license = rustic_string(license, "failed to obtain the license text");
    let normalized_license = rustic_normalize(config.normalization_fn, raw_license);
    rustic_pipeline_detect_license(&gaoya, pipeline, normalized_license)
}
