/*
 * Copyright © 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.policy.ai.token.track;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import io.gravitee.policy.ai.token.track.utils.Tokens;
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
