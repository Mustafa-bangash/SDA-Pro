package com.sdapro.response.strategy;
import java.util.List;
import java.util.Arrays;

public class AggressiveContainmentStrategy implements ResponseStrategy {
    @Override
    public List<String> determineActions(String incidentSeverity) {
        return Arrays.asList("BlockIP", "IsolateEndpoint");
    }
}
