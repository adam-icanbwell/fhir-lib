package com.bwell.fhir.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import java.util.function.Function;

public interface Parser {
    String CONTAINED = "contained";
    String RESOURCE_TYPE = "resourceType";
    ObjectMapper mapper = new ObjectMapper();

    static JsonNode changeJsonValue(JsonNode rootNode, String path, Function<JsonNode, String> function) {
        return ((ObjectNode) rootNode).put(path, function.apply(rootNode.findPath(path)));
    }

    @SneakyThrows
    static JsonNode findJsonNode(String json, String path) {
        JsonNode rootNode = mapper.readTree(json);

        return rootNode.findPath(path);
    }

    static JsonNode findJsonNode(JsonNode node, String path) {
        return node.findPath(path);
    }

    static JsonNode readTree(String json) throws JsonProcessingException {
        return mapper.readTree(json);
    }

    static ArrayNode createArrayNode() {
        return mapper.createArrayNode();
    }
}
