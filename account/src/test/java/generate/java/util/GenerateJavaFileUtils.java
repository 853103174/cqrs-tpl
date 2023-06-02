package generate.java.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.exception.BeetlException;
import org.beetl.core.resource.FileResourceLoader;
import org.noear.snack.ONode;
import org.yaml.snakeyaml.Yaml;

import com.sdnc.common.kits.KV;

/**
 * 数据库表转换成javaBean对象小工具
 * 1 bean属性按原始数据库字段经过去掉下划线,并大写处理首字母.
 * 2 生成的bean带了数据库的字段说明.
 */
public class GenerateJavaFileUtils {
	private static Map<String, String> map = new HashMap<>();
	/***** 包名开始 *****/
	private static String command_assembler_name = "";
	private static String command_service_name = "";
	private static String command_vo_name = "";
	private static String command_po_name = "";
	private static String command_dao_name = "";
	private static String command_metaq_name = "";
	private static String command_event_name = "";
	private static String command_controller_name = "";
	private static String command_businessobject_name = "";
	/***** 包名结束 *****/
	private static String tablename = ""; // 表名
	private static String tableremarks = ""; // 表备注
	private static String pk = ""; // 主键
	private static String baseTemplatePath; // 模板引擎文件夹路径

	public static void main(String[] args) {
		tablename = "area";
		createPackage("area");

		getTableInfo();
		table2entity();
		table2dao();
		table2service();
		table2controller();
	}

	/**
	 * 创建包名
	 *
	 * @param packages
	 *                 包名
	 */
	private static void createPackage(String packages) {
		String basePath = System.getProperty("java.class.path");
		String basepackagename = basePath.substring(0, basePath.indexOf("bin")) + "src/main/java/com/sdnc/account";
		String command_assembler = basepackagename + "/application/assembler/";
		command_assembler_name = String.format(command_assembler, packages);
		File dir = new File(command_assembler_name);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String command_service = basepackagename + "/application/service/%s/";
		command_service_name = String.format(command_service, packages);
		dir = new File(command_service_name);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String command_vo = basepackagename + "/domain/valueobject/%s/";
		command_vo_name = String.format(command_vo, packages);
		dir = new File(command_vo_name);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String command_po = basepackagename + "/domain/persistantobject/%s/";
		command_po_name = String.format(command_po, packages);
		dir = new File(command_po_name);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String command_bo = basepackagename + "/domain/businessobject/%s/";
		command_businessobject_name = String.format(command_bo, packages);
		dir = new File(command_businessobject_name);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String command_mapper = basepackagename + "/infrastructure/dao/%s/";
		command_dao_name = String.format(command_mapper, packages);
		dir = new File(command_dao_name);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// MQ订阅
		String command_metaq = basepackagename + "/interfaces/metaq/";
		command_metaq_name = String.format(command_metaq, packages);
		dir = new File(command_metaq_name);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// MQ发布
		String command_event = basepackagename + "/application/event/";
		command_event_name = String.format(command_event, packages);
		dir = new File(command_event_name);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String command_controller = basepackagename + "/interfaces/controller/%s/";
		command_controller_name = String.format(command_controller, packages);
		dir = new File(command_controller_name);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		baseTemplatePath = System.getProperty("user.dir") + "/generate";
	}

	/**
	 * 获取主键和备注
	 *
	 * @param table
	 *              表名
	 */
	private static void getTableInfo() {
		Connection conn = null;
		DatabaseMetaData dbmd = null;

		try {
			conn = getConnection();
			dbmd = conn.getMetaData();
			ResultSet resultSet = dbmd.getTables(conn.getCatalog(), getSchema(conn), tablename,
					new String[] { "TABLE", "REMARKS" });
			while (resultSet.next()) {
				tableremarks = resultSet.getString("REMARKS");
				DatabaseMetaData dmd = conn.getMetaData();
				ResultSet rs = dmd.getColumns(conn.getCatalog(), getSchema(conn), tablename, "%");
				while (rs.next()) {
					map.put(processColnames(rs.getString("COLUMN_NAME")),
							rs.getString("REMARKS").replaceAll("\n", " "));
				}

				rs = dmd.getPrimaryKeys(conn.getCatalog(), getSchema(conn), tablename);
				while (rs.next()) {
					pk = processColnames(rs.getString(4));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(conn);
		}
	}

	/**
	 * 其他数据库不需要这个方法 oracle和db2需要
	 *
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	private static String getSchema(Connection conn) throws SQLException {
		String schema = conn.getMetaData().getUserName();
		if (schema == null || schema.length() == 0) {
			throw new SQLException("ORACLE数据库模式不允许为空");
		}

		return schema.toUpperCase();
	}

	/**
	 * 把类名改成驼峰命名法
	 *
	 * @param str
	 * @return
	 */
	private static String hump(String str) {
		int index = -1;
		char[] ch = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		sb.append((char) (ch[str.indexOf("_") + 1] - 32));
		for (int i = str.indexOf("_") + 2, size = ch.length; i < size; ++i) {
			if (ch[i] == '_') {
				index = i;
				continue;
			} else if (i == index + 1) {
				sb.append((char) (ch[i] - 32));
			} else {
				sb.append(ch[i]);
			}
		}

		return sb.toString();
	}

	/**
	 * 处理列名,把空格下划线'_'去掉, 同时把下划线后的首字母大写
	 *
	 * @param str
	 */
	private static String processColnames(String str) {
		int index = -10;
		char[] ch = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0, size = ch.length; i < size; ++i) {
			if (ch[i] == '_') {
				index = i;
			} else if (i == index + 1) {
				sb.append((char) (ch[i] - 32));
			} else {
				sb.append(ch[i]);
			}
		}

		return sb.toString();
	}

	/**
	 * 列名首字母大写
	 *
	 * @param str
	 */
	private static String firstCharToUpperCase(String str) {
		char[] ch = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0, size = ch.length; i < size; ++i) {
			if (i == 0) {
				sb.append((char) (ch[0] - 32));
			} else {
				sb.append(ch[i]);
			}
		}

		return sb.toString();
	}

	/**
	 * 将数据库类型转换为Java类型
	 *
	 * @param sqlType
	 * @param javaType
	 * @return
	 */
	private static String sqlType2JavaType(String sqlType, String javaType) {
		if (sqlType.equals("datetime") || sqlType.equals("timestamp")) {
			return "LocalDateTime";
		} else if (sqlType.equals("date")) {
			return "LocalDate";
		} else if (sqlType.equals("decimal")) {
			return "Double";
		} else if (sqlType.equals("bigint")) {
			return "Long";
		} else {
			return javaType.substring(javaType.lastIndexOf(".") + 1);
		}
	}

	/**
	 * 获取jcbc链接
	 *
	 * @return
	 */
	private static Connection getConnection() {
		Connection localConnection = null;
		try {
			String basePath = System.getProperty("java.class.path");
			String confs = basePath.substring(0, basePath.indexOf("bin")) + "src/main/resources/dev/application.yml";
			Map<String, String> conf = new Yaml().load(new FileInputStream(new File(confs)));
			ONode node = ONode.load(conf).select("$.spring.datasource");
			String jdbcUrl = node.get("jdbcUrl").getString();
			String username = node.get("username").getString();
			String password = node.get("password").getString();
			String driverClassName = node.get("driverClassName").getString();
			Class.forName(driverClassName);
			Properties props = new Properties();
			props.put("user", username);
			props.put("password", password);
			props.put("useInformationSchema", "true");
			localConnection = DriverManager.getConnection(jdbcUrl, props);
			localConnection.setAutoCommit(false);
		} catch (ClassNotFoundException | SQLException | FileNotFoundException e) {
			e.printStackTrace();
		}

		return localConnection;
	}

	/**
	 * 释放jdbc链接
	 *
	 * @param conn
	 */
	private static void releaseConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 生成实体
	 */
	public static void table2entity() {
		Connection conn = getConnection();
		String strsql = "SELECT * FROM " + tablename;
		try {
			String colName = null;
			String colType = null;
			String javaType = null;
			String className = hump(tablename);
			String poPackage = command_po_name.substring(command_po_name.indexOf("com/"), command_po_name.length() - 1)
					.replaceAll("/", ".");
			PreparedStatement pstmt = conn.prepareStatement(strsql);
			pstmt.executeQuery();
			ResultSetMetaData rsmd = pstmt.getMetaData();
			KV paras = KV.by("packageName", poPackage).set("tableRemark", tableremarks).set("tableName", tablename)
					.set("className", className).set("importBigInteger", false).set("importDate", false)
					.set("importDateAt", false).set("importTime", false).set("importTimeAt", false);
			List<KV> columns = new ArrayList<>();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				KV column = new KV();
				colName = processColnames(rsmd.getColumnName(i + 1));
				colType = rsmd.getColumnTypeName(i + 1).toLowerCase();
				javaType = sqlType2JavaType(colType, rsmd.getColumnClassName(i + 1));
				if (pk.equals(colName)) {
					column.set("pk", true);
				}
				if (javaType.equals("BigInteger")) {
					paras.set("importBigInteger", true);
				}
				if (javaType.equals("LocalDate")) {
					paras.set("importDate", true);

					if (!"createAt".equals(colName) && !"updateAt".equals(colName)) {
						paras.set("importDateAt", true);
					}
				}
				if (javaType.equals("LocalDateTime")) {
					paras.set("importTime", true);

					if (!"createAt".equals(colName) && !"updateAt".equals(colName)) {
						paras.set("importTimeAt", true);
					}
				}
				column.set("colName", colName);
				column.set("maxSize", rsmd.getColumnDisplaySize(i + 1));
				column.set("colNameToUpper", firstCharToUpperCase(colName));
				column.set("remark", map.get(colName));
				column.set("javaType", javaType);
				columns.add(column);
			}
			paras.set("columns", columns);
			// PO
			StringBuffer sb = new StringBuffer();
			sb.append(command_po_name);
			sb.append(className).append("PO.java");
			FileResourceLoader resourceLoader = new FileResourceLoader(baseTemplatePath, "UTF-8");
			Configuration cfg = Configuration.defaultConfiguration();
			GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
			Template tpl = gt.getTemplate("/po.btl");
			tpl.binding(paras);
			tpl.renderTo(new FileOutputStream(new File(sb.toString())));
			// PageVO
			String boPackage = command_vo_name.substring(command_vo_name.indexOf("com/"), command_vo_name.length() - 1)
					.replaceAll("/", ".");
			paras.set("packageName", boPackage).set("importPO", poPackage);
			sb = new StringBuffer();
			sb.append(command_vo_name);
			sb.append(className).append("PageVO.java");
			resourceLoader = new FileResourceLoader(baseTemplatePath, "UTF-8");
			cfg = Configuration.defaultConfiguration();
			gt = new GroupTemplate(resourceLoader, cfg);
			tpl = gt.getTemplate("/pagevo.btl");
			tpl.binding(paras);
			tpl.renderTo(new FileOutputStream(new File(sb.toString())));
			// ViewVO
			sb = new StringBuffer();
			sb.append(command_vo_name);
			sb.append(className).append("ViewVO.java");
			resourceLoader = new FileResourceLoader(baseTemplatePath, "UTF-8");
			cfg = Configuration.defaultConfiguration();
			gt = new GroupTemplate(resourceLoader, cfg);
			tpl = gt.getTemplate("/viewvo.btl");
			tpl.binding(paras);
			tpl.renderTo(new FileOutputStream(new File(sb.toString())));
			// PageBO
			String doPackage = command_businessobject_name
					.substring(command_businessobject_name.indexOf("com/"), command_businessobject_name.length() - 1)
					.replaceAll("/", ".");
			paras.set("packageName", doPackage);
			sb = new StringBuffer();
			sb.append(command_businessobject_name);
			sb.append(className).append("PageBO.java");
			resourceLoader = new FileResourceLoader(baseTemplatePath, "UTF-8");
			cfg = Configuration.defaultConfiguration();
			gt = new GroupTemplate(resourceLoader, cfg);
			tpl = gt.getTemplate("/pagebo.btl");
			tpl.binding(paras);
			tpl.renderTo(new FileOutputStream(new File(sb.toString())));
			// CreateBO
			sb = new StringBuffer();
			sb.append(command_businessobject_name);
			sb.append(className).append("CreateBO.java");
			resourceLoader = new FileResourceLoader(baseTemplatePath, "UTF-8");
			cfg = Configuration.defaultConfiguration();
			gt = new GroupTemplate(resourceLoader, cfg);
			tpl = gt.getTemplate("/createbo.btl");
			tpl.binding(paras);
			tpl.renderTo(new FileOutputStream(new File(sb.toString())));
			// ModifyBO
			sb = new StringBuffer();
			sb.append(command_businessobject_name);
			sb.append(className).append("ModifyBO.java");
			resourceLoader = new FileResourceLoader(baseTemplatePath, "UTF-8");
			cfg = Configuration.defaultConfiguration();
			gt = new GroupTemplate(resourceLoader, cfg);
			tpl = gt.getTemplate("/modifybo.btl");
			tpl.binding(paras);
			tpl.renderTo(new FileOutputStream(new File(sb.toString())));

			System.out.println("Entity生成成功***********");
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(conn);
		}
	}

	/**
	 * 生成Mapper
	 */
	public static void table2dao() {
		try {
			String className = hump(tablename);
			String packageName = command_dao_name
					.substring(command_dao_name.indexOf("com/"), command_dao_name.length() - 1)
					.replaceAll("/", ".");
			StringBuffer sb = new StringBuffer();
			sb.append(command_dao_name);
			sb.append(className).append("Dao.java");
			String poPackage = command_po_name.substring(command_po_name.indexOf("com/"), command_po_name.length() - 1)
					.replaceAll("/", ".");
			KV paras = KV.by("packageName", packageName).set("tableRemark", tableremarks).set("className", className)
					.set("importPO", poPackage);
			FileResourceLoader resourceLoader = new FileResourceLoader(baseTemplatePath, "UTF-8");
			Configuration cfg = Configuration.defaultConfiguration();
			GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
			Template tpl = gt.getTemplate("/dao.btl");
			tpl.binding(paras);
			tpl.renderTo(new FileOutputStream(new File(sb.toString())));
			System.out.println("Dao生成成功***********");
		} catch (BeetlException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成Service
	 */
	public static void table2service() {
		try {
			String className = hump(tablename);
			String packageName = command_service_name
					.substring(command_service_name.indexOf("com/"), command_service_name.length() - 1)
					.replaceAll("/", ".");
			StringBuffer sb = new StringBuffer();
			sb.append(command_service_name);
			sb.append(className).append("CmdSvc.java");
			String poPackage = command_po_name.substring(command_po_name.indexOf("com/"), command_po_name.length() - 1)
					.replaceAll("/", ".");
			String daoPackage = command_dao_name
					.substring(command_dao_name.indexOf("com/"), command_dao_name.length() - 1)
					.replaceAll("/", ".");
			String doPackage = command_businessobject_name
					.substring(command_businessobject_name.indexOf("com/"), command_businessobject_name.length() - 1)
					.replaceAll("/", ".");
			String boPackage = command_vo_name.substring(command_vo_name.indexOf("com/"), command_vo_name.length() - 1)
					.replaceAll("/", ".");
			KV paras = KV.by("packageName", packageName).set("tableRemark", tableremarks).set("className", className)
					.set("importPO", poPackage).set("importDao", daoPackage).set("importDO", doPackage)
					.set("importBO", boPackage);
			// CmdSvc
			FileResourceLoader resourceLoader = new FileResourceLoader(baseTemplatePath, "UTF-8");
			Configuration cfg = Configuration.defaultConfiguration();
			GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
			Template tpl = gt.getTemplate("/cmdsvc.btl");
			tpl.binding(paras);
			tpl.renderTo(new FileOutputStream(new File(sb.toString())));
			// QrySvc
			sb = new StringBuffer();
			sb.append(command_service_name);
			sb.append(className).append("QrySvc.java");
			resourceLoader = new FileResourceLoader(baseTemplatePath, "UTF-8");
			cfg = Configuration.defaultConfiguration();
			gt = new GroupTemplate(resourceLoader, cfg);
			tpl = gt.getTemplate("/qrysvc.btl");
			tpl.binding(paras);
			tpl.renderTo(new FileOutputStream(new File(sb.toString())));

			System.out.println("Service生成成功***********");
		} catch (BeetlException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成Controller
	 */
	public static void table2controller() {
		try {
			String className = hump(tablename);
			String packageName = command_controller_name
					.substring(command_controller_name.indexOf("com/"), command_controller_name.length() - 1)
					.replaceAll("/", ".");
			String servicePackage = command_service_name
					.substring(command_service_name.indexOf("com/"), command_service_name.length() - 1)
					.replaceAll("/", ".");
			String doPackage = command_businessobject_name
					.substring(command_businessobject_name.indexOf("com/"), command_businessobject_name.length() - 1)
					.replaceAll("/", ".");
			String boPackage = command_vo_name.substring(command_vo_name.indexOf("com/"), command_vo_name.length() - 1)
					.replaceAll("/", ".");
			KV paras = KV.by("packageName", packageName).set("tableRemark", tableremarks)
					.set("path", tablename.substring(tablename.indexOf("_") + 1).replaceAll("_", "-"))
					.set("className", className).set("importSvc", servicePackage).set("importDO", doPackage)
					.set("importBO", boPackage);
			// CmdExe
			StringBuffer sb = new StringBuffer();
			sb.append(command_controller_name);
			sb.append(className).append("CmdExe.java");
			FileResourceLoader resourceLoader = new FileResourceLoader(baseTemplatePath, "UTF-8");
			Configuration cfg = Configuration.defaultConfiguration();
			GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
			Template tpl = gt.getTemplate("/cmdexe.btl");
			tpl.binding(paras);
			tpl.renderTo(new FileOutputStream(new File(sb.toString())));
			// QryExe
			sb = new StringBuffer();
			sb.append(command_controller_name);
			sb.append(className).append("QryExe.java");
			resourceLoader = new FileResourceLoader(baseTemplatePath, "UTF-8");
			cfg = Configuration.defaultConfiguration();
			gt = new GroupTemplate(resourceLoader, cfg);
			tpl = gt.getTemplate("/qryexe.btl");
			tpl.binding(paras);
			tpl.renderTo(new FileOutputStream(new File(sb.toString())));

			System.out.println("Controller生成成功***********");
		} catch (BeetlException | IOException e) {
			e.printStackTrace();
		}
	}

}