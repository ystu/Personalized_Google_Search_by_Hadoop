package dataBase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

import com.mysql.jdbc.Driver;

public class DbManager {
	

	/**
	 * ��o�w�]��Ʈw�s��
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String dbName) throws SQLException {
		return getConnection(dbName, "root", "root");
	}

	/**
	 * ��o��Ʈw�s��
	 * 
	 * @param dbName
	 * @param userName
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String dbName, String userName,
			String password) throws SQLException {

		String url = "jdbc:mysql://localhost:3306/" + dbName
				+ "?Unicode=true&characterEncoding=utf8";

		DriverManager.registerDriver(new Driver());

		return DriverManager.getConnection(url, userName, password);
	}

	/**
	 * �]�w PreparedStatement �Ѽ�
	 * 
	 * @param preStmt
	 * @param params
	 * @throws SQLException
	 */
	public static void setParams(PreparedStatement preStmt, Object... params)throws SQLException {

		if (params == null || params.length == 0)
			return;

		for (int i = 1; i <= params.length; i++) {
			Object param = params[i - 1];
			if (param == null) {
				preStmt.setNull(i, Types.NULL);
			} else if (param instanceof Integer) {
				preStmt.setInt(i, (Integer) param);
			} else if (param instanceof String) {
				preStmt.setString(i, (String) param);
			} else if (param instanceof Double) {
				preStmt.setDouble(i, (Double) param);
			}
		}
	}

	/**
	 * ���� SQL�A�Ǧ^�v�T�����
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static int executeUpdate(String sql,String dbName) throws SQLException {
		return executeUpdate(sql,dbName, new Object[] {});
	}

	/**
	 * �a�Ѽư���SQL�A�Ǧ^�v�T�����
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static int executeUpdate(String sql,String dbName, Object... params)
			throws SQLException {

		Connection conn = null;
		PreparedStatement preStmt = null;

		try {
			conn = getConnection(dbName);

			preStmt = conn.prepareStatement(sql);

			setParams(preStmt, params);

			return preStmt.executeUpdate();

		} finally {
			if (preStmt != null)
				preStmt.close();
			if (conn != null)
				conn.close();
		}
	}

	/**
	 * ��o�`�ơC
	 * 
	 * @param sql
	 *            �榡������ SELECT count(*) FROM ...
	 * @return
	 * @throws SQLException
	 */
	public static int getCount(String sql) throws SQLException {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection("hvsrdb");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();
			return rs.getInt(1);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		}
	}

}
