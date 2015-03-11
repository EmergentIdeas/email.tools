package com.emergentideas.webhandle.apps.oak.email;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.assumptions.oak.interfaces.EmailService;

public class FormHandleBase {
	
	@Resource
	protected EmailService emailService;
	
	protected String sendTo = "dan@emergentideas.com";
	protected String fromAddress = "contact@emergentideas.com";
	protected String subjectLine = "A contact from the web";
	
	protected String emailTemplate = "email/submission-email";
	
	protected String responseTemplate = "msg-thank-you";
	
	protected Object sendContact(Location location, Object submission) {
		String email = createEmail(getEmailTemplateName(location, submission), location, submission);
		email = clean(email);
		String userFrom = extractUserFrom(location, submission);
		if(userFrom == null) {
			userFrom = getFrom(location, submission);
		}
		try {
			send(userFrom, email, location, submission);
		}
		catch (Exception e) {
			send(getFrom(location, submission), email, location, submission);
		}
		
		return getResponseTemplateName(location, submission);
	}
	
	protected String clean(String email) {
		return Jsoup.clean(email, Whitelist.relaxed());
	}
	
	protected String createEmail(String templateName, Location requestLocation, Object submission) {
		EmailResponsent resp = new EmailResponsent();
		String email = resp.createEmail(templateName, new WebAppLocation(requestLocation).getTemplateSource(), null, submission);
		return email;
	}
	
	protected void send(String from, String email, Location location, Object submission) {
		emailService.sendEmail(new String[] {getTo(location, submission)}, from, null, null, getSubject(location, submission), null, email);
	}
	
	protected String extractUserFrom(Location location, Object submission) {
		return null;
	}
	
	protected String getSubject(Location location, Object submission) {
		return subjectLine;
	}
	
	protected String getTo(Location location, Object submission) {
		return sendTo;
	}
	
	protected String getFrom(Location location, Object submission) {
		return fromAddress;
	}
	
	protected String getEmailTemplateName(Location location, Object submission) {
		return emailTemplate;
	}
	
	protected String getResponseTemplateName(Location location, Object submission) {
		return responseTemplate;
	}
	

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getSubjectLine() {
		return subjectLine;
	}

	public void setSubjectLine(String subjectLine) {
		this.subjectLine = subjectLine;
	}

	public String getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(String emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public String getResponseTemplate() {
		return responseTemplate;
	}

	public void setResponseTemplate(String responseTemplate) {
		this.responseTemplate = responseTemplate;
	}

	
}
