package com.graviteesource.policy.ai.token.track.configuration;

import com.graviteesource.policy.ai.token.track.ProviderExtractor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderExtractorConfiguration {

    private String inputTokenPointer;
    private String outputTokenPointer;
    private String modelPointer;

    private String gptInputTokenPointer;
    private String geminiInputTokenPointer;
    private String claudeInputTokenPointer;
    private String mistralInputTokenPointer;

    private String gptOutputTokenPointer;
    private String geminiOutputTokenPointer;
    private String claudeOutputTokenPointer;
    private String mistralOutputTokenPointer;

    private String gptModelPointer;
    private String geminiModelPointer;
    private String claudeModelPointer;
    private String mistralModelPointer;

    private String inputTokenPointer() {
        return firstNotNull(
            inputTokenPointer,
            gptInputTokenPointer,
            geminiInputTokenPointer,
            claudeInputTokenPointer,
            mistralInputTokenPointer
        );
    }

    private String outputTokenPointer() {
        return firstNotNull(
            outputTokenPointer,
            gptOutputTokenPointer,
            geminiOutputTokenPointer,
            claudeOutputTokenPointer,
            mistralOutputTokenPointer
        );
    }

    private String modelPointer() {
        return firstNotNull(modelPointer, gptModelPointer, geminiModelPointer, claudeModelPointer, mistralModelPointer);
    }

    public ProviderExtractor toProviderExtractor() {
        return modelPointer() == null || modelPointer().isBlank()
            ? new ProviderExtractor.ProviderExtractorTokenOnly(inputTokenPointer(), outputTokenPointer())
            : new ProviderExtractor.ProviderExtractorTokenAndModel(inputTokenPointer(), outputTokenPointer(), modelPointer());
    }

    private String firstNotNull(String p1, String p2, String p3, String p4, String p5) {
        if (p1 != null) {
            return p1;
        }
        if (p2 != null) {
            return p2;
        }
        if (p3 != null) {
            return p3;
        }
        if (p4 != null) {
            return p4;
        }
        return p5;
    }
}
