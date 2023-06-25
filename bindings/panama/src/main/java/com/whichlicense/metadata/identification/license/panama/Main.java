/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.panama;

import com.whichlicense.metadata.identification.license.LicenseIdentificationPipeline;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.regex.Pattern;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.GAOYA;
import static com.whichlicense.metadata.identification.license.pipeline.PipelineStep.remove;

public class Main {
    public static void main(String[] args) {
        //var identifier = new PanamaFuzzyLicenseIdentifier();
        var pipeline = new PanamaFuzzyLicenseIdentificationPipeline();
        //var identifier = new PanamaFuzzyLicenseHasher();
        //identifier.forceLoadIndex();
        var before = Instant.now();
        var pattern = Pattern.compile("[0-9]");
        var result = pipeline.identifyLicenses("p1", List.of(remove(pattern), remove(pattern)), """
                Copyright <YEAR> <COPYRIGHT HOLDER>
                                
                Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
                                
                The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
                                
                THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
                """);
        var after = Instant.now();

        result = LicenseIdentificationPipeline.identifyLicenses("p2", GAOYA, List.of(remove(pattern), remove(pattern)), """
                Copyright <YEAR> <COPYRIGHT HOLDER>
                                
                Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
                                
                The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
                                
                THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
                """);

        System.out.println(result);
        System.out.println(Duration.between(before, after).toMillis());
    }
}
