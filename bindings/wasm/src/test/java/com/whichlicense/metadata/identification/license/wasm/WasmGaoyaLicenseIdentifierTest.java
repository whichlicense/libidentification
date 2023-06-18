/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.wasm;

import com.whichlicense.metadata.identification.license.LicenseIdentifier;
import com.whichlicense.testing.filecontent.FileContentSource;
import org.junit.jupiter.params.ParameterizedTest;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.GAOYA;

import static org.assertj.core.api.Assertions.assertThat;

class WasmGaoyaLicenseIdentifierTest {
    final LicenseIdentifier identifier = new WasmGaoyaLicenseIdentifier();

    @ParameterizedTest
    @FileContentSource(path = "/mit.utf8.license")
    void givenUTF8MITLicenseWhenCallingGaoyaIdentifyThenReturnGaoyaMITLicenseMatch(String content) {
        assertThat(identifier.identifyLicense(content)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(GAOYA);
        });
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.ascii.license")
    void givenASCIIMITLicenseWhenCallingGaoyaIdentifyThenReturnGaoyaMITLicenseHash(String content) {
        assertThat(identifier.identifyLicense(content)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(GAOYA);
        });
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.stripped.license")
    void givenStrippedMITLicenseWhenCallingGaoyaIdentifyThenReturnGaoyaMITLicenseHash(String content) {
        assertThat(identifier.identifyLicense(content)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(GAOYA);
        });
    }

    /*@ParameterizedTest
    @FileContentSource(path = "/mit.normalized.license")
    void givenNormalizedMITLicenseWhenCallingGaoyaIdentifyThenReturnGaoyaMITLicenseHash(String content) {
        assertThat(identifier.identifyLicense(content)).hasValueSatisfying(match -> {
            assertThat(match.license()).isEqualTo("mit");
            assertThat(match.algorithm()).isEqualTo(GAOYA);
        });
    }*/
}
