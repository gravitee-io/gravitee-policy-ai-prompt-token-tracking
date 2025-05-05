package com.graviteesource.policy.ai.token.track.utils;

public sealed interface Tokens<T extends Number> {
    T input();
    T output();

    record TokensOnly<U extends Number>(U input, U output) implements Tokens<U> {}

    record TokensAndModel<U extends Number>(U input, U output, String model) implements Tokens<U> {}
}
