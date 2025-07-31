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
<link rel="stylesheet" href="/KohaPlugins/common/styles.css">
<style type="text/css">
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
</style>

<style>
.form-section {
	display: none;
	margin-top: 20px;
}

.active {
	display: block;
}

.container {
	margin: 0 auto;
	font-family: Arial, sans-serif;
}

h2 {
	color: #305080;
}

.result {
	margin-top: 2em;
	background: #f3f3f3;
	border-radius: 5px;
}

.book-info {
	margin-top: 1em;
	background: white;
}

.radio-group {
	margin: 20px 0;
}

.error {
	color: red;
	font-weight: bold;
}
</style>

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
			<section class="new-arrivals">
				<div class="container">

					<h3 class="style3">Generate Next Serial Bar Code</h3>
<!-- <div> <h4> Last Serial Bar Code</h4> -->
		<div id="errorSection" class="error"
						style="display: <%=request.getAttribute("error") != null ? "block" : "none"%>;">
						<%=request.getAttribute("error") != null ? request.getAttribute("error") : ""%>
					</div>
					<div id="resultSection" class="result"
						style="display: <%=request.getAttribute("lBarcode") != null ? "block" : "none"%>;">
						<div>
							<strong><br>Last Serial Barcode:</strong>
							<%= request.getAttribute("lBarcode") %></div>
						
						<div class="book-info">
							<br><u>Volume Information:</u><br> <br>
							Title:
							<%=request.getAttribute("title") != null ? request.getAttribute("title") : ""%><br>
							Volume Information:
							<%=request.getAttribute("serial_issue_number") != null ? request.getAttribute("serial_issue_number") : ""%><br>
							Acquisition Date:
							<%=request.getAttribute("acquisition_date") != null ? request.getAttribute("acquisition_date") : ""%><br>
						</div>
						
					</div>
</div>

<!-- <div> <h4> Next Serial Bar Code</h4> -->
		<div id="errorSection" class="error"
						style="display: <%=request.getAttribute("error") != null ? "block" : "none"%>;">
						<%=request.getAttribute("error") != null ? request.getAttribute("error") : ""%>
					</div>
					<div id="resultSection" class="result"
						style="display: <%=request.getAttribute("nBarcode") != null ? "block" : "none"%>;">
						<div>
							<strong><br>Next Serial Barcode:</strong>
							<%= request.getAttribute("nBarcode")%></div>
						</div>
</div>
	

				</div>
			</section>
			<section class="announcements"></section>
		</main>
	</div>
	<%@ include file="/common/footer.jsp"%>

</body>
</html>