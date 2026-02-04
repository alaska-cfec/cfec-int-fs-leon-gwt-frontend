package com.cfecweb.leon.server;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.cfecweb.leon.shared.ArenewEntity;
import com.cfecweb.leon.shared.ArenewPayment;

import static com.cfecweb.leon.server.PDType1Fonts.TIMES_ROMAN;

public class InserterForm {
	static PDDocument inserter;
	static PDPage pg;
	
	/*public static void main(String[] args) {
		String cnum = "0120-61622-AAAAAA";
		String name = "DALE B BOSWORTH";
		String address = "BOX 45";
		String city = "PETERSBURG";
		String state = "AK";
		String zip = "99833";
		getInserter(cnum, name, address, city, state, zip, "test", "/home/mcmity/scratch/pdf/inserter");
	}*/
	
	public void getInserter(String cnum, String env, String inserterOut, ArenewEntity entity, ArenewPayment pay) throws IOException {
		inserter = new PDDocument();
		pg = new PDPage();
		inserter.addPage(pg);
		PDPageContentStream content;
		content = new PDPageContentStream(inserter, pg);
		/*
		 * user designation, in the case LN for LEON Automation
		 */
		setContent(content, TIMES_ROMAN, 12, 300, 95, "LN");
		/*
		 *  name and address
		 */
		//System.out.println("Inserter BADDRESS content is " + pay.getBaddress());
		if (pay.getBaddress().equalsIgnoreCase("N/A")) {
			content.beginText();
			content.setFont(TIMES_ROMAN, 12);
			content.setLeading(14.5f);
			content.newLineAtOffset(60, 80);
			content.showText(entity.getXname().toUpperCase());
			content.newLine();
			content.showText(entity.getPaddress().toUpperCase());
			content.newLine();
			content.showText(entity.getPcity().toUpperCase() + "              " + 
			        entity.getPstate().toUpperCase()	+ "  " + entity.getPzip().toUpperCase());
			content.endText();
		} else {
			content.beginText();
			content.setFont(TIMES_ROMAN, 12);
			content.setLeading(14.5f);
			content.newLineAtOffset(60, 80);
			content.showText(entity.getXname().toUpperCase());
			content.newLine();
			content.showText(pay.getBaddress().toUpperCase());
			content.newLine();
			content.showText(pay.getBcity().toUpperCase() + "              " + 
			        pay.getBstate().toUpperCase()	+ "  " + pay.getBzip().toUpperCase());
			content.endText();			
		}
		
		/*
		 *  form name in lower left					 
		 */
		setContent(content, TIMES_ROMAN, 6, 30, 17, "PMTINSRT");
		/*
		 *  Close the content stream
		 */
		content.close();
		//if (env.equalsIgnoreCase("test")) {
		//	inserter.save("/home/mcmity/scratch/pdf/inserter/" + cnum + ".pdf");
		//} else {
			inserter.save(inserterOut + "/" + cnum + ".pdf");
		//}
		/*
		 * close PDF document
		 */
		inserter.close();			
	}
	
	private static void setContent(PDPageContentStream content, PDType1Font font, int size, int x, int y, String title) {
		try {
			content.beginText();
			content.setFont(font, size);
			content.newLineAtOffset(x, y);
			content.showText(title);
			content.endText();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
