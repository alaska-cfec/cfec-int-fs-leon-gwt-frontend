package com.cfecweb.leon.dto;

import com.extjs.gxt.ui.client.data.BeanModelTag;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ArenewEntityId implements BeanModelTag, IsSerializable {

	private String cfecid;
	private String ryear;

	public ArenewEntityId() {
	}

	public ArenewEntityId(String cfecid, String ryear) {
		this.cfecid = cfecid;
		this.ryear = ryear;
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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ArenewEntityId))
			return false;
		ArenewEntityId castOther = (ArenewEntityId) other;

		return ((this.getCfecid() == castOther.getCfecid()) || (this
				.getCfecid() != null
				&& castOther.getCfecid() != null && this.getCfecid().equals(
				castOther.getCfecid())))
				&& ((this.getRyear() == castOther.getRyear()) || (this
						.getRyear() != null
						&& castOther.getRyear() != null && this.getRyear()
						.equals(castOther.getRyear())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCfecid() == null ? 0 : this.getCfecid().hashCode());
		result = 37 * result
				+ (getRyear() == null ? 0 : this.getRyear().hashCode());
		return result;
	}

    @Override
    public String toString() {
        return "ArenewEntityId{" +
                "cfecid='" + cfecid + '\'' +
                ", ryear='" + ryear + '\'' +
                '}';
    }
}
