package com.cfecweb.leon.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import com.cfecweb.leon.AppProperties;
import com.cfecweb.leon.dto.FeeTotals;
import com.cfecweb.leon.dto.ClientPaymentContext;
import com.cfecweb.leon.dto.PaymentProcessingContextAndFields;
import com.cfecweb.leon.mappers.ArenewChangesMapper;
import com.cfecweb.leon.mappers.ArenewEntityMapper;
import com.cfecweb.leon.mappers.ArenewPaymentMapper;
import com.cfecweb.leon.mappers.ArenewPermitsMapper;
import com.cfecweb.leon.mappers.ArenewVesselsMapper;
import com.cfecweb.leon.materializers.ArenewOtherpermitsMaterializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import org.hibernate.query.NativeQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cfecweb.leon.shared.ArenewChanges;
import com.cfecweb.leon.shared.ArenewEntity;
import com.cfecweb.leon.shared.ArenewEntityId;
import com.cfecweb.leon.shared.ArenewOtherpermits;
import com.cfecweb.leon.shared.ArenewOtherpermitsId;
import com.cfecweb.leon.shared.ArenewPayment;
import com.cfecweb.leon.shared.ArenewPermits;
import com.cfecweb.leon.shared.ArenewVessels;

import com.cfecweb.leon.materializers.ArenewChangesMaterializer;
import com.cfecweb.leon.materializers.ArenewEntityMaterializer;
import com.cfecweb.leon.materializers.ArenewPaymentMaterializer;
import com.cfecweb.leon.materializers.ArenewPermitsMaterializer;
import com.cfecweb.leon.materializers.ArenewVesselsMaterializer;

import javax.annotation.Nullable;

import static com.cfecweb.leon.AppProperties.CYBERSOURCE_ACCESS_KEY;
import static com.cfecweb.leon.AppProperties.CYBERSOURCE_PROFILE_ID;
import static com.cfecweb.leon.AppProperties.CYBERSOURCE_REDIRECT_URL;
import static com.cfecweb.leon.AppProperties.CYBERSOURCE_SECRET_KEY;

/*
 * This class contains methods that record the necessary information upon the completion and validation 
 * of a LEON transaction.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ProcessOrder {
    private static final Logger LOGGER = LogManager.getLogger(ProcessOrder.class);
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
    }

    Session session = null;
	ConfirmationSheet cc = new ConfirmationSheet();
	ShortForm sf = new ShortForm();
	VesselReceipt vr = new VesselReceipt();
	InserterForm inf = new InserterForm();
	Notify notify = new Notify();
	LEONencrypt leonEncrypt = new LEONencrypt();
	boolean process;
	boolean proceed;
	boolean returl = false;
	private String url;
	private String url2;
	String ryear = null;
	String lyear = null;

    public static final String CYBERSOURCE_REDIRECT_URL_ENV = "CYBERSOURCE_REDIRECT_URL";
    public static final String CYBERSOURCE_ACCESS_KEY_ENV = "CYBERSOURCE_ACCESS_KEY";
    public static final String CYBERSOURCE_PROFILE_ID_ENV = "CYBERSOURCE_PROFILE_ID";
    public static final String CYBERSOURCE_SECRET_KEY_ENV = "CYBERSOURCE_SECRET_KEY";

    public static void saveClientPaymentContext(File file, ClientPaymentContext ctx) throws IOException {
        // Creates or overwrites the file atomically
        MAPPER.writeValue(file, ctx);
    }

    public static ClientPaymentContext loadClientPaymentContext(File file) throws IOException {
        return MAPPER.readValue(file, ClientPaymentContext.class);
    }

    public static @Nullable String getEnvOrDefault(String key, @Nullable String defaultValue) {
        String env = System.getenv(key);
        return env == null ? defaultValue : env;
    }

    private String getUTCDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new java.util.Date());
    }

    public static Date getDbDate(Session session) {
        /*
         *  grab current date stamp
         */
        Object sqlTimestamp = session.createNativeQuery("SELECT CURRENT_TIMESTAMP").getSingleResult();

        if (sqlTimestamp instanceof java.sql.Timestamp) {
            return new Date(((java.sql.Timestamp) sqlTimestamp).getTime());
        } else if (sqlTimestamp instanceof java.time.LocalDateTime) {
            java.sql.Timestamp ts = java.sql.Timestamp.valueOf((java.time.LocalDateTime) sqlTimestamp);
            return new Date(ts.getTime());
        } else if (sqlTimestamp instanceof java.sql.Date) {
            return new Date(((java.sql.Date) sqlTimestamp).getTime());
        } else if (sqlTimestamp instanceof java.time.Instant) {
            return new Date(((java.time.Instant) sqlTimestamp).toEpochMilli());
        } else {
            throw new IllegalStateException("Unexpected Timestamp type: " + sqlTimestamp.getClass());
        }
        // Now 'utilDate' contains the date without time information
    }

    public PaymentProcessingContextAndFields createOrderProcessingPrerequisites(
            com.cfecweb.leon.dto.ArenewEntity entity, com.cfecweb.leon.dto.ArenewPayment pay,
            List<com.cfecweb.leon.dto.ArenewChanges> chg, getDataImpl dataImpl, final Logging leonLog,
            List<com.cfecweb.leon.dto.ArenewPermits> plist, List<com.cfecweb.leon.dto.ArenewVessels> vlist,
            List<com.cfecweb.leon.dto.ArenewPermits> pclist, List<com.cfecweb.leon.dto.ArenewVessels> vclist,
            boolean halred, boolean sabred, FeeTotals feeTotals, boolean firstTime, String ryear,
            String pmtvesCount, String topLeftText, String captchaToken, String remoteIp,
            Properties leonprop) {
        final String paymentData = leonprop.getProperty("LEON.paymentData.Location", "/webapps/LEON/Prod/PaymentData");

        String cnum = null;

        try {
            RecaptchaVerifier verifier = new RecaptchaVerifier();
            RecaptchaVerifier.VerificationResult reCaptchaResult = verifier.verifyV3(AppProperties.get(AppProperties.RECAPTCHA_SECRET_KEY),
                    captchaToken, AppProperties.get(AppProperties.RECAPTCHA_ACTION), AppProperties.get(AppProperties.RECAPTCHA_HOSTNAME),
                    Double.parseDouble(AppProperties.get(AppProperties.RECAPTCHA_MIN_SCORE)), remoteIp);

            if (!reCaptchaResult.ok()) {
                LOGGER.info("reCAPTCHA verification failed. Result: " + reCaptchaResult);
                throw new RuntimeException("reCAPTCHA verification failed: " + reCaptchaResult.details());
            }

            session = dataImpl.fact.openSession();

            // Generate new Confirmation Number
            // This consist of transaction number, year, random number and cfecid
            Number result = (Number) session.createNativeQuery("select count(distinct confirmcode) from tier2.Arenew_Payment where cfecid = :cfecid " +
                            "and ryear = :ryear")
                    .setParameter("cfecid", entity.getId().getCfecid())
                    .setParameter("ryear", entity.getId().getRyear())
                    .uniqueResult();

            long txCount = result != null ? result.longValue() : 0L;
            txCount = (txCount + 1);
            String txNum = (((txCount < 10) ? "0" : "") + txCount);
            String yr = entity.getId().getRyear().substring(2, 4);
            ryear = yr;
            int tyear = Integer.parseInt(ryear);
            lyear = Integer.toString(tyear - 1);

            long range = (long)99999 - (long)10001 + 1;
            long fraction = (long)(range * new Random().nextDouble());
            int randomNumber = (int)(fraction + 10001);
            String rn = Integer.toString(randomNumber);

            cnum = txNum + yr + "-" + rn + "-" + entity.getId().getCfecid();

            // Form singed fields for the hosted card checkout request
            String redirectUrl = getEnvOrDefault(CYBERSOURCE_REDIRECT_URL_ENV, AppProperties.get(CYBERSOURCE_REDIRECT_URL));
            String accessKey = getEnvOrDefault(CYBERSOURCE_ACCESS_KEY_ENV, AppProperties.get(CYBERSOURCE_ACCESS_KEY));
            String profileId = getEnvOrDefault(CYBERSOURCE_PROFILE_ID_ENV, AppProperties.get(CYBERSOURCE_PROFILE_ID));
            String secretKey = getEnvOrDefault(CYBERSOURCE_SECRET_KEY_ENV, AppProperties.get(CYBERSOURCE_SECRET_KEY));

            Map<String, String> fields = new LinkedHashMap<>();
            fields.put("access_key", accessKey);
            fields.put("profile_id", profileId);
            fields.put("transaction_uuid", UUID.randomUUID().toString());
            fields.put("signed_field_names",
                    "access_key,profile_id,transaction_uuid,signed_field_names,unsigned_field_names," +
                            "signed_date_time,locale,transaction_type,reference_number,amount,currency");
            fields.put("unsigned_field_names", "");
            fields.put("signed_date_time", getUTCDateTime());
            fields.put("locale", "en");
            fields.put("transaction_type", "sale");
            fields.put("reference_number", cnum);
            fields.put("amount", pay.getTotalamount());
            fields.put("currency", "USD");

            String signature = CybersourceSecurity.sign(fields, secretKey);
            fields.put("signature", signature);

            // Save the payment processing context to the file on disk
            File paymentDataFolder = new File(paymentData);
            if (!paymentDataFolder.exists()) { paymentDataFolder.mkdirs(); }

            File paymentDataFile = new File(paymentDataFolder, cnum + ".json");
            StoredPaymentContext storedPaymentContext = new StoredPaymentContext(entity, pay, chg, plist, vlist, pclist, vclist,
                    halred, sabred, feeTotals, firstTime, ryear, pmtvesCount, topLeftText);
            StoredPaymentContext.save(paymentDataFile, storedPaymentContext);

            return new PaymentProcessingContextAndFields(cnum, redirectUrl, fields);
        } catch(Exception e) {
            notify.ProcessMajorError(e, entity.getId().getCfecid(), "com.cfecweb.leon.server.ProcessOrder.ProcessOrder()");
            leonLog.log("error " + e.getMessage());
            e.printStackTrace();

            leonLog.log("AN ERROR HAS OCCURED processing order for CFECID " + entity.getId().getCfecid() + ", an email has been sent to an administrator");
            leonLog.log("The record(s) for CFECID " + entity.getId().getCfecid() + ", confirmation code "+cnum+" HAVE NOT been recorded");

            throw new RuntimeException(e);
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    public String getCardType(@Nullable String cardType) {
        String cardTypeName = cardType;
        try {
            if (cardType != null) {
                int cardTypeId = Integer.parseInt(cardType);
                switch (cardTypeId) {
                    case 1: cardTypeName = "Visa"; break;
                    case 2: cardTypeName = "Mastercard"; break;
                    case 3: cardTypeName = "American Express"; break;
                    case 4: cardTypeName = "Discover"; break;
                    case 5: cardTypeName = "Diners Club"; break; // cards starting with 54 or 55 are rejected.
                    case 6: cardTypeName = "Carte Blanche"; break;
                    case 7: cardTypeName = "JCB"; break;
                    case 14: cardTypeName = "EnRoute"; break;
                    case 21: cardTypeName = "JAL"; break;
                    case 24: cardTypeName = "Maestro UK Domestic"; break;
                    case 31: cardTypeName = "Delta"; break;
                    case 33: cardTypeName = "Visa Electron"; break;
                    case 34: cardTypeName = "Dankort"; break;
                    case 36: cardTypeName = "Carte Bancaire"; break;
                    case 37: cardTypeName = "Carta Si"; break;
                    case 42: cardTypeName = "Maestro International"; break;
                    case 43: cardTypeName = "GE Money UK card"; break;
                    case 50: cardTypeName = "Hipercard"; break; // (sale only)
                    case 54: cardTypeName = "Elo"; break;
                    case 62: cardTypeName = "China UnionPay"; break;
                }
            }
        } catch (NumberFormatException e) {
            LOGGER.warn("Unknown card type: {}", cardType);
        }
        if (cardTypeName == null) { cardTypeName = "null"; }
        return cardTypeName;
    }

    public void fillPaymentFields(com.cfecweb.leon.dto.ArenewPayment dtoPay, StoredPaymentContext spc) {
        try {
            Map<String, String> postParameters = spc.getPostParameters();

            String transaction_id = postParameters.get("transaction_id");
            if (transaction_id != null) { dtoPay.setCurtx(transaction_id); }
            String auth_trans_ref_no = postParameters.get("auth_trans_ref_no");
            if (auth_trans_ref_no != null) { dtoPay.setAuthcode(auth_trans_ref_no); }

            String req_bill_to_address_line1 = postParameters.get("req_bill_to_address_line1");
            if (req_bill_to_address_line1 != null) { dtoPay.setBaddress(req_bill_to_address_line1); }
            String req_bill_to_address_line2 = postParameters.get("req_bill_to_address_line2");
            if (req_bill_to_address_line2 != null) { dtoPay.setBaddress2(req_bill_to_address_line2); }
            String req_bill_to_address_city = postParameters.get("req_bill_to_address_city");
            if (req_bill_to_address_city != null) { dtoPay.setBcity(req_bill_to_address_city); }
            String req_bill_to_address_state = postParameters.get("req_bill_to_address_state");
            if (req_bill_to_address_state != null) { dtoPay.setBstate(req_bill_to_address_state); }
            String req_bill_to_address_postal_code = postParameters.get("req_bill_to_address_postal_code");
            if (req_bill_to_address_postal_code != null) { dtoPay.setBzip(req_bill_to_address_postal_code); }
            String req_bill_to_address_country = postParameters.get("req_bill_to_address_country");
            if (req_bill_to_address_country != null) { dtoPay.setBcountry(req_bill_to_address_country); }

            String req_card_number = postParameters.get("req_card_number");
            if (req_card_number != null) { dtoPay.setCcnumber(req_card_number); }
            String req_bill_to_forename = postParameters.get("req_bill_to_forename");
            if (req_bill_to_forename != null) { dtoPay.setCcname(req_bill_to_forename); }
            String req_bill_to_surname = postParameters.get("req_bill_to_surname");
            if (req_bill_to_surname != null) { dtoPay.setCclname(req_bill_to_surname); }

            String cardExpiryDate = postParameters.get("req_card_expiry_date");
            if (cardExpiryDate != null) {
                String[] parts = cardExpiryDate.split("-");
                if (parts.length == 2) {
                    dtoPay.setCcmonth(parts[0]);
                    dtoPay.setCcyear(parts[1]);
                }
            }

            String signedDateTimeStr = postParameters.get("signed_date_time");
            if (signedDateTimeStr != null) {
                Instant instant = Instant.parse(signedDateTimeStr);
                java.util.Date signedDateTime = Date.from(instant);
                dtoPay.setReceiveddate(signedDateTime);
            }

            String req_card_type = postParameters.get("req_card_type");
            if (req_card_type != null) { dtoPay.setCctype(req_card_type); }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unused"})
	public ClientPaymentContext finalizeOrder(String ref, getDataImpl dataImpl, final Logging leonLog, Properties leonprop) throws IOException {
        final String cnum = ref;

        File paymentDataFolder = new File(leonprop.getProperty("LEON.paymentData.Location", "/webapps/LEON/Prod/PaymentData"));
        File paymentFinalizedFile = new File(paymentDataFolder, ref + ".finalized.json");
        File paymentDataFile = new File(paymentDataFolder, ref + ".processed.json");
        if (!paymentDataFile.exists()) {
            throw new FileNotFoundException("Payment doesn't exist or wasn't processed: " + ref);
        }
        if (paymentFinalizedFile.exists()) {
            // Already finalized
            ClientPaymentContext clientPaymentContext = loadClientPaymentContext(paymentFinalizedFile);
            return clientPaymentContext;
        } else {
            paymentFinalizedFile.createNewFile();
        }

        StoredPaymentContext spc = StoredPaymentContext.load(paymentDataFile);
        String paymentDecision = spc.getPostParameters().get("decision");
        String paymentMessage = spc.getPostParameters().get("message");

        com.cfecweb.leon.dto.ArenewEntity dtoEntity = spc.getEntity();
        com.cfecweb.leon.dto.ArenewPayment dtoPay = spc.getPay();
        List<com.cfecweb.leon.dto.ArenewChanges> dtoChg = spc.getChg();
        List<com.cfecweb.leon.dto.ArenewPermits> dtoPlist = spc.getPlist();
        List<com.cfecweb.leon.dto.ArenewVessels> dtoVlist = spc.getVlist();
        List<com.cfecweb.leon.dto.ArenewPermits> dtoPclist = spc.getPclist();
        List<com.cfecweb.leon.dto.ArenewVessels> dtoVclist = spc.getVclist();

        fillPaymentFields(dtoPay, spc);

		/*
		 * assign method variables for the elements contained in the properties file.
		 * if the properties file cannot be loaded, then declare default values.
		 * This properties file was instantiated in the initial load of getDataImpl
		 */
		final boolean manualGlobal = Boolean.parseBoolean(leonprop.getProperty("LEON.manualtrans.Global", "false"));
		boolean manualRandom = Boolean.parseBoolean(leonprop.getProperty("LEON.manualtrans.Random", "false"));
		final String batchprocess = leonprop.getProperty("LEON.anytrans.Batch", "ANY");
		final String batchjobname = leonprop.getProperty("LEON.anytrans.JobName", "LEON_AUTO.sas");
		final String leonletterOutPDF = leonprop.getProperty("LEON.leonLettersPDF.Location", "/webapps/LEON/Prod/LettersPDF");
		final String shortfileOutPDF = leonprop.getProperty("LEON.shortformsPDF.Location", "/webapps/LEON/Prod/ShortFormsPDF");
		final String vesreceptOutPDF = leonprop.getProperty("LEON.vesreceiptsPDF.Location", "/webapps/LEON/Prod/VesselReceiptsPDF");
		final String pmtreceptOutPDF = leonprop.getProperty("LEON.pmtreceiptsPDF.Location", "/webapps/LEON/Prod/PermitReceiptsPDF");
		final String inserterOutPDF = leonprop.getProperty("LEON.insertersPDF.Location", "/webapps/LEON/Prod/InsertersPDF");
		final String confirmationPDF = leonprop.getProperty("LEON.confirmationsPDF.Location", "/webapps/LEON/Prod/ConfirmationsPDF");
        final String paymentData = leonprop.getProperty("LEON.paymentData.Location", "/webapps/LEON/Prod/PaymentData");
		//final String confirmationPDF = leonprop.getProperty("LEON.confirmationsPDF.Location", "/webapps/out/LEONDev/confirmations");
		final String shortfileOutPCL = leonprop.getProperty("LEON.shortformsPCL.Location", "/webapps/LEON/Prod/ShortFormsPCL");
		final String vesreceptOutPCL = leonprop.getProperty("LEON.vesreceiptsPCL.Location", "/webapps/LEON/Prod/VesselReceiptsPCL");
		final String inserterOutPCL = leonprop.getProperty("LEON.insertersPCL.Location", "/webapps/LEON/Prod/InsertersPCL");
		final String allformsOutPCL = leonprop.getProperty("LEON.allformsPCL.Location", "/webapps/LEON/Prod/PCLForms");
		final String imageLocation = leonprop.getProperty("LEON.imagedir.Location", "/webapps/images");
		String thisInFileDir = leonprop.getProperty("LEON.tmpfiledir.Location", "/webapps/tmpfiles/in");
		final boolean autoForms = Boolean.parseBoolean(leonprop.getProperty("LEON.licensing.AutoForms", "true"));
		final boolean sendLetters = Boolean.parseBoolean(leonprop.getProperty("LEON.licensing.SendLetters", "false"));
		final boolean sendPCL = Boolean.parseBoolean(leonprop.getProperty("LEON.licensing.SendPCL", "true"));
		String thisProcess = leonprop.getProperty("LEON.hostinfo.Process", "Prod");
		String thisHostName = leonprop.getProperty("LEON.hostinfo.Name"); 
		String thisLockFile = leonprop.getProperty("LEON.hostinfo.LockFile", "busy.lck");
		final String thisJavaPath = leonprop.getProperty("LEON.hostinfo.JavaPath", "/usr/lib/jvm/jdk1.8.0_05/bin/java");
		String thisOutFileDir = leonprop.getProperty("LEON.hostinfo.OutFileDir", "/leonOut");
		final String jrxmlLocation = leonprop.getProperty("LEON.jrxmlfiles.Location", "/webapps/jrxml/LEON");
		final String thisOutDir = leonprop.getProperty("LEON.hostinfo.OutDirectory", "/webapps/out/LEON");
		String thisPrePrintsDir = leonprop.getProperty("LEON.hostinfo.PreprintDir", "/webapps/out/LEON/preprints");
		final String thisResponseDir = leonprop.getProperty("LEON.hostinfo.ResponseDir", "/webapps/out/LEON/response");
		final String thisReceiptDir = leonprop.getProperty("LEON.hostinfo.ReceiptDir", "/webapps/out/LEON/receipts");
		//final String thisConfirmDir = leonprop.getProperty("LEON.hostinfo.ConfirmDir", "/webapps/out/LEON/confirmations");
		final String thisStagingDir = leonprop.getProperty("LEON.hostinfo.StagingDir", "/webapps/out/LEON/staging");
		String thisLogDir = leonprop.getProperty("LEON.hostinfo.LoggingDir", "/webapps/out/LEON/logs");
		final String thisLicYear = leonprop.getProperty("LEON.licensing.RevenueYear");
		String thisShipFee = leonprop.getProperty("LEON.licensing.ShippingFee");
		String thisServiceFee = leonprop.getProperty("LEON.licensing.ServiceFee");
		final boolean newpermitOverride = Boolean.parseBoolean(leonprop.getProperty("LEON.tracking.NewpermitOverride", "false"));
		final boolean reducepermitOverride = Boolean.parseBoolean(leonprop.getProperty("LEON.tracking.ReducedpermitOverride", "false"));
		final boolean newvesselOverride = Boolean.parseBoolean(leonprop.getProperty("LEON.tracking.NewvesselOverride", "false"));
		final boolean nonusOverride = Boolean.parseBoolean(leonprop.getProperty("LEON.tracking.NonUScitizenOverride", "false"));
		final boolean povertyOverride = Boolean.parseBoolean(leonprop.getProperty("LEON.tracking.PovertyOverride", "false"));
		final boolean agentOverride = Boolean.parseBoolean(leonprop.getProperty("LEON.tracking.AgentOverride", "false"));
		final boolean expressmailOverride = Boolean.parseBoolean(leonprop.getProperty("LEON.tracking.ExpressmailOverride", "false"));
		final boolean pendingembossOverride = Boolean.parseBoolean(leonprop.getProperty("LEON.tracking.PendingEmbossOverride", "false"));
		final boolean ccdelayOverride = Boolean.parseBoolean(leonprop.getProperty("LEON.tracking.CCDelayOverride", "false"));
		
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		/*
		 * Revenue item numbers MUST NOT contain gaps. Because this is a multi-threaded app and because several transactions 
		 * could be processing concurrently with some of them being declined, I can't guarantee no gaps.
		 * To remedy that, I am creating a lock file that prevents more than 1 transaction at a time for credit card processing.
		 * After successful completion, I iterate the revenue item number for the next transaction.
		 */	
		//final File lock = new File(thisOutDir+thisLockFile);
		//final Timer filechk = new Timer();
		//filechk.schedule(new TimerTask() {
			//@Override
		    //public void run() {
				//if (lock.exists()) {
				//	System.out.println(lock);
		    	//	System.out.println(entity.getId().getCfecid() + " Lock file exists, checking every second");
		    	//	leonLog.log(entity.getId().getCfecid() + " Lock file exists, checking every second");
		    	//} else {
		    		session = dataImpl.fact.openSession();

                    ArenewEntity entity = ArenewEntityMaterializer.materialize(session, dtoEntity);
                    Set<ArenewVessels> entityVessels = new HashSet<>(ArenewVesselsMaterializer.materializeList(session, dtoEntity.getArenewVesselses()));
                    Set<ArenewPermits> entityPermits = new HashSet<>(ArenewPermitsMaterializer.materializeList(session, dtoEntity.getArenewPermitses()));
                    ArenewPayment pay = ArenewPaymentMaterializer.materialize(session, dtoPay);
                    List<ArenewChanges> chg = ArenewChangesMaterializer.materializeListWithEntity(session, dtoChg, entity);
                    List<ArenewPermits> plist = ArenewPermitsMaterializer.materializeList(session, dtoPlist);
                    List<ArenewVessels> vlist = ArenewVesselsMaterializer.materializeList(session, dtoVlist);
                    List<ArenewPermits> pclist = ArenewPermitsMaterializer.materializeList(session, dtoPclist);
                    List<ArenewVessels> vclist = ArenewVesselsMaterializer.materializeList(session, dtoVclist);

		    		//System.out.println(entity.getId().getCfecid() + " Lock file does not exists, create it and start cctrans process");
		    		System.out.println(entity.getId().getCfecid() + " Lock file does not exists, don't create one, we are not fully automated");
		    		returl = false;		
		    		url = null;
		    		url2 = null;
		    		process = false;
		    		proceed = false;
		    		//filechk.cancel();
		    		//leonLog.log(entity.getId().getCfecid() + " Lock file does not exists, create it and start cctrans process");
		    		leonLog.log(entity.getId().getCfecid() + " Lock file does not exists, don't create one, we are not fully automated");
		    		/*try {
						lock.createNewFile();
					} catch (IOException e1) {
						System.out.println(entity.getId().getCfecid() + " Error creating new lock file");
						System.out.println(e1.getMessage());
						e1.printStackTrace();
						url = "error";
						proceed = true;
						process = false;
					}*/
		    		/*
					 * This method processes the actual order.
					 */
		    		url2 = null;
		    		String first6 = null;
					String last4 = null;		   
					final boolean manual = false;
					Process proc = null;
					String rjnumber = null;
					String itemnum = null;
					int newitem = 0;
				    Date sdate = null;
					Transaction tx = null;		
					Long txCount = null;
					String txNum = null;
					Random random = new Random();
			    	long range = (long)99999 - (long)10001 + 1;
			    	long fraction = (long)(range * random.nextDouble());
			    	int randomNumber = (int)(fraction + 10001);
			    	String rn = Integer.toString(randomNumber);
			    	List<ArenewPermits> prenewlist = new ArrayList();
			    	List<ArenewVessels> vrenewlist = new ArrayList();
			    	List<ArenewOtherpermits> opmt = new ArrayList();
			    	boolean caddress = false;
					boolean cphone = false;
					boolean cpermit = false;
					boolean cvessel = false;
					boolean cship = false;
					boolean pending = false;
					boolean docs = false;
					String changes = "(";
					String rjnum = null;		
					String myrjnum = null;
					String myitemnum = null;
					String newrjnum = null;
					String curyear = null;
					String nextitemnum = null;	
					boolean halReducedLtr = false;
					boolean sabReducedLtr = false;
					boolean nonUScitLtr = false;
					boolean povertyLtr = false;
					boolean agentLtr = false;
					//String CCpayment[] = new String[11];
					try {
                        sdate = getDbDate(session);

						tx = session.beginTransaction();
						
						/*
						 * 	Generate new Confirmation Number
						 *  This consist of transaction number, year, random number and cfecid
						 */
                        Number result = (Number) session.createNativeQuery("select count(distinct confirmcode) from tier2.Arenew_Payment where cfecid = :cfecid " +
			    			"and ryear = :ryear")
                            .setParameter("cfecid", entity.getId().getCfecid())
                            .setParameter("ryear", entity.getId().getRyear())
                            .uniqueResult();
                        txCount = result != null ? result.longValue() : 0L;
						txCount = (txCount + 1);
                        txNum = (((txCount < 10) ? "0" : "") + txCount);
						String yr = entity.getId().getRyear().substring(2, 4);
						ryear = yr;
						int tyear = Integer.parseInt(ryear);
						lyear = Integer.toString(tyear-1);

						/*
						 * reset these values in case a user goes back to the billing section (of before) and re-enters data after a failed CC transaction
						 */
						url = "good";
						System.out.println(entity.getId().getCfecid() + " URL after reset, line 161 " + url);
						url2 = null;
						//manual = false;
						process = false;
						proceed = false;
						rjnum = null;
						itemnum = null;
						myitemnum = null;
						myrjnum = null;
						newrjnum = null;
						curyear = null;
						nextitemnum = null;
						newitem = 0;
						returl = false;
						halReducedLtr = false;
						sabReducedLtr = false;
						nonUScitLtr = false;
						povertyLtr = false;
						agentLtr = false;

						/*
						 * Determine automatic or manual processing
						 */
						/*
						 * Check the permits associated with this object.
						 * First, lets weed out any new Permits (no serial #) that haven't already been renewed.
						 * Second, lets looks for IFQ reduced fee's for permits that haven't already been renewed.
						 * Third (not implemented yet), look for I type permits NOT renewed prior year by user.
						 * If we find one, set manual to TRUE, then immediately break out of the iteration and continue
						 */
						System.out.println(entity.getId().getCfecid() + " Iterate through permit(s) and check status");
						for (Iterator<ArenewPermits> p = entityPermits.iterator(); p.hasNext();) {
							ArenewPermits permit = (ArenewPermits) p.next();
							System.out.println(entity.getId().getCfecid() + " Permit Fishery is " + permit.getId().getFishery());
			    			if (permit.getId().getSerial().equalsIgnoreCase("Not Issued")) {
			    				System.out.println(entity.getId().getCfecid() + " This permit has no serial number");
			    				if (permit.isNewrenew()) {
			    					System.out.println(entity.getId().getCfecid() + " and it is a new renew. Set manual to true and break out of iteration?");
			    					if (!(newpermitOverride)) {
			    						//manual = true;
			    						break;
			    					} else {
			    						System.out.println(entity.getId().getCfecid() + " manual process is overridden for new permits, proceed with automatic");
			    						leonLog.log(entity.getId().getCfecid() + " manual process is overridden for new permits, proceed with automatic");
			    					}
			    				} else {
			    					System.out.println(entity.getId().getCfecid() + " but it has already been renewed.");
			    				}    				
			    			}
			    			if (permit.getId().getFishery().trim().equalsIgnoreCase("B 06B") ||
			    					permit.getId().getFishery().trim().equalsIgnoreCase("B 61B") ||
			    					permit.getId().getFishery().trim().equalsIgnoreCase("C 06B") ||
			    					permit.getId().getFishery().trim().equalsIgnoreCase("C 61B") ||
			    					permit.getId().getFishery().trim().equalsIgnoreCase("C 09B") ||
			    					permit.getId().getFishery().trim().equalsIgnoreCase("C 91B")) {
			    				System.out.println(entity.getId().getCfecid() + " This permit is one of the 6 open access IFQ fisheries");
			    				if (permit.getFee().equalsIgnoreCase("75")) {
									System.out.println(entity.getId().getCfecid() + " and we are accessing a 75 fee, so it is reduced");
									if (permit.isNewrenew()) {
										System.out.println(entity.getId().getCfecid() + " and it is a new renew. Check RAM data for verification");
										if (permit.getId().getFishery().trim().equalsIgnoreCase("B 06B") || permit.getId().getFishery().trim().equalsIgnoreCase("B 61B")) {
											System.out.println("its a halibut permit ..... ");
											if (!(entity.isAutoHALreduced())) {
												System.out.println(entity.getId().getCfecid() + " and it was by choice, not RAM stats. Set manual to true and break out of iteration?");
												if (!(reducepermitOverride)) {
						    						//manual = true;
						    						docs = true;
						    						halReducedLtr = true;
						    						break;
						    					} else {
						    						System.out.println(entity.getId().getCfecid() + " manual process is overridden for reduced fee permits, proceed with automatic");
						    						leonLog.log(entity.getId().getCfecid() + " manual process is overridden for reduced fee permits, proceed with automatic");
						    					}
											} else {
												System.out.println(entity.getId().getCfecid() + " and it was NOT by choice, but RAM stats");
												halReducedLtr = false;
											}
										} else {
											System.out.println("its a sablefish permit ..... ");
											if (!(entity.isAutoSABreduced())) {
												System.out.println(entity.getId().getCfecid() + " and it was by choice, not RAM stats. Set manual to true and break out of iteration?");
												if (!(reducepermitOverride)) {
						    						//manual = true;
						    						docs = true;
						    						sabReducedLtr = true;
						    						break;
						    					} else {
						    						System.out.println(entity.getId().getCfecid() + " manual process is overridden for reduced fee permits, proceed with automatic");
						    						leonLog.log(entity.getId().getCfecid() + " manual process is overridden for reduced fee permits, proceed with automatic");
						    					}
											} else {
												System.out.println(entity.getId().getCfecid() + " and it was NOT by choice, but RAM stats");
												sabReducedLtr = false;
											}
										}

									}  else {
										System.out.println(entity.getId().getCfecid() + " but it has already been renewed.");
									}
								} else {
									System.out.println(entity.getId().getCfecid() + " but the permit is full price and/or has been renewed already. Set manual to false");
								}
			    			}
			    			// TODO create unlimited permit check for last year ownership
			    			//System.out.println("Permit type is " + permit.getType());
			    			/*if (permit.gettype.trim().equalsIgnoreCase("I")) {
			    				System.out.println(entity.getId().getCfecid() + " This permit is type I - Unlimited Fishery");
			    				if ('they didnt own it last year') {	// insert a manual sqlquery here to check if this serial# was held by this CFECID last year (lyear)
			    					System.out.println(entity.getId().getCfecid() + " did not have this permit last year. Set manual and break out of iteration");
			    					manual = true;
			    					break;
			    				} else {
			    					System.out.println(entity.getId().getCfecid() + " did own this permit last year.");
			    				}
			    			}*/
						}

						/*
						 * Check for new vessels. If we find one, set manual to TRUE and
						 * immediately break out of the iteration and continue.
						 */
						System.out.println(entity.getId().getCfecid() + " Iterate through vessel(s) and check status");
						for (Iterator<ArenewVessels> v = entityVessels.iterator(); v.hasNext();) {
			    			ArenewVessels vessel = (ArenewVessels) v.next();
			    			if (vessel.getId().getAdfg().equalsIgnoreCase("N/A")) {
			    				if (vessel.isNewrenew()) {
			    					System.out.println(entity.getId().getCfecid() + " found vessel with unassigned ADFG value, manual process?");
			    					if (!(newvesselOverride)) {
			    						//manual = true;
			    						break;
			    					} else {
			    						System.out.println(entity.getId().getCfecid() + " manual process is overridden for new vessels, proceed with automatic");
			    						leonLog.log(entity.getId().getCfecid() + " manual process is overridden for new vessels, proceed with automatic");
			    					}
			    				}				
			    			}
			    			if (vessel.getId().getAdfg().length() >= 6) {
								if (vessel.getId().getAdfg().substring(0,4).equalsIgnoreCase("Temp")) {
									if (vessel.isNewrenew()) {
										System.out.println(entity.getId().getCfecid() + " found vessel with unassigned ADFG value, manual process?");
										if (!(newvesselOverride)) {
				    						//manual = true;
				    						break;
				    					} else {
				    						System.out.println(entity.getId().getCfecid() + " manual process is overridden for new vessels, proceed with automatic");
				    						leonLog.log(entity.getId().getCfecid() + " manual process is overridden for new vessels, proceed with automatic");
				    					}
				    				}
			        			}
							}
			    		}

						/*
						 * Did this person select NON-US Citizen? If so, set to manual
						 */
						if (entity.getCitizen().equalsIgnoreCase("false")) {
							nonUScitLtr = true;
							System.out.println(entity.getId().getCfecid() + " user declared NON-US Citizenship, manual?");
							if (!(nonusOverride)) {
	    						//manual = true;
	    						docs = true;
	    					} else {
	    						System.out.println(entity.getId().getCfecid() + " manual process is overridden for non-US citizenship, proceed with automatic");
	    						leonLog.log(entity.getId().getCfecid() + " manual process is overridden for non-US citizenship, proceed with automatic");
	    					}
						} else {
							nonUScitLtr = false;
						}

						/*
						 * Did this person select for Poverty Fee's? If so, set to manual
						 */
						if (entity.getPoverty().equalsIgnoreCase("true")) {
							povertyLtr = true;
							System.out.println(entity.getId().getCfecid() + " user declared poverty fee qualification, manual?");
							if (!(povertyOverride)) {
	    						//manual = true;
	    						docs = true;
	    					} else {
	    						System.out.println(entity.getId().getCfecid() + " manual process is overridden for poverty declaration, proceed with automatic");
	    						leonLog.log(entity.getId().getCfecid() + " manual process is overridden for poverty declaration, proceed with automatic");
	    					}
						} else {
							povertyLtr = false;
						}

						/*
						 * Is this person an Agent? If so, set to manual
						 */
						if (entity.getAgentsub().equalsIgnoreCase("yes")) {
							agentLtr = true;
							System.out.println(entity.getId().getCfecid() + " user declared they are an Agent, manual?");
							if (!(agentOverride)) {
	    						//manual = true;
	    						docs = true;
	    					} else {
	    						System.out.println(entity.getId().getCfecid() + " manual process is overridden for agent filing, proceed with automatic");
	    						leonLog.log(entity.getId().getCfecid() + " manual process is overridden for agent filing, proceed with automatic");
	    					}
						} else {
							agentLtr = false;
						}

						/*
						 * Is this an Express Mail renewal?
						 */
						if (pay.getShiptype().equalsIgnoreCase("em")) {
							System.out.println(entity.getId().getCfecid() + " user declared Express mail, manual?");
							if (!(expressmailOverride)) {
	    						//manual = true;
	    					} else {
	    						System.out.println(entity.getId().getCfecid() + " manual process is overridden for express mailing, proceed with automatic");
	    						leonLog.log(entity.getId().getCfecid() + " manual process is overridden for express mailing, proceed with automatic");
	    					}
						}

						/*
						 * If an ADFG has been or is being licensed, then proceed with an automatic transaction. If not, then set to manual.
						 */
						if (entity.isManual()) {
							System.out.println(entity.getId().getCfecid() + " user specified Permit ADFG that might not be renewed, manual?");
							if (!(pendingembossOverride)) {
								//manual = true;
							} else {
								System.out.println(entity.getId().getCfecid() + " manual process is overridden for pending emboss check, proceed with automatic");
	    						leonLog.log(entity.getId().getCfecid() + " manual process is overridden for pending emboss check, proceed with automatic");
							}
						}

						/*
						 * TODO doesn't really matter if this is active, but it is a placeholder when we decide to do the CC thing correctly.
						 * Is this a NO-CC Info renewal? (saved, but not processed due to CC info being unable to process from terminal)
						 */
						if (pay.getCcnumber().equalsIgnoreCase("0000000000000000")) {
							System.out.println(entity.getId().getCfecid() + " user declared NO CC info, manual?");
							if (!(ccdelayOverride)) {
								//manual = true;
							} else {
								System.out.println(entity.getId().getCfecid() + " manual process is overridden for Credit Card delay, proceed with automatic");
	    						leonLog.log(entity.getId().getCfecid() + " manual process is overridden for Credit Card delay, proceed with automatic");
							}
						}
						
						/*
						 * For contingency purposes, we can set all transaction to either manual or automatic. Due to pathing issues, which 
						 * I should probably address in the property file(s), manual transaction require specific and hardcoded paths. 
						 */
						leonLog.log(entity.getId().getCfecid() + " Global transactions are set to " + manualGlobal);
						System.out.println(entity.getId().getCfecid() + " Global transactions are set to " + manualGlobal);
						if (manualGlobal) {
							//manual = true;
							leonLog.log(entity.getId().getCfecid() + "  All transactions are set to manual");
							System.out.println(entity.getId().getCfecid() + "  All transactions are set to manual");
						} else {
							if (manual) {
								leonLog.log(entity.getId().getCfecid() + " has a manual override for this transaction");
								System.out.println(entity.getId().getCfecid() + " has a manual override for this transaction");
							} else {
								leonLog.log(entity.getId().getCfecid() + " has no manual override for this transaction");
								System.out.println(entity.getId().getCfecid() + " has no manual override for this transaction");
							}
						}
						
						/*
						 * I put this in as 'potential relief', not knowing yet what impact automation of 75% of LEON traffic would suddenly have.
						 * 0-9 whole number random generator. < 5 or else is specifically represents 50 percent (technically) of all automatic transactions.
						 * With this, we can artificially control traffic if it becomes overwhelming or problematic. 
						 */
						/*if (manualRandom) {
							Random rand = new Random();
							int ran = rand.nextInt(10);
							System.out.println("Random number is " + ran);
							if (ran < 5) {
								System.out.println("Set this transaction to MANUAL");
								manual = true;
							} else {
								System.out.println("Set this transaction to AUTOMATIC");
								manual = false;
							}
						}*/						
						
						/*
						 * If processing is automatic, then charge the card and receive back various payment info.
						 * First, get the current RJ and Item number
						 */					
						if (!(manual)) {
							System.out.println(entity.getId().getCfecid() + " Starting auto credit card approval process with confirmcode " + cnum);
							leonLog.log(entity.getId().getCfecid() + " Starting auto credit card approval process with confirmcode " + cnum);
							StringBuffer myqueryrj = new StringBuffer("select to_char(r.curdate, 'MMDDYY'), to_char(CURRENT_TIMESTAMP, 'MMDDYY'), to_char(CURRENT_TIMESTAMP, 'YYYY'), "
								+ "r.myrjnum, lpad(r.myitemnum::text,3,'0') from tier2.arenew_rjtracker r");
							List listrj = session.createNativeQuery(myqueryrj.toString()).setFirstResult(0).setMaxResults(1).list();
							for (Iterator i = listrj.iterator(); i.hasNext();) {
								Object[] rs1 = (Object[]) i.next();
								int curdate = (Integer.parseInt(rs1[0].toString()));
								int newdate = (Integer.parseInt(rs1[1].toString()));
								curyear = rs1[2].toString();
								rjnum = rs1[3].toString();
								itemnum = rs1[4].toString();
								newitem = Integer.parseInt(itemnum);
								leonLog.log(entity.getId().getCfecid() + " Current Item Number " + itemnum);
								leonLog.log(entity.getId().getCfecid() + " Next Item Number int " + (newitem + 1));
								leonLog.log(entity.getId().getCfecid() + " Iterating a new item number placeholder value");
								myrjnum = rjnum;
								myitemnum = itemnum;
								nextitemnum = Integer.toString((newitem + 1));
							}
							rjnumber = curyear+"1"+rjnum+itemnum;
							leonLog.log(entity.getId().getCfecid() + " Temporarily assigning RJNumber " +rjnumber+ " until transaction is approved");	
							if ("ACCEPT".equalsIgnoreCase(paymentDecision)) {
								System.out.println(entity.getId().getCfecid() + " Approval with paymentMessage " + paymentDecision + " / " + paymentMessage + " and URL is " + url);
								leonLog.log(entity.getId().getCfecid() + " Approval with paymentMessage " + paymentDecision + " / " + paymentMessage);
								pay.setRjnumber(rjnumber);
								//pay.setAuthcode(ansstring.substring(2, 8)); Already set above
								pay.setRevoperator("leoncc");
								// TODO not sure I should be setting the next 2 lines, but if an error occurs, it will rollback.
								pay.setRevcomplete("TRUE");
								pay.setRevdate(sdate);
								System.out.println(entity.getId().getCfecid() + " update arenew_rjtracker set myitemnum = '"+(newitem + 1)+"'");
								leonLog.log("update arenew_rjtracker set myitemnum = '"+(newitem + 1)+"'");
                                NativeQuery sqlQuery = session.createNativeQuery("update tier2.arenew_rjtracker set myitemnum = '"+(newitem + 1)+"'");
								sqlQuery.executeUpdate();
								process = true;
							} else {
                                System.out.println(entity.getId().getCfecid() + " Declined with paymentMessage " + paymentDecision +  "/ " + paymentMessage + " and URL is " + url);
                                leonLog.log(entity.getId().getCfecid() + " Declined with paymentMessage " + paymentDecision +  "/ " + paymentMessage);
								url = "declined";
								process = false;
							}	
							proceed = true;
						} else {
							/*
							 * this transaction is manual via the LEON queues and licensing, so just process normally.
							 */
							process = true;
							proceed = true;
						}
						
						process = true;
						proceed = true;
						
						/*
						 * Regardless of if the CC is approved or declined OR if the process boolean is true,
						 * we must wait for the runtime exec above to finish before we can move forward.
						 * So, check every second for the boolean value proceed
						 */			
						//while(!proceed) {
						//	Thread.sleep(1000);
						//}
						
						/*
						 * OK, were done with the CC stuff OR just proceeding manually
						 * if string value url does NOT equal declined, move forward
						 * if boolean process equals true, also move forward
						 * else, something went bad above, CC was either declined or blew up
						 */
						if (!(url.equalsIgnoreCase("declined"))) {
							if (process) {
								/*
								 * Make last changes to the payment object
								 */
								leonLog.log("Processing order "+cnum+" for " + entity.getId().getCfecid());
					    		leonLog.log(entity.getId().getCfecid() + " writing new Entity record");
					    		pay.setConfirmcode(cnum);
					    		pay.setReceiveddate(sdate);
					    		pay.setCurtx(txNum.toString());
					    		/*
					    		 * combine the credit card name values to a single object
					    		 */
                                // TODO: no such notion on CyberSource side, so we keep it as Individual
                                pay.setCcowner("Individual");
					    		if (pay.getCcowner().equalsIgnoreCase("Individual")) {
					    			if (!(pay.getCcmname() == null)) {
					        			pay.setCcname(pay.getCcname() + " " + pay.getCcmname() + " " + pay.getCclname());
					        		} else {
					        			pay.setCcname(pay.getCcname() + " " + pay.getCclname());
					        			pay.setCcmname("N/A");
					        		}
					    		} else {
					    			pay.setCcname(pay.getCcname());
					    			pay.setCcmname("N/A");
					    			pay.setCclname("N/A");
					    		}    		
					    		if (pay.getCctype().equalsIgnoreCase("Visa")) {
					    			pay.setCctype("vi");
					    		} else if (pay.getCctype().equalsIgnoreCase("Master Card")) {
					    			pay.setCctype("mc");
					    		} else {
					    			pay.setCctype("di");
					    		}// TODO: CyberSource supports other card types
					    		if (pay.getShiptype().equalsIgnoreCase("em")) {
					    			cship = true;
					    		} else {
					    			cship = false;
					    		}
					    		if (entity.isManual()) {
					    			pending = true;
					    		} else {
					    			pending = false;
					    		}
					    		if (!(pay.getBcity() == null)) {
					    			pay.setBcity(pay.getBcity().toUpperCase());
					    		}   		
					    		if (pay.getTotalamount().endsWith(".0")) {
									String nfee = pay.getTotalamount().substring(0,(pay.getTotalamount().length()-2));
									pay.setTotalamount(nfee);
								} 		
					    		/*
					    		 * grab the last 4 of the CC number. If auto process, save the number in the payment
					    		 * table as truncated. If manual process, encrypt the number for future use.
					    		 */
					    		first6 = pay.getCcnumber().substring(0, 6);
					    		last4 = pay.getCcnumber().substring(12, 16);
					    		//if (!(manual)) {
					    		//	pay.setCcnumber("XXXXXXXXXXXX"+last4);
					    		//} else {
					    		//	pay.setCcnumber(leonEncrypt.encrypt(pay.getCcnumber()));		    			
					    		//}		    		
					    		//pay.setCcnumber(first6+"XXXXXX"+last4);
					    		pay.setCcnumber(leonEncrypt.encrypt(pay.getCcnumber()));
					    		//pay.setCcnumber(pay.getCcnumber());
					    		//System.out.println(entity.getId().getCfecid() + " setting CC data as " +first6+"XXXXXX"+last4);	
					    		pay.setArenewEntity(entity);
					    		entity.getArenewPayments().add(pay);
					    		
					    		/*
					    		 * Make last changes to the entity object
					    		 */
					    		if (entity.getCreatedate()==null) {
					        		entity.setCreatedate(sdate);    
					    		}    		
					    		if (entity.getResidency().equalsIgnoreCase("resident") || entity.getResidency().equalsIgnoreCase("R")) {
					    			entity.setResidency("R");
					    		} else {
					    			entity.setResidency("N");
					    		}
					    		entity.setUpdatedate(sdate); 
					    		
					    		//System.out.println("reduced halibut is " + entity.getReducedHalibut());
					    		//System.out.println("reduced sablefish is " + entity.getReducedSablefish());
					    		
					    		if (entity.getReducedHalibut() == null) { entity.setReducedHalibut("false"); }
					    		if (entity.getReducedSablefish() == null) { entity.setReducedSablefish("false"); }
                                pay = ArenewPaymentMaterializer.saveOrUpdate(session, pay);
                                entity = ArenewEntityMaterializer.saveOrUpdate(session, entity);
					    		/*
					    		 * Remove any old entries from otherpermits table for this cfecid and ryear. 
					    		 * More later on this table.
					    		 */
								Query dop = session.createNativeQuery("delete from tier2.arenew_otherpermits where cfecid = '"+entity.getId().getCfecid()+"' and ryear = '"+entity.getId().getRyear()+"'");
								dop.executeUpdate();
								/*
								 * This is just some sanity code I put in because we had 3 instances where permits have been dropped from the final database save.
								 * So, I am comparing the number of permits and vessels being renewed from entity objects vs. list object.
								 * If the numbers do not match, then we'll re-populate the permit and/or vessel objects from a copy that was produced just prior to final submission.
								 */
								int epnum = 0;
								int lpnum = 0;
								int evnum = 0;
								int lvnum = 0;
								for (Iterator<ArenewPermits> pe = entityPermits.iterator(); pe.hasNext();) {
									ArenewPermits permit = (ArenewPermits) pe.next();
					    			if (permit.isNewrenew()) {
					    				epnum++;
					    			}
								}
								for (Iterator<ArenewVessels> ve = entityVessels.iterator(); ve.hasNext();) {
					    			ArenewVessels vessel = (ArenewVessels) ve.next();
					    			if (vessel.isNewrenew()) {
					    				evnum++;
					    			}
								}
								for (Iterator pl = plist.iterator(); pl.hasNext();) {
									ArenewPermits permit = (ArenewPermits) pl.next();
					    			if (permit.isNewrenew()) {
					    				lpnum++;
					    			}
								}
								for (Iterator vl = vlist.iterator(); vl.hasNext();) {
									ArenewVessels vessel = (ArenewVessels) vl.next();
					    			if (vessel.isNewrenew()) {
					    				lvnum++;
					    			}
								}
								if ( (epnum == lpnum) && (evnum == lvnum) ) {
									leonLog.log(entity.getId().getCfecid() + " matches permits and vessels on both lists");
								} else {
									if (!(epnum == lpnum)) {
										leonLog.log(entity.getId().getCfecid() + " does not match renewed permit counts, loading backup objects");
										entityPermits.clear();
										for (Iterator<ArenewPermits> p = pclist.iterator(); p.hasNext();) {
								       		ArenewPermits permit = (ArenewPermits) p.next();
								       		entityPermits.add(permit);
								 	    }
										notify.outofsync(entity.getId().getCfecid(), "Permits", entity.getId().getRyear(), cnum);
									}
									if (!(evnum == lvnum)) {
										leonLog.log(entity.getId().getCfecid() + " does not match licensed vessel counts, loading backup objects");
										entityVessels.clear();
										for (Iterator<ArenewVessels> v = vclist.iterator(); v.hasNext();) {
								 	    	ArenewVessels vessel = (ArenewVessels) v.next();
								 	    	entityVessels.add(vessel);
								 	    } 
										notify.outofsync(entity.getId().getCfecid(), "Vessels", entity.getId().getRyear(), cnum);
									}
								}	
								/*
								 * Make last changes to the permit object
								 */
					    		for (Iterator<ArenewPermits> p = entityPermits.iterator(); p.hasNext();) {
					    			ArenewPermits permit = (ArenewPermits) p.next();
					    			if (permit.isNewrenew()) {
					    				permit.setConfirmcode(cnum);
					    				permit.setReceiveddate(sdate);
					    				permit.setLastupdated(sdate);
					    				// TODO need to set this ONLY if auto transaction
					    				if (!(manual)) {
					    					permit.setStatus("Completed");
					    				}
					    				permit.setArenewPayment(pay);
					    				prenewlist.add(permit);
                                        permit = ArenewPermitsMaterializer.saveOrUpdate(session, permit);
					    				leonLog.log(entity.getId().getCfecid() + " Permit " + permit.getId().getSerial() + " for fishery " + permit.getId().getFishery() + " is being renewed");
					    			} else if (permit.isRenewed()) {
					    				leonLog.log(entity.getId().getCfecid() + " Permit " + permit.getId().getSerial() + " for fishery " + permit.getId().getFishery() + " is already renewed");
					    			} else {
					    				/*
					    				 * As we iterate through the list of user permits, we take those that have been selected for
					    				 * renewal and add them to the ArenewPermits object above. If the user has a permit in their
					    				 * inventory that they elected to NOT renew, I capture it here in a table called
					    				 * ArenewOtherpermits. This object will get updated each time the user goes through the system
					    				 * and completes a transaction. So basically, it is a table of un-renewed permits for online
					    				 * users who have completed at least 1 transaction.
					    				 */    			
					    				leonLog.log(entity.getId().getCfecid() + " Permit " + permit.getId().getSerial() + " for fishery " + permit.getId().getFishery() + " is not being renewed");
					    				ArenewOtherpermits other = new ArenewOtherpermits();
					    				ArenewOtherpermitsId otherId = new ArenewOtherpermitsId();
					    				otherId.setFishery(permit.getId().getFishery());
					    				otherId.setPyear(permit.getId().getPyear());
					    				otherId.setSerial(permit.getId().getSerial());
					    				other.setId(otherId);
					    				if (permit.isIntend()) {
					    					other.setIntends("true");
					    				} else {
					    					other.setIntends("false");
					    				}
					    				if (permit.isNointend()) {
					    					other.setNointends("true");
					    				} else {
					    					other.setNointends("false");
					    				}
					    				other.setArenewEntity(entity);
					    				other.setUpdatedate(sdate);
                                        other = ArenewOtherpermitsMaterializer.saveOrUpdate(session, other);
					    				entity.getArenewOtherpermitses().add(other);
					    			}
					    		}					    
					    		String vtemp = null;
					    		int vtemp1 = 0;
					    		int vtempname = 0;
					    		String vtempfinal = null;
					    		/*
					    		 * The following code basically just retrieves the last integer used when assigning temp numbers
					    		 * to new vessels without a valid ADFG number.
					    		 */
					    		for (Iterator<ArenewVessels> v = entityVessels.iterator(); v.hasNext();) {
					    			ArenewVessels vessel = (ArenewVessels) v.next();
					    			if (!(vessel.getId().getAdfg().equalsIgnoreCase("N/A"))) {
					    				if (vessel.getId().getAdfg().length() >= 6) {
					    					if (vessel.getId().getAdfg().substring(0,4).equalsIgnoreCase("Temp")) {
					            				vtemp = vessel.getId().getAdfg().substring(4,6);
					            				if (Integer.parseInt(vtemp) > vtemp1) {
					            					vtemp1 = (Integer.parseInt(vtemp));
					            				}
					            			}
					    				}
					    			}
					    		}
					    		if (vtemp1 > 0) {
					    			vtempname = vtemp1;
					    		} else {
					    			vtempname = 0;
					    		}
					    		/*
					    		 * Make last changes to vessel object
					    		 */
					    		for (Iterator<ArenewVessels> v = entityVessels.iterator(); v.hasNext();) {
					    			ArenewVessels vessel = (ArenewVessels) v.next();
					    			if (vessel.isNewrenew()) {
										if (vessel.getFee().endsWith(".0")) {
											String nfee = vessel.getFee().substring(0,(vessel.getFee().length()-2));
											vessel.setFee(nfee);
										}
					    				vessel.setConfirmcode(cnum);
					    				vessel.setReceiveddate(sdate);
					    				vessel.setLastupdated(sdate);
					    				// TODO need to set this only if automatic transaction
					    				if (!(manual)) {
					    					vessel.setStatus("Completed");
					    				}	
					    				if (vessel.getId().getAdfg().equalsIgnoreCase("N/A")) {
					    					vtempname ++;
					    					vtempfinal = Integer.toString(vtempname);
					    					if (vtempfinal.length() == 1) {
					    						vtempfinal = ("0" + vtempfinal);
					    					}
					    					vessel.getId().setAdfg("TEMP" + vtempfinal);
					    				} else {
					    					vessel.getId().setAdfg(vessel.getId().getAdfg());
					    				}
					    				if (vessel.isNewVessel()) {
					    					/*
					    					 * There are 2 kinds of new vessels, those with an ADFG number and those without.
					    					 * Those that have an adfg number are basically being licensed by a person who
					    					 * didn't license that vessel the year prior. It could be the same person or a 
					    					 * completely different person, we don't care for licensing purposes. However, if 
					    					 * it is a new person AND the vessel was licensed last year by someone else, then it
					    					 * will automatically show up on the other person's online profile. We don't want the
					    					 * vessel to be licensed twice. So following is code to determine all that.
					    					 */
					    					if (vessel.getId().getAdfg().length() == 5) {
					    						/*
					    						 * Check the RNW_VES table to see if a 2011 record exist for this vessel.
					    						 * If one is found, make sure its a difference CFECID, then remove the record
					    						 * so the vessel cannot be renewed twice. Then set the isNew value to false
					    						 * which will prevent the form from printing out the Q&A on a non-ADFG record.
					    						 */
					    						Long existCount = (Long) session.createQuery("select count(*) from RnwVes ov where ov.id.adfgNumber = '"+vessel.getId().getAdfg()+"' and ov.id.XRenewalYear = '"+entity.getId().getRyear()+"' and ov.id.renewalStatus = 'A'").uniqueResult();
					    						if (existCount > 0) {
					    							/*
					    							 * Valid vessel being licensed by a different person than current records indicate.
					    							 * Set isNew to false to prevent new vessel shortform, then set DelOrig to true
					    							 * which will delete the original record of this vessel from the source RNW_VES table later.
					    							 */
					    							vessel.setIsnew("false");
					    							vessel.setDelorig("true");
					    						} else {
					    							/*
					    							 * Valid vessel being licensed by a different person than current records indicate
					    							 * BUT vessel wasn't licensed last year, so there is NO available record to delete.
					    							 */
					    							vessel.setIsnew("false");
					    							vessel.setDelorig("false");
					    						}
					    					} else {
					    						/*
					    						 * New vessel with temp adfg name. Set isNew to true to create custom shortform and
					    						 * set Delorig to false as there is no original record to worry about.
					    						 */
					    						vessel.setIsnew("true");
					    						vessel.setDelorig("false");
					    					}	    					
					    				} else {
					    					/*
					    					 * Vessel is not new to system or owner, proceed normally.
					    					 */
					    					vessel.setIsnew("false");
					    					vessel.setDelorig("false");
					    				}
					    				vessel.setLength(vessel.getLengthFeet());
					    				vessel.setInches(vessel.getLengthInches());
					    				vessel.setArenewPayment(pay);
                                        vessel = ArenewVesselsMaterializer.saveOrUpdate(session, vessel);
					    				vrenewlist.add(vessel);	    				
					    			}
					    		}    							    		
					    		/*
					    		 * declaration to set FIRST sequence number
					    		 */
					    		int x=1;					    		
					    		/*
					    		 * The following method receives the current change list, verify's it's
					    		 * validity and returns a new list containing ONLY the items that really changed.
					    		 */
					    		List<ArenewChanges> chg2 = verifyVesChange(chg, session, leonLog, vrenewlist);
					    		List<ArenewChanges> chg3 = verifyPmtChange(chg2, session, leonLog, prenewlist, entity.getId().getRyear());
					    		List<ArenewChanges> chg4 = verifyPmtChange2(chg3, session, leonLog, prenewlist, entity.getId().getRyear());
					    		/*
					    		 * The list chg4 becomes the final change list that gets recorded.
					    		 */
					    		for (Iterator<ArenewChanges> c = chg4.iterator(); c.hasNext();) {    
					    			ArenewChanges change = (ArenewChanges) c.next();
					    			entity.getArenewChangeses().remove(change);
					    			change.setUpdatedate(sdate);
					    			change.getId().setConfirmcode(cnum);
					    			/*
					    			 * Adding the final sequence number for the change record
					    			 */
					    			change.getId().setSeq(Integer.toString(x));
					    			change.setArenewEntity(entity);
					    			change.setArenewPayment(pay);
					    			x++;
                                    change = ArenewChangesMaterializer.saveOrUpdate(session, change);
					    			if (change.getType().equalsIgnoreCase("Person")) {
					    				if (change.getId().getAttribute().startsWith("Perm")) {
					    					if (!(caddress)) {
					    						caddress = true;
					    						if (cphone || cpermit || cvessel) {
					    							changes = (changes + ", Address");
					    						} else {
					    							changes = (changes + "Address");
					    						}
					    					}
					    				} else if (change.getId().getAttribute().startsWith("phone")) {
					    					if (!(cphone)) {
					    						cphone = true;
					    						if (caddress || cpermit || cvessel) {
					    							changes = (changes + ", Phone");
					    						} else {
					    							changes = (changes + "Phone");
					    						}
					    					}
					    				}
					    			}
					    			if (change.getType().equalsIgnoreCase("Permits")) {
					    				if (!(cpermit)) {
					    					cpermit = true;
					    					if (cphone || caddress || cvessel) {
												changes = (changes + ", Permit");
											} else {
												changes = (changes + "Permit");
											}
					    				}
					    			}
					    			if (change.getType().equalsIgnoreCase("Vessels")) {
					    				if (!(cvessel)) {
					    					cvessel = true;
					    					if (cphone || caddress || cpermit) {
												changes = (changes + ", Vessel");
											} else {
												changes = (changes + "Vessel");
											}
					    				}
					    			}
					    		}    		
					    		// TODO Tracking notes for additional docs and (potentially) bogus CC info and more
					    		if (docs) {
					    			if (cphone || caddress || cpermit || cvessel) {
					    				changes = (changes + ", <b>DOCS</b>");
					    			} else {
					    				changes = (changes + "<b>DOCS</b>");
					    			}
					    		}
					    		if (cship) {
					    			if (cphone || caddress || cpermit || cvessel || docs) {
					    				changes = (changes + ", <b><font color='red'>Express</font></b>");
					    			} else {
					    				changes = (changes + "<b><font color='red'>Express</font></b>");
					    			}
					    		}					    		
					    		if (pending) {
					    			if (cphone || caddress || cpermit || cvessel || docs || cship) {
					    				changes = (changes + ", <b>PENDING</b>");
					    			} else {
					    				changes = (changes + "<b>PENDING</b>");
					    			}
					    		}
					    		if (pay.getCcnumber().equalsIgnoreCase("0000000000000000")) {
					    			if (cphone || caddress || cpermit || cvessel || docs || pending || docs) {
					    				changes = (changes + ", <b>CCINFO</b>");
					    			} else {
					    				changes = (changes + "<b>CCINFO</b>");
					    			}
					    		}
					    		if (cphone || caddress || cpermit || cvessel || cship || pending || docs) {
					    			changes = (changes + ")");
					    			cphone = false;
					    			caddress = false;
					    			cpermit = false;
					    			cvessel = false;
					    			cship = false;
					    			pending = false;
					    			docs = false;
					    			pay.setNotes(changes);
                                    pay = ArenewPaymentMaterializer.saveOrUpdate(session, pay);
					    		}							    		
					    		/*
					    		 * Print the change log in the by type first, then attribute
					    		 */
					    		Collections.sort(chg4);
					    		leonLog.log("START THE CHANGE LOG for (" + entity.getId().getCfecid() + ") - " + entity.getXname());
					    		/*
					    		 * Write the changes to the log
					    		 */
					    		for (Iterator cl = chg4.iterator(); cl.hasNext();) {
					    			ArenewChanges chgprt = (ArenewChanges) cl.next();
					    			if (chgprt.getType().equalsIgnoreCase("Vessels") || chgprt.getType().equalsIgnoreCase("Permits")) {
					    				if (chgprt.getId().getAttribute().equalsIgnoreCase("salmonTrollReg")) {
					        				leonLog.log(entity.getId().getCfecid() + " has changed attribute " + chgprt.getId().getAttribute() + " to " + chgprt.getNewvalue() + " for object " + chgprt.getObject());
					        			} else {
					        				leonLog.log(entity.getId().getCfecid() + " has changed attribute " + chgprt.getId().getAttribute() + " from " + chgprt.getOldvalue() + " to " + chgprt.getNewvalue() + " for object " + chgprt.getObject());
					        			}
					    			} else {
					    				leonLog.log(entity.getId().getCfecid() + " has changed attribute " + chgprt.getId().getAttribute() + " from " + chgprt.getOldvalue() + " to " + chgprt.getNewvalue());
					    			}			
					    		}
					    		leonLog.log("END THE CHANGE LOG for (" + entity.getId().getCfecid() + ") - " + entity.getXname());					    		
					    		/*
					    		 * Now go through the source rnw files and make appropriate updates.
					    		 * Only update the rnw file for existing permits. IUP's will not have serial numbers assigned yet.
					    		 */
								for (Iterator<ArenewPermits> dp = prenewlist.iterator(); dp.hasNext();) {
									ArenewPermits srcPermit = (ArenewPermits) dp.next();
					    			if (!(srcPermit.isNewpermit())) {
					    				/*
										 * Not a new permit, so just update the exisiting RNW_PMT record to indicate pending status and confirmation code
										 */
										Query npmt = session.createQuery("update RnwPmt p set p.id.renewalStatus = 'P', p.id.confirmationCode = " +
											"'"+cnum+"' where p.id.permitHolderFileNumber = '"+entity.getId().getCfecid()+"' and p.id.permitYear = " +
											"'"+srcPermit.getId().getPyear()+"' and p.id.XRenewalYear = '"+entity.getId().getRyear()+"' and p.id.permitSerialNumber = " +
											"'"+srcPermit.getId().getSerial()+"' and p.id.fisheryCode = '"+srcPermit.getId().getFishery()+"'");										
										//System.out.println("update RnwPmt p set p.id.renewalStatus = 'P', p.id.confirmationCode = " +
										//		"'"+cnum+"' where p.id.permitHolderFileNumber = '"+entity.getId().getCfecid()+"' and p.id.permitYear = " +
										//		"'"+srcPermit.getId().getPyear()+"' and p.id.XRenewalYear = '"+entity.getId().getRyear()+"' and p.id.permitSerialNumber = " +
										//		"'"+srcPermit.getId().getSerial()+"' and p.id.fisheryCode = '"+srcPermit.getId().getFishery()+"'");										
										npmt.executeUpdate();
									}
								}			
								/*
								 * If this vessel is not a new vessel, then update rnw_ves.
								 * By new, I mean no ADFG number, doesn't exist in our system.
								 */
								for (Iterator<ArenewVessels> dv = vrenewlist.iterator(); dv.hasNext();) {
									ArenewVessels srcVessel = (ArenewVessels) dv.next();
									if (srcVessel.isNewVessel()) {					
										if (srcVessel.getDelorig().equalsIgnoreCase("true")) {
											/*
											 * OK, this vessel exist in our records but is being licensed by a different cfecid account
											 * than last year. This means an existing available record is in rnw_ves for a different cfecid
											 * account holder. We don't want the vessel to be renewed twice or to cause confusion, so delete it.
											 */
											leonLog.log("CFECID " + entity.getId().getCfecid() + " is licensing vessel " + srcVessel.getId().getAdfg() + ", now removing "+entity.getId().getRyear()+" vessel record from previous licensee");
											/*
											 * Send an email to myself to for record purposes after delete. 
											 */
											Query delves = session.createQuery("delete from RnwVes ov where ov.id.adfgNumber = '"+srcVessel.getId().getAdfg()+"' and ov.id.XRenewalYear = '"+entity.getId().getRyear()+"' and ov.id.renewalStatus = 'A'");
											delves.executeUpdate();
											notify.NotifyVesselChange(entity.getId().getCfecid(), srcVessel.getId().getAdfg(), entity.getId().getRyear());
										}
									} else {
										/*
										 * Not a new vessel, so just update the RNW_VES record to indicate pending status and confirmation code.
										 */
							    		Query nves = session.createQuery("update RnwVes v set v.id.renewalStatus = 'P', v.id.confirmationCode = " +
											"'"+cnum+"' where v.id.permitHolderFileNumber = '"+entity.getId().getCfecid()+"' and v.id.vesselYear = " +
											"'"+srcVessel.getVyear()+"' and v.id.XRenewalYear = '"+entity.getId().getRyear()+"' and v.id.adfgNumber = " +
							    			"'"+srcVessel.getId().getAdfg()+"'");
										nves.executeUpdate();
									}
								}
								System.out.println(entity.getId().getCfecid() + " getting url confirm code for " + cnum);
								/*
								 * sending confirmation report to user
								 */
						    	url = cc.getConfirmationCode(cnum, entity, pay, prenewlist, vrenewlist, last4, session, thisStagingDir, confirmationPDF, imageLocation);
						    	/*
						    	 * if we have any pending vessels for emboss (vessels not licensed that have been specified on renewed permits),
						    	 * send a notification to the user
						    	 */
						    	/*if (entity.isManual()) {
						    		if (sendLetters && autoForms) {
						    			notify.sendNoticePending(entity.getEmail(), pay.getReceiveddate().toString(), 
							    				"blank", "Pending Vessel Notice", cnum, entity.getId().getRyear(), entity, leonletterOutPDF);
						    			System.out.println(entity.getId().getCfecid() + " sending pending emboss email reminder for " + cnum);
						    			leonLog.log(entity.getId().getCfecid() + " sending pending emboss email reminder for " + cnum);
						    		}						    		
						    	}*/
						    	/*
						    	 * Do we have any other letters to send user (reducedFees, nonUS cititenship, PovertyFees, Agent declaration)
						    	 */
						    	/*if (halReducedLtr || sabReducedLtr || nonUScitLtr || povertyLtr || agentLtr) {
							    	if (sendLetters && autoForms) {
							    		
							    		 * created a class in notify to receive the 5 boolean expressions for letters.
							    		 * Iterate through and draft email letter with generic reminder(s)
							    		 * Specify the myinfo distribution email account as the return address
							    		 						    		
							    		notify.sendMiscLetters(entity.getEmail(), pay.getReceiveddate().toString(), "blank", "Additional Documentation Notice", 
								    			cnum, entity.getId().getRyear(), entity, halReducedLtr, sabReducedLtr, nonUScitLtr, povertyLtr, agentLtr, leonletterOutPDF);
								    	System.out.println(entity.getId().getCfecid() + " sending additional documentation email reminder for " + cnum);
								    	leonLog.log(entity.getId().getCfecid() + " sending additional documentation email reminder for " + cnum);						    		
							    	}
						    	}*/
						    	tx.commit();
							}
						}
					} catch(Exception e) {
						notify.ProcessMajorError(e, entity.getId().getCfecid(), "com.cfecweb.leon.server.ProcessOrder.ProcessOrder()");
						leonLog.log("error " + e.getMessage());
						e.printStackTrace();
						System.out.println(e);
						url = "error";
					} finally {
						if (url.equalsIgnoreCase("error")) {
							leonLog.log("AN ERROR HAS OCCURED processing order for CFECID " + entity.getId().getCfecid() + ", an email has been sent to an administrator");
							leonLog.log("The record(s) for CFECID " + entity.getId().getCfecid() + ", confirmation code "+cnum+" HAVE NOT been recorded");
							tx.rollback();
						} else if (url.equalsIgnoreCase("declined")) {
							leonLog.log("The credit card has been declined for CFECID " + entity.getId().getCfecid() + ", an email has been sent to an administrator");
							leonLog.log("The record(s) for CFECID " + entity.getId().getCfecid() + ", confirmation code "+cnum+" HAVE NOT been recorded");
							tx.rollback();
						} else {
							if (!(entity.getEmail() == null)) {
                                try {
                                    if (!(manual)) {
                                        System.out.println(entity.getId().getCfecid() + " Email notificatiom for auto processing");
                                        notify.sendNoticeAuto(entity.getEmail(), pay.getReceiveddate().toString(), "N/A", "Your CFEC online renewal order has been processed",
                                                cnum, entity.getId().getRyear(), confirmationPDF, thisReceiptDir);
                                        leonLog.log("Sending email confirmation and receipt to " + entity.getEmail() + " for CFECID " + entity.getId().getCfecid());
                                    } else {
                                        System.out.println(entity.getId().getCfecid() + " Email notificatiom for manual processing");
                                        notify.sendNoticeManual(entity.getEmail(), pay.getReceiveddate().toString(), "N/A", "Your CFEC online renewal order has been processed",
                                                cnum, entity.getId().getRyear(), confirmationPDF);
                                        leonLog.log("Sending email confirmation to " + entity.getEmail() + " for CFECID " + entity.getId().getCfecid());
                                    }
                                } catch (Exception e) {
                                    String message = String.format("Could NOT send email confirmation to CFECID %s. Error: %s", entity.getId().getCfecid(), e.getMessage());
                                    System.out.println(message);
                                    leonLog.log(message);
                                }
							} else {
								leonLog.log("Could NOT send email confirmation to CFECID " + entity.getId().getCfecid() + " due to no email account on file");
							}
							/*
					    	 *  We need to get this shortform PDF report added and/or merged and indexed into the 
					    	 *  F drive Licensing FileRoom sub-directory from hook, web1 and web2.
					    	 *  We have compiled jasper files at both prod and dev locations.
					    	 *  The method works fine, need to create some cron script to clean out the
					    	 *  /webapps/out/LEONDev/'year'/ staging and shortforms directories after a couple of days.
					    	 */
							/*
							 * Shortform creation for watermarked document, both PDF and PCL files are generated here.
							 * The PCL is used for automatic processing to a print queue
							 */
							if (autoForms) {
								//System.out.println(entity.getId().getCfecid() + " building and indexing shortform to SOLR for " + cnum);
								//leonLog.log(entity.getId().getCfecid() + " building and indexing shortform to SOLR for " + cnum);
								System.out.println(entity.getId().getCfecid() + " creating a shortform PDF file to " + shortfileOutPDF + " for " + cnum);
								leonLog.log(entity.getId().getCfecid() + " creating a shortform PDF file to " + shortfileOutPDF + " for " + cnum);
								sf.getShortFormFile(cnum, leonLog, dataImpl, manual, rjnumber, entity.getId().getCfecid(), jrxmlLocation, shortfileOutPDF, imageLocation, thisStagingDir, entity);
								//System.out.println(entity.getId().getCfecid() + " creating a shortform PCL file to " + shortfileOutPCL + " for " + cnum);
								//leonLog.log(entity.getId().getCfecid() + " creating a shortform PCL file to " + shortfileOutPCL + " for " + cnum);
								////if (!(manual)) {
									/*if (sendPCL) {
										String sfArray[] = {"gs", "-dSAFER", "-dBATCH", "-dNOPAUSE", "-sDEVICE=ljet4", 
												"-sOutputFile="+allformsOutPCL+"/"+cnum+"-SF.pcl", ""+shortfileOutPDF+"/"+cnum+".pdf"};
										try {
											Process process = Runtime.getRuntime().exec(sfArray);
										} catch (IOException e) {
											leonLog.log(entity.getId().getCfecid() + " error building shortform PCL " + cnum);
											e.printStackTrace();
										}
									}*/
								////}
									
								/*
								 * Vessel receipt creation, both PDF and PCL files are generated here IF a vessel was licensed
								 */
								//boolean vrfound = false;
								/*
								 * If auto-processing is set for this transaction, build the vessel receipt (if necessary) and inserter
								 */
								////if (!(manual)) {							
									/*try {
										System.out.println(entity.getId().getCfecid() + " building the vessel receipt(s), if exist, for " + cnum);
										leonLog.log(entity.getId().getCfecid() + " building vessel receipt(s), if exist, for " + cnum);
										System.out.println(entity.getId().getCfecid() + " vrenewlist size is " +vrenewlist.size()+ " for " + cnum);
										if (vrenewlist.size() > 0) {
											vrfound = vr.getVesReceipt(cnum, vesreceptOutPDF, entity.getId().getCfecid(), thisLicYear, vrenewlist, thisProcess, entity);
										}										
									} catch (IOException e) {
										System.out.println(entity.getId().getCfecid() + " exception thrown in Vessel Receipt creation");
										e.printStackTrace();
									} finally {
										if (vrfound && sendPCL) {
											System.out.println(entity.getId().getCfecid() + " creating a vessel receipt PCL file to " + vesreceptOutPCL + " for " + cnum);
											String receiptArray[] = {"gs", "-dSAFER", "-dBATCH", "-dNOPAUSE", "-sDEVICE=ljet4", 
													"-sOutputFile="+allformsOutPCL+"/"+cnum+"-VR.pcl", ""+vesreceptOutPDF+"/"+cnum+".pdf"};
											try {
												Process process = Runtime.getRuntime().exec(receiptArray);
											} catch (IOException e) {
												leonLog.log(entity.getId().getCfecid() + " error building vessel receipt(s) PCL " + cnum);
												e.printStackTrace();
											}
										}									
									}*/							
									/*
								     * Inserter creation, both PDF and PCL files are generated here
								     */
									/*System.out.println(entity.getId().getCfecid() + " building the inserter for " + cnum);
									leonLog.log(entity.getId().getCfecid() + " building the inserter for " + cnum);
									try { 
										inf.getInserter(cnum, thisProcess, inserterOutPDF, entity, pay);
									} catch (IOException e) {
										System.out.println(entity.getId().getCfecid() + " exception thrown in inserter PCL " + cnum);
										e.printStackTrace();
									} finally {
										if (sendPCL) {
											System.out.println(entity.getId().getCfecid() + " creating an inserter PCL file to " + inserterOutPCL + " for " + cnum);
											String inserterArray[] = {"gs", "-dSAFER", "-dBATCH", "-dNOPAUSE", "-sDEVICE=ljet4", 
													"-sOutputFile="+allformsOutPCL+"/"+cnum+"-IN.pcl", ""+inserterOutPDF+"/"+cnum+".pdf"};
											try {
												Process process = Runtime.getRuntime().exec(inserterArray);
											} catch (IOException e) {
												leonLog.log(entity.getId().getCfecid() + " error building the inserter PCL" + cnum);
												e.printStackTrace();
											}
										}
									}*/	
									/*
									 * 
									 */
									//System.out.println(entity.getId().getCfecid() + " starting batch job process for  " + cnum);
									//leonLog.log(entity.getId().getCfecid() + " starting batch job process for  " + cnum);
									//bp.insertUpdate(url, leonLog, dataImpl, batchprocess, batchjobname);
									
								////}
							}
							notify.sendCompletionNotice(cnum, entity, pay, prenewlist, vrenewlist);
						}						
						returl = true;
						if (session.isOpen()) {
							session.close();
						}
					}		
		    	//}
		//	}
		//}, 0, 1000);		
		//while (!(returl)) {
		//	try {
		//		Thread.sleep(1000);
		//	} catch (InterruptedException e) {
		//		e.printStackTrace();
		//		System.out.println(entity.getId().getCfecid() + " exception thrown in thread sleep mode for returl");
		//	}
		//}
		
		//System.out.println(entity.getId().getCfecid() + " Deleting the lock file after process regardless of outcome, if exists");
		//leonLog.log(entity.getId().getCfecid() + " Deleting the lock file after process regardless of outcome, if exists");
		//if (lock.exists()) {
		//	lock.delete();
		//}
		url2 = url;
		System.out.println(entity.getId().getCfecid() + " final value of url2 before return is " + url2);
		returl = false;

        ClientPaymentContext clientPaymentContext = StoredPaymentContext.toClientPaymentContext(url2, spc);
        // Save to "finalized" file
        saveClientPaymentContext(paymentFinalizedFile, clientPaymentContext);

        return clientPaymentContext;
	}
	
	/*
	 * LEON makes a copy of the user's last (entity, payment, vessel, permit, changes) object that it uses to determine present values.
	 * If one of those values is changed, it's easy enough to detect that using GXT listeners and then record that change in a table
	 * along with new and old values. This is pretty nice for the licensing staff because we can then use that table to highlight categories
	 * and associated values that the user modified. The problem came when a user modified a record to a different value, then modified that
	 * record again BACK to the original value. LEON assumed (correctly) a change had been performed and proceeded to highlight that field
	 * on reports. Since I can't assume to know what the user's intent was, the only possible solution (short of re-writing major sections of
	 * the code base) is to compare the post-LEON object with the pre-LEON object. I don't like doing this for several reasons, most of all 
	 * for the possibility of introducing more complexity and more bug potential.
	 */
	/*
	 * This method compares post-LEON Vessel changes to their pre-LEON values
	 */
	public List<ArenewChanges> verifyVesChange(List<ArenewChanges> chg, Session session, Logging leonLog, List<ArenewVessels> vrenewlist) {
		List<ArenewChanges> chg2 = null;
		if (vrenewlist.size() > 0) {
			chg2 = new ArrayList();
			chg2.addAll(chg);
			for (Iterator vc = vrenewlist.iterator(); vc.hasNext();) {
				ArenewVessels vessel = (ArenewVessels) vc.next();
				if (!(vessel.getId().getAdfg().startsWith("TEMP"))) {
					StringBuffer myquery1 = new StringBuffer(
						"select COALESCE(ov.vessel_name, 'N/A'), COALESCE(ov.coast_guard_number, 'N/A'), COALESCE(ov.adfg_number, 'N/A'), 'fee', ov.vessel_year, " +
						"ov.year_built, COALESCE(ov.make, 'N/A'), COALESCE(ov.length_feet, '0'), COALESCE(ov.length_inches, '0'), ov.length_verified, COALESCE(ov.gross_tons::text, '0'), " +
						"COALESCE(ov.net_tons::text, '0'), COALESCE(ov.homeport_city, 'N/A'), COALESCE(ov.homeport_state, 'N/A'), COALESCE(ov.engine_type, 'N/A'), COALESCE(ov.horsepower::text, '0'), " +
						"COALESCE(ov.value::text, '0'), COALESCE(ov.hull_type, 'N/A'), COALESCE(ov.hull_id, 'N/A'), COALESCE(ov.fuel_capacity::text, '0'), COALESCE(ov.refrigeration_flag, 'N/A'), " +
						"COALESCE(ov.live_tank_capacity::text, '0'), COALESCE(ov.hold_tank_capacity::text, '0'), COALESCE(ov.salmon_troll_reg, 'N/A'), COALESCE(ov.salmon_troll_date, null), " +
						"COALESCE(ov.purse_seine_flag, 'N'), COALESCE(ov.beach_seine_flag, 'N'), COALESCE(ov.drift_gillnet_flag, 'N'), COALESCE(ov.set_gillnet_flag, 'N'), " +
						"COALESCE(ov.hand_troll_flag, 'N'), COALESCE(ov.longline_flag, 'N'), COALESCE(ov.fishwheel_flag, 'N'), COALESCE(ov.single_otter_trawl_flag, 'N'), " +
						"COALESCE(ov.pot_gear_flag, 'N'), COALESCE(ov.ring_net_flag, 'N'), COALESCE(ov.beam_trawl_flag, 'N'), COALESCE(ov.dredge_flag, 'N'), COALESCE(ov.dinglebar_flag, 'N'), " +
						"COALESCE(ov.jig_flag, 'N'), COALESCE(ov.double_otter_trawl, 'N'), COALESCE(ov.hearing_gillnet_flag, 'N'), COALESCE(ov.pair_trawl_flag, 'N'), " +
						"COALESCE(ov.other_gear_flag, 'N'), COALESCE(ov.fishing_boat_flag, 'N'), COALESCE(ov.freezer_canner_flag, 'N'), COALESCE(ov.tender_packer_flag, 'N'), " +
						"'transporter', COALESCE(ov.foreign_flag_indicator, 'N'), COALESCE(ov.charter_boat_flag, 'N'), COALESCE(ov.dive_gear_flag, 'N'), COALESCE(ov.power_troll_flag, 'N'), " +
						"COALESCE(ov.salmon_reg_area, 'N/A'), COALESCE(ov.permit_serial_1, 'N/A'), COALESCE(ov.permit_serial_2, 'N/A'), 'renewalStatus', 'confirmcode', " +
						"ov.owner_file_number from tier2.vess_2300_view ov where ov.adfg_number = '" + vessel.getId().getAdfg() + "' order by ov.vessel_year desc");
					List list1 = session.createNativeQuery(myquery1.toString()).setFirstResult(0).setMaxResults(1).list();
					for (Iterator i = list1.iterator(); i.hasNext();) {
						Object[] rs1 = (Object[]) i.next();
						for (Iterator ct = chg.iterator(); ct.hasNext();) {
							ArenewChanges change = (ArenewChanges) ct.next();
							/*
							 * If this change record deals with Vessels AND it is the current vessel, then proceed.
							 */
							if (change.getType().equalsIgnoreCase("Vessels") && change.getObject().equalsIgnoreCase(vessel.getId().getAdfg())) {
								/*
								 * Check the string data
								 */
								if (change.getId().getAttribute().equalsIgnoreCase("vesselName")) {
									if (vessel.getName().equalsIgnoreCase((String) rs1[0])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("grossTons")) {
									if (vessel.getGrossTons().equalsIgnoreCase((String) rs1[10])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("netTons")) {
									if (vessel.getNetTons().equalsIgnoreCase((String) rs1[11])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("homeportCity")) {
									if (vessel.getHomeportCity().equalsIgnoreCase((String) rs1[12])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("homeportState")) {
									if (vessel.getHomeportState().equalsIgnoreCase((String) rs1[13])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("engineType")) {
									if (vessel.getEngineType().equalsIgnoreCase((String) rs1[14])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("horsePower")) {
									if (vessel.getHorsepower().equalsIgnoreCase((String) rs1[15])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("estValue")) {
									if (vessel.getEstValue().equalsIgnoreCase((String) rs1[16])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("fuelCapacity")) {
									if (vessel.getFuel().equalsIgnoreCase((String) rs1[19])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("refrigeration")) {
									if (vessel.getRefrigeration().equalsIgnoreCase((String) rs1[20])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("liveCapacity")) {
									if (vessel.getLiveTank().equalsIgnoreCase((String) rs1[21])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("holdCapacity")) {
									if (vessel.getHoldTank().equalsIgnoreCase((String) rs1[22])) {
										chg2.remove(change);
									}
								}
								/*
								 * Check salmon troll registration date and type
								 */
								else if (change.getId().getAttribute().startsWith("salmonTrollReg")) {
									if (vessel.getSalmontrollReg().equalsIgnoreCase((String) rs1[23])) {
										chg2.remove(change);
									}
								}
								/*
								 * Check gear codes
								 */
								else if (change.getId().getAttribute().equalsIgnoreCase("purseSeine")) {
									if (vessel.getPurseseine().equalsIgnoreCase((String) rs1[25])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("beachSeine")) {
									if (vessel.getBeachseine().equalsIgnoreCase((String) rs1[26])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("driftGillNet")) {
									if (vessel.getDriftgillnet().equalsIgnoreCase((String) rs1[27])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("setGillNet")) {
									if (vessel.getSetgillnet().equalsIgnoreCase((String) rs1[28])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("handTroll")) {
									if (vessel.getHandtroll().equalsIgnoreCase((String) rs1[29])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("longLine")) {
									if (vessel.getLongline().equalsIgnoreCase((String) rs1[30])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("fishWheel")) {
									if (vessel.getFishwheel().equalsIgnoreCase((String) rs1[31])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("singleOtterTrawl")) {
									if (vessel.getSingleottertrawl().equalsIgnoreCase((String) rs1[32])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("potGear")) {
									if (vessel.getPotgear().equalsIgnoreCase((String) rs1[33])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("ringNet")) {
									if (vessel.getRingnet().equalsIgnoreCase((String) rs1[34])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("diveGear")) {
									if (vessel.getDivegear().equalsIgnoreCase((String) rs1[49])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("powerTroll")) {
									if (vessel.getPowertroll().equalsIgnoreCase((String) rs1[50])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("beamTrawl")) {
									if (vessel.getBeamtrawl().equalsIgnoreCase((String) rs1[35])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("dredge")) {
									if (vessel.getDredge().equalsIgnoreCase((String) rs1[36])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("dingleBar")) {
									if (vessel.getDinglebar().equalsIgnoreCase((String) rs1[37])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("jig")) {
									if (vessel.getJig().equalsIgnoreCase((String) rs1[38])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("doubleOtterTrawl")) {
									if (vessel.getDoubleottertrawl().equalsIgnoreCase((String) rs1[39])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("herringGillNet")) {
									if (vessel.getHearinggillnet().equalsIgnoreCase((String) rs1[40])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("pairTrawl")) {
									if (vessel.getPairtrawl().equalsIgnoreCase((String) rs1[41])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("otherGear")) {
									if (vessel.getOthergear().equalsIgnoreCase((String) rs1[42])) {
										chg2.remove(change);
									}
								}
								/*
								 * Check activity codes
								 */
								else if (change.getId().getAttribute().equalsIgnoreCase("fishingBoat")) {
									if (vessel.getFishingboat().equalsIgnoreCase((String) rs1[43])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("freezerCanner")) {
									if (vessel.getFreezerCanner().equalsIgnoreCase((String) rs1[44])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("tenderPacker")) {
									if (vessel.getTenderPacker().equalsIgnoreCase((String) rs1[45])) {
										chg2.remove(change);
									}
								}
								//	TODO	uncomment the transporter check after we are able to query for it.
								//else if (change.getId().getAttribute().equalsIgnoreCase("transporter")) {
								//	if (vessel.getTransporter().equalsIgnoreCase((String) rs1[46])) {
								//		chg2.remove(change);
								//	}
								//}
								/*
								 * Check salmon net registration area and permit(s)
								 */
								else if (change.getId().getAttribute().equalsIgnoreCase("salmonNetRegArea")) {
									if (vessel.getSalmonregArea().equalsIgnoreCase((String) rs1[51])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("salmonPermit1")) {
									if (vessel.getPermitSerial1().equalsIgnoreCase((String) rs1[52])) {
										chg2.remove(change);
									}
								} else if (change.getId().getAttribute().equalsIgnoreCase("salmonPermit2")) {
									if (vessel.getPermitSerial2().equalsIgnoreCase((String) rs1[53])) {
										chg2.remove(change);
									}
								}
							}
						}
					}					
				}				
			}
		} else {
			chg2 = new ArrayList();
			chg2.addAll(chg);
		}		
		return chg2;
	}
	/*
	 * This method compares post-LEON Permit changes to their pre-LEON values. (Note: the only Permit changes allowed are ADFG numbers)
	 */
	public List<ArenewChanges> verifyPmtChange(List<ArenewChanges> chg, Session session, Logging leonLog, List<ArenewPermits> prenewlist, String cyear) {
		List<ArenewChanges> chg3 = null;
		int y = 1;
		if (prenewlist.size() > 0) {
			chg3 = new ArrayList();
			chg3.addAll(chg);
			for (Iterator pc = prenewlist.iterator(); pc.hasNext();) {
				ArenewPermits permit = (ArenewPermits) pc.next();
				/*
				 * Ignore IUP with no serial number
				 */
				if (!(permit.getId().getSerial().equalsIgnoreCase("Not Issued"))) {
					/*
					 * Ignore permits from previous years
					 */
					if (permit.getId().getPyear().equalsIgnoreCase(cyear)) {
						StringBuffer myquery1 = new StringBuffer(
							"select op.p_serial, op.p_fshy, op.p_adfg from tier2.pmts_1300_view op where op.p_serial = '"+permit.getId().getSerial()+"' " +
							"order by op.p_year desc, op.p_pmt_seq desc");
						List list1 = session.createNativeQuery(myquery1.toString()).setFirstResult(0).setMaxResults(1).list();
						for (Iterator i = list1.iterator(); i.hasNext();) {
							Object[] rs1 = (Object[]) i.next();
							for (Iterator ct = chg.iterator(); ct.hasNext();) {
								ArenewChanges change = (ArenewChanges) ct.next();
								/*
								 * If this change record deals with Permits AND it is the current permit serial number, then proceed.
								 */
								if (change.getType().equalsIgnoreCase("Permits") && change.getObject().equalsIgnoreCase(permit.getId().getSerial())) {
									/*
									 * Are we dealing with a Permit ADFG number change?
									 */
									if (change.getId().getAttribute().equalsIgnoreCase("pAdfg")) {
										/*
										 * If so, does the final LEON permit record for this serial number show the same Vessel ADFG number listed
										 * as their last know record (from the query above)?
										 */
										if (permit.getAdfg().equalsIgnoreCase((String) rs1[2])) {
											/*
											 * Yes it does, so lets remove this change record as if it never occurred.
											 */
											leonLog.log(permit.getId().getCfecid() + " has changed ADFG value for serial " + change.getObject() + " to last know recod " + (String) rs1[2] + ". Record will be discarded.");
											//System.out.println(permit.getId().getCfecid() + " has changed ADFG value for serial " + change.getObject() + " to last know recod " + (String) rs1[2] + ". Record will be discarded.");
											chg3.remove(change);
										}
									}
								}
							}
						}	
					}
				}
			}
		} else {
			chg3 = new ArrayList();
			chg3.addAll(chg);
		}		
		
		/*
		 * We are attaching temporary sequence numbers to the change records. These will be used in the method
		 * verifyPmtChange2 as a means to discover multiple changes to the same serial number. Permanent sequence
		 * numbers will be added just prior to actually writing the table. We do this so the final table doesn't
		 * show gaps in sequence numbers because there is still a chance that some of these change records will
		 * be discarded as dupes.
		 */
		for (Iterator fc = chg3.iterator(); fc.hasNext();) {
			ArenewChanges addseq = (ArenewChanges) fc.next();
			addseq.getId().setSeq(Integer.toString(y));
			y++;
		}
		
		return chg3;
	}
	
	/*
	 * This method finds potential duplicate records in the change table for Permit serial numbers (ADFG updates), then removes all of them except the most current seq.
	 * How this happens. If a permit holder changes the permit ADFG number more than once for the same permit, multiple records will be generated in the
	 * the change table for the same permit. When the shortform is created, it will see those records and assume multiple permits must exist.
	 */
	public List<ArenewChanges> verifyPmtChange2(List<ArenewChanges> chg, Session session, Logging leonLog, List<ArenewPermits> prenewlist, String cyear) {
		List<ArenewChanges> chg4 = null;
		List<ArenewChanges> chgtmp = null;
		chg4 = new ArrayList();
		chgtmp = new ArrayList();
		chg4.addAll(chg);
		chgtmp.addAll(chg);
		
		for (Iterator gc = chg.iterator(); gc.hasNext();) {
			ArenewChanges change = (ArenewChanges) gc.next();
			/*
			 * Find change records for permit ADFG number
			 */
			if (change.getId().getAttribute().equalsIgnoreCase("pAdfg")) {
				/*
				 * OK, now iterate through an exact copy of this list and find change records for permit ADFG
				 */
				for (Iterator gt = chgtmp.iterator(); gt.hasNext();) {
					ArenewChanges temp = (ArenewChanges) gt.next();
					if (temp.getId().getAttribute().equalsIgnoreCase("pAdfg")) {
						/*
						 * Are these 2 records dealing with the same object (Permit Serial Number)?
						 */
						if (temp.getObject().equalsIgnoreCase(change.getObject())) {
							/*
							 * If so, is the sequence number from the temp list greater than the current record?
							 */
							if (Integer.parseInt(temp.getId().getSeq()) > Integer.parseInt(change.getId().getSeq())) {
								/*
								 * If so, then we know that this serial number has changed permit ADFG number more than once.
								 * In that case, remove the current record (lower sequence number) from the list we will eventually
								 * send back, then go back to the iteration and look for more.
								 */
								leonLog.log(prenewlist.get(0).getId().getCfecid() + " has changed ADFG value for serial " + change.getObject() + " multiple times. Discarding previous change " + change.getNewvalue());
								//System.out.println(prenewlist.get(0).getId().getCfecid() + " has changed ADFG value for serial " + change.getObject() + " multiple times. Discarding previous change " + change.getNewvalue());
								chg4.remove(change);
							}
						}
					}
				}
			}
		}
			
		return chg4;
	}	
	
	
	/*
	 * This method process changes (updates to a particular record during renewal, such as an address change,
	 * or an ADFG number for a vessel).
	 */
	public String processChanges(String id, String ryear, List<ArenewChanges> chg, getDataImpl dataImpl, Logging leonLog) {
		String results = "true";
		Session session = null;
	    Transaction tx = null;
	    try {
	    	session = dataImpl.fact.openSession();
	    	Date sdate = getDbDate(session);
	    	tx = session.beginTransaction();
	    	ArenewEntityId entId = new ArenewEntityId();
			entId.setRyear(ryear);
			entId.setCfecid(id);
			ArenewEntity entity = (ArenewEntity)session.get(ArenewEntity.class, entId);
	    	for (int x=0;x<chg.size();x++) {
	    		if (chg.get(x).getType().equalsIgnoreCase("Intent")) {
	    			Query npmt = session.createQuery("update ArenewPermits p set p.intend = '"+chg.get(x).getNewvalue()+"' " +
    					"where p.id.cfecid = '"+entity.getId().getCfecid().toUpperCase()+"' " +
    					"and p.id.ryear = '"+entity.getId().getRyear()+"' and p.id.serial = '"+chg.get(x).getId().getAttribute()+"'");
					npmt.executeUpdate();
	    		}
	    		if (chg.get(x).getId().getAttribute().equalsIgnoreCase("PermMailingAddress")) {
	    			if (chg.get(x).getNewvalue().equalsIgnoreCase("Empty")) {
	    				entity.setPaddress(null);
	    			} else {
	    				entity.setPaddress(chg.get(x).getNewvalue());
	    			}
	    			entity.setUpdatedate(sdate);
	    		} else if (chg.get(x).getId().getAttribute().equalsIgnoreCase("PermMailingAddress2")) {
	    			if (chg.get(x).getNewvalue().equalsIgnoreCase("Empty")) {
	    				entity.setPaddress2(null);
	    			} else {
	    				entity.setPaddress2(chg.get(x).getNewvalue());
	    			}	    			
	    			entity.setUpdatedate(sdate);
	    		} else if (chg.get(x).getId().getAttribute().equalsIgnoreCase("PermMailingCity")) {
	    			if (chg.get(x).getNewvalue().equalsIgnoreCase("Empty")) {
	    				entity.setPcity(null);
	    			} else {
	    				entity.setPcity(chg.get(x).getNewvalue());
	    			}	    			
	    			entity.setUpdatedate(sdate);
	    		} else if (chg.get(x).getId().getAttribute().equalsIgnoreCase("PermMailingState")) {
	    			if (chg.get(x).getNewvalue().equalsIgnoreCase("Empty")) {
	    				entity.setPstate(null);
	    			} else {
	    				entity.setPstate(chg.get(x).getNewvalue());
	    			}	    			
	    			entity.setUpdatedate(sdate);
	    		} else if (chg.get(x).getId().getAttribute().equalsIgnoreCase("PermMailingZip")) {
	    			if (chg.get(x).getNewvalue().equalsIgnoreCase("Empty")) {
	    				entity.setPzip(null);
	    			} else {
	    				entity.setPzip(chg.get(x).getNewvalue());
	    			}	    			
	    			entity.setUpdatedate(sdate);
	    		} else if (chg.get(x).getId().getAttribute().equalsIgnoreCase("PhysMailingAddress")) {
	    			if (chg.get(x).getNewvalue().equalsIgnoreCase("Empty")) {
	    				entity.setRaddress(null);
	    			} else {
	    				entity.setRaddress(chg.get(x).getNewvalue());
	    			}
	    			entity.setUpdatedate(sdate);
	    		} else if (chg.get(x).getId().getAttribute().equalsIgnoreCase("PhysMailingAddress2")) {
	    			if (chg.get(x).getNewvalue().equalsIgnoreCase("Empty")) {
	    				entity.setRaddress2(null);
	    			} else {
	    				entity.setRaddress2(chg.get(x).getNewvalue());
	    			}	    			
	    			entity.setUpdatedate(sdate);
	    		} else if (chg.get(x).getId().getAttribute().equalsIgnoreCase("PhysMailingCity")) {
	    			if (chg.get(x).getNewvalue().equalsIgnoreCase("Empty")) {
	    				entity.setRcity(null);
	    			} else {
	    				entity.setRcity(chg.get(x).getNewvalue());
	    			}	    			
	    			entity.setUpdatedate(sdate);
	    		} else if (chg.get(x).getId().getAttribute().equalsIgnoreCase("PhysMailingState")) {
	    			if (chg.get(x).getNewvalue().equalsIgnoreCase("Empty")) {
	    				entity.setRstate(null);
	    			} else {
	    				entity.setRstate(chg.get(x).getNewvalue());
	    			}	    			
	    			entity.setUpdatedate(sdate);
	    		} else if (chg.get(x).getId().getAttribute().equalsIgnoreCase("PhysMailingZip")) {
	    			if (chg.get(x).getNewvalue().equalsIgnoreCase("Empty")) {
	    				entity.setRzip(null);
	    			} else {
	    				entity.setRzip(chg.get(x).getNewvalue());
	    			}	    			
	    			entity.setUpdatedate(sdate);
	    		} else if (chg.get(x).getId().getAttribute().equalsIgnoreCase("phoneArea")) {
	    			entity.setArea(chg.get(x).getNewvalue());
	    			entity.setUpdatedate(sdate);
	    		} else if (chg.get(x).getId().getAttribute().equalsIgnoreCase("phonePre")) {
	    			String phone = null;
	    			if (!(entity.getPhone() == null)) {
	    				phone = chg.get(x).getNewvalue() + entity.getPhone().substring(3, 7);
	    				entity.setUpdatedate(sdate);
	    			} else {
	    				phone = chg.get(x).getNewvalue() + "0000";
	    			}
	    			entity.setPhone(phone);
	    		} else if (chg.get(x).getId().getAttribute().equalsIgnoreCase("phonePost")) {
	    			String phone = null;
	    			if (!(entity.getPhone() == null)) {
	    				phone = entity.getPhone().substring(0, 3) + chg.get(x).getNewvalue();
	    				entity.setUpdatedate(sdate);
	    			} else {
	    				phone = "000" + chg.get(x).getNewvalue();
	    			}
	    			entity.setPhone(phone);
	    		} else if (chg.get(x).getId().getAttribute().equalsIgnoreCase("email")) {
	    			entity.setEmail(chg.get(x).getNewvalue());
	    			entity.setUpdatedate(sdate);
	    		}
	    	}
	    	leonLog.log(id + " id updating existing Entity record");
            entity = ArenewEntityMaterializer.saveOrUpdate(session, entity);
	    	tx.commit();
	    	session.close();
	    } catch(Exception e){
	    	notify.ProcessMajorError(e, id, "com.cfecweb.leon.server.ProcessOrder.processChanges()");
	    	tx.rollback();
	    	results = "bad";
		} finally {
			if (!(results.equalsIgnoreCase("bad"))) {
				if (session.isOpen()) {
					session.flush();
					session.close();
				} 
			} else {			
				leonLog.log("AN ERROR HAS OCCURED processing changes for CFECID " + id + ", an email has been sent to an administrator");
				leonLog.log("The record changes for CFECID " + id + " HAVE NOT been recorded");
				if (session.isOpen()) {
					session.close();
				} 
			}
		}		
		return results;
	}

}
