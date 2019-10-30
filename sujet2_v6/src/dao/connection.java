package dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JComboBox;

public class connection {
	
	public final String url = "jdbc:mysql://localhost/";
	public final String connectionURL = "jdbc:mysql://localhost\\SQLEXPRESS";
	public static JComboBox<String> comboBDS = new JComboBox<String>();

	public final String user = "root";
	public final String password = "";
	public final static String driver = "com.mysql.jdbc.Driver";
	private  Connection cnx=null;
	
	public connection(String db) throws ClassNotFoundException, SQLException {
		super();
		Class.forName(driver);
		cnx = DriverManager.getConnection(url + db, user, password);

		
	}

	
	

	
	// get all the databases 
	public static JComboBox<String> getDbsName(){
	
		
		ResultSet resultSet;
		Connection con;
		try {
			
			
			Class.forName(driver);
		    con=DriverManager.getConnection("jdbc:mysql://localhost","root","");
			DatabaseMetaData meta = con.getMetaData();
			resultSet = meta.getCatalogs();
			while (resultSet.next()) {
			   String name = resultSet.getString("TABLE_CAT");
			   comboBDS.addItem(name);
			}
			
			
			resultSet.close();
			con.close();
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	
		
		return comboBDS;

	
	}
	
	// getters and setters
	
	public  Connection getCnx() {
		return cnx;
	}

	public  void setCnx(Connection cnx) {
		this.cnx = cnx;
	}
	
	
	

}
