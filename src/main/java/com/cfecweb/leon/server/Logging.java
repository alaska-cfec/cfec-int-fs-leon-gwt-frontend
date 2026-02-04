package com.cfecweb.leon.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * This is my Log4J class, which not only defines the logger and types (only info at this point),
 * but also includes some methods to parse and view particular log entries from the client.
 */

public class Logging {
	
	//	define the static logger. gwt-log is the name I gave it in web.xml
	private static Logger loggerI = LogManager.getLogger("gwt-log");
	
	//	stub for other classes to call when writing a log statement
	public void log(String action) {
		loggerI.info(action);
    }
	
	/*
	 * This method builds a string of HTML of current LEON issues and version number for the about button.
	 */
	public String getversionStatus(String lversion) {
		StringBuffer fileTable = new StringBuffer("<table border='0' width='100%' cellspacing='0' class='tbldata'>");
		fileTable.append("<tr><td align='center'><b>LEON Version Log</b></td></tr>");
		fileTable.append("<tr><td align='center'>Current Version: " + lversion + "</td></tr><br>");
		fileTable.append("<tr><td align='center'><span class='regred12'>Current Issues/Updates/Changes</span></td></tr>");
		fileTable.append("<tr><td align='left'><span class='boldred12'>*</span> (Bug) <b>Application generates NULL error on Agent name</b><br>User completed the entire process, then received an error ");
		fileTable.append("box indicating that a NULL value cannot be inserted into the database for Agent Name. This is a true statement, NULL values ");
		fileTable.append("cannot be entered into the database IF and only if the user originally logged in as an Authorized Agent. ");
		fileTable.append("I don't have any followup on this particular error, but based on the code, it cannot be generated unless ");
		fileTable.append("the user selects Authorized Agent at the beginning of the process. For some reason, which I cannot duplicate, ");
		fileTable.append("this user was able to validate the Agent Name box as positive when in fact it was a NULL value. I will wait ");
		fileTable.append("to hear from the acutal user before troubleshooting further.</td></tr><br>");	
		fileTable.append("<tr><td align='left'><span class='boldred12'>*</span> (Bug) <b>Cannot navigate past the Review Screen</b><br>Permit Holder can not navigate past the Review Screen. A single error box ");
		fileTable.append("displays on the screen immediately after pressing the Next >> button. The individual was using ");
		fileTable.append("Internet Explorer 7. The error is definitely a Javascript error but the individual did had their");
		fileTable.append("javascript feature enabled. The person also had the Norton suite of products loaded on their computer ");
		fileTable.append("which includes a firewall feature. I suspect this is the problem, but will have to conduct further ");
		fileTable.append("testing to know for sure. If a Permit Holder is experiencing this, please ask if they are running any 3rd party ");
		fileTable.append("security software such as Norton or Mcafee.</td></tr><br>");		
		fileTable.append("<tr><td align='left'><span class='boldred12'>*</span> (Bug) <b>Total amount on Confirmation receit is wrong</b><br>Permit Holder completed the entire process, but the total amount billed on ");
		fileTable.append("their confirmation report was 0. This individual originally made it to the Payment screen, then ");
		fileTable.append("navigated back to the Address screen to change their residency AFTER Permits were already selected ");
		fileTable.append("for renewal. I believe that was the cause of the problem, and while technically that should be possible, ");
		fileTable.append("it is not a scenerio I originally coded for. After I can successfully re-create this problem, I should have ");
		fileTable.append("a fix shortly afterward. We deleted the individuals record(s) and asked that they perform the process one more ");
		fileTable.append("time, correctly identifying residency prior to Permit selection and that worked.</td></tr><br><br>");		
		fileTable.append("<tr><td align='center'><span class='regblue12'>Previous Issues/Updates/Changes</span></td></tr>");
		fileTable.append("<tr><td align='left'>Version 1.0.6.1203.2008 (Update) <b>Added option for daily summaries</b></td></tr><br>");		
		fileTable.append("<tr><td align='left'>Version 1.0.6.1203.2008 (Update) <b>Added ability for Vessel Owners to license a new Vessel that HAS NOT been previously licensed</b></td></tr><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.6.1203.2008 (Update) <b>Added option for user to select mailing proiority</b></td></tr><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.6.1203.2008 (Update) <b>Added mandatory field for 3-digit credit card security code input</b></td></tr><br>");		
		fileTable.append("<tr><td align='left'>Version 1.0.6.1203.2008 (Update) <b>Added addition Logging values for forms</b></td></tr><br>");		
		fileTable.append("<tr><td align='left'>Version 1.0.6.1203.2008 - (Fixed) <b>Incorrectly calculating Poverty fees</b><br>");
		fileTable.append("When a permit holder, resident OR non-resident selects the poverty fee option at the beginning, I was ");
		fileTable.append("reducing their Vessel license by 50% as well as their Permits. Vessels should not be reduced even when ");
		fileTable.append("that option has been selected, only Permits.</td></tr><br>");		
		fileTable.append("<tr><td align='left'>Version 1.0.6.1203.2008 (Update) <b>Added Re-Submit option on Summary</b></td></tr><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.6.1203.2008 (Update) <b>Added automatic insert of bogus Credit Card number for debugging</b></td></tr><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.6.1203.2008 (Update) <b>Added Re-Submit option on Sys Log</b></td></tr><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.6.1203.2008 (Update) <b>Added ability for Permit Holder to purchase a new Un-Limited permit that they currently DO NOT own</b></td></tr><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.5.1202.2008 (Update) <b>Added Discovery credit card capability</b></td></tr><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.5.1202.2008 (Update) <b>Added Version Log view capability for Administrators and Agents<b></td></tr><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.5.1202.2008 (Update) <b>Added a dummy Permit Holder for CFEC troubleshooting and demo purposes<b></td></tr><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.5.1202.2008 (Fixed) <b>Total amount on Confirmation receit is wrong</b><br>Permit Holder completed the entire process, but the total amount billed on ");
		fileTable.append("their confirmation report was wrong. It was 135.00, should have been 285.00. A piece of code used to determine whether or ");
		fileTable.append("not a Permit holder qualified for Halibut and/or Sablefish reduced fees, then made the fee table adjustments, was ");
		fileTable.append("inadvertantly being called twice, once during the actual action, then again if the user navigated away from and back to ");
		fileTable.append("the Permit Selection screen.</td></tr><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.5.1202.2008 (Fixed) <b>Total amount on Confirmation receit is wrong</b><br>Permit Holder completed the entire process, but the total amount billed on ");
		fileTable.append("thier confirmation report was -75.00. The log shows that this individual selected and de-selected a Sablelfish ");
		fileTable.append("reduced fee dialog box several times, before AND after selecting the Intent box for the same fishery. Regardless ");
		fileTable.append("of how many times a person selects these options, the end result should NOT be a negative number. Clearly this is ");
		fileTable.append("a bug that I will have re-create and fix. (See fix above, same problem).</td></tr><br><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.4.1201.2008 - (Fixed) <b>Cannot navigate pas the Address Screen</b><br>Permit Holder could not navigate past the Address Screen. When ");
		fileTable.append("pressed the Next >> button after completed the Address Screen, they were presented with a LEON box telling them ");
		fileTable.append("that they must first select either the renewal or reprint option. This error box is a function of the Options ");
		fileTable.append("Screen data entry, NOT the Address Screen. The individual was using Firefox version 2.0.0.18. Early versions of ");
		fileTable.append("Firefox required a slightly different mechanism for accessing and updating DOM level objects. This issue is resolved.</td></tr><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.4.1201.2008 - (Fixed) <b>Could not see the bottom portion of the screen</b><br>Some Permit Holders could physically NOT see the bottom of the inital ");
		fileTable.append("Options Screen to make the choice of either Permit Holder or Authorized Agent. This is screen resolution problem. The ");
		fileTable.append("user has their screen resolution set to figure below what I originally thought would be a minimal requirement of ");
		fileTable.append("1024x768. I could see that it would become to troublesome for out staff to walk people through the process of changing ");
		fileTable.append("their resolution, and prehaps a bit presumptious as well. I modified the screen layout to handle lower screen resolutions ");
		fileTable.append("with no actual limit. Now the user will be presented with muliple scroll bars that will allow them, if necessary, to ");
		fileTable.append("sucessfully naviagte anywhere on the screen regardless of screen size.</td></tr><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.3.1129.2008 - (Update) <b>This version contained feature requests only</b></td></tr><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.2.1127.2008 - (Fixed) <b>Receives LEON error after completing Payment process</b><br>Permit Holder was able to complete the entire process, but received a LEON ");
		fileTable.append("generated error after submitting their Payment information. The error indicated a database problem with null (or empty) ");
		fileTable.append("field values. This was a real difficult bug to track down, but the problem occurred when more than a single person were ");
		fileTable.append("making database updates at the same time. LEON was building new Java Objects on each individual request, and when the ");
		fileTable.append("second person came in, their new Java Object was replacing the original and creating null values. I modified the program ");
		fileTable.append("to run in a session mode, which handles each unique session and maintains the same Java Object througout the process. This ");
		fileTable.append("issue is resolved.</td></tr><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.1.1125.2008 - (Fixed) <b>Would not accept Visa credit card</b><br>Permit Holder could not process orders with Visa credit cards, even though ");
		fileTable.append("we support them. This was a syntax error in the credit card verification process and is resolved.</td></tr><br>");
		fileTable.append("<tr><td align='left'>Version 1.0.0.1124.2009 - (Update) <b>LEON officially goes live at 8:00 A.M.</b></td></tr>");
		fileTable.append("</table>");
		return fileTable.toString();
	}

}
