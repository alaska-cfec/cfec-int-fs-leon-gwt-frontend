package com.cfecweb.leon.client;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import static com.cfecweb.leon.client.ScreenProcessing.reopenBasePage;

/**
 * 
 * @author ty_mcmichael
 * This class defines the session counter for automatic reset, it also includes
 * various getters and setters to reset, pass in new time and cancel the timer.
 *
 */

/*
 * This is the session timeout class which keeps track of the session ID and the current inactivity.
 * After 20 minutes of inactivity, the session timesout and the application resets. Timer resets, 
 * based on whatever actions you want to define, occur frequently throughout the application using 
 * the getters and setters below. I decided not to reset the time on any keystroke or mouse click, but
 * instead only on more major things, like buttons.
 */
public class SessionTimer {
	/**
	 * Define variables for client session timeout 
	 */
	private final getDataAsync service = (getDataAsync) getData.Util.getInstance();
	private static final String THE_APP_WILL_RESET = " The application will reset.";
	private Timer sessionTimeoutTimer = null;
	public int time;
	ProgressBar prog = null;
	/**
    * Called only once from <b>onModuleLoad</b>.
    * @sessionTimeInMillis Integer
    */
	public Timer setTimer(int sessionTimeMillis, final String id) {	
		/*
		 * 	The sessionTimeMillis is the total milliseconds pased by the server, which
		 * 	gets it from the session. The session time is set either manually on the
		 * 	server OR automcatically from tomcat.
		 */
		time = sessionTimeMillis;
		final int x = (sessionTimeMillis/100);
		final Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>() {  
			public void handleEvent(MessageBoxEvent ce) {  
				Button btn = ce.getButtonClicked();  
				if (btn.getHtml().toLowerCase().contains("ok")) {
                    reopenBasePage();
				}
			}  
		};
		sessionTimeoutTimer = new Timer() {
			float i = 0;
			@SuppressWarnings("rawtypes")
			public void run() {
				/*
				 * 	Take the number of minutes * 2, then * 2 again
				 * 	Take that number, divide it into 100 and put that into the second variable
				 * 	Add 2 zeros behind the decimal, and put that number into the first variable
				 */
				prog.updateProgress((.000416 * i), (int) (i * .0416) + "% Session Inactivity");  
				i += 1; 
				if (i > (x/5)) {
					sessionTimeoutTimer.cancel();
					prog.updateProgress(100, "Session is Timed Out");
					MessageBox box1 = new MessageBox();
					box1.setButtons(MessageBox.OK);
					box1.setIcon(MessageBox.ERROR);
					box1.getDialog().setTitle("Session Timeout");
					box1.setMessage("Your session has timed out." + THE_APP_WILL_RESET);
					box1.addCallback(l);
					box1.show();
					service.killSession(new AsyncCallback() {
						public void onFailure(Throwable caught) {}
						public void onSuccess(Object result) {}						
					});
				}				
			}
		};
		/*
		 * 	This next value determines the milliseconds for each loop, currently set at 1/2 second.
		 */
		sessionTimeoutTimer.scheduleRepeating(500);
		return sessionTimeoutTimer;
	}
	
	public ProgressBar getProgbar() {
		prog = new ProgressBar();
        prog.setTitle("Session Countdown. Application will reset at 100% (20 minutes of inactivity)");
        prog.setId("progBar");
        prog.setBorders(false);
        prog.setAutoHeight(true);
        prog.setAutoWidth(true);
        return prog;
	}
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public void progReset() {
		prog.reset();
	}
	
	public void timerCancel() {
		sessionTimeoutTimer.cancel();
	}
}
