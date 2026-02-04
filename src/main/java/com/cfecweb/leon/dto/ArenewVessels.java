package com.cfecweb.leon.dto;

import com.extjs.gxt.ui.client.data.BeanModelTag;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class ArenewVessels implements BeanModelTag, IsSerializable, Comparable<ArenewVessels> {

	private ArenewVesselsId id;
	//private ArenewEntity arenewEntity;
	//private ArenewPayment arenewPayment;
	private String name;
	private String confirmcode;	
	private String regNum;
	private String yearBuilt;
	private String makeModel;
	private String length;
	private String inches;
	private String lengthFeet;
	private String lengthInches;
	private String fee;
	private String grossTons;
	private String netTons;
	private String homeportCity;
	private String engineType;
	private String horsepower;
	private String estValue;
	private String hullType;
	private String hullId;
	private String fuel;
	private String holdTank;
	private String liveTank;
	private String refrigeration;
	private String freezerCanner;
	private String tenderPacker;
	private String fishingboat;
	private String purseseine;
	private String beachseine;
	private String driftgillnet;
	private String setgillnet;
	private String handtroll;
	private String longline;
	private String singleottertrawl;
	private String fishwheel;
	private String potgear;
	private String ringnet;
	private String divegear;
	private String powertroll;
	private String beamtrawl;
	private String dredge;
	private String dinglebar;
	private String jig;
	private String doubleottertrawl;
	private String hearinggillnet;
	private String pairtrawl;
	private Date receiveddate;
	private String homeportState;
	private String foerignFlag;
	private String transporter;
	private String othergear;
	private String status;
	private String vyear;
	private String registrationType;
	private String salmontrollReg;
	private String salmontrollDate;
	private String salmonregArea;
	private String permitSerial1;
	private String permitSerial2;
	private Date lastupdated;
	private boolean newVessel;
	private boolean renewed;
	private String details;
	private String isnew;
	private String delorig;
	// These next fields are used only for user added Vessels (new)
	private String q1;
	private String q2;
	private String q3;
	private String q4;
	private String q5;
	private String q6;
	private String q7;
	private String q8;
	private String q9;
	private String q10;
	private String q11;	
	private boolean newrenew;

	public ArenewVessels() {
	}

	public ArenewVessels(ArenewVesselsId id, /*ArenewEntity arenewEntity,
                         ArenewPayment arenewPayment,*/ String vyear) {
		this.id = id;
		//this.arenewEntity = arenewEntity;
		//this.arenewPayment = arenewPayment;
		this.vyear = vyear;
	}

	public ArenewVessels(ArenewVesselsId id, /*ArenewEntity arenewEntity,
                         ArenewPayment arenewPayment,*/ String name, String regNum,
                         String yearBuilt, String makeModel, String length, String fee,
                         String grossTons, String netTons, String homeportCity,
                         String engineType, String horsepower, String estValue,
                         String hullType, String hullId, String fuel, String holdTank,
                         String liveTank, String refrigeration, String freezerCanner,
                         String tenderPacker, String fishingboat,
                         String purseseine, String beachseine, String driftgillnet,
                         String setgillnet, String handtroll, String longline,
                         String singleottertrawl, String fishwheel, String potgear,
                         String ringnet, String divegear, String powertroll,
                         String beamtrawl, String dredge, String dinglebar, String jig,
                         String doubleottertrawl, String hearinggillnet, String pairtrawl,
                         Date receiveddate, String homeportState, String foerignFlag,
                         String transporter, String othergear, String status,
                         String vyear, String registrationType,
                         String salmontrollReg, String salmontrollDate,
                         String salmonregArea, String permitSerial1, String permitSerial2,
                         Date lastupdated) {
		this.id = id;
		//this.arenewEntity = arenewEntity;
		//this.arenewPayment = arenewPayment;
		this.name = name;
		this.regNum = regNum;
		this.yearBuilt = yearBuilt;
		this.makeModel = makeModel;
		this.length = length;
		this.fee = fee;
		this.grossTons = grossTons;
		this.netTons = netTons;
		this.homeportCity = homeportCity;
		this.engineType = engineType;
		this.horsepower = horsepower;
		this.estValue = estValue;
		this.hullType = hullType;
		this.hullId = hullId;
		this.fuel = fuel;
		this.holdTank = holdTank;
		this.liveTank = liveTank;
		this.refrigeration = refrigeration;
		this.freezerCanner = freezerCanner;
		this.tenderPacker = tenderPacker;
		this.fishingboat = fishingboat;
		this.purseseine = purseseine;
		this.beachseine = beachseine;
		this.driftgillnet = driftgillnet;
		this.setgillnet = setgillnet;
		this.handtroll = handtroll;
		this.longline = longline;
		this.singleottertrawl = singleottertrawl;
		this.fishwheel = fishwheel;
		this.potgear = potgear;
		this.ringnet = ringnet;
		this.divegear = divegear;
		this.powertroll = powertroll;
		this.beamtrawl = beamtrawl;
		this.dredge = dredge;
		this.dinglebar = dinglebar;
		this.jig = jig;
		this.doubleottertrawl = doubleottertrawl;
		this.hearinggillnet = hearinggillnet;
		this.pairtrawl = pairtrawl;
		this.receiveddate = receiveddate;
		this.homeportState = homeportState;
		this.foerignFlag = foerignFlag;
		this.transporter = transporter;
		this.othergear = othergear;
		this.status = status;
		this.vyear = vyear;
		this.registrationType = registrationType;
		this.salmontrollReg = salmontrollReg;
		this.salmontrollDate = salmontrollDate;
		this.salmonregArea = salmonregArea;
		this.permitSerial1 = permitSerial1;
		this.permitSerial2 = permitSerial2;
		this.lastupdated = lastupdated;
	}

	public ArenewVesselsId getId() {
		return this.id;
	}

	public void setId(ArenewVesselsId id) {
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegNum() {
		return this.regNum;
	}

	public void setRegNum(String regNum) {
		this.regNum = regNum;
	}

	public String getYearBuilt() {
		return this.yearBuilt;
	}

	public void setYearBuilt(String yearBuilt) {
		this.yearBuilt = yearBuilt;
	}

	public String getMakeModel() {
		return this.makeModel;
	}

	public void setMakeModel(String makeModel) {
		this.makeModel = makeModel;
	}

	public String getLength() {
		return this.length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getGrossTons() {
		return this.grossTons;
	}

	public void setGrossTons(String grossTons) {
		this.grossTons = grossTons;
	}

	public String getNetTons() {
		return this.netTons;
	}

	public void setNetTons(String netTons) {
		this.netTons = netTons;
	}

	public String getHomeportCity() {
		return this.homeportCity;
	}

	public void setHomeportCity(String homeportCity) {
		this.homeportCity = homeportCity;
	}

	public String getEngineType() {
		return this.engineType;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	public String getHorsepower() {
		return this.horsepower;
	}

	public void setHorsepower(String horsepower) {
		this.horsepower = horsepower;
	}

	public String getEstValue() {
		return this.estValue;
	}

	public void setEstValue(String estValue) {
		this.estValue = estValue;
	}

	public String getHullType() {
		return this.hullType;
	}

	public void setHullType(String hullType) {
		this.hullType = hullType;
	}

	public String getHullId() {
		return this.hullId;
	}

	public void setHullId(String hullId) {
		this.hullId = hullId;
	}

	public String getFuel() {
		return this.fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getHoldTank() {
		return this.holdTank;
	}

	public void setHoldTank(String holdTank) {
		this.holdTank = holdTank;
	}

	public String getLiveTank() {
		return this.liveTank;
	}

	public void setLiveTank(String liveTank) {
		this.liveTank = liveTank;
	}

	public String getRefrigeration() {
		return this.refrigeration;
	}

	public void setRefrigeration(String refrigeration) {
		this.refrigeration = refrigeration;
	}

	public String getFreezerCanner() {
		return this.freezerCanner;
	}

	public void setFreezerCanner(String freezerCanner) {
		this.freezerCanner = freezerCanner;
	}

	public String getTenderPacker() {
		return this.tenderPacker;
	}

	public void setTenderPacker(String tenderPacker) {
		this.tenderPacker = tenderPacker;
	}

	public String getFishingboat() {
		return this.fishingboat;
	}

	public void setFishingboat(String fishingboat) {
		this.fishingboat = fishingboat;
	}

	public String getPurseseine() {
		return this.purseseine;
	}

	public void setPurseseine(String purseseine) {
		this.purseseine = purseseine;
	}

	public String getBeachseine() {
		return this.beachseine;
	}

	public void setBeachseine(String beachseine) {
		this.beachseine = beachseine;
	}

	public String getDriftgillnet() {
		return this.driftgillnet;
	}

	public void setDriftgillnet(String driftgillnet) {
		this.driftgillnet = driftgillnet;
	}

	public String getSetgillnet() {
		return this.setgillnet;
	}

	public void setSetgillnet(String setgillnet) {
		this.setgillnet = setgillnet;
	}

	public String getHandtroll() {
		return this.handtroll;
	}

	public void setHandtroll(String handtroll) {
		this.handtroll = handtroll;
	}

	public String getLongline() {
		return this.longline;
	}

	public void setLongline(String longline) {
		this.longline = longline;
	}

	public String getSingleottertrawl() {
		return this.singleottertrawl;
	}

	public void setSingleottertrawl(String singleottertrawl) {
		this.singleottertrawl = singleottertrawl;
	}

	public String getFishwheel() {
		return this.fishwheel;
	}

	public void setFishwheel(String fishwheel) {
		this.fishwheel = fishwheel;
	}

	public String getPotgear() {
		return this.potgear;
	}

	public void setPotgear(String potgear) {
		this.potgear = potgear;
	}

	public String getRingnet() {
		return this.ringnet;
	}

	public void setRingnet(String ringnet) {
		this.ringnet = ringnet;
	}

	public String getDivegear() {
		return this.divegear;
	}

	public void setDivegear(String divegear) {
		this.divegear = divegear;
	}

	public String getPowertroll() {
		return this.powertroll;
	}

	public void setPowertroll(String powertroll) {
		this.powertroll = powertroll;
	}

	public String getBeamtrawl() {
		return this.beamtrawl;
	}

	public void setBeamtrawl(String beamtrawl) {
		this.beamtrawl = beamtrawl;
	}

	public String getDredge() {
		return this.dredge;
	}

	public void setDredge(String dredge) {
		this.dredge = dredge;
	}

	public String getDinglebar() {
		return this.dinglebar;
	}

	public void setDinglebar(String dinglebar) {
		this.dinglebar = dinglebar;
	}

	public String getJig() {
		return this.jig;
	}

	public void setJig(String jig) {
		this.jig = jig;
	}

	public String getDoubleottertrawl() {
		return this.doubleottertrawl;
	}

	public void setDoubleottertrawl(String doubleottertrawl) {
		this.doubleottertrawl = doubleottertrawl;
	}

	public String getHearinggillnet() {
		return this.hearinggillnet;
	}

	public void setHearinggillnet(String hearinggillnet) {
		this.hearinggillnet = hearinggillnet;
	}

	public String getPairtrawl() {
		return this.pairtrawl;
	}

	public void setPairtrawl(String pairtrawl) {
		this.pairtrawl = pairtrawl;
	}

	public Date getReceiveddate() {
		return this.receiveddate;
	}

	public void setReceiveddate(Date receiveddate) {
		this.receiveddate = receiveddate;
	}

	public String getHomeportState() {
		return this.homeportState;
	}

	public void setHomeportState(String homeportState) {
		this.homeportState = homeportState;
	}

	public String getFoerignFlag() {
		return this.foerignFlag;
	}

	public void setFoerignFlag(String foerignFlag) {
		this.foerignFlag = foerignFlag;
	}

	public String getTransporter() {
		return this.transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public String getOthergear() {
		return this.othergear;
	}

	public void setOthergear(String othergear) {
		this.othergear = othergear;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVyear() {
		return this.vyear;
	}

	public void setVyear(String vyear) {
		this.vyear = vyear;
	}

	public String getRegistrationType() {
		return this.registrationType;
	}

	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}

	public String getSalmontrollReg() {
		return this.salmontrollReg;
	}

	public void setSalmontrollReg(String salmontrollReg) {
		this.salmontrollReg = salmontrollReg;
	}

	public String getSalmonregArea() {
		return this.salmonregArea;
	}

	public void setSalmonregArea(String salmonregArea) {
		this.salmonregArea = salmonregArea;
	}

	public String getPermitSerial1() {
		return this.permitSerial1;
	}

	public void setPermitSerial1(String permitSerial1) {
		this.permitSerial1 = permitSerial1;
	}

	public String getPermitSerial2() {
		return this.permitSerial2;
	}

	public void setPermitSerial2(String permitSerial2) {
		this.permitSerial2 = permitSerial2;
	}

	public Date getLastupdated() {
		return this.lastupdated;
	}

	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}

	public String getConfirmcode() {
		return confirmcode;
	}

	public void setConfirmcode(String confirmcode) {
		this.confirmcode = confirmcode;
	}

	public String getLengthFeet() {
		return lengthFeet;
	}

	public void setLengthFeet(String lengthFeet) {
		this.lengthFeet = lengthFeet;
	}

	public String getLengthInches() {
		return lengthInches;
	}

	public void setLengthInches(String lengthInches) {
		this.lengthInches = lengthInches;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public boolean isNewVessel() {
		return newVessel;
	}

	public void setNewVessel(boolean newVessel) {
		this.newVessel = newVessel;
	}

	public boolean isRenewed() {
		return renewed;
	}

	public void setRenewed(boolean renewed) {
		this.renewed = renewed;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}	

	public String getSalmontrollDate() {
		return salmontrollDate;
	}

	public void setSalmontrollDate(String salmontrollDate) {
		this.salmontrollDate = salmontrollDate;
	}	

	public String getQ1() {
		return q1;
	}

	public void setQ1(String q1) {
		this.q1 = q1;
	}

	public String getQ2() {
		return q2;
	}

	public void setQ2(String q2) {
		this.q2 = q2;
	}

	public String getQ3() {
		return q3;
	}

	public void setQ3(String q3) {
		this.q3 = q3;
	}

	public String getQ4() {
		return q4;
	}

	public void setQ4(String q4) {
		this.q4 = q4;
	}

	public String getQ5() {
		return q5;
	}

	public void setQ5(String q5) {
		this.q5 = q5;
	}

	public String getQ6() {
		return q6;
	}

	public void setQ6(String q6) {
		this.q6 = q6;
	}

	public String getQ7() {
		return q7;
	}

	public void setQ7(String q7) {
		this.q7 = q7;
	}

	public String getQ8() {
		return q8;
	}

	public void setQ8(String q8) {
		this.q8 = q8;
	}

	public String getQ9() {
		return q9;
	}

	public void setQ9(String q9) {
		this.q9 = q9;
	}

	public String getQ10() {
		return q10;
	}

	public void setQ10(String q10) {
		this.q10 = q10;
	}

	public String getQ11() {
		return q11;
	}

	public void setQ11(String q11) {
		this.q11 = q11;
	}

	public String getIsnew() {
		return isnew;
	}

	public void setIsnew(String isnew) {
		this.isnew = isnew;
	}

	public String getDelorig() {
		return delorig;
	}

	public void setDelorig(String delorig) {
		this.delorig = delorig;
	}

	public String getInches() {
		return inches;
	}

	public void setInches(String inches) {
		this.inches = inches;
	}

	public boolean isNewrenew() {
		return newrenew;
	}

	public void setNewrenew(boolean newrenew) {
		this.newrenew = newrenew;
	}

	public int compareTo(ArenewVessels ves) {
		try {
		/*
		 * This is a sort feature for return permit list. It compares Vessel Names first, 
		 * then lastnames, and organizes the list to be easier to read. Even though we
		 * are in the BbPermit class, the search is organized by the Misc object, since
		 * at times it is possible for a person to be on a different vessel than the one
		 * that was originally registered with the permit.
		 */
		
		return this.getId().getAdfg().compareTo(ves.getId().getAdfg());
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
