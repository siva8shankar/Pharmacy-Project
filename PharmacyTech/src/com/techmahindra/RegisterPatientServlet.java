package com.techmahindra;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.Naming;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class RegisterPatientServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		String patientname=request.getParameter("patientname");
		String doctorname=request.getParameter("doctorname");
		String madicinename=request.getParameter("madicinename");
		String costofmadicine=request.getParameter("costofmadicine");
		String quantity=request.getParameter("quantity");
		String amount=request.getParameter("amount");
		String date=request.getParameter("date");		
		String stax=request.getParameter("stax");
		String totamt=request.getParameter("totamt");		
		String place=request.getParameter("place");
		String billnumber=request.getParameter("billnumber");
		String email=request.getParameter("email");
		double costofmadic=Double.parseDouble(costofmadicine);
		double quanty=Double.parseDouble(quantity);
		double amnt=Double.parseDouble(amount);
		double staxe=Double.parseDouble(stax);
		double totamnt=Double.parseDouble(totamt);		
		//double mname=Double.parseDouble(num);
		//String country=request.getParameter("country");
		System.out.println(email);
		int status=RegisterUser.registerPatient(patientname, doctorname, madicinename,costofmadic,quanty,amnt,date,staxe,totamnt,place,billnumber,email);	    
		System.out.println("status "+status);
	    
		if(status>0){
				
			request.setAttribute("welcome","WELCOME! YOU HAVE BEEN REGISTERD");
			RequestDispatcher rd=request.getRequestDispatcher("patient.jsp");
			rd.include(request, response);
		}
		else{
			out.print("Sorry,Registration failed. please try later");
			RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
			rd.include(request, response);
		}
		
	out.close();	
	}

}
