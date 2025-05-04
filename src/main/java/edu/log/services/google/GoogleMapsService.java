/*
 * GoogleMapsService.java
 * 
 * This service class interacts with the Google Maps API to calculate the distance between two addresses. It uses RestTemplate to make HTTP requests and processes the JSON response to extract the distance in meters.
 */

package edu.log.services.google;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;
import java.util.Objects;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.net.URLEncoder;

@Service
public class GoogleMapsService {
    @Value("${google.maps.api.key}")
    private String googleApiKey;

    private final RestTemplate rt = new RestTemplate();

    // Get distance between two addresses and return in meters
    public double getDistance(String fromAddress, String toAddress) {
        String url = "https://routes.googleapis.com/directions/v2:computeRoutes";
        String jsonBody = "{\n" +
                "  \"origin\": { \"address\": \"" + fromAddress + "\" },\n" +
                "  \"destination\": { \"address\": \"" + toAddress + "\" },\n" +
                "  \"travelMode\": \"DRIVE\"\n" +
                "}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", googleApiKey);
        headers.set("X-Goog-FieldMask", "routes.distanceMeters");

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<Map<String, Object>> response = rt.exchange(url, HttpMethod.POST, requestEntity,
                (Class<Map<String, Object>>) (Class<?>) Map.class);

        Map<String, Object> responseBody = response.getBody();
        if (Objects.nonNull(responseBody) && responseBody.get("routes") instanceof List) {
            List<?> rawRoutes = (List<?>) responseBody.get("routes");
            if (!rawRoutes.isEmpty() && rawRoutes.get(0) instanceof Map) {
                Map<?, ?> firstRoute = (Map<?, ?>) rawRoutes.get(0);
                if (firstRoute.containsKey("distanceMeters") && firstRoute.get("distanceMeters") instanceof Number) {
                    return ((Number) firstRoute.get("distanceMeters")).doubleValue();
                }
            }
        }
        return 0.0;
    }

    // Validate address using Google Maps API
    public boolean isAddressValid(String address) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                URLEncoder.encode(address, StandardCharsets.UTF_8) +
                "&key=" + googleApiKey;

        Map<String, Object> response = rt.getForObject(url, Map.class);

        if (response == null || !"OK".equals(response.get("status"))) {
            return false;
        }

        List<?> results = (List<?>) response.get("results");
        return results != null && !results.isEmpty();
    }
}
