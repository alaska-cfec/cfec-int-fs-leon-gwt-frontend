package com.cfecweb.leon.client;

import com.google.gwt.user.client.ui.HTML;

public class UserWindows {

	/*
	 * This class contains the text for the larger windows or popup displays. Like other areas within the application that
	 * define text output, these blocks are all contained within HTML tags. If you modify any of these strings, be sure and
	 * keep the tags in the right places or they will display as pure ascii text and look funny.
	 */
	public String getFaq(String ver) {
		HTML faqtext = new HTML();
		faqtext.setHTML("<center><span class='boldblack14'>Frequently Asked Questions.</span></b>&nbsp;&nbsp;&nbsp;<span class='regblack12'> " +
			"(LEON ver&nbsp;"+ver+")</span><br>" +
			"<span class='regblack12'>Comments and/or Questions can be submitted by clicking on the Comments link</span><br><br>" +
			"<table width='100%' align='center'><tr><br><td align='left'>" +
			"<span class='boldblack12'> " +
			"1.	What is a CFEC ID and where can I find it?</span><br>" +
				"<span class='regblack12'> " +
				" The CFEC ID is a unique number we use to identify you. " +
				" The number itself contains 6 digits and will be located on your CFEC permit " +
				" card. If you do not have your card with you, then you can find your CFEC " +
				" ID on our online <a href='http://www.cfec.state.ak.us/plook' target='_blank'> " +
				" permit and vessel search application</a>. Simply enter your first and last name, " +
				" and any other information to limit the search, and click on the find button. " +
				" There you find all publically available information regarding your permit(s) and/or " +
				" vessel(s).</span><br><br>" +
			"<span class='boldblack12'> " +
			" 2. Why do I get the CFEC ID number is invalid error?</span><br>" +
				"<span class='regblack12'> " +
				" There are some permit types, which contain valid CFEC ID numbers, " +
				" that do not require annual renewal, such as test fisheries, hatchery " +
				" fisheries, etc.  If you have a valid and current permit you are trying " +
				" to renew and you receive this error, please contact us at 907-789-6150.</span><br><br>" +
			"<span class='boldblack12'> " +
			" 3. The Application does not format well on my monitor</span><br>" +
				"<span class='regblack12'> " +
				" While we have attempted to provide 'lowest common denominator' principles in the " +
				" development of this application, there are some basic requirements and suggestions " +
				" to avoid feature and resolution issues.  The application is best viewed on a monitor " +
				" set to at least a resolution of 1024x768. Lower resolutions will work, but you may " +
				" experience some 'wrap around' effects that could make the application difficult to " +
				" view and follow.</span><br><br>" +
			"<span class='boldblack12'> " +
			" 4. I have not received my permit(s) or vessel(s) that I renewed online. How come?</span><br><span class='regblack12'> " +
			" There are a few possible reasons. If you renewed a permit, but did not specify " +
			" your intent to fish, then the permit card will not be mailed to you. It is " +
			" possible that you Inadvertently gave us a wrong address. " +
			" If everything was correct and you still do not have your " +
			" card, please notify a licensing agent and we can send you a duplicate.</span><br><br><span class='boldblack12'> " +
			" 5. I did not receive and/or lost my confirmation report.</span><br><span class='regblack12'> " +
			" If you have renewed your licenses online and did not print your confirmation report or misplaced it, " +
			" please contact us at 907-789-6150 and we will send you a copy</span><br><br><span class='boldblack12'> " +
			" 6. I successfully renewed my permit(s) and/or vessel(s), but they are still showing " +
			" a pending status.</span><br><span class='regblack12'> " +
			" Licensing staff processes renewals in the order in which they are received.  It is our goal to renew your " +
			" licenses and mail them out within 3 business days of receiving them.  If you are concerned about the status " +
			" please contact us at 907-789-6150.</span><br><br><span class='boldblack12'> " +
			" 7. I am pressing the Next >> button, but nothing is happening.</span><br><span class='regblack12'> " +
			" The application will not let you proceed to the next screen if there are any un-met requirements on the current page. " +
			" Any un-met requirement will be highlighted in red.</span><br><br><span class='boldblack12'> " +
			" 8. I pressed the 'Back' or 'Forward' button on my browser, and it left the site completely</span><br><span class='regblack12'> " +
			" Do not use your BACK or FORWARD buttons on your browser, this will take you out of the application.  Use the LAST and " +
			" NEXT buttons on the bottom of the page under the Status, fee totals and navigations bar.</span><br><br><span class='boldblack12'> " +
			" 9. Why was I automatically logged out?</span><br><span class='regblack12'> " +
			" There are 2 main reasons why this could occur. One being the connection to our site was momentarily " +
			" lost, and after reconnection a new session ID was created prompting the application to start over. " +
			" Another possibility is that the application was left 'inactive' over a period of 20 minutes or " +
			" more. In this case, we automatically log you out and remove any information on the screen. In " +
			" both cases, any incomplete transaction will not process and you will have to renew those permits " +
			" or vessels again.</span><br><br><span class='boldblack12'> " +
			" 10. Is my data and privacy protected?</span><br><span class='regblack12'> " +
			" The entire transaction occurs over a secure connection, thus providing a high measure of security for " +
			" your private data.</span><br><br><span class='boldblack12'> " +
			" 11. Can I check the status of my renewals?</span><br><span class='regblack12'> " +
			" You can log back into the system and check the status of your permit(s) and/or vessel(s) at any time by going to the status section.</span><br><br><span class='boldblack12'> " +
			" 12. Do I have to renew all of my permits and/or vessels?</span><br><span class='regblack12'> " +
			" No, you may renew as few or many as you prefer in any given transaction throughout the course of " +
			" the licensing year.</span><br><br><span class='boldblack12'> " +
			" 13. Why does the application force me to renew certain permits, or make others unavailable to fish " +
			" after I have selected a different permit?</span><br><span class='regblack12'> " +
			" If you have any permits remaining from previous years, the current years permit for that " +
			" fishery cannot be renewed without renewing the previous year. If you have multiple salmon permits, " +
			" and you select the 'Intend to fish' box, the application will automatically disable that option for your other " +
			" salmon permits NOT in the same fishery or area. If you feel that a decision made by the application " +
			" is wrong, please contact us at 907-789-6150.</span><br><br><span class='boldblack12'> " +
			" 14. Can I make updates to my permit(s), vessel(s) or personal data?</span><br><span class='regblack12'> " +
			" Yes. For permits, you may change the ADFG number of the vessel you intend to fish on, but only if you " +
			" elect to renew that particular permit. When you click on the renew box, the ADFG box will become available " +
			" for edit, at which time you may enter a new number. Vessel data may also be updated by clicking on the " +
			" 'EDIT' button on the vessel selection screen. Like the permits, vessel information may only be changed " +
			" on those vesses being selected for renewal. Some of the vessel information can not not be modified " +
			" in the application.  Personal data, such as temporary address, permanent address, physical address, " +
			" email address, phone number and various billing information may be changed at any time.</span><br><br><span class='boldblack12'> " +
			" 15. I would rather renew my licenses by manually filling out my renewal form and mailing it in. Where can " +
			" I get a copy of my renewal form(s)?</span><br><span class='regblack12'> " +
			" We mail out renewal reminders each November. If you did not receive your reminder or have misplaced it, you can log " +
			" into the system and print your renewal form by selecting option Download your renewal form. delete the rest of the sentence</span><br><br><span class='boldblack12'> " +
			" 16. I qualify for low income fees, how do I specify that in the application?</span><br><span class='regblack12'> " +
			" If you qualify for the low income fee option, simply click the 'Low Income Fee option' box at the opening screen. " +
			" This option does require some verification.</span><br><br><span class='boldblack12'> " +
			" 17. I made a mistake during renewal, but the order has already been submitted.</span><br><span class='regblack12'> " +
			" No problem, contact us at 907-789-6150.</span><br><br><span class='boldblack12'> " +
			" 18. I selected an option and/or entered a value that I do not believe should have been allowed</span><br><span class='regblack12'> " +
			" We have made every effort to ensure that the CFEC Online Permit and Vessel Renewal Application is as " +
			" accurate as possible when dealing with your licenses. Should a bug or loophole occur that allows you to make " +
			" a selection or enter a value which you feel is wrong, please notify us with a phone call and/or a message using " +
			" the Questions/Comments form at the top of this page. Please note that if we discover an inconsistency in your " +
			" submitted data as it relates to our regulations, we reserve the right to notify the Permit holder and make any " +
			" and all necessary changes.</span><br><br><span class='boldblack12'> " +
			" 19. I received an error message and the application reset itself</span><br><span class='regblack12'> " +
			" Errors during the renewal process will generate a message box notifying the user. Errors in the application can " +
			" be broad and caused by a variety of problems, such as internet connections, database issues, browser incompatibilities " +
			" and others. In the event of an error, your current transaction WILL NOT be processed.  It is likely we are already " +
			" working to fix the problem, so please try again at a later time." +
			" </span><br><br><span class='boldblack12'>" +
			" 20. Browser compatibility and/or layout problems</span><br><span class='regblack12'>" +
			" If you are experiencing browser compatibilty and/or layout problems, such as elements NOT display correctly or not " +
			" displaying at all on the screen, please refer to our Requirements section for possible solutions.</span>" +
			"</td></tr></table>");		
		return faqtext.toString();
	}
	
	public String getSupport() {
		HTML supporttext = new HTML();
		supporttext.setHTML("<span class='boldblack14'><center>CFEC Online Renewal Support</center></span><br> " +
			"<br><table width='100%' align='center'><tr><td align='left'><span class='regblack12'> " +
			"The follow phone numbers are available for support.<br><br> " +
			"1-907-789-6150<br><b>Toll-free number available ONLY in Alaska and outside of Juneau.</b><br>1-855-789-6150" +
			"</span></td></tr></table>");		
		return supporttext.toString();
	}
	
	public String getRequirements() {
		HTML requirementstext = new HTML();
		requirementstext.setHTML("<span class='boldblack14'><center>CFEC Online Renewal requirements</center></span><br> " +
			"<br><table width='100%' align='center'><tr><td align='left'><span class='regblack12'> " +
			"The CFEC Online Renewal application is a pure web application and requires no " +
			"additional software be installed. Please note our known compatibility chart below:<br><br> " +
			"<u>Supported and Compatible browsers/versions are as follows:</u><br>" +
			"Microsoft Internet Explorer, version(s) 8.x and greater.<br>" +
			"Mozilla Firefox, Google Chrome or Safari (any recent versions).<br>" +
			"We don't promote the application for mobile devices, but it does appear to work well with any modern smart phone or " +
			"tablet.<br><br>" +
			"<u>Known work-a-round for Microsoft Internet Explorer, version 7.x</u><br>" +
			"A layout issue exists in certain versions of IE 7 which prevent the inital required options (Item 1) from displaying " +
			"the check boxes necessary to complete the first page. Simply changing the size of your browser window, either by minimizing/" +
			"maximizing or by manually 'dragging' a corner to reduce/increase window size will cause a redraw of the content and solve this issue.<br><br>" +
			"<u><b>ANY MICROSOFT INTERNET EXPLORER BROWSER ISSUES</b></u><br>" +
			"While trying to maintain a compatibility mode with the largest number of market browsers possible, we do occassionally experience layout " +
			"issues with particular version of Microsoft Internet Explorer. In that event we would recommend using an alternative web browser such as " +
			"Firefox or Chrome. Links below will take you to the appropriate download/install site.<br><br>" +
			"Other pre-requisites include a valid credit card " +
			"number (Mastercard, Visa or Discover), address information and your CFEC ID number.<br><br> " +
			"<center><a href='http://www.mozilla.com/firefox/'>Click here to install/upgrade Firefox browser</a></center><br>" +
			"<center><a href='http://www.google.com/intl/en/chrome/browser/'>Click here to install/upgrade Chrome browser</a></center><br>" +
			"<center><a href='http://www.adobe.com/products/acrobat/readstep2.html'>Click here to download Adobe Reader for free</a></center> " +
			"</span></td></tr></table>");		
		return requirementstext.toString();
	}
	
	public String getResidency() {
		HTML residency = new HTML();
		residency.setHTML("<span class='boldblack14'><center>CFEC Residency Definition regarding license renewal</center></span><br>" +
			"<br><span class='regblack12' align='left'>For the purpose of assessing fees for the application for, annual issuance of, " +
			"or renewal of entry and interim-use permits, an individual is a resident of this state if, on the date of permit application, " +
			"issuance, or renewal, and throughout the 12-month period before that date, that individual maintained their domicile in this " +
			"state and neither claimed residency in another state, territory, or country nor obtained benefits under a claim of residency " +
			"in another state, territory, or country.<br>" +
			"(20 AAC 05.290)</span></td></tr></table>");		
		return residency.toString();
	}
	
	public String getReduced(String cyear) {
		String yr1 = Integer.toString( ((Integer.parseInt(cyear))-2) );
		String yr2 = Integer.toString( ((Integer.parseInt(cyear))-1) );
		HTML reduced = new HTML();
		reduced.setHTML("<span class='boldblack14'><center>CFEC Low Income Fee requirements regarding license renewal</span><br><br>" +
			"<span class='boldblack12'> If you are currently receiving food stamps or public assistance, you may qualify for the reduced fee. " +
			"You will need to complete the Reduced Permit Fee Application and submit it with a copy of your Quest Card and most recent " +
			"determination letter. You can " +
			"fax it to (907)-789-6170, email it to: dfg.cfec.myinfo@alaska.gov, or mail it to our office. <br><br>If you wish to select " +
			"the low income fee option, click 'OK', If not, then click on 'CANCEL' </span>");

		return reduced.toString();
	}
	
	public String getAlien() {
		HTML reduced = new HTML();
		reduced.setHTML("<span class='boldblack14'><center>CFEC Non US Citizenship requirements regarding license renewal</span><br><br>" +
			"<span class='boldblack12'>When applying online as a non US Citizen, " +
			"you must enter your United States Citizen and Immigration Services Number (USCIS, located on your green card) below AND provide a copy of your green card to the Commission. " +
			"<br><br>If you wish to select the non US Citizen option, click 'OK', If not, then click on 'CANCEL'</span>");		
		return reduced.toString();
	}
	
}
