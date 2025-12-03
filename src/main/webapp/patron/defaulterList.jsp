<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="org.json.JSONArray, org.json.JSONObject" %>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>

<%
    JSONArray arr = (JSONArray) request.getAttribute("defaulters");
%>

<html lang="en">
<head>
<meta charset="UTF-8">
<title>EAKL - Patron PlugIn</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/consolidated-styles.css">

<% if (session.getAttribute("kohaUserid") == null) {
       response.sendRedirect(request.getContextPath()+"/kohaPluginLogin.jsp");
       return;
   }
%>
</head>

<body>

<%@ include file="/common/header.jsp" %>
<%@ include file="/common/searchbox.jsp" %>

<div class="main-layout">
	<%@ include file="/common/sidebar.jsp" %>

	<main class="content">
		<section class="forms-section">
			<h3>Defaulter List</h3>
		</section>

		<section class="message-section">
		
		<% if (request.getAttribute("message") != null) { %>
		
		<div class="info">
							<strong><br>Result:
							<%= request.getAttribute("message") %></strong></div>
						
												
						<%} %>

		<form method="post" action="<%=request.getContextPath()%>/DefaulterSubmitServlet">
			<table class="striped-table">
				<thead>
					<tr>
						<th>S No.</th>
						<th>Card Number</th>
						<th>Member Name</th>
						<th>Borrower No</th>
						<th>Category</th>
						<th>Amount Outstanding (Rs.)</th>
					</tr>
				</thead>
				<tbody>

				<%
					if (arr != null) {
						for (int i = 0; i < arr.length(); i++) {
							JSONObject o = arr.getJSONObject(i);
				%>
					<tr>
						<td><%= o.optString("serial_no") %></td>
							<td>
							<%= o.optString("cardnumber") %>
							<input type="hidden" name="cardnumber" value="<%= o.optString("cardnumber") %>"></td>
						<td><%= o.optString("firstname") %>
						<input type="hidden" name="firstname" value="<%= o.optString("firstname") %>"></td>
						<td><%= o.optString("pat_no") %>
						<input type="hidden" name="pat_no" value="<%= o.optString("pat_no") %>"></td>
						<td><%= o.optString("categorycode") %></td>
						<td><%= o.optString("amountoutstanding") %></td>
					</tr>
				<%
						}
					}
				%>

				</tbody>
			</table>

			<br>
			<div class="button-row">
			<button type="submit">Stop Registration</button>
			<button type="button" onclick="window.location.href='https://www.eakl.neduet.edu.pk:8001/cgi-bin/koha/circ/circulation-home.pl'">&nbsp;
			No, Return &nbsp;</button>
			</div>
		</form>
		</section>
	</main>
</div>

<%@ include file="/common/footer.jsp" %>

</body>
</html>
