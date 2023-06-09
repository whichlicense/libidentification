// Generated by jextract

package com.whichlicense.metadata.identification.license.panama.internal;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemoryLayout.PathElement;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.StructLayout;
import java.lang.invoke.VarHandle;

/**
 * {@snippet :
 * struct GaoyaHashingConfig {
 *     LicenseIndex index;
 *     uintptr_t band_count;
 *     uintptr_t band_width;
 *     uintptr_t shingle_size;
 *     CNormalizationFn normalization_fn;
 * };
 *}
 */
public class GaoyaHashingConfig {
    static final StructLayout $struct$LAYOUT = MemoryLayout.structLayout(
            Constants$root.C_POINTER$LAYOUT.withName("index"),
            Constants$root.C_LONG_LONG$LAYOUT.withName("band_count"),
            Constants$root.C_LONG_LONG$LAYOUT.withName("band_width"),
            Constants$root.C_LONG_LONG$LAYOUT.withName("shingle_size"),
            Constants$root.C_POINTER$LAYOUT.withName("normalization_fn")
    ).withName("GaoyaHashingConfig");
    static final VarHandle index$VH = $struct$LAYOUT.varHandle(PathElement.groupElement("index"));
    static final VarHandle band_count$VH = $struct$LAYOUT.varHandle(PathElement.groupElement("band_count"));
    static final VarHandle band_width$VH = $struct$LAYOUT.varHandle(PathElement.groupElement("band_width"));
    static final VarHandle shingle_size$VH = $struct$LAYOUT.varHandle(PathElement.groupElement("shingle_size"));
    static final VarHandle normalization_fn$VH = $struct$LAYOUT.varHandle(PathElement.groupElement("normalization_fn"));

    public static MemoryLayout $LAYOUT() {
        return GaoyaHashingConfig.$struct$LAYOUT;
    }

    /**
     * Setter for field:
     * {@snippet :
     * LicenseIndex index;
     *}
     */
    public static void index$set(MemorySegment seg, MemorySegment x) {
        GaoyaHashingConfig.index$VH.set(seg, x);
    }

    /**
     * Setter for field:
     * {@snippet :
     * uintptr_t band_count;
     *}
     */
    public static void band_count$set(MemorySegment seg, long x) {
        GaoyaHashingConfig.band_count$VH.set(seg, x);
    }

    /**
     * Setter for field:
     * {@snippet :
     * uintptr_t band_width;
     *}
     */
    public static void band_width$set(MemorySegment seg, long x) {
        GaoyaHashingConfig.band_width$VH.set(seg, x);
    }

    /**
     * Setter for field:
     * {@snippet :
     * uintptr_t shingle_size;
     *}
     */
    public static void shingle_size$set(MemorySegment seg, long x) {
        GaoyaHashingConfig.shingle_size$VH.set(seg, x);
    }

    /**
     * Setter for field:
     * {@snippet :
     * CNormalizationFn normalization_fn;
     *}
     */
    public static void normalization_fn$set(MemorySegment seg, MemorySegment x) {
        GaoyaHashingConfig.normalization_fn$VH.set(seg, x);
    }

    public static MemorySegment allocate(SegmentAllocator allocator) {
        return allocator.allocate($LAYOUT());
    }
}
