package com.cfecweb.leon.shared;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class GWTChanges implements Serializable {
	private String seq;
	private String attribute;
	private String confirmcode;
	private ArenewEntity arenewEntity;
	private ArenewPayment arenewPayment;
	private String type;
	private String oldvalue;
	private String newvalue;
	private Date updatedate;
	private String object;
	private String cfecid;
	private String ryear;
	
	public GWTChanges() {
		super();
	}

	public GWTChanges(String seq, String attribute, String confirmcode,
			ArenewEntity arenewEntity, ArenewPayment arenewPayment,
			String type, String oldvalue, String newvalue, Date updatedate,
			String object, String cfecid, String ryear) {
		super();
		this.seq = seq;
		this.attribute = attribute;
		this.confirmcode = confirmcode;
		this.arenewEntity = arenewEntity;
		this.arenewPayment = arenewPayment;
		this.type = type;
		this.oldvalue = oldvalue;
		this.newvalue = newvalue;
		this.updatedate = updatedate;
		this.object = object;
		this.cfecid = cfecid;
		this.ryear = ryear;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getConfirmcode() {
		return confirmcode;
	}

	public void setConfirmcode(String confirmcode) {
		this.confirmcode = confirmcode;
	}

	public ArenewEntity getArenewEntity() {
		return arenewEntity;
	}

	public void setArenewEntity(ArenewEntity arenewEntity) {
		this.arenewEntity = arenewEntity;
	}

	public ArenewPayment getArenewPayment() {
		return arenewPayment;
	}

	public void setArenewPayment(ArenewPayment arenewPayment) {
		this.arenewPayment = arenewPayment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOldvalue() {
		return oldvalue;
	}

	public void setOldvalue(String oldvalue) {
		this.oldvalue = oldvalue;
	}

	public String getNewvalue() {
		return newvalue;
	}

	public void setNewvalue(String newvalue) {
		this.newvalue = newvalue;
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
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
	
}
