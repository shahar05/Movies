package com.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository

public interface OrderRepository extends JpaRepository<Order, Long> {

   Optional< List<Order>> findByPrice(Double price);

}
