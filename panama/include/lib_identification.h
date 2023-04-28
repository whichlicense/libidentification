//#include <stdarg.h>
#include <stdbool.h>
#include <stdint.h>
//#include <stdlib.h>

typedef struct FuzzyHashingConfig {
  const char *licenses_json;
  uint8_t confidence_threshold;
  bool exit_on_exact_match;
} FuzzyHashingConfig;

typedef struct GaoyaHashingConfig {
  const char *licenses_json;
  uintptr_t band_count;
  uintptr_t band_width;
  uintptr_t shingle_size;
} GaoyaHashingConfig;

const char *fuzzy_compute_hash(const struct FuzzyHashingConfig *config, const char *license);

const char *fuzzy_detect_license(const struct FuzzyHashingConfig *config, const char *license);

const char *gaoya_compute_hash(const struct GaoyaHashingConfig *config, const char *license);

const char *gaoya_detect_license(const struct GaoyaHashingConfig *config, const char *license);
