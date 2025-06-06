package com.eodb.eodb_tariff.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user_master")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
	
	@Id
    private int userId;
	
	private String userName;
	
	private String userPassword;
	
	private String employeeName;
	
	private  int roleId;
	
	private int isPasswordChanged;
	
	
	
	
		
	@ManyToOne
	@JoinColumn(name = "officeId")
	private OfficeModel office;
	

}
