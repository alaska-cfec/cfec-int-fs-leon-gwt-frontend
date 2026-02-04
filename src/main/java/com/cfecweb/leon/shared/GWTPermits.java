package com.cfecweb.leon.shared;

import java.io.Serializable;
import java.util.Date;


public class GWTPermits implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cfecid;
	private String ryear;
	private String serial;
	private String pyear;
	private String fishery;	
	private String adfg;
	private double ofee;
	private double fee;
	private boolean intend;
	private String msna;
	private String mpmt;
	private Date receiveddate;
	private String confirmcode;
	private String description;
	private String operator;
	private String status;
	private Date lastupdated;
	private boolean newpermit;
	private String notes;
	private boolean renewed;
	private boolean povertyfee;
	private boolean reducedfee;
	private boolean halibut;
	private boolean sablefish;
	private boolean nointend;
	private boolean already;
	
	public GWTPermits(String cfecid, String ryear, String fishery, double ofee,
			String serial, String adfg, double fee, boolean intend, String msna,
			Date receiveddate, String confirmcode, String description,
			String operator, String status, String pyear, Date lastupdated, 
			boolean newpermit, String notes, boolean renewed, boolean reducedfee, 
			boolean nointend, boolean halibut, boolean sablefish, 
			boolean povertyfee, boolean already, String mpmt) {
		super();
		this.cfecid = cfecid;
		this.ryear = ryear;
		this.fishery = fishery;
		this.serial = serial;
		this.adfg = adfg;
		this.fee = fee;
		this.intend = intend;
		this.msna = msna;
		this.receiveddate = receiveddate;
		this.confirmcode = confirmcode;
		this.description = description;
		this.operator = operator;
		this.status = status;
		this.pyear = pyear;
		this.lastupdated = lastupdated;
		this.newpermit = newpermit;
		this.notes = notes;
		this.renewed = renewed;
		this.reducedfee = reducedfee;
		this.nointend = nointend;
		this.halibut = halibut;
		this.sablefish = sablefish;
		this.povertyfee = povertyfee;
		this.ofee = ofee;
		this.already = already;
		this.mpmt = mpmt;
	}
	
	public double getOfee() {
		return ofee;
	}

	public void setOfee(double ofee) {
		this.ofee = ofee;
	}

	public boolean isAlready() {
		return already;
	}

	public void setAlready(boolean already) {
		this.already = already;
	}

	public boolean isHalibut() {
		return halibut;
	}

	public void setHalibut(boolean halibut) {
		this.halibut = halibut;
	}

	public boolean isSablefish() {
		return sablefish;
	}

	public void setSablefish(boolean sablefish) {
		this.sablefish = sablefish;
	}

	public boolean isNointend() {
		return nointend;
	}

	public void setNointend(boolean nointend) {
		this.nointend = nointend;
	}

	public GWTPermits() {
		super();
	}

	public boolean isPovertyfee() {
		return povertyfee;
	}

	public void setPovertyfee(boolean povertyfee) {
		this.povertyfee = povertyfee;
	}

	public boolean isReducedfee() {
		return reducedfee;
	}

	public void setReducedfee(boolean reducedfee) {
		this.reducedfee = reducedfee;
	}

	public boolean getRenewed() {
		return renewed;
	}

	public void setRenewed(boolean renewed) {
		this.renewed = renewed;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getCfecid() {
		return cfecid;
	}

	public void setCfecid(String cfecid) {
		this.cfecid = cfecid;
	}

	public String getRyear() {
		return ryear;
	}

	public void setRyear(String ryear) {
		this.ryear = ryear;
	}

	public String getFishery() {
		return fishery;
	}

	public void setFishery(String fishery) {
		this.fishery = fishery;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getAdfg() {
		return adfg;
	}

	public void setAdfg(String adfg) {
		this.adfg = adfg;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public boolean getIntend() {
		return intend;
	}

	public void setIntend(boolean intend) {
		this.intend = intend;
	}

	public String getMsna() {
		return msna;
	}

	public void setMsna(String msna) {
		this.msna = msna;
	}

	public Date getReceiveddate() {
		return receiveddate;
	}

	public void setReceiveddate(Date receiveddate) {
		this.receiveddate = receiveddate;
	}

	public String getConfirmcode() {
		return confirmcode;
	}

	public void setConfirmcode(String confirmcode) {
		this.confirmcode = confirmcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPyear() {
		return pyear;
	}

	public void setPyear(String pyear) {
		this.pyear = pyear;
	}

	public Date getLastupdated() {
		return lastupdated;
	}

	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}

	public boolean isNewpermit() {
		return newpermit;
	}

	public void setNewpermit(boolean newpermit) {
		this.newpermit = newpermit;
	}

	public String getMpmt() {
		return mpmt;
	}

	public void setMpmt(String mpmt) {
		this.mpmt = mpmt;
	}
	
}

