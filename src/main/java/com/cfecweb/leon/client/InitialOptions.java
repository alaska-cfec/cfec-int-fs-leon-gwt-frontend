package com.cfecweb.leon.client;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;

/*
 * This class contains a group of getters and setters that define the initial option content panel (first screen of the application)
 */
public class InitialOptions {
	UserWindows gwin = new UserWindows();
	UserMessages gmsg = new UserMessages();
	public VerticalPanel opt1Content = null;
	public VerticalPanel opt2Content = null;
	public VerticalPanel opt3Content = null;
	public VerticalPanel opt4Content = null;
	public VerticalPanel tickerContent = null;
	public Radio renewal = null;
	public Radio forms = null;
	public Radio povyes = null;
	public Radio povno = null;
	public Radio ares = null;
	public Radio nres = null;
	public Radio acit = null;
	public Radio ncit = null;
	public CheckBox privacy = null;
	public CheckBox reduced = null;
	public TextField<String> arn = null;
	public RadioGroup povg = null;
	MessageBox ab = null;
	public int x = 0;
	
	public TextField<String> getArn() {
		return arn;
	}

	public void setArn(TextField<String> arn) {
		this.arn = arn;
	}
	
	public CheckBox getPrivacy() {
		return privacy;
	}

	public void setPrivacy(CheckBox privacy) {
		this.privacy = privacy;
	}

	public CheckBox getReduced() {
		return reduced;
	}

	public void setReduced(CheckBox reduced) {
		this.reduced = reduced;
	}
	
	public Radio getAcit() {
		return acit;
	}

	public void setAcit(Radio acit) {
		this.acit = acit;
	}

	public Radio getNcit() {
		return ncit;
	}

	public void setNcit(Radio ncit) {
		this.ncit = ncit;
	}
	
	public Radio getAres() {
		return ares;
	}

	public void setAres(Radio ares) {
		this.ares = ares;
	}

	public Radio getNres() {
		return nres;
	}

	public void setNres(Radio nres) {
		this.nres = nres;
	}
	
	public Radio getRenewal() {
		return renewal;
	}

	public void setRenewal(Radio renewal) {
		this.renewal = renewal;
	}

	public Radio getForms() {
		return forms;
	}

	public void setForms(Radio forms) {
		this.forms = forms;
	}
	
	public Radio getPovyes() {
		return povyes;
	}

	public void setPoveyes(Radio povyes) {
		this.povyes = povyes;
	}

	public Radio getPovno() {
		return povno;
	}

	public void setPovno(Radio povno) {
		this.povno = povno;
	}
	
	public VerticalPanel getOpt2Content() {
		return opt2Content;
	}

	public void setOpt2Content(VerticalPanel opt2Content) {
		this.opt2Content = opt2Content;
	}

	public VerticalPanel getOpt3Content() {
		return opt3Content;
	}

	public void setOpt3Content(VerticalPanel opt3Content) {
		this.opt3Content = opt3Content;
	}
	
	public VerticalPanel getOpt4Content() {
		return opt4Content;
	}

	public void setOpt4Content(VerticalPanel opt4Content) {
		this.opt4Content = opt4Content;
	}

	public VerticalPanel getOpt1Content() {
		return opt1Content;
	}

	public void setOpt1Content(VerticalPanel opt1Content) {
		this.opt1Content = opt1Content;
	}
	
	/*
	public VerticalPanel getTickerContent() {
		return tickerContent;
	}

	public void setTickerContent(VerticalPanel tickerContent) {
		this.tickerContent = tickerContent;
	}
	*/

	public ContentPanel getOption1(String ryear) {
		final ContentPanel opt1 = new ContentPanel();
		opt1.setHeaderVisible(false);
		opt1.setFrame(true);
		opt1.setBodyBorder(true);
		opt1.setBorders(false);
	    opt1.setAutoWidth(true);
	    opt1.setAutoHeight(true);
	    opt1.setShadow(false);
	    opt1.addStyleName("option1");
	    opt1Content = new VerticalPanel();
	    opt1Content.setTableWidth("100%");
	    opt1Content.setStyleAttribute("background", "#EEEEEE");
	    opt1Content.setHorizontalAlign(HorizontalAlignment.CENTER);
	    opt1Content.setVerticalAlign(VerticalAlignment.MIDDLE);
	    TableData opt1Header = new TableData();
	    opt1Content.add(new Html("<span class='boldblack12'>Item 1</span>&nbsp;&nbsp;-&nbsp;&nbsp;<span class='boldred12'>"
	    		+ "REQUIRED SECTION</span> - <span class='regblack12'>must choose one of the following</span>"), opt1Header);
	    RadioGroup g1 = new RadioGroup();
	    g1.setWidth(500);
	    g1.setStyleName("opt1radiogroup");
	    renewal = new Radio();
	    renewal.setName("phrf");
	    renewal.setStyleName("renewal");
	    renewal.setBoxLabel("- <span class='regblue12'>Renew your Permits/Vessels</span>");
	    renewal.setTitle("Select this option if you want to add, edit, renew or view the status of your CFEC "
	    		+ "permits and/or vessels using our secure online renewal process.");
	    renewal.addListener(Events.OnClick, new Listener<FieldEvent>() {
            public void handleEvent(FieldEvent be) {
                forms.setValue(false);
            }
        });
	    g1.add(renewal);
	    forms = new Radio();
	    forms.setName("phrf");
	    forms.setStyleName("forms");
	    forms.setBoxLabel("- <span class='regblue12'>Download your renewal form&nbsp;</span>");
	    forms.setTitle("Select this option if you want to download current CFEC documents, including " +
	    		"your current renewal forms. This requires a PDF viewer on your computer.");
	    forms.addListener(Events.OnClick, new Listener<FieldEvent>() {
            public void handleEvent(FieldEvent be) {
                renewal.setValue(false);
            }
        });
	    g1.add(forms);
	    opt1Content.add(g1);
	    opt1.add(opt1Content);
		return opt1;
	}
	
	public ContentPanel getOption2(String ryear) {
	   ContentPanel opt2 = new ContentPanel();
	   opt2.setHeaderVisible(false);
	   opt2.setFrame(true);
	   opt2.setBodyBorder(true);
	   opt2.setBorders(false);
	   opt2.setAutoWidth(true);
	   opt2.setAutoHeight(true);
	   opt2.setShadow(false);
	   opt2.addStyleName("option3");
       opt2Content = new VerticalPanel();
       opt2Content.setTableWidth("100%");
       opt2Content.setStyleAttribute("background", "#e6efdc");
       opt2Content.setHorizontalAlign(HorizontalAlignment.CENTER);
       opt2Content.setVerticalAlign(VerticalAlignment.MIDDLE);
       TableData opt2Header = new TableData();
       opt2Content.add(new Html("<span class='boldblack12'>Item 4</span>&nbsp;&nbsp;-&nbsp;&nbsp;<span class='boldred12'>"
       		+ "REQUIRED SECTION</span> - <span class='regblack12'>please read & select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
       		+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>"), opt2Header);
       RadioGroup g3 = new RadioGroup();
       g3.setWidth(500);
       privacy = new CheckBox();
       privacy.setName("phpr");
       privacy.setBoxLabel("- <span class='reggreen12'>Privacy Act verification&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
       		+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>");
       privacy.setStyleName("privacy");
       g3.add(privacy);
       opt2Content.add(g3);
       TableData opt2r1desc = new TableData();
       opt2Content.add(new Html("<span class='regblack12'>By selecting this item, I swear, under penalty of perjury, that the information " +
       		"provided by me is true. I understand that making a false claim is a crime (AS.11.56.210) " +
       		"which is punishable by up to one year in prison and/or a $5,000 fine as well as administrative fines and/or suspension or revocation of fishing privileges." +
       		"(AS.16.43.960). Under AS 16.05.815 and AS 16.05.480, social security numbers and other information required on this form may be used for fisheries research, " +
       		"management and licensing purposes and may be disclosed to: the Alaska Department of Fish and Game, the National Marine Fisheries Service, staff of the Pacific " +
       		"States Marine Fisheries Commission who are employed by the Alaska Fisheries Information Network Project, the North Pacific Fisheries Management Council, " +
       		"Department of Public Safety, Department of Revenue, Child Support Enforcement Agencies for child support purposes, and to other agencies or individuals as " +
       		"required by law or court order. Name, address, and licenses held are public information that may be released.</span>"), opt2r1desc);
       opt2.add(opt2Content);
	   return opt2;  
	}
	
	public ContentPanel getOption3(final String ryear) {
		final ContentPanel opt3 = new ContentPanel();
		opt3.setHeaderVisible(false);
		opt3.setFrame(true);
		opt3.setBodyBorder(true);
		opt3.setBorders(false);
	    opt3.setAutoWidth(true);
	    opt3.setAutoHeight(true);
	    opt3.setShadow(false);
	    opt3.addStyleName("option4");
	    opt3Content = new VerticalPanel();
	    opt3Content.setTableWidth("100%");
	    opt3Content.setStyleAttribute("background", "#fff0e0");
	    opt3Content.setHorizontalAlign(HorizontalAlignment.CENTER);
	    opt3Content.setVerticalAlign(VerticalAlignment.MIDDLE);
	    TableData opt3Header = new TableData();
	    opt3Content.add(new Html("<span class='boldblack12'>Item 3</span>&nbsp;&nbsp;-&nbsp;&nbsp;<span class='boldred12'>"
	    		+ "REQUIRED SECTION</span> - <span class='regblack12'>must choose one of the following</span>"), opt3Header);
	    final RadioGroup p1 = new RadioGroup();
	    p1.setWidth(500);	    
	    final Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>() {  
	    	public void handleEvent(MessageBoxEvent ce) { 
	    		if (ce.getButtonClicked().getHtml().toLowerCase().contains("ok")) {
	    		} else {
	    			povno.setValue(true);
	    		}
	    		x = 0;
	       	}  
	    };	    
	    povyes = new Radio();
	    povyes.setName("povgp");
	    povyes.setId("pyes");
	    povyes.setBoxLabel("- <span class='boldorange12'>YES, I qualify for Poverty Fees&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>");
	    povyes.setTitle("Select this option if you qualify for poverty fees. This will require additional paperwork");
	    povyes.addListener(Events.OnClick, new Listener<FieldEvent>() {
	    	public void handleEvent(FieldEvent be) {
	    		//Log.info("Value of X before MessageBox is " + x);
	    		if (x > 0) {
	    			x = 0;
	    		} else {
					MessageBox ab = new MessageBox();
					ab.setType(MessageBox.MessageBoxType.CONFIRM);
					ab.setButtons(MessageBox.OKCANCEL);
					ab.getDialog().setTitle("<center>Poverty Fees Definition and Confirmation - <span class='regred12'>PLEASE READ!</span></center>");
					ab.setMessage(gwin.getReduced(ryear));
					ab.setMinWidth(500);
					ab.addCallback(l);
					ab.show();
					x = x + 1;
	    		}	    		
			}        	
        });
	    povno = new Radio();
	    povno.setName("povgp");
	    povno.setId("pno");
	    povno.setBoxLabel("- <span class='boldorange12'>NO, I do NOT qualify for Poverty Fees</span>");
	    povno.setTitle("Select this option if you do NOT qualify for poverty fees.");
	    p1.add(povno);
	    p1.add(povyes);
	    opt3Content.add(p1);
	    opt3.add(opt3Content);
	    return opt3;  
	}
	
	public ContentPanel getOption4(String ryear) {
		final Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>() {  
	    	public void handleEvent(MessageBoxEvent ce) { 
	    		if (ce.getButtonClicked().getHtml().toLowerCase().contains("ok")) {
	    		} else {
	    			povno.setValue(true);
	    		}
	    		x = 0;
	       	}  
	    };	  
		final ContentPanel opt4 = new ContentPanel();
		opt4.setHeaderVisible(false);
		opt4.setFrame(true);
		opt4.setBodyBorder(true);
		opt4.setBorders(false);
	    opt4.setAutoWidth(true);
	    opt4.setAutoHeight(true);
	    opt4.setShadow(false);
	    opt4.addStyleName("option2");
	    opt4Content = new VerticalPanel();
	    opt4Content.setTableWidth("100%");
	    opt4Content.setStyleAttribute("background", "#e8dad3");
	    opt4Content.setHorizontalAlign(HorizontalAlignment.CENTER);
	    opt4Content.setVerticalAlign(VerticalAlignment.MIDDLE);
	    TableData opt4Header = new TableData();
	    opt4Content.add(new Html("<span class='boldblack12'>Item 2</span>&nbsp;&nbsp;-&nbsp;&nbsp;"
	    		+ "<span class='boldred12'>REQUIRED SECTION</span> - <span class='regblack12'>must choose one in each group&nbsp;&nbsp;&nbsp;</span>"), opt4Header);
	    RadioGroup g1 = new RadioGroup();
	    g1.setWidth(500);
	    g1.setStyleName("opt2radiogroup1");
	    ares = new Radio();
	    ares.setName("res");
	    ares.setBoxLabel("- <span class='regbrown12'>Alaska Resident</span>");
	    ares.setTitle("Select this option if you are an Alaska State Resident");
	    ares.addListener(Events.OnClick, new Listener<FieldEvent>() {
            public void handleEvent(FieldEvent be) {
                nres.setValue(false);
            }
        });
	    ares.setStyleName("resoptionsY");
	    g1.add(ares);	    
	    nres = new Radio();
	    nres.setName("res");
	    nres.setBoxLabel("- <span class='regbrown12'>Alaska Non Resident</span>");
	    nres.setTitle("Select this option if you are NOT an Alaska State Resident.");
	    nres.addListener(Events.OnClick, new Listener<FieldEvent>() {
            public void handleEvent(FieldEvent be) {
                ares.setValue(false);
            }
        });
	    nres.setStyleName("resoptionsN");
	    g1.add(nres);
	    opt4Content.add(g1);	    	    
	    RadioGroup g2 = new RadioGroup();	    
	    g2.setWidth(500);
	    g2.setStyleName("opt2radiogroup2");
	    acit = new Radio();
	    acit.setName("cit");
	    acit.setBoxLabel("- <span class='regbrown12'>US Citizen</span>");
	    acit.setTitle("Select this option if you are US Citizen");
	    acit.addListener(Events.OnClick, new Listener<FieldEvent>() {
            public void handleEvent(FieldEvent be) {
                ncit.setValue(false);
                arn.setValue(null);
                arn.setEnabled(false);
                arn.setAllowBlank(true);
                arn.clearInvalid();
                arn.setEmptyText("Registration Number Not Required");
            }
        });
	    acit.setStyleName("citoptionsY");
	    g2.add(acit);	    
	    ncit = new Radio();
	    ncit.setName("cit");
	    ncit.setBoxLabel("- <span class='regbrown12'>Not a US Citizen</span>");
	    ncit.setTitle("Select this option if you are NOT a US Citizen.");
	    ncit.addListener(Events.OnClick, new Listener<FieldEvent>() {
            public void handleEvent(FieldEvent be) {
            	if (x > 0) {
	    			x = 0;
	    		} else {
					MessageBox ab = new MessageBox();
					ab.setType(MessageBox.MessageBoxType.CONFIRM);
					ab.setButtons(MessageBox.OKCANCEL);
					ab.getDialog().setTitle("<center>Non US Citizen Confirmation - <span class='regred12'>PLEASE READ!</span></center>");
					ab.setMessage(gwin.getAlien());
					ab.setMinWidth(500);
					ab.addCallback(l);
					ab.show();
					x = x + 1;
	    		}
                acit.setValue(false);
                arn.setEnabled(true);
                arn.setAllowBlank(false);
                arn.isValid();
                arn.setEmptyText("Registration Number Required");
            }
        });	    
	    ncit.setStyleName("citoptionsN");
	    g2.add(ncit);	    	    
	    arn = new TextField<String>();  
        arn.setEnabled(false);
        arn.setWidth(200);
        arn.setStyleName("arntextfield");
        arn.setEmptyText("Alien Registration Number");
	    opt4Content.add(g2);
	    opt4Content.add(arn);	    
	    Anchor resdef = new Anchor();
        resdef.setHTML("<u><font size='2' style='cursor: pointer; cursor: hand;'>Residency Definition</font>&nbsp;&nbsp;-&nbsp;&nbsp;"
        		+ "<font size='1' style='cursor: pointer; cursor: hand;'>You are certifying under penalty of perjury that the information "
        		+ "contained herein is true and complete</font></u>");
        resdef.setTitle("Click on this link to get a full description of residency requirements");
        resdef.addClickHandler(new ClickHandler() {
     	  public void onClick(ClickEvent sender) {
     		  final Window resWin = new Window();
     		  resWin.setSize(300, 300);
     		  resWin.setHeadingHtml("Residency Definition");
     		  resWin.setLayout(new FitLayout());  
     		  resWin.setScrollMode(Scroll.AUTO);
     		  resWin.addText(gwin.getResidency());
     		  resWin.addButton(new Button("Close", new SelectionListener<ButtonEvent>() {
     			  public void componentSelected(ButtonEvent ce) {
     				  resWin.hide();
     			  }
     		  }));  
     		  resWin.show();
     	  }
        }); 
	    resdef.setStyleName("citoptions");
	    opt4Content.add(resdef);
	    opt4.add(opt4Content);
		return opt4;
	}
	
	/*
	public ContentPanel getTicker() {
		   ContentPanel tick = new ContentPanel();
		   tick.setHeaderVisible(false);
		   tick.setFrame(true);
		   tick.setBodyBorder(true);
		   tick.setBorders(false);
		   tick.setAutoWidth(true);
		   tick.setAutoHeight(true);
		   tick.setShadow(false);
		   tick.addStyleName("opt1");
	       tickerContent = new VerticalPanel();
	       tickerContent.setTableWidth("100%");
	       tickerContent.setStyleAttribute("background", "#EEEEEE");
	       tickerContent.disable();
	       tickerContent.setHorizontalAlign(HorizontalAlignment.CENTER);
	       tickerContent.setVerticalAlign(VerticalAlignment.MIDDLE);
	       TableData opt3Header = new TableData();
	       tickerContent.add(new Html("News Ticker&nbsp;&nbsp;-&nbsp;&nbsp;<font color='red'>Relevant CFEC updates</font>"), opt3Header);
	       tick.add(tickerContent);
		   return tick;  
		}
	*/
	
}
