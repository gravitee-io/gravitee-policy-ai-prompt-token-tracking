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
package io.gravitee.policy.ai.token.track.configuration;

import io.gravitee.policy.ai.token.track.ProviderExtractor;

public record ProviderExtractorConfiguration(ExtractorType type, String inputTokenPointer, String outputTokenPointer, String modelPointer) {
    public String inputTokenPointer() {
        return switch (type) {
            case CUSTOM -> inputTokenPointer;
            default -> type.inputTokenPointer();
        };
    }

    public String outputTokenPointer() {
        return switch (type) {
            case CUSTOM -> outputTokenPointer;
            default -> type.outputTokenPointer();
        };
    }

    public String modelPointer() {
        return switch (type) {
            case CUSTOM -> modelPointer;
            default -> type.modelPointer();
        };
    }

    public ProviderExtractor toProviderExtractor() {
        return modelPointer() == null || modelPointer().isBlank()
            ? new ProviderExtractor.ProviderExtractorTokenOnly(inputTokenPointer(), outputTokenPointer())
            : new ProviderExtractor.ProviderExtractorTokenAndModel(inputTokenPointer(), outputTokenPointer(), modelPointer());
    }
}
