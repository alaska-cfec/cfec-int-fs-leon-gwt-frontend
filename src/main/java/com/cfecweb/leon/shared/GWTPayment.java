package com.cfecweb.leon.shared;

import java.io.Serializable;
import java.util.Date;

public class GWTPayment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String baddress;
	private String baddress2;
	private String bcity;
	private String bcopy;
	private String bzip;
	private String bstate;
	private String bcountry;
	private String ccmonth;
	private String ccname;
	private String ccnumber;
	private String ccsec;
	private String cctype;
	private String ccyear;
	private String cfecid;
	private String confirmcode;
	private String curtx;
	private String indicator;
	private Date receivedate;
	private String ryear;
	private String shiptype;
	private double totalamout;
	
	public GWTPayment(String baddress, String bcity, String bcopy, String bzip,
			String bstate, String ccmonth, String ccname, String ccnumber,
			String ccsec, String cctype, String ccyear, String cfecid,
			String confirmcode, String curtx, String indicator, String bcountry,
			Date receivedate, String ryear, String shiptype, double totalamout) {
		super();
		this.baddress = baddress;
		this.bcity = bcity;
		this.bcopy = bcopy;
		this.bzip = bzip;
		this.bstate = bstate;
		this.bcountry = bcountry;
		this.ccmonth = ccmonth;
		this.ccname = ccname;
		this.ccnumber = ccnumber;
		this.ccsec = ccsec;
		this.cctype = cctype;
		this.ccyear = ccyear;
		this.cfecid = cfecid;
		this.confirmcode = confirmcode;
		this.curtx = curtx;
		this.indicator = indicator;
		this.receivedate = receivedate;
		this.ryear = ryear;
		this.shiptype = shiptype;
		this.totalamout = totalamout;
	}

	public String getBcountry() {
		return bcountry;
	}

	public void setBcountry(String bcountry) {
		this.bcountry = bcountry;
	}

	public GWTPayment() {
		super();
	}

	public String getBaddress() {
		return baddress;
	}

	public void setBaddress(String baddress) {
		this.baddress = baddress;
	}

	public String getBcity() {
		return bcity;
	}

	public void setBcity(String bcity) {
		this.bcity = bcity;
	}

	public String getBcopy() {
		return bcopy;
	}

	public void setBcopy(String bcopy) {
		this.bcopy = bcopy;
	}

	public String getBzip() {
		return bzip;
	}

	public void setBzip(String bzip) {
		this.bzip = bzip;
	}

	public String getBstate() {
		return bstate;
	}

	public void setBstate(String bstate) {
		this.bstate = bstate;
	}

	public String getCcmonth() {
		return ccmonth;
	}

	public void setCcmonth(String ccmonth) {
		this.ccmonth = ccmonth;
	}

	public String getCcname() {
		return ccname;
	}

	public void setCcname(String ccname) {
		this.ccname = ccname;
	}

	public String getCcnumber() {
		return ccnumber;
	}

	public void setCcnumber(String ccnumber) {
		this.ccnumber = ccnumber;
	}

	public String getCcsec() {
		return ccsec;
	}

	public void setCcsec(String ccsec) {
		this.ccsec = ccsec;
	}

	public String getCctype() {
		return cctype;
	}

	public void setCctype(String cctype) {
		this.cctype = cctype;
	}

	public String getCcyear() {
		return ccyear;
	}

	public void setCcyear(String ccyear) {
		this.ccyear = ccyear;
	}

	public String getCfecid() {
		return cfecid;
	}

	public void setCfecid(String cfecid) {
		this.cfecid = cfecid;
	}

	public String getConfirmcode() {
		return confirmcode;
	}

	public void setConfirmcode(String confirmcode) {
		this.confirmcode = confirmcode;
	}

	public String getCurtx() {
		return curtx;
	}

	public void setCurtx(String curtx) {
		this.curtx = curtx;
	}

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public Date getReceivedate() {
		return receivedate;
	}

	public void setReceivedate(Date receivedate) {
		this.receivedate = receivedate;
	}

	public String getRyear() {
		return ryear;
	}

	public void setRyear(String ryear) {
		this.ryear = ryear;
	}

	public String getShiptype() {
		return shiptype;
	}

	public void setShiptype(String shiptype) {
		this.shiptype = shiptype;
	}

	public double getTotalamout() {
		return totalamout;
	}

	public void setTotalamout(double totalamout) {
		this.totalamout = totalamout;
	}	
	
	public String getBaddress2() {
		return baddress2;
	}

	public void setBaddress2(String baddress2) {
		this.baddress2 = baddress2;
	}
	
}
