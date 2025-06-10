package com.twad.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.twad.entity.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {

	   @Query(
		        value = "SELECT * FROM user_master WHERE user_name = :userName AND :application = ANY(applications)",
		        nativeQuery = true
		    )
		    UserModel findByUserName(
		        @Param("userName") String userName,
		        @Param("application") String application
		    );
	@Query(value = "From UserModel where userName=:userName and userPassword=:userPassword")
	UserModel findByUserByNamePassword(@Param("userName") String userName, @Param("userPassword") String oldPassword);

}
