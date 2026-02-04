package com.cfecweb.leon.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GWTChangeMonitor implements Serializable {
	private String confirmnum;
	private boolean paddress = false;
	private boolean paddress2 = false;
	private boolean pcity = false;
	private boolean pstate = false;
	private boolean pzip = false;
	private boolean baddress = false;
	private boolean baddress2 = false;
	private boolean bcity = false;
	private boolean bstate = false;
	private boolean bzip = false;
	private boolean email = false;
	private boolean phone = false;
	private boolean rtime = false;
	private boolean padfg = false;
	
	public GWTChangeMonitor() {
		super();
	}

	public String getConfirmnum() {
		return confirmnum;
	}

	public void setConfirmnum(String confirmnum) {
		this.confirmnum = confirmnum;
	}

	public boolean isPaddress() {
		return paddress;
	}

	public void setPaddress(boolean paddress) {
		this.paddress = paddress;
	}

	public boolean isPaddress2() {
		return paddress2;
	}

	public void setPaddress2(boolean paddress2) {
		this.paddress2 = paddress2;
	}

	public boolean isPcity() {
		return pcity;
	}

	public void setPcity(boolean pcity) {
		this.pcity = pcity;
	}

	public boolean isPstate() {
		return pstate;
	}

	public void setPstate(boolean pstate) {
		this.pstate = pstate;
	}

	public boolean isPzip() {
		return pzip;
	}

	public void setPzip(boolean pzip) {
		this.pzip = pzip;
	}

	public boolean isEmail() {
		return email;
	}

	public void setEmail(boolean email) {
		this.email = email;
	}

	public boolean isPhone() {
		return phone;
	}

	public void setPhone(boolean phone) {
		this.phone = phone;
	}

	public boolean isBaddress() {
		return baddress;
	}

	public void setBaddress(boolean baddress) {
		this.baddress = baddress;
	}

	public boolean isBaddress2() {
		return baddress2;
	}

	public void setBaddress2(boolean baddress2) {
		this.baddress2 = baddress2;
	}

	public boolean isBcity() {
		return bcity;
	}

	public void setBcity(boolean bcity) {
		this.bcity = bcity;
	}

	public boolean isBstate() {
		return bstate;
	}

	public void setBstate(boolean bstate) {
		this.bstate = bstate;
	}

	public boolean isBzip() {
		return bzip;
	}

	public void setBzip(boolean bzip) {
		this.bzip = bzip;
	}

	public boolean isRtime() {
		return rtime;
	}

	public void setRtime(boolean rtime) {
		this.rtime = rtime;
	}

	public boolean isPadfg() {
		return padfg;
	}

	public void setPadfg(boolean padfg) {
		this.padfg = padfg;
	}

}
