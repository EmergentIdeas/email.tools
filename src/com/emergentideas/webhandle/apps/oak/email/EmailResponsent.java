package com.emergentideas.webhandle.apps.oak.email;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.output.HTML5SegmentedOutput;
import com.emergentideas.webhandle.output.HtmlDocRespondent;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.TemplateSource;

public class EmailResponsent extends HtmlDocRespondent {
	
	public EmailResponsent() {
		super(null);
		try {
			SegmentedOutput output = new HTML5SegmentedOutput();
			setOutput((Object)output);
			setOutput((SegmentedOutput)output);
			this.output = output;
		
			lineReturnBytes = lineReturn.getBytes(characterSet);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String createEmail(String templateName, TemplateSource templateSource, Location parentLocation, Object ... locationObjects) {
		Location location;
		if(parentLocation != null) {
			location = new AppLocation(parentLocation);
		}
		else {
			location = new AppLocation();
		}
		
		WebAppLocation wal = new WebAppLocation(location);
		if(templateSource != null) {
			wal.setTemplateSource(templateSource);
		}
		else if(templateSource == null && parentLocation != null){
			wal.setTemplateSource(new WebAppLocation(parentLocation).getTemplateSource());
		}
		
		for(Object o : locationObjects) {
			location.add(o);
		}
		
		wal.getTemplateSource().get(templateName).render(output, location, "body", null);
		
		return getHtmlContent();
	}
	
	public String getHtmlContent() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			
			// get the doc type and html element created
			write(os, output.getStream("docType"), true);
			write(os, output.getStream("htmlTag"), true);
			
			// add the header content
			write(os, "<head>", true);
			
			write(os, output.getStream("htmlHeader"), true);
			
			
			if(output.getStream("title").length() > 0) {
				write(os, "<title>" + StringEscapeUtils.escapeHtml(output.getStream("title").toString()) + "</title>", true);
			}
			
			// Write the meta tags that are just name/value pairs
			Map<String, String> namedMeta = output.getPropertySet("namedMeta");
			for(String key : namedMeta.keySet()) {
				String value = namedMeta.get(key);
				if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
					write(os, "<meta name=\"" + key + "\" content=\"" + StringEscapeUtils.escapeHtml(value) + "\" />", true);
				}
			}
			
			Map<String, String> cssIncludes = output.getPropertySet("cssIncludes");
			
			if(cssIncludes.size() > 0) {
				write(os, "<style type=\"text/css\">", true);
			}
			for(String key : cssIncludes.keySet()) {
				String mediaQuery = cssIncludes.get(key);
				write(os, "@import url(" + key + ") ", false);
				if(StringUtils.isBlank(mediaQuery) == false) {
					write(os, mediaQuery, false);
				}
				write(os, ";", true);
			}
			if(cssIncludes.size() > 0) {
				write(os, "</style>", true);
			}
			
			
			addJavascriptLibraries(os, output.getList("headerLibraries"));
			
			addJavascript(os, output.getStream("headerScript"));
			
			StringBuilder sb = output.getStream("headerStyle");
			if(sb.length() > 0) {
				write(os, "<style type=\"text/css\">", true);
				write(os, sb, true);
				write(os, "</style>", true);
			}
			
			write(os, "</head>", true);
			
			// done with the header, let's work on the body
			write(os, output.getStream("bodyOpen"), true);
			write(os, output.getStream("bodyPre"), true);
			
			write(os, output.getStream("body"), true);
			
			addJavascriptLibraries(os, output.getList("footerLibraries"));
			addJavascript(os, output.getStream("footerScript"));
	
			write(os, output.getStream("bodyPost"), true);
			write(os, output.getStream("docClose"), false);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return os.toString();
	}
}
