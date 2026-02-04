package com.cfecweb.leon.server;

import com.cfecweb.leon.AppProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class RecaptchaVerifier {
    private static final URI VERIFY_URI = URI.create(AppProperties.get(AppProperties.RECAPTCHA_VERIFIER_URL));
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public VerificationResult verifyV3(
            String secretKey,
            String captchaToken,
            String expectedAction,       // e.g. "submit", "pay"
            String expectedHostname,     // e.g. "example.com"
            Double minScore,             // e.g. 0.5
            String remoteIp              // optional
    ) throws Exception {
        String body = "secret=" + url(secretKey) + "&response=" + url(captchaToken);

        if (remoteIp != null && !remoteIp.isBlank()) {
            body += "&remoteip=" + url(remoteIp); // optional
        }

        HttpRequest req = HttpRequest.newBuilder(VERIFY_URI)
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        JsonNode json = OBJECT_MAPPER.readTree(resp.body());

        boolean success = json.path("success").asBoolean(false);
        String hostname = json.path("hostname").asText(null);

        // v3 fields may appear depending on key type/config; treat missing defensively.
        String action = json.path("action").asText(null);
        double score = json.path("score").isNumber() ? json.path("score").asDouble() : -1.0;

        // Collect error codes (if present)
        String errorCodes = json.has("error-codes") ? json.get("error-codes").toString() : null;

        // Basic validations
        if (!success) {
            return VerificationResult.fail("recaptcha_failed", errorCodes);
        }
        if (expectedHostname != null && hostname != null && !hostname.equalsIgnoreCase(expectedHostname)) {
            return VerificationResult.fail("hostname_mismatch", "got=" + hostname);
        }

        // v3 action validation (recommended usage pattern)
        if (expectedAction != null && action != null && !expectedAction.equals(action)) {
            return VerificationResult.fail("action_mismatch", "expected=" + expectedAction + ", got=" + action);
        }

        // v3 score thresholding (v3 is score-based)
        if (minScore != null && score >= 0.0 && score < minScore) {
            return VerificationResult.fail("low_score", "score=" + score);
        }

        return VerificationResult.ok(score, action, hostname);
    }

    private static String url(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    public record VerificationResult(boolean ok, String reason, String details,
                                     double score, String action, String hostname) {
        public static VerificationResult ok(double score, String action, String hostname) {
            return new VerificationResult(true, null, null, score, action, hostname);
        }
        public static VerificationResult fail(String reason, String details) {
            return new VerificationResult(false, reason, details, -1.0, null, null);
        }
    }
}