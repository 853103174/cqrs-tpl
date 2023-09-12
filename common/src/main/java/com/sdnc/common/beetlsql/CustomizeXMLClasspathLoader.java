package com.sdnc.common.beetlsql;

import org.beetl.sql.core.SqlId;
import org.beetl.sql.xml.XMLClasspathLoader;

import java.net.URL;

public class CustomizeXMLClasspathLoader extends XMLClasspathLoader {

	public CustomizeXMLClasspathLoader(String root, String charset) {
		super(root, charset);

	}

	public CustomizeXMLClasspathLoader(String root) {
		super(root);
	}

	public CustomizeXMLClasspathLoader() {
		this("");
	}

	@Override
	protected URL getFilePath(String root, SqlId id) {
		String path = this.getPathBySqlId(id);
		String filePath = root + "/";
		if (path.indexOf(".")==-1) {
			filePath += path + "/" + path + ".xml";
		} else {
			filePath += path.replace(".", "/") + ".xml";
		}

		return this.getFile(filePath);
	}

}
