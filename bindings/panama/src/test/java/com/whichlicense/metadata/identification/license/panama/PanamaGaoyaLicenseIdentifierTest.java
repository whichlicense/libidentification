/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.panama;

import com.whichlicense.metadata.identification.license.LicenseIdentifier;
import com.whichlicense.testing.filecontent.FileContentSource;
import org.junit.jupiter.params.ParameterizedTest;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.GAOYA;
import static org.assertj.core.api.Assertions.assertThat;

class PanamaGaoyaLicenseIdentifierTest {
    final LicenseIdentifier identifier = new PanamaGaoyaLicenseIdentifier();

    @ParameterizedTest
    @FileContentSource(path = "/mit.utf8.license")
    void givenUTF8MITLicenseWhenCallingGaoyaIdentifyThenReturnGaoyaMITLicenseMatch(String content) {
        assertThat(identifier.identifyLicense(content)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(GAOYA);
        });
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.utf8.license")
    void givenUTF8MITLicenseWhenCallingGaoyaIdentifyWithAsciiNormalizationThenReturnGaoyaMITLicenseMatch(String content) {
        assertThat(identifier.identifyLicense(content, AsciiHelper::stripNonAscii)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(GAOYA);
        });
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.ascii.license")
    void givenASCIIMITLicenseWhenCallingGaoyaIdentifyThenReturnGaoyaMITLicenseMatch(String content) {
        assertThat(identifier.identifyLicense(content)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(GAOYA);
        });
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.ascii.license")
    void givenASCIIMITLicenseWhenCallingGaoyaIdentifyWithAsciiNormalizationThenReturnGaoyaMITLicenseMatch(String content) {
        assertThat(identifier.identifyLicense(content, AsciiHelper::stripNonAscii)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(GAOYA);
        });
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.stripped.license")
    void givenStrippedMITLicenseWhenCallingGaoyaIdentifyThenReturnGaoyaMITLicenseMatch(String content) {
        assertThat(identifier.identifyLicense(content)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(GAOYA);
        });
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.stripped.license")
    void givenStrippedMITLicenseWhenCallingGaoyaIdentifyWithAsciiNormalizationThenReturnGaoyaMITLicenseMatch(String content) {
        assertThat(identifier.identifyLicense(content, AsciiHelper::stripNonAscii)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(GAOYA);
        });
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.normalized.license")
    void givenNormalizedMITLicenseWhenCallingGaoyaIdentifyThenReturnGaoyaMITLicenseMatch(String content) {
        assertThat(identifier.identifyLicense(content)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(GAOYA);
        });
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.normalized.license")
    void givenNormalizedMITLicenseWhenCallingGaoyaIdentifyWithAsciiNormalizationThenReturnGaoyaMITLicenseMatch(String content) {
        assertThat(identifier.identifyLicense(content, AsciiHelper::stripNonAscii)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(GAOYA);
        });
    }
}
