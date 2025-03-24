package edu.log.utils;

import edu.log.models.booking.Booking;
import edu.log.models.booking.enums.BookingServiceType;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BookingCalculator {

    public static double computePrice(Booking booking) {
        double baseRate = 50; // Base price for any booking
        double volumeFactor = 5 * booking.getVolume();
        double weightFactor = 3 * booking.getWeight();
        double distanceFactor = 0.5 * convertMetersToMiles(booking.getDistance());

        // Apply service type multipliers
        double serviceMultiplier = getServiceMultiplier(booking.getServiceType());

        // Compute final price
        double finalPrice = (baseRate + volumeFactor + weightFactor + distanceFactor) * serviceMultiplier;

        // Round to 2 decimal places
        return roundToTwoDecimals(finalPrice);
    }

    public static int computeEstimatedDeliveryTime(Booking booking) {
        double baseTime = (booking.getDistance() / 1000.0) + 1;
        double estimatedHours = baseTime * getDeliveryTimeMultiplier(booking.getServiceType());
        int estimatedDays = (int) Math.ceil(estimatedHours / 24.0);
    
        switch (booking.getServiceType()) {
            case OVERNIGHT:
            case LOCAL:
                return 1;
            case EXPEDITED:
                return Math.min(estimatedDays, 2);
            case PRIORITY:
                return Math.min(estimatedDays, 3);
            case FLAT_RATE:
            case ECONOMY:
            case INTERNATIONAL:
                return Math.min(estimatedDays, 5);
            default:
                return estimatedDays;
        }
    }
    

    public static double convertMetersToMiles(double meters) {
        return meters * 0.000621371;
    }

    private static double getServiceMultiplier(BookingServiceType type) {
        switch (type) {
            case EXPEDITED: return 1.5;
            case OVERNIGHT: return 2.0;
            case PRIORITY: return 1.8;
            case FLAT_RATE: return 1.0;
            case ECONOMY: return 0.8;
            case LOCAL: return 0.6;
            case INTERNATIONAL: return 2.5;
            default: return 1.0;
        }
    }

    private static double getDeliveryTimeMultiplier(BookingServiceType type) {
        switch (type) {
            case EXPEDITED: return 0.5; // Faster
            case OVERNIGHT: return 0.2; // Should be at most 1 day
            case PRIORITY: return 0.6;
            case FLAT_RATE: return 1.0;
            case ECONOMY: return 1.5;
            case LOCAL: return 0.5;
            case INTERNATIONAL: return 2.0;
            default: return 1.0;
        }
    }

    private static double roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
