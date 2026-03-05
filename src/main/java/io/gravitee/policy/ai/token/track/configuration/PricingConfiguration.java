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

import io.gravitee.policy.ai.token.track.utils.Tokens;

public record PricingConfiguration(Double inputPriceValue, Double inputPriceUnit, Double outputPriceValue, Double outputPriceUnit) {
    public Tokens<Double> cost(Tokens<Long> tokens) {
        // inputPriceUnit & outputPriceUnit can't be 0 due to the schema.
        return new Tokens.TokensOnly<>(
            (tokens.input() * inputPriceValue) / inputPriceUnit,
            (tokens.output() * outputPriceValue) / outputPriceUnit
        );
    }
}
