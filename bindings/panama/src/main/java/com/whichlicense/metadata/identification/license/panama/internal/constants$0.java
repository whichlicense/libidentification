// Generated by jextract

package com.whichlicense.metadata.identification.license.panama.internal;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemoryLayout;
import java.lang.invoke.MethodHandle;

final class constants$0 {
    static final FunctionDescriptor CNormalizationFn$FUNC = FunctionDescriptor.of(Constants$root.C_POINTER$LAYOUT,
            Constants$root.C_POINTER$LAYOUT
    );
    static final FunctionDescriptor CNormalizationFn_UP$FUNC = FunctionDescriptor.of(Constants$root.C_POINTER$LAYOUT,
            Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle CNormalizationFn_UP$MH = RuntimeHelper.upcallHandle(CNormalizationFn.class, "apply", constants$0.CNormalizationFn_UP$FUNC);
    static final FunctionDescriptor construct_fuzzy_index$FUNC = FunctionDescriptor.of(Constants$root.C_POINTER$LAYOUT,
            Constants$root.C_POINTER$LAYOUT,
            Constants$root.C_LONG_LONG$LAYOUT
    );
    static final MethodHandle construct_fuzzy_index$MH = RuntimeHelper.downcallHandle(
            "construct_fuzzy_index",
            constants$0.construct_fuzzy_index$FUNC
    );
    static final FunctionDescriptor fuzzy_compute_hash$FUNC = FunctionDescriptor.of(Constants$root.C_POINTER$LAYOUT,
            Constants$root.C_POINTER$LAYOUT,
            Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle fuzzy_compute_hash$MH = RuntimeHelper.downcallHandle(
            "fuzzy_compute_hash",
            constants$0.fuzzy_compute_hash$FUNC
    );
    static final FunctionDescriptor fuzzy_detect_license$FUNC = FunctionDescriptor.of(MemoryLayout.structLayout(
                    Constants$root.C_POINTER$LAYOUT.withName("matches"),
                    Constants$root.C_LONG_LONG$LAYOUT.withName("length")
            ).withName("LicenseMatches"),
            Constants$root.C_POINTER$LAYOUT,
            Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle fuzzy_detect_license$MH = RuntimeHelper.downcallHandle(
            "fuzzy_detect_license",
            constants$0.fuzzy_detect_license$FUNC
    );
    static final FunctionDescriptor construct_gaoya_index$FUNC = FunctionDescriptor.of(Constants$root.C_POINTER$LAYOUT,
            Constants$root.C_POINTER$LAYOUT,
            Constants$root.C_LONG_LONG$LAYOUT
    );
    static final MethodHandle construct_gaoya_index$MH = RuntimeHelper.downcallHandle(
            "construct_gaoya_index",
            constants$0.construct_gaoya_index$FUNC
    );

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$0() {
    }
}