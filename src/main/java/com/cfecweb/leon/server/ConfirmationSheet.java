package com.cfecweb.leon.server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.cfecweb.leon.shared.ArenewEntity;
import com.cfecweb.leon.shared.ArenewPayment;
import com.cfecweb.leon.shared.ArenewPermits;
import com.cfecweb.leon.shared.ArenewVessels;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

/*
 * This class contains the method to build the confirmation sheet after a transaction has taken place. It 
 * receives the confirmation code, the completed entity, payment, permit and vessel objects, then builds and
 * sends the confirmation report to the user.
 * 
 * I really should rebuild this class to use iReport, then make the reports dynamic instead of static URL's
 */

public class ConfirmationSheet {
	
	/*
	 * Passed variables description:
	 * ccode			String containing the new confirmation code
	 * entity			final user entity object
	 * payment			final user payment object
	 * pmt2				ArenewPermits array list of only renewed permits for this transaction
	 * ves2				ArenewVessels array list of only licenesed vessels for this transaction
	 * last4			String containing the last 4 characters of the users creditcard number
	 * session			hibernate session object
	 */
	@SuppressWarnings("unused")
	public String getConfirmationCode(String ccode, ArenewEntity entity, ArenewPayment payment, List<ArenewPermits> pmt2, 
			List<ArenewVessels> ves2, String last4, Session session, String thisStagingDir, String thisConfirmDir, String imageLocation) {
		/*
		 * Variable description:
		 * curdate			null string container for formatted date
		 * stype			null string container for ship type amount
		 * pmttotal			double container for final permit fee totals
		 * vestotal			double container for final vessel fee totals
		 * recnum			temporary container holding permit or vessel list size
		 * pathname			absolute path where confirmation report will be saved
		 * stagename		absolute path where confirmation staging area resides, this is where the watermark gets placed
		 * imagepath		absolute path where images reside that are used on the form
		 * document			iText document object
		 */
		String curdate = null;
    	String stype = null;
    	String ftype = null;
    	double pmttotal = 0.0;
    	double vestotal = 0.0;
		long recnum = 0;
		String pathname = null;
		String stagename = null;
		String imagepath = null;
		boolean halred = false;
		boolean sabred = false;
		
		Document document = new Document(PageSize.LETTER, 50F, 50F, 50F, 50F);
    	try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            Date now = new Date();
            curdate = sdf.format(now);
            DateFormat f1 = new SimpleDateFormat("MM-dd-yyyy");
            stagename = thisStagingDir+"/confirm_"+ccode+".pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(stagename));
            pathname = thisConfirmDir+"/confirm_"+ccode+".pdf";
            imagepath = imageLocation+"/watermark.jpg";
            document.open();          
            Font fontTitle = new Font(2, 20F, 1);
            Font fontTitle2 = new Font(2, 14F, 1);
            Font fontTitle1 = new Font(2, 12F, 1);
            Font fontBody = new Font(2, 10F, 0);
            Font fontHead = new Font(2, 8F, 1);
            float widths[] = { 0.2F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.3F };
            PdfPTable table = new PdfPTable(widths);
            table.setWidthPercentage(100F);
            Paragraph ph = new Paragraph("CFEC Online Renewal Confirmation Report", fontTitle);
            ph.setAlignment(5);
            PdfPCell Title = new PdfPCell(ph);
            int tb = 13;
            Title.setBorder(tb);
            Title.setColspan(7);
            Title.setHorizontalAlignment(1);
            Title.setVerticalAlignment(5);
            Title.setPaddingTop(5);
            Title.setPaddingBottom(10);
            table.addCell(Title);
            Paragraph ps = new Paragraph((new StringBuilder()).append("Date: ").append(curdate).toString(), fontTitle2);
            ph.setAlignment(5);
            PdfPCell subTitle = new PdfPCell(ps);
            int bb = 14;
            subTitle.setBorder(bb);
            subTitle.setColspan(7);
            subTitle.setHorizontalAlignment(1);
            subTitle.setVerticalAlignment(5);
            subTitle.setPaddingBottom(10);
            table.addCell(subTitle);
            Paragraph c = new Paragraph("Your Confirmation number is " + ccode, fontTitle2);
            c.setAlignment(5);
            PdfPCell confirm = new PdfPCell(c);
            confirm.setBorder(0);
            confirm.setColspan(7);
            confirm.setPaddingTop(10);
            confirm.setPaddingBottom(10);
            confirm.setHorizontalAlignment(1);
            confirm.setVerticalAlignment(5);
            table.addCell(confirm);
            Paragraph c3 = new Paragraph("Your CFEC ID is " + entity.getId().getCfecid(), fontTitle2);
            c3.setAlignment(5);
            PdfPCell confirm3 = new PdfPCell(c3);
            confirm3.setBorder(0);
            confirm3.setColspan(7);
            confirm3.setPaddingTop(10);
            confirm3.setPaddingBottom(10);
            confirm3.setHorizontalAlignment(1);
            confirm3.setVerticalAlignment(5);
            table.addCell(confirm3);
            if (pmt2.size() > 0) {
            	for (int x=0;x<pmt2.size();x++) {
            		if (pmt2.get(x).getId().getFishery().equalsIgnoreCase("B 06B") || pmt2.get(x).getId().getFishery().equalsIgnoreCase("B 61B")) {
 	    				if (pmt2.get(x).isReducedfee()) {
	    					halred = true;
	    				} 
 	    			} else if (pmt2.get(x).getId().getFishery().equalsIgnoreCase("C 06B") || pmt2.get(x).getId().getFishery().equalsIgnoreCase("C 61B") 
 	    					|| pmt2.get(x).getId().getFishery().equalsIgnoreCase("C 09B") || pmt2.get(x).getId().getFishery().equalsIgnoreCase("C 91B")) {
 	    				if (pmt2.get(x).isReducedfee()) {
	    					sabred = true;
	    				} 
 	    			}
            	}
            }
            if (halred || sabred) {
            	Paragraph c1 = new Paragraph("NOTE: This renewal contains specific instructions related to your selection of " +
            	"reduced fees for either halibut or sablefish, or both. Please read and follow those instructions at the bottom " +
            	"of this confirmation report.", fontTitle1);
                c1.setAlignment(5);
                PdfPCell confirm1 = new PdfPCell(c1);
                confirm1.setBorder(0);
                confirm1.setColspan(7);
                confirm1.setPaddingTop(10);
                confirm1.setPaddingBottom(10);
                confirm1.setHorizontalAlignment(1);
                confirm1.setVerticalAlignment(5);
                table.addCell(confirm1);
            }            
            Paragraph c2 = new Paragraph("Please keep this receipt for your records. The confimation number and your CFEC ID will be used " +
            		"to reference your transaction. Once your online renewal is received by our Licensing staff, please allow 3 to 5 business days for processing.", fontBody);
            PdfPCell confirm2 = new PdfPCell(c2);
            confirm2.setBorder(0);
            confirm2.setColspan(7);
            confirm2.setHorizontalAlignment(0);
            confirm2.setVerticalAlignment(5);
            table.addCell(confirm2);
            Paragraph c6 = new Paragraph("You can log back into the CFEC Online Renewal System at any time to check the status of your transactions. " +
            		"If you need to speak to our CFEC Licensing staff about your online transaction, please wait 48 hours after you submitted your information, " +
            		"and have your CFEC ID and confirmation code available. Licensing staff may be reached at 907-789-6150.", fontBody);
			PdfPCell confirm6 = new PdfPCell(c6);
			confirm6.setBorder(0);
			confirm6.setColspan(7);
			confirm6.setHorizontalAlignment(0);
			confirm6.setVerticalAlignment(5);
			table.addCell(confirm6);
			//Paragraph c7 = new Paragraph("If CFEC discovers an error or finds a problem that prevents a particular vessel and/or " +
            //		"permit from being renewed, the remainder of your transaction will be processed and handled accordingly.", fontBody);
			Paragraph c7 = new Paragraph("Permits that require a vessel will not be embossed or mailed until that vessel has " +
            		"been licensed. If you do not intend to fish a permit, it will not be embossed or mailed.", fontBody);
            PdfPCell confirm7 = new PdfPCell(c7);
            confirm7.setBorder(0);
            confirm7.setColspan(7);
            confirm7.setHorizontalAlignment(0);
            confirm7.setVerticalAlignment(5);
            confirm7.setPaddingBottom(15);
            table.addCell(confirm7);
            Paragraph mb = new Paragraph("Your Transaction details", fontBody);
            PdfPCell mainBody1 = new PdfPCell(mb);
            int cb1 = 13;
            mainBody1.setBorder(cb1);
            mainBody1.setColspan(7);
            mainBody1.setHorizontalAlignment(1);
            mainBody1.setVerticalAlignment(5);
            table.addCell(mainBody1);
            boolean multiple = false;
            Paragraph mbph = null;
            if (multiple) {
            	mbph = new Paragraph("Permits Renewed (Past year(s) listed)", fontBody);
            	multiple = false;
            } else {
            	mbph = new Paragraph("Permits Renewed", fontBody);
            }
            PdfPCell mainBodyph = new PdfPCell(mbph);
            int cbph = 12;
            mainBodyph.setBorder(cbph);
            mainBodyph.setColspan(7);
            mainBodyph.setHorizontalAlignment(1);
            mainBodyph.setVerticalAlignment(5);
            mainBodyph.setPaddingBottom(5);
            table.addCell(mainBodyph);       
            recnum = pmt2.size();
            if (recnum > 0) {
            	Paragraph p = new Paragraph("Fishery", fontHead);
                p.setAlignment(5);
                PdfPCell pbody = new PdfPCell(p);
                pbody.setHorizontalAlignment(1);
                pbody.setVerticalAlignment(5);
                table.addCell(pbody);
                p = new Paragraph("Serial", fontHead);
                p.setAlignment(5);
                pbody = new PdfPCell(p);
                pbody.setHorizontalAlignment(1);
                pbody.setVerticalAlignment(5);
                table.addCell(pbody);
                p = new Paragraph("ADFG", fontHead);
                p.setAlignment(5);
                pbody = new PdfPCell(p);
                pbody.setHorizontalAlignment(1);
                pbody.setVerticalAlignment(5);
                table.addCell(pbody);
                p = new Paragraph("Fee", fontHead);
                p.setAlignment(5);
                pbody = new PdfPCell(p);
                pbody.setHorizontalAlignment(1);
                pbody.setVerticalAlignment(5);
                table.addCell(pbody);
                p = new Paragraph("Intent", fontHead);
                p.setAlignment(5);
                pbody = new PdfPCell(p);
                pbody.setHorizontalAlignment(1);
                pbody.setVerticalAlignment(5);
                table.addCell(pbody);
                p = new Paragraph("MSNA", fontHead);
                p.setAlignment(5);
                pbody = new PdfPCell(p);
                pbody.setHorizontalAlignment(1);
                pbody.setVerticalAlignment(5);
                table.addCell(pbody);
                p = new Paragraph("Description", fontHead);
                p.setAlignment(5);
                pbody = new PdfPCell(p);
                pbody.setHorizontalAlignment(1);
                pbody.setVerticalAlignment(5);
                table.addCell(pbody); 
                for (int x=0;x<pmt2.size();x++) {
    				String fishery = pmt2.get(x).getId().getFishery(); String serial = pmt2.get(x).getId().getSerial(); 
    				String adfg = pmt2.get(x).getAdfg(); String pyear = pmt2.get(x).getId().getPyear(); 
            		String fee = pmt2.get(x).getFee(); boolean intent = pmt2.get(x).isIntend(); String msna = pmt2.get(x).getMsna(); 
            		String description = pmt2.get(x).getDescription();
            		/*if (pmt2.get(x).getId().getFishery().equalsIgnoreCase("B 06B") || pmt2.get(x).getId().getFishery().equalsIgnoreCase("B 61B")) {
 	    				if (pmt2.get(x).isReducedfee()) {
	    					halred = true;
	    				} 
 	    			} else if (pmt2.get(x).getId().getFishery().equalsIgnoreCase("C 06B") || pmt2.get(x).getId().getFishery().equalsIgnoreCase("C 61B") 
 	    					|| pmt2.get(x).getId().getFishery().equalsIgnoreCase("C 09B") || pmt2.get(x).getId().getFishery().equalsIgnoreCase("C 91B")) {
 	    				if (pmt2.get(x).isReducedfee()) {
	    					sabred = true;
	    				} 
 	    			}*/
            		pmttotal = (pmttotal + Double.parseDouble(fee));
            		Paragraph mbp = new Paragraph(fishery + " (" + pyear + ")", fontBody);
                    PdfPCell mainBodyp = new PdfPCell(mbp);
                    mbp.setAlignment(5);
                    mainBodyp.setHorizontalAlignment(1);
                    mainBodyp.setVerticalAlignment(5);
                    mainBodyp.setPaddingBottom(3);
                    table.addCell(mainBodyp);
                    mbp = new Paragraph(serial, fontBody);
                    mbp.setAlignment(5);
                    mainBodyp = new PdfPCell(mbp);
                    mainBodyp.setHorizontalAlignment(1);
                    mainBodyp.setVerticalAlignment(5);
                    mainBodyp.setPaddingBottom(3);
                    table.addCell(mainBodyp);
                    mbp = new Paragraph(adfg, fontBody);
                    mbp.setAlignment(5);
                    mainBodyp = new PdfPCell(mbp);
                    mainBodyp.setHorizontalAlignment(1);
                    mainBodyp.setVerticalAlignment(5);
                    mainBodyp.setPaddingBottom(3);
                    table.addCell(mainBodyp);
                    mbp = new Paragraph(fee, fontBody);
                    mbp.setAlignment(5);
                    mainBodyp = new PdfPCell(mbp);
                    mainBodyp.setHorizontalAlignment(1);
                    mainBodyp.setVerticalAlignment(5);
                    mainBodyp.setPaddingBottom(3);
                    table.addCell(mainBodyp);
                    String intent2 = null;
                    if (intent) { intent2 = "Yes"; } else { intent2 = "No"; }
                    mbp = new Paragraph(intent2, fontBody);
                    mbp.setAlignment(5);
                    mainBodyp = new PdfPCell(mbp);
                    mainBodyp.setHorizontalAlignment(1);
                    mainBodyp.setVerticalAlignment(5);
                    mainBodyp.setPaddingBottom(3);
                    table.addCell(mainBodyp);
                    mbp = new Paragraph(msna, fontBody);
                    mbp.setAlignment(5);
                    mainBodyp = new PdfPCell(mbp);
                    mainBodyp.setHorizontalAlignment(1);
                    mainBodyp.setVerticalAlignment(5);
                    mainBodyp.setPaddingBottom(3);
                    table.addCell(mainBodyp);
                    mbp = new Paragraph(description, fontBody);
                    mbp.setAlignment(5);
                    mainBodyp = new PdfPCell(mbp);
                    mainBodyp.setHorizontalAlignment(1);
                    mainBodyp.setVerticalAlignment(5);
                    mainBodyp.setPaddingBottom(3);
                    table.addCell(mainBodyp);
                }          
            } else {
            	Paragraph mbph2 = new Paragraph("No Permits Renewed", fontBody);
                PdfPCell mainBodyph2 = new PdfPCell(mbph2);
                int cbph2 = 12;
                mainBodyph2.setBorder(cbph2);
                mainBodyph2.setColspan(7);
                mainBodyph2.setHorizontalAlignment(1);
                mainBodyph2.setVerticalAlignment(5);
                mainBodyph2.setPaddingTop(3);
                mainBodyph2.setPaddingBottom(5);
                table.addCell(mainBodyph2);      
            }
            Paragraph mbvh = new Paragraph("Vessels Renewed for " + entity.getId().getRyear(), fontBody);
            PdfPCell mainBodyvh = new PdfPCell(mbvh);
            int cbvh = 12;
            mainBodyvh.setBorder(cbvh);
            mainBodyvh.setColspan(7);
            mainBodyvh.setHorizontalAlignment(1);
            mainBodyvh.setVerticalAlignment(5);
            mainBodyvh.setPaddingTop(10);
            mainBodyvh.setPaddingBottom(5);
            table.addCell(mainBodyvh);
            recnum = ves2.size();
            if (recnum > 0) {
            	Paragraph v = new Paragraph("Name", fontHead);
                v.setAlignment(5);
                PdfPCell vbody = new PdfPCell(v);
                vbody.setHorizontalAlignment(1);
                vbody.setVerticalAlignment(5);
                table.addCell(vbody);
                v = new Paragraph("Reg. Number", fontHead);
                v.setAlignment(5);
                vbody = new PdfPCell(v);
                vbody.setHorizontalAlignment(1);
                vbody.setVerticalAlignment(5);
                table.addCell(vbody);
                v = new Paragraph("ADFG", fontHead);
                v.setAlignment(5);
                vbody = new PdfPCell(v);
                vbody.setHorizontalAlignment(1);
                vbody.setVerticalAlignment(5);
                table.addCell(vbody);
                v = new Paragraph("Fee", fontHead);
                v.setAlignment(5);
                vbody = new PdfPCell(v);
                vbody.setHorizontalAlignment(1);
                vbody.setVerticalAlignment(5);
                table.addCell(vbody);
                v = new Paragraph("Hull ID", fontHead);
                v.setAlignment(5);
                vbody = new PdfPCell(v);
                vbody.setHorizontalAlignment(1);
                vbody.setVerticalAlignment(5);
                table.addCell(vbody);
                v = new Paragraph("Year Built", fontHead);
                v.setAlignment(5);
                vbody = new PdfPCell(v);
                vbody.setHorizontalAlignment(1);
                vbody.setVerticalAlignment(5);
                table.addCell(vbody);
                v = new Paragraph("Homeport City/State", fontHead);
                v.setAlignment(5);
                vbody = new PdfPCell(v);
                vbody.setHorizontalAlignment(1);
                vbody.setVerticalAlignment(5);
                table.addCell(vbody);
                for (int x=0;x<ves2.size();x++) {
    				String name = ves2.get(x).getName(); String reg = ves2.get(x).getRegNum(); String adfg = ves2.get(x).getId().getAdfg(); String fee = ves2.get(x).getFee();
                	String hull = ves2.get(x).getHullId(); String built = ves2.get(x).getYearBuilt(); String hpcs = (ves2.get(x).getHomeportCity() + ", " + ves2.get(x).getHomeportState());
                	vestotal = (vestotal + Double.parseDouble(fee));
                	Paragraph mbv = new Paragraph(name, fontBody);
                    PdfPCell mainBodyv = new PdfPCell(mbv);
                    mbv.setAlignment(5);
                    mainBodyv.setHorizontalAlignment(1);
                    mainBodyv.setVerticalAlignment(5);
                    mainBodyv.setPaddingBottom(3);
                    table.addCell(mainBodyv);
                    mbv = new Paragraph(reg, fontBody);
                    mbv.setAlignment(5);
                    mainBodyv = new PdfPCell(mbv);
                    mainBodyv.setHorizontalAlignment(1);
                    mainBodyv.setVerticalAlignment(5);
                    mainBodyv.setPaddingBottom(3);
                    table.addCell(mainBodyv);
                    mbv = new Paragraph(adfg, fontBody);
                    mbv.setAlignment(5);
                    mainBodyv = new PdfPCell(mbv);
                    mainBodyv.setHorizontalAlignment(1);
                    mainBodyv.setVerticalAlignment(5);
                    mainBodyv.setPaddingBottom(3);
                    table.addCell(mainBodyv);
                    mbv = new Paragraph(fee, fontBody);
                    mbv.setAlignment(5);
                    mainBodyv = new PdfPCell(mbv);
                    mainBodyv.setHorizontalAlignment(1);
                    mainBodyv.setVerticalAlignment(5);
                    mainBodyv.setPaddingBottom(3);
                    table.addCell(mainBodyv);
                    mbv = new Paragraph(hull, fontBody);
                    mbv.setAlignment(5);
                    mainBodyv = new PdfPCell(mbv);
                    mainBodyv.setHorizontalAlignment(1);
                    mainBodyv.setVerticalAlignment(5);
                    mainBodyv.setPaddingBottom(3);
                    table.addCell(mainBodyv);
                    mbv = new Paragraph(built, fontBody);
                    mbv.setAlignment(5);
                    mainBodyv = new PdfPCell(mbv);
                    mainBodyv.setHorizontalAlignment(1);
                    mainBodyv.setVerticalAlignment(5);
                    mainBodyv.setPaddingBottom(3);
                    table.addCell(mainBodyv);
                    mbv = new Paragraph(hpcs, fontBody);
                    mbv.setAlignment(5);
                    mainBodyv = new PdfPCell(mbv);
                    mainBodyv.setHorizontalAlignment(1);
                    mainBodyv.setVerticalAlignment(5);
                    mainBodyv.setPaddingBottom(3);
                    table.addCell(mainBodyv);
                }
            } else {
            	Paragraph mbvh2 = new Paragraph("No Vessels Renewed", fontBody);
                PdfPCell mainBodyvh2 = new PdfPCell(mbvh2);
                int cbvh2 = 12;
                mainBodyvh2.setBorder(cbvh2);
                mainBodyvh2.setColspan(7);
                mainBodyvh2.setHorizontalAlignment(1);
                mainBodyvh2.setVerticalAlignment(5);
                mainBodyvh2.setPaddingTop(3);
                mainBodyvh2.setPaddingBottom(5);
                table.addCell(mainBodyvh2);
            }
            Paragraph sum = new Paragraph("Summary:", fontBody);
            PdfPCell sumBody = new PdfPCell(sum);
            int csum = 4;
            sumBody.setBorder(csum);
            sumBody.setColspan(5);
            sumBody.setHorizontalAlignment(0);
            sumBody.setVerticalAlignment(5);
            sumBody.setPaddingTop(10);
            sumBody.setPaddingBottom(5);
            sumBody.setPaddingLeft(10);
            table.addCell(sumBody);
            sum = new Paragraph("Explanation:", fontHead);
            sum.setAlignment(5);
            sumBody = new PdfPCell(sum);
            csum = 8;
            sumBody.setBorder(csum);
            sumBody.setColspan(2);
            sumBody.setHorizontalAlignment(0);
            sumBody.setVerticalAlignment(5);
            sumBody.setPaddingLeft(10);
            table.addCell(sumBody);
            if (payment.getShiptype().equalsIgnoreCase("fm")) {
            	stype = "$0.00";
            	ftype = "$0.00";
            } else {
            	stype = "$15.00";
            	ftype = "$24.25";
            }
            Paragraph sum1 = new Paragraph("Name: " + entity.getXname(), fontBody);
            PdfPCell sumBody1 = new PdfPCell(sum1);
            int csum1 = 4;
            sumBody1.setBorder(csum1);
            sumBody1.setColspan(5);
            sumBody1.setHorizontalAlignment(0);
            sumBody1.setVerticalAlignment(5);
            sumBody1.setPaddingTop(5);
            sumBody1.setPaddingBottom(3);
            sumBody1.setPaddingLeft(5);
            table.addCell(sumBody1);
            sum1 = new Paragraph("Your complete name as it appears in our records", fontHead);
            sum1.setAlignment(5);
            sumBody1 = new PdfPCell(sum1);
            csum1 = 8;
            sumBody1.setBorder(csum1);
            sumBody1.setColspan(2);
            sumBody1.setHorizontalAlignment(0);
            sumBody1.setVerticalAlignment(5);
            table.addCell(sumBody1);
            String poverty2 = null;
            if (entity.getPoverty().equalsIgnoreCase("true")) { poverty2 = "Yes"; } else { poverty2 = "No"; }
            Paragraph sum2 = new Paragraph("Poverty Fee: " + poverty2, fontBody);
            PdfPCell sumBody2 = new PdfPCell(sum2);
            int csum2 = 4;
            sumBody2.setBorder(csum2);
            sumBody2.setColspan(5);
            sumBody2.setHorizontalAlignment(0);
            sumBody2.setVerticalAlignment(5);
            sumBody2.setPaddingTop(5);
            sumBody2.setPaddingBottom(3);
            sumBody2.setPaddingLeft(5);
            table.addCell(sumBody2);
            sum2 = new Paragraph("Your qualification status to receive the poverty fee", fontHead);
            sum2.setAlignment(5);
            sumBody2 = new PdfPCell(sum2);
            csum2 = 8;
            sumBody2.setBorder(csum2);
            sumBody2.setColspan(2);
            sumBody2.setHorizontalAlignment(0);
            sumBody2.setVerticalAlignment(5);
            table.addCell(sumBody2);
            String res = null;
            if (entity.getResidency().equalsIgnoreCase("R")) {
            	res = "Yes";
            } else {
            	res = "No";
            }
            Paragraph sum3 = new Paragraph("Residence: " + res, fontBody);
            PdfPCell sumBody3 = new PdfPCell(sum3);
            int csum3 = 4;
            sumBody3.setBorder(csum3);
            sumBody3.setColspan(5);
            sumBody3.setHorizontalAlignment(0);
            sumBody3.setVerticalAlignment(5);
            sumBody3.setPaddingTop(5);
            sumBody3.setPaddingBottom(3);
            sumBody3.setPaddingLeft(5);
            table.addCell(sumBody3);
            sum3 = new Paragraph("Your status regarding State of Alaska residency", fontHead);
            sum3.setAlignment(5);
            sumBody3 = new PdfPCell(sum3);
            csum3 = 8;
            sumBody3.setBorder(csum2);
            sumBody3.setColspan(2);
            sumBody3.setHorizontalAlignment(0);
            sumBody3.setVerticalAlignment(5);
            table.addCell(sumBody3);
            String citizen2 = null;
            if (entity.getCitizen().equalsIgnoreCase("true")) { citizen2 = "Yes"; } else { citizen2 = "No"; }
            Paragraph sum4 = new Paragraph("Citizenship: " + citizen2, fontBody);
            PdfPCell sumBody4 = new PdfPCell(sum4);
            int csum4 = 4;
            sumBody4.setBorder(csum4);
            sumBody4.setColspan(5);
            sumBody4.setHorizontalAlignment(0);
            sumBody4.setVerticalAlignment(5);
            sumBody4.setPaddingTop(5);
            sumBody4.setPaddingBottom(3);
            sumBody4.setPaddingLeft(5);
            table.addCell(sumBody4);            
            sum4 = new Paragraph("Your status regarding your United States citizenship", fontHead);
            sum4.setAlignment(5);
            sumBody4 = new PdfPCell(sum4);
            csum4 = 8;
            sumBody4.setBorder(csum2);
            sumBody4.setColspan(2);
            sumBody4.setHorizontalAlignment(0);
            sumBody4.setVerticalAlignment(5);
            table.addCell(sumBody4);            
            Paragraph sum5 = new Paragraph("Agent: " + entity.getAgent(), fontBody);
            PdfPCell sumBody5 = new PdfPCell(sum5);
            int csum5 = 4;
            sumBody5.setBorder(csum5);
            sumBody5.setColspan(5);
            sumBody5.setHorizontalAlignment(0);
            sumBody5.setVerticalAlignment(5);
            sumBody5.setPaddingTop(5);
            sumBody5.setPaddingBottom(3);
            sumBody5.setPaddingLeft(5);
            table.addCell(sumBody5);            
            sum5 = new Paragraph("Your Agent or Representative's complete name", fontHead);
            sum5.setAlignment(5);
            sumBody5 = new PdfPCell(sum5);
            csum5 = 8;
            sumBody5.setBorder(csum2);
            sumBody5.setColspan(2);
            sumBody5.setHorizontalAlignment(0);
            sumBody5.setVerticalAlignment(5);
            table.addCell(sumBody5);
            String msnarea = null;
            if (entity.getMsnarea() == null) {
            	msnarea = "None";
            } else {
            	msnarea = entity.getMsnarea().toString().toUpperCase();
            }
            Paragraph sum6 = new Paragraph("MSN Area: " + msnarea, fontBody);
            PdfPCell sumBody6 = new PdfPCell(sum6);
            int csum6 = 4;
            sumBody6.setBorder(csum6);
            sumBody6.setColspan(5);
            sumBody6.setHorizontalAlignment(0);
            sumBody6.setVerticalAlignment(5);
            sumBody6.setPaddingTop(5);
            sumBody6.setPaddingBottom(3);
            sumBody6.setPaddingLeft(5);
            table.addCell(sumBody6);            
            sum6 = new Paragraph("The area you specified for Multiple Salmon Net permits", fontHead);
            sum6.setAlignment(5);
            sumBody6 = new PdfPCell(sum6);
            csum6 = 8;
            sumBody6.setBorder(csum6);
            sumBody6.setColspan(2);
            sumBody6.setHorizontalAlignment(0);
            sumBody6.setVerticalAlignment(5);
            table.addCell(sumBody6);        
            Paragraph sum8 = new Paragraph("Credit Card ending in: XXXX-XXXX-XXXX-"+last4, fontBody);
            PdfPCell sumBody8 = new PdfPCell(sum8);
            int csum8 = 4;
            sumBody8.setBorder(csum8);
            sumBody8.setColspan(5);
            sumBody8.setHorizontalAlignment(0);
            sumBody8.setVerticalAlignment(5);
            sumBody8.setPaddingTop(5);
            sumBody8.setPaddingBottom(3);
            sumBody8.setPaddingLeft(5);
            table.addCell(sumBody8);
            sum8 = new Paragraph("The Credit Card you used to purchase your licenses", fontHead);
            sum8.setAlignment(5);
            sumBody8 = new PdfPCell(sum8);
            csum8 = 8;
            sumBody8.setBorder(csum8);
            sumBody8.setColspan(2);
            sumBody8.setHorizontalAlignment(0);
            sumBody8.setVerticalAlignment(5);
            table.addCell(sumBody8);
            String ct = null;
            if (payment.getCctype().equalsIgnoreCase("vi")) {
            	ct = "Visa";
            } else if (payment.getCctype().equalsIgnoreCase("mc")) {
            	ct = "Master Card";
            } else if (payment.getCctype().equalsIgnoreCase("di")) {
            	ct = "Discovery";
            } else {
            	ct = "Unknown";
            }
            Paragraph sum9 = new Paragraph("Credit Card Type: " + ct, fontBody);
            PdfPCell sumBody9 = new PdfPCell(sum9);
            int csum9 = 4;
            sumBody9.setBorder(csum9);
            sumBody9.setColspan(5);
            sumBody9.setHorizontalAlignment(0);
            sumBody9.setVerticalAlignment(5);
            sumBody9.setPaddingTop(5);
            sumBody9.setPaddingBottom(3);
            sumBody9.setPaddingLeft(5);
            table.addCell(sumBody9);
            sum9 = new Paragraph("The Credit Card type you used to purchase your licenses", fontHead);
            sum9.setAlignment(5);
            sumBody9 = new PdfPCell(sum9);
            csum9 = 8;
            sumBody9.setBorder(csum9);
            sumBody9.setColspan(2);
            sumBody9.setHorizontalAlignment(0);
            sumBody9.setVerticalAlignment(5);
            table.addCell(sumBody9);
            String st = null;
            Double tad = Double.parseDouble(payment.getTotalamount());
            String ta = Double.toString(tad);
            if (payment.getShiptype().equalsIgnoreCase("fm")) {
            	st = "FirstClass";
            } else if (payment.getShiptype().equalsIgnoreCase("em")) {
            	st = "Express";
            } else {
            	st = "Unknown";
            }
            Paragraph sum11 = new Paragraph("Mail Service Fee: " + stype, fontBody);
            PdfPCell sumBody11 = new PdfPCell(sum11);
            int csum11 = 4;
            sumBody11.setBorder(csum11);
            sumBody11.setColspan(5);
            sumBody11.setHorizontalAlignment(0);
            sumBody11.setVerticalAlignment(5);
            sumBody11.setPaddingTop(5);
            sumBody11.setPaddingBottom(3);
            sumBody11.setPaddingLeft(5);
            table.addCell(sumBody11);
            sum11 = new Paragraph("The Mail Service Fee, if applicable, for Express Mail", fontHead);
            sum11.setAlignment(5);
            sumBody11 = new PdfPCell(sum11);
            csum11 = 8;
            sumBody11.setBorder(csum11);
            sumBody11.setColspan(2);
            sumBody11.setHorizontalAlignment(0);
            sumBody11.setVerticalAlignment(5);
            table.addCell(sumBody11);
            Paragraph sum22 = new Paragraph("Standard Express Ship Rate Fee: " + ftype, fontBody);
            PdfPCell sumBody22 = new PdfPCell(sum22);
            int csum22 = 4;
            sumBody22.setBorder(csum22);
            sumBody22.setColspan(5);
            sumBody22.setHorizontalAlignment(0);
            sumBody22.setVerticalAlignment(5);
            sumBody22.setPaddingTop(5);
            sumBody22.setPaddingBottom(3);
            sumBody22.setPaddingLeft(5);
            table.addCell(sumBody22);
            sum22 = new Paragraph("The Standard Express Ship Rate Fee, if applicable, for Express Mail", fontHead);
            sum22.setAlignment(5);
            sumBody22 = new PdfPCell(sum22);
            csum22 = 8;
            sumBody22.setBorder(csum22);
            sumBody22.setColspan(2);
            sumBody22.setHorizontalAlignment(0);
            sumBody22.setVerticalAlignment(5);
            table.addCell(sumBody22);            
            String diffresults = null;
            Double diffAmountD = Double.parseDouble(entity.getDiffamount());
            String diffAmount = (Double.toString(diffAmountD) + "0");
            if (diffAmountD > 0) {
            	diffresults = "Yes";
            } else {
            	diffresults = "No";
            }
            Paragraph sum7 = new Paragraph("Differential: " + diffresults + " - $" + diffAmount, fontBody);
            PdfPCell sumBody7 = new PdfPCell(sum7);
            int csum7 = 4;
            sumBody7.setBorder(csum7);
            sumBody7.setColspan(5);
            sumBody7.setHorizontalAlignment(0);
            sumBody7.setVerticalAlignment(5);
            sumBody7.setPaddingTop(5);
            sumBody7.setPaddingBottom(3);
            sumBody7.setPaddingLeft(5);
            table.addCell(sumBody7);            
            sum7 = new Paragraph("Status of your differential payment (Non-Residents only)", fontHead);
            sum7.setAlignment(5);
            sumBody7 = new PdfPCell(sum7);
            csum7 = 8;
            sumBody7.setBorder(csum7);
            sumBody7.setColspan(2);
            sumBody7.setHorizontalAlignment(0);
            sumBody7.setVerticalAlignment(5);
            table.addCell(sumBody7);            
            Paragraph sum12 = new Paragraph("Total Permit fees: $" + (Double.toString(pmttotal) + "0"), fontBody);
            PdfPCell sumBody12 = new PdfPCell(sum12);
            int csum12 = 4;
            sumBody12.setBorder(csum12);
            sumBody12.setColspan(5);
            sumBody12.setHorizontalAlignment(0);
            sumBody12.setVerticalAlignment(5);
            sumBody12.setPaddingTop(5);
            sumBody12.setPaddingBottom(3);
            sumBody12.setPaddingLeft(5);
            table.addCell(sumBody12);            
            sum12 = new Paragraph("The total calculated fees for permits being renewed", fontHead);
            sum12.setAlignment(5);
            sumBody12 = new PdfPCell(sum12);
            csum12 = 8;
            sumBody12.setBorder(csum12);
            sumBody12.setColspan(2);
            sumBody12.setHorizontalAlignment(0);
            sumBody12.setVerticalAlignment(5);
            table.addCell(sumBody12);            
            Paragraph sum13 = new Paragraph("Total Vessel fees: $" + (Double.toString(vestotal) + "0"), fontBody);
            PdfPCell sumBody13 = new PdfPCell(sum13);
            int csum13 = 4;
            sumBody13.setBorder(csum13);
            sumBody13.setColspan(5);
            sumBody13.setHorizontalAlignment(0);
            sumBody13.setVerticalAlignment(5);
            sumBody13.setPaddingTop(5);
            sumBody13.setPaddingBottom(3);
            sumBody13.setPaddingLeft(5);
            table.addCell(sumBody13);            
            sum13 = new Paragraph("The total calculated fees for vessels being licensed", fontHead);
            sum13.setAlignment(5);
            sumBody13 = new PdfPCell(sum13);
            csum13 = 8;
            sumBody13.setBorder(csum13);
            sumBody13.setColspan(2);
            sumBody13.setHorizontalAlignment(0);
            sumBody13.setVerticalAlignment(5);
            table.addCell(sumBody13);            
            Paragraph sum10 = new Paragraph("* - Total Amount to be billed: $" + (ta), fontBody);
            PdfPCell sumBody10 = new PdfPCell(sum10);
            int csum10 = 4;
            sumBody10.setBorder(csum10);
            sumBody10.setColspan(5);
            sumBody10.setHorizontalAlignment(0);
            sumBody10.setVerticalAlignment(5);
            sumBody10.setPaddingTop(5);
            sumBody10.setPaddingBottom(3);
            sumBody10.setPaddingLeft(5);
            table.addCell(sumBody10);
            sum10 = new Paragraph("The total amount (pre-audit) to be billed", fontHead);
            sum10.setAlignment(5);
            sumBody10= new PdfPCell(sum10);
            csum10 = 8;
            sumBody10.setBorder(csum10);
            sumBody10.setColspan(2);
            sumBody10.setHorizontalAlignment(0);
            sumBody10.setVerticalAlignment(5);
            table.addCell(sumBody10);            
            Paragraph end = new Paragraph("", fontBody);
            PdfPCell endBody = new PdfPCell(end);
            int cend = 14;
            endBody.setBorder(cend);
            endBody.setColspan(7);
            endBody.setHorizontalAlignment(0);
            endBody.setVerticalAlignment(5);
            endBody.setPaddingTop(5);
            endBody.setPaddingBottom(3);
            table.addCell(endBody);            
            Paragraph c17 = new Paragraph("", fontBody);
            PdfPCell confirm17 = new PdfPCell(c17);
            confirm17.setBorder(0);
            confirm17.setColspan(7);
            confirm17.setHorizontalAlignment(0);
            confirm17.setVerticalAlignment(5);
            confirm17.setPaddingBottom(15);
            table.addCell(confirm17);         
            if (halred) {
            	Paragraph c18 = new Paragraph("IMPORTANT NOTE: - You have specified the reduced fee option for a halibut permit. You MUST provide " + 
    			"evidence that you either landed under 8000 lbs of halibut in the previous year (IFQ landing report) OR you are a member of a Western AK CDQ halibut group " + 
    	    	"(Statement of CDQ participation). This documentation can be emailed to dfg.CFEC.licensing@alaska.gov or faxed to 907-789-6170." +
    	    	"Your application will processed AFTER we receive this supporting documentation. ", fontBody);
                PdfPCell confirm18 = new PdfPCell(c18);
                confirm18.setBorder(0);
                confirm18.setColspan(7);
                confirm18.setHorizontalAlignment(0);
                confirm18.setVerticalAlignment(5);
                confirm18.setPaddingBottom(15);
                table.addCell(confirm18);    
            }
            if (sabred) {
            	Paragraph c19 = new Paragraph("IMPORTANT NOTE: - You have specified the reduced fee option for a sablefish permit. You MUST provide " + 
    			"evidence that you landed under 9000 lbs of sablefish in the previous year (IFQ landing report). " + 
    	    	"This documentation can be emailed to dfg.CFEC.licensing@alaska.gov or faxed to 907-789-6170." +
    	    	"Your application will processed AFTER we receive this supporting documentation. ", fontBody);
                PdfPCell confirm19 = new PdfPCell(c19);
                confirm19.setBorder(0);
                confirm19.setColspan(7);
                confirm19.setHorizontalAlignment(0);
                confirm19.setVerticalAlignment(5);
                confirm19.setPaddingBottom(15);
                table.addCell(confirm19);    
            }            
            Paragraph c20 = new Paragraph("PLEASE NOTE: The final price on this confirmation report may be subject to change based on required " +
            	"information not yet received such as poverty fees, reduced fees and shipping requirements");
            PdfPCell confirm20 = new PdfPCell(c20);
            confirm20.setBorder(0);
            confirm20.setColspan(7);
            confirm20.setHorizontalAlignment(0);
            confirm20.setVerticalAlignment(5);
            confirm20.setPaddingBottom(15);
            table.addCell(confirm20);    
            
            document.add(table);
    	} catch (DocumentException de) {
        	String err = "";
			String errEmail = "";
			int errcount = 0;
			err = "";
			errEmail = "";
			errcount = 0;
			StackTraceElement elements[] = de.getStackTrace();
			for  ( int i=0, n=elements.length; i < n; i++ )   {
				if (elements[i].getFileName().startsWith("Leon")) {
					err = err + "File name: " + elements[i].getFileName() + " - Line Number: " + elements[i].getLineNumber() + " - Method: " + elements[i].getMethodName() + "$";
					errEmail = errEmail + "File name: " + elements[i].getFileName() + " - Line Number: " + elements[i].getLineNumber() + " - Method: " + elements[i].getMethodName() + "\r";
					errcount = (errcount + 1);
				}
		    }
			errEmail = errEmail + "\r" + de;
			//sendError(varArray[0], errEmail);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
       		//if (session.isOpen()) {
       		//	session.close();
       		//}
       	}
		document.close();
		addWatermark(pathname, stagename, imagepath);
		String url = ccode;
		return url;
	}
	
	public void addWatermark(String path, String stage, String image) {
		try {
			PdfReader reader = new PdfReader(stage);
		    int n = reader.getNumberOfPages();
		    PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(path));
            int i = 1;
            PdfContentByte under;
            Image watermark = Image.getInstance(image);
            watermark.setAbsolutePosition(50, 150);  
            while (i <= n) {
            	under = stamp.getUnderContent(i);
                under.addImage(watermark);        
                i++;
            }
            stamp.close();
		} catch (Exception io) {
            System.out.println("Error adding watermark to PDF: path " + path +  " stage " + stage + " image " + image + " " + io.getMessage());
			io.printStackTrace();
		}		
	}

}
