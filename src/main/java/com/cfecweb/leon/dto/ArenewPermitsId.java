package com.cfecweb.leon.dto;

import com.extjs.gxt.ui.client.data.BeanModelTag;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ArenewPermitsId implements BeanModelTag, IsSerializable {

	private String cfecid;
	private String ryear;
	private String serial;
	private String pyear;
	private String fishery;

	public ArenewPermitsId() {
	}

	public ArenewPermitsId(String cfecid, String ryear, String serial,
                           String pyear, String fishery) {
		this.cfecid = cfecid;
		this.ryear = ryear;
		this.serial = serial;
		this.pyear = pyear;
		this.fishery = fishery;
	}

	public String getCfecid() {
		return this.cfecid;
	}

	public void setCfecid(String cfecid) {
		this.cfecid = cfecid;
	}

	public String getRyear() {
		return this.ryear;
	}

	public void setRyear(String ryear) {
		this.ryear = ryear;
	}

	public String getSerial() {
		return this.serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getPyear() {
		return this.pyear;
	}

	public void setPyear(String pyear) {
		this.pyear = pyear;
	}

	public String getFishery() {
		return this.fishery;
	}

	public void setFishery(String fishery) {
		this.fishery = fishery;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ArenewPermitsId))
			return false;
		ArenewPermitsId castOther = (ArenewPermitsId) other;

		return ((this.getCfecid() == castOther.getCfecid()) || (this
				.getCfecid() != null
				&& castOther.getCfecid() != null && this.getCfecid().equals(
				castOther.getCfecid())))
				&& ((this.getRyear() == castOther.getRyear()) || (this
						.getRyear() != null
						&& castOther.getRyear() != null && this.getRyear()
						.equals(castOther.getRyear())))
				&& ((this.getSerial() == castOther.getSerial()) || (this
						.getSerial() != null
						&& castOther.getSerial() != null && this.getSerial()
						.equals(castOther.getSerial())))
				&& ((this.getPyear() == castOther.getPyear()) || (this
						.getPyear() != null
						&& castOther.getPyear() != null && this.getPyear()
						.equals(castOther.getPyear())))
				&& ((this.getFishery() == castOther.getFishery()) || (this
						.getFishery() != null
						&& castOther.getFishery() != null && this.getFishery()
						.equals(castOther.getFishery())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCfecid() == null ? 0 : this.getCfecid().hashCode());
		result = 37 * result
				+ (getRyear() == null ? 0 : this.getRyear().hashCode());
		result = 37 * result
				+ (getSerial() == null ? 0 : this.getSerial().hashCode());
		result = 37 * result
				+ (getPyear() == null ? 0 : this.getPyear().hashCode());
		result = 37 * result
				+ (getFishery() == null ? 0 : this.getFishery().hashCode());
		return result;
	}

    @Override
    public String toString() {
        return "ArenewPermitsId{" +
                "cfecid='" + cfecid + '\'' +
                ", ryear='" + ryear + '\'' +
                ", serial='" + serial + '\'' +
                ", pyear='" + pyear + '\'' +
                ", fishery='" + fishery + '\'' +
                '}';
    }
}
