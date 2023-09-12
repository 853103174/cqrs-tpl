package com.sdnc.common.beetlsql;

import org.beetl.sql.core.SQLManager;
import org.beetl.sql.xml.XMLBeetlSQL;
import org.beetl.sql.xml.XMLClasspathLoader;

import lombok.NoArgsConstructor;

/**
 * BeetlSQL支持XML语法
 */
@NoArgsConstructor
public final class CustomizeXMLBeetlSQL extends XMLBeetlSQL {

    public void config(String root, SQLManager sqlManager) {
        XMLClasspathLoader xmlClasspathLoader = new CustomizeXMLClasspathLoader(root);
        changeToXMLLoader(sqlManager, xmlClasspathLoader);
        super.config(sqlManager);
    }

}
