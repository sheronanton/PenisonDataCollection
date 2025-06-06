package com.twad.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twad.entity.VerifyRuralDevData;

@Repository
public interface VerifyDataRepository extends JpaRepository<VerifyRuralDevData ,Long>{

}
