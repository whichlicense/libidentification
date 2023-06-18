/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/license-detection-backends.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.panama;

import com.whichlicense.metadata.identification.license.LicenseIdentifier;
import com.whichlicense.testing.filecontent.FileContentSource;
import org.junit.jupiter.params.ParameterizedTest;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.FUZZY;
import static org.assertj.core.api.Assertions.assertThat;

class PanamaFuzzyLicenseIdentifierTest {
    final LicenseIdentifier identifier = new PanamaFuzzyLicenseIdentifier();

    @ParameterizedTest
    @FileContentSource(path = "/mit.utf8.license")
    void givenUTF8MITLicenseWhenCallingFuzzyIdentifyThenReturnFuzzyMITLicenseMatch(String content) {
        assertThat(identifier.identifyLicense(content)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(FUZZY);
        });
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.utf8.license")
    void givenUTF8MITLicenseWhenCallingFuzzyIdentifyWithAsciiNormalizationThenReturnFuzzyMITLicenseMatch(String content) {
        assertThat(identifier.identifyLicense(content, AsciiHelper::stripNonAscii)).hasValueSatisfying(match -> {
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
    @FileContentSource(path = "/mit.ascii.license")
    void givenASCIIMITLicenseWhenCallingFuzzyIdentifyWithAsciiNormalizationThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(identifier.identifyLicense(content, AsciiHelper::stripNonAscii)).hasValueSatisfying(match -> {
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

    @ParameterizedTest
    @FileContentSource(path = "/mit.stripped.license")
    void givenStrippedMITLicenseWhenCallingFuzzyIdentifyWithAsciiNormalizationThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(identifier.identifyLicense(content, AsciiHelper::stripNonAscii)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(FUZZY);
        });
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.normalized.license")
    void givenNormalizedMITLicenseWhenCallingFuzzyIdentifyThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(identifier.identifyLicense(content)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(FUZZY);
        });
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.normalized.license")
    void givenNormalizedMITLicenseWhenCallingFuzzyIdentifyWithAsciiNormalizationThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(identifier.identifyLicense(content, AsciiHelper::stripNonAscii)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(FUZZY);
        });
    }
}
