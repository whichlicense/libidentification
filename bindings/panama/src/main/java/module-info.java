/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/license-detection-backends.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import com.whichlicense.metadata.identification.license.LicenseHasher;
import com.whichlicense.metadata.identification.license.LicenseIdentifier;
import com.whichlicense.metadata.identification.license.panama.PanamaFuzzyLicenseHasher;
import com.whichlicense.metadata.identification.license.panama.PanamaFuzzyLicenseIdentifier;
import com.whichlicense.metadata.identification.license.panama.PanamaGaoyaLicenseHasher;
import com.whichlicense.metadata.identification.license.panama.PanamaGaoyaLicenseIdentifier;

module whichlicense.identification.license.panama {
    requires java.logging;
    requires transitive whichlicense.foreign;
    requires transitive whichlicense.identification.license;
    provides LicenseHasher with PanamaFuzzyLicenseHasher, PanamaGaoyaLicenseHasher;
    provides LicenseIdentifier with PanamaFuzzyLicenseIdentifier, PanamaGaoyaLicenseIdentifier;
}
