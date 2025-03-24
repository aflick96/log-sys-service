package edu.log.services.google;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;
import java.util.Objects;
import java.util.List;

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
        ResponseEntity<Map<String, Object>> response = rt.exchange(url, HttpMethod.POST, requestEntity, (Class<Map<String, Object>>)(Class<?>)Map.class);
        
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
}
