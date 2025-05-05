package com.graviteesource.policy.ai.token.track.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import com.graviteesource.policy.ai.token.track.utils.Tokens;
import org.junit.jupiter.api.Test;

class AiTokenTrackingConfigurationTest {

    @Test
    void testCalculatePricing() {
        AiTokenTrackingConfiguration sut = new AiTokenTrackingConfiguration();
        sut.setPricing(buildPricingConfiguration(2));

        var cost = sut.getCost(new Tokens.TokensAndModel<>(12L, 12L, "skynet"));

        assertThat(cost).map(Tokens::input).contains(24D);
        assertThat(cost).map(Tokens::output).contains(6D);
    }

    private static PricingConfiguration buildPricingConfiguration(long factor) {
        return new PricingConfiguration(factor, 1, 1, factor);
    }
}
