package com.cfecweb.leon.shared;

import java.io.Serializable;

public class GWTfisheryTable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fishery;
	private String resfee;
	private String nonresfee;
	private String povfee;
	private String underfee;
	private String description;
	private String status;
	private String myfee;

	public GWTfisheryTable(String fishery, String resfee, String nonresfee,
			String povfee, String underfee, String description, String status, String myfee) {
		super();
		this.fishery = fishery;
		this.resfee = resfee;
		this.nonresfee = nonresfee;
		this.povfee = povfee;
		this.underfee = underfee;
		this.description = description;
		this.status = status;
		this.myfee = myfee;
	}
		
	public GWTfisheryTable() {
		super();
	}

	public String getFishery() {
		return fishery;
	}
	public void setFishery(String fishery) {
		this.fishery = fishery;
	}
	public String getResfee() {
		return resfee;
	}
	public void setResfee(String resfee) {
		this.resfee = resfee;
	}
	public String getNonresfee() {
		return nonresfee;
	}
	public void setNonresfee(String nonresfee) {
		this.nonresfee = nonresfee;
	}
	public String getPovfee() {
		return povfee;
	}
	public void setPovfee(String povfee) {
		this.povfee = povfee;
	}
	public String getUnderfee() {
		return underfee;
	}
	public void setUnderfee(String underfee) {
		this.underfee = underfee;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getMyfee() {
		return myfee;
	}

	public void setMyfee(String myfee) {
		this.myfee = myfee;
	}

	public String toString() {
		String thes = "Fishery: " + this.getFishery() + " Desc:  " + this.description + " Fee: " + this.getMyfee(); 
		return thes;
	}

}
