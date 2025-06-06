package com.twad.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DCBCollection {
	private String regionName;
	private String circleName;
	private String divisionName;
	private int arrearWc;
	private int arrearInt;
	private int arrear;
	private int demandWc;
	private int demandInt;
	private int demand;
	private int totalDue;
	private int colnWc;
	private int colnInt;
	private int coln;
	private int balanceDue;
	private int percentageOfColnToDemand;
	private int percentageOfColnToDue;

	// Constructor, getters, and setters
	public DCBCollection(String regionName, String circleName, String divisionName, int arrearWc, int arrearInt,
			int arrear, int demandWc, int demandInt, int demand, int totalDue, int colnWc,
			int colnInt, int coln, int balanceDue, int percentageOfColnToDemand,
			int percentageOfColnToDue) {
		this.regionName = regionName;
		this.circleName = circleName;
		this.divisionName = divisionName;
		this.arrearWc = arrearWc;
		this.arrearInt = arrearInt;
		this.arrear = arrear;
		this.demandWc = demandWc;
		this.demandInt = demandInt;
		this.demand = demand;
		this.totalDue = totalDue;
		this.colnWc = colnWc;
		this.colnInt = colnInt;
		this.coln = coln;
		this.balanceDue = balanceDue;
		this.percentageOfColnToDemand = percentageOfColnToDemand;
		this.percentageOfColnToDue = percentageOfColnToDue;
	}

	public DCBCollection() {
		// TODO Auto-generated constructor stub
	}

	// Getters and setters for each field
	// ...

}
