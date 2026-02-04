package com.cfecweb.leon.dto;

import com.extjs.gxt.ui.client.data.BeanModelTag;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ArenewPayment implements BeanModelTag, IsSerializable {

	private String confirmcode;
	//private ArenewEntity arenewEntity;
	private String baddress = "N/A";
	private String baddress2 = "N/A";
	private String bcity = "N/A";
	private String bstate = "N/A";
	private String bzip = "N/A";
	private String ccnumber;
	private String ccowner;
	private String ccname;
	private String ccmname;
	private String cclname;
	private String ccmonth;
	private String ccyear;
	private String curtx;
	private Date receiveddate;
	private String cctype;
	private String indicator = "N/A";
	private String totalamount;
	private String dfee1 = "N/A";
	private String dfee2 = "N/A";
	private String dfee3 = "N/A";
	private String ccsec = "N/A";
	private String serfee = "N/A";
	private String shipfee = "N/A";
	private String shiptype;
	private String bcountry = "N/A";
	private String notes;
	private String rjnumber;
	private String revoperator;
	private String prnoperator;
	private String revcomplete;
	private String prncomplete;
	private Date revdate;
	private Date prndate;
	private String authcode;
	//private Set<ArenewVessels> arenewVesselses = new HashSet<ArenewVessels>(0);
	//private Set<ArenewPermits> arenewPermitses = new HashSet<ArenewPermits>(0);
	//private Set<ArenewChanges> arenewChangeses = new HashSet<ArenewChanges>(0);

	public ArenewPayment() {
	}

	public ArenewPayment(String confirmcode, //ArenewEntity arenewEntity,
			String baddress, String bcity, String bstate,
			String bzip, String ccnumber, String ccname, String ccmonth,
			String ccyear, String curtx, Date receiveddate, String cctype,
			String totalamount, String ccowner) {
		this.confirmcode = confirmcode;
		//this.arenewEntity = arenewEntity;
		this.baddress = baddress;
		this.bcity = bcity;
		this.bstate = bstate;
		this.bzip = bzip;
		this.ccnumber = ccnumber;
		this.ccname = ccname;
		this.ccmonth = ccmonth;
		this.ccyear = ccyear;
		this.curtx = curtx;
		this.receiveddate = receiveddate;
		this.cctype = cctype;
		this.totalamount = totalamount;
		this.ccowner = ccowner;
	}

	public ArenewPayment(String confirmcode, ArenewEntity arenewEntity,
			String baddress, String bcity, String bstate,
			String bzip, String ccnumber, String ccname, String ccmonth,
			String ccyear, String curtx, Date receiveddate, String cctype,
			String indicator, String totalamount, String ccsec,
			String shiptype, String bcountry, String ccowner/*,
			Set<ArenewVessels> arenewVesselses,
			Set<ArenewPermits> arenewPermitses,
			Set<ArenewChanges> arenewChangeses*/) {
		this.confirmcode = confirmcode;
		//this.arenewEntity = arenewEntity;
		this.baddress = baddress;
		this.bcity = bcity;
		this.bstate = bstate;
		this.bzip = bzip;
		this.ccnumber = ccnumber;
		this.ccowner = ccowner;
		this.ccname = ccname;
		this.ccmonth = ccmonth;
		this.ccyear = ccyear;
		this.curtx = curtx;
		this.receiveddate = receiveddate;
		this.cctype = cctype;
		this.indicator = indicator;
		this.totalamount = totalamount;
		this.ccsec = ccsec;
		this.shiptype = shiptype;
		this.bcountry = bcountry;
		/*this.arenewVesselses = arenewVesselses;
		this.arenewPermitses = arenewPermitses;
		this.arenewChangeses = arenewChangeses;*/
	}

	public String getConfirmcode() {
		return this.confirmcode;
	}

	public void setConfirmcode(String confirmcode) {
		this.confirmcode = confirmcode;
	}

	/*public ArenewEntity getArenewEntity() {
		return this.arenewEntity;
	}

	public void setArenewEntity(ArenewEntity arenewEntity) {
		this.arenewEntity = arenewEntity;
	}*/

	public String getBaddress() {
		return this.baddress;
	}

	public void setBaddress(String baddress) {
		this.baddress = baddress;
	}

	public String getBcity() {
		return this.bcity;
	}

	public void setBcity(String bcity) {
		this.bcity = bcity;
	}

	public String getBstate() {
		return this.bstate;
	}

	public void setBstate(String bstate) {
		this.bstate = bstate;
	}

	public String getBzip() {
		return this.bzip;
	}

	public void setBzip(String bzip) {
		this.bzip = bzip;
	}

	public String getCcnumber() {
		return this.ccnumber;
	}

	public void setCcnumber(String ccnumber) {
		this.ccnumber = ccnumber;
	}

	public String getCcname() {
		return this.ccname;
	}

	public void setCcname(String ccname) {
		this.ccname = ccname;
	}

	public String getCcmonth() {
		return this.ccmonth;
	}

	public void setCcmonth(String ccmonth) {
		this.ccmonth = ccmonth;
	}

	public String getCcyear() {
		return this.ccyear;
	}

	public void setCcyear(String ccyear) {
		this.ccyear = ccyear;
	}

	public String getCurtx() {
		return this.curtx;
	}

	public void setCurtx(String curtx) {
		this.curtx = curtx;
	}

	public Date getReceiveddate() {
		return this.receiveddate;
	}

	public void setReceiveddate(Date receiveddate) {
		this.receiveddate = receiveddate;
	}

	public String getCctype() {
		return this.cctype;
	}

	public void setCctype(String cctype) {
		this.cctype = cctype;
	}

	public String getIndicator() {
		return this.indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getTotalamount() {
		return this.totalamount;
	}

	public void setTotalamount(String totalamount) {
		this.totalamount = totalamount;
	}

	public String getCcsec() {
		return this.ccsec;
	}

	public void setCcsec(String ccsec) {
		this.ccsec = ccsec;
	}

	public String getShiptype() {
		return this.shiptype;
	}

	public void setShiptype(String shiptype) {
		this.shiptype = shiptype;
	}

	public String getBcountry() {
		return this.bcountry;
	}

	public void setBcountry(String bcountry) {
		this.bcountry = bcountry;
	}

	/*public Set<ArenewVessels> getArenewVesselses() {
		return this.arenewVesselses;
	}

	public void setArenewVesselses(Set<ArenewVessels> arenewVesselses) {
		this.arenewVesselses = arenewVesselses;
	}

	public Set<ArenewPermits> getArenewPermitses() {
		return this.arenewPermitses;
	}

	public void setArenewPermitses(Set<ArenewPermits> arenewPermitses) {
		this.arenewPermitses = arenewPermitses;
	}

	public Set<ArenewChanges> getArenewChangeses() {
		return this.arenewChangeses;
	}

	public void setArenewChangeses(Set<ArenewChanges> arenewChangeses) {
		this.arenewChangeses = arenewChangeses;
	}*/

	public String getBaddress2() {
		return baddress2;
	}

	public void setBaddress2(String baddress2) {
		this.baddress2 = baddress2;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getRjnumber() {
		return rjnumber;
	}

	public void setRjnumber(String rjnumber) {
		this.rjnumber = rjnumber;
	}

	public String getRevoperator() {
		return revoperator;
	}

	public void setRevoperator(String revoperator) {
		this.revoperator = revoperator;
	}

	public String getPrnoperator() {
		return prnoperator;
	}

	public void setPrnoperator(String prnoperator) {
		this.prnoperator = prnoperator;
	}

	public String getRevcomplete() {
		return revcomplete;
	}

	public void setRevcomplete(String revcomplete) {
		this.revcomplete = revcomplete;
	}

	public String getPrncomplete() {
		return prncomplete;
	}

	public void setPrncomplete(String prncomplete) {
		this.prncomplete = prncomplete;
	}
	
	public String getAuthcode() {
		return authcode;
	}
	
	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}

	public Date getRevdate() {
		return revdate;
	}

	public void setRevdate(Date revdate) {
		this.revdate = revdate;
	}

	public Date getPrndate() {
		return prndate;
	}

	public void setPrndate(Date prndate) {
		this.prndate = prndate;
	}

	public String getCcowner() {
		return ccowner;
	}

	public void setCcowner(String ccowner) {
		this.ccowner = ccowner;
	}

	public String getCcmname() {
		return ccmname;
	}

	public void setCcmname(String ccmname) {
		this.ccmname = ccmname;
	}

	public String getCclname() {
		return cclname;
	}

	public void setCclname(String cclname) {
		this.cclname = cclname;
	}
	
	public String getDfee1() {
		return dfee1;
	}

	public void setDfee1(String dfee1) {
		this.dfee1 = dfee1;
	}

	public String getDfee2() {
		return dfee2;
	}

	public void setDfee2(String dfee2) {
		this.dfee2 = dfee2;
	}

	public String getDfee3() {
		return dfee3;
	}

	public void setDfee3(String dfee3) {
		this.dfee3 = dfee3;
	}
	
	public String getSerfee() {
		return serfee;
	}

	public void setSerfee(String serfee) {
		this.serfee = serfee;
	}

	public String getShipfee() {
		return shipfee;
	}

	public void setShipfee(String shipfee) {
		this.shipfee = shipfee;
	}

    @Override
    public String toString() {
        return "ArenewPayment{" +
                "confirmcode='" + confirmcode + '\'' +
                ", baddress='" + baddress + '\'' +
                ", baddress2='" + baddress2 + '\'' +
                ", bcity='" + bcity + '\'' +
                ", bstate='" + bstate + '\'' +
                ", bzip='" + bzip + '\'' +
                ", ccnumber='" + ccnumber + '\'' +
                ", ccowner='" + ccowner + '\'' +
                ", ccname='" + ccname + '\'' +
                ", ccmname='" + ccmname + '\'' +
                ", cclname='" + cclname + '\'' +
                ", ccmonth='" + ccmonth + '\'' +
                ", ccyear='" + ccyear + '\'' +
                ", curtx='" + curtx + '\'' +
                ", receiveddate=" + receiveddate +
                ", cctype='" + cctype + '\'' +
                ", indicator='" + indicator + '\'' +
                ", totalamount='" + totalamount + '\'' +
                ", dfee1='" + dfee1 + '\'' +
                ", dfee2='" + dfee2 + '\'' +
                ", dfee3='" + dfee3 + '\'' +
                ", ccsec='" + ccsec + '\'' +
                ", serfee='" + serfee + '\'' +
                ", shipfee='" + shipfee + '\'' +
                ", shiptype='" + shiptype + '\'' +
                ", bcountry='" + bcountry + '\'' +
                ", notes='" + notes + '\'' +
                ", rjnumber='" + rjnumber + '\'' +
                ", revoperator='" + revoperator + '\'' +
                ", prnoperator='" + prnoperator + '\'' +
                ", revcomplete='" + revcomplete + '\'' +
                ", prncomplete='" + prncomplete + '\'' +
                ", revdate=" + revdate +
                ", prndate=" + prndate +
                ", authcode='" + authcode + '\'' +
                '}';
    }
}
