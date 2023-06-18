/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.wasm;

import com.whichlicense.metadata.identification.license.LicenseHasher;
import com.whichlicense.metadata.identification.license.wasm.internal.PolyglotUtils;
import org.graalvm.polyglot.Value;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.GAOYA;

public final class WasmGaoyaLicenseHasher implements LicenseHasher {
    @Override
    public String computeHash(String license) {
        return PolyglotUtils.computeHash(license, (context, licenseText) -> {
            Value config = context.eval("js", "new wasm_bindgen.GaoyaHashingConfig(\"\", 42, 3, 50)");
            Value func = context.eval("js", "wasm_bindgen.gaoya_compute_hash");
            return func.execute(config, context.asValue(license)).asString();
        });
    }

    @Override
    public String algorithm() {
        return GAOYA;
    }
}
