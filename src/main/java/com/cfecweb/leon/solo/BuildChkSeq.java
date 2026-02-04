package com.cfecweb.leon.solo;

import java.io.IOException;

import com.google.zxing.WriterException;

public class BuildChkSeq {
	static String fishery = null;
	static String pserial = null;
	static String pserialchk = null;
	static String pyear = null;
	static String pyearseq = null;
	static String pyearchk = null;
	static String name = null;
	static String cfecid = null;
	static String vname = null;
	static String adfg = null;
	static String pdob = null;
	static String fisherydesc = null;
	static String residency = null;
	static String newline = null;
	
	public static void main(String[] args) {
		//CFEC Fishery,Permit,Year Seq,CFEC Permit Holder,CFEC File Number,Vessel name,Vessel ADFG,Fishers DOB,Fishery Description
		CheckDigitCalculator cdc = new CheckDigitCalculator();
		StringGenerator stg = new StringGenerator();
		//PDFBarCode pbc = new PDFBarCode();
		fishery = "S03T";
		pserial = "11111";
		pserialchk = cdc.calculatePermitCheckCharacter(fishery, pserial).substring(5,6);
		pyear = "2020";
		pyearseq = "01";
		pyearchk = (cdc.calculateYearSeqCheckCharacter(pserial+pserialchk, pyear.substring(2,4)+pyearseq)).substring(4, 5);
		name = "Drifter John Q";
		cfecid = "222222";
		vname = "BRISTOL DRIFTER";
		adfg = "33333";
		pdob = "55";
		fisherydesc = "SALMON DRIFT GILLNET BRISTOL BAY";
		residency = "N";
		//String test1 = cdc.calculatePermitCheckCharacter("S04T", "55415");
		//System.out.println("Permit with Check Digit: " + test1);
		//String test2 = cdc.calculateYearSeqCheckCharacter(test1, "2001");
		//System.out.println("Year with Seq Check Digit: " + test2);
		//String tempLine = fishery+","+pserial+pserialchk+","+pyear.substring(2, 4)+pyearseq+pyearchk+","+name+","+cfecid+","+vname+","+adfg+","+pdob+","+fisherydesc;
		String tempLine = fishery+","+pserial+","+pyear.substring(2, 4)+pyearseq+pyearchk+","+name+","+cfecid+","+vname+","+adfg+","+pdob+","+fisherydesc+","+residency;
		System.out.println(tempLine);
		//String[] st = tempLine.split(",");
		//for (int x=0; x<st.length; x++) {
		//	System.out.println(st[x]);
		//}
		try {
			newline = stg.formatForMagStripeAndSave(tempLine);
			System.out.println(newline);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			PDFBarCode.generateQRCodeImage(newline, 350, 350, "/home/mcmity/scratch/pdf/test/MyQRCode.png");
		} catch (WriterException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getencode(String pfishery, String serial, String year, String yearseq, String ename, String filnum, 
			String vesname, String padfg, String birthdate, String fdesc, String eresidency) {
		String endcodeLine = null;
		CheckDigitCalculator cdc = new CheckDigitCalculator();
		StringGenerator stg = new StringGenerator();
		fishery = pfishery.replaceAll("\\s+","");
		pserial = serial;
		pserialchk = cdc.calculatePermitCheckCharacter(fishery, pserial).substring(5,6);
		pyear = year;
		pyearseq = yearseq;
		pyearchk = (cdc.calculateYearSeqCheckCharacter(pserial+pserialchk, pyear.substring(2,4)+pyearseq)).substring(4, 5);
		//name = ename;
		name = "SAWYER JUSTIN T";
		cfecid = filnum;
		vname = vesname;
		adfg = padfg;
		pdob = birthdate;
		fisherydesc = fdesc.replaceAll(",","");
		residency = eresidency;
		endcodeLine = fishery+","+pserial+","+pyear.substring(2, 4)+pyearseq+pyearchk+","+name+","+cfecid+","+vname+","+adfg+","+pdob+","+fisherydesc+","+residency;
		//System.out.println(endcodeLine);
		try {
			newline = stg.formatForMagStripeAndSave(endcodeLine);
			//System.out.println(newline);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newline;
	}

}
