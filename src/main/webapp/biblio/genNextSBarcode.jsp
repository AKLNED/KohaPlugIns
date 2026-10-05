<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
//Session expiry check
if (session == null || session.getAttribute("kohaUserid") == null) {
	response.sendRedirect(request.getContextPath() + "/kohaPluginLogin.jsp?route=nextser");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">

<title>EAKL - Serial Barcode PlugIn</title>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/consolidated-styles.css">

</head>

<body>

	<%@ include file="/common/header.jsp"%>

	<%@ include file="/common/searchbox.jsp"%>

	<!-- Main Layout: Sidebar and Content -->
	<div class="main-layout">
		<!-- Sidebar Section -->
		<%@ include file="/common/sidebar.jsp"%>
		<!-- Main Content Section -->

		<main class="content">
			<section class="forms-section">
				<div class="container">

					<h3 >Generate Next Serial Bar Code</h3>
<!-- <div> <h4> Last Serial Bar Code</h4> -->
<!-- 		<div id="errorSection" class="error c_c1e05bb6"error") != null ? "block" : "none"%>;"> -->
<%-- 						<%=request.getAttribute("error") != null ? request.getAttribute("error") : ""%> --%>
<!-- 					</div> -->
					<div id="resultSection" style="display: <%=request.getAttribute("lBarcode") != null ? "block" : "none"%>;">
						<div class="info">
							<strong><br>Last Serial Barcode:</strong>
							<%= request.getAttribute("lBarcode") %>
<!-- 							</div> -->
						
<!-- 						<div class="info"> -->
							<br><br><u>Volume Information:</u><br> <br>
							Title:
							<%=request.getAttribute("title") != null ? request.getAttribute("title") : ""%><br>
							Volume Information:
							<%=request.getAttribute("serial_issue_number") != null ? request.getAttribute("serial_issue_number") : ""%><br>
							Acquisition Date:
							<%=request.getAttribute("acquisition_date") != null ? request.getAttribute("acquisition_date") : ""%><br>
							
							
							<strong><br>Next Serial Barcode:
							<%= request.getAttribute("nBarcode")%></strong>
							
						</div>
						
					</div>
</div>
</section>
<section class="message-section">
<!-- <div> <h4> Next Serial Bar Code</h4> -->
<div class = "container">
		<div id="errorSection" class="error" style="display: <%=request.getAttribute("error") != null ? "block" : "none"%>;">
						<%=request.getAttribute("error") != null ? request.getAttribute("error") : ""%>
					</div>
					<%-- <div id="resultSection" class="result c_c1e05bb6"nBarcode") != null ? "block" : "none"%>;">
						<div>
							<strong><br>Next Serial Barcode:</strong>
							<%= request.getAttribute("nBarcode")%></div>
						</div>
 --%>
 </div>
			</section>
			
		</main>
	</div>
	<%@ include file="/common/footer.jsp"%>

</body>
</html>