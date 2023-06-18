/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/license-detection-backends.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.panama;

import com.whichlicense.foreign.ForeignRuntimeHelper;
import com.whichlicense.metadata.identification.license.LicenseIdentifier;
import com.whichlicense.metadata.identification.license.LicenseMatch;
import com.whichlicense.metadata.identification.license.LicenseNormalization;
import com.whichlicense.metadata.identification.license.jmh.JmhForcedConfiguration;
import com.whichlicense.metadata.identification.license.panama.internal.CNormalizationFn;
import com.whichlicense.metadata.identification.license.panama.internal.FuzzyHashingConfig;
import com.whichlicense.metadata.identification.license.panama.internal.RuntimeHelper;
import com.whichlicense.metadata.identification.license.panama.internal.lib_identification_h;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Map;
import java.util.Set;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.FUZZY;
import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.FuzzyAlgorithmArguments.EXIT_ON_EXACT_MATCH;
import static com.whichlicense.metadata.identification.license.panama.PanamaFuzzyLicenseIdentifier.IndexHolder.*;
import static com.whichlicense.metadata.identification.license.panama.internal.HashingAlgorithmConfiguration.configureFuzzy;
import static java.lang.foreign.MemorySegment.NULL;

public final class PanamaFuzzyLicenseIdentifier implements LicenseIdentifier, JmhForcedConfiguration {
    @Override
    public Set<LicenseMatch> identifyLicenses(String license, LicenseNormalization normalization) {
        return identifyLicenses(license, normalization, configureFuzzy(true));
    }

    @Override
    public Set<LicenseMatch> identifyLicenses(String license, LicenseNormalization normalization, Map<String, Object> parameters) {
        try (var arena = Arena.openConfined()) {
            var fuzzyConfig = FuzzyHashingConfig.allocate(arena);
            var licenseRef = arena.allocateUtf8String(license.strip());

            FuzzyHashingConfig.index$set(fuzzyConfig, FUZZY_INDEX);

            var exitOnExactMatch = (boolean) parameters.computeIfAbsent(EXIT_ON_EXACT_MATCH, ignored -> true);
            FuzzyHashingConfig.exit_on_exact_match$set(fuzzyConfig, exitOnExactMatch);

            if (normalization != null) {
                var wrapped = RuntimeHelper.wrapNormalizationFunction(normalization, arena);
                FuzzyHashingConfig.normalization_fn$set(fuzzyConfig, CNormalizationFn.allocate(wrapped, arena.scope()));
            } else {
                FuzzyHashingConfig.normalization_fn$set(fuzzyConfig, NULL);
            }

            var raw_matches = lib_identification_h.fuzzy_detect_license(arena, fuzzyConfig, licenseRef);
            return RuntimeHelper.licenseMatchSetOfAddress(raw_matches, FUZZY, parameters, arena.scope());
        }
    }

    @Override
    public String algorithm() {
        return FUZZY;
    }

    @Override
    public void forceLoadIndex() {
        var resource = ForeignRuntimeHelper.mapResource(FUZZY_INDEX_NAME, LOADER);
        FUZZY_INDEX = lib_identification_h.construct_fuzzy_index(resource.segment(), resource.size());
    }

    static final class IndexHolder {
        static final ClassLoader LOADER = PanamaFuzzyLicenseIdentifier.class.getClassLoader();
        static final String FUZZY_INDEX_NAME = "fuzzy.index.bincode";
        static MemorySegment FUZZY_INDEX;

        static {
            var resource = ForeignRuntimeHelper.mapResource(FUZZY_INDEX_NAME, LOADER);
            FUZZY_INDEX = lib_identification_h.construct_fuzzy_index(resource.segment(), resource.size());
        }
    }
}
