package com.cfecweb.leon.client;

import com.extjs.gxt.ui.client.widget.MessageBox;

/*
 * This is more or less a helper class that has predefined types of message boxes. These are
 * called throughout the application for popups, errors, warnings, confirmations, etc. Data is
 * basically passed to them by the calling method.
 */
public class UserMessages {
	MessageBox wb = new MessageBox();
	
	public void alert(String title, String msg, int width) {
		MessageBox ab = new MessageBox();
        ab.setType(MessageBox.MessageBoxType.ALERT);
        ab.getDialog().setTitle(title);
        ab.setMessage(msg);
        ab.setMinWidth(width);
        ab.setIcon("alertBox");
        ab.setIcon(MessageBox.INFO);
        ab.show();
    }
	
	public void error(String title, String msg, int width) {
		MessageBox ab = new MessageBox();
        ab.setType(MessageBox.MessageBoxType.ALERT);
        ab.getDialog().setTitle(title);
        ab.setMessage(msg);
        ab.setMinWidth(width);
        ab.setIcon("alertBox");
        ab.setIcon(MessageBox.ERROR);
        ab.show();
    }
	
	public void waitStart(String title, String msg, String init, int width) {
		wb = new MessageBox();
		wb.setType(MessageBox.MessageBoxType.WAIT);
		wb.getDialog().setTitle(title);
		wb.setMessage(msg);
		wb.setMinWidth(width);
		wb.show();
	}
	
	public void waitStop() {
		wb.close();
	}
}
