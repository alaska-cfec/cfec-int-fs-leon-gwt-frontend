package com.cfecweb.leon.shared;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class ArenewOtherpermits implements Serializable {

	private ArenewOtherpermitsId id;
	private ArenewEntity arenewEntity;
	private String intends;
	private Date updatedate;
	private String nointends;
	
	public ArenewOtherpermits() {
		
	}

	public ArenewOtherpermits(String intends, Date updatedate, String nointends) {
		super();
		this.intends = intends;
		this.updatedate = updatedate;
		this.nointends = nointends;
	}

	public ArenewOtherpermits(ArenewOtherpermitsId id,
			ArenewEntity arenewEntity, String intends, Date updatedate,
			String nointends) {
		super();
		this.id = id;
		this.arenewEntity = arenewEntity;
		this.intends = intends;
		this.updatedate = updatedate;
		this.nointends = nointends;
	}

	public ArenewOtherpermitsId getId() {
		return id;
	}

	public void setId(ArenewOtherpermitsId id) {
		this.id = id;
	}

	public ArenewEntity getArenewEntity() {
		return arenewEntity;
	}

	public void setArenewEntity(ArenewEntity arenewEntity) {
		this.arenewEntity = arenewEntity;
	}

	public String getIntends() {
		return intends;
	}

	public void setIntends(String intends) {
		this.intends = intends;
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public String getNointends() {
		return nointends;
	}

	public void setNointends(String nointends) {
		this.nointends = nointends;
	}
	
}
