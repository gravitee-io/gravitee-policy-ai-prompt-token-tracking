package com.graviteesource.policy.ai.token.track.configuration;

import com.graviteesource.policy.ai.token.track.ProviderExtractor;
import com.graviteesource.policy.ai.token.track.utils.Tokens;
import io.gravitee.policy.api.PolicyConfiguration;
import java.util.Optional;

public class AiTokenTrackingConfiguration implements PolicyConfiguration {

    private ProviderExtractorConfiguration extraction;

    private PricingConfiguration pricing;

    public void setExtraction(ProviderExtractorConfiguration extraction) {
        this.extraction = extraction;
    }

    public void setPricing(PricingConfiguration pricing) {
        this.pricing = pricing;
    }

    public ProviderExtractor toProviderExtractor() {
        return extraction.toProviderExtractor();
    }

    public Optional<Tokens<Double>> getCost(Tokens<Long> tokens) {
        return pricing == null ? Optional.empty() : Optional.of(pricing.cost(tokens));
    }
}
