/*
 * BookingServiceType.java
 * 
 * This enum represents the different types of booking services available. It is used in the Booking model to specify the service type for a booking.
 */

package edu.log.models.booking.enums;

//https://www.shopify.com/blog/types-of-shipping#1
public enum BookingServiceType {
    EXPEDITED,
    OVERNIGHT,
    PRIORITY,
    FLAT_RATE,
    ECONOMY,
    LOCAL,
    INTERNATIONAL
}