package com.eodb.eodb_tariff.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eodb.eodb_tariff.entity.VerifyRuralDevData;

@Repository
public interface VerifyDataRepository extends JpaRepository<VerifyRuralDevData ,Long>{

}
