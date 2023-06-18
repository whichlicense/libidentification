/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.panama.internal;

import java.util.HashMap;
import java.util.Map;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.FuzzyAlgorithmArguments.EXIT_ON_EXACT_MATCH;
import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.GaoyaAlgorithmArguments.*;

public final class HashingAlgorithmConfiguration {
    public static Map<String, Object> configureFuzzy(boolean exitOnExactMatch) {
        var parameters = new HashMap<String, Object>();
        parameters.put(EXIT_ON_EXACT_MATCH, exitOnExactMatch);
        return parameters;
    }

    public static Map<String, Object> configureGaoya(long bandCount, long bandWidth, long shingleSize) {
        var parameters = new HashMap<String, Object>();
        parameters.put(BAND_COUNT, bandCount);
        parameters.put(BAND_WIDTH, bandWidth);
        parameters.put(SHINGLE_SIZE, shingleSize);
        return parameters;
    }
}
