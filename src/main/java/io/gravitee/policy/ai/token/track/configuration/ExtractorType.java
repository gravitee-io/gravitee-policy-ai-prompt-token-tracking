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

import com.fasterxml.jackson.annotation.JsonValue;

public enum ExtractorType {
    GPT(
        "GPT",
        "{#response.jsonContent.usage.prompt_tokens}",
        "{#response.jsonContent.usage.completion_tokens}",
        "{#response.jsonContent.model}"
    ),
    GEMINI(
        "GEMINI",
        "{#response.jsonContent.usage.input_tokens}",
        "{#response.jsonContent.usage.output_tokens}",
        "{#response.jsonContent.model}"
    ),
    CLAUDE(
        "CLAUDE",
        "{#response.jsonContent.usageMetadata.promptTokenCount}",
        "{#response.jsonContent.usageMetadata.candidatesTokenCount}",
        "{#response.jsonContent.modelVersion}"
    ),
    MISTRAL(
        "MISTRAL",
        "{#response.jsonContent.usage.prompt_tokens}",
        "{#response.jsonContent.usage.completion_tokens}",
        "{#response.jsonContent.model}"
    ),
    CUSTOM("CUSTOM", null, null, null);

    @JsonValue
    private final String type;

    private final String inputTokenPointer;
    private final String outputTokenPointer;
    private final String modelPointer;

    ExtractorType(String type, String inputTokenPointer, String outputTokenPointer, String modelPointer) {
        this.type = type;
        this.inputTokenPointer = inputTokenPointer;
        this.outputTokenPointer = outputTokenPointer;
        this.modelPointer = modelPointer;
    }

    public String inputTokenPointer() {
        return inputTokenPointer;
    }

    public String outputTokenPointer() {
        return outputTokenPointer;
    }

    public String modelPointer() {
        return modelPointer;
    }
}
