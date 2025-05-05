package com.graviteesource.policy.ai.token.track.configuration;

import com.graviteesource.policy.ai.token.track.utils.Tokens;

public class PricingConfiguration {

    private double inputPriceValue;
    private double inputPriceUnit;
    private double outputPriceValue;
    private double outputPriceUnit;

    public PricingConfiguration() {}

    public PricingConfiguration(double inputPriceValue, double inputPriceUnit, double outputPriceValue, double outputPriceUnit) {
        this.inputPriceValue = inputPriceValue;
        this.inputPriceUnit = inputPriceUnit;
        this.outputPriceValue = outputPriceValue;
        this.outputPriceUnit = outputPriceUnit;
    }

    public void setInputPriceValue(double inputPriceValue) {
        this.inputPriceValue = inputPriceValue;
    }

    public void setInputPriceUnit(double inputPriceUnit) {
        this.inputPriceUnit = inputPriceUnit;
    }

    public void setOutputPriceValue(double outputPriceValue) {
        this.outputPriceValue = outputPriceValue;
    }

    public void setOutputPriceUnit(double outputPriceUnit) {
        this.outputPriceUnit = outputPriceUnit;
    }

    public Tokens<Double> cost(Tokens<Long> tokens) {
        return new Tokens.TokensOnly<>(
            tokens.input() * inputPriceValue / inputPriceUnit,
            tokens.output() * outputPriceValue / outputPriceUnit
        );
    }
}
