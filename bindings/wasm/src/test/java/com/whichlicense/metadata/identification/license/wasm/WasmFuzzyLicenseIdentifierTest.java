/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/license-detection-backends.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.wasm;

import com.whichlicense.metadata.identification.license.LicenseIdentifier;
import com.whichlicense.testing.filecontent.FileContentSource;
import org.junit.jupiter.params.ParameterizedTest;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.FUZZY;

import static org.assertj.core.api.Assertions.assertThat;

class WasmFuzzyLicenseIdentifierTest {
    final LicenseIdentifier identifier = new WasmFuzzyLicenseIdentifier();

    @ParameterizedTest
    @FileContentSource(path = "/mit.utf8.license")
    void givenUTF8MITLicenseWhenCallingFuzzyIdentifyThenReturnFuzzyMITLicenseMatch(String content) {
        assertThat(identifier.identifyLicense(content)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(FUZZY);
        });
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.ascii.license")
    void givenASCIIMITLicenseWhenCallingFuzzyIdentifyThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(identifier.identifyLicense(content)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(FUZZY);
        });
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.stripped.license")
    void givenStrippedMITLicenseWhenCallingFuzzyIdentifyThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(identifier.identifyLicense(content)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(FUZZY);
        });
    }

    /*@ParameterizedTest
    @FileContentSource(path = "/mit.normalized.license")
    void givenNormalizedMITLicenseWhenCallingFuzzyIdentifyThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(identifier.identifyLicense(content)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(FUZZY);
        });
    }*/
}
