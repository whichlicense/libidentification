/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/license-detection-backends.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.panama;

import com.whichlicense.metadata.identification.license.LicenseIdentificationPipeline;
import com.whichlicense.metadata.identification.license.LicenseIdentificationPipelineStepTrace;
import com.whichlicense.metadata.identification.license.panama.internal.FuzzyHashingConfig;
import com.whichlicense.metadata.identification.license.panama.internal.PipelineConfig;

import java.lang.foreign.Arena;
import java.util.Collections;
import java.util.List;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.FUZZY;

public class PanamaFuzzyLicenseIdentificationPipeline implements LicenseIdentificationPipeline {
    @Override
    public List<LicenseIdentificationPipelineStepTrace> identifyLicenses(String license) {
        try (var arena = Arena.openConfined()) {
            var fuzzyConfig = FuzzyHashingConfig.allocate(arena);
            var pipelineConfig = PipelineConfig.allocate(arena);
            var licenseRef = arena.allocateUtf8String(license.strip());

            /*FuzzyHashingConfig.license_index$set(fuzzyConfig, FUZZY_INDEX);
            FuzzyHashingConfig.license_index_size$set(fuzzyConfig, FUZZY_INDEX_SIZE);
            FuzzyHashingConfig.max_license_count$set(fuzzyConfig, Long.MAX_VALUE);
            FuzzyHashingConfig.confidence_threshold$set(fuzzyConfig, (byte) 50);
            FuzzyHashingConfig.exit_on_exact_match$set(fuzzyConfig, true);

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

            var raw_matches = lib_identification_h.fuzzy_pipeline_detect_license_default_normalization(arena, fuzzyConfig, pipelineConfig, licenseRef);
            return RuntimeHelper.licenseIdentificationPipelineStepTraceSetOfAddress(raw_matches, FUZZY, arena.scope());*/
            return Collections.emptyList();
        }
    }

    @Override
    public String algorithm() {
        return FUZZY;
    }
}
