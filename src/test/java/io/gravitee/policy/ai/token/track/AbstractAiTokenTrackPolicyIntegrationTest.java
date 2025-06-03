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

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.gravitee.apim.gateway.tests.sdk.AbstractPolicyTest;
import io.gravitee.apim.gateway.tests.sdk.connector.EndpointBuilder;
import io.gravitee.apim.gateway.tests.sdk.connector.EntrypointBuilder;
import io.gravitee.apim.gateway.tests.sdk.reporter.FakeReporter;
import io.gravitee.plugin.endpoint.EndpointConnectorPlugin;
import io.gravitee.plugin.endpoint.http.proxy.HttpProxyEndpointConnectorFactory;
import io.gravitee.plugin.entrypoint.EntrypointConnectorPlugin;
import io.gravitee.plugin.entrypoint.http.proxy.HttpProxyEntrypointConnectorFactory;
import io.gravitee.policy.ai.token.track.configuration.AiTokenTrackingConfiguration;
import io.gravitee.reporter.api.v4.metric.Metrics;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.vertx.core.http.HttpMethod;
import io.vertx.rxjava3.core.http.HttpClient;
import io.vertx.rxjava3.core.http.HttpClientRequest;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;

class AbstractAiTokenTrackPolicyIntegrationTest extends AbstractPolicyTest<AiTokenTrackPolicy, AiTokenTrackingConfiguration> {

    private static final String GPT_RESPONSE =
        """
        {
            "id": "chatcmpl-7e1a2d4f-0b3c-4c5e-bb8f-6a2d9e3f1a2d",
            "model": "gpt-4",
            "usage": {
                "prompt_tokens": 500000,
                "completion_tokens": 750000
            }
        }
        """;

    private static final String GEMINI_RESPONSE =
        """
        {
            "id": "chatcmpl-7e1a2d4f-0b3c-4c5e-bb8f-6a2d9e3f1a2d",
            "model": "gemini-2",
            "usage": {
                "input_tokens": 500000,
                "output_tokens": 750000
            }
        }
        """;

    private static final String CLAUDE_RESPONSE =
        """
        {
            "id": "chatcmpl-7e1a2d4f-0b3c-4c5e-bb8f-6a2d9e3f1a2d",
            "modelVersion": "claude-3",
            "usageMetadata": {
                "promptTokenCount": 500000,
                "candidatesTokenCount": 750000
            }
        }
        """;

    private static final String MISTRAL_RESPONSE =
        """
        {
            "id": "chatcmpl-7e1a2d4f-0b3c-4c5e-bb8f-6a2d9e3f1a2d",
            "model": "mistral-1",
            "usage": {
                "prompt_tokens": 500000,
                "completion_tokens": 750000
            }
        }
        """;

    private static final String CUSTOM_RESPONSE =
        """
        {
            "id": "chatcmpl-7e1a2d4f-0b3c-4c5e-bb8f-6a2d9e3f1a2d",
            "custom_model": "custom-1",
            "usage": {
                "custom_prompt_tokens": 500000,
                "custom_completion_tokens": 750000
            }
        }
        """;

    private static final String CUSTOM_RESPONSE_WITHOUT_MODEL =
        """
            {
                "id": "chatcmpl-7e1a2d4f-0b3c-4c5e-bb8f-6a2d9e3f1a2d",
                "usage": {
                    "custom_prompt_tokens": 500000,
                    "custom_completion_tokens": 750000
                }
            }
            """;

    BehaviorSubject<Metrics> metricsSubject;

    @BeforeEach
    void setUp() {
        metricsSubject = BehaviorSubject.create();

        FakeReporter fakeReporter = getBean(FakeReporter.class);
        fakeReporter.setReportableHandler(reportable -> {
            if (reportable instanceof Metrics metrics) {
                metricsSubject.onNext(metrics);
            }
        });
    }

    protected void stubBackend(WireMockServer wiremock) {
        wiremock.stubFor(WireMock.post("/gpt").willReturn(WireMock.jsonResponse(GPT_RESPONSE, 200)));
        wiremock.stubFor(WireMock.post("/gemini").willReturn(WireMock.jsonResponse(GEMINI_RESPONSE, 200)));
        wiremock.stubFor(WireMock.post("/claude").willReturn(WireMock.jsonResponse(CLAUDE_RESPONSE, 200)));
        wiremock.stubFor(WireMock.post("/mistral").willReturn(WireMock.jsonResponse(MISTRAL_RESPONSE, 200)));
        wiremock.stubFor(WireMock.post("/custom").willReturn(WireMock.jsonResponse(CUSTOM_RESPONSE, 200)));
        wiremock.stubFor(WireMock.post("/without-model").willReturn(WireMock.jsonResponse(CUSTOM_RESPONSE_WITHOUT_MODEL, 200)));
    }

    protected void send(HttpClient client, String path) {
        client
            .rxRequest(HttpMethod.POST, path)
            .flatMap(HttpClientRequest::rxSend)
            .doOnSuccess(response -> assertThat(response.statusCode()).isEqualTo(200))
            .subscribe();
    }

    @Override
    public void configureEntrypoints(Map<String, EntrypointConnectorPlugin<?, ?>> entrypoints) {
        entrypoints.putIfAbsent("http-proxy", EntrypointBuilder.build("http-proxy", HttpProxyEntrypointConnectorFactory.class));
    }

    @Override
    public void configureEndpoints(Map<String, EndpointConnectorPlugin<?, ?>> endpoints) {
        endpoints.putIfAbsent("http-proxy", EndpointBuilder.build("http-proxy", HttpProxyEndpointConnectorFactory.class));
    }
}
