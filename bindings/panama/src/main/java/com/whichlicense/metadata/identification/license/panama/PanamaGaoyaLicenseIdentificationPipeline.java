/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.panama;

import com.whichlicense.metadata.identification.license.LicenseIdentificationPipeline;
import com.whichlicense.metadata.identification.license.LicenseIdentificationPipelineStepTrace;
import com.whichlicense.metadata.identification.license.panama.internal.GaoyaHashingConfig;
import com.whichlicense.metadata.identification.license.panama.internal.PipelineConfig;

import java.lang.foreign.Arena;
import java.util.Collections;
import java.util.List;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.GAOYA;

public class PanamaGaoyaLicenseIdentificationPipeline implements LicenseIdentificationPipeline {
    @Override
    public List<LicenseIdentificationPipelineStepTrace> identifyLicenses(String license) {
        try (var arena = Arena.openConfined()) {
            var gaoyaConfig = GaoyaHashingConfig.allocate(arena);
            var pipelineConfig = PipelineConfig.allocate(arena);
            var licenseRef = arena.allocateUtf8String(license.strip());

            /*GaoyaHashingConfig.license_index$set(gaoyaConfig, GAOYA_INDEX);
            GaoyaHashingConfig.license_index_size$set(gaoyaConfig, GAOYA_INDEX_SIZE);
            GaoyaHashingConfig.max_license_count$set(gaoyaConfig, Long.MAX_VALUE);

            GaoyaHashingConfig.band_count$set(gaoyaConfig, 42L);
            GaoyaHashingConfig.band_width$set(gaoyaConfig, 3L);
            GaoyaHashingConfig.shingle_size$set(gaoyaConfig, 50L);

            PipelineConfig.length$set(pipelineConfig, 1L);
            PipelineConfig.threshold$set(pipelineConfig, 100);

            var pipelineStep = PipelineStep.allocate(arena);
            PipelineStep.kind$set(pipelineStep, REGEX());
            PipelineStep.operation$set(pipelineStep, REMOVE());

            var pipelineStepArguments = PipelineStepArguments.allocate(arena);
            var regex = arena.allocateUtf8String("[0-9]");
            PipelineStepArguments.regex$set(pipelineStepArguments, regex);

            PipelineStep.arguments$set(pipelineStep, pipelineStepArguments);
            PipelineConfig.steps$set(pipelineConfig, 0L, pipelineStep);

            var raw_matches = lib_identification_h.gaoya_pipeline_detect_license_default_normalization(arena, gaoyaConfig, pipelineConfig, licenseRef);
            return RuntimeHelper.licenseIdentificationPipelineStepTraceSetOfAddress(raw_matches, GAOYA, arena.scope());*/
            return Collections.emptyList();
        }
    }

    @Override
    public String algorithm() {
        return GAOYA;
    }
}
