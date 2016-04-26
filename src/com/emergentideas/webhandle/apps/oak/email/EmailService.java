package com.emergentideas.webhandle.apps.oak.email;

import java.util.Map;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.webhandle.Type;

@Type("com.emergentideas.webhandle.assumptions.oak.interfaces.EmailService")
public class EmailService implements
		com.emergentideas.webhandle.assumptions.oak.interfaces.EmailService {

	protected String host;
	protected int port;
	protected String user;
	protected String pass;
	protected boolean startTLSEnabled = true;
	protected boolean lockFromUser = true;
	
	protected Logger log = SystemOutLogger.get(EmailService.class);
	
	public boolean sendEmail(String[] to, String from, String[] cc, String[] bcc,
			String subject, String textContent, String htmlContent, Map<String, String> headers) {
		
		try {
			HtmlEmail email = createEmailWithConnectionInfo(to, bcc, from, subject);
			if(htmlContent != null) {
				email.setHtmlMsg(htmlContent);
			}
			if(textContent != null) {
				email.setTextMsg(textContent);
			}
			
			if(headers != null) {
				for(String key : headers.keySet()) {
					email.addHeader(key, headers.get(key));
				}
			}
			
			email.send();
			return true;
		}
		catch(EmailException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean sendEmail(String[] to, String from, String[] cc, String[] bcc,
			String subject, String textContent, String htmlContent) {
		
		try {
			HtmlEmail email = createEmailWithConnectionInfo(to, bcc, from, subject);
			if(htmlContent != null) {
				email.setHtmlMsg(htmlContent);
			}
			if(textContent != null) {
				email.setTextMsg(textContent);
			}
			
			email.send();
			return true;
		}
		catch(EmailException e) {
			throw new RuntimeException(e);
		}
	}

	protected HtmlEmail createEmailWithConnectionInfo(String[] to, String[] bcc,
			String from, String subject) throws EmailException {
		if(lockFromUser) {
			from = "\"Web Server\" <" + user + ">";
		}
		HtmlEmail email = new HtmlEmail();
		email.setHostName(host);
		email.setSmtpPort(port);
		email.setAuthentication(user, pass);
		
		try {
			email.setStartTLSEnabled(isStartTLSEnabled());
		}
		catch(Throwable e) {
			email.setTLS(isStartTLSEnabled());
		}
		
		if(to != null && to.length > 0) {
			for(String s : to) {
				email.addTo(s);
			}
		}
		
		if(bcc != null && bcc.length > 0) {
			for(String s : bcc) {
				email.addBcc(s);
			}
		}
		
		email.setFrom(from);
		email.setSubject(subject);
		return email;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public boolean isStartTLSEnabled() {
		return startTLSEnabled;
	}

	public boolean getStartTLSEnabled() {
		return startTLSEnabled;
	}

	public void setStartTLSEnabled(boolean startTLSEnabled) {
		this.startTLSEnabled = startTLSEnabled;
	}
	
	public boolean isLockFromUser() {
		return lockFromUser;
	}
	
	public boolean getLockFromUser() {
		return lockFromUser;
	}
	
	public void setLockFromUser(boolean lockFromUser) {
		this.lockFromUser = lockFromUser;
	}
}
