/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.panama;

import static java.util.stream.Collectors.joining;

final class AsciiHelper {
    static String stripNonAscii(String original) {
        return original.chars().filter(c -> (c >= 'a' && c <= 'z')
                        || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'))
                .mapToObj(c -> String.valueOf((char) c)).collect(joining());
    }
}
