/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
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
import com.whichlicense.metadata.identification.license.panama.internal.GaoyaHashingConfig;
import com.whichlicense.metadata.identification.license.panama.internal.RuntimeHelper;
import com.whichlicense.metadata.identification.license.panama.internal.lib_identification_h;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Map;
import java.util.Set;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.GAOYA;
import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.GaoyaAlgorithmArguments.*;
import static com.whichlicense.metadata.identification.license.panama.PanamaFuzzyLicenseIdentifier.IndexHolder.LOADER;
import static com.whichlicense.metadata.identification.license.panama.PanamaGaoyaLicenseIdentifier.IndexHolder.GAOYA_INDEX;
import static com.whichlicense.metadata.identification.license.panama.PanamaGaoyaLicenseIdentifier.IndexHolder.GAOYA_INDEX_NAME;
import static com.whichlicense.metadata.identification.license.panama.internal.HashingAlgorithmConfiguration.configureGaoya;

public final class PanamaGaoyaLicenseIdentifier implements LicenseIdentifier, JmhForcedConfiguration {
    @Override
    public Set<LicenseMatch> identifyLicenses(String license, LicenseNormalization normalization) {
        return identifyLicenses(license, normalization, configureGaoya(42L, 3L, 50L));
    }

    @Override
    public Set<LicenseMatch> identifyLicenses(String license, LicenseNormalization normalization, Map<String, Object> parameters) {
        try (var arena = Arena.openConfined()) {
            var gaoyaConfig = GaoyaHashingConfig.allocate(arena);
            var licenseRef = arena.allocateUtf8String(license.strip());

            GaoyaHashingConfig.index$set(gaoyaConfig, GAOYA_INDEX);

            var bandCount = (long) parameters.computeIfAbsent(BAND_COUNT, ignored -> 42L);
            GaoyaHashingConfig.band_count$set(gaoyaConfig, bandCount);

            var bandWidth = (long) parameters.computeIfAbsent(BAND_WIDTH, ignored -> 3L);
            GaoyaHashingConfig.band_width$set(gaoyaConfig, bandWidth);

            var shingleSize = (long) parameters.computeIfAbsent(SHINGLE_SIZE, ignored -> 50L);
            GaoyaHashingConfig.shingle_size$set(gaoyaConfig, shingleSize);

            if (normalization != null) {
                var wrapped = RuntimeHelper.wrapNormalizationFunction(normalization, arena);
                GaoyaHashingConfig.normalization_fn$set(gaoyaConfig, CNormalizationFn.allocate(wrapped, arena.scope()));
            }

            var raw_matches = lib_identification_h.gaoya_detect_license(arena, gaoyaConfig, licenseRef);
            return RuntimeHelper.licenseMatchSetOfAddress(raw_matches, GAOYA, parameters, arena.scope());
        }
    }

    @Override
    public String algorithm() {
        return GAOYA;
    }

    @Override
    public void forceLoadIndex() {
        var resource = ForeignRuntimeHelper.mapResource(GAOYA_INDEX_NAME, LOADER);
        GAOYA_INDEX = lib_identification_h.construct_gaoya_index(resource.segment(), resource.size());
    }

    static final class IndexHolder {
        static final ClassLoader LOADER = PanamaGaoyaLicenseIdentifier.class.getClassLoader();
        static final String GAOYA_INDEX_NAME = "gaoya.index.bincode";
        static MemorySegment GAOYA_INDEX;

        static {
            var resource = ForeignRuntimeHelper.mapResource(GAOYA_INDEX_NAME, LOADER);
            GAOYA_INDEX = lib_identification_h.construct_gaoya_index(resource.segment(), resource.size());
        }
    }
}
