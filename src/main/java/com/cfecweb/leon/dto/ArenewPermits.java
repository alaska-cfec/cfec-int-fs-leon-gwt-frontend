package com.cfecweb.leon.dto;

import com.extjs.gxt.ui.client.data.BeanModelTag;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class ArenewPermits implements BeanModelTag, IsSerializable, Comparable<ArenewPermits> {

	private ArenewPermitsId id;
	//private ArenewEntity arenewEntity;
	//private ArenewPayment arenewPayment;
	private String adfg;
	private String ofee;
	private String fee;	
	private String opfee;
	private String pfee;
	private String intends;
	private String nointends;
	private String msna;
	private String mpmt;
	private String notes;
	private Date receiveddate;
	private String confirmcode;
	private String description;
	private String status;
	private String type;
	private Date lastupdated;
	private boolean newpermit;
	private boolean renewed;
	private boolean povertyfee;
	private boolean reducedfee;
	private boolean halibut;
	private boolean sablefish;
	private boolean nointend;
	private boolean intend;
	private boolean already;
	private String intendString;
	private boolean newrenew;
	private String vlicensed;

	public ArenewPermits() {
	}

	public ArenewPermits(ArenewPermitsId id, /*ArenewEntity arenewEntity,
                         ArenewPayment arenewPayment,*/ String fee, Date receiveddate,
                         String description, String pfee) {
		this.id = id;
		//this.arenewEntity = arenewEntity;
		//this.arenewPayment = arenewPayment;
		this.fee = fee;
		this.receiveddate = receiveddate;
		this.description = description;
		this.pfee = pfee;
	}

	public ArenewPermits(ArenewPermitsId id, /*ArenewEntity arenewEntity,
                         ArenewPayment arenewPayment,*/ String adfg, String fee, String confirmcode,
                         boolean intend, String msna, Date receiveddate, String description,
                         String status, Date lastupdated, boolean nointend, String pfee) {
		this.id = id;
		//this.arenewEntity = arenewEntity;
		//this.arenewPayment = arenewPayment;
		this.adfg = adfg;
		this.fee = fee;
		this.intend = intend;
		this.msna = msna;
		this.receiveddate = receiveddate;
		this.description = description;
		this.status = status;
		this.lastupdated = lastupdated;
		this.nointend = nointend;
		this.confirmcode = confirmcode;
		this.pfee = pfee;
	}

	public ArenewPermitsId getId() {
		return this.id;
	}

	public void setId(ArenewPermitsId id) {
		this.id = id;
	}

	/*public ArenewEntity getArenewEntity() {
		return this.arenewEntity;
	}

	public void setArenewEntity(ArenewEntity arenewEntity) {
		this.arenewEntity = arenewEntity;
	}

	public ArenewPayment getArenewPayment() {
		return this.arenewPayment;
	}

	public void setArenewPayment(ArenewPayment arenewPayment) {
		this.arenewPayment = arenewPayment;
	}*/

	public String getAdfg() {
		return this.adfg;
	}

	public void setAdfg(String adfg) {
		this.adfg = adfg;
	}

	public String getMsna() {
		return this.msna;
	}

	public void setMsna(String msna) {
		this.msna = msna;
	}

	public Date getReceiveddate() {
		return this.receiveddate;
	}

	public void setReceiveddate(Date receiveddate) {
		this.receiveddate = receiveddate;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getLastupdated() {
		return this.lastupdated;
	}

	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}

	public String getOfee() {
		return ofee;
	}

	public void setOfee(String ofee) {
		this.ofee = ofee;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getConfirmcode() {
		return confirmcode;
	}

	public void setConfirmcode(String confirmcode) {
		this.confirmcode = confirmcode;
	}

	public boolean isNewpermit() {
		return newpermit;
	}

	public void setNewpermit(boolean newpermit) {
		this.newpermit = newpermit;
	}

	public boolean isRenewed() {
		return renewed;
	}

	public void setRenewed(boolean renewed) {
		this.renewed = renewed;
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

	public boolean isAlready() {
		return already;
	}

	public void setAlready(boolean already) {
		this.already = already;
	}

	public boolean isIntend() {
		return intend;
	}

	public void setIntend(boolean intend) {
		this.intend = intend;
	}

	public boolean isNointend() {
		return nointend;
	}

	public void setNointend(boolean nointend) {
		this.nointend = nointend;
	}

	public String getIntends() {
		return intends;
	}

	public void setIntends(String intends) {
		this.intends = intends;
	}

	public String getNointends() {
		return nointends;
	}

	public void setNointends(String nointends) {
		this.nointends = nointends;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getMpmt() {
		return mpmt;
	}

	public void setMpmt(String mpmt) {
		this.mpmt = mpmt;
	}

	public String getPfee() {
		return pfee;
	}

	public void setPfee(String pfee) {
		this.pfee = pfee;
	}

	public String getOpfee() {
		return opfee;
	}

	public void setOpfee(String opfee) {
		this.opfee = opfee;
	}

	public String getIntendString() {
		return intendString;
	}

	public void setIntendString(String intendString) {
		this.intendString = intendString;
	}

	public boolean isNewrenew() {
		return newrenew;
	}

	public void setNewrenew(boolean newrenew) {
		this.newrenew = newrenew;
	}
	
	public String getVlicensed() {
		return vlicensed;
	}

	public void setVlicensed(String vlicensed) {
		this.vlicensed = vlicensed;
	}

	public int compareTo(ArenewPermits pmt) {
		try {
		/*
		 * This is a sort feature for return permit list. It compares Fisheries first, 
		 * then Permit Year, and organizes the list to be easier to read.
		 */
		
		if (this.getId().getFishery().equalsIgnoreCase(pmt.getId().getFishery())) {
			return this.getId().getPyear().compareTo(pmt.getId().getPyear());
		} else {
			return this.getId().getFishery().compareTo(pmt.getId().getFishery());
		}
		
		//return this.getId().getFishery().compareTo(pmt.getId().getFishery());
		} catch (Exception e) {
			/*Print out error message to Stout because we have no logging*/
			//System.out.println("Sorry Ty no logging in BbPermit.java ");
			e.printStackTrace();
			//System.out.println(bb);
			System.out.println(this);
		}
		return 0;
	}
}
