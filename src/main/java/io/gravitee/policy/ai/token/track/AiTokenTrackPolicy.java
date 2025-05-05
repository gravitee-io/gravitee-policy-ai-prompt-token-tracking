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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.gravitee.common.http.MediaType;
import io.gravitee.gateway.api.buffer.Buffer;
import io.gravitee.gateway.api.http.HttpHeaderNames;
import io.gravitee.gateway.reactive.api.context.http.HttpPlainExecutionContext;
import io.gravitee.gateway.reactive.api.policy.http.HttpPolicy;
import io.gravitee.policy.ai.token.track.configuration.AiTokenTrackingConfiguration;
import io.gravitee.policy.ai.token.track.utils.Tokens;
import io.reactivex.rxjava3.core.Completable;
import java.io.IOException;
import java.util.List;

public class AiTokenTrackPolicy implements HttpPolicy {

    private final AiTokenTrackingConfiguration configuration;
    private final ProviderExtractor providerExtractor;

    public AiTokenTrackPolicy(AiTokenTrackingConfiguration configuration) {
        this.configuration = configuration;
        providerExtractor = configuration.toProviderExtractor();
    }

    @Override
    public String id() {
        return "ai-prompt-token-tracking";
    }

    @Override
    public Completable onResponse(HttpPlainExecutionContext ctx) {
        return ctx.response().body().doOnSuccess(content -> extracted(content, ctx)).ignoreElement();
    }

    private void extracted(Buffer content, HttpPlainExecutionContext ctx) throws IOException {
        if (!isJsonBody(ctx)) {
            return;
        }
        JsonNode jsonNode = JsonMapper.builder().build().readValue(content.getBytes(), JsonNode.class);
        var extractedData = providerExtractor.apply(jsonNode);
        extractedData.ifPresent(data -> {
            ctx.metrics().putAdditionalMetric("long_ai-prompt-token-sent", data.input());
            ctx.metrics().putAdditionalMetric("long_ai-prompt-token-receive", data.output());
            if (data instanceof Tokens.TokensAndModel<?> tokensAndModel) {
                ctx.metrics().putAdditionalKeywordMetric("keyword_ai-prompt-token-model", tokensAndModel.model());
            }
            configuration
                .getCost(data)
                .ifPresent(cost -> {
                    ctx.metrics().putAdditionalMetric("double_ai-prompt-token-sent-cost", cost.input());
                    ctx.metrics().putAdditionalMetric("double_ai-prompt-token-receive-cost", cost.output());
                });
        });
    }

    private static boolean isJsonBody(HttpPlainExecutionContext ctx) {
        return ctx
            .response()
            .headers()
            .getOrDefault(HttpHeaderNames.CONTENT_TYPE, List.of())
            .stream()
            .anyMatch(MediaType.APPLICATION_JSON::equals);
    }
}
