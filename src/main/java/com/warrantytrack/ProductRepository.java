
package com.warrantytrack;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByOwner_Id(Long ownerId);

    Optional<Product> findByIdAndOwner_Id(Long id, Long ownerId);
}
