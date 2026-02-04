package com.cfecweb.leon.dto;

import com.extjs.gxt.ui.client.data.BeanModelTag;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ArenewEntity implements BeanModelTag, IsSerializable {
	private ArenewEntityId id;
	private String poverty;
	private String residency;
	private String citizen;	
	private String years;
	private String months;
	private String area;
	private String phone;
	private String email;
	private String ryear;	
	private String paddress;
	private String paddress2;
	private String pcity;
	private String pstate;
	private String pzip;
	private String pcountry;
	private String raddress;
	private String raddress2;
	private String rcity;
	private String rcountry;
	private String rstate;
	private String rzip;
	private String alienreg;
	private String reducedHalibut;
	private String reducedSablefish;
	private String msnarea;
	private String xname;
	private String agent;
	private String differentialc;
	private boolean foreign;
	private String agentsub;
	private String diffamount;
	private String firsttime;
	private String company;
	private String illegal;
	private Date createdate;
	private String differentialp;
	private String differentialp2;
	private Date updatedate;	
	private long pmtcount;
	private long vescount;
	private long totcount;
	private String diffamountcyear;
	private String diffamountpyear;
	private String diffamountp2year;
	private String phonepub;
	private String emailpub;
	private boolean manual;
	private boolean autoHALreduced;
	private boolean autoSABreduced;
	private Set<ArenewVessels> arenewVesselses = new HashSet<ArenewVessels>(0);
	private Set<ArenewPermits> arenewPermitses = new HashSet<ArenewPermits>(0);
//	private Set<ArenewPayment> arenewPayments = new HashSet<ArenewPayment>(0);
	private Set<ArenewChanges> arenewChangeses = new HashSet<ArenewChanges>(0);
//	private Set<ArenewOtherpermits> arenewOtherpermitses = new HashSet<ArenewOtherpermits>(0);
	private String pyearabsent;
	private String pyearabsenttext;
	private String alaskaid;
	private String alaskaidtext;
	private String alaskavote;
	private String othertext;	

	public ArenewEntity() {
	}	

	public ArenewEntity(ArenewEntityId id, String poverty, String residency,
			String citizen, String years, String months, String area,
			String phone, String email, String paddress,
			String pcity, String pstate, String pzip, String pcountry,
			String raddress, String raddress2, String rcity, String rcountry,
			String rstate, String rzip, String alienreg, String reducedHalibut,
			String reducedSablefish, String msnarea, String xname,
			String agent, String differentialc, String agentsub,
			String diffamount, String firsttime, String company, String illegal,
			Date createdate, String differentialp, Date updatedate,
			long pmtcount, long vescount, long totcount, String ryear,
			Set<ArenewVessels> arenewVesselses,
			Set<ArenewPermits> arenewPermitses,
			/*Set<ArenewPayment> arenewPayments,*/
			Set<ArenewChanges> arenewChangeses) {
		this.id = id;
		this.poverty = poverty;
		this.residency = residency;
		this.citizen = citizen;
		this.years = years;
		this.months = months;
		this.area = area;
		this.phone = phone;
		this.email = email;
		this.paddress = paddress;
		this.pcity = pcity;
		this.pstate = pstate;
		this.pzip = pzip;
		this.pcountry = pcountry;
		this.raddress = raddress;
		this.raddress2 = raddress2;
		this.rcity = rcity;
		this.rcountry = rcountry;
		this.rstate = rstate;
		this.rzip = rzip;
		this.alienreg = alienreg;
		this.reducedHalibut = reducedHalibut;
		this.reducedSablefish = reducedSablefish;
		this.msnarea = msnarea;
		this.xname = xname;
		this.agent = agent;
		this.differentialc = differentialc;
		this.agentsub = agentsub;
		this.diffamount = diffamount;
		this.firsttime = firsttime;
		this.company = company;
		this.illegal = illegal;
		this.createdate = createdate;
		this.differentialp = differentialp;
		this.updatedate = updatedate;
		this.pmtcount = pmtcount;
		this.vescount = vescount;
		this.totcount = totcount;
		this.arenewVesselses = arenewVesselses;
		this.arenewPermitses = arenewPermitses;
		/*this.arenewPayments = arenewPayments;*/
		this.arenewChangeses = arenewChangeses;
	}

	public ArenewEntityId getId() {
		return this.id;
	}

	public String getDifferentialp2() {
		return differentialp2;
	}

	public String getDiffamountp2year() {
		return diffamountp2year;
	}

	public void setDiffamountp2year(String diffamountp2year) {
		this.diffamountp2year = diffamountp2year;
	}

	public void setDifferentialp2(String differentialp2) {
		this.differentialp2 = differentialp2;
	}

	public void setId(ArenewEntityId id) {
		this.id = id;
	}

	public String getPoverty() {
		return this.poverty;
	}

	public void setPoverty(String poverty) {
		this.poverty = poverty;
	}

	public String getResidency() {
		return this.residency;
	}

	public void setResidency(String residency) {
		this.residency = residency;
	}

	public String getCitizen() {
		return this.citizen;
	}

	public void setCitizen(String citizen) {
		this.citizen = citizen;
	}

	public String getPaddress() {
		return this.paddress;
	}

	public void setPaddress(String paddress) {
		this.paddress = paddress;
	}

	public String getPcity() {
		return this.pcity;
	}

	public void setPcity(String pcity) {
		this.pcity = pcity;
	}

	public String getPstate() {
		return this.pstate;
	}

	public void setPstate(String pstate) {
		this.pstate = pstate;
	}

	public String getPzip() {
		return this.pzip;
	}

	public void setPzip(String pzip) {
		this.pzip = pzip;
	}

	public String getYears() {
		return this.years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getMonths() {
		return this.months;
	}

	public void setMonths(String months) {
		this.months = months;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAlienreg() {
		return this.alienreg;
	}

	public void setAlienreg(String alienreg) {
		this.alienreg = alienreg;
	}

	public String getReducedHalibut() {
		return this.reducedHalibut;
	}

	public void setReducedHalibut(String reducedHalibut) {
		this.reducedHalibut = reducedHalibut;
	}

	public String getReducedSablefish() {
		return this.reducedSablefish;
	}

	public void setReducedSablefish(String reducedSablefish) {
		this.reducedSablefish = reducedSablefish;
	}

	public String getMsnarea() {
		return this.msnarea;
	}

	public void setMsnarea(String msnarea) {
		this.msnarea = msnarea;
	}

	public String getXname() {
		return this.xname;
	}

	public void setXname(String xname) {
		this.xname = xname;
	}

	public String getAgent() {
		return this.agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getDifferentialc() {
		return this.differentialc;
	}

	public void setDifferentialc(String differentialc) {
		this.differentialc = differentialc;
	}

	public String getAgentsub() {
		return this.agentsub;
	}

	public void setAgentsub(String agentsub) {
		this.agentsub = agentsub;
	}

	public String getDiffamount() {
		return this.diffamount;
	}

	public void setDiffamount(String diffamount) {
		this.diffamount = diffamount;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getDifferentialp() {
		return this.differentialp;
	}

	public void setDifferentialp(String differentialp) {
		this.differentialp = differentialp;
	}

	public Date getUpdatedate() {
		return this.updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public String getPcountry() {
		return this.pcountry;
	}

	public void setPcountry(String pcountry) {
		this.pcountry = pcountry;
	}

	public String getFirsttime() {
		return firsttime;
	}

	public void setFirsttime(String firsttime) {
		this.firsttime = firsttime;
	}

	public Set<ArenewVessels> getArenewVesselses() {
		return this.arenewVesselses;
	}	

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
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

	/*public Set<ArenewPayment> getArenewPayments() {
		return this.arenewPayments;
	}

	public void setArenewPayments(Set<ArenewPayment> arenewPayments) {
		this.arenewPayments = arenewPayments;
	}*/

	public Set<ArenewChanges> getArenewChangeses() {
		return this.arenewChangeses;
	}

	public void setArenewChangeses(Set<ArenewChanges> arenewChangeses) {
		this.arenewChangeses = arenewChangeses;
	}

	/*public Set<ArenewOtherpermits> getArenewOtherpermitses() {
		return arenewOtherpermitses;
	}

	public void setArenewOtherpermitses(Set<ArenewOtherpermits> arenewOtherpermitses) {
		this.arenewOtherpermitses = arenewOtherpermitses;
	}*/

	public long getPmtcount() {
		return pmtcount;
	}

	public void setPmtcount(long pmtcount) {
		this.pmtcount = pmtcount;
	}

	public long getVescount() {
		return vescount;
	}

	public void setVescount(long vescount) {
		this.vescount = vescount;
	}

	public long getTotcount() {
		return totcount;
	}

	public void setTotcount(long totcount) {
		this.totcount = totcount;
	}

	public String getRaddress() {
		return raddress;
	}

	public void setRaddress(String raddress) {
		this.raddress = raddress;
	}

	public String getRcity() {
		return rcity;
	}

	public void setRcity(String rcity) {
		this.rcity = rcity;
	}

	public String getRstate() {
		return rstate;
	}

	public void setRstate(String rstate) {
		this.rstate = rstate;
	}

	public String getRzip() {
		return rzip;
	}

	public void setRzip(String rzip) {
		this.rzip = rzip;
	}

	public String getRaddress2() {
		return raddress2;
	}

	public void setRaddress2(String raddress2) {
		this.raddress2 = raddress2;
	}

	public String getRcountry() {
		return rcountry;
	}

	public void setRcountry(String rcountry) {
		this.rcountry = rcountry;
	}

	public String getPaddress2() {
		return paddress2;
	}

	public void setPaddress2(String paddress2) {
		this.paddress2 = paddress2;
	}

	public String getRyear() {
		return ryear;
	}

	public void setRyear(String ryear) {
		this.ryear = ryear;
	}

	public boolean isForeign() {
		return foreign;
	}

	public void setForeign(boolean foreign) {
		this.foreign = foreign;
	}

	public String getIllegal() {
		return illegal;
	}

	public void setIllegal(String illegal) {
		this.illegal = illegal;
	}

	public String getDiffamountcyear() {
		return diffamountcyear;
	}

	public void setDiffamountcyear(String diffamountcyear) {
		this.diffamountcyear = diffamountcyear;
	}

	public String getDiffamountpyear() {
		return diffamountpyear;
	}

	public void setDiffamountpyear(String diffamountpyear) {
		this.diffamountpyear = diffamountpyear;
	}	
	
	public String getPhonepub() {
		return phonepub;
	}

	public void setPhonepub(String phonepub) {
		this.phonepub = phonepub;
	}
	
	public String getEmailpub() {
		return emailpub;
	}

	public void setEmailpub(String emailpub) {
		this.emailpub = emailpub;
	}
	
	public boolean isManual() {
		return manual;
	}

	public void setManual(boolean manual) {
		this.manual = manual;
	}
	
	public boolean isAutoHALreduced() {
		return autoHALreduced;
	}

	public void setAutoHALreduced(boolean autoHALreduced) {
		this.autoHALreduced = autoHALreduced;
	}
	
	public boolean isAutoSABreduced() {
		return autoSABreduced;
	}

	public void setAutoSABreduced(boolean autoSABreduced) {
		this.autoSABreduced = autoSABreduced;
	}
	
	public String getPyearabsent() {
		return pyearabsent;
	}

	public void setPyearabsent(String pyearabsent) {
		this.pyearabsent = pyearabsent;
	}

	public String getPyearabsenttext() {
		return pyearabsenttext;
	}

	public void setPyearabsenttext(String pyearabsenttext) {
		this.pyearabsenttext = pyearabsenttext;
	}

	public String getAlaskaid() {
		return alaskaid;
	}

	public void setAlaskaid(String alaskaid) {
		this.alaskaid = alaskaid;
	}

	public String getAlaskaidtext() {
		return alaskaidtext;
	}

	public void setAlaskaidtext(String alaskaidtext) {
		this.alaskaidtext = alaskaidtext;
	}

	public String getAlaskavote() {
		return alaskavote;
	}

	public void setAlaskavote(String alaskavote) {
		this.alaskavote = alaskavote;
	}

	public String getOthertext() {
		return othertext;
	}

	public void setOthertext(String othertext) {
		this.othertext = othertext;
	}
	
}
