<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Customer Admin</title>
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
	<div class="form-content-table">
		<%
			if (request.getAttribute("deleted") != null) {
				out.print("<table>");
				out.print("<div width='200' align='left'>");
				out.print("<font color='blue'><font size='10'>"
						+ request.getAttribute("deleted"));

				out.print("</div>");
				out.print("<table>");
			}
		%>

		<table width="300" border="0" cellspacing="10" cellpadding="0"
			align="center" class="customer-admin">
			<%
				try {
					Connection con = GetCon.getCon();
					PreparedStatement ps = con
							.prepareStatement("Select * from newcust4");
					//ps.setString(1,uname);
					ResultSet rs = ps.executeQuery();
					//out.print("<table>");

					out.print("<td><a href='Deletecustomer.jsp'>Delete</a></td>");

					//out.print("<table align='left' width='300' border='0' cellspacing='10' cellpadding='0'>");
					out.print("<tr><th>id</th><th>username</th><th>password</th><th>adderess</th><th>phone</th><th>Email</th></tr>");
					while (rs.next()) {
						int id = rs.getInt(1);
						session.setAttribute("id", id);
						out.print("<tr>");
						out.print("<td>" + rs.getString(1) + "</td>");
						out.print("<td>" + rs.getString(2) + "</td>");
						out.print("<td>" + rs.getString(3) + "</td>");
						out.print("<td>" + rs.getString(6) + "</td>");
						out.print("<td>" + rs.getString(11) + "</td>");
						out.print("<td>" + rs.getString(12) + "</td>");
						//out.print("<td>" + rs.getString(4) + "</td>");
						//out.print("<td>" DeleteServlet.Del`"</td>");
						out.print("</tr>");

					}
					//out.print("</table>");
					//out.print("<table>");

					//out.print("</table>");

				} catch (SQLException e) {
					e.printStackTrace();
				}
			%>
		</table>
		<%
			
		%>
		<%@ page import="java.sql.*"%>
		<%@ page import="java.io.*"%>
		<%@ page import="javax.servlet.*"%>
		<%@ page import="com.techmahindra.*"%>
	</div>
	<%@ include file="footer.html"%>
</body>
</html>