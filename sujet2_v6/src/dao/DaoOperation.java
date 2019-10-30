package dao;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;



import model.Bucket;
import utilities.Parameter;

import java.util.Map.Entry;
public class DaoOperation {
	
	private static Connection cnx;
	private static Statement stm;
	
	
	public static PreparedStatement prstm;
	
	
	private static Map<String,String> tableFields;
	
	
	// Establish the connection to the db 
	public DaoOperation(String dbname) throws ClassNotFoundException, SQLException {
		super();
		connection c= new connection(dbname);
		cnx=c.getCnx();
		stm=cnx.createStatement();
		
		
	}
	
	
	
	
	// get all tables from the the database
	public static Vector<String> getAllTables(){
		try {
		DatabaseMetaData md = cnx.getMetaData();
		ResultSet rs;
		
			rs = md.getTables(null, null, "%", new String[] { "TABLE" });
			Vector<String> tables = new Vector<>();
			while (rs.next())
				tables.add(rs.getString(3));
			rs.close();
			return tables;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	
	// return a map with the field name as a key and its type as a value
	public static Map<String,String> getAllFields(String tableName) {
		ResultSet rs;
		try {
			rs = stm.executeQuery("SELECT * FROM " + tableName + " ;");
			ResultSetMetaData metadata = rs.getMetaData();
			HashMap<String, String> map = new HashMap<>();
			Parameter.columns= new Object[metadata.getColumnCount()];
			for (int i = 1; i <= metadata.getColumnCount(); i++) {
				Parameter.columns[i-1]=metadata.getColumnName(i);
				if(metadata.getColumnTypeName(i).equalsIgnoreCase("VARCHAR") || metadata.getColumnTypeName(i).equalsIgnoreCase("CHAR")) {
					map.put(metadata.getColumnName(i),"String");
				}
				else if(metadata.getColumnTypeName(i).equalsIgnoreCase("DATE")) {
					map.put(metadata.getColumnName(i),"date");
				}
				else if(metadata.getColumnTypeName(i).equalsIgnoreCase("INT")) {
				map.put(metadata.getColumnName(i),"int");
				//System.out.println("int");
				}
				else if(metadata.getColumnTypeName(i).equalsIgnoreCase("FLOAT")) {
					map.put(metadata.getColumnName(i),"float");
				}
				else if(metadata.getColumnTypeName(i).equalsIgnoreCase("DOUBLE")) {
					map.put(metadata.getColumnName(i),"double");
				}
			}
			tableFields=map;
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	// get the field of String type
	public static  Vector<String> getStringFields(String tb){
		Map<String,String> mp=DaoOperation.getAllFields(tb);
		Vector<String> stringfields = new Vector<String>();
		for(Entry entry:mp.entrySet()) {
			if(entry.getValue().equals("String")) {
				stringfields.add((String) entry.getKey());
			}
		}
		return stringfields;
	}
	
	
	// get field of numeric type
	public static  Vector<String> getNumericFields(String tb){
		Map<String,String> mp=DaoOperation.getAllFields(tb);
		Vector<String> stringfields = new Vector<String>();
		for(Entry entry:mp.entrySet()) {
			if(entry.getValue().equals("int")) {
				stringfields.add((String) entry.getKey());
			}
		}
		return stringfields;
	}
	

	
	
	
	
	
	
	

	
	// get all the records grouped by buckets (groupid)
public static ArrayList<Bucket> getAllBuckets() {
		
		ArrayList<Bucket> bucketList=new ArrayList<Bucket>();
		try {
			stm = cnx.createStatement();
			ResultSet rs = stm.executeQuery("select  DISTINCT "+Parameter.bktId+" from "+Parameter.tableName+";");
			List<Integer> bucketGroups=new ArrayList<Integer>();

			while (rs.next()) {
				bucketGroups.add(rs.getInt(Parameter.bktId));
			}
			Bucket b;
			for(Integer i:bucketGroups) {
				 rs = stm.executeQuery("select * from "+Parameter.tableName+" where "+Parameter.bktId+" ="+i+";");
				  b= new Bucket();
				  Map<String,String> record;
					while (rs.next()) {
						record= new HashMap<String, String>();
						for(Entry ele :tableFields.entrySet()) {
						//	System.out.println(rs.getString(ele.getKey().toString()));
							record.put(ele.getKey().toString(), rs.getString(ele.getKey().toString()));
						}
						b.getRecords().add(record);
						
					}
					bucketList.add(b);
			}

		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return bucketList;
		
	}



// get total number of data  in database
public  static   int getNumber(String table) {
	
	int rows=0;
	try {

		ResultSet rs = stm.executeQuery("SELECT count(*) FROM " + table +  ";");
		if(rs.next())
			rows=rs.getInt(1);
		
		
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	return rows;
	
	
}

	



public static Object[][] getResultSet(String table) {
	ResultSet rs = null;
	ResultSet rs1 = null;
	Object[][] rowdata = null ;
	int rows=0;
		try {
			//System.out.println("SELECT * FROM " + table + " order by "+Parameter.bktId+";");
			rs1 = stm.executeQuery("SELECT count(*) FROM " + table +  ";");
			if(rs1.next())
				rows=rs1.getInt(1);
			
			rs = stm.executeQuery("SELECT * FROM " + table + " order by "+Parameter.bktId+";");
			 rowdata = new Object[rows][Parameter.columns.length];
			int j=0;
			while(rs.next()) {
			for(int i=0;i<Parameter.columns.length;i++) {
					rowdata[j][i]=rs.getString(i+1);
				}
				
				j++;
			}
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		return rowdata;
}


// insert the result into a new table
public static void insertResult(ArrayList<Bucket> list) {
	
	String createTab="create table if NOT EXISTS "+Parameter.resultTable+" as select * from "+Parameter.tableName+";";
	String trunCatTab="TRUNCATE "+Parameter.resultTable+";";
	
	String insertQry="insert into "+Parameter.resultTable+" (";
	String compeletion=" values(";
	
	
	for(int i=0;i<Parameter.columns.length;i++) {
		if(i==Parameter.columns.length-1) {
			insertQry=insertQry+" "+Parameter.columns[i]+")";
			compeletion+="?);";
		}else {
			insertQry=insertQry+" "+Parameter.columns[i]+",";	
			compeletion+="?,";
		}
		
		
	}
	
	
	
	  try {
		stm.executeUpdate(createTab);
		stm.executeUpdate(trunCatTab);
		
		prstm=cnx.prepareStatement(insertQry+compeletion);
		
		
		
		for(Bucket b :list) {
			
		    
			for(Map<String, String> ele:b.getRecords()) {
				
				for(int i=1;i<=Parameter.columns.length;i++) {
					
					prstm.setObject(i, ele.get(Parameter.columns[i-1]));
					
				}
				prstm.executeUpdate();
			}
			}
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	
			
}






}
