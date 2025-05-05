package com.graviteesource.policy.ai.token.track.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import com.graviteesource.policy.ai.token.track.utils.Tokens;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PricingConfigurationTest {

    PricingConfiguration pricingConfiguration = new PricingConfiguration(0.40, 1000000, 8, 1000000);

    @ParameterizedTest
    @CsvSource(value = { "1000000,1000000,.4,8", "100,1000,.00004,.008" })
    void testModel(long inputTokens, long outputTokens, double expectedInputPrice, double expectedOutputPrice) {
        Tokens<Double> cost = pricingConfiguration.cost(new Tokens.TokensOnly<>(inputTokens, outputTokens));

        assertThat(cost).isEqualTo(new Tokens.TokensOnly<>(expectedInputPrice, expectedOutputPrice));
    }
}
