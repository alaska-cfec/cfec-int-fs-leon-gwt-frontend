package com.cfecweb.leon.server;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.cfecweb.leon.AppProperties;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.hibernate.Session;
import org.hibernate.Transaction;

//import com.cfecweb.leon.server.LEONencrypt;
//import com.cfecweb.leon.server.Logging;
import com.cfecweb.leon.server.getDataImpl;
import com.cfecweb.leon.shared.ArenewChanges;
import com.cfecweb.leon.shared.ArenewEntity;
import com.cfecweb.leon.shared.ArenewPayment;
import com.cfecweb.leon.shared.ArenewPermits;
import com.cfecweb.leon.shared.ArenewVessels;
import com.cfecweb.leon.shared.GWTChangeMonitor;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import static com.cfecweb.leon.config.HibernateSessionFactoryProvider.LEON_DB_PASSWORD;
import static com.cfecweb.leon.config.HibernateSessionFactoryProvider.LEON_DB_URL;
import static com.cfecweb.leon.config.HibernateSessionFactoryProvider.LEON_DB_USERNAME;
import static com.cfecweb.leon.config.HibernateSessionFactoryProvider.getEnvOrDefault;

public class ShortForm {
    final static String connectionURLThin = getEnvOrDefault(LEON_DB_URL, AppProperties.get(AppProperties.HIBERNATE_CONNECTION_URL));
    final static String userID = getEnvOrDefault(LEON_DB_USERNAME, AppProperties.get(AppProperties.HIBERNATE_CONNECTION_USERNAME));
    final static String userPassword = getEnvOrDefault(LEON_DB_PASSWORD, AppProperties.get(AppProperties.HIBERNATE_CONNECTION_PASSWORD));
	static Connection conn = null;
	ArenewPayment pay = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getShortForm(String ccode, Logging leonLog, getDataImpl getDataImpl, boolean manual, String rjnumber, String cfecid) {
		Session sess = null;
		Transaction tx = null;
		//LEONencrypt le = new LEONencrypt();
		List<ArenewPermits> plist = null;
		List<ArenewVessels> vlist = null; 
		GWTChangeMonitor cmon = null;
		List clist = null; 
		String last = null;
		String first = null;
		String hpounds = null;
		String spounds = null;
		String rdate = null;
		Map params = null;
		String infile = null;
		String outfileStage = null;
		String outfile = null;
		String imagesf = null;
		String id = null;
		String confirm = null;
		String year = null;
		List mlist = null;
		try {
			sess = getDataImpl.fact.openSession();
			mlist = sess.createQuery("from ArenewPayment p where p.id.confirmcode = '"+ccode+"'").list();
			for (Iterator p = mlist.iterator(); p.hasNext();) {
				pay = (ArenewPayment) p.next();
				/*
				 * descrypt the credit card and security numbers first
				 */
				tx = sess.beginTransaction();
				//if (manual) {
				//	if (!(pay.getCcnumber().startsWith("XXXX"))) {
				//		pay.setCcnumber(le.encrypt(pay.getCcnumber()));
				//	}
				//}
				pay = sess.merge(pay);
				tx.commit();
				id = pay.getArenewEntity().getId().getCfecid();
				confirm = pay.getConfirmcode();
				year = pay.getArenewEntity().getId().getRyear();
				plist = sess.createQuery("from ArenewPermits p where p.id.cfecid = '"+id+"' and p.id.ryear = '"+year+"' and p.arenewPayment.confirmcode = '"+confirm+"'").list();
				vlist = sess.createQuery("from ArenewVessels v where v.id.cfecid = '"+id+"' and v.id.ryear = '"+year+"' and v.arenewPayment.confirmcode = '"+confirm+"'").list();
				clist = sess.createQuery("from ArenewChanges c where c.arenewEntity.id.cfecid = '"+id+"' and c.arenewEntity.id.ryear = '"+year+"' and c.id.confirmcode = '"+confirm+"' order by c.type, c.object").list();
				if (clist.size() > 0) {
					cmon = new GWTChangeMonitor();
					for (Iterator it = clist.iterator(); it.hasNext();) {
						ArenewChanges chg = (ArenewChanges) it.next();
						if (chg.getId().getAttribute().equalsIgnoreCase("phoneArea")) { cmon.setPhone(true); }
						if (chg.getId().getAttribute().equalsIgnoreCase("phonePre")) { cmon.setPhone(true); }
						if (chg.getId().getAttribute().equalsIgnoreCase("phonePost")) { cmon.setPhone(true); }
						if (chg.getId().getAttribute().equalsIgnoreCase("email")) { cmon.setEmail(true); }		
						if (chg.getId().getAttribute().equalsIgnoreCase("PermMailingAddress")) { cmon.setPaddress(true); }
						if (chg.getId().getAttribute().equalsIgnoreCase("PermMailingAddress2")) { cmon.setPaddress2(true); }
						if (chg.getId().getAttribute().equalsIgnoreCase("PermMailingCity")) { cmon.setPcity(true); }
						if (chg.getId().getAttribute().equalsIgnoreCase("PermMailingState")) { cmon.setPstate(true); }
						if (chg.getId().getAttribute().equalsIgnoreCase("PermMailingZip")) { cmon.setPzip(true); }
					}
				} else {
					cmon = new GWTChangeMonitor();
				}
				List name = sess.createNativeQuery("select person_last_name, person_first_name from tier2.people_view where file_number = '"+id+"' and name_type is null").list();
				if (name.size() > 0) {
					Object[] rs1 = (Object[]) name.iterator().next();
					last = rs1[0].toString();
					first = rs1[1].toString();
					last = last.trim().toUpperCase();
					StringTokenizer st = new StringTokenizer(first);
					first = st.nextToken().trim().toUpperCase();
					List results = sess.createNativeQuery("select COALESCE(halbs, '0'), COALESCE(sablbs, '0') from tier2.ramrpt where lastname = '"+last+"' and firstname = '"+first+"' and year = '"+((Integer.parseInt(year))-1)+"'").list();
					if (results.size() > 0) {
						Object[] rs2 = (Object[]) results.iterator().next();
						hpounds = rs2[0].toString();
						spounds = rs2[1].toString();
					} else if (results.size() == 0) {
						hpounds = "0";
						spounds = "0";
					}
				} else {
					last = "Invalid";
					first = "Invalid";
					hpounds = "0";
					spounds = "0";
				}
				List list = sess.createNativeQuery("select to_char(p.receiveddate, 'MM/DD/YYYY HH:mi'), p.confirmcode from tier2.arenew_payment p where p.confirmcode = '"+confirm+"'").list();
				if (list.size() > 0) {
					Object[] rs3 = (Object[]) list.iterator().next();
					rdate = rs3[0].toString();
				} else if (list.size() == 0) {
					rdate = "Invalid";
				}
				params = new HashMap();
				params.put("cfecid", id);
				params.put("year", year);
				params.put("cnum", confirm);
				params.put("psize", Integer.toString(plist.size()));
				params.put("vsize", Integer.toString(vlist.size()));
				params.put("rdates", rdate);
				params.put("hpounds", hpounds);
				params.put("spounds", spounds);
				params.put("cmon", cmon);
				params.put("rjnum", rjnumber);
				infile = "/webapps/jrxml/LEON/sformAll.jrxml";
				//outfile = "/webapps/out/LEONDev/"+year+"/shortforms/"+ccode+".pdf";
				outfile = "/webapps/LEON/Prod/ShortFormsPDF/"+ccode+".pdf";
				outfileStage = "/webapps/out/LEON/staging/"+ccode+".pdf";
				conn = DriverManager.getConnection(connectionURLThin, userID, userPassword);
				JasperDesign jasperDesign = JRXmlLoader.load(infile);
		        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
		        JasperExportManager.exportReportToPdfFile(jasperPrint, outfileStage);
		        imagesf = "/webapps/images/imagesf.png";
				/*
				 * Now, re-encrypt the credit card and security numbers
				 */
				//tx = sess.beginTransaction();
				//if (manual) {
				//	if (!(pay.getCcnumber().startsWith("XXXX"))) {
				//		pay.setCcnumber(le.encrypt(pay.getCcnumber()));
				//	}	
				//}
				//sess.update(pay);
				//tx.commit();
			}			
		} catch(Exception e) {
			e.printStackTrace();
			/*
			 * in case of an exception, check to see what state the credit card and security
			 * numbers are in. If they are NOT encrypted (class blew up prior to the code), 
			 * then do so prior to leaving.
			 */
			/*tx = sess.beginTransaction();
			if (pay.getCcnumber().length() < 17) {
				if (!(pay.getCcnumber().startsWith("XXXX"))) {
					pay.setCcnumber(le.encrypt(pay.getCcnumber()));
				}				
			}
			if (pay.getCcsec().length() < 4) {
				pay.setCcsec(le.encrypt(pay.getCcsec()));
			}
			sess.update(pay);
			tx.commit();*/
			System.out.println("failed on short report for cfecid " + id + ", confirm number " + confirm + " for year " + year);
		} finally {
			if (sess.isOpen()) {
				sess.flush();
				sess.close();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			addWatermark(outfile, outfileStage, imagesf);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getShortFormFile(String ccode, Logging leonLog, getDataImpl getDataImpl, boolean manual, String rjnumber, 
			String cfecid, String jrxmlLocation, String shortfileOut, String imageLocation, String thisStagingDir, ArenewEntity entity) {
		Session sess = null;
		//Transaction tx = null;
		//LEONencrypt le = new LEONencrypt();
		Set<ArenewPermits> plist = null;
		Set<ArenewVessels> vlist = null; 
		Set<ArenewChanges> clist = null;
		GWTChangeMonitor cmon = null;
		//List clist = null; 
		String last = null;
		String first = null;
		String hpounds = null;
		String spounds = null;
		String rdate = null;
		Map params = null;
		String infile = null;
		String outfileStage = null;
		String outfile = null;
		String imagesf = null;
		String id = null;
		String confirm = null;
		String year = null;
		//List mlist = null;
		//List<DrenewPayment> mlist = null;
		//List<DrenewPermits> plist = null;
		//List<DrenewVessels> vlist = null;
		//List<DrenewChanges> clist = null;
		try {
			sess = getDataImpl.fact.openSession();
			//mlist = sess.createQuery("from DrenewPayment p where p.id.confirmcode = '"+ccode+"'").list();
			//mlist = (List<DrenewPayment>) entity.getDrenewPayments();
			for (Iterator<ArenewPayment> p = entity.getArenewPayments().iterator(); p.hasNext();) {
			//for (Iterator p = mlist.iterator(); p.hasNext();) {
				pay = (ArenewPayment) p.next();
				if (pay.getConfirmcode().equalsIgnoreCase(ccode)) {
					/*
					 * descrypt the credit card and security numbers first
					 */
					//tx = sess.beginTransaction();
					//if (manual) {
					//	if (!(pay.getCcnumber().startsWith("XXXX"))) {
					//		pay.setCcnumber(le.encrypt(pay.getCcnumber()));
					//	}
					//}
					//sess.update(pay);
					//tx.commit();
					id = pay.getArenewEntity().getId().getCfecid();
					confirm = pay.getConfirmcode();
					year = pay.getArenewEntity().getId().getRyear();
					plist = pay.getArenewPermitses();
					vlist = pay.getArenewVesselses();
					clist = pay.getArenewChangeses();
					//plist = sess.createQuery("from DrenewPermits p where p.id.cfecid = '"+id+"' and p.id.ryear = '"+year+"' and p.drenewPayment.confirmcode = '"+confirm+"'").list();
					//vlist = sess.createQuery("from DrenewVessels v where v.id.cfecid = '"+id+"' and v.id.ryear = '"+year+"' and v.drenewPayment.confirmcode = '"+confirm+"'").list();
					//clist = sess.createQuery("from DrenewChanges c where c.drenewEntity.id.cfecid = '"+id+"' and c.drenewEntity.id.ryear = '"+year+"' and c.id.confirmcode = '"+confirm+"' order by c.type, c.object").list();
					if (clist.size() > 0) {
						cmon = new GWTChangeMonitor();
						for (Iterator it = clist.iterator(); it.hasNext();) {
							ArenewChanges chg = (ArenewChanges) it.next();
							if (chg.getId().getAttribute().equalsIgnoreCase("phoneArea")) { cmon.setPhone(true); }
							if (chg.getId().getAttribute().equalsIgnoreCase("phonePre")) { cmon.setPhone(true); }
							if (chg.getId().getAttribute().equalsIgnoreCase("phonePost")) { cmon.setPhone(true); }
							if (chg.getId().getAttribute().equalsIgnoreCase("email")) { cmon.setEmail(true); }		
							if (chg.getId().getAttribute().equalsIgnoreCase("PermMailingAddress")) { cmon.setPaddress(true); }
							if (chg.getId().getAttribute().equalsIgnoreCase("PermMailingAddress2")) { cmon.setPaddress2(true); }
							if (chg.getId().getAttribute().equalsIgnoreCase("PermMailingCity")) { cmon.setPcity(true); }
							if (chg.getId().getAttribute().equalsIgnoreCase("PermMailingState")) { cmon.setPstate(true); }
							if (chg.getId().getAttribute().equalsIgnoreCase("PermMailingZip")) { cmon.setPzip(true); }
						}
					} else {
						cmon = new GWTChangeMonitor();
					}
					List name = sess.createNativeQuery("select person_last_name, person_first_name from tier2.people_view where file_number = '"+id+"' and name_type is null").list();
					if (name.size() > 0) {
						Object[] rs1 = (Object[]) name.iterator().next();
						last = rs1[0].toString();
						first = rs1[1].toString();
						last = last.trim().toUpperCase();
						StringTokenizer st = new StringTokenizer(first);
						first = st.nextToken().trim().toUpperCase();
						List results = sess.createNativeQuery("select COALESCE(halbs, '0'), COALESCE(sablbs, '0') from tier2.ramrpt where lastname = '"+last+"' and firstname = '"+first+"' and year = '"+((Integer.parseInt(year))-1)+"'").list();
						if (results.size() > 0) {
							Object[] rs2 = (Object[]) results.iterator().next();
							hpounds = rs2[0].toString();
							spounds = rs2[1].toString();
						} else if (results.size() == 0) {
							hpounds = "0";
							spounds = "0";
						}
					} else {
						last = "Invalid";
						first = "Invalid";
						hpounds = "0";
						spounds = "0";
					}
					List list = sess.createNativeQuery("select to_char(p.receiveddate, 'MM/DD/YYYY HH:mi'), p.confirmcode from tier2.arenew_payment p where p.confirmcode = '"+confirm+"'").list();
					if (list.size() > 0) {
						Object[] rs3 = (Object[]) list.iterator().next();
						rdate = rs3[0].toString();
					} else if (list.size() == 0) {
						rdate = "Invalid";
					}
					params = new HashMap();
					params.put("cfecid", id);
					params.put("year", year);
					params.put("cnum", confirm);
					params.put("psize", Integer.toString(plist.size()));
					params.put("vsize", Integer.toString(vlist.size()));
					params.put("rdates", rdate);
					params.put("hpounds", hpounds);
					params.put("spounds", spounds);
					params.put("cmon", cmon);
					params.put("rjnum", rjnumber);
					infile = jrxmlLocation+"/sformAll.jrxml";
					outfile = shortfileOut+"/"+ccode+".pdf";
					outfileStage = thisStagingDir+"/"+ccode+".pdf";
					conn = DriverManager.getConnection(connectionURLThin, userID, userPassword);
					JasperDesign jasperDesign = JRXmlLoader.load(infile);
			        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
			        JasperExportManager.exportReportToPdfFile(jasperPrint, outfileStage);
			        imagesf = imageLocation+"/imagesf.png";
					/*
					 * Now, re-encrypt the credit card and security numbers
					 */
					//tx = sess.beginTransaction();
					//if (manual) {
					//	if (!(pay.getCcnumber().startsWith("XXXX"))) {
					//		pay.setCcnumber(le.encrypt(pay.getCcnumber()));
					//	}	
					//}
					//sess.update(pay);
					//tx.commit();
				}				
			}			
			
		} catch(Exception e) {
			e.printStackTrace();
			/*
			 * in case of an exception, check to see what state the credit card and security
			 * numbers are in. If they are NOT encrypted (class blew up prior to the code), 
			 * then do so prior to leaving.
			 */
			//tx = sess.beginTransaction();
			//if (pay.getCcnumber().length() < 17) {
			//	if (!(pay.getCcnumber().startsWith("XXXX"))) {
			//		pay.setCcnumber(le.encrypt(pay.getCcnumber()));
			//	}				
			//}
			//if (pay.getCcsec().length() < 4) {
			//	pay.setCcsec(le.encrypt(pay.getCcsec()));
			//}
			//sess.update(pay);
			//tx.commit();
			System.out.println("failed on short report for cfecid " + id + ", confirm number " + confirm + " for year " + year);
		} finally {
			if (sess.isOpen()) {
				//sess.flush();
				sess.close();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			addWatermark(outfile, outfileStage, imagesf);
		}
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
			io.printStackTrace();
		}		
	}
}
