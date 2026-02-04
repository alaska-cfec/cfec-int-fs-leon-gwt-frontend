package com.cfecweb.leon.client;

import com.google.gwt.user.client.ui.HTML;

/*
 * This class defines the progress bar, which is located horizontally in the status region of the application.
 * Each status block is given an ID field, which can be called and updated throughout the application. Basically,
 * as the person navigates back and forth, I am simply changing the backgroud color of that particular screen title 
 * to represent completion, pending or remaining.
 * This is another example of a table that should probably be maintained as pure css. Would be a good update for whoever
 * wants to do it. Otherwise, be sure and maintain the proper HTML tags.
 */
public class ProgressbarText {

	public String getPro() {
		HTML probarText = new HTML();		
		probarText.setHTML("<table width='100%' border='0' cellspacing='0' align='center'><tr>" +
					  "<td align='center' width='11%' height='20' bgcolor='#DCDCDC' valign='middle' id='progressBar1' " +
					  "style='border-top:blue; border-top-style:solid; border-left:blue; " +
					  "border-left-style:solid; border-bottom:blue; border-bottom-style:solid; border-width:thin;'>" +
					  "<span class='regblack10'>OPTIONS</span></td>" +
			          "<td align='center' width='11%' height='20' bgcolor='White' valign='middle' id='progressBar2' " +
					  "style='border-top:blue; border-top-style:solid; border-bottom:blue; border-bottom-style:solid; border-width:thin;'>" +
					  "<span class='regblack10'>ADDRESS</span></td>" +
                      "<td align='center' width='11%' bgcolor='White' valign='middle' id='progressBar3' " +
            		  "style='border-top:blue; border-top-style:solid; border-bottom:blue; border-bottom-style:solid; border-width:thin;'>" +
            		  "<span class='regblack10'>VESSELS</span></td>" +
                      "<td align='center' width='11%' bgcolor='White' valign='middle' id='progressBar4' " +
            		  "style='border-top:blue; border-top-style:solid; border-bottom:blue; border-bottom-style:solid; border-width:thin;'>" +
            		  "<span class='regblack10'>PERMITS</span></td>" +
                      "<td align='center' width='11%' bgcolor='White' valign='middle' id='progressBar5' " +
            		  "style='border-top:blue; border-top-style:solid; border-bottom:blue; border-bottom-style:solid; border-width:thin;'>" +
            		  "<span class='regblack10'>PAYMENT</span></td>" +
                      "<td align='center' width='11%' bgcolor='White' valign='middle' id='progressBar6' " +
            		  "style='border-top:blue; border-top-style:solid; border-right:blue; border-right-style:solid; border-bottom:blue; border-bottom-style:solid; border-width:thin;'>" +
            		  "<span class='regblack10'>CONFIRM</span></td>" +
            		  "</tr></table>");		
		return probarText.toString();
	}
	
}
