/*
 * Copyright (c) 2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/whichlicense/libidentification.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.whichlicense.metadata.identification.license.panama.internal;

import com.whichlicense.metadata.identification.license.pipeline.PipelineStep;
import com.whichlicense.metadata.identification.license.pipeline.PipelineStep.Batch;
import com.whichlicense.metadata.identification.license.pipeline.PipelineStep.Custom;
import com.whichlicense.metadata.identification.license.pipeline.PipelineStep.Remove;
import com.whichlicense.metadata.identification.license.pipeline.PipelineStep.Replace;
import com.whichlicense.metadata.identification.license.pipeline.PipelineStepArgument.Regex;
import com.whichlicense.metadata.identification.license.pipeline.PipelineStepArgument.Text;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static com.whichlicense.metadata.identification.license.panama.internal.Constants$root.C_POINTER$LAYOUT;

public final class PipelineHelper {
    public static MemorySegment wrapStep(Arena arena, PipelineStep step) {
        return switch (step) {
            case Remove(var argument) -> switch (argument) {
                case Regex(var pattern) -> {
                    var regex = arena.allocateUtf8String(pattern.pattern());
                    yield lib_identification_h.pipeline_remove_regex_step(regex);
                }
                case Text(var string) -> {
                    var text = arena.allocateUtf8String(string);
                    yield lib_identification_h.pipeline_remove_text_step(text);
                }
            };
            case Replace(var target, var replacement) -> {
                var next = arena.allocateUtf8String(replacement);
                yield switch (target) {
                    case Regex(var pattern) -> {
                        var regex = arena.allocateUtf8String(pattern.pattern());
                        yield lib_identification_h.pipeline_replace_regex_step(regex, next);
                    }
                    case Text(var string) -> {
                        var text = arena.allocateUtf8String(string);
                        yield lib_identification_h.pipeline_replace_text_step(text, next);
                    }
                };
            }
            case Custom(var function) -> MemorySegment.NULL;
            case Batch(var steps) -> {
                var array = MemorySegment.allocateNative(C_POINTER$LAYOUT.byteSize() * steps.size(), arena.scope());

                for (var i = 0; i < steps.size(); i++) {
                    array.set(C_POINTER$LAYOUT, i, wrapStep(arena, steps.get(i)));
                }

                yield lib_identification_h.pipeline_batch_steps(array, steps.size());
            }
        };
    }
}
