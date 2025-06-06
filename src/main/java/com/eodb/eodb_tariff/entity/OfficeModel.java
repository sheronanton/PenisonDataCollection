package com.eodb.eodb_tariff.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="office_master")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficeModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "office_id")
	private Long officeId ;
	
	private int regionId ;
	
	private String regionName ;
	
	private int circleId ;
	
	private String circleName ;
	
	private String officeName ;
	
	private String officeLevelId;

}
