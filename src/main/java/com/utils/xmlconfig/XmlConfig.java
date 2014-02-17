package com.utils.xmlconfig;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class XmlConfig {
	static public Object getObjectFromXml(String xmlObjectString)
			throws Exception {
		try {

			XMLDecoder xmlDecoder = new XMLDecoder(new BufferedInputStream(
					new FileInputStream(xmlObjectString)));
			Object object = xmlDecoder.readObject();
			if (object == null)
				throw new Exception();
			return object;
		} catch (Exception e) {
			throw e;
		}
	}
}
