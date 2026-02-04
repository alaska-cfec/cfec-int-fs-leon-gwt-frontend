package com.cfecweb.leon.solo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.cfecweb.leon.AppProperties;
import com.cfecweb.leon.config.HibernateSessionFactoryProvider;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.cfecweb.leon.shared.ArenewChanges;
import com.cfecweb.leon.shared.ArenewPayment;
import com.cfecweb.leon.shared.ArenewPermits;
import com.cfecweb.leon.shared.ArenewVessels;
import com.cfecweb.leon.shared.GWTChangeMonitor;

import static com.cfecweb.leon.config.HibernateSessionFactoryProvider.LEON_DB_PASSWORD;
import static com.cfecweb.leon.config.HibernateSessionFactoryProvider.LEON_DB_URL;
import static com.cfecweb.leon.config.HibernateSessionFactoryProvider.LEON_DB_USERNAME;
import static com.cfecweb.leon.config.HibernateSessionFactoryProvider.getEnvOrDefault;

public class BuildLeonRptPdf {
	final SessionFactory fact = HibernateSessionFactoryProvider.getSessionFactory();
	final static String connectionURLThin = getEnvOrDefault(LEON_DB_URL, AppProperties.get(AppProperties.HIBERNATE_CONNECTION_URL));
	final static String userID = getEnvOrDefault(LEON_DB_USERNAME, AppProperties.get(AppProperties.HIBERNATE_CONNECTION_USERNAME));
	final static String userPassword = getEnvOrDefault(LEON_DB_PASSWORD, AppProperties.get(AppProperties.HIBERNATE_CONNECTION_PASSWORD));
	static Connection conn = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BuildLeonRptPdf(String type, String report, String jyear) {
		Session sess = null;
		//Transaction tx = null;
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
		String outfile = null;
		String id = null;
		String confirm = null;
		String year = null;
		List mlist = null;
		try {
			sess = fact.openSession();
			if (type.equalsIgnoreCase("All")) {
				mlist = sess.createQuery("from ArenewPayment p").list();
			} else if (type.length() == 4) {
				mlist = sess.createQuery("from ArenewPayment p where p.arenewEntity.id.ryear = '"+type+"'").list();
			} else {
				mlist = sess.createQuery("from ArenewPayment p where p.arenewEntity.id.cfecid = '"+type+"' and p.arenewEntity.id.ryear ='"+jyear+"'").list();
			}			
			for (Iterator p = mlist.iterator(); p.hasNext();) {
				ArenewPayment pay = (ArenewPayment) p.next();
				/*
				 * descrypt the credit card and security numbers first
				 */
				//tx = sess.beginTransaction();
				//pay.setCcnumber(le.decrypt(pay.getCcnumber()));
				//pay.setCcsec(le.decrypt(pay.getCcsec()));
				//sess.update(pay);
				//tx.commit();
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
				params.put("rjnum", "Not Assigned");
				/*if (plist.size() > 0 && vlist.size() > 0) {
					//	Both Permits and Vessels
					if (report.equalsIgnoreCase("printouts")) {
						infile = "/webapps/jrxml/LEON/rformAllNC.jrxml";
					} else if (report.equalsIgnoreCase("shortforms")) {
						infile = "/webapps/jrxml/LEON/sformAll.jrxml";
					} else {
						//infile = "/webapps/jrxml/LEON/sformAll.jrxml";
					}
				} else if (plist.size() > 0 && vlist.size() < 1) {
					//	Permits but no Vessels
					if (report.equalsIgnoreCase("printouts")) {
						infile = "/webapps/jrxml/LEON/rformPmtNC.jrxml";
					} else if (report.equalsIgnoreCase("shortforms")) {
						infile = "/webapps/jrxml/LEON/sformAll.jrxml";
					} else {
						//
					}
				} else if (plist.size() < 1 && vlist.size() > 0) {
					//	Vessels but no Permits
					if (report.equalsIgnoreCase("printouts")) {
						infile = "/webapps/jrxml/LEON/rformVesNC.jrxml";
					} else if (report.equalsIgnoreCase("shortforms")) {
						infile = "/webapps/jrxml/LEON/sformAll.jrxml";
					} else {
						//
					}
				} else {
					//	No Permits or Vessels
					if (report.equalsIgnoreCase("printouts")) {
						infile = "/webapps/jrxml/LEON/rformSolo.jrxml";
					} else if (report.equalsIgnoreCase("shortforms")) {
						infile = "/webapps/jrxml/LEON/sformAll.jrxml";
					} else {
						//
					}
				}
				if (report.equalsIgnoreCase("printouts")) {
					outfile = "/webapps/out/LEON/"+year+"/printouts/r"+confirm+".pdf";
				} else if (report.equalsIgnoreCase("shortforms")) {
					outfile = "/webapps/out/LEON/"+year+"/shortforms/s"+confirm+".pdf";
				} else {
					//
				}*/
				infile = "/webapps/jrxml/LEON/sformAll.jrxml";
				outfile = "/webapps/out/LEON/"+year+"/shortforms/s"+confirm+".pdf";
				conn = DriverManager.getConnection(connectionURLThin, userID, userPassword);
				JasperDesign jasperDesign = JRXmlLoader.load(infile);
		        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
				JasperExportManager.exportReportToPdfFile(jasperPrint, outfile);											
				/*
				try {
					Runtime.getRuntime().exec("lpr " + outfile);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				*/
				/*
				 * Now, re-encrypt the credit card and security numbers
				 */
				//tx = sess.beginTransaction();
				//pay.setCcnumber(le.encrypt(pay.getCcnumber()));
				//pay.setCcsec(le.encrypt(pay.getCcsec()));
				//sess.update(pay);
				//tx.commit();
			}			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("failed on report for cfecid " + id + ", confirm number " + confirm + " for year " + year);
		} finally {
			if (sess.isOpen()) {
				sess.flush();
				sess.close();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws SQLException, IOException {
		@SuppressWarnings("unused")
		BuildLeonRptPdf testhiber = new BuildLeonRptPdf("611464", "shortforms", "2021");
	}
}
