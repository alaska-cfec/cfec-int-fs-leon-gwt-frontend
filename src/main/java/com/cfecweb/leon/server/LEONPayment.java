package com.cfecweb.leon.server;

public class LEONPayment {

	private String type = "C1";
	private String user = "LEONCC";
	private String rjnum = "11111111CC01";
	private String ccnum = "5121212121212124";
	private String month = "12";
	private String year = "22";
	private String ammount = "0.06";
	private String filler = "      ";
	private String confirm = "0119-123456-56789";
	private String authcode = null;
	private String status = null;

	public String getAuthcode() {
		return authcode;
	}

	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getRjnum() {
		return rjnum;
	}

	public void setRjnum(String rjnum) {
		this.rjnum = rjnum;
	}

	public String getCcnum() {
		return ccnum;
	}

	public void setCcnum(String ccnum) {
		this.ccnum = ccnum;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getAmmount() {
		return ammount;
	}

	public void setAmmount(String ammount) {
		this.ammount = ammount;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

}
