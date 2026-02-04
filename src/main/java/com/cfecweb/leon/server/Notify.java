package com.cfecweb.leon.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cfecweb.leon.shared.ArenewEntity;
import com.cfecweb.leon.shared.ArenewPayment;
import com.cfecweb.leon.shared.ArenewPermits;
import com.cfecweb.leon.shared.ArenewVessels;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/*
 * This is my javax class, which handles any SMTP event within LEON
 */

public class Notify {

	// Create a logger with the name of the current class
    private static final Logger logger = LogManager.getLogger(Notify.class);
	
	/*
	 * a method to automatically notify a user they will need to provide additional documentation for one
	 * of the following circumstances:
	 * Reduced Halibut or Sablefish fee by their choice
	 * Non-US citizenship declaration 
	 * Poverty declaration
	 * Agent declaration
	 */
	public void sendMiscLetters(String emailaddress, String curdate, String comment, String nsubject, String cn, String year, 
			ArenewEntity entity, boolean halreduced, boolean sabreduced, boolean nonus, boolean poverty, boolean agent, String leonletterOut) {
		try {
			StringBuffer misc = new StringBuffer("");
			String to = emailaddress;
            String from = "DFG.CFEC.myinfo@alaska.gov";
            String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
			logger.info("Using Email host: " + host);
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            Session session = Session.getInstance(props, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setSubject(nsubject+'\r');
            /*
             * iterate through possibilities here and append to stringbuffer
             */
            if (halreduced || sabreduced) {
            	misc.append("A Halibut and/or Sablefish permit was selected for reduced fees, please submit require documentation" +'\r');
            }
            if (nonus) {
            	misc.append("Non US Citizenship was selected, please submit require documentation" +'\r');
            }
            if (poverty) {
            	misc.append("Poverty Fees was selected, please submit require documentation" +'\r');
            }
            if (agent) {
            	misc.append("An Agent declaration was selected, please submit require documentation" +'\r');
            }
            message.setText("An online CFEC renewal was recently completed for " + entity.getXname() + ", confirmation number " + cn+'\r'+'\r' +
               		"It looks like the following items were selected and will require additional documentation:"+'\r'+'\r'+'\r' +
            		misc.toString()+'\r'+'\r' +
            		"Permit Cards or Vessel Licenses cannot be processed and shipped without supporting documentation for these items"+'\r'+'\r' +
              		"If you have any questions and/or concerns, please do not hestiate to contact a licensing agent at 1-907-789-6150. "+'\r'+'\r' +
               		"YOU CAN USE THIS EMAIL ADDRESS TO REPLY AND ATTACH THE SUPPORTING DOCUMENTATION");
           message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
           Transport.send(message);
           createPdf(message.getSubject(), (String) message.getContent(), cn, leonletterOut, entity, curdate, "Additional Documentation Notice", "other");
		} catch (MessagingException me) {
            me.printStackTrace();
        } catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {           
        }
	}
	
	/*
	 * a method to automatically notify a user in the case of a permit renewal that includes an unlicensed vessel
	 */
	public void sendCompletionNotice(String cnum, ArenewEntity entity, ArenewPayment pay, List<ArenewPermits> pmt2, List<ArenewVessels> ves2) {
		try {
			Date now = new Date();
			String to = "brant.oliphant@alaska.gov";
			//String to2 = "brant.oliphant@alaska.gov";
	    	String from = "DFG.CFEC.Renewal@alaska.gov";
	        String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
			logger.info("Using Email host: " + host);
	        Properties props = new Properties();
            props.put("mail.smtp.host", host);
            Session session = Session.getInstance(props, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setSubject("LEON renewal completion for CFEC ID - " + entity.getId().getCfecid() + " on " + now);
            message.setText(entity.getId().getCfecid() + " has successfully completed an Online Renewal Application"+'\r'+'\r'
					+"The user name is " +entity.getXname()+'\r'
					+"The Confirmation number is " + cnum+'\r'
					+"Number of Permits " +pmt2.size()+'\r'
					+"Number of Vessels "+ves2.size()+'\r'
					+"Payment amount is "+pay.getTotalamount());
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
            Transport.send(message);
            //message.setRecipient(Message.RecipientType.TO,new InternetAddress(to2));
            //Transport.send(message);
		} catch (MessagingException me) {
	        me.printStackTrace();
	    }  finally {           
	    }
	}
	
	/*
	 * This method receives a date, ID and comment from with LEON when an error occurs and captured,
	 * then sends that to a particular set of people at CFEC.
	 */
	public void sendError(String curdate, String id, String comment) {
    	try {
	    	String to = "DFG.CFEC.Renewal.Error@alaska.gov";
	    	String from = "DFG.CFEC.Renewal@alaska.gov";
	        String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
			logger.info("Using Email host: " + host);
	        Properties props = new Properties();
            props.put("mail.smtp.host", host);
            Session session = Session.getInstance(props, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setSubject("LEON unrecoverable system error for CFEC ID - " + id + " on " + curdate);
            message.setText("We have received the following error(s) from CFEC ID " + id + '\r' + comment);
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
            Transport.send(message);
	    } catch (MessagingException me) {
	        me.printStackTrace();
	    }  finally {           
	    }
    }
	
	/*
	 * This method receives an email address, date, comment, subject and confirmation number, then sends
	 * the user (CFECID) an email notification regarding a completed transaction with the confirmation
	 * sheet as an attachment.
	 */
	public void sendNoticePending(String emailaddress, String curdate, String comment, String nsubject, String cn, String year, 
			ArenewEntity entity, String leonletterOut) {
        try {            
           StringBuffer vadfg = new StringBuffer("");
           String to = emailaddress;
           String from = "DFG.CFEC.Renewal@alaska.gov";
           String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
		   logger.info("Using Email host: " + host);
           Properties props = new Properties();
           props.put("mail.smtp.host", host);
           Session session = Session.getInstance(props, null);
           Message message = new MimeMessage(session);
           message.setFrom(new InternetAddress(from));
           message.setSubject(nsubject+'\r');
           for (Iterator<ArenewPermits> p = entity.getArenewPermitses().iterator(); p.hasNext();) {
        	   ArenewPermits permit = (ArenewPermits) p.next();
        	   //System.out.println("serial " + permit.getId().getSerial() + " - new? " + permit.isNewrenew() + " - intent? " + permit.isIntend());
        	   if (permit.isNewrenew()) {
        		   if (permit.isIntend()) {
        			   if (permit.getConfirmcode().equalsIgnoreCase(cn)) {
                		   if (permit.getVlicensed().equalsIgnoreCase("NO")) {
                			   vadfg.append("Permit Serial " + permit.getId().getSerial() + "   -   Vessel " + permit.getAdfg() + " is not licensed" +'\r');
                		   }
                		   System.out.println(entity.getId().getCfecid() + " found an unlicensed vessel " + permit.getAdfg() + " for permit " + permit.getId().getSerial());
                	   }
        		   }
        	   }        	   
           }
           message.setText("An online CFEC renewal was recently completed for " + entity.getXname() + ", confirmation number " + cn+'\r'+'\r' +
              	"It looks like the following Vessels have not been licensed yet, but are specified on 1 or more of the renewed permits:"+'\r'+'\r' +
           		vadfg.toString()+'\r' +
           		"A permit card (that requires a vessel) cannot be embossed or shipped until that vessel has been licensed"+'\r'+'\r' +
           		"You can license this/these vessel(s) using LEON, https://www.cfec.state.ak.us/Leon at anytime"+'\r'+'\r' +
              	"If you have any questions and/or concerns, please do not hestiate to contact a licensing agent at 1-907-789-6150. "+'\r'+'\r' +
              	"************ PLEASE DO NOT REPLY TO THIS EMAIL. ***************");
           message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
           Transport.send(message);
           createPdf(message.getSubject(), (String) message.getContent(), cn, leonletterOut, entity, curdate, "Pending Emboss Notice", "pending");
        } catch (MessagingException me) {
            me.printStackTrace();
        } catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {           
        }
    }
	
	/*
	 * This method receives an email address, date, comment, subject and confirmation number, then sends
	 * the user (CFECID) an email notification regarding a completed transaction with the confirmation
	 * sheet as an attachment.
	 */
	public void sendNoticeAuto(String emailaddress, String curdate, String comment, String nsubject, 
			String cn, String year, String thisConfirmDir, String thisReceiptDir) {
        try {            
           String to = emailaddress;
           String from = "DFG.CFEC.Renewal@alaska.gov";
            String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
			logger.info("Using Email host: " + host);
           // TODO modified for multiple servers
           //String filename1 = "///webapps//out//LEON//"+year+"//confirmations//confirm_"+cn+".pdf";
           String filename1 = thisConfirmDir+"//confirm_"+cn+".pdf";
           //String filename2 = "///webapps//out//LEON//"+year+"//receipts//"+cn+".RCP";
           String filename2 = thisReceiptDir+"//"+cn+".RCP";
           Properties props = new Properties();
           props.put("mail.smtp.host", host);
           Session session = Session.getInstance(props, null);
           Message message = new MimeMessage(session);
           message.setFrom(new InternetAddress(from));
           message.setSubject(nsubject);
           message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
           MimeBodyPart messageBody = new MimeBodyPart();
           messageBody.setText("Please find attached a copy of your recent ("+curdate+") transaction with the CFEC Online Renewal Application. " +
           		"If you have any questions and/or concerns, please do not hestiate to contact a licensing agent at 1-907-789-6150. " +
           		"************ PLEASE DO NOT REPLY TO THIS EMAIL. ***************");
           Multipart multipart = new MimeMultipart();
           multipart.addBodyPart(messageBody);
           messageBody = new MimeBodyPart();
           DataSource source1 = new FileDataSource(filename1);
           messageBody.setDataHandler(new DataHandler(source1));
           messageBody.setFileName("confirm_"+cn+".pdf");
           multipart.addBodyPart(messageBody);
           messageBody = new MimeBodyPart();
           DataSource source2 = new FileDataSource(filename2);
           messageBody.setDataHandler(new DataHandler(source2));
           messageBody.setFileName("receipt_"+cn+".txt");
           multipart.addBodyPart(messageBody);
           message.setContent(multipart);
           Transport.send(message);
        } catch (MessagingException me) {
            me.printStackTrace();
        }  finally {           
        }
    }
	
	/*
	 * This method receives an email address, date, comment, subject and confirmation number, then sends
	 * the user (CFECID) an email notification regarding a completed transaction with the confirmation
	 * sheet as an attachment.
	 */
	public void sendNoticeManual(String emailaddress, String curdate, String comment, String nsubject, 
			String cn, String year, String thisConfirmDir) {
        try {            
           String to = emailaddress;
           String from = "DFG.CFEC.Renewal@alaska.gov";
           String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
		   logger.info("Using Email host: " + host);
           // TODO modified for multiple servers
           String filename = thisConfirmDir+"//confirm_"+cn+".pdf";
           //String filename = "///webapps//out//LEON//"+year+"//confirmations//confirm_"+cn+".pdf";   
           Properties props = new Properties();
           props.put("mail.smtp.host", host);
           Session session = Session.getInstance(props, null);
           Message message = new MimeMessage(session);
           message.setFrom(new InternetAddress(from));
           message.setSubject(nsubject);
           message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
           MimeBodyPart messageBody = new MimeBodyPart();
           messageBody.setText("Please find attached a copy of your recent ("+curdate+") transaction with the CFEC Online Renewal Application. " +
           		"If you have any questions and/or concerns, please do not hestiate to contact a licensing agent at 1-907-789-6150. " +
           		"************ PLEASE DO NOT REPLY TO THIS EMAIL. ***************");
           Multipart multipart = new MimeMultipart();
           multipart.addBodyPart(messageBody);
           messageBody = new MimeBodyPart();
           DataSource source = new FileDataSource(filename);
           messageBody.setDataHandler(new DataHandler(source));
           messageBody.setFileName("confirm_"+cn+".pdf");
           multipart.addBodyPart(messageBody);
           message.setContent(multipart);
           Transport.send(message);
        } catch (MessagingException me) {
            me.printStackTrace();
        }  finally {           
        }
    }
	
	/*
	 This method receives a list or array objects, type String, generated in the server package
	 for custom bulk email communications based on the sql query.
	 LEON has email addresses that are pretty well vetted, and this is a way to use them in lieu
	 of a List Server, which we should still build out.
	 */
	public void bulkCustomNotice(List<String[]> flist, Date sdate) {
		String cfecid = null;
		String email = null;
		String name = null;
		String year = null;
		String[] list = null;
		//String filename = "/home/mcmity/scratch/pdf/publicDistro/2022-dec-to-vessel-owners-re-sewage-disposal.pdf";
		String filename1 = null;
		String filename2 = null;
		//String filename3 = null;
		//String filename = "Alaska_DEC_Notice_Illegal_to_Discharge_Sewage_within_3_miles_of_shore.pdf";
		for (int i = 0; i < flist.size(); i++) {
		//for (int i = 0; i < 1; i++) {
			list = new String[4];
			list = flist.get(i);
			cfecid = list[0];
			email = list[1];
			//email = "trmcmichael@gmail.com";
			//email = "glenn.haight@alaska.gov";
			//email = "nicole.lynch@alaska.gov";
			//email = "brant.oliphant@alaska.gov";
			name = list[2];
			year = list[3];
			System.out.println(i+". " + cfecid + " - " + email + " - " + name + " - " + year);
			/*try {
		    	String to = email;
		    	String from = "DFG.CFEC.Notice@alaska.gov";
		                    String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
			logger.info("Using Email host: " + host);
		        Properties props = new Properties();
	            props.put("mail.smtp.host", host);
	            Session session = Session.getInstance(props, null);
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(from));
	            message.setSubject("CFEC message for " + name);
	            message.setText("****** PLEASE DO NOT REPLY TO THIS EMAIL. ****** \r\rz" +
	        	// name + "  -  CFECID: " + cfecid + "\r\r" +
	        	// "Your CFECID " + cfecid + " participated in CFEC's online renewal in " + year + "\r\r" +
	            //
	            "CFEC vessel owner or permit holder, \r\r" +
	            "CFEC is sending this information as a courtesy to DEC, rather than providing a mailing list to them. " +
	            "You are receiving it because you have allowed CFEC to publish your email address through selection within LEON. " +
	            "If you wish to change this designation please contact our records officer at nicole.lynch@alaska.gov or change " +
	            "the default selection in LEON when you renew you permits/vessels " +
	            "\r\r" +
	            //
	            "If you have any questions regarding this email, please respond to randy.bates@alaska.gov Director, Divisions of Water, 907-465-5180.  \r\r" +
	            //
	            //
	            "Thank you, \r" +
	            //
	            "CFEC Team \r\r\r" +
	            //
	            //
	            "****** PLEASE DO NOT REPLY TO THIS EMAIL. ******");
	            message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
	            Transport.send(message);
		    } catch (MessagingException me) {
		        me.printStackTrace();
		    }  finally {           
		    }*/
			try {            
		           String to = email;
		           String from = "DFG.CFEC.Notice@alaska.gov";
		           String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
				   logger.info("Using Email host: " + host);
		           String filepath = "/home/mcmity/scratch/pdf";
		           filename1 = filepath+"//publicDistro/AdditionalNoticeInfo_June2022_final.pdf";
		           filename2 = filepath+"//publicDistro/PublicNotice_Non-Newspaper_June2022_final.pdf";
		           //filename3 = filepath+"//publicDistro/RegsProject_June2022_draft2.pdf";
		           Properties props = new Properties();
		           props.put("mail.smtp.host", host);
		           Session session = Session.getInstance(props, null);
		           Message message = new MimeMessage(session);
		           message.setFrom(new InternetAddress(from));
		           message.setSubject("CFEC message for " + name);
		           message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
		           MimeBodyPart messageBody = new MimeBodyPart();
		           messageBody.setText("****** PLEASE DO NOT REPLY TO THIS EMAIL. ****** \r\r\r" +
		   	        	// name + "  -  CFECID: " + cfecid + "\r\r" +
			        	// "Your CFECID " + cfecid + " participated in CFEC's online renewal in " + year + "\r\r" +
			            //
			        	"CFEC registered vessel owner or permit holder, \r\r" +
			            //
			        	//
			            "Commercial Fisheries Entry Commission is sending you this notice because we believe you may be interested in the proposed regulation changes. \r\r" +	
			            //
			            //
			            "The Commercial Fisheries Entry Commission proposes to amend gear code 61 to allow pot gear in " +
			            "the Northern Southeast Inside sablefish longline fishery and to amend Area L, Chignik, to allow use " +
			            "of Tanner crab interim-use permits in the Semidi Island Overlap Section of the Kodiak District. \r\r" +
			            // 
			            //
			            "The following supporting documents are attached: \r\r" +
			            //
			            //
			            "Notice of proposed changes \r" +
			            //
			            "Additional regulation notice information \r\r" +
			            //
			            //
			            "The proposed regulations changes are posted online: https://aws.state.ak.us/OnlinePublicNotices. \r\r" +
			            //
			            //
			            "If you have questions or need additional information, you can contact Nicole Lynch at: nicole.lynch@alaska.gov or by telephone at 907-790-6941. \r\r" +
			            //
			            //
			            "Thank you, \r" +
			            //
			            "CFEC Team \r\r\r" +
			            //
			            //
			            "****** PLEASE DO NOT REPLY TO THIS EMAIL. ******");
		           Multipart multipart = new MimeMultipart();
		           multipart.addBodyPart(messageBody);
		           
		           MimeBodyPart messageBody1 = new MimeBodyPart();
		           DataSource source1 = new FileDataSource(filename1);
		           messageBody1.setDataHandler(new DataHandler(source1));
		           messageBody1.setFileName("AdditionalNoticeInfo_June2022_final.pdf");
		           multipart.addBodyPart(messageBody1);
		           
		           MimeBodyPart messageBody2 = new MimeBodyPart();
		           DataSource source2 = new FileDataSource(filename2);
		           messageBody2.setDataHandler(new DataHandler(source2));
		           messageBody2.setFileName("PublicNoice_Non-Newspaper_June2022_final.pdf");
		           multipart.addBodyPart(messageBody2);
		           //DataSource source3 = new FileDataSource(filename3);
		           //messageBody.setDataHandler(new DataHandler(source3));
		           //messageBody.setFileName("RegsProject_June2022_draft2.pdf");
		           //multipart.addBodyPart(messageBody);
		           message.setContent(multipart);
		           Transport.send(message);
		        } catch (MessagingException me) {
		            me.printStackTrace();
		        }  finally {           
		        }
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		/*for (int i = 0; i < 200; i++) {
	        try {            
	           //String to = "fate.putman@alaska.gov";
	           //String to = "jon.haghayeghi@alaska.gov";
	           String to = "brant.oliphant@alaska.gov";
	           String from = "DFG.CFEC.Notice@alaska.gov";
	           String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
			   logger.info("Using Email host: " + host);
	           filename = filepath+"//testdoc.pdf";
	           Properties props = new Properties();
	           props.put("mail.smtp.host", host);
	           Session session = Session.getInstance(props, null);
	           Message message = new MimeMessage(session);
	           message.setFrom(new InternetAddress(from));
	           message.setSubject("CFEC message for " + name);
	           message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
	           MimeBodyPart messageBody = new MimeBodyPart();
	           messageBody.setText("************ PLEASE DO NOT REPLY TO THIS EMAIL. *************** \r\r\r" +
	        		    name + "  -  CFECID: " + cfecid + "\r" +
	        		    //"Your CFECID " + cfecid + " participated in CFEC's online renewal in " + year + "\r\r" +
	                    "Please take note of the attached document pertaining to the 2020 fishing season.\r\r" +
	           		    "This is just a test, it is only a test of the emergency CFEC broadcast system. Please do not be alarmed. \r" +
	              		"If you have any questions and/or concerns, please do not hesitate to contact a CFEC agent at 1-111-111-1111. \r\r\r" +
	              		"************ PLEASE DO NOT REPLY TO THIS EMAIL. ***************");
	           Multipart multipart = new MimeMultipart();
	           multipart.addBodyPart(messageBody);
	           messageBody = new MimeBodyPart();
	           DataSource source = new FileDataSource(filename);
	           messageBody.setDataHandler(new DataHandler(source));
	           messageBody.setFileName("CFEC-INFO.pdf");
	           multipart.addBodyPart(messageBody);
	           message.setContent(multipart, "text/html; charset=utf-8");
	           Transport.send(message);
	        } catch (MessagingException me) {
	            me.printStackTrace();
	        }  finally {           
	        }
	        try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
    }
    
	/*
	 * This method allows a CFEC admin to re-send a confirmation code to any emaill address that is passed.
	 */
     String sendNoticeAgain(String emailaddress, String curdate, String comment, String nsubject, String cn, 
    		 String id, String year, String thisConfirmDir) {
    	String rtable = "good";
    	try {            
           String to = emailaddress;
           String from = "DFG.CFEC.Renewal@alaska.gov";
           String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
		   logger.info("Using Email host: " + host);
           // TODO modified for multiple servers
           String filename = thisConfirmDir+"//confirm_"+cn+".pdf";
           //String filename = "///webapps//out//LEON//"+year+"//confirmations//confirm_"+cn+".pdf";     
           Properties props = new Properties();
           props.put("mail.smtp.host", host);
           Session session = Session.getInstance(props, null);
           Message message = new MimeMessage(session);
           message.setFrom(new InternetAddress(from));
           message.setSubject(nsubject);
           message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
           MimeBodyPart messageBody = new MimeBodyPart();
           messageBody.setText("Please find attached a copy of your recent ("+curdate+") transaction with the CFEC Online Renewal Application. " +
           		"If you have any questions and/or concerns, please do not hestiate to contact a licensing agent at 1-907-789-6150. " +
           		"************ PLEASE DO NOT REPLY TO THIS EMAIL. ***************");
           Multipart multipart = new MimeMultipart();
           multipart.addBodyPart(messageBody);
           messageBody = new MimeBodyPart();
           DataSource source = new FileDataSource(filename);
           messageBody.setDataHandler(new DataHandler(source));
           messageBody.setFileName("confirm_"+cn+".pdf");
           multipart.addBodyPart(messageBody);
           message.setContent(multipart);
           Transport.send(message);
        } catch (MessagingException me) {
        	String err = "";
			String errEmail = "";
			int errcount = 0;
			err = "";
			errEmail = "";
			errcount = 0;
			StackTraceElement elements[] = me.getStackTrace();
			for  ( int i=0, n=elements.length; i < n; i++ )   {
				if (elements[i].getFileName().startsWith("Leon")) {
					err = err + "File name: " + elements[i].getFileName() + " - Line Number: " + elements[i].getLineNumber() + " - Method: " + elements[i].getMethodName() + "$";
					errEmail = errEmail + "File name: " + elements[i].getFileName() + " - Line Number: " + elements[i].getLineNumber() + " - Method: " + elements[i].getMethodName() + "\r";
					errcount = (errcount + 1);
				}
		    }
			errEmail = errEmail + "\r" + me;
			sendError(curdate, id, errEmail);
		    rtable = "error"+"~"+me+"~"+err+"~"+errcount;
        }  finally {           
        }
        return rtable;
    }
    
     /*
      * This method records user comments, then sends them to a particular set of people at CFEC
      */
    public void sendComments(String esubject, String ebody, String eto, String efrom, Logging leonLog, getDataImpl getDataImpl) {
    	try {
	    	String to = eto;
	    	String from = "DFG.CFEC.Renewal@alaska.gov";
	        String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
			logger.info("Using Email host: " + host);
	        Properties props = new Properties();
            props.put("mail.smtp.host", host);
            Session session = Session.getInstance(props, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setSubject("LEON User Comment - " + esubject);
            message.setText("We have received the following comment from " + efrom + '\r' + ebody);
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
            Transport.send(message);
	    } catch (MessagingException me) {
	        me.printStackTrace();
	    }  finally {           
	    }
	    leonLog.log(efrom + " has just sent a comment/question to CFEC with the subject " + esubject);
    }
    
    public void ProcessMajorError(Exception ex, String id, String mymethod) {
		//pmtimpl.bbaylog.errort("Found an Error", ex);
		try {
			Throwable cause = ex.getCause();
			StackTraceElement elements[] = ex.getStackTrace();
			String filename = elements[0].getFileName();
			String classname = elements[0].getClassName();
			int linenumber = elements[0].getLineNumber();
			String methodname = elements[0].getMethodName();
			String humanerr = ex.getLocalizedMessage();
			Date now = new Date();
			String to = "brant.oliphant@alaska.gov";
	    	String from = "DFG.CFEC.Renewal@alaska.gov";
	        String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
			logger.info("Using Email host: " + host);
	        Properties props = new Properties();
            props.put("mail.smtp.host", host);
            Session session = Session.getInstance(props, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setSubject("LEON unrecoverable system error for CFEC ID - " + id + " on " + now);
            message.setText("An error has occured in the Online Renewal Application"+'\r'+'\r'
					+"The user email " +id+ " " +
					"received the following error:"+'\r'+'\r'
					+"Filename is " +filename+'\r'
					+"Classname is "+classname+'\r'
					+"Linenumber is "+linenumber+'\r'
					+"Methodname is " +methodname+'\r'+'\r'
					+"The human readable error is"+'\r'+humanerr+'\r'+'\r'
					+"The cause is"+'\r'+cause+'\r'+'\r'
					+"I say the method name is"+'\r'+mymethod+'\r'+'\r'
					+"The error occured on " +now+", Message from LEON");
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
            Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}	
    
    public void outofsync(String id, String type, String ryear, String cnum) {
    	try {
    		Date now = new Date();
    		String to = "brant.oliphant@alaska.gov";
			//String to2 = "brant.oliphant@alaska.gov";
	    	String from = "DFG.CFEC.Renewal@alaska.gov";
	        String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
			logger.info("Using Email host: " + host);
	        Properties props = new Properties();
            props.put("mail.smtp.host", host);
            Session session = Session.getInstance(props, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setSubject("LEON out-of-sync alert for CFEC ID - " + id + " on " + now);
            message.setText("This user just processed transaction ("+cnum+") that might be a problem."+'\r'+'\r'
					+"The number of "+type+" received to process was different than expected, so I fell back to a secondary copy of the "+type+" object."+'\r'+'\r'
					+"Check the log files of user "+id+" for confirmation number "+cnum+" and confirm with the LEON client that all "+type+" selected for renewal/licensing were actually renewed/licensed.");
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
            Transport.send(message);
            //message.setRecipient(Message.RecipientType.TO,new InternetAddress(to2));
            //Transport.send(message);            
    	} catch (MessagingException e) {
			e.printStackTrace();
		}
    }
    
    public void sentToTy(String id, String adfg, String ryear) {
    	try {
    		Date now = new Date();
    		String to = "brant.oliphant@alaska.gov";
			//String to2 = "brant.oliphant@alaska.gov";
	    	String from = "DFG.CFEC.Renewal@alaska.gov";
	        String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
			logger.info("Using Email host: " + host);
	        Properties props = new Properties();
            props.put("mail.smtp.host", host);
            Session session = Session.getInstance(props, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setSubject("LEON sanity check for CFEC ID - " + id + " on " + now);
            message.setText("This user just licensed a vessel that has a valid "+ryear+" record for another user."+'\r'+'\r'
					+"Run the following commands to confirm this, then delete the original."+'\r'+'\r'
					+"The original un-licensed record....."+'\r'
					+"select * from rnw_ves ov where ov.adfg_number = '"+adfg+"' and ov.x_renewal_year = '"+ryear+"' and ov.renewal_status = 'A'"+'\r'+'\r'
					+"The new licensed record......"+'\r'
					+"select * from arenew_vessels where adfg = '"+adfg+"' and ryear = '"+ryear+"'"+'\r'+'\r'
					+"Now delete the orginal record which is still available......"+'\r'
					+"delete from rnw_ves ov where ov.adfg_number = '"+adfg+"' and ov.x_renewal_year = '"+ryear+"' and ov.renewal_status = 'A'");
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
            Transport.send(message);
            //message.setRecipient(Message.RecipientType.TO,new InternetAddress(to2));
            //Transport.send(message);            
    	} catch (MessagingException e) {
			e.printStackTrace();
		}
    }
    
    public void NotifyVesselChange(String id, String adfg, String ryear) {
    	try {
    		Date now = new Date();
    		String to = "brant.oliphant@alaska.gov";
			//String to2 = "brant.oliphant@alaska.gov";
	    	String from = "DFG.CFEC.Renewal@alaska.gov";
	        String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
			logger.info("Using Email host: " + host);
	        Properties props = new Properties();
            props.put("mail.smtp.host", host);
            Session session = Session.getInstance(props, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setSubject("LEON notification for CFEC ID - " + id + " on " + now);
            message.setText("This user just licensed a vessel that has a valid "+ryear+" record for another user. Original record is deleted and new record created."+'\r'+'\r'
					+"The original un-licensed record.....(should be blank)"+'\r'
					+"select * from rnw_ves ov where ov.adfg_number = '"+adfg+"' and ov.x_renewal_year = '"+ryear+"' and ov.renewal_status = 'A'"+'\r'+'\r'
					+"The new licensed record......"+'\r'
					+"select * from arenew_vessels where adfg = '"+adfg+"' and ryear = '"+ryear+"'"+'\r'+'\r');
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
            Transport.send(message);
            //message.setRecipient(Message.RecipientType.TO,new InternetAddress(to2));
            //Transport.send(message);            
    	} catch (MessagingException e) {
			e.printStackTrace();
		}
    }
    
    public void ProcessMinorError(Exception ex, String id, String mymethod) {
		//pmtimpl.bbaylog.errort("Found an Error", ex);
		try {
			Throwable cause = ex.getCause();
			StackTraceElement elements[] = ex.getStackTrace();
			String filename = elements[0].getFileName();
			String classname = elements[0].getClassName();
			int linenumber = elements[0].getLineNumber();
			String methodname = elements[0].getMethodName();
			String humanerr = ex.getLocalizedMessage();
			Date now = new Date();
			String to = "brant.oliphant@alaska.gov";
			//String to2 = "brant.oliphant@alaska.gov";
	    	String from = "DFG.CFEC.Renewal@alaska.gov";
	        String host = getDataImpl.leonprop.getProperty("LEON.hostinfo.SMTPhost", "146.63.17.2");
			logger.info("Using Email host: " + host);
	        Properties props = new Properties();
            props.put("mail.smtp.host", host);
            Session session = Session.getInstance(props, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setSubject("LEON minor system error for CFEC ID - " + id + " on " + now);
            message.setText("An error has occured in the Online Renewal Application"+'\r'+'\r'
					+"The user email " +id+ " " +
					"received the following error:"+'\r'+'\r'
					+"Filename is " +filename+'\r'
					+"Classname is "+classname+'\r'
					+"Linenumber is "+linenumber+'\r'
					+"Methodname is " +methodname+'\r'+'\r'
					+"The human readable error is"+'\r'+humanerr+'\r'+'\r'
					+"The cause is"+'\r'+cause+'\r'+'\r'
					+"I say the method name is"+'\r'+mymethod+'\r'+'\r'
					+"The error occured on " +now+", Message from LEON");
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
            Transport.send(message);
            //message.setRecipient(Message.RecipientType.TO,new InternetAddress(to2));
            //Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}	
    
    public static void createPdf(String filename, String body, String cnum, String leonletterOut, ArenewEntity entity, String curdate, String title, String extension)
            throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(leonletterOut+"/"+cnum + "-"+extension+".pdf"));
        document.open();
        document.add(new Paragraph("Email sent to " + entity.getEmail() + ", " +entity.getXname() + " on " + curdate + ", confirmation number " + cnum));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(title));
        document.add(new Paragraph(body));
        document.close();
    }


}
