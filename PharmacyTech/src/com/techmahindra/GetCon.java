package com.techmahindra;
import java.sql.*;
public class GetCon {
private GetCon(){}

public static Connection con;
static{
	try {
		Class.forName(DBIntializer.DRIVER);
		System.out.print("Driver loaded successfully");
		con=DriverManager.getConnection(DBIntializer.CON_STRING,DBIntializer.USERNAME,DBIntializer.PASSWORD);
		System.out.print("Connection established successfully");
	} catch (ClassNotFoundException e) {
		
		e.printStackTrace();
	} catch (SQLException e) {
	
		System.out.println("Exception in GetCon");
	}
	
}
public static Connection getCon(){
	return con;
}



public static int getPrimaryKey(){
	int nextvalue=0;
	Connection con=GetCon.getCon();
	PreparedStatement ps2;
	try {
	
	ps2=con.prepareStatement("select javatpoint1.nextval from dual");
	
	ResultSet rs=ps2.executeQuery();
	rs.next();
	nextvalue=rs.getInt(1);

	
	
} catch (SQLException e) {
		
		e.printStackTrace();
	}
return nextvalue;

}

public static int getPatientPrimaryKey(){
	int nextvalue1=0;
	Connection con=GetCon.getCon();
	PreparedStatement ps3;
	try {
	
	ps3=con.prepareStatement("select Patient_seq.nextval from dual");
	
	ResultSet rs=ps3.executeQuery();
	rs.next();
	nextvalue1=rs.getInt(1);

	
	
} catch (SQLException e) {
		
		e.printStackTrace();
	}
return nextvalue1;

}

}

