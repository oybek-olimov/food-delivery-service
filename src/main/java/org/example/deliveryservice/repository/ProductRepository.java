package org.example.deliveryservice.repository;

import org.example.deliveryservice.entity.Category;
import org.example.deliveryservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);

    List<Product> findByProductNameContainingIgnoreCase(String name);

//    Optional<Product> findByProductName(String name);

//    @Override
//    @Query(value = "select p. , p.description , p.price , p.ingredients , p.image , p.discount  from Product p")
//    List<Product> findAll2();
}
