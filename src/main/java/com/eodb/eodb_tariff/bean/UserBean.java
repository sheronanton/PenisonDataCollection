package com.eodb.eodb_tariff.bean;

public class UserBean {
	
	
		private int userId;
		
		private String userName;
		
		private String userPassword;
		
		private String employeeName;
		
		private  int roleId;
		
		private int isPasswordChanged;
		
		private String oldPassword;
		

		public int getIsPasswordChanged() {
			return isPasswordChanged;
		}

		public void setIsPasswordChanged(int isPasswordChanged) {
			this.isPasswordChanged = isPasswordChanged;
		}

		public int getRoleId() {
			return roleId;
		}

		public void setRoleId(int roleId) {
			this.roleId = roleId;
		}

		private OfficeBean office;
		
		
			

		public OfficeBean getOffice() {
			return office;
		}

		public void setOffice(OfficeBean office) {
			this.office = office;
		}

	public String getUserName() {
		 return userName ;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	
	

}
