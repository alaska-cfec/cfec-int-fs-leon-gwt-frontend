package com.cfecweb.leon.shared;

import java.io.Serializable;
import java.util.Date;

public class GWTVessels implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cfecid;
	private String ryear;
	private String adfg;	
	private String confirmcode;	
	private String vname;
	private String regNum;
	private String yearBuilt;
	private String makeModel;
	private String lengthFeet;
	private String lengthInches;
	private double fee;
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
	private String charterboat;
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
	private String operator;
	private String vyear;
	private String registrationType;
	private String salmontrollReg;
	private Date salmontrollDate;
	private String salmonregArea;
	private String permitSerial1;
	private String permitSerial2;
	private Date feepaidDate;
	private Date lastupdated;
	private boolean newVessel;
	private boolean renewed;
	private String details;
	// These next fields are used only for user added Vessels (new)
	private String metalPlate;
	private String plateADFG;
	private String whenPurchased;
	private String fromWhom;
	private String otherNumbers;
	private String commfishAlaska;
	private String commfishYears;
	private String otherName;
	private String otherNameText;
	private String vesModified;
	private String vesModifiedText;	

	public GWTVessels() {
		super();
	}		

	public GWTVessels(String cfecid, String confirmcode, String ryear,
			String adfg, String vname, String regNum, String yearBuilt,
			String makeModel, String lengthFeet, String lengthInches, double fee, String grossTons,
			String netTons, String homeportCity, String engineType,
			String horsepower, String estValue, String hullType, String hullId,
			String fuel, String holdTank, String liveTank,
			String refrigeration, String freezerCanner, String tenderPacker,
			String charterboat, String fishingboat, String purseseine,
			String beachseine, String driftgillnet, String setgillnet,
			String handtroll, String longline, String singleottertrawl,
			String fishwheel, String potgear, String ringnet, String divegear,
			String powertroll, String beamtrawl, String dredge,
			String dinglebar, String jig, String doubleottertrawl,
			String hearinggillnet, String pairtrawl, Date receiveddate,
			String homeportState, String foerignFlag, String transporter,
			String othergear, String status, String operator, String vyear,
			String registrationType, String salmontrollReg, boolean newVessel,
			Date salmontrollDate, String salmonregArea, String permitSerial1,
			String permitSerial2, Date lastupdated, boolean renewed,
			String details, String metalPlate, String plateADFG, Date feepaidDate,
			String whenPurchased, String fromWhom, String otherNumbers,
			String commfishAlaska, String commfishYears, String otherName,
			String otherNameText, String vesModified, String vesModifiedText) {
		super();
		this.cfecid = cfecid;
		this.confirmcode = confirmcode;
		this.ryear = ryear;
		this.adfg = adfg;
		this.vname = vname;
		this.regNum = regNum;
		this.yearBuilt = yearBuilt;
		this.makeModel = makeModel;
		this.lengthFeet = lengthFeet;
		this.lengthInches = lengthInches;
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
		this.charterboat = charterboat;
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
		this.operator = operator;
		this.vyear = vyear;
		this.registrationType = registrationType;
		this.salmontrollReg = salmontrollReg;
		this.salmontrollDate = salmontrollDate;
		this.salmonregArea = salmonregArea;
		this.permitSerial1 = permitSerial1;
		this.permitSerial2 = permitSerial2;
		this.feepaidDate = feepaidDate;
		this.lastupdated = lastupdated;
		this.renewed = renewed;
		this.details = details;
		this.metalPlate = metalPlate;
		this.plateADFG = plateADFG;
		this.whenPurchased = whenPurchased;
		this.fromWhom = fromWhom;
		this.otherNumbers = otherNumbers;
		this.commfishAlaska = commfishAlaska;
		this.commfishYears = commfishYears;
		this.otherName = otherName;
		this.otherNameText = otherNameText;
		this.vesModified = vesModified;
		this.vesModifiedText = vesModifiedText;
		this.newVessel = newVessel;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public boolean isRenewed() {
		return renewed;
	}

	public void setRenewed(boolean renewed) {
		this.renewed = renewed;
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

	public String getRyear() {
		return ryear;
	}

	public void setRyear(String ryear) {
		this.ryear = ryear;
	}

	public String getAdfg() {
		return adfg;
	}

	public void setAdfg(String adfg) {
		this.adfg = adfg;
	}

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public String getRegNum() {
		return regNum;
	}

	public void setRegNum(String regNum) {
		this.regNum = regNum;
	}

	public String getYearBuilt() {
		return yearBuilt;
	}

	public void setYearBuilt(String yearBuilt) {
		this.yearBuilt = yearBuilt;
	}

	public String getMakeModel() {
		return makeModel;
	}

	public void setMakeModel(String makeModel) {
		this.makeModel = makeModel;
	}

	public double getFee() {
		return fee;
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

	public void setFee(double fee) {
		this.fee = fee;
	}

	public String getGrossTons() {
		return grossTons;
	}

	public void setGrossTons(String grossTons) {
		this.grossTons = grossTons;
	}

	public String getNetTons() {
		return netTons;
	}

	public void setNetTons(String netTons) {
		this.netTons = netTons;
	}

	public String getHomeportCity() {
		return homeportCity;
	}

	public void setHomeportCity(String homeportCity) {
		this.homeportCity = homeportCity;
	}

	public String getEngineType() {
		return engineType;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	public String getHorsepower() {
		return horsepower;
	}

	public void setHorsepower(String horsepower) {
		this.horsepower = horsepower;
	}

	public String getEstValue() {
		return estValue;
	}

	public void setEstValue(String estValue) {
		this.estValue = estValue;
	}

	public String getHullType() {
		return hullType;
	}

	public void setHullType(String hullType) {
		this.hullType = hullType;
	}

	public String getHullId() {
		return hullId;
	}

	public void setHullId(String hullId) {
		this.hullId = hullId;
	}

	public String getFuel() {
		return fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getHoldTank() {
		return holdTank;
	}

	public void setHoldTank(String holdTank) {
		this.holdTank = holdTank;
	}

	public String getLiveTank() {
		return liveTank;
	}

	public void setLiveTank(String liveTank) {
		this.liveTank = liveTank;
	}

	public String getRefrigeration() {
		return refrigeration;
	}

	public void setRefrigeration(String refrigeration) {
		this.refrigeration = refrigeration;
	}

	public String getFreezerCanner() {
		return freezerCanner;
	}

	public void setFreezerCanner(String freezerCanner) {
		this.freezerCanner = freezerCanner;
	}

	public String getTenderPacker() {
		return tenderPacker;
	}

	public void setTenderPacker(String tenderPacker) {
		this.tenderPacker = tenderPacker;
	}

	public String getCharterboat() {
		return charterboat;
	}

	public void setCharterboat(String charterboat) {
		this.charterboat = charterboat;
	}

	public String getFishingboat() {
		return fishingboat;
	}

	public void setFishingboat(String fishingboat) {
		this.fishingboat = fishingboat;
	}

	public String getPurseseine() {
		return purseseine;
	}

	public void setPurseseine(String purseseine) {
		this.purseseine = purseseine;
	}

	public String getBeachseine() {
		return beachseine;
	}

	public void setBeachseine(String beachseine) {
		this.beachseine = beachseine;
	}

	public String getDriftgillnet() {
		return driftgillnet;
	}

	public void setDriftgillnet(String driftgillnet) {
		this.driftgillnet = driftgillnet;
	}

	public String getSetgillnet() {
		return setgillnet;
	}

	public void setSetgillnet(String setgillnet) {
		this.setgillnet = setgillnet;
	}

	public String getHandtroll() {
		return handtroll;
	}

	public void setHandtroll(String handtroll) {
		this.handtroll = handtroll;
	}

	public String getLongline() {
		return longline;
	}

	public void setLongline(String longline) {
		this.longline = longline;
	}

	public String getSingleottertrawl() {
		return singleottertrawl;
	}

	public void setSingleottertrawl(String singleottertrawl) {
		this.singleottertrawl = singleottertrawl;
	}

	public String getFishwheel() {
		return fishwheel;
	}

	public void setFishwheel(String fishwheel) {
		this.fishwheel = fishwheel;
	}

	public String getPotgear() {
		return potgear;
	}

	public void setPotgear(String potgear) {
		this.potgear = potgear;
	}

	public String getRingnet() {
		return ringnet;
	}

	public void setRingnet(String ringnet) {
		this.ringnet = ringnet;
	}

	public String getDivegear() {
		return divegear;
	}

	public void setDivegear(String divegear) {
		this.divegear = divegear;
	}

	public String getPowertroll() {
		return powertroll;
	}

	public void setPowertroll(String powertroll) {
		this.powertroll = powertroll;
	}

	public String getBeamtrawl() {
		return beamtrawl;
	}

	public void setBeamtrawl(String beamtrawl) {
		this.beamtrawl = beamtrawl;
	}

	public String getDredge() {
		return dredge;
	}

	public void setDredge(String dredge) {
		this.dredge = dredge;
	}

	public String getDinglebar() {
		return dinglebar;
	}

	public void setDinglebar(String dinglebar) {
		this.dinglebar = dinglebar;
	}

	public String getJig() {
		return jig;
	}

	public void setJig(String jig) {
		this.jig = jig;
	}

	public String getDoubleottertrawl() {
		return doubleottertrawl;
	}

	public void setDoubleottertrawl(String doubleottertrawl) {
		this.doubleottertrawl = doubleottertrawl;
	}

	public String getHearinggillnet() {
		return hearinggillnet;
	}

	public void setHearinggillnet(String hearinggillnet) {
		this.hearinggillnet = hearinggillnet;
	}

	public String getPairtrawl() {
		return pairtrawl;
	}

	public void setPairtrawl(String pairtrawl) {
		this.pairtrawl = pairtrawl;
	}

	public Date getReceiveddate() {
		return receiveddate;
	}

	public void setReceiveddate(Date receiveddate) {
		this.receiveddate = receiveddate;
	}

	public String getHomeportState() {
		return homeportState;
	}

	public void setHomeportState(String homeportState) {
		this.homeportState = homeportState;
	}

	public String getFoerignFlag() {
		return foerignFlag;
	}

	public void setFoerignFlag(String foerignFlag) {
		this.foerignFlag = foerignFlag;
	}

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public String getOthergear() {
		return othergear;
	}

	public void setOthergear(String othergear) {
		this.othergear = othergear;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getVyear() {
		return vyear;
	}

	public void setVyear(String vyear) {
		this.vyear = vyear;
	}

	public String getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}

	public String getSalmontrollReg() {
		return salmontrollReg;
	}

	public void setSalmontrollReg(String salmontrollReg) {
		this.salmontrollReg = salmontrollReg;
	}

	public Date getSalmontrollDate() {
		return salmontrollDate;
	}

	public void setSalmontrollDate(Date salmontrollDate) {
		this.salmontrollDate = salmontrollDate;
	}

	public String getSalmonregArea() {
		return salmonregArea;
	}

	public void setSalmonregArea(String salmonregArea) {
		this.salmonregArea = salmonregArea;
	}

	public String getPermitSerial1() {
		return permitSerial1;
	}

	public void setPermitSerial1(String permitSerial1) {
		this.permitSerial1 = permitSerial1;
	}

	public String getPermitSerial2() {
		return permitSerial2;
	}

	public void setPermitSerial2(String permitSerial2) {
		this.permitSerial2 = permitSerial2;
	}

	public Date getLastupdated() {
		return lastupdated;
	}

	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}
	
	public String getMetalPlate() {
		return metalPlate;
	}

	public void setMetalPlate(String metalPlate) {
		this.metalPlate = metalPlate;
	}

	public String getPlateADFG() {
		return plateADFG;
	}

	public void setPlateADFG(String plateADFG) {
		this.plateADFG = plateADFG;
	}

	public String getWhenPurchased() {
		return whenPurchased;
	}

	public void setWhenPurchased(String whenPurchased) {
		this.whenPurchased = whenPurchased;
	}

	public String getFromWhom() {
		return fromWhom;
	}

	public void setFromWhom(String fromWhom) {
		this.fromWhom = fromWhom;
	}

	public String getOtherNumbers() {
		return otherNumbers;
	}

	public void setOtherNumbers(String otherNumbers) {
		this.otherNumbers = otherNumbers;
	}

	public String getCommfishAlaska() {
		return commfishAlaska;
	}

	public void setCommfishAlaska(String commfishAlaska) {
		this.commfishAlaska = commfishAlaska;
	}

	public String getCommfishYears() {
		return commfishYears;
	}

	public void setCommfishYears(String commfishYears) {
		this.commfishYears = commfishYears;
	}

	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public String getOtherNameText() {
		return otherNameText;
	}

	public void setOtherNameText(String otherNameText) {
		this.otherNameText = otherNameText;
	}

	public String getVesModified() {
		return vesModified;
	}

	public void setVesModified(String vesModified) {
		this.vesModified = vesModified;
	}

	public String getVesModifiedText() {
		return vesModifiedText;
	}

	public void setVesModifiedText(String vesModifiedText) {
		this.vesModifiedText = vesModifiedText;
	}
	
	public Date getFeepaidDate() {
		return feepaidDate;
	}

	public void setFeepaidDate(Date feepaidDate) {
		this.feepaidDate = feepaidDate;
	}

	public boolean isNewVessel() {
		return newVessel;
	}

	public void setNewVessel(boolean newVessel) {
		this.newVessel = newVessel;
	}

}
