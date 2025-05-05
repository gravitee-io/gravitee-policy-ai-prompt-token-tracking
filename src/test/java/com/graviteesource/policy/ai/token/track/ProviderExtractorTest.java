package com.graviteesource.policy.ai.token.track;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.graviteesource.policy.ai.token.track.utils.Tokens;
import org.junit.jupiter.api.Test;

class ProviderExtractorTest {

    public static final JsonMapper JSON_MAPPER = JsonMapper.builder().build();

    @Test
    void extract() throws JsonProcessingException {
        ProviderExtractor extractor = new ProviderExtractor.ProviderExtractorTokenAndModel("/in", "/out", "/model");
        JsonNode jsonNode = JSON_MAPPER.readValue(
            """
                {
                "in": 12,
                "out": 13,
                "model": "skynet"
                }
                """,
            JsonNode.class
        );

        var result = extractor.apply(jsonNode);

        var skynet = new Tokens.TokensAndModel<>(12L, 13L, "skynet");
        assertThat(result).contains(skynet);
    }

    @Test
    void extractFail() throws JsonProcessingException {
        ProviderExtractor extractor = new ProviderExtractor.ProviderExtractorTokenAndModel("/not", "/out", "/model");
        JsonNode jsonNode = JSON_MAPPER.readValue(
            """
                {
                "in": 12,
                "out": 13
                }
                """,
            JsonNode.class
        );

        var result = extractor.apply(jsonNode);

        assertThat(result).isEmpty();
    }
}
