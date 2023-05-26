//#include <stdarg.h>
#include <stdbool.h>
#include <stdint.h>
//#include <stdlib.h>

typedef enum PipelineStepArgumentKind {
  TEXT,
  REGEX,
} PipelineStepArgumentKind;

typedef enum PipelineStepOperation {
  REMOVE,
  REPLACE,
  BATCH,
} PipelineStepOperation;

typedef const char *(*CNormalizationFn)(const char*);

typedef struct FuzzyHashingConfig {
  const uint8_t *license_index;
  uintptr_t license_index_size;
  uintptr_t max_license_count;
  uint8_t confidence_threshold;
  bool exit_on_exact_match;
  CNormalizationFn normalization_fn;
} FuzzyHashingConfig;

typedef struct LicenseMatchEntry {
  const char *name;
  float confidence;
} LicenseMatchEntry;

typedef struct LicenseMatches {
  const struct LicenseMatchEntry *matches;
  uintptr_t length;
} LicenseMatches;

typedef struct GaoyaHashingConfig {
  const uint8_t *license_index;
  uintptr_t license_index_size;
  uintptr_t max_license_count;
  uintptr_t band_count;
  uintptr_t band_width;
  uintptr_t shingle_size;
  CNormalizationFn normalization_fn;
} GaoyaHashingConfig;

typedef struct PipelineLicenseMatches {
  const struct LicenseMatches *matches;
  uintptr_t length;
} PipelineLicenseMatches;

typedef struct ReplacementPipelineStep {
  enum PipelineStepArgumentKind kind;
  const union PipelineStepArguments *arguments;
  const char *text;
} ReplacementPipelineStep;

typedef struct BatchPipelineStep {
  const struct PipelineStep *steps;
  uintptr_t size;
} BatchPipelineStep;

typedef union PipelineStepArguments {
  const char *text;
  const char *regex;
  const struct ReplacementPipelineStep *replacement;
  const struct BatchPipelineStep *batch;
} PipelineStepArguments;

typedef struct PipelineStep {
  enum PipelineStepArgumentKind kind;
  enum PipelineStepOperation operation;
  const union PipelineStepArguments *arguments;
} PipelineStep;

typedef struct PipelineConfig {
  const struct PipelineStep *steps;
  uintptr_t length;
  float threshold;
} PipelineConfig;

const char *fuzzy_compute_hash_default_normalization(const struct FuzzyHashingConfig *config,
                                                     const char *license);

const char *fuzzy_compute_hash(const struct FuzzyHashingConfig *config, const char *license);

struct LicenseMatches fuzzy_detect_license_default_normalization(const struct FuzzyHashingConfig *config,
                                                                 const char *license);

struct LicenseMatches fuzzy_detect_license(const struct FuzzyHashingConfig *config,
                                           const char *license);

const char *gaoya_compute_hash_default_normalization(const struct GaoyaHashingConfig *config,
                                                     const char *license);

const char *gaoya_compute_hash(const struct GaoyaHashingConfig *config, const char *license);

struct LicenseMatches gaoya_detect_license_default_normalization(const struct GaoyaHashingConfig *config,
                                                                 const char *license);

struct LicenseMatches gaoya_detect_license(const struct GaoyaHashingConfig *config,
                                           const char *license);

struct PipelineLicenseMatches fuzzy_pipeline_detect_license_default_normalization(const struct FuzzyHashingConfig *config,
                                                                                  const struct PipelineConfig *pipeline,
                                                                                  const char *license);

struct PipelineLicenseMatches fuzzy_pipeline_detect_license(const struct FuzzyHashingConfig *config,
                                                            const struct PipelineConfig *pipeline,
                                                            const char *license);

struct PipelineLicenseMatches gaoya_pipeline_detect_license_default_normalization(const struct GaoyaHashingConfig *config,
                                                                                  const struct PipelineConfig *pipeline,
                                                                                  const char *license);

struct PipelineLicenseMatches gaoya_pipeline_detect_license_default(const struct GaoyaHashingConfig *config,
                                                                    const struct PipelineConfig *pipeline,
                                                                    const char *license);
