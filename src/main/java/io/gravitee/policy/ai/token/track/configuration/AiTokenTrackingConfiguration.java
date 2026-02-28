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

import io.gravitee.policy.ai.token.track.ProviderExtractor;
import io.gravitee.policy.ai.token.track.utils.Tokens;
import io.gravitee.policy.api.PolicyConfiguration;
import java.util.Optional;

public record AiTokenTrackingConfiguration(ProviderExtractorConfiguration extraction, PricingConfiguration pricing) implements
    PolicyConfiguration {
    public ProviderExtractor toProviderExtractor() {
        return extraction.toProviderExtractor();
    }

    public Optional<Tokens<Double>> getCost(Tokens<Long> tokens) {
        // Hard to write a simple schema form to have a null pricing configuration. So as a workaround we check that all
        // attributes are not null.
        return Optional.ofNullable(pricing)
            .filter(
                pricing ->
                    pricing.inputPriceValue() != null &&
                    pricing.inputPriceUnit() != null &&
                    pricing.outputPriceValue() != null &&
                    pricing.outputPriceUnit() != null
            )
            .map(pricing -> pricing.cost(tokens));
    }
}
