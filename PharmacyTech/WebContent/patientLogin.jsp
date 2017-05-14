<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Patient Login</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	function ctck() {
		var sds = document.getElementById("dum");
		if (sds == null) {
			alert("You are using a free package.\n You are not allowed to remove the tag.\n");
		}
	}
</script>

<SCRIPT LANGUAGE="JavaScript">
	function dil(form) {
		for (var i = 0; i < form.elements.length; i++) {
			if (form.elements[i].value == "") {
				alert("Fill out all Fields")
				document.F1.accountno.focus()
				return false
			}
		}

		if (isNaN(document.F1.accountno.value)) {
			alert("Accountno must  be  number & can't be null")
			document.F1.accountno.value = ""
			document.F1.accountno.focus()
			return false
		}
		if (!isNaN(document.F1.username.value)) {
			alert("User Name  must  be  char's & can't be null")
			document.F1.username.value = ""
			document.F1.username.focus()
			return false
		}

		if (!isNaN(document.F1.password.value)) {
			alert("Password  must  be  char's & can't be null")
			document.F1.password.value = ""
			document.F1.password.focus()
			return false
		}

		return true
	}
</SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Global Banking ..</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	function ctck() {
		var sds = document.getElementById("dum");

	}
</script>
<body>
	<%@ include file="header.html"%>
	<div class="form-content patient-login-success">
				<table width="300" border="0" cellspacing="10" cellpadding="0"
					align="center">
					<%
						String patientname = request.getParameter("patientname");
						String email = request.getParameter("email");
						boolean status = verifyLogin1.checkPatientLogin(patientname, email);

						try {
							if (status == true) {
								out.print("Welcome    " + patientname);								

								out.println("<br><a href='patientdetails.jsp?email="+email+"'>Click to Know your details</a> ");

								Connection con = GetCon.getCon();
								PreparedStatement ps = con.prepareStatement("");

							} else {
								out.print("Please check your username and Password");
								request.setAttribute("check",
										"Please check your username and Password");
					%>
					<jsp:forward page="patient.jsp"></jsp:forward>
					<%
						}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					%>
				</table>
				</div>
				 <%@ page import="java.sql.*"%> <%@ page
					import="java.io.*"%> <%@ page
					import="javax.servlet.*"%> <%@ page
					import="com.techmahindra.*"%>

	<%@ include file="footer.html"%>
</body>
</html>