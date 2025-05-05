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

import static org.assertj.core.api.Assertions.assertThat;

import io.gravitee.policy.ai.token.track.utils.Tokens;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PricingConfigurationTest {

    PricingConfiguration pricingConfiguration = new PricingConfiguration(0.40, 1000000.0, 8.0, 1000000.0);

    @ParameterizedTest
    @CsvSource(value = { "1000000,1000000,.4,8", "100,1000,.00004,.008" })
    void testModel(long inputTokens, long outputTokens, double expectedInputPrice, double expectedOutputPrice) {
        Tokens<Double> cost = pricingConfiguration.cost(new Tokens.TokensOnly<>(inputTokens, outputTokens));

        assertThat(cost).isEqualTo(new Tokens.TokensOnly<>(expectedInputPrice, expectedOutputPrice));
    }
}
