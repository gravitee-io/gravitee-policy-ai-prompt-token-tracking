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

import static org.assertj.core.api.Assertions.assertThat;

import io.gravitee.apim.gateway.tests.sdk.annotations.DeployApi;
import io.gravitee.apim.gateway.tests.sdk.annotations.GatewayTest;
import io.gravitee.reporter.api.v4.metric.AdditionalMetric;
import io.gravitee.reporter.api.v4.metric.Metrics;
import io.reactivex.rxjava3.core.Completable;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava3.core.http.HttpClient;
import org.assertj.core.api.InstanceOfAssertFactories;
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

            var metricsAsserts = metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .asInstanceOf(InstanceOfAssertFactories.SET)
                        .containsExactlyInAnyOrder(
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-sent", 500000L),
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-received", 750000L),
                            new AdditionalMetric.KeywordMetric("keyword_llm-proxy_model", "gpt-4")
                        )
                )
                .ignoreElements();

            var clientAsserts = send(client, "/gpt-token-only");

            finalAssert(context, metricsAsserts, clientAsserts);
        }

        @Test
        void should_extract_tokens_usage_and_pricing_from_gpt_response(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            var metricsAsserts = metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .asInstanceOf(InstanceOfAssertFactories.SET)
                        .containsExactlyInAnyOrder(
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-sent", 500000L),
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-received", 750000L),
                            new AdditionalMetric.KeywordMetric("keyword_llm-proxy_model", "gpt-4"),
                            new AdditionalMetric.DoubleMetric("double_llm-proxy_sent-cost", 0.2),
                            new AdditionalMetric.DoubleMetric("double_llm-proxy_received-cost", 0.6)
                        )
                )
                .ignoreElements();

            var clientAsserts = send(client, "/gpt-token-and-pricing");

            finalAssert(context, metricsAsserts, clientAsserts);
        }
    }

    @Nested
    @GatewayTest
    @DeployApi({ "/apis/gemini-token-only.json", "/apis/gemini-token-and-pricing.json" })
    class Gemini extends AbstractAiTokenTrackPolicyIntegrationTest {

        @Test
        void should_extract_tokens_usage(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            var metricsAsserts = metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .asInstanceOf(InstanceOfAssertFactories.SET)
                        .containsExactlyInAnyOrder(
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-sent", 500000L),
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-received", 750000L),
                            new AdditionalMetric.KeywordMetric("keyword_llm-proxy_model", "gemini-2")
                        )
                )
                .ignoreElements();

            var clientAsserts = send(client, "/gemini-token-only");

            finalAssert(context, metricsAsserts, clientAsserts);
        }

        @Test
        void should_extract_tokens_usage_and_pricing(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            var metricsAsserts = metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .asInstanceOf(InstanceOfAssertFactories.SET)
                        .containsExactlyInAnyOrder(
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-sent", 500000L),
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-received", 750000L),
                            new AdditionalMetric.KeywordMetric("keyword_llm-proxy_model", "gemini-2"),
                            new AdditionalMetric.DoubleMetric("double_llm-proxy_sent-cost", 0.2),
                            new AdditionalMetric.DoubleMetric("double_llm-proxy_received-cost", 0.6)
                        )
                )
                .ignoreElements();

            var clientAsserts = send(client, "/gemini-token-and-pricing");

            finalAssert(context, metricsAsserts, clientAsserts);
        }
    }

    @Nested
    @GatewayTest
    @DeployApi({ "/apis/claude-token-only.json", "/apis/claude-token-and-pricing.json" })
    class Claude extends AbstractAiTokenTrackPolicyIntegrationTest {

        @Test
        void should_extract_tokens_usage(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            var metricsAsserts = metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .asInstanceOf(InstanceOfAssertFactories.SET)
                        .containsExactlyInAnyOrder(
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-sent", 500000L),
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-received", 750000L),
                            new AdditionalMetric.KeywordMetric("keyword_llm-proxy_model", "claude-3")
                        )
                )
                .ignoreElements();

            var clientAsserts = send(client, "/claude-token-only");

            finalAssert(context, metricsAsserts, clientAsserts);
        }

        @Test
        void should_extract_tokens_usage_and_pricing(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            var metricsAsserts = metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .asInstanceOf(InstanceOfAssertFactories.SET)
                        .containsExactlyInAnyOrder(
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-sent", 500000L),
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-received", 750000L),
                            new AdditionalMetric.KeywordMetric("keyword_llm-proxy_model", "claude-3"),
                            new AdditionalMetric.DoubleMetric("double_llm-proxy_sent-cost", 0.2),
                            new AdditionalMetric.DoubleMetric("double_llm-proxy_received-cost", 0.6)
                        )
                )
                .ignoreElements();

            var clientAsserts = send(client, "/claude-token-and-pricing");

            finalAssert(context, metricsAsserts, clientAsserts);
        }
    }

    @Nested
    @GatewayTest
    @DeployApi({ "/apis/mistral-token-only.json", "/apis/mistral-token-and-pricing.json" })
    class Mistral extends AbstractAiTokenTrackPolicyIntegrationTest {

        @Test
        void should_extract_tokens_usage(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            var metricsAsserts = metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .asInstanceOf(InstanceOfAssertFactories.SET)
                        .containsExactlyInAnyOrder(
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-sent", 500000L),
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-received", 750000L),
                            new AdditionalMetric.KeywordMetric("keyword_llm-proxy_model", "mistral-1")
                        )
                )
                .ignoreElements();

            var clientAsserts = send(client, "/mistral-token-only");

            finalAssert(context, metricsAsserts, clientAsserts);
        }

        @Test
        void should_extract_tokens_usage_and_pricing(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            var metricsAsserts = metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .asInstanceOf(InstanceOfAssertFactories.SET)
                        .containsExactlyInAnyOrder(
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-sent", 500000L),
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-received", 750000L),
                            new AdditionalMetric.KeywordMetric("keyword_llm-proxy_model", "mistral-1"),
                            new AdditionalMetric.DoubleMetric("double_llm-proxy_sent-cost", 0.2),
                            new AdditionalMetric.DoubleMetric("double_llm-proxy_received-cost", 0.6)
                        )
                )
                .ignoreElements();

            var clientAsserts = send(client, "/mistral-token-and-pricing");

            finalAssert(context, metricsAsserts, clientAsserts);
        }
    }

    @Nested
    @GatewayTest
    @DeployApi({ "/apis/custom-token-only.json", "/apis/custom-token-and-pricing.json" })
    class Custom extends AbstractAiTokenTrackPolicyIntegrationTest {

        @Test
        void should_extract_tokens_usage(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            var metricsAsserts = metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .asInstanceOf(InstanceOfAssertFactories.SET)
                        .containsExactlyInAnyOrder(
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-sent", 500000L),
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-received", 750000L),
                            new AdditionalMetric.KeywordMetric("keyword_llm-proxy_model", "custom-1")
                        )
                )
                .ignoreElements();

            var clientAsserts = send(client, "/custom-token-only");

            finalAssert(context, metricsAsserts, clientAsserts);
        }

        @Test
        void should_extract_tokens_usage_and_pricing(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            var metricsAsserts = metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .asInstanceOf(InstanceOfAssertFactories.SET)
                        .containsExactlyInAnyOrder(
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-sent", 500000L),
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-received", 750000L),
                            new AdditionalMetric.KeywordMetric("keyword_llm-proxy_model", "custom-1"),
                            new AdditionalMetric.DoubleMetric("double_llm-proxy_sent-cost", 0.2),
                            new AdditionalMetric.DoubleMetric("double_llm-proxy_received-cost", 0.6)
                        )
                )
                .ignoreElements();

            var clientAsserts = send(client, "/custom-token-and-pricing");

            finalAssert(context, metricsAsserts, clientAsserts);
        }
    }

    @Nested
    @GatewayTest
    @DeployApi({ "/apis/custom-without-model.json" })
    class CustomWithoutModel extends AbstractAiTokenTrackPolicyIntegrationTest {

        @Test
        void should_extract_tokens_usage_and_pricing_event_dont_find_model(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            var metricsAsserts = metricsSubject
                .map(metrics ->
                    assertThat(metrics)
                        .extracting(Metrics::getAdditionalMetrics)
                        .asInstanceOf(InstanceOfAssertFactories.SET)
                        .containsExactlyInAnyOrder(
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-sent", 500000L),
                            new AdditionalMetric.LongMetric("long_llm-proxy_tokens-received", 750000L),
                            new AdditionalMetric.DoubleMetric("double_llm-proxy_sent-cost", 0.2),
                            new AdditionalMetric.DoubleMetric("double_llm-proxy_received-cost", 0.6)
                        )
                )
                .ignoreElements();

            var clientAsserts = send(client, "/without-model");

            finalAssert(context, metricsAsserts, clientAsserts);
        }
    }

    @Nested
    @GatewayTest
    @DeployApi({ "/apis/claude-token-and-pricing-bad-el.json" })
    class BadConfig extends AbstractAiTokenTrackPolicyIntegrationTest {

        @Test
        void should_extract_tokens_usage_and_pricing_event_dont_find_model(HttpClient client, VertxTestContext context) {
            stubBackend(wiremock);

            var metricsAsserts = metricsSubject
                .doOnNext(metrics ->
                    assertThat(metrics.getAdditionalMetrics())
                        .extracting(AdditionalMetric::name)
                        .doesNotContain(
                            "long_llm-proxy_tokens-sent",
                            "long_llm-proxy_tokens-received",
                            "double_llm-proxy_sent-cost",
                            "double_llm-proxy_received-cost"
                        )
                )
                .ignoreElements();

            var clientAsserts = send(client, "/gpt-token-and-pricing");

            finalAssert(context, metricsAsserts, clientAsserts);
        }
    }

    private static void finalAssert(VertxTestContext context, Completable metricsAsserts, Completable clientAsserts) {
        Completable
            .mergeArray(metricsAsserts, clientAsserts)
            .doOnComplete(context::completeNow)
            .doFinally(context::completeNow)
            .doOnTerminate(context::completeNow)
            .doOnEvent(e -> {
                if (e == null) {
                    context.completeNow();
                } else {
                    context.failNow(e);
                }
            })
            .subscribe();
    }
}
