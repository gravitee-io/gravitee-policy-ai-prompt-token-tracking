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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.gravitee.apim.gateway.tests.sdk.annotations.DeployApi;
import io.gravitee.apim.gateway.tests.sdk.annotations.GatewayTest;
import io.gravitee.reporter.api.v4.metric.Metrics;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava3.core.http.HttpClient;
import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AiTokenTrackPolicyIntegrationTest {

    @Nested
    @GatewayTest
    @DeployApi({ "/apis/gpt-token-only.json", "/apis/gpt-token-and-pricing.json" })
    class OpenAI extends AbstractAiTokenTrackPolicyIntegrationTest {

        @Test
        void should_extract_tokens_usage_from_gpt_response(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .isEqualTo(
                            Map.ofEntries(
                                Map.entry("long_ai-prompt-token-sent", 500000L),
                                Map.entry("long_ai-prompt-token-receive", 750000L),
                                Map.entry("keyword_ai-prompt-token-model", "gpt-4")
                            )
                        )
                )
                .doOnNext(m -> context.completeNow())
                .doOnError(context::failNow)
                .subscribe();

            send(client, "/gpt-token-only");
        }

        @Test
        void should_extract_tokens_usage_and_pricing_from_gpt_response(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .isEqualTo(
                            Map.ofEntries(
                                Map.entry("long_ai-prompt-token-sent", 500000L),
                                Map.entry("long_ai-prompt-token-receive", 750000L),
                                Map.entry("keyword_ai-prompt-token-model", "gpt-4"),
                                Map.entry("double_ai-prompt-token-sent-cost", 0.2),
                                Map.entry("double_ai-prompt-token-receive-cost", 0.6)
                            )
                        )
                )
                .doOnNext(m -> context.completeNow())
                .doOnError(context::failNow)
                .subscribe();

            send(client, "/gpt-token-and-pricing");
        }
    }

    @Nested
    @GatewayTest
    @DeployApi({ "/apis/gemini-token-only.json", "/apis/gemini-token-and-pricing.json" })
    class Gemini extends AbstractAiTokenTrackPolicyIntegrationTest {

        @Test
        void should_extract_tokens_usage(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .isEqualTo(
                            Map.ofEntries(
                                Map.entry("long_ai-prompt-token-sent", 500000L),
                                Map.entry("long_ai-prompt-token-receive", 750000L),
                                Map.entry("keyword_ai-prompt-token-model", "gemini-2")
                            )
                        )
                )
                .doOnNext(m -> context.completeNow())
                .doOnError(context::failNow)
                .subscribe();

            send(client, "/gemini-token-only");
        }

        @Test
        void should_extract_tokens_usage_and_pricing(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .isEqualTo(
                            Map.ofEntries(
                                Map.entry("long_ai-prompt-token-sent", 500000L),
                                Map.entry("long_ai-prompt-token-receive", 750000L),
                                Map.entry("keyword_ai-prompt-token-model", "gemini-2"),
                                Map.entry("double_ai-prompt-token-sent-cost", 0.2),
                                Map.entry("double_ai-prompt-token-receive-cost", 0.6)
                            )
                        )
                )
                .doOnNext(m -> context.completeNow())
                .doOnError(context::failNow)
                .subscribe();

            send(client, "/gemini-token-and-pricing");
        }
    }

    @Nested
    @GatewayTest
    @DeployApi({ "/apis/claude-token-only.json", "/apis/claude-token-and-pricing.json" })
    class Claude extends AbstractAiTokenTrackPolicyIntegrationTest {

        @Test
        void should_extract_tokens_usage(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .isEqualTo(
                            Map.ofEntries(
                                Map.entry("long_ai-prompt-token-sent", 500000L),
                                Map.entry("long_ai-prompt-token-receive", 750000L),
                                Map.entry("keyword_ai-prompt-token-model", "claude-3")
                            )
                        )
                )
                .doOnNext(m -> context.completeNow())
                .doOnError(context::failNow)
                .subscribe();

            send(client, "/claude-token-only");
        }

        @Test
        void should_extract_tokens_usage_and_pricing(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .isEqualTo(
                            Map.ofEntries(
                                Map.entry("long_ai-prompt-token-sent", 500000L),
                                Map.entry("long_ai-prompt-token-receive", 750000L),
                                Map.entry("keyword_ai-prompt-token-model", "claude-3"),
                                Map.entry("double_ai-prompt-token-sent-cost", 0.2),
                                Map.entry("double_ai-prompt-token-receive-cost", 0.6)
                            )
                        )
                )
                .doOnNext(m -> context.completeNow())
                .doOnError(context::failNow)
                .subscribe();

            send(client, "/claude-token-and-pricing");
        }
    }

    @Nested
    @GatewayTest
    @DeployApi({ "/apis/mistral-token-only.json", "/apis/mistral-token-and-pricing.json" })
    class Mistral extends AbstractAiTokenTrackPolicyIntegrationTest {

        @Test
        void should_extract_tokens_usage(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .isEqualTo(
                            Map.ofEntries(
                                Map.entry("long_ai-prompt-token-sent", 500000L),
                                Map.entry("long_ai-prompt-token-receive", 750000L),
                                Map.entry("keyword_ai-prompt-token-model", "mistral-1")
                            )
                        )
                )
                .doOnNext(m -> context.completeNow())
                .doOnError(context::failNow)
                .subscribe();

            send(client, "/mistral-token-only");
        }

        @Test
        void should_extract_tokens_usage_and_pricing(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .isEqualTo(
                            Map.ofEntries(
                                Map.entry("long_ai-prompt-token-sent", 500000L),
                                Map.entry("long_ai-prompt-token-receive", 750000L),
                                Map.entry("keyword_ai-prompt-token-model", "mistral-1"),
                                Map.entry("double_ai-prompt-token-sent-cost", 0.2),
                                Map.entry("double_ai-prompt-token-receive-cost", 0.6)
                            )
                        )
                )
                .doOnNext(m -> context.completeNow())
                .doOnError(context::failNow)
                .subscribe();

            send(client, "/mistral-token-and-pricing");
        }
    }

    @Nested
    @GatewayTest
    @DeployApi({ "/apis/custom-token-only.json", "/apis/custom-token-and-pricing.json" })
    class Custom extends AbstractAiTokenTrackPolicyIntegrationTest {

        @Test
        void should_extract_tokens_usage(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .isEqualTo(
                            Map.ofEntries(
                                Map.entry("long_ai-prompt-token-sent", 500000L),
                                Map.entry("long_ai-prompt-token-receive", 750000L),
                                Map.entry("keyword_ai-prompt-token-model", "custom-1")
                            )
                        )
                )
                .doOnNext(m -> context.completeNow())
                .doOnError(context::failNow)
                .subscribe();

            send(client, "/custom-token-only");
        }

        @Test
        void should_extract_tokens_usage_and_pricing(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .isEqualTo(
                            Map.ofEntries(
                                Map.entry("long_ai-prompt-token-sent", 500000L),
                                Map.entry("long_ai-prompt-token-receive", 750000L),
                                Map.entry("keyword_ai-prompt-token-model", "custom-1"),
                                Map.entry("double_ai-prompt-token-sent-cost", 0.2),
                                Map.entry("double_ai-prompt-token-receive-cost", 0.6)
                            )
                        )
                )
                .doOnNext(m -> context.completeNow())
                .doOnError(context::failNow)
                .subscribe();

            send(client, "/custom-token-and-pricing");
        }
    }

    @Nested
    @GatewayTest
    @DeployApi({ "/apis/custom-without-model.json" })
    class CustomWithoutModel extends AbstractAiTokenTrackPolicyIntegrationTest {

        @Test
        void should_extract_tokens_usage_and_pricing_event_dont_find_model(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .isEqualTo(
                            Map.ofEntries(
                                Map.entry("long_ai-prompt-token-sent", 500000L),
                                Map.entry("long_ai-prompt-token-receive", 750000L),
                                Map.entry("double_ai-prompt-token-sent-cost", 0.2),
                                Map.entry("double_ai-prompt-token-receive-cost", 0.6)
                            )
                        )
                )
                .doOnNext(m -> context.completeNow())
                .doOnError(context::failNow)
                .subscribe();

            send(client, "/without-model");
        }
    }
}
