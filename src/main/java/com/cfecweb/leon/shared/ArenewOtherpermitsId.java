package com.cfecweb.leon.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ArenewOtherpermitsId implements Serializable {
	
	private String pyear;
	private String serial;
	private String fishery;
	
	public ArenewOtherpermitsId() {
		
	}

	public ArenewOtherpermitsId(String pyear, String serial,
			String fishery) {
		super();
		this.pyear = pyear;
		this.serial = serial;
		this.fishery = fishery;
	}

	public String getPyear() {
		return pyear;
	}

	public void setPyear(String pyear) {
		this.pyear = pyear;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getFishery() {
		return fishery;
	}

	public void setFishery(String fishery) {
		this.fishery = fishery;
	}

    @Override
    public String toString() {
        return "ArenewOtherpermitsId{" +
                "pyear='" + pyear + '\'' +
                ", serial='" + serial + '\'' +
                ", fishery='" + fishery + '\'' +
                '}';
    }
}
