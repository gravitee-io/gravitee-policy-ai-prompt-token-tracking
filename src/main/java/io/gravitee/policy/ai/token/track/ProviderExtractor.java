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

import io.gravitee.gateway.reactive.api.context.http.HttpPlainExecutionContext;
import io.gravitee.policy.ai.token.track.utils.Tokens;
import io.reactivex.rxjava3.core.Maybe;
import java.util.function.Function;

public sealed interface ProviderExtractor extends Function<HttpPlainExecutionContext, Maybe<Tokens<Long>>> {
    record ProviderExtractorTokenOnly(String inputTokenPtr, String outputTokenPtr) implements ProviderExtractor {
        @Override
        public Maybe<Tokens<Long>> apply(HttpPlainExecutionContext ctx) {
            var in = ctx.getTemplateEngine().eval(inputTokenPtr(), Long.class);
            var out = ctx.getTemplateEngine().eval(outputTokenPtr(), Long.class);
            return Maybe.zip(in, out, Tokens.TokensOnly::new);
        }
    }

    record ProviderExtractorTokenAndModel(String inputTokenPtr, String outputTokenPtr, String modelPtr) implements ProviderExtractor {
        @Override
        public Maybe<Tokens<Long>> apply(HttpPlainExecutionContext ctx) {
            var in = ctx.getTemplateEngine().eval(inputTokenPtr(), Long.class);
            var out = ctx.getTemplateEngine().eval(outputTokenPtr(), Long.class);
            var model = ctx.getTemplateEngine().eval(modelPtr(), String.class).defaultIfEmpty("").toMaybe();
            return Maybe.zip(in, out, model, Tokens::of);
        }
    }
}
