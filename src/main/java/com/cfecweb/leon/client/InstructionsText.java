package com.cfecweb.leon.client;

import com.google.gwt.user.client.ui.HTML;

/*
 * Throughout the application, simple instructions can be found on the bottom contentpanel of the west side. The text for each of these
 * can be found and modified here. Note that I return the text as an HTML string for formatting purposes. Should probably do this 
 * completely with css, but I never came back to rewrite it.
 */
public class InstructionsText {

	public String getInit() {
		HTML initialInstruc = new HTML();		
		initialInstruc.setHTML("<table width='100%' align='left' border='0' cellspacing ='0'><tr>" +
       		"<td><span class='boldred12'><p><center>**  PLEASE READ FIRST  **</center></p></span>" +
       		"<br><span class='regblack12'><p>Before you can begin the renewal process, you must <b>FIRST</b> declare if you are the actual " +
       		"Permit Holder, Vessel Owner <b>OR</b> if you are an Authorized Agent.<br><br>That choice can be made in the Status, " +
       		"Fee Totals and Navigation section to your right and above.<br><br>Please press the appropriate button to begin the renewal process.</p></span>" +
       		"<br><span class='boldred12'><p><center>**  PLEASE READ FIRST  **</center></p></span></td></tr></table>");		
		return initialInstruc.toString();
	}
	
	public String getOptions() {
		HTML optionsInstruc = new HTML();		
		optionsInstruc.setHTML("<table width='100%' bgcolor='#FFFFCC' align='left' border='0' cellspacing ='0'><tr>" +
       		"<td><br><span class='regblack12'><p>The CFEC ID number can be found on the permit card, or by visiting our online search database " +
       		"<a href='http://www.cfec.state.ak.us/plook' target='_blank' title='Click here to open a seperate window for the CFEC Permit and Vessel " +
       		"search application'>here</a>. All 4 items are required.</p></span><br>" +
       		"<span class='boldred12'><p>PLEASE NOTE:</span><span class='regblack12'> For security purposes, this appilcation will timeout after 20 minutes of inactivity</p></span><br>" +
       		"<span class='boldred12'><p>PLEASE NOTE:</span><span class='regblack12'> When your online renewal is received by our staff, please allow 3 to 5 business days for processing.</p></span></td></tr></table>");		
		return optionsInstruc.toString();
	}
	
	public String getAddress() {
		HTML optionsInstruc = new HTML();		
		optionsInstruc.setHTML("<table width='100%' bgcolor='#FFFFCC' align='left' border='0' cellspacing ='0'><tr>" +
       		"<td><br><span class='regblack12'><p>Your permanent mailing address is displayed above. You may " +
       		"change this address by checking the permanent mailing address box and entering your new address.<br><br>A physical address " +
       		"(where you live) is also required ONLY if you are a resident of Alaska. If you have used LEON in the past, we will pre-populate " +
       		"your physical address and years/months occupancy.<br><br>Please provide a temporary mailing address <b>ONLY</b> if you want your licenses mailed to an " +
       		"address <b>OTHER</b> than your permanent mailing address.<br><br>Phone number and email address are now required, and they " +
       		"also will be pre-populated if entered in a prior renewal.</p></span></td></tr></table>");		
		return optionsInstruc.toString();
	}
	
	public String getReview() {
		HTML optionsInstruc = new HTML();		
		optionsInstruc.setHTML("<table width='100%' bgcolor='#FFFFCC' align='left' border='0' cellspacing ='0'><tr>" +
       		"<td><br><span class='regblack12'><p>Listed to the right are the permits and/or vessels that are available for renewal. To the far right of each " +
       		"permit and/or vessel is the status for each license. At the top of each section you have the opportunity to purchase additional permits " +
       		"and/or vessels that are NOT currently owned by you. </p></span></td></tr></table>");		
		return optionsInstruc.toString();
	}
	
	public String getPermits() {
		HTML optionsInstruc = new HTML();		
		optionsInstruc.setHTML("<table width='100%' bgcolor='#FFFFCC' align='left' border='0' cellspacing ='0'><tr>" +
       		"<td><br><span class='regblack12'><p>Please indicate what permit(s) you wish to renew AND if you intend to fish that permit.<br><br>To modify an ADFG number:<br>" +
       		"1) Select 'Renew?' box for the appropriate permit.<br> " +
       		"2) Double click on the existing ADFG number under the column ADFG.<br>" +
       		"3) Enter the new 5 digit ADFG number. (only valid numbers are accepted) and press enter<br><br>" +
       		"<b>Note:</b> The ADFG number cannot be altered after a permit is renewed. A new permit card must be produced.<br><br>" +
       		"If you are being accessed the full price of Halibut and/or Sablefish fisheries, you may select the the <span class='regblue12'>'Lower Fee?'</span> " +
       		"box if you believe you landed less than stated amount in the notes column.<br><br>If you owe backfees for a permit, " +
       		"ALL years prior to the year you select will automatically be selected<br><br></p></span><span class='boldblack12'><p> If you do not intend to fish a permit, " +
       		"the card will NOT be issued to you.</p></span></td></tr></table>");		
		return optionsInstruc.toString();
	}
	
	public String getVessels() {
		HTML optionsInstruc = new HTML();		
		optionsInstruc.setHTML("<table width='100%' bgcolor='#FFFFCC' align='left' border='0' cellspacing ='0'><tr>" +
       		"<td><br><span class='regblack12'><p>Please indicate which vessels you would like to license for the current year.<br><br> " +
       		"Please DO NOT license a vessel if you do not plan to use it as the Entry Commission cannot give you a refund.<br><br> " +
       		"1) To license the vessel select the box below the column <span class=regblue12'>'LICENSE?'</span><br>" +
       		"2) To edit the vessel information, select the <span class='regblue12'>'Click here to edit vessel'</span> under the column Information. The vessel "+
       		"must already be selected for renewal in order to make edits to a vessel.<br>" +
       		"3) To add an existing/new vessel, click on the <span class='regred12'>CLICK HERE to add new vessel</span> button and follow the prompts." +
			"</p></span></td></tr></table>");		
		return optionsInstruc.toString();
	}
	
	public String getBilling3() {
		HTML optionsInstruc = new HTML();		
		optionsInstruc.setHTML("<table width='100%' bgcolor='#FFFFCC' align='left' border='0' cellspacing ='0'><tr>" +
       		"<td><br><span class='regblack12'><p>It appears you are a first time user for the current year and have NOT selected any Permits to renew " +
       		"OR any Vessels to license. Until either a Permit is selected for renewal OR a Vessel is selected to license, you cannot continue. " +
       		"There is no action to process at this time, either press the <font color='red'><< Last</font> button to return to previous screens, " +
       		"or just press the <span class='boldblack12'>'Reset Application'</span> button below.</p></span></td></tr></table>");		
		return optionsInstruc.toString();
	}
	
	public String getBilling2() {
		HTML optionsInstruc = new HTML();		
		optionsInstruc.setHTML("<table width='100%' bgcolor='#FFFFCC' align='left' border='0' cellspacing ='0'><tr>" +
       		"<td><br><span class='regblack12'><p>You have NOT selected any Permits to renew OR any Vessels to license, but you have made changes to your " +
       		"temporary address, phone number or email address. Pressing the <span class='regred12'>Next >></span> button will record these " +
       		"changes with CFEC, but WILL NOT generate a confirmation report. You may also press the <span class='regred12'><< Last</span> button " +
       		"to return to previous pages.</p></span></td></tr></table>");		
		return optionsInstruc.toString();
	}
	
	public String getBilling4() {
		HTML optionsInstruc = new HTML();		
		optionsInstruc.setHTML("<table width='100%' bgcolor='#FFFFCC' align='left' border='0' cellspacing ='0'><tr>" +
       		"<td><br><span class='regblack12'><p>You have NOT selected any Permits to renew OR any Vessels to license and you have made no changes to your " +
       		"temporary address, phone number or email address. There is no action to process at this time, either press the <span class='regred12'>" +
       		"<< Last</span> button to return to previous screens, or just press the <span class='boldblack12'>'Reset Application'</span> button below.</p></span></td></tr></table>");		
		return optionsInstruc.toString();
	}
	
	public String getBilling5() {
		HTML optionsInstruc = new HTML();		
		optionsInstruc.setHTML("<table width='100%' bgcolor='#FFFFCC' align='left' border='0' cellspacing ='0'><tr>" +
       		"<td><br><span class='regblack12'><p>You have NOT selected any Permits to renew OR any Vessels to license. There is no action to process " +
       		"at this time, either press the <span class='regred12'>" +
       		"<< Last</span> button to return to previous screens, or just press the <span class='boldblack12'>'Reset Application'</span> button below.</p></span></td></tr></table>");		
		return optionsInstruc.toString();
	}
	
	public String getBilling1() {
		HTML optionsInstruc = new HTML();		
		optionsInstruc.setHTML("<table width='100%' bgcolor='#FFFFCC' align='left' border='0' cellspacing ='0'><tr>" +
       		"<td><br><br><span class='regblack12'><p>The total amount due is listed at " +
       		"the top right of the screen.<br><br>Once you have completed all necessary fields, press " +
       		"the <span class='regred12'>Next >></span> button to submit your order. To cancel your order, press the <span class='boldblack12'>" +
       		"'Reset Application'</span> button above this text. " +
       		"To return to previous screens to modify your selection(s), press the <span class='regred12'><< Last</span> button.</p></span></td></tr><tr><td align='center'><br><img src='images/33s.gif' " +
       		"alt='Accepted Credit Cards' height='25' width='140' border='0'/></td></tr></table>");		
		return optionsInstruc.toString();
	}
	
	public String getAdmin() {
		HTML optionsInstruc = new HTML();		
		optionsInstruc.setHTML("<table width='100%' bgcolor='#FFFFCC' align='left' border='0' cellspacing ='0'><tr>" +
       		"<td><br><span class='regblack12'><p>Welcome to the Leon Administration Screen. To begin, enter a valid CFEC ID (if necessary) and Password, then select " +
       		"either the appropriate option.<br><br><span class='regred12'>*WARNING*</span> The Delete button on the main toolbar, if selected, will " +
       		"delete ALL transactions for the CFEC ID entered.</p></span></td></tr></table>");		
		return optionsInstruc.toString();
	}
	
	public String getConfirm() {
		HTML optionsInstruc = new HTML();		
		optionsInstruc.setHTML("<table width='100%' bgcolor='#FFFFCC' align='left' border='0' cellspacing ='0'><tr>" +
       		"<td><br><span class='regblack12'><p>Please look over your final order. If you are satisfied, press "+
       		"the <span class='regred12'>Next >></span> button to process the order.<br><br>Pressing the <span class='regred12'><< Last</span> button " +
       		"will return to the previous page. To cancel this order completely, please "+
       		"press the <span class='boldblack12'>'Reset Application'</span> button above.</p></span></td></tr></table>");		
		return optionsInstruc.toString();
	}
	
	public String getProcess() {
		HTML optionsInstruc = new HTML();		
		optionsInstruc.setHTML("<table width='100%' bgcolor='#FFFFCC' align='left' border='0' cellspacing ='0'><tr>" +
       		"<td><br><span class='regblack12'><p>Please read the information to the right. If you encounter any problems or have any questions, please call our office at 907-789-6150. "
       		+ "Please note our normal business hours of 8-5, Monday thru Friday excluding holidays.</p></span></td></tr></table>");		
		return optionsInstruc.toString();
	}
	
	public String getForms() {
		HTML optionsInstruc = new HTML();		
		optionsInstruc.setHTML("<table width='100%' bgcolor='#FFFFCC' align='left' border='0' cellspacing ='0'><tr>" +
       		"<td><br><span class='regblack12'><p>These are the forms most commonly used, available to download for your convienence. The links " +
       		"labeled 'Your 'year' Pre-Printed Form(s)' will create and display your pre-printed renewal form for that particular year. " +
       		"We mail these forms annually in the fall. This is an easy way to get an original copy of your renewal form, " +
       		"and can be used in lieu of online renewal.</p></span></td></tr></table>");		
		return optionsInstruc.toString();
	}
	
	public String getTech() {
		HTML technicalMessage = new HTML();
		technicalMessage.setHTML("<span class='boldblack14'><center>Technical Difficulties</center></span><br>" +
    		"<span class='regblack12' align='left'>We apologize for this inconvience, but our system is currently experiencing technical " +
    		"difficulties which is preventing this data request. We are probably already working on the matter, and will attempt to have it " +
    		"resolved very quickly. For further assistance, please contact a technical representative at 1-907-789-6160 (option #4) OR simply wait and try again later..</span>");
		return technicalMessage.toString();
	}
	
}
