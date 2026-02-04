package com.cfecweb.leon.dto;

import com.extjs.gxt.ui.client.data.BeanModelTag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gwt.user.client.rpc.IsSerializable;

/*
 * The fee totals class contains a group of getters and setters to maintain a persons fee status throughout the application.
 */
public class FeeTotals implements BeanModelTag, IsSerializable {
	private double resFishingPermits;
	private double nonresFishingPermits;
	private double resDifferential;
	private double nonresDifferential;
	private double resShipping;
	private double nonresShipping;
	private double resVessels;
	private double nonresVessels;
	private double resTotals;
	private double nonresTotals;
	private int diffc = 0;
	private int diffp = 0;
	private int diffp2 = 0;
	private int pmt = 0;
	private int ves = 0;
	
	public int getPmt() {
		return pmt;
	}
	public void setPmt(int pmt) {
		this.pmt = pmt;
	}
	public int getVes() {
		return ves;
	}
	public void setVes(int ves) {
		this.ves = ves;
	}
    @JsonIgnore
	public int pmtPlus() {
		pmt = (pmt + 1);
		return pmt;
	}
    @JsonIgnore
	public int pmtMinus() {
		pmt = (pmt - 1);
		return pmt;
	}
    @JsonIgnore
	public int vesPlus() {
		ves = (ves + 1);
		return ves;
	}
    @JsonIgnore
	public int vesMinus() {
		ves = (ves - 1);
		return ves;
	}
	public int getDiffc() {
		return diffc;
	}
	public void setDiffc(int diffc) {
		this.diffc = diffc;
	}
    @JsonIgnore
	public int diffPlusc() {
		diffc = (diffc + 1);
		return diffc;
	}
    @JsonIgnore
	public int diffMinusc() {
		diffc = (diffc - 1);
		return diffc;
	}	
	public int getDiffp() {
		return diffp;
	}
	public void setDiffp(int diffp) {
		this.diffp = diffp;
	}
    @JsonIgnore
	public int diffPlusp() {
		diffp = (diffp + 1);
		return diffp;
	}
    @JsonIgnore
	public int diffMinusp() {
		diffp = (diffp - 1);
		return diffp;
	}	
	public int getDiffp2() {
		return diffp2;
	}
	public void setDiffp2(int diffp2) {
		this.diffp2 = diffp2;
	}
    @JsonIgnore
	public int diffPlusp2() {
		diffp2 = (diffp2 + 1);
		return diffp2;
	}
    @JsonIgnore
	public int diffMinusp2() {
		diffp2 = (diffp2 - 1);
		return diffp2;
	}	
	public double getResFishingPermits() {
		return resFishingPermits;
	}
	public void setResFishingPermits(double resFishingPermits) {
		this.resFishingPermits = resFishingPermits;
	}
	public double getNonresFishingPermits() {
		return nonresFishingPermits;
	}
	public void setNonresFishingPermits(double nonresFishingPermits) {
		this.nonresFishingPermits = nonresFishingPermits;
	}
	public double getResDifferential() {
		return resDifferential;
	}
	public void setResDifferential(double resDifferential) {
		this.resDifferential = resDifferential;
	}
	public double getNonresDifferential() {
		return nonresDifferential;
	}
	public void setNonresDifferential(double nonresDifferential) {
		this.nonresDifferential = nonresDifferential;
	}
	public double getResShipping() {
		return resShipping;
	}
	public void setResShipping(double resShipping) {
		this.resShipping = resShipping;
	}
	public double getNonresShipping() {
		return nonresShipping;
	}
	public void setNonresShipping(double nonresShipping) {
		this.nonresShipping = nonresShipping;
	}
	public double getResVessels() {
		return resVessels;
	}
	public void setResVessels(double resVessels) {
		this.resVessels = resVessels;
	}
	public double getNonresVessels() {
		return nonresVessels;
	}
	public void setNonresVessels(double nonresVessels) {
		this.nonresVessels = nonresVessels;
	}
	public double getResTotals() {
		return resTotals;
	}
	public void setResTotals(double resTotals) {
		this.resTotals = resTotals;
	}
	public double getNonresTotals() {
		return nonresTotals;
	}
	public void setNonresTotals(double nonresTotals) {
		this.nonresTotals = nonresTotals;
	}

    @JsonIgnore
	public String getResTotal() {
		double restot = (resFishingPermits + resVessels + resShipping);
		String rtot = Double.toString(restot);
		if (rtot.indexOf(".") != -1) {
			rtot = round(rtot);
		}
		return rtot;
	}

    @JsonIgnore
	public String getNonResTotal() {
		double nonrestot = (nonresFishingPermits + nonresDifferential + nonresVessels + nonresShipping);
		String ntot = Double.toString(nonrestot);
		if (ntot.indexOf(".") != -1) {
			ntot = round(ntot);
		}
		return ntot;
	}

    @JsonIgnore
	public String getFeeTotals(String res) {
		StringBuffer ft = new StringBuffer();
		if (res.equalsIgnoreCase("resident") || res.equalsIgnoreCase("R")) {
			double restot = (resFishingPermits + resDifferential + resVessels + resShipping);
			String rtot = Double.toString(restot);
			ft.append("<table width='100%' border='0' cellspacing='0' align='center'>");
			ft.append("<tr><td class='boldblack12' width='15%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Alaska Resident</td>" +
				"<td align='center' class='regblack12' width='22%'>Fishing Permits:&nbsp;&nbsp$<span id='rfp'><i>"+resFishingPermits+"</i></span></td>" +
				"<td align='center' class='regblack12' width='16%'>Non-Res Differential:&nbsp;&nbsp;$<span id='rd'><i>"+resDifferential+"</i></span></td>" +
				"<td align='center' class='regblack12' width='14%'>Vessels:&nbsp;&nbsp;$<span id='rv'><i>"+resVessels+"</i></span></td>" +
				"<td align='center' class='regblack12' width='18%'>Shipping:&nbsp;&nbsp;$<span id ='rs'><i>"+resShipping+"</i></span></td>" +
				"<td align='center' class='regblack12' width='15%'>Total:&nbsp;&nbsp;$<span id='rt'><i>"+rtot+"</i></span></td></tr></table>");
		} else {
			double nonrestot = (nonresFishingPermits + nonresDifferential + nonresVessels + nonresShipping);
			String ntot = Double.toString(nonrestot);
			ft.append("<table width='100%' border='0' cellspacing='0' align='center'>");
			ft.append("<tr><td class='boldblack12' width='15%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Nonresident</td>" +
				"<td align='center' class='regblack12' width='22%'>Fishing Permits:&nbsp;&nbsp$<span id='nfp'><i>"+nonresFishingPermits+"</i></span></td>" +
				"<td align='center' class='regblack12' width='16%'>Non-Res Differential:&nbsp;&nbsp;$<span id='nd'><i>"+nonresDifferential+"</i></span></td>" +
				"<td align='center' class='regblack12' width='14%'>Vessels:&nbsp;&nbsp;$<span id='nv'><i>"+nonresVessels+"</i></span></td>" +
				"<td align='center' class='regblack12' width='18%'>Shipping:&nbsp;&nbsp;$<span id ='ns'><i>"+nonresShipping+"</i></span></td>" +
				"<td align='center' class='regblack12' width='15%'>Total:&nbsp;&nbsp;$<span id='nt'><i>"+ntot+"</i></span></td></tr></table>");
		}
		return ft.toString();
	}
	
	/*
	 * I was having precision issues with doubles when I introduced the standard flat rate shipping fee of 22.68
	 * Instead of going through a hundred lines of code, all I really needed to do was check for remainders that
	 * exceed 2 characters, then round appropriately and send back the correct string.
	 * 
	 * Keep in mind, the GWT 2.x client software does NOT support certain java.math functions and therefore does NOT
	 * allow me to handle doubles in a normal methodology with regard to precision. This is the easiest and fastest way.
	 */
    @JsonIgnore
	public static String round(String tot) {
		String newtotal = null;
		if (tot.indexOf(".") != -1) {
			newtotal = null;
			String remain = tot.substring(tot.indexOf(".")+1, tot.length());
			String begin = tot.substring(0, tot.indexOf("."));
			int total = remain.length();
			if (remain.trim().length() == 1) {
				newtotal = tot + "0";
			} else if (remain.trim().length() == 2) {
				newtotal = tot;
			} else {				
				int firstI = Integer.parseInt(remain.substring(0, 1));
				int secondI = Integer.parseInt(remain.substring(1, 2));
				int thirdI = Integer.parseInt(remain.substring(2, 3));
				if (total > 2) {
					if (thirdI >= 5) {
						newtotal = begin+"."+Integer.toString(firstI)+Integer.toString(secondI+1);
					} else {
						newtotal = begin+"."+Integer.toString(firstI)+Integer.toString(secondI);
					}					
				} else {
					newtotal = tot;
				}
			}							
		} else {
			newtotal = tot;
		}		
		return newtotal;
	}
}
