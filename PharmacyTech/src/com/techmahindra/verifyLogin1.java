package com.techmahindra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class verifyLogin1 {

public static boolean checkLogin(String username,String password){
	boolean status=false;
	Connection con=GetCon.getCon();
	try {
		//PreparedStatement ps=con.prepareStatement("Select * from MAILCASTINGUSER where EMAILADD = ? and PASSWORD =?");
		PreparedStatement ps=con.prepareStatement("Select * from NEWCUST4 where username = ? and password =?");
		//ps.setInt(1,accountno);
		ps.setString(1,username);
		ps.setString(2,password);
		
		ResultSet rs=ps.executeQuery();
		status=rs.next();
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return status;
}
public static boolean checkPatientLogin(String patientname,String email){
	boolean status=false;
	Connection con=GetCon.getCon();
	try {
		//PreparedStatement ps=con.prepareStatement("Select * from MAILCASTINGUSER where EMAILADD = ? and PASSWORD =?");
		PreparedStatement ps1=con.prepareStatement("Select * from NEWPATIENT where PATIENT_NAME = ? and EMAIL =?");
		//ps.setInt(1,accountno);
		ps1.setString(1,patientname);
		ps1.setString(2,email);
		
		ResultSet rs=ps1.executeQuery();
		status=rs.next();
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return status;
}

}
