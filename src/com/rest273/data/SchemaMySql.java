package com.rest273.data;

import java.sql.*;

import org.codehaus.jettison.json.JSONArray;

import com.rest273.util.ToJSON;



public class SchemaMySql extends MysqlClient{

	public int insertIntoPC_PARTS(String clientID, String times)
			throws Exception {

		PreparedStatement query = null;
		Connection conn = null;

		try {
			/*
			 * If this was a real application, you should do data validation
			 * here before starting to insert data into the database.
			 * 
			 * Important: The primary key on PC_PARTS table will auto increment.
			 * That means the PC_PARTS_PK column does not need to be apart of
			 * the SQL insert query below.
			 */
			conn = mysqlPcPartsConnection();
			query = conn
					.prepareStatement("insert into clientinfo "
							+ "(clientID, times) "
							+ "VALUES ( ?, ? ) ");

			query.setString(1, clientID);
			
			int avilInt = Integer.parseInt(times);
			query.setInt(2, avilInt);
/*
			// PC_PARTS_AVAIL is a number column, so we need to convert the
			// String into a integer
			int avilInt = Integer.parseInt(PC_PARTS_AVAIL);
			query.setInt(4, avilInt);

			query.setString(5, PC_PARTS_DESC);
			
*/
			query.executeUpdate(); // note the new command for insert statement

		} catch (Exception e) {
			e.printStackTrace();
			return 500; // if a error occurs, return a 500
		} finally {
			if (conn != null)
				conn.close();
		}

		return 200;
	}

	public JSONArray queryReturnbrandParts(String brand) throws Exception {

		PreparedStatement query = null;
		Connection conn = null;

		ToJSON converter = new ToJSON();
		JSONArray json = new JSONArray();

		try {
			conn = mysqlPcPartsConnection();
			query = conn
					.prepareStatement("select * from clientinfo where UPPER(clientID) = ?");
			query.setString(1, brand.toUpperCase());
			ResultSet rs = query.executeQuery();

			json = converter.toJSONArray(rs);
			query.close(); // close connection
		}

		catch (SQLException sqlError) {
			sqlError.printStackTrace();
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return json;
		} finally {
			if (conn != null)
				conn.close();
		}

		return json;
	}
}
