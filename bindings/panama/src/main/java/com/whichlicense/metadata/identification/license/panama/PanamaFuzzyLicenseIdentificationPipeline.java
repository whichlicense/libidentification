/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.panama;

import com.whichlicense.metadata.identification.license.LicenseIdentificationPipeline;
import com.whichlicense.metadata.identification.license.LicenseIdentificationPipelineTrace;
import com.whichlicense.metadata.identification.license.panama.internal.FuzzyHashingConfig;
import com.whichlicense.metadata.identification.license.panama.internal.PipelineConfig;
import com.whichlicense.metadata.identification.license.panama.internal.RuntimeHelper;
import com.whichlicense.metadata.identification.license.panama.internal.lib_identification_h;
import com.whichlicense.metadata.identification.license.pipeline.PipelineStep;

import java.lang.foreign.Arena;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.FUZZY;
import static com.whichlicense.metadata.identification.license.panama.PanamaFuzzyLicenseIdentifier.IndexHolder.FUZZY_INDEX;
import static com.whichlicense.metadata.identification.license.panama.internal.PipelineHelper.wrapStep;
import static java.lang.foreign.MemorySegment.NULL;
import static java.util.Collections.emptySet;
import static java.util.Map.Entry.comparingByValue;

public class PanamaFuzzyLicenseIdentificationPipeline implements LicenseIdentificationPipeline {
    @Override
    public LicenseIdentificationPipelineTrace identifyLicenses(String name, List<PipelineStep> steps, float threshold, String license) {
        try (var arena = Arena.openConfined()) {
            var params = new HashMap<String, Object>();
            var fuzzyConfig = FuzzyHashingConfig.allocate(arena);
            var pipelineConfig = PipelineConfig.allocate(arena);
            var licenseRef = arena.allocateUtf8String(license.strip());

            FuzzyHashingConfig.index$set(fuzzyConfig, FUZZY_INDEX);
            FuzzyHashingConfig.exit_on_exact_match$set(fuzzyConfig, true);
            FuzzyHashingConfig.normalization_fn$set(fuzzyConfig, NULL);

            var stepPointers = RuntimeHelper.allocatePointerArray(arena, steps.stream()
                    .map(s -> wrapStep(arena, s)).iterator(), steps.size());

            PipelineConfig.steps$set(pipelineConfig, stepPointers);
            PipelineConfig.length$set(pipelineConfig, steps.size());
            PipelineConfig.threshold$set(pipelineConfig, threshold);

            var raw_matches = lib_identification_h.fuzzy_pipeline_detect_license(arena, fuzzyConfig, pipelineConfig, licenseRef);
            var traces = RuntimeHelper.licenseIdentificationPipelineStepTraceSetOfAddress(raw_matches, FUZZY, threshold, params, steps, arena.scope());

            var licenseName = traces.stream()
                    .dropWhile(t -> !t.terminated())
                    .findFirst()
                    .map(t -> t.matches().entrySet())
                    .orElse(emptySet())
                    .stream()
                    .max(comparingByValue())
                    .map(Entry::getKey)
                    .orElse(null);

            var confidence = traces.stream()
                    .dropWhile(t -> !t.terminated())
                    .findFirst()
                    .map(t -> t.matches().entrySet())
                    .orElse(emptySet())
                    .stream()
                    .max(comparingByValue())
                    .map(Entry::getValue)
                    .orElse(0f);

            return new LicenseIdentificationPipelineTrace(name, licenseName, confidence, FUZZY, params, traces, license);
        }
    }

    @Override
    public String algorithm() {
        return FUZZY;
    }
}
