/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.panama;

import com.whichlicense.metadata.identification.license.LicenseHasher;
import com.whichlicense.testing.filecontent.FileContentSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class PanamaGaoyaLicenseHasherTest {
    final LicenseHasher hasher = new PanamaGaoyaLicenseHasher();

    @ParameterizedTest
    @FileContentSource(path = "/mit.utf8.license")
    void givenUTF8MITLicenseWhenCallingGaoyaHashThenReturnGaoyaMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content)).isEqualTo("[23979,2396780,6313269,761130,3610111,1186666,1303932,1508153,4442427,1651964,2338514,1901198,1294498,2578085,4675105,4812751,506803,3099489,902404,331744,1604799,2660917,5558194,339826,570567,5201782,301447,1557312,1341467,1301612,3875153,4423132,1396252,1051108,26873,3744828,338994,694123,15951736,1694619,1969374,1192053,1152261,919957,2699217,761009,3380290,2610592,7844080,2057965,5377804,1673191,2285567,96210,1401256,1755944,1273828,677869,556238,1124392,1261469,417531,3116984,4403883,225841,928050,2411409,1351418,1166061,1024547,3822919,5716166,5612956,3801661,3899707,925585,1100692,4993517,2831376,7677380,2542397,852146,1728453,10486050,1010258,7030255,3037414,4548199,756386,995961,302342,436015,1076049,2342489,8432744,1019860,1214765,2309382,2080569,602298,5505972,4908483,8302555,1089133,1315047,1432922,1163037,1429031,6117001,8750751,1483882,6821708,1646212,4241528,2698360,7221021,759265,1548256,311719,650737,2154098,3706046,1087778,1802404,2961894,7757501]");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.utf8.license")
    void givenUTF8MITLicenseWhenCallingGaoyaHashWithAsciiNormalizationThenReturnGaoyaMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content, AsciiHelper::stripNonAscii)).isEqualTo("[23979,2396780,6313269,761130,3610111,1186666,1303932,1508153,4442427,1651964,2338514,1901198,1294498,2578085,4675105,4812751,506803,3099489,902404,331744,1604799,2660917,5558194,339826,570567,5201782,301447,1557312,1341467,1301612,3875153,4423132,1396252,1051108,26873,3744828,338994,694123,15951736,1694619,1969374,1192053,1152261,919957,2699217,761009,3380290,2610592,7844080,2057965,5377804,1673191,2285567,96210,1401256,1755944,1273828,677869,556238,1124392,1261469,417531,3116984,4403883,225841,928050,2411409,1351418,1166061,1024547,3822919,5716166,5612956,3801661,3899707,925585,1100692,4993517,2831376,7677380,2542397,852146,1728453,10486050,1010258,7030255,3037414,4548199,756386,995961,302342,436015,1076049,2342489,8432744,1019860,1214765,2309382,2080569,602298,5505972,4908483,8302555,1089133,1315047,1432922,1163037,1429031,6117001,8750751,1483882,6821708,1646212,4241528,2698360,7221021,759265,1548256,311719,650737,2154098,3706046,1087778,1802404,2961894,7757501]");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.ascii.license")
    void givenASCIIMITLicenseWhenCallingGaoyaHashThenReturnGaoyaMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content)).isEqualTo("[23979,2396780,6313269,761130,3610111,1186666,1303932,1508153,4442427,1651964,2338514,1901198,1294498,2578085,4675105,4812751,506803,3099489,902404,331744,1604799,2660917,5558194,339826,570567,5201782,301447,1557312,1341467,1301612,3875153,4423132,1396252,1051108,26873,3744828,338994,694123,15951736,1694619,1969374,1192053,1152261,919957,2699217,761009,3380290,2610592,7844080,2057965,5377804,1673191,2285567,96210,1401256,1755944,1273828,677869,556238,1124392,1261469,417531,3116984,4403883,225841,928050,2411409,1351418,1166061,1024547,3822919,5716166,5612956,3801661,3899707,925585,1100692,4993517,2831376,7677380,2542397,852146,1728453,10486050,1010258,7030255,3037414,4548199,756386,995961,302342,436015,1076049,2342489,8432744,1019860,1214765,2309382,2080569,602298,5505972,4908483,8302555,1089133,1315047,1432922,1163037,1429031,6117001,8750751,1483882,6821708,1646212,4241528,2698360,7221021,759265,1548256,311719,650737,2154098,3706046,1087778,1802404,2961894,7757501]");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.ascii.license")
    void givenASCIIMITLicenseWhenCallingGaoyaHashWithAsciiNormalizationThenReturnGaoyaMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content, AsciiHelper::stripNonAscii)).isEqualTo("[23979,2396780,6313269,761130,3610111,1186666,1303932,1508153,4442427,1651964,2338514,1901198,1294498,2578085,4675105,4812751,506803,3099489,902404,331744,1604799,2660917,5558194,339826,570567,5201782,301447,1557312,1341467,1301612,3875153,4423132,1396252,1051108,26873,3744828,338994,694123,15951736,1694619,1969374,1192053,1152261,919957,2699217,761009,3380290,2610592,7844080,2057965,5377804,1673191,2285567,96210,1401256,1755944,1273828,677869,556238,1124392,1261469,417531,3116984,4403883,225841,928050,2411409,1351418,1166061,1024547,3822919,5716166,5612956,3801661,3899707,925585,1100692,4993517,2831376,7677380,2542397,852146,1728453,10486050,1010258,7030255,3037414,4548199,756386,995961,302342,436015,1076049,2342489,8432744,1019860,1214765,2309382,2080569,602298,5505972,4908483,8302555,1089133,1315047,1432922,1163037,1429031,6117001,8750751,1483882,6821708,1646212,4241528,2698360,7221021,759265,1548256,311719,650737,2154098,3706046,1087778,1802404,2961894,7757501]");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.stripped.license")
    void givenStrippedMITLicenseWhenCallingGaoyaHashThenReturnGaoyaMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content)).isEqualTo("[23979,2396780,6313269,761130,3610111,1186666,1303932,1508153,4442427,1651964,2338514,1901198,1294498,2578085,4675105,4812751,506803,3099489,902404,331744,1604799,2660917,5558194,339826,570567,5201782,301447,1557312,1341467,1301612,3875153,4423132,1396252,1051108,26873,3744828,338994,694123,16821291,1694619,1969374,1192053,1152261,919957,2699217,761009,3380290,2610592,7844080,2057965,5377804,1673191,2285567,96210,1401256,1755944,1273828,677869,556238,1124392,1261469,417531,3116984,4403883,225841,928050,2411409,1351418,1166061,1024547,3822919,5716166,5612956,3801661,3899707,925585,1100692,4993517,2831376,7677380,2542397,852146,1728453,10486050,1010258,7030255,3037414,4548199,756386,995961,302342,436015,1076049,2342489,8432744,1019860,1214765,7272700,2080569,602298,5505972,4908483,8302555,1089133,1315047,1432922,1163037,1429031,6117001,8750751,1483882,6821708,1646212,4241528,3456040,7221021,759265,1548256,311719,650737,2154098,3706046,1087778,1802404,2961894,10565247]");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.stripped.license")
    void givenStrippedMITLicenseWhenCallingGaoyaHashWithAsciiNormalizationThenReturnGaoyaMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content, AsciiHelper::stripNonAscii)).isEqualTo("[23979,2396780,6313269,761130,3610111,1186666,1303932,1508153,4442427,1651964,2338514,1901198,1294498,2578085,4675105,4812751,506803,3099489,902404,331744,1604799,2660917,5558194,339826,570567,5201782,301447,1557312,1341467,1301612,3875153,4423132,1396252,1051108,26873,3744828,338994,694123,16821291,1694619,1969374,1192053,1152261,919957,2699217,761009,3380290,2610592,7844080,2057965,5377804,1673191,2285567,96210,1401256,1755944,1273828,677869,556238,1124392,1261469,417531,3116984,4403883,225841,928050,2411409,1351418,1166061,1024547,3822919,5716166,5612956,3801661,3899707,925585,1100692,4993517,2831376,7677380,2542397,852146,1728453,10486050,1010258,7030255,3037414,4548199,756386,995961,302342,436015,1076049,2342489,8432744,1019860,1214765,7272700,2080569,602298,5505972,4908483,8302555,1089133,1315047,1432922,1163037,1429031,6117001,8750751,1483882,6821708,1646212,4241528,3456040,7221021,759265,1548256,311719,650737,2154098,3706046,1087778,1802404,2961894,10565247]");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.normalized.license")
    void givenNormalizedMITLicenseWhenCallingGaoyaHashThenReturnGaoyaMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content)).isEqualTo("[23979,2396780,6313269,761130,3610111,1186666,1303932,1508153,4442427,1651964,2338514,1901198,1294498,2578085,4675105,4812751,506803,3099489,902404,331744,1604799,2660917,5558194,339826,570567,5201782,301447,1557312,1341467,1301612,3875153,4423132,1396252,1051108,26873,3744828,338994,694123,16821291,1694619,1969374,1192053,1152261,919957,2699217,761009,3380290,2610592,7844080,2057965,5377804,1673191,2285567,96210,1401256,1755944,1273828,677869,556238,1124392,1261469,417531,3116984,4403883,225841,928050,2411409,1351418,1166061,1024547,3822919,5716166,5612956,3801661,3899707,925585,1100692,4993517,2831376,7677380,2542397,852146,1728453,10486050,1010258,7030255,3037414,4548199,756386,995961,302342,436015,1076049,2342489,8432744,1019860,1214765,7272700,2080569,602298,5505972,4908483,8302555,1089133,1315047,1432922,1163037,1429031,6117001,8750751,1483882,6821708,1646212,4241528,3456040,7221021,759265,1548256,311719,650737,2154098,3706046,1087778,1802404,2961894,10565247]");
    }

    @ParameterizedTest
    @FileContentSource(path = "/mit.normalized.license")
    void givenNormalizedMITLicenseWhenCallingGaoyaHashWithAsciiNormalizationThenReturnGaoyaMITLicenseHash(String content) {
        assertThat(hasher.computeHash(content, AsciiHelper::stripNonAscii)).isEqualTo("[23979,2396780,6313269,761130,3610111,1186666,1303932,1508153,4442427,1651964,2338514,1901198,1294498,2578085,4675105,4812751,506803,3099489,902404,331744,1604799,2660917,5558194,339826,570567,5201782,301447,1557312,1341467,1301612,3875153,4423132,1396252,1051108,26873,3744828,338994,694123,16821291,1694619,1969374,1192053,1152261,919957,2699217,761009,3380290,2610592,7844080,2057965,5377804,1673191,2285567,96210,1401256,1755944,1273828,677869,556238,1124392,1261469,417531,3116984,4403883,225841,928050,2411409,1351418,1166061,1024547,3822919,5716166,5612956,3801661,3899707,925585,1100692,4993517,2831376,7677380,2542397,852146,1728453,10486050,1010258,7030255,3037414,4548199,756386,995961,302342,436015,1076049,2342489,8432744,1019860,1214765,7272700,2080569,602298,5505972,4908483,8302555,1089133,1315047,1432922,1163037,1429031,6117001,8750751,1483882,6821708,1646212,4241528,3456040,7221021,759265,1548256,311719,650737,2154098,3706046,1087778,1802404,2961894,10565247]");
    }
}
