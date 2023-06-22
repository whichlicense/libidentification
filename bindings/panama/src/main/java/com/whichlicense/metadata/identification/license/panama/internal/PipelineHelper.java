package com.whichlicense.metadata.identification.license.panama.internal;

import com.whichlicense.metadata.identification.license.pipeline.PipelineStep;
import com.whichlicense.metadata.identification.license.pipeline.PipelineStep.Batch;
import com.whichlicense.metadata.identification.license.pipeline.PipelineStep.Remove;
import com.whichlicense.metadata.identification.license.pipeline.PipelineStep.Replace;
import com.whichlicense.metadata.identification.license.pipeline.PipelineStepArgument.Regex;
import com.whichlicense.metadata.identification.license.pipeline.PipelineStepArgument.Text;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

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
            case PipelineStep.Custom(var function) -> {
                yield MemorySegment.NULL;
            }
            case Batch(var steps) -> {
                /*IntStream.range(0, steps.size()).forEach(i -> {

                });

                var wrapped = steps.stream().map(s -> wrapStep(arena, s)).toList();


                steps.forEach(s -> {

                });*/
                yield MemorySegment.NULL;
            }
        };
    }
}
