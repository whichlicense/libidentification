/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.panama;

import com.whichlicense.metadata.identification.license.LicenseIdentificationPipeline;
import com.whichlicense.metadata.identification.license.LicenseIdentificationPipelineTrace;
import com.whichlicense.metadata.identification.license.panama.internal.GaoyaHashingConfig;
import com.whichlicense.metadata.identification.license.panama.internal.PipelineConfig;
import com.whichlicense.metadata.identification.license.panama.internal.RuntimeHelper;
import com.whichlicense.metadata.identification.license.panama.internal.lib_identification_h;
import com.whichlicense.metadata.identification.license.pipeline.PipelineStep;

import java.lang.foreign.Arena;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.GAOYA;
import static com.whichlicense.metadata.identification.license.panama.PanamaGaoyaLicenseIdentifier.IndexHolder.GAOYA_INDEX;
import static com.whichlicense.metadata.identification.license.panama.internal.PipelineHelper.wrapStep;
import static java.util.Collections.emptySet;
import static java.util.Map.Entry.comparingByValue;

public class PanamaGaoyaLicenseIdentificationPipeline implements LicenseIdentificationPipeline {
    @Override
    public LicenseIdentificationPipelineTrace identifyLicenses(String name, List<PipelineStep> steps, float threshold, String license) {
        try (var arena = Arena.openConfined()) {
            var params = new HashMap<String, Object>();
            var gaoyaConfig = GaoyaHashingConfig.allocate(arena);
            var pipelineConfig = PipelineConfig.allocate(arena);
            var licenseRef = arena.allocateUtf8String(license.strip());

            GaoyaHashingConfig.index$set(gaoyaConfig, GAOYA_INDEX);
            GaoyaHashingConfig.band_count$set(gaoyaConfig, 42L);
            GaoyaHashingConfig.band_width$set(gaoyaConfig, 3L);
            GaoyaHashingConfig.shingle_size$set(gaoyaConfig, 50L);

            IntStream.range(0, steps.size()).forEach(i ->
                    PipelineConfig.steps$set(pipelineConfig, i, wrapStep(arena, steps.get(i))));
            PipelineConfig.length$set(pipelineConfig, steps.size());
            PipelineConfig.threshold$set(pipelineConfig, threshold);

            var raw_matches = lib_identification_h.gaoya_pipeline_detect_license(arena, gaoyaConfig, pipelineConfig, licenseRef);
            var traces = RuntimeHelper.licenseIdentificationPipelineStepTraceSetOfAddress(raw_matches, GAOYA, params, steps, arena.scope());

            var licenseName = traces.stream()
                    .dropWhile(t -> !t.terminated())
                    .findFirst()
                    .map(t -> t.matches().entrySet())
                    .orElse(emptySet())
                    .stream()
                    .max(comparingByValue())
                    .map(Entry::getKey)
                    .orElse(null);

            return new LicenseIdentificationPipelineTrace(name, licenseName, threshold, GAOYA, params, traces, license);
        }
    }

    @Override
    public String algorithm() {
        return GAOYA;
    }
}
