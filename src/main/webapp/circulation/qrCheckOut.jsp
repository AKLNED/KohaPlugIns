<%@ page import="com.KohaPlugins.service.KohaPatronService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
// Session expiry check
if (session == null || session.getAttribute("kohaUserid") == null) {
	response.sendRedirect(request.getContextPath() + "/kohaPluginLogin.jsp?route=qrcheckout");
	return;
}

String error = null;
String qrInput = request.getParameter("qrCode");
if ("POST".equalsIgnoreCase(request.getMethod()) && qrInput != null) {
	// Use similar logic to extractUserId (from studentAddUpdate.jsp), but here in Java
	String memberId = null;
	String value = qrInput.trim();
	String baseUrl = "https://pl.neduet.edu.pk/qrapp/qrapp.jsp?";
	// Direct ID
	if (value.matches("^\\d{3,7}$")) {
		memberId = value;
	} else if (value.startsWith(baseUrl)) {
		String param = value.contains("param=") ? value.split("param=")[1] : "";
		if (value.endsWith("S")) {
	// UG pattern
	String[] parts = param.split("R");
	if (parts.length > 1) {
		memberId = parts[0];
		if (memberId.length() > 7) {
			memberId = memberId.substring(memberId.length() - 7);
		}
	}
		} else if (value.endsWith("P")) {
	// PG pattern
	String[] parts = param.split("i");
	if (parts.length > 1) {
		memberId = parts[0];
		if (memberId.length() > 7) {
			memberId = memberId.substring(memberId.length() - 7);
		}
	}
		}
	}
	if (memberId == null) {
		error = "Invalid QR code or Member ID format.";
	} else {
		// Query KohaPatronService for patronID
		KohaPatronService service = new KohaPatronService();
		org.json.JSONObject patronObj = service.getKohaPatronByCardNumber(memberId);
		if (patronObj == null) {
	error = "No patron found for this Member ID.";
		} else {
	int patronId = patronObj.optInt("patron_id", -1);
	if (patronId == -1) {
		error = "Unable to retrieve patron ID.";
	} else {
		//response.sendRedirect( "http://www.seakl.neduet.edu.pk:8001/cgi-bin/koha/circ/circulation.pl?borrowernumber="	+ patronId);
		out.println("<script type='text/javascript'>");
		out.println("window.open('http://www.seakl.neduet.edu.pk:8001/cgi-bin/koha/circ/circulation.pl?borrowernumber=" + patronId + "', '_blank');");
		//out.println("document.getElementById('qrForm').reset();"); // This resets the form for next input
	    //out.println("document.getElementById('qrCode').focus();"); // Optionally refocus on the input
    
    out.println("</script>");

				return;
	}
		}
	}
}
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>QR Check-In | Koha PlugIns</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/common/styles.css">
<style>

.style1 {
	color: #800000
}

.style2 {
	color: #428bca
}

.style3 {
	color: #80000;
}

.message {
	color: blue;
	font-weight: demibold;
	margin-bottom: 1em;
}


}
</style>
</head>
<body>
	<%@ include file="/common/header.jsp"%>
	<%@ include file="/common/searchbox.jsp"%>
<%-- 	<%@ include file="/common/sidebar.jsp"%> --%>
	
	<!-- Main Layout: Sidebar and Content -->
	<div class="main-layout">
		<!-- Sidebar Section -->
		<%@ include file="/common/sidebar.jsp"%>
	<main class="content">
	<section class="new-arrivals">
				<div class="style2">
					<h3>QR Code Check-Out</h3>
					<br>
			<%
			if (error != null) {
			%>
			<div class="message"><%=error%></div>
			<%
			}
			%>
			<form method="POST" action="" id="qrForm">
				<label for="qrCode">QR Code URL:</label> <input
					type="text" id="qrCode" name="qrCode" required
					placeholder="Scan QR code from University ID Card"> <br><br>
					
				<input type="submit" value=" Check Out ">
			</form>
		</section>
		<section class="announcements"></section>
	</main>
	</div>
	<%@ include file="/common/footer.jsp"%>
</body>
</html>