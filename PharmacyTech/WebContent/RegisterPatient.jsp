<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>New Patient Registration </title>
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
				document.F1.patientname.focus()
				return false
			}
		}
		if (!isNaN(document.F1.patientname.value)) {
			alert("Patient Name  must  be  char's & can't be null")
			document.F1.patientname.value = ""
			document.F1.patientname.focus()
			return false
		}	

		if (!isNaN(document.F1.doctorname.value)) {
			alert("Doctor NAME  must  be  char's & can't be null")
			document.F1.doctorname.value = ""
			document.F1.doctorname.focus()
			return false
		}

		if (isNaN(document.F1.quantity.value)) {
			alert("Quantity field  must  be  numbers")
			document.F1.quantity.value = ""
			document.F1.quantity.focus()
			return false
		}
		if (isNaN(document.F1.amount.value)) {
			alert("Amount field  must  be  numbers")
			document.F1.amount.value = ""
			document.F1.amount.focus()
			return false
		}
		if (isNaN(document.F1.stax.value)) {
			alert("Service Tax field  must  be  numbers")
			document.F1.stax.value = ""
			document.F1.stax.focus()
			return false
		}
		if (isNaN(document.F1.totamt.value)) {
			alert("Total Amount field  must  be  numbers")
			document.F1.totamt.value = ""
			document.F1.totamt.focus()
			return false
		}

		return true
	}
	function billNum() {
		//alert(document.F1.patientname.value)
		//alert(document.F1.date.value)
		//alert(new Date().getDate())
		
		var patientname = document.F1.patientname.value;
		var doctorname = document.F1.doctorname.value;
		var date = document.F1.date.value;
		var place = document.F1.place.value;
		var datedd = new Date().getDate();
		var bill_num = patientname.substr(0,4)+""+doctorname.substr(0,3)+""+place.substr(0,3)+""+datedd;		
		document.F1.billnumber.value = bill_num
	}
</SCRIPT>
</head>
<body>
	<%@ include file="header.html"%>

	<div class="form-content">

		<form name=F1 onSubmit="return dil(this)" action="RegisterPatientServlet">
			<table cellspacing="5" cellpadding="3">
				<tr>
					<td colspan="2" class="form-heading">NEW PATIENT REGISTRATION</td>
				</tr>
				<tr>
					<td>PATIENT NAME:</td>
					<td><input type="text" name="patientname" onkeypress="return billNum(this)" /></td>
				</tr>
				<tr>
					<td>DOCTOR NAME:</td>
					<td><input type="text" name="doctorname" onkeypress="return billNum(this)" /></td>
				</tr>
				<tr>
					<td>MADICINE NAME:</td>
					<td><input type="text" name="madicinename" /></td>
				</tr>

				<!-- Gender:<br/><br/>
					Male<input type="radio" name="gender" value="male"> Female<input type="radio" name="gender" value="female"/><br/><br/> -->
				<tr>
					<td>COST OF MADICINE:</td>
					<td><input type="text" name="costofmadicine" /></td>
				</tr>
				<tr>
					<td>QUANTITY:</td>
					<td><input type="text" name="quantity" /></td>
				</tr>
				<tr>
					<td>AMOUNT:</td>
					<td><input type="text" name="amount" /></td>
				</tr>

				<TR>
					<TD>DATE:</TD>
					<TD><input type="date" name="date" /></TD>

				</TR>
				<TR>
					<TD>SERVICE TAX :</TD>
					<TD><input type="text" name="stax" /></TD>


				</TR>
				<tr>
					<td>TOTAL AMOUNT:</td>
					<td><input type="text" name="totamt" /></td>
				</tr>
				<tr>
					<td>PLACE:</td>
					<td><input type="text" name="place" onkeypress="return billNum(this)" /></td>
				</tr>
				<tr>
					<td>BILL NUMBER:</td>
					<td><input type="text" name="billnumber" onfocus="return billNum(this)" /></td>
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