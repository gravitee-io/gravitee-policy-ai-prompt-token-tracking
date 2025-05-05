package com.graviteesource.policy.ai.token.track;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.graviteesource.policy.ai.token.track.utils.Tokens;
import java.util.Optional;
import java.util.function.Function;

public sealed interface ProviderExtractor extends Function<JsonNode, Optional<Tokens<Long>>> {
    record ProviderExtractorTokenOnly(JsonPointer inputTokenPtr, JsonPointer outputTokenPtr) implements ProviderExtractor {
        public ProviderExtractorTokenOnly(String inputTokenPtr, String outputTokenPtr) {
            this(JsonPointer.compile(inputTokenPtr), JsonPointer.compile(outputTokenPtr));
        }

        @Override
        public Optional<Tokens<Long>> apply(JsonNode jsonNode) {
            JsonNode inputNode = jsonNode.at(inputTokenPtr);
            JsonNode outputNode = jsonNode.at(outputTokenPtr);
            return inputNode.isNumber() && outputNode.isNumber()
                ? Optional.of(new Tokens.TokensOnly<>(inputNode.asLong(), outputNode.asLong()))
                : Optional.empty();
        }
    }

    record ProviderExtractorTokenAndModel(JsonPointer inputTokenPtr, JsonPointer outputTokenPtr, JsonPointer modelPtr)
        implements ProviderExtractor {
        public ProviderExtractorTokenAndModel(String inputTokenPtr, String outputTokenPtr, String modelPtr) {
            this(JsonPointer.compile(inputTokenPtr), JsonPointer.compile(outputTokenPtr), JsonPointer.compile(modelPtr));
        }

        @Override
        public Optional<Tokens<Long>> apply(JsonNode jsonNode) {
            JsonNode inputNode = jsonNode.at(inputTokenPtr);
            JsonNode outputNode = jsonNode.at(outputTokenPtr);
            JsonNode modelNode = jsonNode.at(modelPtr);
            if (inputNode.isNumber() && outputNode.isNumber()) {
                return modelNode.isTextual()
                    ? Optional.of(new Tokens.TokensAndModel<>(inputNode.asLong(), outputNode.asLong(), modelNode.asText()))
                    : Optional.of(new Tokens.TokensOnly<>(inputNode.asLong(), outputNode.asLong()));
            } else {
                return Optional.empty();
            }
        }
    }
}
