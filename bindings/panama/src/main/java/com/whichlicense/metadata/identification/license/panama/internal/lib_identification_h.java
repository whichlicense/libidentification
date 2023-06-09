// Generated by jextract

package com.whichlicense.metadata.identification.license.panama.internal;

import com.whichlicense.foreign.ForeignRuntimeHelper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
public class lib_identification_h  {
    /**
     * {@snippet :
     * typedef void* LicenseIndex;
     * }
     */
    public static final OfAddress LicenseIndex = Constants$root.C_POINTER$LAYOUT;
    public static MethodHandle construct_fuzzy_index$MH() {
        return ForeignRuntimeHelper.requireNonNull(constants$0.construct_fuzzy_index$MH,"construct_fuzzy_index");
    }
    /**
     * {@snippet :
     * LicenseIndex construct_fuzzy_index(const uint8_t* entries, uintptr_t size);
     * }
     */
    public static MemorySegment construct_fuzzy_index(MemorySegment entries, long size) {
        var mh$ = construct_fuzzy_index$MH();
        try {
            return (MemorySegment)mh$.invokeExact(entries, size);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle fuzzy_compute_hash$MH() {
        return ForeignRuntimeHelper.requireNonNull(constants$0.fuzzy_compute_hash$MH,"fuzzy_compute_hash");
    }
    /**
     * {@snippet :
     * char* fuzzy_compute_hash(struct FuzzyHashingConfig* config, char* license);
     * }
     */
    public static MemorySegment fuzzy_compute_hash(MemorySegment config, MemorySegment license) {
        var mh$ = fuzzy_compute_hash$MH();
        try {
            return (MemorySegment)mh$.invokeExact(config, license);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle fuzzy_detect_license$MH() {
        return ForeignRuntimeHelper.requireNonNull(constants$0.fuzzy_detect_license$MH,"fuzzy_detect_license");
    }
    /**
     * {@snippet :
     * struct LicenseMatches fuzzy_detect_license(struct FuzzyHashingConfig* config, char* license);
     * }
     */
    public static MemorySegment fuzzy_detect_license(SegmentAllocator allocator, MemorySegment config, MemorySegment license) {
        var mh$ = fuzzy_detect_license$MH();
        try {
            return (MemorySegment)mh$.invokeExact(allocator, config, license);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle construct_gaoya_index$MH() {
        return ForeignRuntimeHelper.requireNonNull(constants$0.construct_gaoya_index$MH,"construct_gaoya_index");
    }
    /**
     * {@snippet :
     * LicenseIndex construct_gaoya_index(const uint8_t* entries, uintptr_t size);
     * }
     */
    public static MemorySegment construct_gaoya_index(MemorySegment entries, long size) {
        var mh$ = construct_gaoya_index$MH();
        try {
            return (MemorySegment)mh$.invokeExact(entries, size);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle gaoya_compute_hash$MH() {
        return ForeignRuntimeHelper.requireNonNull(constants$1.gaoya_compute_hash$MH,"gaoya_compute_hash");
    }
    /**
     * {@snippet :
     * char* gaoya_compute_hash(struct GaoyaHashingConfig* config, char* license);
     * }
     */
    public static MemorySegment gaoya_compute_hash(MemorySegment config, MemorySegment license) {
        var mh$ = gaoya_compute_hash$MH();
        try {
            return (MemorySegment)mh$.invokeExact(config, license);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle gaoya_detect_license$MH() {
        return ForeignRuntimeHelper.requireNonNull(constants$1.gaoya_detect_license$MH,"gaoya_detect_license");
    }
    /**
     * {@snippet :
     * struct LicenseMatches gaoya_detect_license(struct GaoyaHashingConfig* config, char* license);
     * }
     */
    public static MemorySegment gaoya_detect_license(SegmentAllocator allocator, MemorySegment config, MemorySegment license) {
        var mh$ = gaoya_detect_license$MH();
        try {
            return (MemorySegment)mh$.invokeExact(allocator, config, license);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle pipeline_remove_text_step$MH() {
        return ForeignRuntimeHelper.requireNonNull(constants$1.pipeline_remove_text_step$MH,"pipeline_remove_text_step");
    }
    /**
     * {@snippet :
     * void* pipeline_remove_text_step(char* str);
     * }
     */
    public static MemorySegment pipeline_remove_text_step(MemorySegment str) {
        var mh$ = pipeline_remove_text_step$MH();
        try {
            return (MemorySegment)mh$.invokeExact(str);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle pipeline_remove_regex_step$MH() {
        return ForeignRuntimeHelper.requireNonNull(constants$1.pipeline_remove_regex_step$MH,"pipeline_remove_regex_step");
    }
    /**
     * {@snippet :
     * void* pipeline_remove_regex_step(char* pattern);
     * }
     */
    public static MemorySegment pipeline_remove_regex_step(MemorySegment pattern) {
        var mh$ = pipeline_remove_regex_step$MH();
        try {
            return (MemorySegment)mh$.invokeExact(pattern);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle pipeline_replace_text_step$MH() {
        return ForeignRuntimeHelper.requireNonNull(constants$1.pipeline_replace_text_step$MH,"pipeline_replace_text_step");
    }
    /**
     * {@snippet :
     * void* pipeline_replace_text_step(char* target, char* replacement);
     * }
     */
    public static MemorySegment pipeline_replace_text_step(MemorySegment target, MemorySegment replacement) {
        var mh$ = pipeline_replace_text_step$MH();
        try {
            return (MemorySegment)mh$.invokeExact(target, replacement);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle pipeline_replace_regex_step$MH() {
        return ForeignRuntimeHelper.requireNonNull(constants$1.pipeline_replace_regex_step$MH,"pipeline_replace_regex_step");
    }
    /**
     * {@snippet :
     * void* pipeline_replace_regex_step(char* pattern, char* replacement);
     * }
     */
    public static MemorySegment pipeline_replace_regex_step(MemorySegment pattern, MemorySegment replacement) {
        var mh$ = pipeline_replace_regex_step$MH();
        try {
            return (MemorySegment)mh$.invokeExact(pattern, replacement);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle pipeline_batch_steps$MH() {
        return ForeignRuntimeHelper.requireNonNull(constants$2.pipeline_batch_steps$MH,"pipeline_batch_steps");
    }
    /**
     * {@snippet :
     * void* pipeline_batch_steps(void* steps, uintptr_t length);
     * }
     */
    public static MemorySegment pipeline_batch_steps(MemorySegment steps, long length) {
        var mh$ = pipeline_batch_steps$MH();
        try {
            return (MemorySegment)mh$.invokeExact(steps, length);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle fuzzy_pipeline_detect_license$MH() {
        return ForeignRuntimeHelper.requireNonNull(constants$2.fuzzy_pipeline_detect_license$MH,"fuzzy_pipeline_detect_license");
    }
    /**
     * {@snippet :
     * struct PipelineLicenseMatches fuzzy_pipeline_detect_license(struct FuzzyHashingConfig* config, struct PipelineConfig* pipeline, char* license);
     * }
     */
    public static MemorySegment fuzzy_pipeline_detect_license(SegmentAllocator allocator, MemorySegment config, MemorySegment pipeline, MemorySegment license) {
        var mh$ = fuzzy_pipeline_detect_license$MH();
        try {
            return (MemorySegment)mh$.invokeExact(allocator, config, pipeline, license);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle gaoya_pipeline_detect_license$MH() {
        return ForeignRuntimeHelper.requireNonNull(constants$2.gaoya_pipeline_detect_license$MH,"gaoya_pipeline_detect_license");
    }
    /**
     * {@snippet :
     * struct PipelineLicenseMatches gaoya_pipeline_detect_license(struct GaoyaHashingConfig* config, struct PipelineConfig* pipeline, char* license);
     * }
     */
    public static MemorySegment gaoya_pipeline_detect_license(SegmentAllocator allocator, MemorySegment config, MemorySegment pipeline, MemorySegment license) {
        var mh$ = gaoya_pipeline_detect_license$MH();
        try {
            return (MemorySegment)mh$.invokeExact(allocator, config, pipeline, license);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
}
