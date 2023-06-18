/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.wasm;

import com.whichlicense.metadata.identification.license.LicenseIdentifier;
import com.whichlicense.metadata.identification.license.LicenseMatch;
import com.whichlicense.metadata.identification.license.wasm.internal.PolyglotUtils;

import java.util.Set;

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.GAOYA;

public final class WasmGaoyaLicenseIdentifier implements LicenseIdentifier {
    @Override
    public Set<LicenseMatch> identifyLicenses(String license) {
        return PolyglotUtils.detectLicenses(license, GAOYA, (context, licenses, licenseText) -> {
            var config_constructor = context.eval("js", "wasm_bindgen.GaoyaHashingConfig");
            var config = config_constructor.newInstance(context.asValue(licenses),
                    context.asValue(42), context.asValue(3), context.asValue(50));
            var func = context.eval("js", "wasm_bindgen.gaoya_detect_license");
            return func.execute(config, context.asValue(licenseText)).asString();
        });
    }

    @Override
    public String algorithm() {
        return GAOYA;
    }
}
