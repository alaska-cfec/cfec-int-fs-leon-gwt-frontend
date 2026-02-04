package com.cfecweb.leon.server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.cfecweb.leon.config.HibernateSessionFactoryProvider;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.impl.pdf417.PDF417Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cfecweb.leon.shared.ArenewEntity;
import com.cfecweb.leon.shared.ArenewEntityId;
import com.cfecweb.leon.shared.ArenewPermits;
import com.cfecweb.leon.solo.BuildChkSeq;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import static com.cfecweb.leon.server.PDType1Fonts.TIMES_ROMAN;
//import com.google.zxing.qrcode.QRCodeWriter;

public class PermitCertificate {
	static PDDocument pcert = null;
	static PDPage pg = null;
	static PDPageContentStream content = null;
	static boolean found = false;
	static BuildChkSeq chkseq = new BuildChkSeq();
	static Session session = null;
	static final SessionFactory fact = HibernateSessionFactoryProvider.getSessionFactory();
	
	public static void main(String[] args) {
		ArenewEntityId entId = new ArenewEntityId();
		entId.setCfecid("508039");
		entId.setRyear("2020");
		session = fact.openSession();
		ArenewEntity entity = (ArenewEntity) session.get(ArenewEntity.class, entId);
		List<ArenewPermits> permits = new ArrayList<ArenewPermits>();
		permits.addAll(entity.getArenewPermitses());
		try {
			getPerCert("0120-94956-508039", "/home/mcmity/scratch/pdf/pmtcert", "508039", "2020", entity, permits, "test", "Test Vessel", "60");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			session.close();
			fact.close();
		}
	}
	
	public static void getPerCert(String cnum, String destfile, String cfecid, String ryear, ArenewEntity entity, 
			List<ArenewPermits> permits, String mode, String vname, String vlength) throws IOException {
		pcert = null;
		pg = null;
		content = null;
		found = false;
		String encodeLine = null;
		for (Iterator<ArenewPermits> pc = permits.iterator(); pc.hasNext();) {
			ArenewPermits permit = (ArenewPermits) pc.next();
			if (permit.getId().getSerial().equalsIgnoreCase("Not Issued")) {
				System.out.println(entity.getId().getCfecid() + " This permit is new, no need for a permit certificate now - " + cnum);
			} else {
				if (permit.getArenewPayment().getConfirmcode().equalsIgnoreCase(cnum)) {
					encodeLine = chkseq.getencode(permit.getId().getFishery(), permit.getId().getSerial(), permit.getId().getRyear(), "01", entity.getXname(), 
							permit.getId().getCfecid(), "BRISTOL DRIFTER", permit.getAdfg(), "55", permit.getDescription(), entity.getResidency());
					//System.out.println(encodeLine);
					found = true;
					pcert = Loader.loadPDF(new File("/home/mcmity/scratch/pdf/forms/pmt2021_9.pdf"));
					pg = pcert.getDocumentCatalog().getPages().get(0);
					content = new PDPageContentStream(pcert, pg,  PDPageContentStream.AppendMode.APPEND, true, true);
					/*
					 * The line below was intended to put in either a serial/chk number or fishery code in the main doc title.
					 */
					//setContent(content,TIMES_ROMAN, 14, 220, 708, "123456");
					/*
					 *  Permit Holder Header					 
					 */
					setContent(content, TIMES_ROMAN, 14, 60, 605, "Permit Holder");
					drawLine(content, "Permit Holder", 1, 60, 605, -2, 14, TIMES_ROMAN);
					/*
					 *  Address Header	
					 */
					setContent(content, TIMES_ROMAN, 14, 355, 605, "Permanent Mailing Address (if different)");
					drawLine(content, "Permanent Mailing Address (if different)", 1, 355, 605, -2, 14, TIMES_ROMAN);
					/*
					 *  Vessel Owner Content	
					 */
					
					/*
					 * Bar Code
					 */
					//try {
					//	generateQRCodeImage(pcert, pg, encodeLine, 230, 532);
					//} catch (WriterException e) {
					//	// TODO Auto-generated catch block
					//	e.printStackTrace();
					//}
					
					//System.out.println("VesselReceipt BADDRESS content is " + vessel.getDrenewPayment().getBaddress());
					if (permit.getArenewPayment().getBaddress().equalsIgnoreCase("N/A")) {
						content.beginText();
						content.setFont(TIMES_ROMAN, 12);
						content.setLeading(14.5f);
						content.newLineAtOffset(60, 586);
						content.showText(entity.getXname().toUpperCase());
						content.newLine();
						content.showText(entity.getPaddress().toUpperCase());
						content.newLine();
						content.showText(entity.getPcity().toUpperCase() + "              " + 
								entity.getPstate().toUpperCase()	+ "  " + entity.getPzip().toUpperCase());
						content.endText();
						/*
						 *  Close the content stream
						 */
						//content.close();
					} else {
						content.beginText();
						content.setFont(TIMES_ROMAN, 12);
						content.setLeading(14.5f);
						content.newLineAtOffset(60, 586);
						content.showText(entity.getXname().toUpperCase());
						content.newLine();
						content.showText(permit.getArenewPayment().getBaddress().toUpperCase());
						content.newLine();
						content.showText(permit.getArenewPayment().getBcity().toUpperCase() + "              " + 
								permit.getArenewPayment().getBstate().toUpperCase()	+ "  " + permit.getArenewPayment().getBzip().toUpperCase());
						content.endText();
						content.beginText();
						content.setFont(TIMES_ROMAN, 12);
						content.setLeading(14.5f);
						content.newLineAtOffset(355, 586);
						content.showText(entity.getXname().toUpperCase());
						content.newLine();
						content.showText(entity.getPaddress().toUpperCase());
						content.newLine();
						content.showText(entity.getPcity().toUpperCase() + "              " + 
								entity.getPstate().toUpperCase()	+ "  " + entity.getPzip().toUpperCase());
						content.endText();						
					}
					/*
					 * First fold marks
					 */
					drawLine(content, "--", 1, 10, 520, -2, 14, TIMES_ROMAN);
					drawLine(content, "--", 1, 300, 520, -2, 14, TIMES_ROMAN);
					drawLine(content, "--", 1, 595, 520, -2, 14, TIMES_ROMAN);
					/*
					 *  Permit Holder Residency Declaration					 
					 */
					setContent(content, TIMES_ROMAN, 14, 60, 465, "Residency Declaration");
					drawLine(content, "Residency Declaration", 1, 60, 465, -2, 14, TIMES_ROMAN);
					/*
					 *  Permit Holder Residency Content					 
					 */
					String res = null;
					if (entity.getResidency().equalsIgnoreCase("N")) {
						res = "NON-RESIDENT";
					} else {
						res = "AK-RESIDENT";
					}
					setContent(content, TIMES_ROMAN, 12, 80, 446, res);
					/*
					 * Fishing Vessel Name
					 */
					setContent(content, TIMES_ROMAN, 14, 230, 465, "Vessel Name");
					drawLine(content, "Vessel Name", 1, 230, 465, -2, 14, TIMES_ROMAN);
					/*
					 * Fishing Vessel content
					 */
					setContent(content, TIMES_ROMAN, 12, 230, 446, vname.toUpperCase());
					/*
					 * Fishing Vessel ADFG
					 */
					setContent(content, TIMES_ROMAN, 14, 370, 465, "Vessel ADFG");
					drawLine(content, "Vessel ADFG", 1, 370, 465, -2, 14, TIMES_ROMAN);
					/*
					 * Fishing ADFG content
					 */
					setContent(content, TIMES_ROMAN, 12, 395, 446, permit.getAdfg().toUpperCase());
					/*
					 * Fishing Vessel length
					 */
					setContent(content, TIMES_ROMAN, 14, 485, 465, "Vessel Length");
					drawLine(content, "Vessel Length", 1, 485, 465, -2, 14, TIMES_ROMAN);
					/*
					 * Fishing length content
					 */
					setContent(content, TIMES_ROMAN, 12, 520, 446, vlength.toUpperCase());
					/*
					 * Fishery Code 
					 */
					setContent(content, TIMES_ROMAN, 14, 60, 395, "Fishery Code");
					drawLine(content, "Fishery Code", 1, 60, 395, -2, 14, TIMES_ROMAN);
					/*
					 * Fishery Code content
					 */
					setContent(content, TIMES_ROMAN, 12, 80, 373, permit.getId().getFishery().toUpperCase());
					/*
					 * Fishery Serial Number 
					 */
					setContent(content, TIMES_ROMAN, 14, 165, 395, "Fishery Serial Number");
					drawLine(content, "Fishery Serial Number", 1, 165, 395, -2, 14, TIMES_ROMAN);
					/*
					 * Fishery Serial Number content
					 */
					setContent(content, TIMES_ROMAN, 12, 205, 373, permit.getId().getSerial().toUpperCase() + "-" + "X");
					/*
					 * Fishery Year
					 */
					setContent(content, TIMES_ROMAN, 14, 325, 395, "Fishery Year");
					drawLine(content, "Fishery Year", 1, 325, 395, -2, 14, TIMES_ROMAN);
					/*
					 * Fishery Year content
					 */
					setContent(content, TIMES_ROMAN, 12, 345, 373, permit.getId().getRyear().toUpperCase() + "-" + "2");
					/*
					 * CFECID
					 */
					setContent(content, TIMES_ROMAN, 14, 425, 395, "CFECID");
					drawLine(content, "CFECID", 1, 425, 395, -2, 14, TIMES_ROMAN);
					/*
					 * CFECID content
					 */
					setContent(content, TIMES_ROMAN, 12, 432, 373, permit.getId().getCfecid().toUpperCase());
					/*
					 * Birth Year
					 */
					setContent(content, TIMES_ROMAN, 14, 500, 395, "Birth Year");
					drawLine(content, "Birth Year", 1, 500, 395, -2, 14, TIMES_ROMAN);
					/*
					 * Birth year content
					 */
					setContent(content, TIMES_ROMAN, 12, 515, 373, "1965");
					/*
					 * Fishery Description
					 */
					setContent(content, TIMES_ROMAN, 14, 130, 330, "Fishery Description");
					drawLine(content, "Fishery Description", 1, 130, 330, -2, 14, TIMES_ROMAN);
					/*
					 * Fishery Description content
					 */
					setContent(content, TIMES_ROMAN, 12, 75, 310, permit.getDescription().toUpperCase());
					/*
					 * Fishery Fee
					 */
					setContent(content, TIMES_ROMAN, 14, 430, 330, "Fishery Fee");
					drawLine(content, "Fishery Fee", 1, 430, 330, -2, 14, TIMES_ROMAN);
					/*
					 * Fishery Fee content
					 */
					setContent(content, TIMES_ROMAN, 12, 445, 310, "$"+permit.getFee()+".00");
					/*
					 * First fold marks
					 */
					drawLine(content, "--", 1, 10, 255, -2, 14, TIMES_ROMAN);
					drawLine(content, "--", 1, 300, 255, -2, 14, TIMES_ROMAN);
					drawLine(content, "--", 1, 595, 255, -2, 14, TIMES_ROMAN);
					
					/*
					 * Bar Code bottom
					 */
					try {
						generateQRCodeImageBig(pcert, pg, encodeLine, 210, 26);
					} catch (WriterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					/*setContent(content, TIMES_ROMAN, 10, 415, 180, "This is some text for border protection");
					setContent(content, TIMES_ROMAN, 10, 415, 170, "We can do this to the bottom of QR");
					setContent(content, TIMES_ROMAN, 10, 415, 160, "Hard line breaks are finite, not dynamic.");
					setContent(content, TIMES_ROMAN, 10, 415, 140, "It fairly represents what capacity we have");
					setContent(content, TIMES_ROMAN, 10, 415, 110, "Line spacing is entire dictated, as is font");
					setContent(content, TIMES_ROMAN, 8, 415, 90, "Can also change the font size for more text");
					setContent(content, TIMES_ROMAN, 10, 415, 70, "Here is the likely bottom of this paragraph");
					
					setContent(content, TIMES_ROMAN, 10, 35, 180, "Bullet point number 1 with arrow image");
					setContent(content, TIMES_ROMAN, 10, 35, 165, "Bullet point number 2 with arrow image");
					setContent(content, TIMES_ROMAN, 10, 35, 150, "Bullet point number 3 with arrow image");
					setContent(content, TIMES_ROMAN, 10, 35, 135, "Bullet point number 4 with arrow image");
					setContent(content, TIMES_ROMAN, 10, 35, 110, "Line spacing is entire dictated, as is font");
					setContent(content, PDType1Font.TIMES_BOLD, 8, 35, 90, "Can also change the font texture, README");
					setContent(content, TIMES_ROMAN, 10, 35, 70, "Bullet point number 5 with arrow image"); */
					
					/*
					 * Bar Code
					 */
					//setContent(content, TIMES_ROMAN, 14, 240, 320, "E-Landings Bar Code");
					//drawLine(content, "E-Landings Bar Code", 1, 240, 320, -2, 14, TIMES_ROMAN);
					/*
					 * Fishery Description content
					 */
					//try {
					//	generateQRCodeImage(pcert, pg, encodeLine, 250, 210);
					//} catch (WriterException e) {
						// TODO Auto-generated catch block
					//	e.printStackTrace();
					//}
					//addBarcodePdf417(pcert, pg, "60276S~DCC~~EMB~N BRISTOL DRIFTER           SALMON DRIFT GILLNET BRISTOL SAWYER JUSTIN T     S03T 60276S 37415   2001Z 508039    55  ~ENC~%BS03T  60276S^SAWYER JUSTIN T             ^2001ZBRISTOL DRIFTER           ?L;60276200150803937415?L~END~@@@@@@", 200, 260);
					//String test = "60276S~DCC~~EMB~N BRISTOL DRIFTER           \"SALMON DRIFT GILLNET BRISTOL \"SAWYER JUSTIN T     \"S03T 60276S 37415   \"2001Z 508039    55  \"~ENC~%BS03T  60276S^SAWYER JUSTIN T             ^2001ZBRISTOL DRIFTER           ?L;60276200150803937415?L~END~@@@@@@\"";
					//setContent(content, TIMES_ROMAN, 12, 245, 300, "Barcode will go here");
					
					/*
					 *  Close the content stream
					 */
					content.close();
				}
			}
		}
		System.out.println(encodeLine);
		System.out.println("found value is " + found);
		if (found) {
			/*
			 * save file to either a test environment or a production environment
			 */
			if (pcert != null) {
				if (mode.equalsIgnoreCase("Test")) {				
					//vreceipt.save("/home/mcmity/scratch/pdf/vesreceipt/" + cnum + ".pdf");		
					pcert.save(destfile + "/" + cnum + "-PC.pdf");
				} else {
					pcert.save(destfile + "/" + cnum + "-PC.pdf");
				}
			}
			/*
			 * close PDF document
			 */
			pcert.close();
		}
	}
	
	private static void setContent(PDPageContentStream content, PDType1Font font, int size, int x, int y, String title) throws IOException {
		content.beginText();
		content.setFont(font, size);
		content.newLineAtOffset(x, y);
		content.showText(title);
		content.endText();			
	}

	/*
	 * PDFBox unbelievably does not have a font choice for underline, so this
	 * method takes the geometric data from a particular piece of text and
	 * places a line directly underneath it by 2 pixels.
	 */
	public static void drawLine(PDPageContentStream contentStream, String text, float lineWidth, 
			float sx, float sy, float linePosition, float fontSize, PDFont font)
			throws IOException {
		/*
		 *  Calculate String width
		 */
		float stringWidth = fontSize * font.getStringWidth(text) / 1000;
		float lineEndPoint = sx + stringWidth;
		/*
		 *  begin to draw our line
		 */
		contentStream.setLineWidth(lineWidth);
		contentStream.moveTo(sx, sy + linePosition);
		contentStream.lineTo(lineEndPoint, sy + linePosition);
		contentStream.stroke();
	}
	
	public static void addBarcode39(PDDocument document, PDPage page, String text, float x, float y) {
		try {
			PDPageContentStream contentStream = new PDPageContentStream(
					document, page, PDPageContentStream.AppendMode.APPEND, true);
			int dpi = 300;
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi,BufferedImage.TYPE_BYTE_BINARY, false, 0);
			Code39Bean code39Bean = new Code39Bean();
			code39Bean.generateBarcode(canvas, text.trim());
			canvas.finish();
			BufferedImage bImage = canvas.getBufferedImage();
			PDImageXObject image = JPEGFactory.createFromImage(document, bImage);
			contentStream.drawImage(image, x, y, 200, 50);
			contentStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void addBarcodePdf417(PDDocument document, PDPage page, String text, float x, float y) {
		try {
			PDPageContentStream contentStream = new PDPageContentStream(
					document, page, PDPageContentStream.AppendMode.APPEND, true);
			int dpi = 300;
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi,BufferedImage.TYPE_BYTE_BINARY, false, 0);
			PDF417Bean pdf417Bean = new PDF417Bean();
			pdf417Bean.generateBarcode(canvas, text);
			canvas.finish();
			BufferedImage bImage = canvas.getBufferedImage();
			PDImageXObject image = JPEGFactory.createFromImage(document, bImage);
			contentStream.drawImage(image, x, y, 200, 50);
			contentStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	static void generateQRCodeImage(PDDocument document, PDPage page, String text, float x, float y)
            throws WriterException, IOException {
		PDPageContentStream contentStream = new PDPageContentStream(
				  document, page, PDPageContentStream.AppendMode.APPEND, true);
        //QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int a = Math.round(x);
        int b = Math.round(y);
        //BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, a, b);
        BitMatrix matrix = new MultiFormatWriter().encode(
        	     new String(text.getBytes("UTF-8"), "UTF-8"),
        	     BarcodeFormat.QR_CODE, 100, 100);
        MatrixToImageConfig config = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
        BufferedImage bImage = MatrixToImageWriter.toBufferedImage(matrix, config);
        PDImageXObject image = JPEGFactory.createFromImage(document, bImage);
        //Path path = FileSystems.getDefault().getPath(filePath);
        //MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);        
        contentStream.drawImage(image, a, b, 115, 115);
		contentStream.close();
    }
	
	static void generateQRCodeImageBig(PDDocument document, PDPage page, String text, float x, float y)
            throws WriterException, IOException {
		PDPageContentStream contentStream = new PDPageContentStream(
				  document, page, PDPageContentStream.AppendMode.APPEND, true);
        //QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int a = Math.round(x);
        int b = Math.round(y);
        //BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, a, b);
        BitMatrix matrix = new MultiFormatWriter().encode(
        	     new String(text.getBytes("UTF-8"), "UTF-8"),
        	     BarcodeFormat.QR_CODE, 100, 100);
        MatrixToImageConfig config = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
        BufferedImage bImage = MatrixToImageWriter.toBufferedImage(matrix, config);
        PDImageXObject image = JPEGFactory.createFromImage(document, bImage);
        //Path path = FileSystems.getDefault().getPath(filePath);
        //MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);        
        contentStream.drawImage(image, a, b, 205, 205);
		contentStream.close();
    }
	
	
	/*private static void addQRCode(PDDocument document, PDPage page, String text, float x, float y) {		 
	  try {	 
		  PDPageContentStream contentStream = new PDPageContentStream(
				  document, page, PDPageContentStream.AppendMode.APPEND, true);
		  Map<encodehinttype, object=""> hintMap = new HashMap<encodehinttype, object="">();
		  hintMap.put(EncodeHintType.MARGIN, 0);
		  hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		  	
		  BitMatrix matrix = new MultiFormatWriter().encode(
				  new String(text.getBytes("UTF-8"), "UTF-8"),
				  BarcodeFormat.QR_CODE, 100, 100, hintMap);
	 
		  MatrixToImageConfig config = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
		  BufferedImage bImage = MatrixToImageWriter.toBufferedImage(matrix, config);
		  PDImageXObject image = JPEGFactory.createFromImage(document, bImage);
		  contentStream.drawImage(image, x, y, 75, 75);
		  contentStream.close();	 
	  } catch (Exception e) {
		  e.printStackTrace();
	  }	 
	  
    }*/

}

