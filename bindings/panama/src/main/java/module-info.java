/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import com.whichlicense.metadata.identification.license.LicenseHasher;
import com.whichlicense.metadata.identification.license.LicenseIdentificationPipeline;
import com.whichlicense.metadata.identification.license.LicenseIdentifier;
import com.whichlicense.metadata.identification.license.panama.*;

open module whichlicense.identification.license.panama {
    requires java.logging;
    requires transitive whichlicense.foreign;
    requires transitive whichlicense.identification.license;
    provides LicenseHasher with PanamaFuzzyLicenseHasher, PanamaGaoyaLicenseHasher;
    provides LicenseIdentifier with PanamaFuzzyLicenseIdentifier, PanamaGaoyaLicenseIdentifier;
    provides LicenseIdentificationPipeline with PanamaFuzzyLicenseIdentificationPipeline, PanamaGaoyaLicenseIdentificationPipeline;
}
