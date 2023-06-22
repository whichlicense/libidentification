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
import com.whichlicense.metadata.identification.license.panama.internal.RuntimeHelper;
import com.whichlicense.metadata.identification.license.panama.internal.lib_identification_h;
import com.whichlicense.metadata.identification.license.pipeline.PipelineStep;

import java.lang.foreign.Arena;
import java.util.List;
import java.util.stream.IntStream;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.GAOYA;
import static com.whichlicense.metadata.identification.license.panama.internal.PipelineHelper.wrapStep;

public class PanamaGaoyaLicenseIdentificationPipeline implements LicenseIdentificationPipeline {
    @Override
    public List<LicenseIdentificationPipelineStepTrace> identifyLicenses(List<PipelineStep> steps, float threshold, String license) {
        try (var arena = Arena.openConfined()) {
            var gaoyaConfig = GaoyaHashingConfig.allocate(arena);
            var pipelineConfig = PipelineConfig.allocate(arena);
            var licenseRef = arena.allocateUtf8String(license.strip());

            IntStream.range(0, steps.size()).forEach(i ->
                    PipelineConfig.steps$set(pipelineConfig, i, wrapStep(arena, steps.get(i))));
            PipelineConfig.length$set(pipelineConfig, steps.size());
            PipelineConfig.threshold$set(pipelineConfig, threshold);

            var raw_matches = lib_identification_h.gaoya_pipeline_detect_license(arena, gaoyaConfig, pipelineConfig, licenseRef);
            return RuntimeHelper.licenseIdentificationPipelineStepTraceSetOfAddress(raw_matches, GAOYA, arena.scope());
        }
    }

    @Override
    public String algorithm() {
        return GAOYA;
    }
}
