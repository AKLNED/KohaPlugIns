<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>EAKL - QR Check-In PlugIn</title>
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
.error { color: red; font-weight: 600; margin-bottom: 1em; }
}
</style>

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

<% if (request.getAttribute("patronId") != null) { %>
window.onload = function() {
    resetAndFocusForm();
    window.open('http://www.seakl.neduet.edu.pk:8001/cgi-bin/koha/circ/circulation.pl?borrowernumber=<%=request.getAttribute("patronId")%>', '_blank');
};
<% } %>

</script>

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
		
			<% if (request.getAttribute("errorMsg") != null) { %>
                    <div class="error"><%= request.getAttribute("errorMsg") %></div>
                    
			<% } %>
<!-- 			<form method="POST" action="" id="qrForm"> -->
<form method="POST" action="/KohaPlugins/QRCheckoutServlet" id="qrForm">
				<label for="qrCode">QR Code URL:</label> <input
					type="text" id="qrCode" name="qrCode" required
					placeholder="Scan QR code from University ID Card"> <br><br>
					
				<input type="submit" value=" Check Out ">
			
			</form>
		</div>
		</section>
		<section class="announcements"></section>
	</main>
	</div>
	<%@ include file="/common/footer.jsp"%>
</body>
</html>