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
import com.cfecweb.leon.client.model.FeeTotals;
import com.cfecweb.leon.client.model.ClientPaymentContext;
import com.cfecweb.leon.client.model.PaymentProcessingContextAndFields;
import com.cfecweb.leon.dto.UserSessionSettings;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.cfecweb.leon.client.getData;
import com.cfecweb.leon.client.model.ArenewChanges;
import com.cfecweb.leon.client.model.ArenewEntity;
import com.cfecweb.leon.client.model.ArenewPayment;
import com.cfecweb.leon.client.model.ArenewPermits;
import com.cfecweb.leon.client.model.ArenewVessels;
import com.cfecweb.leon.client.model.GWTfisheryTable;

/*
 * This is the server side remote service for RCP calls. I try to keep the methods
 * as stubs, then do the work in another class, keeps it clean that way. All RCP calls
 * from the client must go through this file first.
 */

@SuppressWarnings({"rawtypes", "unchecked"})
public class getDataImpl extends RemoteServiceServlet implements getData {
	private static final long serialVersionUID = 1L;
	private final int timeInMinutes = 20;
	private static String leonproplocation = "/home/tomcat/properties/leon.properties";
	//private static String leonproplocation = "C:\\home\\tomcat\\properties\\leonWin.properties";
		
	public static Properties leonprop = new Properties();
		
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
        throw new UnsupportedOperationException();
	}

	/*
	 *  Passes Strings for CFECID and required initial options, returns something(non-Javadoc)
	 * @see com.cfecweb.leon.client.getData#getVitals(java.lang.String, java.lang.String, boolean, boolean)
	 */
    @Override
	public ArenewEntity getVitals(String id, String ryear, boolean option, boolean poverty) {
        throw new UnsupportedOperationException();
	}

    // Forms prerequisites for order processing using Secure Acceptance Hosted Checkout (CyberSource)
    @SuppressWarnings("unused")
    @Override
    public PaymentProcessingContextAndFields createOrderProcessingPrerequisites(ArenewEntity ent, ArenewPayment pay, List<ArenewChanges> chg, List<ArenewPermits> plist, List<ArenewVessels> vlist,
                                                                                List<ArenewPermits> pclist, List<ArenewVessels> vclist,
                                                                                boolean halred, boolean sabred, FeeTotals feeTotals, boolean firstTime, String ryear, String pmtvesCount, String topLeftText,
                                                                                String captchaToken
                                                                                ) {
        throw new UnsupportedOperationException();
        /*HttpServletRequest req = getThreadLocalRequest();
        return go.createOrderProcessingPrerequisites(ent, pay, chg, this, leonLog, plist, vlist, pclist, vclist,
                halred, sabred, feeTotals, firstTime, ryear, pmtvesCount, topLeftText, captchaToken, req.getRemoteAddr(), leonprop);*/
    }

	/*
	 * Passes various LEON objects after final selection and processing button to a method that will complete the DB work and charge the CC, if automatic.
	 * @see com.cfecweb.leon.client.getData#processOrder(com.cfecweb.leon.shared.ArenewEntity, com.cfecweb.leon.shared.ArenewPayment, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List)
	 */
	@SuppressWarnings("unused")
    @Override
	public ClientPaymentContext processOrder(String ref) {
        throw new UnsupportedOperationException();
	}

	/*
	 * Generates necessary CFEC form(s) link for user wishing to download standard and/or pre-printed forms
	 * @see com.cfecweb.leon.client.getData#getForms(java.lang.String, java.lang.String)
	 */
    @Override
	public List<String> getForms(String id, String ryear) {
        throw new UnsupportedOperationException();
	}

	/*
	 * Receives and processes any changes the user may have made to their records
	 * @see com.cfecweb.leon.client.getData#processChange(java.lang.String, java.lang.String, java.util.List)
	 */
    @Override
	public String processChange(String id, String ryear, List<ArenewChanges> chg) {
        throw new UnsupportedOperationException();
	}

	/*
	 * Sends an email to CFEC with user comments
	 * @see com.cfecweb.leon.client.getData#emailComments(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
    @Override
	public void emailComments(String subject, String body, String to, String from) {
        throw new UnsupportedOperationException();
	}

	/*
	 * Should be the first RPC call from all UIDef*.onModuleLoad()
	 * 
	 * @return java.lang.Integer (-1 if the user session has already timed out,
	 *         otherwise, the number of milliseconds)
	 */
    @Override
	public UserSessionSettings getUserSessionTimeoutMillis() {
        throw new UnsupportedOperationException();
        /*UserSessionSettings returnObj = null;
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
        String recaptchaSiteKey = AppProperties.get(AppProperties.RECAPTCHA_SITE_KEY);
        String recaptchaAction = AppProperties.get(AppProperties.RECAPTCHA_ACTION);
		//HttpSession session = getThreadLocalRequest().getSession(true);
		if (session.getAttribute("active")!=null) {
			leonLog.log("session id " + session.getId() + " has been re-activated");
			session.setMaxInactiveInterval(timeInMinutes * (60 * 1000));
			// get current renewal year from table file view
			//String renewalYear = gv.getRyear(this);
			String renewalYear = leonprop.getProperty("LEON.licensing.RevenueYear", "2020");
			returnObj = new UserSessionSettings(session.getMaxInactiveInterval(), Integer.parseInt(renewalYear), recaptchaSiteKey, recaptchaAction);
		} else {
			//session = getThreadLocalRequest().getSession();
			session.setAttribute("active", true);
			leonLog.log("session id " + session.getId() + " has been activated");
			session.setMaxInactiveInterval(timeInMinutes * (60 * 1000));
			// get current renewal year from table file view
			//String renewalYear = gv.getRyear(this);
			String renewalYear = leonprop.getProperty("LEON.licensing.RevenueYear", "2020");
			returnObj = new UserSessionSettings(session.getMaxInactiveInterval(), Integer.parseInt(renewalYear), recaptchaSiteKey, recaptchaAction);
		}
		return returnObj;*/
	}

	/*
	 * Method to kill the current session after 20 minutes of inactivity
	 * @see com.cfecweb.leon.client.getData#killSession()
	 */
    @Override
	public String killSession() {
        throw new UnsupportedOperationException();
        /*HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		leonLog.log("session id " + session.getId() + " has been invalidated thru inactivity");
		session.invalidate();
		return null;*/
	}

	/*
	 * Sorts a permit list and returns a list of objects
	 * @see com.cfecweb.leon.client.getData#sortPlist(java.util.List, java.lang.String)
	 */
    @Override
	public List<ArenewPermits> sortPlist(List<ArenewPermits> plist, String poverty) {
        throw new UnsupportedOperationException();
	}
	
	/*
	 * Sorts a vessel list and returns a list of objects
	 * @see com.cfecweb.leon.client.getData#sortVlist(java.util.List)
	 */
    @Override
	public List<ArenewVessels> sortVlist(List<ArenewVessels> vlist) {
        throw new UnsupportedOperationException();
	}

	/*
	 * Returns an object of a specific vessel by ADFG number
	 * @see com.cfecweb.leon.client.getData#getsingleVessel(java.lang.String, java.lang.String, java.lang.String)
	 */
    @Override
	public ArenewVessels getsingleVessel(String adfg, String ryear, String cfecid) {
        throw new UnsupportedOperationException();
	}
	
	@Override
	public List<ArenewPermits> checkVessel(List<ArenewPermits> plist, String ryear) {
        throw new UnsupportedOperationException();
	}
	
	@Override
	public String checkCC(ArenewPayment pay) {
        throw new UnsupportedOperationException();
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
