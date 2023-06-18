#include <stdarg.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>

typedef void *LicenseIndex;

typedef const char *(*CNormalizationFn)(const char*);

typedef struct FuzzyHashingConfig {
  LicenseIndex index;
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
  LicenseIndex index;
  uintptr_t band_count;
  uintptr_t band_width;
  uintptr_t shingle_size;
  CNormalizationFn normalization_fn;
} GaoyaHashingConfig;

typedef struct PipelineLicenseMatches {
  const struct LicenseMatches *step_matches;
  uintptr_t length;
} PipelineLicenseMatches;

typedef struct PipelineConfig {
  void *steps;
  uintptr_t length;
  float threshold;
} PipelineConfig;

LicenseIndex construct_fuzzy_index(const uint8_t *entries, uintptr_t size);

const char *fuzzy_compute_hash(const struct FuzzyHashingConfig *config, const char *license);

struct LicenseMatches fuzzy_detect_license(const struct FuzzyHashingConfig *config,
                                           const char *license);

LicenseIndex construct_gaoya_index(const uint8_t *entries, uintptr_t size);

const char *gaoya_compute_hash(const struct GaoyaHashingConfig *config, const char *license);

struct LicenseMatches gaoya_detect_license(const struct GaoyaHashingConfig *config,
                                           const char *license);

void *pipeline_remove_text_step(const char *str);

void *pipeline_remove_regex_step(const char *pattern);

void *pipeline_replace_text_step(const char *target, const char *replacement);

void *pipeline_replace_regex_step(const char *pattern, const char *replacement);

struct PipelineLicenseMatches fuzzy_pipeline_detect_license(const struct FuzzyHashingConfig *config,
                                                            const struct PipelineConfig *pipeline,
                                                            const char *license);

struct PipelineLicenseMatches gaoya_pipeline_detect_license(const struct GaoyaHashingConfig *config,
                                                            const struct PipelineConfig *pipeline,
                                                            const char *license);
