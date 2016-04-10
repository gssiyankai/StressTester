package com.gregory.testing.result;

import com.gregory.testing.message.TimestampedMessage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class RegexpMessageResolver implements MessageResolver {

    private final String requestRegexp;
    private final String requestReplacement;
    private final String responseRegexp;
    private final String responseReplacement;

    public RegexpMessageResolver(String requestRegexp, String requestReplacement, String responseRegexp, String responseReplacement) {
        this.requestRegexp = requestRegexp;
        this.requestReplacement = requestReplacement;
        this.responseRegexp = responseRegexp;
        this.responseReplacement = responseReplacement;
    }

    @Override
    public List<Communication> resolve(List<TimestampedMessage> requests, List<TimestampedMessage> responses) {
        List<Communication> communications = new ArrayList<>();
        Map<String, TimestampedMessage> requestsByKey = keys(requests, requestRegexp, requestReplacement);
        Map<String, TimestampedMessage> responsesByKey = keys(responses, responseRegexp, responseReplacement);
        for (String key : requestsByKey.keySet()) {
            TimestampedMessage request = requestsByKey.get(key);
            TimestampedMessage response = responsesByKey.get(key);
            if (response == null) {
                throw new RuntimeException("Failed to resolve request with key " + key);
            }
            communications.add(new Communication(request, response));
        }
        return communications;
    }

    private Map<String, TimestampedMessage> keys(List<TimestampedMessage> messages, String regex, String replacement) {
        Map<String, TimestampedMessage> result = new LinkedHashMap<>();
        for (TimestampedMessage message : messages) {
            String data = new String(message.message().data());
            String key = data.replaceAll(regex, replacement);
            result.put(key, message);
        }
        return result;
    }

}
