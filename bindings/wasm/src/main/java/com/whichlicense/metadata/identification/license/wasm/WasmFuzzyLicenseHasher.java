/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/license-detection-backends.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.wasm;

import com.whichlicense.metadata.identification.license.LicenseHasher;
import com.whichlicense.metadata.identification.license.wasm.internal.PolyglotUtils;
import org.graalvm.polyglot.Value;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.FUZZY;

public final class WasmFuzzyLicenseHasher implements LicenseHasher {
    @Override
    public String computeHash(String license) {
        return PolyglotUtils.computeHash(license, (context, licenseText) -> {
            Value config = context.eval("js", "new wasm_bindgen.FuzzyHashingConfig(\"\", 50, true)");
            Value func = context.eval("js", "wasm_bindgen.fuzzy_compute_hash");
            return func.execute(config, context.asValue(license)).asString();
        });
    }

    @Override
    public String algorithm() {
        return FUZZY;
    }
}
