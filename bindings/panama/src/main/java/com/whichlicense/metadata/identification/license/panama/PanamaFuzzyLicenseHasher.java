/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.panama;

import com.whichlicense.metadata.identification.license.LicenseHasher;
import com.whichlicense.metadata.identification.license.LicenseNormalization;
import com.whichlicense.metadata.identification.license.panama.internal.CNormalizationFn;
import com.whichlicense.metadata.identification.license.panama.internal.FuzzyHashingConfig;
import com.whichlicense.metadata.identification.license.panama.internal.RuntimeHelper;
import com.whichlicense.metadata.identification.license.panama.internal.lib_identification_h;

import java.lang.foreign.Arena;
import java.util.Map;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.FUZZY;
import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.FuzzyAlgorithmArguments.EXIT_ON_EXACT_MATCH;
import static com.whichlicense.metadata.identification.license.panama.PanamaFuzzyLicenseIdentifier.IndexHolder.FUZZY_INDEX;
import static com.whichlicense.metadata.identification.license.panama.internal.HashingAlgorithmConfiguration.configureFuzzy;
import static java.lang.foreign.MemorySegment.NULL;

public final class PanamaFuzzyLicenseHasher implements LicenseHasher {
    @Override
    public String computeHash(String license, LicenseNormalization normalization) {
        return computeHash(license, normalization, configureFuzzy(true));
    }

    @Override
    public String computeHash(String license, LicenseNormalization normalization, Map<String, Object> parameters) {
        try (var arena = Arena.openConfined()) {
            var fuzzyConfig = FuzzyHashingConfig.allocate(arena);
            var licenseRef = arena.allocateUtf8String(license);

            FuzzyHashingConfig.index$set(fuzzyConfig, FUZZY_INDEX);

            var exitOnExactMatch = (boolean) parameters.computeIfAbsent(EXIT_ON_EXACT_MATCH, ignored -> true);
            FuzzyHashingConfig.exit_on_exact_match$set(fuzzyConfig, exitOnExactMatch);

            if (normalization != null) {
                var wrapped = RuntimeHelper.wrapNormalizationFunction(normalization, arena);
                FuzzyHashingConfig.normalization_fn$set(fuzzyConfig, CNormalizationFn.allocate(wrapped, arena.scope()));
            } else {
                FuzzyHashingConfig.normalization_fn$set(fuzzyConfig, NULL);
            }

            return lib_identification_h.fuzzy_compute_hash(fuzzyConfig, licenseRef).getUtf8String(0);
        }
    }

    @Override
    public String algorithm() {
        return FUZZY;
    }
}
