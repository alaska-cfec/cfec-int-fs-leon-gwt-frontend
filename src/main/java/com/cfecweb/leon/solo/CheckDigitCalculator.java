package com.cfecweb.leon.solo;

import java.util.HashMap;

@SuppressWarnings("rawtypes")
public class CheckDigitCalculator {
	HashMap ebcdicValues = null;
	  @SuppressWarnings("unchecked")
	public CheckDigitCalculator() {
	    ebcdicValues = new HashMap();
	    ebcdicValues.put(" ", new Integer(0x0040));
	    ebcdicValues.put("A", new Integer(0x00C1));
	    ebcdicValues.put("B", new Integer(0x00C2));
	    ebcdicValues.put("C", new Integer(0x00C3));
	    ebcdicValues.put("D", new Integer(0x00C4));
	    ebcdicValues.put("E", new Integer(0x00C5));
	    ebcdicValues.put("F", new Integer(0x00C6));
	    ebcdicValues.put("G", new Integer(0x00C7));
	    ebcdicValues.put("H", new Integer(0x00C8));
	    ebcdicValues.put("I", new Integer(0x00C9));
	    ebcdicValues.put("J", new Integer(0x00D1));
	    ebcdicValues.put("K", new Integer(0x00D2));
	    ebcdicValues.put("L", new Integer(0x00D3));
	    ebcdicValues.put("M", new Integer(0x00D4));
	    ebcdicValues.put("N", new Integer(0x00D5));
	    ebcdicValues.put("O", new Integer(0x00D6));
	    ebcdicValues.put("P", new Integer(0x00D7));
	    ebcdicValues.put("Q", new Integer(0x00D8));
	    ebcdicValues.put("R", new Integer(0x00D9));
	    ebcdicValues.put("S", new Integer(0x00E2));
	    ebcdicValues.put("T", new Integer(0x00E3));
	    ebcdicValues.put("U", new Integer(0x00E4));
	    ebcdicValues.put("V", new Integer(0x00E5));
	    ebcdicValues.put("W", new Integer(0x00E6));
	    ebcdicValues.put("X", new Integer(0x00E7));
	    ebcdicValues.put("Y", new Integer(0x00E8));
	    ebcdicValues.put("Z", new Integer(0x00E9));
	    ebcdicValues.put("0", new Integer(0x00F0));
	    ebcdicValues.put("1", new Integer(0x00F1));
	    ebcdicValues.put("2", new Integer(0x00F2));
	    ebcdicValues.put("3", new Integer(0x00F3));
	    ebcdicValues.put("4", new Integer(0x00F4));
	    ebcdicValues.put("5", new Integer(0x00F5));
	    ebcdicValues.put("6", new Integer(0x00F6));
	    ebcdicValues.put("7", new Integer(0x00F7));
	    ebcdicValues.put("8", new Integer(0x00F8));
	    ebcdicValues.put("9", new Integer(0x00F9));
	  }
	  
	  /**
	   * Calculates the correct check character for a CFEC fishery and permit, and returns the permit number with the check character.
	   */
	  public String calculatePermitCheckCharacter(String fishery, String permit) 
	  {
	    // iterate through the alphabet to find the check character for the year seq
	    String cfecCheckAlpha = "0ABCEFGHIJKLMNOPQRSUVWXZ";
	    for (int i = 0; i < cfecCheckAlpha.length(); i++) 
	    {       
	      String checkDigit = cfecCheckAlpha.substring(i, i + 1);
	      
	      if (validateCfecPermitCheckDigit(fishery, permit + checkDigit)) 
	      {
	        return permit + checkDigit;
	      }
	    } 
	    // shouldn't ever fall through, but if we do this is logical
	    return permit;
	  }
	  
	  /**
	   * Calculates the correct check character for a CFEC permit and year sequence, and returns the year sequence with the check character.
	   */
	public String calculateYearSeqCheckCharacter(String permit, String yearSeq) {
		// iterate through the alphabet to find the check character for the year
		// seq
		String cfecCheckAlpha = "0ABCEFGHIJKLMNOPQRSUVWXZ";
		for (int i = 0; i < cfecCheckAlpha.length(); i++) {
			String checkDigit = cfecCheckAlpha.substring(i, i + 1);
			if (validateCfecYearSequenceCheckDigit(permit, yearSeq + checkDigit)) {
				return yearSeq + checkDigit;
			}
		}
		// shouldn't ever fall through, but if we do this is logical
		return yearSeq;
	}
	  
	  /**
	   * Validate the CFEC permit by calculating the check character and comparing it to the value on the specified 
	   * permit number. Validation succeeds when all the following conditions are met:
	   * <ol>
	   * <li>CFEC fishery must exist in the appropriate database table</li>
	   * <li>CFEC permit must be specified</li>
	   * <li>The expanded fishery ID, which is looked up based on the CFEC fishery, must be exactly 6 characters</li>
	   * <li>CFEC permit must be exactly 6 characters</li>
	   * <li>The check character calculated from the fishery and permit matches the last character of the permit specified.</li>
	   * </ol>
	   */
	  public boolean validateCfecPermitCheckDigit(String fishery, String permit) {
	    if (null == fishery || null == permit ) {
	      return false;
	    }
	    String expandedFishery = fishery.substring(0, 1) + " " + fishery.substring(1);
	    if(expandedFishery.length() == 5) {
	      expandedFishery += " ";
	    }
	    if (permit.length() != 6 || expandedFishery.length() != 6) {
	      return false;
	    }	    
	    String tempFishery1 = expandedFishery.substring(0,1) +  expandedFishery.substring(2,5);
	    String tempFishery2 = expandedFishery.substring(1,2) +  expandedFishery.substring(5,expandedFishery.length());
	    String checkString = tempFishery1.toUpperCase() + permit.substring(0,permit.length() - 1) + tempFishery2.toUpperCase(); 
	    int[] checkValues = new int[checkString.length()];
	    for (int i = 0;i < checkString.length(); i++) {
	      if (ebcdicValues.containsKey(checkString.substring(i, i+1))) {
	        checkValues[i] = ((Integer)ebcdicValues.get(checkString.substring(i, i+1))).intValue();
	      } else {
	        return false;
	      }
	    }  
	    int acc = 192;
	    acc = acc + checkValues[0];
	    acc = acc - (8  * checkValues[1]);
	    acc = acc + (2* checkValues[2]);
	    acc = acc - (16 * checkValues[3]);
	    acc = acc + (4 * checkValues[4]);
	    acc = acc - checkValues[5];
	    acc = acc + (8 * checkValues[6]);
	    acc = acc - (2 * checkValues[7]);
	    acc = acc + (16 * checkValues[8]);
	    acc = acc - (4* checkValues[9]);
	    acc = acc + checkValues[10];
	    acc = (acc < 1) ? acc * -1 : acc;
	    int mod = (acc % 23) + 1 ;
	    String cfecCheckAlpha = "0ABCEFGHIJKLMNOPQRSUVWXZ";
	    String checkDigit = cfecCheckAlpha.substring(mod, mod + 1);
	    return ( checkDigit.equals(permit.substring(permit.length()-1,permit.length())));
	  }
	  
	  /**
	   * Validate the CFEC permit year sequence by calculating the check character and comparing it to the value on the specified 
	   * permit year sequence. Validation succeeds when all the following conditions are met:
	   * <ol>
	   * <li>The CFEC permit must be specified</li>
	   * <li>The CFEC permit year sequence must be specified</li>
	   * <li>The CFEC permit must be exactly 6 characters</li>
	   * <li>The CFEC permit year sequence must be exactly 5 characters</li>
	   * <li>The check character calculated from the permit and year sequence matches the last character of the permit year sequence specified.</li>
	   * </ol>
	   * If the permit is 00098A and the year sequence is 9999W the validation will pass. This is to allow 9998 00098A 9999W for the missing value 
	   * of CFEC permit, as listed on the 2009 laminated code sheet. After 2009 this code can be removed.
	   */
	  public boolean validateCfecYearSequenceCheckDigit(String permit, String yearsequence) {
	      if (null == permit || null == yearsequence) {
	          return false;
	      }	    
	      if (("00098A".equals(permit)) && "9999W".equals(yearsequence)) {
	          // 9999W is listed as a valid year sequence missing value on 2009 laminated code sheet. 
	          // After 2009 this code can be removed, if 9999W is removed as a valid missing year/seq value from the code sheet    
	          return true;
	      }	  
	      if (permit.length() != 6 || yearsequence.length() != 5) {
	    	  return false;
	      }	  
	      String checkString = permit.substring(0,permit.length() - 1) + yearsequence.substring(0,yearsequence.length() - 1) + "  "; 
	      int[] checkValues = new int[checkString.length()];
	      for (int i = 0;i < checkString.length(); i++) { 
	    	  if (ebcdicValues.containsKey(checkString.substring(i, i+1))) {
	    		  checkValues[i] = ((Integer)ebcdicValues.get(checkString.substring(i, i+1))).intValue();
	    	  } else {
	    		  return false;
	    	  }
	      }
	      int acc = 192;
	      acc = acc + checkValues[0];
	      acc = acc - (8  * checkValues[1]);
	      acc = acc + (2* checkValues[2]);
	      acc = acc - (16 * checkValues[3]);
	      acc = acc + (4 * checkValues[4]);
	      acc = acc - checkValues[5];
	      acc = acc + (8 * checkValues[6]);
	      acc = acc - (2 * checkValues[7]);
	      acc = acc + (16 * checkValues[8]);
	      acc = acc - (4* checkValues[9]);
	      acc = acc + checkValues[10];
	      acc = (acc < 1) ? acc * -1 : acc;
	      int mod = (acc % 23) + 1 ;
	      String cfecCheckAlpha = "0ABCEFGHIJKLMNOPQRSUVWXZ";
	      String checkDigit = cfecCheckAlpha.substring(mod, mod + 1);
	      return ( checkDigit.equals(yearsequence.substring(yearsequence.length()-1,yearsequence.length())));
	  }  
}
