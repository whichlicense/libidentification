/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/license-detection-backends.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.wasm.internal;

@FunctionalInterface
public interface TriFunction<T1, T2, T3, R> {
    R apply(T1 t1, T2 t2, T3 t3);
}
