package com.cfecweb.leon.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;







//import org.hibernate.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cfecweb.leon.shared.ArenewEntity;
import com.cfecweb.leon.shared.ArenewEntityId;
import com.cfecweb.leon.shared.ArenewOtherpermits;
import com.cfecweb.leon.shared.ArenewPayment;
import com.cfecweb.leon.shared.ArenewPermits;
import com.cfecweb.leon.shared.ArenewPermitsId;
import com.cfecweb.leon.shared.ArenewVessels;
import com.cfecweb.leon.shared.ArenewVesselsId;
import com.cfecweb.leon.shared.GWTfisheryTable;

/*
 * This class contains particular queries that build and return sets of data necessary for the 
 * various steps as a person goes through LEON.
 */

@SuppressWarnings({ "rawtypes" })
public class GetVitals {
    private static final Logger LOGGER = LogManager.getLogger(GetVitals.class);

	Session session = null;
	Transaction tx = null;
	List addressList = null;
	Notify notify = new Notify();
	public String table = null;

	/*
	 * This method receives an ID and current year, then builds 3 years worth of
	 * renewal forms for the CFECID, then returns the object with relative links
	 * to the PDF.
	 */
	@SuppressWarnings("resource")
	public List<String> getForms(String id, String ryear, Logging leonLog, getDataImpl dataImpl, String thisInFileDir, String thisOutFileDir, String thisPrePrintsDir) {
		String start = "no";
		String line;
		String cfecnumstart = "START " + id;
		String cfecnumstop = "END " + id;
		List<String> formlinks = new ArrayList<String>();
		boolean valid = false;
		Long ecount = null;
		boolean found = false;
		String link = null;
		int year = 0;
		/*
		 *  Automatically generate copy of renewal form(s) for current year plus last 2 if user selected download forms
		 */
		try {
			session = dataImpl.fact.openSession();
			ecount = (Long) session.createQuery("select count(*) from RnwEntity oe where oe.id.permitHolderFileNumber = '" + id + "'").uniqueResult();
			if (id.toUpperCase().equalsIgnoreCase("AAAAAA")) {
				valid = true;
			} else if (ecount > 0) {
				valid = true;
			} else {
				valid = false;
			}
			if (valid) {
				for (int syear = (Integer.parseInt(ryear)); syear >= (Integer.parseInt(ryear) - 1); syear--) {
					String RanNum = new Integer((int) (Math.random() * 99999999)).toString();
					BufferedReader in = new BufferedReader(new FileReader(thisInFileDir+"/Printout_" + syear + ".PCL"));
					File file = new File(thisInFileDir+"/data" + RanNum + ".txt");
					BufferedWriter out = new BufferedWriter(new FileWriter(file));
					if (!in.ready())
						throw new IOException();
					while ((line = in.readLine()) != null) {
						if (line.endsWith(cfecnumstop)) {
							found = true;
							break;
						}
						if (start.equalsIgnoreCase("yes")) {
							found = true;
							out.write(line + '\r' + '\n');
						}
						if (line.endsWith(cfecnumstart)) {
							found = true;
							start = "yes";
						}
					}
					in.close();
					out.close();
					if (found) {
						String[] cmd = {
							thisInFileDir+"/r" + syear + ".sh",
							thisInFileDir+"/data" + RanNum + ".txt",
							thisPrePrintsDir+"/"+ id  + syear + ".pdf"};
                        LOGGER.info("GetVitals.getForms: Running command: {} {} {}", cmd[0], cmd[1], cmd[2]);
						link = thisOutFileDir+"/preprints/" + id + syear + ".pdf";
						year = syear;
						Runtime.getRuntime().exec(cmd);
					} else {
						link = "nodata";
						year = syear;
					}
					formlinks.add(link);
					formlinks.add(Integer.toString(year));
					start = "no";
					found = false;
					file.deleteOnExit();
				}
			} else {
				formlinks.add("invalid");
			}
		} catch (Exception ex) {
            LOGGER.error("GetForms error", ex);
			if (tx != null) {
				// tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
		return formlinks;
	}

	/*
	 * This method receives an ID, current year, renewal option and poverty
	 * selection, then builds and returns an object containing their current and
	 * temporary address.
	 */
	public ArenewEntity getAddress(String id, String ryear, boolean option, boolean poverty, Logging leonLog, getDataImpl dataImpl) {
		ArenewEntity entity = null;
		ArenewEntityId entId = null;
		//@SuppressWarnings("unused")
		//List<ArenewPermits> plist = null;
		Long existCount2 = null;
		Long existCount3 = null;
		Long pmtCount = null;
		Long vesCount = null;
		Long totalCount = null;
		Long diffpaid1 = null;
		Long diffpaid2 = null;
		Long diff2paidPast1 = null;
		Long diff2paidPast2 = null;
		Long diff3paidPast1 = null;
		Long diff3paidPast2 = null;
		StringBuffer myquery = null;
		int l2yeartmp = (Integer.parseInt(ryear) - 1);
		String l2year = Integer.toString(l2yeartmp);
		int l3yeartmp = (Integer.parseInt(ryear) - 2);
		String l3year = Integer.toString(l3yeartmp);
		List<ArenewPermits> rplist = null;
		List<ArenewOtherpermits> nplist = null;
		List<ArenewVessels> ovlist = null;
		List<ArenewPayment> oblist = null;
		try {
			entId = new ArenewEntityId();
			entId.setCfecid(id);
			entId.setRyear(ryear);
			session = dataImpl.fact.openSession();
			entity = (ArenewEntity) session.get(ArenewEntity.class, entId);
			/*
			 *  Check the entity numbers first, determine which object to return
			 */
			existCount2 = (Long) session.createQuery("select count(*) from RnwEntity oe where oe.id.permitHolderFileNumber = '" + id + "' and oe.id.XRenewalYear = '" + ryear + "'").uniqueResult();
			existCount3 = (Long) session.createQuery("select count(*) from PeopleView le where le.id.fileNumber = '" + id + "'").uniqueResult();
			if (!(entity == null)) {
				System.out.println("getVitals client option 1, existing transactions this year");
				System.out.println("getVitals client option 1, phonePub " + entity.getPhonepub());			
				/*
				 * this person has successfully completed at least 1 transaction in LEON for current year
				 */
				rplist = new ArrayList<ArenewPermits>();
				nplist = new ArrayList<ArenewOtherpermits>();
				nplist.addAll(entity.getArenewOtherpermitses());
				/*
				 * add to rplist all currently renewed permits that have a valid serial number
				 */
				for (Iterator it1 = entity.getArenewPermitses().iterator(); it1.hasNext();) {
					ArenewPermits permit = (ArenewPermits) it1.next();
					//if (!(permit.getId().getSerial().equalsIgnoreCase("Not Issued"))) {
						if (permit.getStatus().equalsIgnoreCase("Pending") || permit.getStatus().equalsIgnoreCase("Completed")) {
							permit.setNotes("<span class='regred10'>Permit already renewed</span>");
							permit.setFee(permit.getFee());
							permit.setOfee(permit.getFee());
							permit.setRenewed(true);
							permit.setAlready(true);
						}
						rplist.add(permit);
					//}					
				}
				ovlist = new ArrayList<ArenewVessels>();
				/*
				 * add to ovlist all currently relicensed vessels that have a valid adfg number
				 */
				for (Iterator it2 = entity.getArenewVesselses().iterator(); it2.hasNext();) {
					ArenewVessels vessel = (ArenewVessels) it2.next();
					//if (!(vessel.getId().getAdfg().substring(0, 4).equalsIgnoreCase("Temp"))) {
						if (vessel.getStatus().equalsIgnoreCase("Pending") || vessel.getStatus().equalsIgnoreCase("Completed")) {
							vessel.setDetails("<span class='regred10'>Vessel already licensed</span>");
							vessel.setRenewed(true);
						}
						ovlist.add(vessel);
					//}					
				}
				oblist = new ArrayList<ArenewPayment>();
				oblist.addAll(entity.getArenewPayments());
				entity.getArenewPayments().clear();
				entity.getArenewPayments().addAll(oblist);
				entity.setFirsttime("false");		
				// TODO test this next update
				/*
				 *  Get perm address info for user and place into object for building
				 *  We do this just in case a person changes their name or address during the licensing year manually
				 */
				if (id.equalsIgnoreCase("AAAAAA")) {
					myquery = new StringBuffer("select ne.id.XFilsname, COALESCE(ne.id.addressStreet, ' '), COALESCE(ne.id.addressCity, 'N'), COALESCE(ne.id.addressState, 'F'), "
							+ "COALESCE(ne.id.addressPostalCode, 'F'), COALESCE(ne.id.addressCountry, ' '), ne.id.permitHolderId, COALESCE(ne.id.permitHolderPhone, 'none') from RnwEntity ne where ne.id.permitHolderFileNumber = '"
							+ id + "' and ne.id.XRenewalYear = '" + ryear + "'");
				} else {
					myquery = new StringBuffer(
							"select le.id.personsName, COALESCE(le.id.addressStreet, ''), COALESCE(le.id.addressCity, 'N'), COALESCE(le.id.addressState, 'F'), "
							+ "COALESCE(le.id.addressPostalCode, 'F'), COALESCE(le.id.addressCountry, ''), le.id.idNumber, COALESCE(le.id.phoneNumber, 'none') from PeopleView le where le.id.fileNumber = '"
							+ id + "'");
				}
				Iterator list = session.createQuery(myquery.toString()).setFirstResult(0).list().iterator();
				if (list.hasNext()) {
					while (list.hasNext()) {
						Object[] rs = (Object[]) list.next();
						entity.setXname((String) rs[0]);
						if (rs[3].toString().equalsIgnoreCase("F") || rs[4].toString().equalsIgnoreCase("F")) {
							/*
							 *  This entity has a foreign address, handle it differently
							 */
							if (rs[2].toString().equalsIgnoreCase("N")) {
								entity.setPaddress2((String) rs[1] + ", " + (String) rs[5]);
							} else {
								entity.setPaddress2((String) rs[1] + ", " + (String) rs[2] + ", " + (String) rs[5]);
							}
							entity.setPaddress(null);
							entity.setPcity(null);
							entity.setPstate(null);
							entity.setPzip(null);
							entity.setPcountry((String) rs[5]);
							entity.setForeign(true);
						} else {
							/*
							 *  This entity has a U.S. address
							 */
							entity.setPaddress((String) rs[1]);
							entity.setPcity((String) rs[2]);
							entity.setPstate((String) rs[3]);
							entity.setPzip((String) rs[4]);
							entity.setPcountry((String) rs[5]);
							entity.setForeign(false);
						}						
					}
				}				
				if (!(entity.getPaddress() == null)) {
					entity.setForeign(false);
				} else {
					entity.setForeign(true);
				}
				/*
				 *  first get a total count of all permits and vessels
				 */
				pmtCount = (Long) session.createQuery("select count(*) from RnwPmt op where op.id.permitHolderFileNumber = '" + id + "' and op.id.XRenewalYear = '" + ryear + "'").uniqueResult();
				vesCount = (Long) session.createQuery("select count(*) from RnwVes ov where ov.id.permitHolderFileNumber = '" + id + "' and ov.id.XRenewalYear = '" + ryear + "'").uniqueResult();
				/*
				 * Next, grab the current and previous years differential value
				 */
				List currentDiff = session.createNativeQuery("select cf.differential from tier2.TBL_DIFFERENTIAL cf where cf.year = '"+ryear+"'").setFirstResult(0).list();
				String currDiff = (String) currentDiff.iterator().next();
				List p2reviousDiff = session.createNativeQuery("select cf.differential from tier2.TBL_DIFFERENTIAL cf where cf.year = '"+l2year+"'").setFirstResult(0).list();
				String p2revDiff = (String) p2reviousDiff.iterator().next();
				List p3reviousDiff = session.createNativeQuery("select cf.differential from tier2.TBL_DIFFERENTIAL cf where cf.year = '"+l3year+"'").setFirstResult(0).list();
				String p3revDiff = (String) p3reviousDiff.iterator().next();
				totalCount = (pmtCount + vesCount);
				/*
				 *  Now, check to see if at least 1 permit has been renewed to determine differential status
				 */
				// TODO verify differential paid is working for all years
				/*
				 * check differential paid for current year in both ArenewPermits and DrnwPmt
				 */
				diffpaid1 = (Long) session.createQuery("select count(*) from ArenewPermits op where op.id.cfecid = '" + id + "' and op.id.pyear = '" + ryear
						+ "' and upper(op.status) in ('COMPLETED', 'PENDING')").uniqueResult();
				//System.out.println("diffpaid1  -  select count(*) from ArenewPermits op where op.id.cfecid = '" + id + "' and op.id.pyear = '" + ryear
				//		+ "' and upper(op.status) in ('COMPLETED', 'PENDING')");
				if (diffpaid1 > 0) {
					entity.setDifferentialc("true");
				} else {
					diffpaid2 = (Long) session.createQuery("select count(*) from RnwPmt op where op.id.permitHolderFileNumber = '" + id + "' and op.id.XRenewalYear = '" + ryear
							+ "' and upper(op.id.renewalStatus) in ('C', 'P')").uniqueResult();
					//System.out.println("diffpaid2  -  select count(*) from DrnwPmt op where op.id.permitHolderFileNumber = '" + id + "' and op.id.XRenewalYear = '" + ryear
					//		+ "' and upper(op.id.renewalStatus) in ('C', 'P')");
					if (diffpaid2 > 0) {
						entity.setDifferentialc("true");
					} else {
						entity.setDifferentialc("false");
					}					
				}
				/*
				 * check differential paid for last year in both ArenewPermits and DrnwPmt
				 */
				diff2paidPast1 = (Long) session.createQuery("select count(*) from ArenewPermits op where op.id.cfecid = '" + id + "' and op.id.pyear = '" + l2year
						+ "' and upper(op.status) in ('COMPLETED', 'PENDING')").uniqueResult();
				//System.out.println("diffpaidPast1  -  select count(*) from ArenewPermits op where op.id.cfecid = '" + id + "' and op.id.pyear = '" + l2year
				//		+ "' and upper(op.status) in ('COMPLETED', 'PENDING')");
				if (diff2paidPast1 > 0) {
					entity.setDifferentialp("true");
				} else {
					diff2paidPast2 = (Long) session.createQuery("select count(*) from RnwPmt op where op.id.permitHolderFileNumber = '" + id + "' and op.id.XRenewalYear = '" + l2year
							+ "' and upper(op.id.renewalStatus) in ('C', 'P')").uniqueResult();
					//System.out.println("diffpaid2  -  select count(*) from DrnwPmt op where op.id.permitHolderFileNumber = '" + id + "' and op.id.XRenewalYear = '" + l2year
					//		+ "' and upper(op.id.renewalStatus) in ('C', 'P')");
					if (diff2paidPast2 > 0) {
						entity.setDifferentialp("true");
					} else {
						entity.setDifferentialp("false");
					}					
				}
				/*
				 * check differential paid for 2 years ago in both ArenewPermits and DrnwPmt
				 */
				diff3paidPast1 = (Long) session.createQuery("select count(*) from ArenewPermits op where op.id.cfecid = '" + id + "' and op.id.pyear = '" + l3year
						+ "' and upper(op.status) in ('COMPLETED', 'PENDING')").uniqueResult();
				//System.out.println("diffpaidPast1  -  select count(*) from ArenewPermits op where op.id.cfecid = '" + id + "' and op.id.pyear = '" + l3year
				//		+ "' and upper(op.status) in ('COMPLETED', 'PENDING')");
				if (diff3paidPast1 > 0) {
					entity.setDifferentialp("true");
				} else {
					diff3paidPast2 = (Long) session.createQuery("select count(*) from RnwPmt op where op.id.permitHolderFileNumber = '" + id + "' and op.id.XRenewalYear = '" + l3year
							+ "' and upper(op.id.renewalStatus) in ('C', 'P')").uniqueResult();
					//System.out.println("diffpaid2  -  select count(*) from DrnwPmt op where op.id.permitHolderFileNumber = '" + id + "' and op.id.XRenewalYear = '" + l3year
					//		+ "' and upper(op.id.renewalStatus) in ('C', 'P')");
					if (diff3paidPast2 > 0) {
						entity.setDifferentialp("true");
					} else {
						entity.setDifferentialp("false");
					}					
				}
				entity.getId().setRyear(ryear);
				entity.setPmtcount(pmtCount);
				entity.setVescount(vesCount);
				entity.setTotcount(totalCount);
				entity.getArenewPermitses().clear();
				List<ArenewPermits> perlist = getplist(entity, id, ryear, rplist, nplist, session);
				if (perlist.size() > 0) {
					entity.getArenewPermitses().addAll(perlist);
				}
				entity.getArenewVesselses().clear();
				List<ArenewVessels> veslist = getvlist(entity, id, ryear, ovlist, session);
				if (veslist.size() > 0) {
					entity.getArenewVesselses().addAll(veslist);
				}
				entity.setIllegal("false");
				entity.setDiffamountcyear(currDiff);
				entity.setDiffamountpyear(p2revDiff);
				entity.setDiffamountp2year(p3revDiff);
				
				/*
				 * reset the halibut reducedFee values for this new session
				 */
				entity.setReducedHalibut("false");
				entity.setReducedSablefish("false");
				entity.setAgentsub("N/A");
				entity.setAlienreg(null);
				
				/*
				 * Before we pre-populate the physical address, check to make sure it is not a PO Box combination
				 */
				//if (entity.getResidency().equalsIgnoreCase("N")) {
					if (!(entity.getRaddress()==null)) {
						if (entity.getRaddress().toString().toLowerCase().matches(""
								+ "[P|p]+(OST|ost)\\.?[\\s]*[O|o]+(FFICE|ffice)\\.?[\\s]*[b|B]?[\\s]*[o|O]?[\\s]*[x|X]?[\\s]*[0-9#]*|"
								+ "\\b[P|p]+(OST|ost)\\.?[\\s]*[C|c]+(ODE|ode)\\.?[\\s]*[b|B]?[\\s]*[o|O]?[\\s]*[x|X]?[\\s]*[0-9#]*|"
								+ "\\b[P|p]+(OSTAL|ostal)\\.?[\\s]*[O|o]+(FFICE|ffice)\\.?[\\s]*[b|B]?[\\s]*[o|O]?[\\s]*[x|X]?[\\s]*[0-9#]*|"
								+ "\\b[P|p]+(OSTAL|ostal)\\.?[\\s]*[C|c]+(ODE|ode)\\.?[\\s]*[b|B]?[\\s]*[o|O]?[\\s]*[x|X]?[\\s]*[0-9#]*|"
								+ "\\b[B|b]+(OX|ox)\\.?[\\s]*[C|c]+(ODE|ode)\\.?[\\s]*[0-9#]*|"
								+ "\\b[B|b]+(OX|ox)\\.?[\\s]*[0-9#]*|"
								+ "\\b[p|P]\\.?[\\s]*[o|O]\\.?[\\s]*[b|B]?[\\s]*[o|O]?[\\s]*[x|X]?[\\s]*[0-9#]*")) {
							entity.setRaddress(null);
							entity.setRcity(null);
							entity.setRstate(null);
							entity.setRzip(null);
							entity.setYears(null);
							entity.setMonths(null);
						}
					}					
				//}	
				//System.out.println("1 - FirstTime? " + entity.getFirsttime());
				//System.out.println("1 - phone is " + entity.getArea() + entity.getPhone());
				//System.out.println("1 - email is " + entity.getEmail());
				//System.out.println("1 - Pub is " + entity.getPhonepub());
			} else if (existCount2 > 0) {
				System.out.println("getVitals option 2, existing transactions previous year(s)");
				/*
				 * This person has not used LEON this current year, but has available records
				 */
				entity = new ArenewEntity();
				entity.setId(entId);
				entity.setFirsttime("true");
				/*
				 * get a permit and vessel count
				 */
				pmtCount = (Long) session.createQuery("select count(*) from RnwPmt op where op.id.permitHolderFileNumber = '" + id + "' " +
						"and op.id.XRenewalYear = '" + ryear + "'").uniqueResult();
				vesCount = (Long) session.createQuery("select count(*) from RnwVes ov where ov.id.permitHolderFileNumber = '" + id + "' " +
						"and ov.id.XRenewalYear = '" + ryear + "'").uniqueResult();
				List currentDiff = session.createNativeQuery("select cf.differential from tier2.TBL_DIFFERENTIAL cf where cf.year = '"+ryear+"'").setFirstResult(0).list();
				String currDiff = (String) currentDiff.iterator().next();
				List p2reviousDiff = session.createNativeQuery("select cf.differential from tier2.TBL_DIFFERENTIAL cf where cf.year = '"+l2year+"'").setFirstResult(0).list();
				String p2revDiff = (String) p2reviousDiff.iterator().next();
				List p3reviousDiff = session.createNativeQuery("select cf.differential from tier2.TBL_DIFFERENTIAL cf where cf.year = '"+l3year+"'").setFirstResult(0).list();
				String p3revDiff = (String) p3reviousDiff.iterator().next();
				totalCount = (pmtCount + vesCount);
				/*
				 * checkDifferential method receives the cfecid and year value, then checks to see if the person has already renewed a permit
				 * for the year passed. If not, and they are a non-resident, then they must pay the differential.
				 */
				if (checkDifferential(id, ryear)) {
					entity.setDifferentialc("true");
				} else {
					entity.setDifferentialc("false");
				}
				if (checkDifferential(id, l2year)) {
					entity.setDifferentialp("true");
				} else {
					entity.setDifferentialp("false");
				}
				if (checkDifferential(id, l3year)) {
					entity.setDifferentialp2("true");
				} else {
					entity.setDifferentialp2("false");
				}
				/*
				 *  Get perm address info for user and place into object for building
				 */
				if (id.equalsIgnoreCase("AAAAAA")) {
					myquery = new StringBuffer("select ne.id.XFilsname, COALESCE(ne.id.addressStreet, ' '), COALESCE(ne.id.addressCity, 'N'), COALESCE(ne.id.addressState, 'F'), "
							+ "COALESCE(ne.id.addressPostalCode, 'F'), COALESCE(ne.id.addressCountry, ' '), ne.id.permitHolderId, COALESCE(ne.id.permitHolderPhone, 'none') from RnwEntity ne where ne.id.permitHolderFileNumber = '"
							+ id + "' and ne.id.XRenewalYear = '" + ryear + "'");
				} else {
					myquery = new StringBuffer(
							"select le.id.personsName, COALESCE(le.id.addressStreet, ''), COALESCE(le.id.addressCity, 'N'), COALESCE(le.id.addressState, 'F'), "
							+ "COALESCE(le.id.addressPostalCode, 'F'), COALESCE(le.id.addressCountry, ''), le.id.idNumber, COALESCE(le.id.phoneNumber, 'none') from PeopleView le where le.id.fileNumber = '"
							+ id + "'");
				}
				Iterator old = session.createQuery("select COALESCE(ar.xname, ''), COALESCE(ar.raddress, ''), COALESCE(ar.raddress2, 'N'), COALESCE(ar.rcity, ''), COALESCE(ar.rstate, ''), "
						+ "COALESCE(ar.rzip, ''), ar.months, ar.years, ar.citizen, ar.residency, COALESCE(ar.email, ''), ar.poverty, COALESCE(ar.alienreg, 'N') "
						+ "from ArenewEntity ar where ar.id.cfecid = '" + id + "' order by ar.id.ryear desc").setFirstResult(0).list().iterator();
				Iterator list = session.createQuery(myquery.toString()).setFirstResult(0).list().iterator();
				if (old.hasNext()) {
					Object[] rs1 = (Object[]) old.next();
					entity.setXname((String) rs1[0]);
					if ((rs1[2]).toString().equalsIgnoreCase("N")) {
						entity.setRaddress((String) rs1[1]);
						entity.setRcity((String) rs1[3]);
						entity.setRstate((String) rs1[4]);
						entity.setRzip((String) rs1[5]);
						entity.setForeign(false);
					} else {
						entity.setRaddress2((String) rs1[2]);
						entity.setForeign(true);
						entity.setAlienreg((String) rs1[12]);
					}							
					entity.setMonths((String) rs1[6]);
					entity.setYears((String) rs1[7]);
					entity.setCitizen((String) rs1[8]);
					entity.setResidency((String) rs1[9]);
					entity.setEmail((String) rs1[10]);
					entity.setPoverty((String) rs1[11]);
				}
				if (list.hasNext()) {
					while (list.hasNext()) {
						/*
						 *  populate entity object
						 */
						Object[] rs = (Object[]) list.next();
						entity.setXname((String) rs[0]);
						if (rs[3].toString().equalsIgnoreCase("F") || rs[4].toString().equalsIgnoreCase("F")) {
							/*
							 *  This entity has a foreign address, handle it differently
							 */
							if (rs[2].toString().equalsIgnoreCase("N")) {
								entity.setPaddress2((String) rs[1] + ", " + (String) rs[5]);
							} else {
								entity.setPaddress2((String) rs[1] + ", " + (String) rs[2] + ", " + (String) rs[5]);
							}
							entity.setPaddress(null);
							entity.setPcity(null);
							entity.setPstate(null);
							entity.setPzip(null);
							entity.setPcountry((String) rs[5]);
							entity.setForeign(true);
						} else {
							/*
							 *  This entity has a U.S. address
							 */
							entity.setPaddress((String) rs[1]);
							entity.setPcity((String) rs[2]);
							entity.setPstate((String) rs[3]);
							entity.setPzip((String) rs[4]);
							entity.setPcountry((String) rs[5]);
							entity.setForeign(false);
						} 						
						String firstthree = ((String) rs[6].toString().substring(0, 3));
						String secondtwo = ((String) rs[6].toString().substring(3, 5));
						if (firstthree.equalsIgnoreCase("000") && secondtwo.equalsIgnoreCase("00")) {
							entity.setCompany("true");
							entity.setIllegal("false");
						} else if (firstthree.equalsIgnoreCase("999") && secondtwo.equalsIgnoreCase("00")) {
							entity.setCompany("false");
							entity.setIllegal("true");
						} else if (firstthree.equalsIgnoreCase("999") && secondtwo.equalsIgnoreCase("01")) {
							entity.setCompany("false");
							entity.setIllegal("true");
						} else if (firstthree.equalsIgnoreCase("000") && secondtwo.equalsIgnoreCase("98")) {
							entity.setCompany("false");
							entity.setIllegal("true");
						} else {
							entity.setCompany("false");
							entity.setIllegal("false");
						}

						if (poverty) {
							entity.setPoverty("true");
						} else {
							entity.setPoverty("false");
						}
						entity.setIllegal("false");
						if (!(rs[7].toString().equalsIgnoreCase("none"))) {
							if (rs[7].toString().length() == 10) {
								String earea = ((String) rs[7].toString().substring(0, 3));
								String ephone = ((String) rs[7].toString().substring(3, 10));
								if (!(earea.equalsIgnoreCase("000"))) {
									entity.setArea(earea);
								}
								if (!(ephone.equalsIgnoreCase("0000000"))) {
									entity.setPhone(ephone);
								}
							} else if (rs[7].toString().length() == 7) {
								String ephone = ((String) rs[7].toString().substring(0, 7));
								if (!(ephone.equalsIgnoreCase("0000000"))) {
									entity.setPhone(ephone);
								}
							}
						}
					}
				}
				entity.getId().setRyear(ryear);
				entity.setPmtcount(pmtCount);
				entity.setVescount(vesCount);
				entity.setTotcount(totalCount);
				if (entity.getDifferentialc().equalsIgnoreCase("true")) {
					entity.setDiffamount(currDiff);
				} else {
					entity.setDiffamount("0.0");
				}
				entity.setDiffamountcyear(currDiff);
				entity.setDiffamountpyear(p2revDiff);
				entity.setDiffamountp2year(p3revDiff);
				entity.setReducedHalibut("false");
				entity.setReducedSablefish("false");
				entity.setAgentsub("N/A");
				entity.setAlienreg(null);
				
				/*
				 * Before we pre-populate the physical address, check to make sure it is not a PO Box combination
				 */
				//if (entity.getResidency().equalsIgnoreCase("R")) {
					if (!(entity.getRaddress()==null)) {
						if (entity.getRaddress().toString().toLowerCase().matches(""
								+ "[P|p]+(OST|ost)\\.?[\\s]*[O|o]+(FFICE|ffice)\\.?[\\s]*[b|B]?[\\s]*[o|O]?[\\s]*[x|X]?[\\s]*[0-9#]*|"
								+ "\\b[P|p]+(OST|ost)\\.?[\\s]*[C|c]+(ODE|ode)\\.?[\\s]*[b|B]?[\\s]*[o|O]?[\\s]*[x|X]?[\\s]*[0-9#]*|"
								+ "\\b[P|p]+(OSTAL|ostal)\\.?[\\s]*[O|o]+(FFICE|ffice)\\.?[\\s]*[b|B]?[\\s]*[o|O]?[\\s]*[x|X]?[\\s]*[0-9#]*|"
								+ "\\b[P|p]+(OSTAL|ostal)\\.?[\\s]*[C|c]+(ODE|ode)\\.?[\\s]*[b|B]?[\\s]*[o|O]?[\\s]*[x|X]?[\\s]*[0-9#]*|"
								+ "\\b[B|b]+(OX|ox)\\.?[\\s]*[C|c]+(ODE|ode)\\.?[\\s]*[0-9#]*|"
								+ "\\b[B|b]+(OX|ox)\\.?[\\s]*[0-9#]*|"
								+ "\\b[p|P]\\.?[\\s]*[o|O]\\.?[\\s]*[b|B]?[\\s]*[o|O]?[\\s]*[x|X]?[\\s]*[0-9#]*")) {
							entity.setRaddress(null);
							entity.setRcity(null);
							entity.setRstate(null);
							entity.setRzip(null);
							entity.setYears(null);
							entity.setMonths(null);
						}
					}					
				//}					
				
				List<ArenewPermits> perlist = getplist(entity, id, ryear, rplist, nplist, session);
				if (perlist.size() > 0) {
					entity.getArenewPermitses().addAll(perlist);
				}
				List<ArenewVessels> veslist = getvlist(entity, id, ryear, ovlist, session);
				if (veslist.size() > 0) {
					entity.getArenewVesselses().addAll(veslist);
				}
				//System.out.println("getVitals option 2, " + entity.getArea() + " - " + entity.getPhone());
			} else if (existCount3 > 0) {
				System.out.println("getVitals option 3, no history in LEON");
				/*
				 * This person doesnt have any valid LEON records NOR do they have any permits or vessels to renew
				 */
				entity = new ArenewEntity();
				entity.setId(entId);
				entity.setFirsttime("true");
				/*
				 *  Get perm address info for user and place into object for building
				 */
				myquery = new StringBuffer(
						"select le.id.personsName, COALESCE(le.id.addressStreet, ''), COALESCE(le.id.addressCity, 'N'), COALESCE(le.id.addressState, 'F'), "
						+ "COALESCE(le.id.addressPostalCode, 'F'), COALESCE(le.id.addressCountry, ''), le.id.idNumber, COALESCE(le.id.phoneNumber, 'none') " +
						"from PeopleView le where le.id.fileNumber = '" + id + "'");
				Iterator list = session.createQuery(myquery.toString()).setFirstResult(0).list().iterator();
				if (list.hasNext()) {
					while (list.hasNext()) {
						Object[] rs = (Object[]) list.next();
						entity.setXname((String) rs[0]);
						if (rs[3].toString().equalsIgnoreCase("F") || rs[4].toString().equalsIgnoreCase("F")) {
							/*
							 *  This entity has a foreign address, handle it differently
							 */
							if (rs[2].toString().equalsIgnoreCase("N")) {
								entity.setPaddress2((String) rs[1] + ", " + (String) rs[5]);
							} else {
								entity.setPaddress2((String) rs[1] + ", " + (String) rs[2] + ", " + (String) rs[5]);
							}
							entity.setPaddress(null);
							entity.setPcity(null);
							entity.setPstate(null);
							entity.setPzip(null);
							entity.setPcountry((String) rs[5]);
							entity.setForeign(true);
						} else {
							/*
							 *  This entity has a U.S. address
							 */
							entity.setPaddress((String) rs[1]);
							entity.setPcity((String) rs[2]);
							entity.setPstate((String) rs[3]);
							entity.setPzip((String) rs[4]);
							entity.setPcountry((String) rs[5]);
							entity.setForeign(false);
						}
						if (poverty) {
							entity.setPoverty("true");
						} else {
							entity.setPoverty("false");
						}
						String firstthree = ((String) rs[6].toString().substring(0, 3));
						String secondtwo = ((String) rs[6].toString().substring(3, 5));
						if (firstthree.equalsIgnoreCase("000") && secondtwo.equalsIgnoreCase("00")) {
							entity.setCompany("true");
							entity.setIllegal("false");
						} else if (firstthree.equalsIgnoreCase("999") && secondtwo.equalsIgnoreCase("00")) {
							entity.setCompany("false");
							entity.setIllegal("true");
						} else if (firstthree.equalsIgnoreCase("999") && secondtwo.equalsIgnoreCase("01")) {
							entity.setCompany("false");
							entity.setIllegal("true");
						} else if (firstthree.equalsIgnoreCase("000") && secondtwo.equalsIgnoreCase("98")) {
							entity.setCompany("false");
							entity.setIllegal("true");
						} else {
							entity.setCompany("false");
							entity.setIllegal("false");
						}
						if (poverty) {
							entity.setPoverty("true");
						} else {
							entity.setPoverty("false");
						}
						if (!(rs[7].toString().equalsIgnoreCase("none"))) {
							if (rs[7].toString().length() == 10) {
								String earea = ((String) rs[7].toString().substring(0, 3));
								String ephone = ((String) rs[7].toString().substring(3, 10));
								if (!(earea.equalsIgnoreCase("000"))) {
									entity.setArea(earea);
								}
								if (!(ephone.equalsIgnoreCase("0000000"))) {
									entity.setPhone(ephone);
								}
							} else if (rs[7].toString().length() == 7) {
								String ephone = ((String) rs[7].toString().substring(0, 7));
								if (!(ephone.equalsIgnoreCase("0000000"))) {
									entity.setPhone(ephone);
								}
							}
						}
					}
				}
				entity.getId().setRyear(ryear);
				entity.setPmtcount(0);
				entity.setVescount(0);
				entity.setTotcount(0);
				entity.setDifferentialc("false");
				entity.setDifferentialp("false");
				List currentDiff = session.createNativeQuery("select cf.differential from tier2.TBL_DIFFERENTIAL cf where cf.year = '"+ryear+"'").setFirstResult(0).list();
				String currDiff = (String) currentDiff.iterator().next();
				entity.setDiffamount("0.0");
				entity.setDiffamountcyear(currDiff);
				entity.setDiffamountpyear("000");
				entity.setDiffamountp2year("000");
				//System.out.println("getVitals option 3, " + entity.getArea() + " - " + entity.getPhone());
			}
            // Preload lazy collections
            Hibernate.initialize(entity.getArenewChangeses());
            Hibernate.initialize(entity.getArenewPermitses());
            Hibernate.initialize(entity.getArenewVesselses());
		} catch (Exception ex) {
			if (tx != null) {
				// tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
		return entity;
	}

	/*
	 * This method builds and returns a permit list that can consist of completed and non-completed permits.
	 * For completed permits, get all of the info from arenew_permits to include any changes this year.
	 * For non-completed permits, get the info from last Don's table rnw-pmt
	 */
	public List<ArenewPermits> getplist(ArenewEntity entity, String id, String ryear, List<ArenewPermits> rplist, List<ArenewOtherpermits> nplist, Session session) {
		String msna = null;
		boolean found = false;
		List<ArenewPermits> plist = new ArrayList<ArenewPermits>();
		if (!(rplist == null)) {
			plist.addAll(rplist);
		}		
		StringBuffer myquery1 = new StringBuffer(
				"select op.id.fisheryCode, op.id.permitSerialNumber, COALESCE(op.id.permitAdfgNumber, 'N/A'), "
				+ "op.id.FLdesc, oe.id.XFilsname, COALESCE(op.id.XNote, 'No Notes Available'), op.id.permitYear, op.id.XRenewalYear, op.id.FRfee, "
				+ "op.id.FPfee, op.id.XDifferential, op.id.XMsna, op.id.permitType, op.id.permitFeeType, op.id.renewalStatus, "
				+ "COALESCE(op.id.confirmationCode, 'N/A'), op.id.permitHolderFileNumber, op.id.XMultipmts from "
				+ "RnwPmt op, RnwEntity oe where op.id.permitHolderFileNumber = '" + id + "' and op.id.permitHolderFileNumber = "
				+ "oe.id.permitHolderFileNumber and op.id.XRenewalYear = '" + ryear + "' and op.id.XRenewalYear = oe.id.XRenewalYear "
				+ "order by substr(op.id.fisheryCode, 1, 1), substr(op.id.fisheryCode, 4, 2), op.id.permitYear asc");
		List list1 = session.createQuery(myquery1.toString()).setFirstResult(0).list();
		if (list1.size() > 0) {
			int curyear = 0;
			int thisyear = 0;
			String fishery = null;
			String xnotes = null;
			boolean old = false;
			boolean normal = true;
			for (Iterator i = list1.iterator(); i.hasNext();) {				
				Object[] rs1 = (Object[]) i.next();
				curyear = Integer.parseInt(ryear);
				thisyear = Integer.parseInt(rs1[6].toString());
				if (rs1[5].toString().startsWith("If you landed less than 8000 lbs of halibut")) {
					xnotes = "If you landed less than 8000 lbs of halibut in "+(curyear-1)+" OR you are a member of one of the Western AK CDQ halibut" +
							" groups (NSEDC, YDFDA, CVRF, CBSFA, APICDA, BBEDC), select lower fee option";
				} else {
					xnotes = rs1[5].toString();
				}
				/*
				 * When we create a new years permit table, usually sometime in early November, it will be possible for permit
				 * holders to have 3 years of renewal fees (current calendar year, previous calendar year AND the new permit year
				 * tables that were just created for the next calendar year). 
				 * 
				 * Per Yvonne, we don't want people to be able to renew all 3 years in this short time period, thus the following code.
				 * 
				 * At the new year, licensing will revoke those remaining and they will disappear from rnw_pmt and we won't have to worry
				 * about it.
				 * 
				 * I'm not sure this is the 'right' way to handle this. Regulations seem to indicate that a person can in fact rewnew
				 * all 3 years (in this situation) during the time between cutoff and the new year as we are STILL in the correct
				 * calendar year.
				 * 
				 * Anyway, the following code will discover those situations (usually around 300 or so people) and tag each affected
				 * permit as non-renewable with a note to call licensing staff for assistance.
				 */
				
				/*
				 * If the permit year is the same as renewal year OR within 2 years.
				 */
				if ( (curyear - thisyear) <= 2) {
					ArenewPermits pmt = new ArenewPermits();
					ArenewPermitsId pmtId = new ArenewPermitsId();
					pmtId.setCfecid(id);
					pmtId.setFishery(rs1[0].toString());
					pmtId.setPyear(rs1[6].toString());
					pmtId.setRyear(rs1[7].toString());
					pmtId.setSerial(rs1[1].toString());
					pmt.setId(pmtId);
					String descript = (((String) rs1[3]).replace("'", ""));
					pmt.setAdfg((String) rs1[2]);
					if (pmt.getAdfg().equalsIgnoreCase("00000")) {
						pmt.setAdfg("N/A");
					}
					pmt.setDescription(descript);					
					/*
					 * If the permit year is 2 years old from current renewal year.
					 */
					if ((curyear - thisyear) == 2) {
						old = true;
						fishery = rs1[0].toString();
						normal = true;
						//normal = false;
					/*
					 * If we are currently iterating through a set of 2 year non-nenewed permits
					 */
					} else if (old == true && rs1[0].toString().equalsIgnoreCase(fishery)) {
						old = true;
						fishery = rs1[0].toString();
						normal = true;
						//normal = false;
					/*
					 * Nope, just a normal permit
					 */
					} else {
						old = false;
						fishery = rs1[0].toString();
						normal = true;
					}					
					/*
					 * standard processing for normal permits (current permit year AND permit year - 1)
					 */
					if (normal) {
						if (xnotes.substring(0, 14).equalsIgnoreCase("Fishery Closed")) {
							pmt.setStatus("Waived");
							pmt.setNotes(xnotes);
							pmt.setReducedfee(false);
							pmt.setHalibut(false);
							pmt.setSablefish(false);
						} else if (rs1[14].toString().equalsIgnoreCase("R")) {
							pmt.setStatus("Rejected");
							pmt.setNotes(xnotes);
							pmt.setReducedfee(false);
							pmt.setHalibut(false);
							pmt.setSablefish(false);
						} else if (rs1[14].toString().equalsIgnoreCase("A")) {
							pmt.setStatus("Available");
							pmt.setNotes(xnotes);
							pmt.setReducedfee(false);
							pmt.setHalibut(false);
							pmt.setSablefish(false);
						} else if (rs1[14].toString().equalsIgnoreCase("P")) {
							pmt.setStatus("Pending");
							pmt.setNotes("<span class='regred10'>Permit already renewed</span>");
							pmt.setReducedfee(false);
							pmt.setHalibut(false);
							pmt.setSablefish(false);
						} else {
							pmt.setStatus("Completed");
							pmt.setNotes("<span class='regred10'>Permit already renewed</span>");
							if (!(rs1[13] == null)) {
								if (rs1[13].toString().equalsIgnoreCase("O") || rs1[13].toString().equalsIgnoreCase("M")) {
									pmt.setReducedfee(true);
									if (pmt.getId().getFishery().substring(0, 1).equalsIgnoreCase("C")) {
										pmt.setHalibut(false);
										pmt.setSablefish(true);
									} else {
										pmt.setHalibut(true);
										pmt.setSablefish(false);
									}
								} else {
									pmt.setReducedfee(false);
								}
							} else {
								pmt.setReducedfee(false);
							}
						}
						String sfee = rs1[8].toString();
						String sfeePov = rs1[9].toString();
						String dfee = sfee;
						String dfeePov = sfeePov;
						pmt.setFee(dfee);
						pmt.setOfee(dfee);
						pmt.setPfee(dfeePov);
						pmt.setOpfee(dfeePov);
						String smsna = rs1[11].toString();
						pmt.setMsna(smsna);
						pmt.setIntend(false);
						pmt.setRenewed(false);
						pmt.setNewpermit(false);
						pmt.setAlready(false);
						pmt.setPovertyfee(false);
						// Not sure I am handling this correctly
						if (!(msna == null)) {
							if (Integer.parseInt(pmt.getMsna()) > 0) {
								if (!(pmt.getId().getFishery().substring(4, 5)
										.equalsIgnoreCase(msna))) {
									pmt.setNointend(true);
								} else {
									pmt.setNointend(false);
								}
							} else {
								pmt.setNointend(false);
							}
						} else {
							pmt.setNointend(false);
						}
						pmt.setType(rs1[12].toString().toUpperCase().trim());
						pmt.setConfirmcode((String) rs1[15]);
						pmt.setMpmt((String) rs1[17]);
						if (!(rplist == null)) {
							ArenewPermits permit = null;
							for (Iterator p = rplist.iterator(); p.hasNext();) {
								found = false;
								permit = (ArenewPermits) p.next();
								if (permit.getId().getSerial().equalsIgnoreCase(pmt.getId().getSerial())) {
									if (permit.getId().getPyear().equalsIgnoreCase(pmt.getId().getPyear())) {
										if (pmt.getStatus().equalsIgnoreCase("C")) {
											permit.setStatus("Completed");
										}
										if (permit.getIntends().equalsIgnoreCase("True")) {
											permit.setIntend(true);
										} else {
											permit.setIntend(false);
										}
										if (permit.getNointends().equalsIgnoreCase("True")) {
											permit.setNointend(true);
										} else {
											permit.setNointend(false);
										}
										found = true;
										break;
									}
								}
							}
							if (!(found)) {
								plist.add(pmt);
								//System.out.println(pmt.getId().getFishery() + " - " + pmt.getStatus());
								found = false;
							} else {
								//plist.add(permit);
								found = false;
							}
						}
						else {
							plist.add(pmt);
							//System.out.println(pmt.getId().getFishery() + " - " + pmt.getStatus());
						}
					/*
					 * This permit is part of a set that includes a non paid fee from 2 years.
					 */
					} else {
						if (rs1[14].toString().equalsIgnoreCase("C")) {
							pmt.setStatus("Completed");
							pmt.setNotes("<span class='regred10'>Permit already renewed</span>");
							if (!(rs1[13] == null)) {
								if (rs1[13].toString().equalsIgnoreCase("O") || rs1[13].toString().equalsIgnoreCase("M")) {
									pmt.setReducedfee(true);
									if (pmt.getId().getFishery().substring(0, 1).equalsIgnoreCase("C")) {
										pmt.setHalibut(false);
										pmt.setSablefish(true);
									} else {
										pmt.setHalibut(true);
										pmt.setSablefish(false);
									}
								} else {
									pmt.setReducedfee(false);
								}
							} else {
								pmt.setReducedfee(false);
							}
						} else {
							pmt.setStatus("Action");
							pmt.setNotes("<span class='regred10'>Please call our office @ 907-789-6150</span>");
							if (!(rs1[13] == null)) {
								if (rs1[13].toString().equalsIgnoreCase("O") || rs1[13].toString().equalsIgnoreCase("M")) {
									pmt.setReducedfee(true);
									if (pmt.getId().getFishery().substring(0, 1).equalsIgnoreCase("C")) {
										pmt.setHalibut(false);
										pmt.setSablefish(true);
									} else {
										pmt.setHalibut(true);
										pmt.setSablefish(false);
									}
								} else {
									pmt.setReducedfee(false);
								}
							} else {
								pmt.setReducedfee(false);
							}
						}
						String sfee = rs1[8].toString();
						String sfeePov = rs1[9].toString();
						String dfee = sfee;
						String dfeePov = sfeePov;
						pmt.setFee(dfee);
						pmt.setOfee(dfee);
						pmt.setPfee(dfeePov);
						pmt.setOpfee(dfeePov);
						String smsna = rs1[11].toString();
						pmt.setMsna(smsna);
						pmt.setIntend(false);
						pmt.setRenewed(false);
						pmt.setNewpermit(false);
						pmt.setAlready(false);
						pmt.setPovertyfee(false);
						if (!(msna == null)) {
							if (Integer.parseInt(pmt.getMsna()) > 0) {
								if (!(pmt.getId().getFishery().substring(4, 5)
										.equalsIgnoreCase(msna))) {
									pmt.setNointend(true);
								} else {
									pmt.setNointend(false);
								}
							} else {
								pmt.setNointend(false);
							}
						} else {
							pmt.setNointend(false);
						}
						pmt.setType(rs1[12].toString().toUpperCase().trim());
						pmt.setConfirmcode((String) rs1[15]);
						pmt.setMpmt((String) rs1[17]);
						if (!(rplist == null)) {
							ArenewPermits permit = null;
							for (Iterator p = rplist.iterator(); p.hasNext();) {
								found = false;
								permit = (ArenewPermits) p.next();
								if (permit.getId().getSerial().equalsIgnoreCase(pmt.getId().getSerial())) {
									if (pmt.getStatus().equalsIgnoreCase("C")) {
										permit.setStatus("Completed");
									}
									if (permit.getIntends().equalsIgnoreCase("True")) {
										permit.setIntend(true);
									} else {
										permit.setIntend(false);
									}
									if (permit.getNointends().equalsIgnoreCase("True")) {
										permit.setNointend(true);
									} else {
										permit.setNointend(false);
									}
									found = true;
									break;
								}
							}
							if (!(found)) {
								plist.add(pmt);
								//System.out.println(pmt.getId().getFishery() + " - " + pmt.getStatus());
								found = false;
							} else {
								//plist.add(permit);
								found = false;
							}
						}
						else {
							plist.add(pmt);
							//System.out.println(pmt.getId().getFishery() + " - " + pmt.getStatus());
						}
					}					
				} 
			}
		}
		return plist;
	}

	/*
	 * This method builds and returns a list of completed and non-complete vessels for this year.
	 * For completed vessels, get the information from arenew_vessels.
	 * For non-completed vessels, get last years record from rnw_ves.
	 */
	public List<ArenewVessels> getvlist(ArenewEntity entity, String id, String ryear, List<ArenewVessels> ovlist, Session session) {
		List<ArenewVessels> vlist = new ArrayList<ArenewVessels>();
		if (!(ovlist == null)) {
			vlist.addAll(ovlist);
		}		
		boolean found = false;
		StringBuffer myquery1 = new StringBuffer(
				"select COALESCE(ov.id.vesselName, 'N/A'), COALESCE(ov.id.coastGuardNumber, 'N/A'), COALESCE(ov.id.adfgNumber, 'N/A'), "
				+ "oe.id.XFilsname, ov.id.FRfee, ov.id.vesselYear, ov.id.yearBuilt, COALESCE(ov.id.make, 'N/A'), COALESCE(ov.id.lengthFeet, '0'), COALESCE(ov.id.lengthInches, '0'), ov.id.lengthVerified, "
				+ "COALESCE(ov.id.grossTons, '0'), COALESCE(ov.id.netTons, '0'), COALESCE(ov.id.homeportCity, 'N/A'), COALESCE(ov.id.homeportState, 'N/A'), COALESCE(ov.id.engineType, 'N/A'), "
				+ "COALESCE(ov.id.horsepower, '0'), COALESCE(ov.id.value, '0'), COALESCE(ov.id.hullType, 'N/A'), COALESCE(ov.id.hullId, 'N/A'), COALESCE(ov.id.fuelCapacity, '0'), COALESCE(ov.id.refrigerationFlag, 'N/A'),"
				+ "COALESCE(ov.id.liveTankCapacity, '0'), COALESCE(ov.id.holdTankCapacity, '0'), COALESCE(ov.id.salmonTrollReg, 'N/A'), COALESCE(ov.id.salmonTrollDate, null), "
				+ "COALESCE(ov.id.purseSeineFlag, 'N'), COALESCE(ov.id.beachSeineFlag, 'N'), COALESCE(ov.id.driftGillnetFlag, 'N'), COALESCE(ov.id.setGillnetFlag, 'N'), COALESCE(ov.id.handTrollFlag, 'N'), COALESCE(ov.id.longlineFlag, 'N'), COALESCE(ov.id.fishwheelFlag, 'N'), "
				+ "COALESCE(ov.id.singleOtterTrawlFlag, 'N'), COALESCE(ov.id.potGearFlag, 'N'), COALESCE(ov.id.ringNetFlag, 'N'), COALESCE(ov.id.beamTrawlFlag, 'N'), COALESCE(ov.id.dredgeFlag, 'N'), "
				+ "COALESCE(ov.id.dinglebarFlag, 'N'), COALESCE(ov.id.jigFlag, 'N'), COALESCE(ov.id.doubleOtterTrawl, 'N'), COALESCE(ov.id.hearingGillnetFlag, 'N'), COALESCE(ov.id.pairTrawlFlag, 'N'), COALESCE(ov.id.otherGearFlag, 'N'), "
				+ "COALESCE(ov.id.fishingBoatFlag, 'N'), COALESCE(ov.id.freezerCannerFlag, 'N'), COALESCE(ov.id.tenderPackerFlag, 'N'), COALESCE(ov.id.transporterFlag, 'N'), COALESCE(ov.id.foreignFlagIndicator, 'N'), COALESCE(ov.id.charterBoatFlag, 'N'), "
				+ "COALESCE(ov.id.diveGearFlag, 'N'), COALESCE(ov.id.powerTrollFlag, 'N'), COALESCE(ov.id.salmonRegArea, 'N/A'), COALESCE(ov.id.permitSerial1, 'N/A'), COALESCE(ov.id.permitSerial2, 'N/A'), "
				+ "ov.id.renewalStatus, COALESCE(ov.id.confirmationCode, 'N/A') from RnwVes ov, RnwEntity oe where  ov.id.permitHolderFileNumber = '" + id + "' and "
				+ "ov.id.permitHolderFileNumber = oe.id.permitHolderFileNumber and ov.id.vesselYear = '"
				+ ryear + "' and ov.id.vesselYear = oe.id.XRenewalYear");
		List list1 = session.createQuery(myquery1.toString()).setFirstResult(0).list();
		if (list1.size() > 0) {
			for (Iterator i = list1.iterator(); i.hasNext();) {
				Object[] rs1 = (Object[]) i.next();
				ArenewVessels ves = new ArenewVessels();
				ArenewVesselsId vesId = new ArenewVesselsId();
				ves.setName((String) rs1[0]);
				ves.setRegNum((String) rs1[1]);
				vesId.setAdfg((String) rs1[2]);
				ves.setStatus("Available");
				String sfee = rs1[4].toString();
				String dfee = sfee;
				ves.setFee(dfee);
				vesId.setRyear(ryear);
				ves.setVyear((String) rs1[5]);
				ves.setYearBuilt(rs1[6].toString());
				ves.setMakeModel(rs1[7].toString());
				ves.setLengthFeet(rs1[8].toString());
				ves.setLengthInches(rs1[9].toString());
				ves.setGrossTons(rs1[11].toString());
				ves.setNetTons(rs1[12].toString());
				ves.setHomeportCity(rs1[13].toString());
				ves.setHomeportState(rs1[14].toString());
				ves.setEngineType(rs1[15].toString());
				ves.setHorsepower(rs1[16].toString());
				ves.setEstValue(rs1[17].toString());
				ves.setHullType(rs1[18].toString());
				ves.setHullId(rs1[19].toString());
				ves.setFuel(rs1[20].toString());
				ves.setRefrigeration(rs1[21].toString());
				ves.setLiveTank(rs1[22].toString());
				ves.setHoldTank(rs1[23].toString());
				ves.setSalmontrollReg(rs1[24].toString());
				if (!(rs1[25] == null)) {
					SimpleDateFormat vesdateformat = new SimpleDateFormat("yyyy-MM-dd");
					String strDate = vesdateformat.format(rs1[25]);
					if (!(strDate.substring(0, 4).equalsIgnoreCase(ryear))) {
						strDate = ryear + strDate.substring(4, 10);
					}
					ves.setSalmontrollDate(strDate);
				}
				ves.setPurseseine(rs1[26].toString());
				ves.setBeachseine(rs1[27].toString());
				ves.setDriftgillnet(rs1[28].toString());
				ves.setSetgillnet(rs1[29].toString());
				ves.setHandtroll(rs1[30].toString());
				ves.setLongline(rs1[31].toString());
				ves.setFishwheel(rs1[32].toString());
				ves.setSingleottertrawl(rs1[33].toString());
				ves.setPotgear(rs1[34].toString());
				ves.setRingnet(rs1[35].toString());
				ves.setBeamtrawl(rs1[36].toString());
				ves.setDredge(rs1[37].toString());
				ves.setDinglebar(rs1[38].toString());
				ves.setJig(rs1[39].toString());
				ves.setDoubleottertrawl(rs1[40].toString());
				ves.setHearinggillnet(rs1[41].toString());
				ves.setPairtrawl(rs1[42].toString());
				ves.setOthergear(rs1[43].toString());
				ves.setFishingboat(rs1[44].toString());
				ves.setFreezerCanner(rs1[45].toString());
				ves.setTenderPacker(rs1[46].toString());
				ves.setTransporter(rs1[47].toString());
				ves.setFoerignFlag(rs1[48].toString());
				ves.setDivegear(rs1[50].toString());
				ves.setPowertroll(rs1[51].toString());
				ves.setSalmonregArea(rs1[52].toString());
				ves.setPermitSerial1(rs1[53].toString());
				ves.setPermitSerial2(rs1[54].toString());
				vesId.setCfecid(id);
				ves.setConfirmcode(rs1[56].toString());
				ves.setId(vesId);
				ves.setName((String) rs1[0]);
				ves.setRegNum((String) rs1[1]);
				vesId.setAdfg((String) rs1[2]);
				if (rs1[55].toString().equalsIgnoreCase("C")) {
					ves.setStatus("Completed");
					ves.setRenewed(true);
					ves.setDetails("<span class='regred10'>Vessel already licensed</span>");
				} else if (rs1[55].toString().equalsIgnoreCase("P")) {
					ves.setStatus("Pending");
					ves.setRenewed(true);
					ves.setDetails("<span class='regred10'>Vessel already licensed</span>");
				} else {
					ves.setStatus("Available");
					ves.setRenewed(false);
					// ves.setDetails("<a href='#' style='text-decoration:none'>'Double Click' here to edit Vessel ("+rs1[2]+") details</a>");
					ves.setDetails("<span class='regblue10' style='cursor: pointer; cursor: hand;'>Click here to edit vessel</span>");
				}
				if (!(ovlist == null)) {
					ArenewVessels vessel = null;
					for (Iterator v = ovlist.iterator(); v.hasNext();) {
						found = false;
						vessel = (ArenewVessels) v.next();
						if (vessel.getId().getAdfg().equalsIgnoreCase(ves.getId().getAdfg())) {
							if (ves.getStatus().equalsIgnoreCase("Completed")) {
								vessel.setStatus("Completed");
							}
							found = true;
							break;
						}
					}
					if (!(found)) {
						vlist.add(ves);
						found = false;
					} else {
						//vlist.add(vessel);
						found = false;
					}
				} else {
					vlist.add(ves);
				}
			}
		}
		return vlist;
	}

	/*
	 * Builds and returns a single vessel object
	 */
	public ArenewVessels getsingleVessel(String adfg, String ryear, String cfecid, Logging leonLog, getDataImpl dataImpl) {
		ArenewVessels ves = null;
		ArenewVesselsId vesId = null;
		try {
			session = dataImpl.fact.openSession();
			StringBuffer myquery1 = new StringBuffer(
					"select COALESCE(ov.vessel_name, 'N/A'), COALESCE(ov.coast_guard_number, 'N/A'), COALESCE(ov.adfg_number, 'N/A'), "
					+ "oe.persons_name, 'fee', ov.vessel_year, ov.year_built, COALESCE(ov.make, 'N/A'), COALESCE(ov.length_feet, '0'), COALESCE(ov.length_inches, '0'), ov.length_verified, "
					+ "COALESCE(ov.gross_tons, '0'), COALESCE(ov.net_tons, '0'), COALESCE(ov.homeport_city, 'N/A'), COALESCE(ov.homeport_state, 'N/A'), COALESCE(ov.engine_type, 'N/A'), "
					+ "COALESCE(ov.horsepower, '0'), COALESCE(ov.value, '0'), COALESCE(ov.hull_type, 'N/A'), COALESCE(ov.hull_id, 'N/A'), COALESCE(ov.fuel_capacity, '0'), COALESCE(ov.refrigeration_flag, 'N/A'),"
					+ "COALESCE(ov.live_tank_capacity, '0'), COALESCE(ov.hold_tank_capacity, '0'), COALESCE(ov.salmon_troll_reg, 'N/A'), COALESCE(ov.salmon_troll_date, null), "
					+ "COALESCE(ov.purse_seine_flag, 'N'), COALESCE(ov.beach_seine_flag, 'N'), COALESCE(ov.drift_gillnet_flag, 'N'), COALESCE(ov.set_gillnet_flag, 'N'), COALESCE(ov.hand_troll_flag, 'N'), COALESCE(ov.longline_flag, 'N'), COALESCE(ov.fishwheel_flag, 'N'), "
					+ "COALESCE(ov.single_otter_trawl_flag, 'N'), COALESCE(ov.pot_gear_flag, 'N'), COALESCE(ov.ring_net_flag, 'N'), COALESCE(ov.beam_trawl_flag, 'N'), COALESCE(ov.dredge_flag, 'N'), "
					+ "COALESCE(ov.dinglebar_flag, 'N'), COALESCE(ov.jig_flag, 'N'), COALESCE(ov.double_otter_trawl, 'N'), COALESCE(ov.hearing_gillnet_flag, 'N'), COALESCE(ov.pair_trawl_flag, 'N'), COALESCE(ov.other_gear_flag, 'N'), "
					+ "COALESCE(ov.fishing_boat_flag, 'N'), COALESCE(ov.freezer_canner_flag, 'N'), COALESCE(ov.tender_packer_flag, 'N'), 'transporter', COALESCE(ov.foreign_flag_indicator, 'N'), COALESCE(ov.charter_boat_flag, 'N'), "
					+ "COALESCE(ov.dive_gear_flag, 'N'), COALESCE(ov.power_troll_flag, 'N'), COALESCE(ov.salmon_reg_area, 'N/A'), COALESCE(ov.permit_serial_1, 'N/A'), COALESCE(ov.permit_serial_2, 'N/A'), "
					+ "'renewalStatus', 'confirmcode', ov.owner_file_number from tier2.vess_2300_view ov, tier2.people_view oe where ov.adfg_number = '" + adfg + "' and "
					+ "ov.owner_id = oe.id_number order by ov.vessel_year desc");
			List list1 = session.createNativeQuery(myquery1.toString()).setFirstResult(0).list();
			/*
			 * Vessel does exist, build and return a new vessel object
			 */
			if (list1.size() > 0) {
				for (Iterator i = list1.iterator(); i.hasNext();) {
					Object[] rs1 = (Object[]) i.next();
					ves = new ArenewVessels();
					vesId = new ArenewVesselsId();
					ves.setName((String) rs1[0]);
					ves.setRegNum((String) rs1[1]);
					vesId.setAdfg((String) rs1[2]);
					ves.setStatus("Available");
					vesId.setRyear(ryear);
					/*
					 * If a record exists for current renewal year, this vessel
					 * is already licensed. In that case, set a bogus vessel
					 * year in the retuned object. NOTE: I am checking 2 sources
					 * for this. The vess_2300 view for the licensing system and
					 * also arenew_vessel just in case the vessel was renewed
					 * and has not been entered into the system yet.
					 */
					if (rs1[5].toString().equalsIgnoreCase(ryear)) {
						/*
						 * vessel already exists in vess_2300 for current year,
						 * already licensed.
						 */
						ves.setVyear("9999");
					} else {
						List vesExist = session.createQuery("from ArenewVessels ov where ov.id.adfg = '"+adfg+"' and ov.id.ryear = '"+ryear+"'").list();
						if (vesExist.size() > 0) {
							/*
							 * vessel doesn't exist in vess_2300, but does have
							 * a current year object in arenew_vessels. This
							 * vessel is already licensed, but hasn't been
							 * entered yet OR is between updates.
							 */
							ves.setVyear("9999");
						} else {
							/*
							 * vessel doesn't exist in vess_2300 OR
							 * arenew_vessels for current year. It is not
							 * licensed, so set the vessel_year to current year.
							 */
							ves.setVyear(ryear);
						}						
					}
					ves.setYearBuilt(rs1[6].toString());
					ves.setMakeModel(rs1[7].toString());
					ves.setLengthFeet(rs1[8].toString());
					ves.setLengthInches(rs1[9].toString());
					int vl = Integer.parseInt(ves.getLengthFeet());
					double vfee = 0.0;
					if (vl <= 25) {
						vfee = 24.00;
					} else if (vl <= 50) {
						vfee = 60.00;
					} else if (vl <= 75) {
						vfee = 120.00;
					} else if (vl <= 100) {
						vfee = 225.00;
					} else if (vl <= 125) {
						vfee = 300.00;
					} else if (vl <= 150) {
						vfee = 375.00;
					} else if (vl <= 175) {
						vfee = 450.00;
					} else if (vl <= 200) {
						vfee = 525.00;
					} else if (vl <= 225) {
						vfee = 600.00;
					} else if (vl <= 250) {
						vfee = 675.00;
					} else if (vl <= 275) {
						vfee = 750.00;
					} else if (vl <= 300) {
						vfee = 825.00;
					} else {
						vfee = 900.00;
					}
					ves.setFee(Double.toString(vfee));
					ves.setGrossTons(rs1[11].toString());
					ves.setNetTons(rs1[12].toString());
					ves.setHomeportCity(rs1[13].toString());
					ves.setHomeportState(rs1[14].toString());
					ves.setEngineType(rs1[15].toString());
					ves.setHorsepower(rs1[16].toString());
					ves.setEstValue(rs1[17].toString());
					ves.setHullType(rs1[18].toString());
					ves.setHullId(rs1[19].toString());
					ves.setFuel(rs1[20].toString());
					ves.setRefrigeration(rs1[21].toString());
					ves.setLiveTank(rs1[22].toString());
					ves.setHoldTank(rs1[23].toString());
					ves.setSalmontrollReg(rs1[24].toString());
					if (!(rs1[25] == null)) {
						SimpleDateFormat vesdateformat = new SimpleDateFormat("yyyy-MM-dd");
						String strDate = vesdateformat.format(rs1[25]);
						if (!(strDate.substring(0, 4).equalsIgnoreCase(ryear))) {
							strDate = ryear + strDate.substring(4, 10);
						}
						// ves.setSalmontrollDate(rs1[25].toString());
						ves.setSalmontrollDate(strDate);
					}
					ves.setPurseseine(rs1[26].toString());
					ves.setBeachseine(rs1[27].toString());
					ves.setDriftgillnet(rs1[28].toString());
					ves.setSetgillnet(rs1[29].toString());
					ves.setHandtroll(rs1[30].toString());
					ves.setLongline(rs1[31].toString());
					ves.setFishwheel(rs1[32].toString());
					ves.setSingleottertrawl(rs1[33].toString());
					ves.setPotgear(rs1[34].toString());
					ves.setRingnet(rs1[35].toString());
					ves.setBeamtrawl(rs1[36].toString());
					ves.setDredge(rs1[37].toString());
					ves.setDinglebar(rs1[38].toString());
					ves.setJig(rs1[39].toString());
					ves.setDoubleottertrawl(rs1[40].toString());
					ves.setHearinggillnet(rs1[41].toString());
					ves.setPairtrawl(rs1[42].toString());
					ves.setOthergear(rs1[43].toString());
					ves.setFishingboat(rs1[44].toString());
					ves.setFreezerCanner(rs1[45].toString());
					ves.setTenderPacker(rs1[46].toString());
					// TODO figure out Transporter value!!!!
					// ves.setTransporter(rs1[47].toString());
					ves.setFoerignFlag(rs1[48].toString());
					ves.setDivegear(rs1[50].toString());
					ves.setPowertroll(rs1[51].toString());
					ves.setSalmonregArea(rs1[52].toString());
					ves.setPermitSerial1(rs1[53].toString());
					ves.setPermitSerial2(rs1[54].toString());
					vesId.setCfecid(cfecid);
					// ves.setConfirmcode(rs1[56].toString());
					ves.setId(vesId);
					ves.setStatus("Available");
					ves.setRenewed(false);
					ves.setDetails("<span class='regblue10' style='cursor: pointer; cursor: hand;'>Click here to edit vessel</span>");
					break;
				}
			} else {
				/*
				 * Vessel does not exist. Populate key with zeros and return
				 * empty object.
				 */
				ves = new ArenewVessels();
				vesId = new ArenewVesselsId();
				vesId.setAdfg("00000");
				vesId.setCfecid("000000");
				vesId.setRyear("0000");
				ves.setVyear("0000");
				ves.setId(vesId);
			}
		} catch (Exception ex) {
			notify.ProcessMajorError(ex, cfecid, "com.cfecweb.leon.server.GetReview.getsingleFishery()");
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
		return ves;
	}
	
	/*
	 * Gets and returns current renewal year
	 */
	public String getRyear(getDataImpl dataImpl) {
		String renewalYear = null;
		List results = null;
		try {
			session = dataImpl.fact.openSession();
			results = session.createNativeQuery("select py.year from tier2.TBL_PERMITYEAR py").setFirstResult(0).list();
			renewalYear = (String) results.iterator().next();
		} catch (Exception ex) {
			notify.ProcessMajorError(ex, "unknown", "com.cfecweb.leon.server.GetReview.getRyear()");
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
		return renewalYear;
	}

	/*
	 * Builds and returns a fishery table object
	 */
	@SuppressWarnings("unused")
	public List<GWTfisheryTable> getFisheryTable(String cfecid, String res, String pov, String yr, Logging leonLog, List<ArenewPermits> pmt, getDataImpl dataImpl) {
		List results = null;
		boolean pmtfound = false;
		List<GWTfisheryTable> list = new ArrayList<>();
		int myFee = 0;
		String tf = null;
		String mf = null;
		try {
			session = dataImpl.fact.openSession();
			String myquery = "select ft.id.fishery, ft.id.resfee, ft.id.nonresfee, ft.id.respovertyfee, ft.id.nonrespovertyfee, "
					+ "ft.id.description, ft.id.status, ft.id.year from RenewFishery ft where ft.id.year = '"+yr+"' and ft.id.fishery not in"
					+ "(select op.id.fisheryCode from RnwPmt op where op.id.permitHolderFileNumber = '" + cfecid.toUpperCase()
					+ "' and op.id.XRenewalYear = '" + yr + "') and ft.id.fishery not in"
					+ "(select np.id.fishery from ArenewPermits np where np.id.cfecid = '" + cfecid.toUpperCase()
					+ "' and np.id.ryear = '" + yr + "') and " + "length(ft.id.fishery) = 5 and ft.id.fishery not in ('G 77Z')";
			//System.out.println(myquery);
			results = session.createQuery(myquery.toString()).setFirstResult(0).list();
			for (Iterator i = results.iterator(); i.hasNext();) {
				Object[] rs = (Object[]) i.next();
				GWTfisheryTable ft = new GWTfisheryTable();
				if (((String) rs[6]).equalsIgnoreCase("N")) {
					ft.setFishery((String) rs[0]);
					ft.setResfee((String) rs[1]);
					ft.setNonresfee((String) rs[2]);
					ft.setPovfee((String) rs[3]);
					ft.setUnderfee((String) rs[4]);
					ft.setDescription((String) rs[5]);
					ft.setStatus((String) rs[6]);
					if (res.equalsIgnoreCase("resident")) {
						if (pov.equalsIgnoreCase("false")) {
							myFee = Integer.parseInt((String) rs[1]);
							ft.setMyfee(Integer.toString(myFee));
						} else {
							myFee = Integer.parseInt((String) rs[3]);
							ft.setMyfee(Integer.toString(myFee));
						}
					} else {
						if (pov.equalsIgnoreCase("false")) {
							myFee = Integer.parseInt((String) rs[2]);
							ft.setMyfee(Integer.toString(myFee));
						} else {
							myFee = Integer.parseInt((String) rs[3]);
							ft.setMyfee(Integer.toString(myFee));
						}
					}
					/*
					 * for the time being, I am taking out these rules. 01-01-2018
					 */
					tf = ft.getFishery().substring(0, 1);
					for (int x = 0; x < pmt.size(); x++) {
						if (ft.getFishery().equalsIgnoreCase(pmt.get(x).getId().getFishery())) {
							pmtfound = true;
							break;
						} /*else if (pmt.get(x).getId().getFishery().substring(2, 4).equalsIgnoreCase("06")) {
							mf = pmt.get(x).getId().getFishery().substring(0, 1);
							if (mf.equalsIgnoreCase(tf)) {
								if (ft.getFishery().substring(2, 4).equalsIgnoreCase("61")) {
									pmtfound = true;
									break;
								}
							}
						} else if (pmt.get(x).getId().getFishery().substring(2, 4).equalsIgnoreCase("09")) {
							mf = pmt.get(x).getId().getFishery().substring(0, 1);
							if (mf.equalsIgnoreCase(tf)) {
								if (ft.getFishery().substring(2, 4).equalsIgnoreCase("91")) {
									pmtfound = true;
									break;
								}
							}
						} else if (pmt.get(x).getId().getFishery().substring(2, 4).equalsIgnoreCase("61")) {
							mf = pmt.get(x).getId().getFishery().substring(0, 1);
							if (mf.equalsIgnoreCase(tf)) {
								if (ft.getFishery().substring(2, 4).equalsIgnoreCase("06")) {
									pmtfound = true;
									break;
								}
							}
						} else if (pmt.get(x).getId().getFishery().substring(2, 4).equalsIgnoreCase("91")) {
							mf = pmt.get(x).getId().getFishery().substring(0, 1);
							if (mf.equalsIgnoreCase(tf)) {
								if (ft.getFishery().substring(2, 4).equalsIgnoreCase("09")) {
									pmtfound = true;
									break;
								}
							}
						}*/
					}
					if (!(pmtfound)) {
						list.add(ft);
						pmtfound = false;
					} else {
						pmtfound = false;
					}
				}
			}
		} catch (Exception ex) {
            ex.printStackTrace();
            leonLog.log(ex.getMessage());
			notify.ProcessMajorError(ex, cfecid, "com.cfecweb.leon.server.GetReview.getFisheryTable()");
            // TODO: why execution is not interrupted here? returns an empty list.
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
		return list;
	}
	
	public boolean checkDifferential(String filenum, String pyear) {
		boolean found = false;
		//System.out.println("select op.fee_paid_date, op.fee_paid_flag, op.permit_fee_type from PMT_1300 op where op.permit_holder_file_number = '" + filenum + "' " +
		//		"and op.permit_year = '" + pyear + "'");
		List list = session.createNativeQuery("select op.fee_paid_date, op.fee_paid_flag, op.permit_fee_type from tier2.PMT_1300 op where op.permit_holder_file_number = '" + filenum + "' " +
				"and op.permit_year = '" + pyear + "'").setFirstResult(0).list();
		if (!(list == null)) {
			if (list.size() == 1) {
				for (Iterator i = list.iterator(); i.hasNext();) {
					Object[] rs1 = (Object[]) i.next();
					if (!((Date) rs1[0] == null)) {
						found = true;
						//System.out.println(filenum + " - Found a paid differential for year " + pyear);
					} else {
						found = false;
					}
				}
			} else {
				for (Iterator i = list.iterator(); i.hasNext();) {
					Object[] rs1 = (Object[]) i.next();
					if (!((Date) rs1[0] == null)) {
						if (rs1[1].toString().equalsIgnoreCase("P")) {
							found = true;
							//System.out.println(filenum + " - Found a paid differential for year " + pyear);
							break;
						}						
					} else {
						found = false;
					}
				}
			}			
		} else {
			found = false;
		}	
		return found;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
}
