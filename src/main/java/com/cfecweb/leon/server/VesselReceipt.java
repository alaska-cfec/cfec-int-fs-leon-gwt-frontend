package com.cfecweb.leon.server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cfecweb.leon.config.HibernateSessionFactoryProvider;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.krysalis.barcode4j.impl.pdf417.PDF417Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import com.cfecweb.leon.shared.ArenewEntity;
import com.cfecweb.leon.shared.ArenewEntityId;
import com.cfecweb.leon.shared.ArenewVessels;

import static com.cfecweb.leon.server.PDType1Fonts.TIMES_ROMAN;

public class VesselReceipt {
	static PDDocument vreceipt = null;
	static PDPage pg = null;
	static PDPageContentStream content = null;
	static boolean found = false;
	static Session session = null;
	static final SessionFactory fact = HibernateSessionFactoryProvider.getSessionFactory();

	/*public static void main(String[] args) {
		DrenewEntityId entId = new DrenewEntityId();
		entId.setCfecid("AAAAAA");
		entId.setRyear("2020");
		session = fact.openSession();
		DrenewEntity entity = (DrenewEntity) session.get(DrenewEntity.class, entId);
			try {
				getVesReceipt("0220-69176-AAAAAA", "/home/mcmity/scratch/pdf", "AAAAAA", "2020", entity, "test");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public VesselReceipt() {
		GwtConfigurationHelper.initGwtStatelessBeanManager(new HibernateUtil(fact));
	}*/
	
	public static void main(String[] args) {
		ArenewEntityId entId = new ArenewEntityId();
		entId.setCfecid("508039");
		entId.setRyear("2020");
		session = fact.openSession();
		ArenewEntity entity = (ArenewEntity) session.get(ArenewEntity.class, entId);
		List<ArenewVessels> vessels = new ArrayList<ArenewVessels>();
		vessels.addAll(entity.getArenewVesselses());
		getVesReceipt2("0120-94956-508039", "/home/mcmity/scratch/pdf/vesreceipt", "508039", "2020", vessels, "test", entity);
	}
	
	public static void getVesReceipt2(String cnum, String vesreceptOut, String id, String year, List<ArenewVessels> vrenewlist, 
			String env, ArenewEntity entity) {
		vreceipt = null;
		pg = null;
		content = null;
		found = false;
		try {
			for (Iterator<ArenewVessels> dv = vrenewlist.iterator(); dv.hasNext();) {
				ArenewVessels vessel = (ArenewVessels) dv.next();
				if (vessel.getId().getAdfg().equalsIgnoreCase("N/A") || (vessel.getId().getAdfg().substring(0,4).equalsIgnoreCase("Temp"))) {
					// new vessel, not processing vessel receipt yet
					System.out.println(entity.getId().getCfecid() + " This vessel is new, no need for a vessel receipt - " + cnum);
				} else {
					//System.out.println(vessel.getArenewPayment().getConfirmcode());
					if (vessel.getArenewPayment().getConfirmcode().equalsIgnoreCase(cnum)) {
						found = true;
						vreceipt = Loader.loadPDF(new File("/home/mcmity/scratch/pdf/forms/ves2020_3.pdf"));
						pg = vreceipt.getDocumentCatalog().getPages().get(0);
						content = new PDPageContentStream(vreceipt, pg,  PDPageContentStream.AppendMode.APPEND, true, true);
						//PDFont font = PDType1Font.HELVETICA_BOLD;
						/*
						 *  Vessel Owner Header					 
						 */
						setContent(content, TIMES_ROMAN, 14, 60, 620, "Vessel Owner");
						drawLine(content, "Vessel Owner", 1, 60, 620, -2, 14, TIMES_ROMAN);
						/*
						 *  Address Header	
						 */
						setContent(content, TIMES_ROMAN, 14, 325, 620, "Permanent Mailing Address (if different)");
						drawLine(content, "Permanent Mailing Address (if different)", 1, 325, 620, -2, 14, TIMES_ROMAN);
						/*
						 *  Vessel Owner Content	
						 */
						//System.out.println("VesselReceipt BADDRESS content is " + vessel.getDrenewPayment().getBaddress());
						if (vessel.getArenewPayment().getBaddress().equalsIgnoreCase("N/A")) {
							content.beginText();
							content.setFont(TIMES_ROMAN, 12);
							content.setLeading(14.5f);
							content.newLineAtOffset(60, 601);
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
							content.newLineAtOffset(60, 601);
							content.showText(entity.getXname().toUpperCase());
							content.newLine();
							content.showText(vessel.getArenewPayment().getBaddress().toUpperCase());
							content.newLine();
							content.showText(vessel.getArenewPayment().getBcity().toUpperCase() + "              " + 
									vessel.getArenewPayment().getBstate().toUpperCase()	+ "  " + vessel.getArenewPayment().getBzip().toUpperCase());
							content.endText();
							content.beginText();
							content.setFont(TIMES_ROMAN, 12);
							content.setLeading(14.5f);
							content.newLineAtOffset(325, 601);
							content.showText(entity.getXname().toUpperCase());
							content.newLine();
							content.showText(entity.getPaddress().toUpperCase());
							content.newLine();
							content.showText(entity.getPcity().toUpperCase() + "              " + 
									entity.getPstate().toUpperCase()	+ "  " + entity.getPzip().toUpperCase());
							content.endText();
						}
						/*
						 *  Vessel Description Header					 
						 */
						//System.out.println(vrenewlist + " has " + vrenewlist.size() + " vessels");
						setContent(content, TIMES_ROMAN, 14, 60, 510, "Vessel Description");
						drawLine(content, "Vessel Description", 1, 60, 510, -2, 14, TIMES_ROMAN);
						/*
						 *  Vessel Description Content					 
						 */
						/*
						 *  ADFG number sub header and content
						 */
						setContent(content, TIMES_ROMAN, 12, 60, 486, vessel.getId().getAdfg().toUpperCase());
						setContent(content, TIMES_ROMAN, 8, 60, 474, "ADFG No.");
						/*
						 *  vessel name sub header and content
						 */
						//System.out.println("ves name " + vessel.getName().toUpperCase());
						String vesname = "N/A";
						if (vessel.getName().equalsIgnoreCase("N/A") || vessel.getName().equals(null)) {
							vesname = "N/A";
						} else {
							vesname = vessel.getName().toUpperCase();
						}
						setContent(content, TIMES_ROMAN, 12, 115, 486, vesname);
						setContent(content, TIMES_ROMAN, 8, 115, 474, "Vessel Name");
						/*
						 *  USCG or DOC sub header and content
						 */
						//System.out.println("ves reg# is " + vessel.getRegNum().toUpperCase());
						String vesreg = "N/A";
						if (vessel.getRegNum().equalsIgnoreCase("N/A") || vessel.getRegNum().equals(null)) {
							vesreg = "N/A";
						} else {
							vesreg = vessel.getRegNum().toUpperCase();
						}
						setContent(content, TIMES_ROMAN, 12, 264, 486, vesreg);
						setContent(content, TIMES_ROMAN, 8, 258, 474, "USCG Re. or Doc. No.");
						/*
						 *  overall length header and content
						 */
						//System.out.println("ves overall length " + vessel.getLength().toUpperCase());
						setContent(content, TIMES_ROMAN, 12, 384, 486, vessel.getLength().toUpperCase());
						setContent(content, TIMES_ROMAN, 8, 364, 474, "Overall Length");
						/*
						 *  hull id sub header and content
						 */
						//System.out.println("ves hullid " + vessel.getHullId());
						String veshid = "N/A";
						if (vessel.getHullId().equalsIgnoreCase("N/A") || vessel.getHullId().equals(null)) {
							veshid = "N/A";
						} else {
							veshid = vessel.getHullId().toUpperCase();
						}
						setContent(content, TIMES_ROMAN, 12, 440, 486, veshid);
						setContent(content, TIMES_ROMAN, 8, 440, 474, "Hull ID Number");
						/*
						 *  hull contruction sub header and content
						 */
						//System.out.println("ves hull type " + vessel.getHullType().toLowerCase());
						String vesht = "N/A";
						if (vessel.getHullType().equalsIgnoreCase("A")) {
							vesht = "ALUMINUM";
						} else if (vessel.getHullType().equalsIgnoreCase("F")) {
							vesht = "FIBERGLASS/PLASTIC";
						} else if (vessel.getHullType().equalsIgnoreCase("K")) {
							vesht = "Concrete";
						} else if (vessel.getHullType().equalsIgnoreCase("S")) {
							vesht = "IRON/STEEL/ALLOY";
						} else if (vessel.getHullType().equalsIgnoreCase("R")) {
							vesht = "RUBBER";
						} else if (vessel.getHullType().equalsIgnoreCase("W")) {
							vesht = "WOOD";
						} else {
							vesht = "N/A";
						}
						setContent(content, TIMES_ROMAN, 12, 60, 438, vesht);
						setContent(content, TIMES_ROMAN, 8, 60, 426, "Hull Construction");
						/*
						 *  gross tons sub header and content
						 */
						setContent(content, TIMES_ROMAN, 12, 202, 438, vessel.getGrossTons().toUpperCase());
						setContent(content, TIMES_ROMAN, 8, 187, 426, "Gross Tons");
						/*
						 *  net tons sub header and content
						 */
						setContent(content, TIMES_ROMAN, 12, 272, 438, vessel.getNetTons().toUpperCase());
						setContent(content, TIMES_ROMAN, 8, 260, 426, "Net Tons");
						/*
						 *  make/model sub header and content
						 */
						//System.out.print("ves make " + vessel.getMakeModel().toUpperCase());
						String vesmake = "N/A"; 
						if (vessel.getMakeModel().equalsIgnoreCase("N/A") || vessel.getMakeModel().equals(null)) {
							vesmake = "N/A";
						} else {
							vesmake = vessel.getMakeModel().toUpperCase();
						}
						setContent(content, TIMES_ROMAN, 12, 330, 438, vesmake);
						setContent(content, TIMES_ROMAN, 8, 366, 426, "Make / Model");
						/*
						 *  year build sub header and content
						 */
						//System.out.print("ves year " + vessel.getYearBuilt().toUpperCase());
						String vesyear = "N/A";
						if (vessel.getMakeModel().equalsIgnoreCase("N/A") || vessel.getMakeModel().equals(null)) {
							vesyear = "N/A";
						} else {
							vesyear = vessel.getYearBuilt().toUpperCase();
						}
						setContent(content, TIMES_ROMAN, 12, 516, 438, vesyear);
						setContent(content, TIMES_ROMAN, 8, 512, 426, "Year Built");
						/*
						 *  Vessel Activities Header					 
						 */
						setContent(content, TIMES_ROMAN, 14, 60, 388, "Vessel Activities");
						drawLine(content, "Vessel Activities", 1, 60, 388, -2, 14, TIMES_ROMAN);
						/*
						 *  salmon troll reg sub header and content
						 */
						//System.out.println("troll reg " + vessel.getSalmontrollReg());
						String trollreg = "N/A";
						if (vessel.getSalmontrollReg().equalsIgnoreCase("N/A") || vessel.getSalmontrollReg().equals(null)) {
							trollreg = "N/A";
						}
						setContent(content, TIMES_ROMAN, 12, 60, 353, trollreg.toUpperCase());
						setContent(content, TIMES_ROMAN, 8, 60, 341, "Salmon Troll Registration");
						/*
						 *  effective date sub header and content
						 */
						//System.out.println("effective date " + vessel.getSalmontrollDate());
						String trolldate = "N/A";
						if (vessel.getSalmontrollDate() == null) {
							trolldate = "N/A";
						}
						setContent(content, TIMES_ROMAN, 12, 187, 353, trolldate.toUpperCase());
						setContent(content, TIMES_ROMAN, 8, 187, 341, "Effective Date");
						/*
						 *  salmon net area sub header and content
						 */
						//System.out.println("net area " + vessel.getSalmonregArea());
						setContent(content, TIMES_ROMAN, 12, 80, 305, vessel.getSalmonregArea());
						setContent(content, TIMES_ROMAN, 8, 60, 293, "Salmon Net Area");
						/*
						 * Bar Code
						 */
						setContent(content, TIMES_ROMAN, 12, 435, 353, "Bar Code");
						drawLine(content, "Bar Code", 1, 435, 353, -2, 14, TIMES_ROMAN);
						/*
						 * Fishery Description content
						 */
						addBarcodePdf417(vreceipt, pg, "This vessel is legal for 2020", 360, 290);						
						/*
						 *  salmon net permit(s) sub header and content
						 */
						//System.out.println("net permit(s) " + vessel.getPermitSerial1() + " and " + vessel.getPermitSerial2());
						String netarea = null;
						if (vessel.getPermitSerial1().equalsIgnoreCase("N/A") && vessel.getPermitSerial2().equalsIgnoreCase("N/A")) {
							//System.out.println("both N/A");
							netarea = "N/A";
						} else if (vessel.getPermitSerial1().equalsIgnoreCase("N/A") && !(vessel.getPermitSerial2().equalsIgnoreCase("N/A"))) {
							netarea = vessel.getPermitSerial2();
						} else if (!vessel.getPermitSerial1().equalsIgnoreCase("N/A") && vessel.getPermitSerial2().equalsIgnoreCase("N/A")) {
							netarea = vessel.getPermitSerial1();
						} else {
							netarea = vessel.getPermitSerial1() + " / " + vessel.getPermitSerial2();
						}
						setContent(content, TIMES_ROMAN, 12, 187, 305, netarea.toUpperCase());
						setContent(content, TIMES_ROMAN, 8, 187, 293, "Salmon Net Permit(s)");
						/*
						 *  fishing activity sub header and content
						 */
						//System.out.println("ves activities " + vessel.getFishingboat() + " and " + vessel.getFreezerCanner() +
						//		" and " + vessel.getTenderPacker() + " and " + vessel.getTransporter());
						StringBuffer vesact = new StringBuffer("");
						if (vessel.getFishingboat().equalsIgnoreCase("X")) {
							vesact = vesact.append("FISHING ");
						}
						if (vessel.getFreezerCanner().equalsIgnoreCase("X")) {
							vesact = vesact.append("FREEZER/CANNER ");
						}
						if (vessel.getTenderPacker().equalsIgnoreCase("X")) {
							vesact = vesact.append("TENDER ");
						}
						if (vessel.getTransporter().equalsIgnoreCase("X")) {
							vesact = vesact.append("TRANSPORTER ");
						}
						setContent(content, TIMES_ROMAN, 12, 60, 250, vesact.toString());
						setContent(content, TIMES_ROMAN, 8, 60, 238, "Type(s) of Vessel Activity");
						/*
						 *  issuance date sub header and content
						 */
						//System.out.println("Pass in current date for this field, dont know what DH means");
						setContent(content, TIMES_ROMAN, 12, 60, 212, "2019-11-25  LN");
						setContent(content, TIMES_ROMAN, 8, 60, 200, "Date of Issuance");
						/*
						 *  amount paid sub header and content
						 */
						setContent(content, TIMES_ROMAN, 12, 207, 212, "$"+vessel.getFee());
						setContent(content, TIMES_ROMAN, 8, 197, 200, "Amount Paid");
						/*
						 *  signature line sub header and content
						 */
						setContent(content, TIMES_ROMAN, 12, 300, 212, "__________________________________________");
						setContent(content, TIMES_ROMAN, 8, 300, 200, "Signature of Vessel Owner or Authorized Agent (circle whichever applies");
						/*
						 *  form name in lower left					 
						 */
						setContent(content, TIMES_ROMAN, 6, 30, 27, "VESRECPT");
						/*
						 *  Close the content stream
						 */
						content.close();
						//vreceipt.save(vesreceptOut + "/" + cnum + ".pdf");		
						//vreceipt.close();
					}
				}
			}
			if (found) {
				/*
				 * save file to either a test environment or a production environment
				 */
				if (vreceipt != null) {
					if (env.equalsIgnoreCase("Test")) {				
						//vreceipt.save("/home/mcmity/scratch/pdf/vesreceipt/" + cnum + ".pdf");		
						vreceipt.save(vesreceptOut + "/" + cnum + "-VR.pdf");
					} else {
						vreceipt.save(vesreceptOut + "/" + cnum + "-VR.pdf");
					}
				}
				/*
				 * close PDF document
				 */
				vreceipt.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			session.close();
			fact.close();
		}
	}

	
	public boolean getVesReceipt(String cnum, String vesreceptOut, String id, String year, List<ArenewVessels> vrenewlist, 
			String env, ArenewEntity entity) throws IOException {
		vreceipt = null;
		pg = null;
		content = null;
		found = false;
		for (Iterator<ArenewVessels> dv = vrenewlist.iterator(); dv.hasNext();) {
			ArenewVessels vessel = (ArenewVessels) dv.next();
			//System.out.println("vesselConfirm " + vessel.getConfirmcode());
			//System.out.println("systemConfirm " + cnum);
			if (vessel.getId().getAdfg().equalsIgnoreCase("N/A") || (vessel.getId().getAdfg().substring(0,4).equalsIgnoreCase("Temp"))) {
				// new vessel, not processing vessel receipt yet
				System.out.println(entity.getId().getCfecid() + " This vessel is new, no need for a vessel receipt - " + cnum);
			} else {
				if (vessel.getConfirmcode().equalsIgnoreCase(cnum)) {
					if (vreceipt != null) {
						//vreceipt = new PDDocument();
					} else {
						vreceipt = new PDDocument();
					}
					found = true;
					pg = new PDPage();
					vreceipt.addPage(pg);
					content = new PDPageContentStream(vreceipt, pg);
					/*
					 *  Vessel Owner Header					 
					 */
					setContent(content, TIMES_ROMAN, 14, 60, 629, "Vessel Owner");
					drawLine(content, "Vessel Owner", 1, 60, 629, -2, 14, TIMES_ROMAN);
					/*
					 *  Address Header	
					 */
					setContent(content, TIMES_ROMAN, 14, 325, 629, "Permanent Mailing Address (if different)");
					drawLine(content, "Permanent Mailing Address (if different)", 1, 325, 629, -2, 14, TIMES_ROMAN);
					/*
					 *  Vessel Owner Content	
					 */
					//System.out.println("VesselReceipt BADDRESS content is " + vessel.getDrenewPayment().getBaddress());
					if (vessel.getArenewPayment().getBaddress().equalsIgnoreCase("N/A")) {
						content.beginText();
						content.setFont(TIMES_ROMAN, 12);
						content.setLeading(14.5f);
						content.newLineAtOffset(60, 610);
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
						content.newLineAtOffset(60, 610);
						content.showText(entity.getXname().toUpperCase());
						content.newLine();
						content.showText(vessel.getArenewPayment().getBaddress().toUpperCase());
						content.newLine();
						content.showText(vessel.getArenewPayment().getBcity().toUpperCase() + "              " + 
								vessel.getArenewPayment().getBstate().toUpperCase()	+ "  " + vessel.getArenewPayment().getBzip().toUpperCase());
						content.endText();
						content.beginText();
						content.setFont(TIMES_ROMAN, 12);
						content.setLeading(14.5f);
						content.newLineAtOffset(325, 610);
						content.showText(entity.getXname().toUpperCase());
						content.newLine();
						content.showText(entity.getPaddress().toUpperCase());
						content.newLine();
						content.showText(entity.getPcity().toUpperCase() + "              " + 
								entity.getPstate().toUpperCase()	+ "  " + entity.getPzip().toUpperCase());
						content.endText();
					}
					/*
					 *  Vessel Description Header					 
					 */
					//System.out.println(vrenewlist + " has " + vrenewlist.size() + " vessels");
					setContent(content, TIMES_ROMAN, 14, 60, 510, "Vessel Description");
					drawLine(content, "Vessel Description", 1, 60, 510, -2, 14, TIMES_ROMAN);
					/*
					 *  Vessel Description Content					 
					 */
					/*
					 *  ADFG number sub header and content
					 */
					setContent(content, TIMES_ROMAN, 12, 60, 486, vessel.getId().getAdfg().toUpperCase());
					setContent(content, TIMES_ROMAN, 8, 60, 474, "ADFG No.");
					/*
					 *  vessel name sub header and content
					 */
					//System.out.println("ves name " + vessel.getName().toUpperCase());
					String vesname = "N/A";
					if (vessel.getName().equalsIgnoreCase("N/A") || vessel.getName().equals(null)) {
						vesname = "N/A";
					} else {
						vesname = vessel.getName().toUpperCase();
					}
					setContent(content, TIMES_ROMAN, 12, 115, 486, vesname);
					setContent(content, TIMES_ROMAN, 8, 115, 474, "Vessel Name");
					/*
					 *  USCG or DOC sub header and content
					 */
					//System.out.println("ves reg# is " + vessel.getRegNum().toUpperCase());
					String vesreg = "N/A";
					if (vessel.getRegNum().equalsIgnoreCase("N/A") || vessel.getRegNum().equals(null)) {
						vesreg = "N/A";
					} else {
						vesreg = vessel.getRegNum().toUpperCase();
					}
					setContent(content, TIMES_ROMAN, 12, 264, 486, vesreg);
					setContent(content, TIMES_ROMAN, 8, 258, 474, "USCG Re. or Doc. No.");
					/*
					 *  overall length header and content
					 */
					//System.out.println("ves overall length " + vessel.getLength().toUpperCase());
					setContent(content, TIMES_ROMAN, 12, 384, 486, vessel.getLength().toUpperCase());
					setContent(content, TIMES_ROMAN, 8, 364, 474, "Overall Length");
					/*
					 *  hull id sub header and content
					 */
					//System.out.println("ves hullid " + vessel.getHullId());
					String veshid = "N/A";
					if (vessel.getHullId().equalsIgnoreCase("N/A") || vessel.getHullId().equals(null)) {
						veshid = "N/A";
					} else {
						veshid = vessel.getHullId().toUpperCase();
					}
					setContent(content, TIMES_ROMAN, 12, 440, 486, veshid);
					setContent(content, TIMES_ROMAN, 8, 440, 474, "Hull ID Number");
					/*
					 *  hull contruction sub header and content
					 */
					//System.out.println("ves hull type " + vessel.getHullType().toLowerCase());
					String vesht = "N/A";
					if (vessel.getHullType().equalsIgnoreCase("A")) {
						vesht = "ALUMINUM";
					} else if (vessel.getHullType().equalsIgnoreCase("F")) {
						vesht = "FIBERGLASS/PLASTIC";
					} else if (vessel.getHullType().equalsIgnoreCase("K")) {
						vesht = "Concrete";
					} else if (vessel.getHullType().equalsIgnoreCase("S")) {
						vesht = "IRON/STEEL/ALLOY";
					} else if (vessel.getHullType().equalsIgnoreCase("R")) {
						vesht = "RUBBER";
					} else if (vessel.getHullType().equalsIgnoreCase("W")) {
						vesht = "WOOD";
					} else {
						vesht = "N/A";
					}
					setContent(content, TIMES_ROMAN, 12, 60, 438, vesht);
					setContent(content, TIMES_ROMAN, 8, 60, 426, "Hull Construction");
					/*
					 *  gross tons sub header and content
					 */
					setContent(content, TIMES_ROMAN, 12, 202, 438, vessel.getGrossTons().toUpperCase());
					setContent(content, TIMES_ROMAN, 8, 187, 426, "Gross Tons");
					/*
					 *  net tons sub header and content
					 */
					setContent(content, TIMES_ROMAN, 12, 272, 438, vessel.getNetTons().toUpperCase());
					setContent(content, TIMES_ROMAN, 8, 260, 426, "Net Tons");
					/*
					 *  make/model sub header and content
					 */
					//System.out.print("ves make " + vessel.getMakeModel().toUpperCase());
					String vesmake = "N/A"; 
					if (vessel.getMakeModel().equalsIgnoreCase("N/A") || vessel.getMakeModel().equals(null)) {
						vesmake = "N/A";
					} else {
						vesmake = vessel.getMakeModel().toUpperCase();
					}
					setContent(content, TIMES_ROMAN, 12, 330, 438, vesmake);
					setContent(content, TIMES_ROMAN, 8, 366, 426, "Make / Model");
					/*
					 *  year build sub header and content
					 */
					//System.out.print("ves year " + vessel.getYearBuilt().toUpperCase());
					String vesyear = "N/A";
					if (vessel.getMakeModel().equalsIgnoreCase("N/A") || vessel.getMakeModel().equals(null)) {
						vesyear = "N/A";
					} else {
						vesyear = vessel.getYearBuilt().toUpperCase();
					}
					setContent(content, TIMES_ROMAN, 12, 516, 438, vesyear);
					setContent(content, TIMES_ROMAN, 8, 512, 426, "Year Built");
					/*
					 *  Vessel Activities Header					 
					 */
					setContent(content, TIMES_ROMAN, 14, 60, 388, "Vessel Activities");
					drawLine(content, "Vessel Activities", 1, 60, 388, -2, 14, TIMES_ROMAN);
					/*
					 *  salmon troll reg sub header and content
					 */
					//System.out.println("troll reg " + vessel.getSalmontrollReg());
					String trollreg = "N/A";
					if (vessel.getSalmontrollReg().equalsIgnoreCase("N/A") || vessel.getSalmontrollReg().equals(null)) {
						trollreg = "N/A";
					}
					setContent(content, TIMES_ROMAN, 12, 60, 353, trollreg.toUpperCase());
					setContent(content, TIMES_ROMAN, 8, 60, 341, "Salmon Troll Registration");
					/*
					 *  effective date sub header and content
					 */
					//System.out.println("effective date " + vessel.getSalmontrollDate());
					String trolldate = "N/A";
					if (vessel.getSalmontrollDate() == null) {
						trolldate = "N/A";
					}
					setContent(content, TIMES_ROMAN, 12, 187, 353, trolldate.toUpperCase());
					setContent(content, TIMES_ROMAN, 8, 187, 341, "Effective Date");
					/*
					 *  salmon net area sub header and content
					 */
					//System.out.println("net area " + vessel.getSalmonregArea());
					setContent(content, TIMES_ROMAN, 12, 80, 305, vessel.getSalmonregArea());
					setContent(content, TIMES_ROMAN, 8, 60, 293, "Salmon Net Area");
					/*
					 *  salmon net permit(s) sub header and content
					 */
					//System.out.println("net permit(s) " + vessel.getPermitSerial1() + " and " + vessel.getPermitSerial2());
					String netarea = null;
					if (vessel.getPermitSerial1().equalsIgnoreCase("N/A") && vessel.getPermitSerial2().equalsIgnoreCase("N/A")) {
						//System.out.println("both N/A");
						netarea = "N/A";
					} else if (vessel.getPermitSerial1().equalsIgnoreCase("N/A") && !(vessel.getPermitSerial2().equalsIgnoreCase("N/A"))) {
						netarea = vessel.getPermitSerial2();
					} else if (!vessel.getPermitSerial1().equalsIgnoreCase("N/A") && vessel.getPermitSerial2().equalsIgnoreCase("N/A")) {
						netarea = vessel.getPermitSerial1();
					} else {
						netarea = vessel.getPermitSerial1() + " / " + vessel.getPermitSerial2();
					}
					setContent(content, TIMES_ROMAN, 12, 187, 305, netarea.toUpperCase());
					setContent(content, TIMES_ROMAN, 8, 187, 293, "Salmon Net Permit(s)");
					/*
					 *  fishing activity sub header and content
					 */
					//System.out.println("ves activities " + vessel.getFishingboat() + " and " + vessel.getFreezerCanner() +
					//		" and " + vessel.getTenderPacker() + " and " + vessel.getTransporter());
					StringBuffer vesact = new StringBuffer("");
					if (vessel.getFishingboat().equalsIgnoreCase("X")) {
						vesact = vesact.append("FISHING ");
					}
					if (vessel.getFreezerCanner().equalsIgnoreCase("X")) {
						vesact = vesact.append("FREEZER/CANNER ");
					}
					if (vessel.getTenderPacker().equalsIgnoreCase("X")) {
						vesact = vesact.append("TENDER ");
					}
					if (vessel.getTransporter().equalsIgnoreCase("X")) {
						vesact = vesact.append("TRANSPORTER ");
					}
					setContent(content, TIMES_ROMAN, 12, 60, 250, vesact.toString());
					setContent(content, TIMES_ROMAN, 8, 60, 238, "Type(s) of Vessel Activity");
					/*
					 *  issuance date sub header and content
					 */
					//System.out.println("Pass in current date for this field, dont know what DH means");
					setContent(content, TIMES_ROMAN, 12, 60, 212, "2019-11-25  LN");
					setContent(content, TIMES_ROMAN, 8, 60, 200, "Date of Issuance");
					/*
					 *  amount paid sub header and content
					 */
					setContent(content, TIMES_ROMAN, 12, 207, 212, "$"+vessel.getFee());
					setContent(content, TIMES_ROMAN, 8, 197, 200, "Amount Paid");
					/*
					 *  signature line sub header and content
					 */
					setContent(content, TIMES_ROMAN, 12, 300, 212, "__________________________________________");
					setContent(content, TIMES_ROMAN, 8, 300, 200, "Signature of Vessel Owner or Authorized Agent (circle whichever applies");
					/*
					 *  form name in lower left					 
					 */
					setContent(content, TIMES_ROMAN, 6, 30, 27, "VESRECPT");
					/*
					 *  Close the content stream
					 */
					content.close();
				}
			}			
		}
		System.out.println("found value is " + found);
		if (found) {
			/*
			 * save file to either a test environment or a production environment
			 */
			if (vreceipt != null) {
				if (env.equalsIgnoreCase("Test")) {				
					//vreceipt.save("/home/mcmity/scratch/pdf/vesreceipt/" + cnum + ".pdf");		
					vreceipt.save(vesreceptOut + "/" + cnum + ".pdf");
				} else {
					vreceipt.save(vesreceptOut + "/" + cnum + ".pdf");
				}
			}
			/*
			 * close PDF document
			 */
			vreceipt.close();
		}
		return found;
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
	
	private static void addBarcodePdf417(PDDocument document, PDPage page,
			String text, float x, float y) {
		try {
			PDPageContentStream contentStream = new PDPageContentStream(
					document, page, PDPageContentStream.AppendMode.APPEND, true);
			int dpi = 300;
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi,
					BufferedImage.TYPE_BYTE_BINARY, false, 0);
			PDF417Bean pdf417Bean = new PDF417Bean();
			pdf417Bean.generateBarcode(canvas, text.trim());
			canvas.finish();
			BufferedImage bImage = canvas.getBufferedImage();
			PDImageXObject image = JPEGFactory
					.createFromImage(document, bImage);
			contentStream.drawImage(image, x, y, 200, 50);
			contentStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
