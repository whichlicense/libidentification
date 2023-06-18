/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/license-detection-backends.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.wasm;

import com.whichlicense.metadata.identification.license.LicenseHasher;
import com.whichlicense.testing.filecontent.FileContentSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class WasmFuzzyLicenseHasherTest {
    final LicenseHasher hasher = new WasmFuzzyLicenseHasher();

    @ParameterizedTest
    @FileContentSource(path = "/mit.utf8.license")
    void givenUTF8MITLicenseWhenCallingFuzzyIdentifyThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content)).isEqualTo("12:z2crhKUGsvT+hItU7N+rP2paJR2NtiYQzyLAKOVmSaARYs3p6NBiFo:xNKmvT+hEVe3MyLA6ERYG6NBh");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.ascii.license")
    void givenASCIIMITLicenseWhenCallingFuzzyIdentifyThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content)).isEqualTo("12:z2crhKUEJT+hItU7N+rP2paJRMgNtiYQzyLAKOVmSaARYs3p6NBiFo:xNKfJT+hEVepUMyLA6ERYG6NBh");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.stripped.license")
    void givenStrippedMITLicenseWhenCallingFuzzyIdentifyThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content)).isEqualTo("12:vcrhKUEJT+hItU7N+rP2paJRMgNtiYQzyLAKOVmSaARYs3p6NBiFo:0NKfJT+hEVepUMyLA6ERYG6NBh");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.normalized.license")
    void givenNormalizedMITLicenseWhenCallingFuzzyIdentifyThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content)).isEqualTo("12:vcJZPKUx1+hWD+EW5U7N+VGeH2paJriQYOrw6tiryLSKOVmSaOGOxM5BiFi:0PPK21+hWDm5DGeGvyLS6yGMM5BV");
    }
}
