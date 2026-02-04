package com.cfecweb.leon.solo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public class StringGenerator {

  // We do not use the OBI constant anymore
  final static String OBI = "999999";

  final static String CDF = "~DCC~";
  final static String EDF = "~EMB~";
  final static String R = "R"; // not truly a static.  This determines if the permit holder is a resident or not.  Use R for resident and N for non-Resident
  final static String FENC = "~ENC~";
  final static String START_SENTINAL_TRACK_ONE= "%";
  final static String VERSION_B = "B";  //probably not a true static
  final static String END_SENTINAL = "?";
  final static String START_SENTINAL_TRACK_TWO = ";";
  final static String END = "~END~@@@@@@";

  ArrayList<String> attributes = new ArrayList<String>();
  
  CheckDigitCalculator checkDigitCalculator = new CheckDigitCalculator();
  String csvFileName = null;
  String outputDirectoryLocation = null;

  public StringGenerator() {

  }
  
  @SuppressWarnings({ "rawtypes", "resource" })
public void processCSVFile() throws Exception {
    if(null == getCsvFileName()) {
      return;
    }
    if(null == getOutputDirectoryLocation()) {
      return;
    }
    File file = new File(getCsvFileName());
    String line = null;
    ArrayList<String> preparedStrings = new ArrayList<String>();
    try {

    //     The following commented out code helped me find the current directory, which I need to know
    //       to put the cvs file that it processes.
    //          File dir1 = new File (".");
    //          File dir2 = new File ("..");
    //          System.out.println ("Current dir : " + dir1.getCanonicalPath());
    //          System.out.println ("Parent  dir : " + dir2.getCanonicalPath());

      BufferedReader bufRdr = new BufferedReader(new FileReader(file));
      line = bufRdr.readLine();

      StringTokenizer st = new StringTokenizer(line, ",");

      while(st.hasMoreTokens()) {
        attributes.add(st.nextToken());
      }

      line = bufRdr.readLine();

      while(null != line)
      {
        st = new StringTokenizer(line, ",");
        String tempLine = "";
        while(st.hasMoreTokens())
        {
          //System.out.print("element: " + st.nextToken() + " ");
          tempLine = tempLine + st.nextToken();
          if(st.hasMoreTokens()) {
            tempLine = tempLine  + ",";
          }
        }
        //System.out.println(tempLine);
        preparedStrings.add(formatForMagStripeAndSave(tempLine));
        line = bufRdr.readLine();
      }
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    //String output
    Iterator iter = preparedStrings.iterator();
    while(iter.hasNext()) {
      System.out.println((String)iter.next());
    }
  }

  // formatForMagStripeAndSave method slaps together the pieces of the string into one string and saves it to a file
  @SuppressWarnings({ "rawtypes", "unchecked" })
public String formatForMagStripeAndSave(String line) throws Exception {
	ArrayList<String> titles = new ArrayList<String>();
	titles.add("CFEC Fishery");
	titles.add("Permit");
	titles.add("Year Seq");
	titles.add("CFEC Permit Holder");
	titles.add("CFEC File Number");
	titles.add("Vessel name");
	titles.add("Vessel ADFG");
	titles.add("Fishers DOB");
	titles.add("Fishery Description");
	titles.add("Residency");
	String[] sp = line.split(",");
	HashMap map = new HashMap();
    //StringTokenizer st = new StringTokenizer(line, ",");
    int count = 0;
    for (int x=0; x<sp.length; x++) {
		System.out.println(sp[x]);
		map.put(titles.get(count), sp[x]);
	    count++;
	}
    /*while(st.hasMoreTokens()) {
      map.put(attributes.get(count), st.nextToken());
      count++;
    }
    if (count < 2) {
    	throw new Exception("Issue reading CSV file, check format.");
    }*/
    map.put("Permit", addLeadingZeros((String)map.get("Permit"), 5));
    map.put("Permit", checkDigitCalculator.calculatePermitCheckCharacter((String)map.get("CFEC Fishery"), (String)map.get("Permit")));
    map.put("Year Seq", checkDigitCalculator.calculateYearSeqCheckCharacter((String)map.get("Permit"), (String)map.get("Year Seq")));
    
    map.put("Vessel ADFG", addLeadingZeros((String)map.get("Vessel ADFG"), 5));
    map.put("CFEC File Number", addLeadingZeros((String)map.get("CFEC File Number"), 6));
    
    String tempLine = "";
    String temp = "";
    temp += (String)map.get("Permit");
    temp =  temp.substring(0,6);
    tempLine += temp;

    //tempLine += CDF + EDF + R + " ";
    tempLine += CDF + EDF;
    temp = (String)map.get("Residency") + " ";
    tempLine += temp;

    temp = (String)map.get("Vessel name");
    tempLine += addTrailingSpaces(temp, 26);
    tempLine += "\"";

    temp = (String)map.get("Fishery Description");
    tempLine += addTrailingSpaces(temp, 29);
    tempLine += "\"";

    temp = (String)map.get("CFEC Permit Holder");
    temp = temp.toUpperCase();
    tempLine += addTrailingSpaces(temp, 20);
    tempLine += "\"";

    temp = (String)map.get("CFEC Fishery");
    temp += " ";
    temp += (String)map.get("Permit");
    temp += " " + (String)map.get("Vessel ADFG");
    tempLine += addTrailingSpaces(temp, 20);
    tempLine += "\"";

    temp = (String)map.get("Year Seq") + " ";
    temp += (String)map.get("CFEC File Number") + "    ";
    temp += (String)map.get("Fishers DOB");
    tempLine += addTrailingSpaces(temp, 20);
    tempLine += "\"";

    tempLine += FENC + START_SENTINAL_TRACK_ONE + VERSION_B;
    temp = (String)map.get("CFEC Fishery") + "  " + (String)map.get("Permit") + "^";
    tempLine += temp;

    temp = (String)map.get("CFEC Permit Holder");
    temp = temp.toUpperCase();
    tempLine += addTrailingSpaces(temp, 28);
    tempLine += "^";

    temp = (String)map.get("Year Seq");
    temp += (String)map.get("Vessel name");
    tempLine += addTrailingSpaces(temp, 31);

    temp = END_SENTINAL + "L" + START_SENTINAL_TRACK_TWO;
    tempLine += temp;

    temp = (String)map.get("Permit");
    temp = temp.substring(0,5);
    tempLine += temp;

    temp = (String)map.get("Year Seq");
    temp = temp.substring(0,4);
    tempLine += temp;

    temp = (String)map.get("CFEC File Number");
    tempLine += temp;

    temp = (String)map.get("Vessel ADFG");
    tempLine += temp;

    tempLine += END_SENTINAL + "L" + END;
    
    //String fileName = (String)map.get("CFEC Fishery") + (String)map.get("Permit") + ".txt";
    
    //Loop through the quantity on the csv and build bat file and sh file
    //for each line.  The bat / sh file sends the cards to embosser in DOS or Linux environment. 
    /*String quantity = (String)map.get("Quantity");
    if(quantity != null && !quantity.equalsIgnoreCase(""))
    {
      quantity = quantity.trim();
      BigInteger qauntityBigInteger = new BigInteger(quantity);
      int quantityInt = qauntityBigInteger.intValue();
      StringBuilder sb = new StringBuilder();
      StringBuilder sblinux = new StringBuilder();
      for(int i = 0; i < quantityInt; i++)
      {
    	sblinux.append("cat  " + fileName + " | lpr -T " +fileName+  " -Peds2100p1\n");
    	sblinux.append("sleep 20");
        sb.append("Copy " + fileName + " com1:" + "\n");
        sb.append("sleep 20");
        if(i + 1 < quantityInt)
        {
          sb.append("\n");
          sblinux.append("\n");
        }
      }
      //save bat file with copy commands to outputdir
      String batout = (String)map.get("CFEC Fishery") + (String)map.get("Permit") + ".bat";
      String shout = (String)map.get("CFEC Fishery") + (String)map.get("Permit") + ".sh";
      savePreparedStatementsToFile(sb.toString(), getOutputDirectoryLocation() + "/" + batout);
      savePreparedStatementsToFile(sblinux.toString(), getOutputDirectoryLocation() + "/" + shout);
    }
    
   
    savePreparedStatementsToFile(tempLine, getOutputDirectoryLocation() + "/" + fileName);*/
    return tempLine;
  }

  // addTrailingSpaces method adds necissary trailing spaces to a data element, except
  // when that element is too long, then it truncates it instead.
  public String addTrailingSpaces(String str, int totalLength) {
    while(str.length() != totalLength) {
      if(str.length() > totalLength) {
        str = str.substring(0, totalLength);
      }
      else {
        str = str + " ";
      }
    }
    return str;
  }
  
  public String addLeadingZeros(String str, int totalLength) throws Exception {
    while(str.length() != totalLength) {
      if(str.length() > totalLength) {
        throw new Exception("Data too long, string: " + str);
      }
      else {
        str = "0" + str;
      }
    }
    return str;
  }


  // savePreparedStatementstoFile writes a String to a file
  public void savePreparedStatementsToFile(String str, String fileName) {
    try{
      OutputStream out = new FileOutputStream(fileName);
      OutputStreamWriter osw = new OutputStreamWriter(out);

      osw.write(str);
      osw.close();
      out.close();
    } catch(IOException ex) {
      ex.printStackTrace();
    }
  }

  public String getCsvFileName() {
    return csvFileName;
  }

  public void setCsvFileName(String csvFileName) {
    this.csvFileName = csvFileName;
  }

  public String getOutputDirectoryLocation() {
    return outputDirectoryLocation;
  }

  public void setOutputDirectoryLocation(String outputDirectoryLocation) {
    this.outputDirectoryLocation = outputDirectoryLocation;
  }
  
  @SuppressWarnings({ "resource", "rawtypes" })
public void processInsertFile() throws Exception {
    if(null == getCsvFileName()) {
      return;
    }
    if(null == getOutputDirectoryLocation()) {
      return;
    }
    File file = new File(getCsvFileName());
    String line = null;
    ArrayList<String> preparedStrings = new ArrayList<String>();
    try {

    //     The following commented out code helped me find the current directory, which I need to know
    //       to put the cvs file that it processes.
    //          File dir1 = new File (".");
    //          File dir2 = new File ("..");
    //          System.out.println ("Current dir : " + dir1.getCanonicalPath());
    //          System.out.println ("Parent  dir : " + dir2.getCanonicalPath());

      BufferedReader bufRdr = new BufferedReader(new FileReader(file));
      line = bufRdr.readLine();

      StringTokenizer st = new StringTokenizer(line, ",");

      while(st.hasMoreTokens()) {
        attributes.add(st.nextToken());
      }

      line = bufRdr.readLine();

      while(null != line)
      {
        st = new StringTokenizer(line, ",");
        String tempLine = "";
        while(st.hasMoreTokens())
        {
          //System.out.print("element: " + st.nextToken() + " ");
          tempLine = tempLine + st.nextToken();
          if(st.hasMoreTokens()) {
            tempLine = tempLine  + ",";
          }
        }
        //System.out.println(tempLine);
        preparedStrings.add(formatForInsert(tempLine));
        line = bufRdr.readLine();
      }
      saveInsertFile(preparedStrings);
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    //String output
    Iterator iter = preparedStrings.iterator();
    while(iter.hasNext()) {
      System.out.println((String)iter.next());
    }
  }
  
  // formatForInsert method puts together an insert statement for the row of data.
  @SuppressWarnings({ "rawtypes", "unchecked" })
public String formatForInsert(String line) throws Exception {
    HashMap map = new HashMap();
    StringTokenizer st = new StringTokenizer(line, ",");
    int count = 0;
    while(st.hasMoreTokens()) {
      map.put(attributes.get(count), st.nextToken());
      count++;
    }
    map.put("Permit", addLeadingZeros((String)map.get("Permit"), 5));
    map.put("Permit", checkDigitCalculator.calculatePermitCheckCharacter((String)map.get("CFEC Fishery"), (String)map.get("Permit")));
    map.put("Year Seq", checkDigitCalculator.calculateYearSeqCheckCharacter((String)map.get("Permit"), (String)map.get("Year Seq")));
    
    //map.put("Vessel ADFG", addLeadingZeros((String)map.get("Vessel ADFG"), 5));
    //map.put("CFEC File Number", addLeadingZeros((String)map.get("CFEC File Number"), 6));
    
    String tempLine = "INSERT INTO cfec_permit\n" +
                      "VALUES(";
    tempLine += addInsertStringValue((String)map.get("CFEC Fishery"));
    tempLine += ",";
    tempLine += addInsertStringValue((String)map.get("Permit"));
    tempLine += ",";
    tempLine += addInsertStringValue((String)map.get("Year Seq"));
    tempLine += ",";
    tempLine += addInsertStringValue((String)map.get("CFEC Permit Holder"));
    tempLine += ",";
    //hardcode Revoked="N"
    tempLine += addInsertStringValue("N");
    tempLine += ",";
    tempLine += addInsertDateValue("2008-12-11 00:00:00");
    tempLine += ",";
    tempLine += addInsertValue("NULL");
    tempLine += ",";
    tempLine += addInsertValue((String)map.get("CFEC File Number"));
    tempLine += ");";
    return tempLine;
  }
  
  public String addInsertValue(String value) {
    String temp = "";
    temp += value;
    return temp;
  }
  
  public String addInsertStringValue(String value) {
    String temp = "";
    temp += "'";
    temp += value;
    temp += "'";
    return temp;
  }
  
  public String addInsertDateValue(String value) {
    String temp = "";
    temp += "TO_DATE(";
    temp += "'";
    temp += value;
    temp += "'";
    temp += ",";
    temp += " ";
    temp += "'";
    temp += "YYYY-MM-DD HH24:MI:SS";
    temp += "'";
    temp+= ")";
    return temp;
  }
  
  @SuppressWarnings("rawtypes")
public void saveInsertFile(ArrayList<String> preparedStrings) {
    String fileName = "Permit Inserts.sql";
    Iterator iter = preparedStrings.iterator();
    String inserts = "";
    while(iter.hasNext()) {
      inserts += (String)iter.next() +"\n";
    }
    savePreparedStatementsToFile(inserts, getOutputDirectoryLocation() + "/" + fileName);
  }
}
