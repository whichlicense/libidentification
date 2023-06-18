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

import static com.whichlicense.metadata.identification.license.internal.HashingAlgorithm.FUZZY;

public final class WasmFuzzyLicenseIdentifier implements LicenseIdentifier {
    @Override
    public Set<LicenseMatch> identifyLicenses(String license) {
        return PolyglotUtils.detectLicenses(license, FUZZY, (context, licenses, licenseText) -> {
            var config_constructor = context.eval("js", "wasm_bindgen.FuzzyHashingConfig");
            var config = config_constructor.newInstance(context.asValue(licenses),
                    context.asValue(50), context.asValue(true));
            var func = context.eval("js", "wasm_bindgen.fuzzy_detect_license");
            return func.execute(config, context.asValue(licenseText)).asString();
        });
    }

    @Override
    public String algorithm() {
        return FUZZY;
    }
}
