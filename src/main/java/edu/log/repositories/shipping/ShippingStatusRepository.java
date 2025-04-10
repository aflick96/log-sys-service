/*
 * ShippingStatusRepository.java
 * 
 * This repository interface provides methods to perform CRUD operations on ShippingStatus entities. It extends JpaRepository to leverage Spring Data JPA's capabilities for database interactions.
 */

package edu.log.repositories.shipping;

import edu.log.models.shipping.ShippingStatus;
import edu.log.models.shipping.enums.ShippingStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShippingStatusRepository extends JpaRepository<ShippingStatus, Long> {
    ShippingStatus findByBooking_Id(Long bookingId);
    List<ShippingStatus> findByStatus(ShippingStatusType status);
    List<ShippingStatus> findByWarehouse_Id(Long warehouseId);    
}
