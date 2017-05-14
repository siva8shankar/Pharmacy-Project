package com.techmahindra;
import java.sql.*;
public class RegisterUser {
static int status=0;
//int accountno=1;
public static int register(String username,String password,String repassword,String disname,String adderess,String cityname,String statename,String country,String region,double phone,String email){
	//public static int register(String email,String password,String gender,String country,String name){

	Connection con=GetCon.getCon();
	PreparedStatement ps;
	try {
		ps = con.prepareStatement("Insert into NEWCUST4 values(?,?,?,?,?,?,?,?,?,?,?,?)");
		int	nextvalue1=GetCon.getPrimaryKey();
	 	ps.setInt(1,nextvalue1);
	    ps.setString(2,username);
		ps.setString(3,password);
		ps.setString(4,repassword);
		ps.setString(5,disname);
		ps.setString(6,adderess);
		ps.setString(7,cityname);
		ps.setString(8,statename);
		ps.setString(9,country);
		ps.setString(10,region);
		ps.setDouble(11,phone);
		ps.setString(12,email);
		
		status=ps.executeUpdate();
		
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
	return status;
	
}
public static int registerPatient(String patientname, String doctorname,
		String madicinename, double costofmadicine, double quantity,
		double amount, String date, double stax, double totamt, String place,
		String billnumber, String email) {
	Connection con1=GetCon.getCon();
	PreparedStatement ps1;
	try {
		ps1 = con1.prepareStatement("Insert into NEWPATIENT values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
		int	nextvalue1=GetCon.getPatientPrimaryKey();
	 	ps1.setInt(1,nextvalue1);
	    ps1.setString(2,patientname);
		ps1.setString(3,doctorname);
		ps1.setString(4,madicinename);
		ps1.setDouble(5,costofmadicine);
		ps1.setDouble(6,quantity);
		ps1.setDouble(7,amount);
		ps1.setString(8,date);
		ps1.setDouble(9,stax);
		ps1.setDouble(10,totamt);
		ps1.setString(11,place);
		ps1.setString(12,billnumber);
		ps1.setString(13,email);
		
		status=ps1.executeUpdate();
		
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
	return status;
}

}
