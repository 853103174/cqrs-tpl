package com.sdnc.common.beetlsql;

import lombok.NoArgsConstructor;
import org.beetl.sql.core.SqlId;
import org.beetl.sql.xml.XMLClasspathLoader;

import java.net.URL;

/**
 *
 * BeetlSQL支持XML语法
 *
 */
@NoArgsConstructor
public class CustomizeXMLClasspathLoader extends XMLClasspathLoader {

	public CustomizeXMLClasspathLoader(String root) {
		super(root);
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
