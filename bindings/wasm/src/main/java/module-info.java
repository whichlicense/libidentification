/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import com.whichlicense.metadata.identification.license.LicenseHasher;
import com.whichlicense.metadata.identification.license.LicenseIdentifier;
import com.whichlicense.metadata.identification.license.wasm.WasmFuzzyLicenseHasher;
import com.whichlicense.metadata.identification.license.wasm.WasmFuzzyLicenseIdentifier;
import com.whichlicense.metadata.identification.license.wasm.WasmGaoyaLicenseHasher;
import com.whichlicense.metadata.identification.license.wasm.WasmGaoyaLicenseIdentifier;

module whichlicense.identification.license.wasm {
    requires org.graalvm.truffle;
    requires transitive whichlicense.identification.license;
    provides LicenseHasher with WasmFuzzyLicenseHasher, WasmGaoyaLicenseHasher;
    provides LicenseIdentifier with WasmFuzzyLicenseIdentifier, WasmGaoyaLicenseIdentifier;
}
