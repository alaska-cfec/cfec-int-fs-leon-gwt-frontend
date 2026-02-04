package com.cfecweb.leon.client;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;

public class HeaderText {
	static String LEONversion = "11.08.00.2019";
	HorizontalPanel hp = null;
	UserWindows gwin = new UserWindows();

	/*
	 * This method returns the horizontal header at the top of the application
	 */
	public HorizontalPanel getHeader(String ryear) {
	   hp = new HorizontalPanel();
       hp.setStyleName("header");
       hp.setTableWidth("100%");
       hp.setTableHeight("100%");    
       TableData leftmargin = new TableData();
       leftmargin.setHorizontalAlign(HorizontalAlignment.LEFT);
       leftmargin.setWidth("1%");
       hp.add(new Html(""), leftmargin);
       Anchor supportl = new Anchor();
       supportl.setHTML("<span class='regblack12'><font color='blue' style='cursor: pointer; cursor: hand;'><u>Support Contacts</u></font></span>");
       supportl.setTitle("Click here to access our support numbers");
       supportl.addClickHandler(new ClickHandler() {
    	  public void onClick(ClickEvent sender) {
    		  final Window supportWin = new Window();
    		  supportWin.setSize(400, 200);
    		  supportWin.setHeadingHtml("LEON Support Contacts");
    		  supportWin.setLayout(new FitLayout());  
    		  supportWin.setScrollMode(Scroll.AUTO);
    		  supportWin.addText(gwin.getSupport());
    		  supportWin.addButton(new Button("Close", new SelectionListener<ButtonEvent>() {
    			  public void componentSelected(ButtonEvent ce) {
    				  supportWin.hide();
    			  }
    		  }));  
    		  supportWin.show();
    		  Log.info("The Support page is being viewed");
    	  }
       });   
       TableData assist = new TableData();
       assist.setVerticalAlign(VerticalAlignment.MIDDLE);
       assist.setHorizontalAlign(HorizontalAlignment.LEFT);
       assist.setWidth("17%");
       hp.add(supportl, assist);
       //hp.add(new Html("<span class='regblack12'>Support? 907-789-6150</span>"), assist);           
       Anchor faql = new Anchor();
       faql.setHTML("<span class='regblack12'><font color='blue' style='cursor: help;'><u>FAQ</u></font></span>");
       faql.setTitle("Click here to read our FAQ section");
       faql.addClickHandler(new ClickHandler() {
    	  public void onClick(ClickEvent sender) {
    		  final Window faqWin = new Window();
    		  faqWin.setSize(625, 425);
    		  faqWin.setHeadingHtml("Frequently Asked Questions");
    		  faqWin.setLayout(new FitLayout());  
    		  faqWin.setScrollMode(Scroll.AUTO);
    		  faqWin.addText(gwin.getFaq(LEONversion));
    		  faqWin.addButton(new Button("Close", new SelectionListener<ButtonEvent>() {
    			  public void componentSelected(ButtonEvent ce) {
    				  faqWin.hide();
    			  }
    		  }));  
    		  faqWin.show();
    		  Log.info("The FAQ document is being viewed");
    	  }
       });               
       TableData faq = new TableData();
       faq.setVerticalAlign(VerticalAlignment.MIDDLE);
       faq.setWidth("4%");
       faq.setHorizontalAlign(HorizontalAlignment.CENTER);
       hp.add(faql, faq);         
       Anchor requirementsl = new Anchor();
       requirementsl.setHTML("<span class='regblack12'><font color='blue' style='cursor: pointer; cursor: hand;'><u>Requirements</u></font></span>");
       requirementsl.setTitle("Click here to read our requirements section");
       requirementsl.addClickHandler(new ClickHandler() {
     	  public void onClick(ClickEvent sender) {
     		  final Window requirementsWin = new Window();
     		  requirementsWin.setSize(600, 450);
     		  requirementsWin.setHeadingHtml("Application Requirements");
     		  requirementsWin.setLayout(new FitLayout());  
     		  requirementsWin.setScrollMode(Scroll.AUTO);
     		  requirementsWin.addText(gwin.getRequirements());
     		  requirementsWin.addButton(new Button("Close", new SelectionListener<ButtonEvent>() {
     			  public void componentSelected(ButtonEvent ce) {
     				 requirementsWin.hide();
     			  }
     		  }));  
     		  requirementsWin.show();
     		  Log.info("The Requirements document is being viewed");
     	  }
       });       
       TableData requirements = new TableData();
       requirements.setVerticalAlign(VerticalAlignment.MIDDLE);
       requirements.setWidth("10%");
       requirements.setHorizontalAlign(HorizontalAlignment.CENTER);
       hp.add(requirementsl, requirements);            
       TableData name = new TableData();
       name.setVerticalAlign(VerticalAlignment.MIDDLE);
       name.setHorizontalAlign(HorizontalAlignment.CENTER);
       name.setWidth("36%");
       name.setStyleName("mainTitle");
       hp.add(new Html("<span class='boldblack14'>"+ryear+" CFEC Online Management Application</span>"), name);              
       Anchor commentsl = new Anchor();
       commentsl.setHTML("<span class='regblack12'><font color='blue' style='cursor: pointer; cursor: hand;'><u>Comments</u></font></span>");
       commentsl.setTitle("Click here to send a comment and/or question to our staff");
       commentsl.addClickHandler(new ClickHandler() {
      	  public void onClick(ClickEvent sender) {
      		  final Window commentsWin = new Window();
      		  commentsWin.setSize(500, 300);
      		  commentsWin.setHeadingHtml("Questions and/or Comments");
      		  commentsWin.setLayout(new FitLayout());  
      		  FormPanel panel = new FormPanel();  
      		  panel.setBorders(false);  
      		  panel.setBodyBorder(false);  
  		      panel.setLabelWidth(55);  
  		      panel.setPadding(5);  
  		      panel.setHeaderVisible(false);  
  		      TextField<String> to = new TextField<String>();  
  		      to.setFieldLabel("Send To"); 
  		      to.setValue("DFG.CFEC.Renewal@alaska.gov");
  		      to.setReadOnly(true);
  		      panel.add(to, new FormData("100%"));  
  		      TextField<String> from = new TextField<String>();  
		      from.setFieldLabel("From");  
		      from.setAllowBlank(false);
		      from.getMessages().setBlankText("The From field cannot be blank");
		      panel.add(from, new FormData("100%"));  
		      TextField<String> subject = new TextField<String>();  
		      subject.setFieldLabel("Subject");  
		      subject.setAllowBlank(false);
		      subject.getMessages().setBlankText("The Subject field cannot be blank");
  		      panel.add(subject, new FormData("100%"));  
  		      TextArea area = new TextArea();  
  		      area.setHideLabel(true);  
  		      panel.add(area, new FormData("100% -53")); 
  		      area.setAllowBlank(false);
  		      area.getMessages().setBlankText("You have not specified any text to send");
  		      commentsWin.addButton(new Button("Send", new SelectionListener<ButtonEvent>() {
     			  public void componentSelected(ButtonEvent ce) {     				  
     				  commentsWin.hide();
     			  }
     		  }));    
  		      commentsWin.addButton(new Button("Cancel", new SelectionListener<ButtonEvent>() {
     			  public void componentSelected(ButtonEvent ce) {
     				  commentsWin.hide();
     			  }
     		  }));    
  		      commentsWin.add(panel);  
  		      commentsWin.show();
  		      Log.info("The Comments screen has been lauched");
      	  }
       });        
       final Listener<BaseEvent> attachListener = new Listener<BaseEvent>() {
           @Override
           public void handleEvent(final BaseEvent be) {
               final Html html = (Html) be.getSource();
               html.el().addEventsSunk(Event.ONCLICK);
           }
       };
       final Listener<BaseEvent> homelistener = new Listener<BaseEvent>() {
           public void handleEvent(final BaseEvent be) {
           	Log.info("The CFEC home page is being accessed");
           }
       };
       final Listener<BaseEvent> plooklistener = new Listener<BaseEvent>() {
           public void handleEvent(final BaseEvent be) {
           	Log.info("The CFEC Public Lookup application is being accessed");
           }
       };
       TableData comments = new TableData();
       comments.setVerticalAlign(VerticalAlignment.MIDDLE);
       comments.setWidth("7%");
       comments.setHorizontalAlign(HorizontalAlignment.CENTER);
       hp.add(commentsl, comments);        
       Html cfechomel = new Html("<span class='regblack12'><a href='http://www.cfec.state.ak.us' style='color:blue' target='_blank' title='Click here to open a seperate window for the CFEC Home page'>CFEC</a></span>");
       TableData cfecHome = new TableData();
       cfecHome.setVerticalAlign(VerticalAlignment.MIDDLE);
       cfecHome.setWidth("7%");
       cfecHome.setHorizontalAlign(HorizontalAlignment.CENTER);
       cfechomel.addListener(Events.Attach, attachListener);
       cfechomel.addListener(Events.OnClick, homelistener);
       hp.add(cfechomel, cfecHome);        
       Html additionall = new Html("<span class='regblack12'><a href='http://www.cfec.state.ak.us/plook' style='color:blue' target='_blank' title='Click here to open a seperate window for the CFEC Permit and Vessel search application'>Permit & Vessel lookup</a></span>");
       TableData additional = new TableData();
       additional.setVerticalAlign(VerticalAlignment.MIDDLE);
       additional.setHorizontalAlign(HorizontalAlignment.RIGHT);
       additional.setWidth("17%");
       additionall.addListener(Events.Attach, attachListener);
       additionall.addListener(Events.OnClick, plooklistener);
       hp.add(additionall, additional);
       TableData rightmargin = new TableData();
       rightmargin.setHorizontalAlign(HorizontalAlignment.RIGHT);
       rightmargin.setWidth("1%");
       hp.add(new Html(""), rightmargin);
	   return hp;
	}
}
