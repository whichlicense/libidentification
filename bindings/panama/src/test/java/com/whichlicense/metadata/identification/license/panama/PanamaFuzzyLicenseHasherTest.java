/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/license-detection-backends.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.panama;

import com.whichlicense.metadata.identification.license.LicenseHasher;
import com.whichlicense.testing.filecontent.FileContentSource;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.Random.class)
class PanamaFuzzyLicenseHasherTest {
    final LicenseHasher hasher = new PanamaFuzzyLicenseHasher();

    @ParameterizedTest
    @FileContentSource(path = "/mit.utf8.license")
    void givenUTF8MITLicenseWhenCallingFuzzyHashThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content)).isEqualTo("12:+1cJZPKUx1+hWD+EW5U7N+VGeH2paJriQYOrw6tiryLSKOVmSaOGOxM5BiFi:+qPPK21+hWDm5DGeGvyLS6yGMM5BV");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.utf8.license")
    void givenUTF8MITLicenseWhenCallingFuzzyHashWithAsciiNormalizationThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content, AsciiHelper::stripNonAscii)).isEqualTo("12:+1cJZPKUx1+hWD+EW5U7N+VGeH2paJriQYOrw6tiryLSKOVmSaOGOxM5BiFi:+qPPK21+hWDm5DGeGvyLS6yGMM5BV");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.ascii.license")
    void givenASCIIMITLicenseWhenCallingFuzzyHashThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content)).isEqualTo("12:+1cJZPKUx1+hWD+EW5U7N+VGeH2paJriQYOrw6tiryLSKOVmSaOGOxM5BiFi:+qPPK21+hWDm5DGeGvyLS6yGMM5BV");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.ascii.license")
    void givenASCIIMITLicenseWhenCallingFuzzyHashWithAsciiNormalizationThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content, AsciiHelper::stripNonAscii)).isEqualTo("12:+1cJZPKUx1+hWD+EW5U7N+VGeH2paJriQYOrw6tiryLSKOVmSaOGOxM5BiFi:+qPPK21+hWDm5DGeGvyLS6yGMM5BV");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.stripped.license")
    void givenStrippedMITLicenseWhenCallingFuzzyHashThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content)).isEqualTo("12:vcJZPKUx1+hWD+EW5U7N+VGeH2paJriQYOrw6tiryLSKOVmSaOGOxM5BiFi:0PPK21+hWDm5DGeGvyLS6yGMM5BV");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.stripped.license")
    void givenStrippedMITLicenseWhenCallingFuzzyHashWithAsciiNormalizationThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content, AsciiHelper::stripNonAscii)).isEqualTo("12:vcJZPKUx1+hWD+EW5U7N+VGeH2paJriQYOrw6tiryLSKOVmSaOGOxM5BiFi:0PPK21+hWDm5DGeGvyLS6yGMM5BV");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.normalized.license")
    void givenNormalizedMITLicenseWhenCallingFuzzyHashThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content)).isEqualTo("12:vcJZPKUx1+hWD+EW5U7N+VGeH2paJriQYOrw6tiryLSKOVmSaOGOxM5BiFi:0PPK21+hWDm5DGeGvyLS6yGMM5BV");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.normalized.license")
    void givenNormalizedMITLicenseWhenCallingFuzzyHashWithAsciiNormalizationThenReturnFuzzyMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content, AsciiHelper::stripNonAscii)).isEqualTo("12:vcJZPKUx1+hWD+EW5U7N+VGeH2paJriQYOrw6tiryLSKOVmSaOGOxM5BiFi:0PPK21+hWDm5DGeGvyLS6yGMM5BV");
    }
}
