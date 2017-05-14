<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>New Customer Registration </title>
<link href="style.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	function ctck() {
		var sds = document.getElementById("dum");
		if (sds == null) {
			alert("You are using a free package.\n You are not allowed to remove the tag.\n");
		}
	}
</script>
<head>
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

		if (document.F1.password.value != document.F1.repassword.value) {
			alert("Check Confirm PWD");
			document.F1.repassword.value = ""
			document.F1.repassword.focus()
			return false
		}

		if (!isNaN(document.F1.disname.value)) {
			alert("DISTRIBUTER NAME  must  be  char's & can't be null")
			document.F1.disname.value = ""
			document.F1.disname.focus()
			return false
		}

		if (!isNaN(document.F1.adderess.value)) {
			alert("adderess field  must  be  char's & can't be null")
			document.F1.adderess.value = ""
			document.F1.adderess.focus()
			return false
		}
		if (!isNaN(document.F1.cityname.value)) {
			alert("cityname field  must  be  char's & can't be null")
			document.F1.cityname.value = ""
			document.F1.cityname.focus()
			return false
		}

		if (!isNaN(document.F1.statename.value)) {
			alert("statename field  must  be  char's & can't be null")
			document.F1.statename.value = ""
			document.F1.statename.focus()
			return false
		}

		if (!isNaN(document.F1.phone.value)) {
			if (document.F1.phone.value > 9999999999) {
				alert("ye kabhi nhi aayegi")
				document.F1.phone.value = ""
				document.F1.phone.focus()
				return false
			}
		} else {
			alert("This  field  must  be  number")
			document.F1.phone.value = ""
			return false
		}

		return true
	}
</SCRIPT>
</head>
<body>
	<%@ include file="header.html"%>

	<div class="form-content">

		<form name=F1 onSubmit="return dil(this)" action="CreateServlet">
			<table cellspacing="5" cellpadding="3">
				<tr>
					<td colspan="2" class="form-heading">NEW CUSTOMER REGISTRATION</td>
				</tr>
				<tr>
					<td>USER NAME:</td>
					<td><input type="text" name="username" /></td>
				</tr>
				<tr>
					<td>PASSWORD:</td>
					<td><input type="password" name="password" /></td>
				</tr>
				<tr>
					<td>RE-PASSWORD:</td>
					<td><input type="password" name="repassword" /></td>
				</tr>
				<tr>
					<td>DISTRIBUTER NAME:</td>
					<td><input type="text" name="disname" /></td>
				</tr>

				<!-- Gender:<br/><br/>
					Male<input type="radio" name="gender" value="male"> Female<input type="radio" name="gender" value="female"/><br/><br/> -->
				<tr>
					<td>ADDRESS:</td>
					<td><input type="text" name="adderess" /></td>
				</tr>
				<tr>
					<td>CITY NAME:</td>
					<td><input type="text" name="cityname" /></td>
				</tr>
				<tr>
					<td>STATE NAME:</td>
					<td><input type="text" name="statename" /></td>
				</tr>

				<TR>
					<TD>Country Name :</TD>
					<TD><SELECT NAME="country">
							<option value=a>America
							<option value=b>Bangladesh
							<option value=c1>China
							<option value=c2>Canada
							<option value=g>Germany
							<option value=h>Holland
							<option value=i>India
							<option value=a>Malasia
							<option value=p>Polland
							<option value=r>Russia
							<option value=u>UK
					</SELECT></TD>

				</TR>
				<TR>
					<TD>Region :</TD>
					<TD><SELECT NAME="region">
							<option value=a1>Africa
							<option value=a2>Astralia
							<option value=a3>Asia
							<option value=e>Europe
							<option value=i>Iropa
							<option value=n>North America
							<option value=c2>South America
					</SELECT></TD>


				</TR>
				<tr>
					<td>PHONE:</td>
					<td><input type="text" name="phone" /></td>
				</tr>
				<tr>
					<td>EMAIL:</td>
					<td><input type="text" name="email" /></td>
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