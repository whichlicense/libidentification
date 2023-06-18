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
import com.whichlicense.metadata.identification.license.panama.internal.GaoyaHashingConfig;
import com.whichlicense.metadata.identification.license.panama.internal.RuntimeHelper;
import com.whichlicense.metadata.identification.license.panama.internal.lib_identification_h;

import java.lang.foreign.Arena;
import java.util.Map;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.GAOYA;
import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.GaoyaAlgorithmArguments.*;
import static com.whichlicense.metadata.identification.license.panama.PanamaGaoyaLicenseIdentifier.IndexHolder.GAOYA_INDEX;
import static com.whichlicense.metadata.identification.license.panama.internal.HashingAlgorithmConfiguration.configureGaoya;

public final class PanamaGaoyaLicenseHasher implements LicenseHasher {
    @Override
    public String computeHash(String license, LicenseNormalization normalization) {
        return computeHash(license, normalization, configureGaoya(42L, 3L, 50L));
    }

    @Override
    public String computeHash(String license, LicenseNormalization normalization, Map<String, Object> parameters) {
        try (var arena = Arena.openConfined()) {
            var gaoyaConfig = GaoyaHashingConfig.allocate(arena);
            var licenseRef = arena.allocateUtf8String(license);

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

            return lib_identification_h.gaoya_compute_hash(gaoyaConfig, licenseRef).getUtf8String(0);
        }
    }

    @Override
    public String algorithm() {
        return GAOYA;
    }
}
