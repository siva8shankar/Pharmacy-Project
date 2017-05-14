<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Distributer Login</title>
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
				document.F1.username.focus()
				return false
			}
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

	<div class="form-content">

		<form name=F1 onSubmit="return dil(this)" action="distributer.jsp">
			<table cellspacing="10" cellpadding="8">
				<%
					if (request.getAttribute("distributer") != null) {
						out.print("<div>");
						out.print("<font color='blue'><font size='4'>"
								+ request.getAttribute("distributer"));

						out.print("</div>");
					}
				%>
				<tr>
					<td colspan="2" class="form-heading">DISTRIBUTER LOGIN</td>
				</tr>
				<tr>
					<td>LOGIN NAME:</td>
					<td><input type="text" name="username" /></td>
				</tr>

				<tr>
					<td>PASSWORD:</td>
					<td><input type="password" name="password" /></td>
				</tr>

				<tr>
					<td></td>
					<td><input type="submit" value="Submit" /> <INPUT TYPE=RESET
						VALUE="CLEAR"></td>
				</tr>

			</table>
		</form>
	</div>

	<%@ include file="footer.html"%>
</body>
</html>