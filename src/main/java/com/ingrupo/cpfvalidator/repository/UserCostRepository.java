package com.ingrupo.cpfvalidator.repository;

import com.ingrupo.cpfvalidator.models.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCostRepository extends JpaRepository<Requisition, Integer> {
}
