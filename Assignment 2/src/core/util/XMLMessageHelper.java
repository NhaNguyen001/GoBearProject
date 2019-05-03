package core.util;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import java.io.File;

public class XMLMessageHelper {
	private Document doc;
	private final String MSG_CRITERIA = "Attribute.elementAddress";
	private final String MSGVALUE = "Attribute.attributeValue";
	private final String MSGSTATUS = "Notification.alarmStatus";
	private final String MSG_ADDITIONALINFO = "Attribute.attributeValue";
	private final String NOTIFICATION_DEVICE = "Notification.device";

	public XMLMessageHelper(String xmlFilePath) {
		File fXmlFile = new File(xmlFilePath);
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public String getMsgValue() {
		return getElement(MSGVALUE);
	}
	
	public String getMsgStatus() {
		return getElement(MSGSTATUS);
	}
	
	public String getMsgAdditionalInfo() {
		return getSecondElement(MSG_ADDITIONALINFO);
	}
	
	
	public String getMsgCriteria() {
		return getSecondElement(MSG_CRITERIA);
	}
	
	public String getNotificationDevice() {
		return getElement(NOTIFICATION_DEVICE);
	}

	private String getElement(String name) {
		return doc != null ? doc.getElementsByTagName(name).item(0).getTextContent() : null;
	}
	
	private String getSecondElement(String name) {
		return doc != null && doc.getElementsByTagName(name).getLength() != 0
				&& doc.getElementsByTagName(name).getLength() > 1
						? doc.getElementsByTagName(name).item(1).getTextContent()
						: null;
	}
}
