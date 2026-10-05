<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>EAKL - QR Check-In PlugIn</title>
<!--  <link rel="stylesheet" -->
<%-- 	href="<%=request.getContextPath()%>/common/styles.css">  --%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/consolidated-styles.css">


<script type="text/javascript">

function resetAndFocusForm() {
    var form = document.getElementById('qrForm');
    if (form) {
        form.reset();
        var qrInput = document.getElementById('qrCode');
        if (qrInput) qrInput.focus();
    }
}
// On every page load, reset and focus (including after POST)
window.onload = resetAndFocusForm;

<%if (request.getAttribute("patronId") != null) {%>
window.onload = function() {
    resetAndFocusForm();
    //window.open('http://www.seakl.neduet.edu.pk:8001/cgi-bin/koha/circ/circulation.pl?borrowernumber=<%=request.getAttribute("patronId")%>', '_blank');
    window.open('https://eakl.neduet.edu.pk:8001/cgi-bin/koha/circ/circulation.pl?borrowernumber=<%=request.getAttribute("patronId")%>',	'_blank');
	};
<%}%>
	
</script>


</head>
<body>
	<%@ include file="/common/header.jsp"%>
	<%@ include file="/common/searchbox.jsp"%>
	
	<!-- Main Layout: Sidebar and Content -->
	<div class="main-layout">
		<!-- Sidebar Section -->
		<%@ include file="/common/sidebar.jsp"%>
		<main class="content">
			<!-- Section 1: Form -->
			<section class="forms-section">
				<h3>QR Code Check-Out</h3>
				<br>

				<!-- 			<form method="POST" action="" id="qrForm"> -->
				<form method="POST" action="/KohaPlugins/QRCheckoutServlet"
					id="qrForm" class="forms-standard">
					<div class="form-row"><label for="qrCode">QR Code URL:</label> <input type="text"
						id="qrCode" name="qrCode" required
						placeholder="Scan QR code from University ID Card"> <br></div>
					<br> 
					<div class="button-row"><input type="submit" value=" Check Out "></div>

				</form>

			</section>

			<!-- Section 2: Messages -->
			<section class="message-section">
				<%
				if (request.getAttribute("errorMsg") != null) {
				%>
				<div class="error"><%=request.getAttribute("errorMsg")%></div>
				<%
				}
				%>
				
			</section>
		</main>
	</div>
	<%@ include file="/common/footer.jsp"%>
</body>
</html>