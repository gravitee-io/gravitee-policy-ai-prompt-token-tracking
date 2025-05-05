package com.graviteesource.policy.ai.token.track;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.graviteesource.policy.ai.token.track.configuration.AiTokenTrackingConfiguration;
import com.graviteesource.policy.ai.token.track.utils.Tokens;
import io.gravitee.gateway.api.buffer.Buffer;
import io.gravitee.gateway.reactive.api.context.HttpExecutionContext;
import io.gravitee.gateway.reactive.api.context.MessageExecutionContext;
import io.gravitee.gateway.reactive.api.message.Message;
import io.gravitee.gateway.reactive.api.policy.Policy;
import io.gravitee.reporter.api.v4.metric.Metrics;
import io.reactivex.rxjava3.core.Completable;
import java.io.IOException;

public class AiTokenTrackPolicy implements Policy {

    private final AiTokenTrackingConfiguration configuration;
    private final ProviderExtractor providerExtractor;

    public AiTokenTrackPolicy(AiTokenTrackingConfiguration configuration) {
        this.configuration = configuration;
        providerExtractor = configuration.toProviderExtractor();
    }

    @Override
    public String id() {
        return "ai-token-track";
    }

    @Override
    public Completable onResponse(HttpExecutionContext ctx) {
        return ctx.response().body().doOnSuccess(content -> extracted(content, ctx.metrics())).ignoreElement();
    }

    @Override
    public Completable onMessageResponse(MessageExecutionContext ctx) {
        return ctx.response().messages().map(Message::content).doOnNext(content -> extracted(content, ctx.metrics())).ignoreElements();
    }

    private void extracted(Buffer content, Metrics metrics) throws IOException {
        JsonNode jsonNode = JsonMapper.builder().build().readValue(content.getBytes(), JsonNode.class);
        var extractedData = providerExtractor.apply(jsonNode);
        extractedData.ifPresent(data -> {
            metrics.putAdditionalMetric("long_ai-prompt-token-sent", data.input());
            metrics.putAdditionalMetric("long_ai-prompt-token-receive", data.output());
            if (data instanceof Tokens.TokensAndModel<?> tokensAndModel) {
                metrics.putAdditionalKeywordMetric("keyword_ai-prompt-token-sent", tokensAndModel.model());
            }
            configuration
                .getCost(data)
                .ifPresent(cost -> {
                    metrics.putAdditionalMetric("double_ai-prompt-token-sent-cost", cost.input());
                    metrics.putAdditionalMetric("double_ai-prompt-token-receive-cost", cost.output());
                });
        });
    }
}
