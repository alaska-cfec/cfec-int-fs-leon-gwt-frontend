package com.cfecweb.leon.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cfecweb.leon.AppProperties;
import com.cfecweb.leon.dto.FeeTotals;
import com.cfecweb.leon.config.HibernateSessionFactoryProvider;
import com.cfecweb.leon.dto.ClientPaymentContext;
import com.cfecweb.leon.dto.PaymentProcessingContextAndFields;
import com.cfecweb.leon.dto.UserSessionSettings;
import com.cfecweb.leon.mappers.ArenewChangesMapper;
import com.cfecweb.leon.mappers.ArenewEntityMapper;
import com.cfecweb.leon.mappers.ArenewPermitsMapper;
import com.cfecweb.leon.mappers.ArenewVesselsMapper;
import com.cfecweb.leon.mappers.GWTfisheryTableMapper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.hibernate.SessionFactory;

import com.cfecweb.leon.client.getData;
import com.cfecweb.leon.dto.ArenewChanges;
import com.cfecweb.leon.dto.ArenewEntity;
import com.cfecweb.leon.dto.ArenewPayment;
import com.cfecweb.leon.dto.ArenewPermits;
import com.cfecweb.leon.dto.ArenewVessels;
import com.cfecweb.leon.dto.GWTfisheryTable;

/*
 * This is the server side remote service for RCP calls. I try to keep the methods
 * as stubs, then do the work in another class, keeps it clean that way. All RCP calls
 * from the client must go through this file first.
 */

@SuppressWarnings({"rawtypes", "unchecked"})
public class getDataImpl extends RemoteServiceServlet implements getData {
	private static final long serialVersionUID = 1L;
	GetVitals gv = new GetVitals();
	ProcessOrder go = new ProcessOrder();
	PervasiveVesCheck pvc = new PervasiveVesCheck();
	Notify notify = new Notify();
	public Logging leonLog = new Logging();	
	private final int timeInMinutes = 20;
	private static String leonproplocation = "/home/tomcat/properties/leon.properties";
	//private static String leonproplocation = "C:\\home\\tomcat\\properties\\leonWin.properties";
		
	public static Properties leonprop = new Properties();
		
	public final SessionFactory fact = HibernateSessionFactoryProvider.getSessionFactory();
	public getDataImpl() {
	    try {
			leonprop.load(new FileInputStream(leonproplocation));
	        // Printing the properties
	        System.out.println("Printing leonprop properties:");
	        for (String key : leonprop.stringPropertyNames()) {
	            String value = leonprop.getProperty(key);
	            System.out.println(key + ": " + value);
	        }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 *  Passes CFECID and return list of FisheryTable objects(non-Javadoc)
	 * @see com.cfecweb.leon.client.getData#getfshytable(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List)
	 */
    @Override
	public List<GWTfisheryTable> getfshytable(String id, String res, String pov, String yr, List<ArenewPermits> pmt) {
        List<com.cfecweb.leon.shared.ArenewPermits> sPmt = ArenewPermitsMapper.INSTANCE.toSharedList(pmt);
		List<com.cfecweb.leon.shared.GWTfisheryTable> results = gv.getFisheryTable(id, res, pov, yr, leonLog, sPmt, this);
		return GWTfisheryTableMapper.INSTANCE.toDtoList(results);
	}

	/*
	 *  Passes Strings for CFECID and required initial options, returns something(non-Javadoc)
	 * @see com.cfecweb.leon.client.getData#getVitals(java.lang.String, java.lang.String, boolean, boolean)
	 */
    @Override
	public ArenewEntity getVitals(String id, String ryear, boolean option, boolean poverty) {
        com.cfecweb.leon.shared.ArenewEntity ent = gv.getAddress(id, ryear, option, poverty, leonLog, this);
		return ArenewEntityMapper.INSTANCE.toDto(ent);
	}

    // Forms prerequisites for order processing using Secure Acceptance Hosted Checkout (CyberSource)
    @SuppressWarnings("unused")
    @Override
    public PaymentProcessingContextAndFields createOrderProcessingPrerequisites(ArenewEntity ent, ArenewPayment pay, List<ArenewChanges> chg, List<ArenewPermits> plist, List<ArenewVessels> vlist,
                                                                                List<ArenewPermits> pclist, List<ArenewVessels> vclist,
                                                                                boolean halred, boolean sabred, FeeTotals feeTotals, boolean firstTime, String ryear, String pmtvesCount, String topLeftText,
                                                                                String captchaToken
                                                                                ) {
        HttpServletRequest req = getThreadLocalRequest();
        return go.createOrderProcessingPrerequisites(ent, pay, chg, this, leonLog, plist, vlist, pclist, vclist,
                halred, sabred, feeTotals, firstTime, ryear, pmtvesCount, topLeftText, captchaToken, req.getRemoteAddr(), leonprop);
    }

	/*
	 * Passes various LEON objects after final selection and processing button to a method that will complete the DB work and charge the CC, if automatic.
	 * @see com.cfecweb.leon.client.getData#processOrder(com.cfecweb.leon.shared.ArenewEntity, com.cfecweb.leon.shared.ArenewPayment, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List)
	 */
	@SuppressWarnings("unused")
    @Override
	public ClientPaymentContext processOrder(String ref) {
        try {
            return go.finalizeOrder(ref, this, leonLog, leonprop);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
	}

	/*
	 * Generates necessary CFEC form(s) link for user wishing to download standard and/or pre-printed forms
	 * @see com.cfecweb.leon.client.getData#getForms(java.lang.String, java.lang.String)
	 */
    @Override
	public List<String> getForms(String id, String ryear) {
		String thisInFileDir = leonprop.getProperty("LEON.tmpfiledir.Location", "/webapps/tmpfiles/in");
		String thisOutFileDir = leonprop.getProperty("LEON.hostinfo.OutFileDir", "/leonOut");
		String thisPrePrintsDir = leonprop.getProperty("LEON.hostinfo.PreprintDir", "/webapps/out/LEON/preprints");
		List<String> formlinks = gv.getForms(id, ryear, leonLog, this, thisInFileDir, thisOutFileDir, thisPrePrintsDir);
		return formlinks;
	}

	/*
	 * Receives and processes any changes the user may have made to their records
	 * @see com.cfecweb.leon.client.getData#processChange(java.lang.String, java.lang.String, java.util.List)
	 */
    @Override
	public String processChange(String id, String ryear, List<ArenewChanges> chg) {
        List<com.cfecweb.leon.shared.ArenewChanges> sChg = ArenewChangesMapper.INSTANCE.toSharedList(chg);
		String results = go.processChanges(id, ryear, sChg, this, leonLog);
		return results;
	}

	/*
	 * Sends an email to CFEC with user comments
	 * @see com.cfecweb.leon.client.getData#emailComments(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
    @Override
	public void emailComments(String subject, String body, String to, String from) {
		notify.sendComments(subject, body, to, from, leonLog, this);
	}

	/*
	 * Should be the first RPC call from all UIDef*.onModuleLoad()
	 * 
	 * @return java.lang.Integer (-1 if the user session has already timed out,
	 *         otherwise, the number of milliseconds)
	 */
    @Override
	public UserSessionSettings getUserSessionTimeoutMillis() {
        UserSessionSettings returnObj = null;
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
        String recaptchaSiteKey = AppProperties.get(AppProperties.RECAPTCHA_SITE_KEY);
        String recaptchaAction = AppProperties.get(AppProperties.RECAPTCHA_ACTION);
		//HttpSession session = getThreadLocalRequest().getSession(true);
		if (session.getAttribute("active")!=null) {
			leonLog.log("session id " + session.getId() + " has been re-activated");
			session.setMaxInactiveInterval(timeInMinutes * (60 * 1000));
			/*
			 * get current renewal year from table file view
			 */
			//String renewalYear = gv.getRyear(this);
			String renewalYear = leonprop.getProperty("LEON.licensing.RevenueYear", "2020");
			returnObj = new UserSessionSettings(session.getMaxInactiveInterval(), Integer.parseInt(renewalYear), recaptchaSiteKey, recaptchaAction);
		} else {
			//session = getThreadLocalRequest().getSession();
			session.setAttribute("active", true);
			leonLog.log("session id " + session.getId() + " has been activated");
			session.setMaxInactiveInterval(timeInMinutes * (60 * 1000));
			/*
			 * get current renewal year from table file view
			 */
			//String renewalYear = gv.getRyear(this);
			String renewalYear = leonprop.getProperty("LEON.licensing.RevenueYear", "2020");
			returnObj = new UserSessionSettings(session.getMaxInactiveInterval(), Integer.parseInt(renewalYear), recaptchaSiteKey, recaptchaAction);
		}
		return returnObj;
	}

	/*
	 * Method to kill the current session after 20 minutes of inactivity
	 * @see com.cfecweb.leon.client.getData#killSession()
	 */
    @Override
	public String killSession() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		leonLog.log("session id " + session.getId() + " has been invalidated thru inactivity");
		session.invalidate();
		return null;
	}

	/*
	 * Sorts a permit list and returns a list of objects
	 * @see com.cfecweb.leon.client.getData#sortPlist(java.util.List, java.lang.String)
	 */
    @Override
	public List<ArenewPermits> sortPlist(List<ArenewPermits> plist, String poverty) {
		for (Iterator it = plist.iterator(); it.hasNext();) {
			ArenewPermits ps = (ArenewPermits) it.next();
			if (poverty.equalsIgnoreCase("true")) {
				if (ps.getStatus().equalsIgnoreCase("Available")) {
					ps.setFee(ps.getPfee());
					ps.setOfee(ps.getOpfee());
				} else if (ps.getStatus().equalsIgnoreCase("Waived")) {
					ps.setFee("0.0");
					ps.setOfee("0.0");
				}
			}
		}
		Collections.sort(plist);
		return plist;
	}
	
	/*
	 * Sorts a vessel list and returns a list of objects
	 * @see com.cfecweb.leon.client.getData#sortVlist(java.util.List)
	 */
    @Override
	public List<ArenewVessels> sortVlist(List<ArenewVessels> vlist) {
		Collections.sort(vlist);
		return vlist;
	}

	/*
	 * Returns an object of a specific vessel by ADFG number
	 * @see com.cfecweb.leon.client.getData#getsingleVessel(java.lang.String, java.lang.String, java.lang.String)
	 */
    @Override
	public ArenewVessels getsingleVessel(String adfg, String ryear, String cfecid) {
        com.cfecweb.leon.shared.ArenewVessels ves = gv.getsingleVessel(adfg, ryear, cfecid, leonLog, this);
		return ArenewVesselsMapper.INSTANCE.toDto(ves);
	}
	
	@Override
	public List<ArenewPermits> checkVessel(List<ArenewPermits> plist, String ryear) {
		//System.out.println("got to checkVessel getDataImpl, plist size is " + plist.size() + ", year is " + ryear);
		String btreiveHost = leonprop.getProperty("LEON.hostinfo.Process", "Prod");
        List<com.cfecweb.leon.shared.ArenewPermits> sPlist = ArenewPermitsMapper.INSTANCE.toSharedList(plist);
		List<com.cfecweb.leon.shared.ArenewPermits> results = PervasiveVesCheck.getVessel(ryear, sPlist, btreiveHost, this);
		return ArenewPermitsMapper.INSTANCE.toDtoList(results);
	}
	
	@Override
	public String checkCC(ArenewPayment pay) {
		String results = null;
		System.out.println("Server side CC validity check prior to final processing");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			results = "error";
			e.printStackTrace();
		}
		results = "good";
		return results;
	}

    /// Retrieves the application version string that was loaded from the
    /// server-side `app.properties` file.
    /// This value is injected at build time via Maven resource filtering
    /// and made available through [AppProperties].
    ///
    /// @return the application version, or `"unknown"` if the property
    ///         is not defined or cannot be read
    @Override
    public String getAppVersion() {
        return AppProperties.get(AppProperties.APP_VERSION, "unknown");
    }

}
