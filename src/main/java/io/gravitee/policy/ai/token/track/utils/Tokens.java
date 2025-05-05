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
package io.gravitee.policy.ai.token.track.utils;

public sealed interface Tokens<T extends Number> {
    T input();
    T output();

    record TokensOnly<U extends Number>(U input, U output) implements Tokens<U> {}

    record TokensAndModel<U extends Number>(U input, U output, String model) implements Tokens<U> {}

    static <U extends Number> Tokens<U> of(U input, U output, String model) {
        return model == null || model.isBlank() ? new TokensOnly<>(input, output) : new TokensAndModel<>(input, output, model);
    }
}
