<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>Library Catalog - Koha Inspired</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/common/styles.css">
<style type="text/css">
<!--
.style1 {
	color: #800000
}

.style2 {
	color: #428bca
}

.style3 {
	color: #80000;
}

-->
.message {
	color: blue;
	font-weight: demibold;
	margin-bottom: 1em;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
				<div class="style2">
					<h3 class="style1">Add / Update Patrons to Koha</h3>
					<br>
					<!-- Protects sensitive JSPs and servlets with a session check (e.g., check for an attribute like kohaUserid in session).
					If not present, redirect to your login page before any API call is attempted. -->
					<%
					if (session.getAttribute("kohaUserid") == null) {
						response.sendRedirect(request.getContextPath()+"/kohaPluginLogin.jsp");
						return;
					}
					%>

					<c:choose>
						<c:when test="${status == 'found_same'}">
							<p>Member is already registered in the library database.</p>
							<br>
							<button type="button"
								onclick="window.location.href='<%=request.getContextPath()%>/patron/studentAddUpdate.jsp'">&nbsp;
								Add New Member &nbsp;</button>
						</c:when>

						<c:when test="${status == 'found_different'}">
							<p>
								<b>Same Member with different information found in Library
									!!</b>
							</p>
							<form action="<%=request.getContextPath()%>/UpdateKohaServlet" method="POST">
								<br>
								<p>Student ID: ${studentId}</p>
								<br>
								<p>-> Existing Koha Borrower No: ${kohaRollNo}</p>
								<br>
								<p>-> Updated Portal SIS Roll No: ${rollNo}</p>
								<br>
								<p>
									<b>Do you want to update member information with the above
										?</b>
								</p>
								<br> <input type="hidden" name="studentId"
									value="${studentId}" /> <input type="hidden"
									name="patron_attributes" value="${patronAttributes}" /> <input
									type="submit" value=" Yes, Update Member " />
								<button type="button"
									onclick="window.location.href='<%=request.getContextPath()%>/patron/studentAddUpdate.jsp'">&nbsp;
									No, Return &nbsp;</button>
							</form>
						</c:when>

						<c:when test="${status == 'not_in_koha'}">
							<p>
								<b>Member is found with the following Information:</b>
							</p>
							<form action="<%=request.getContextPath()%>/InsertKohaServlet" method="POST">
								<input type="hidden" name="studentId" value="${studentId}" /> <input
									type="hidden" name="firstname" value="${firstname}" /> <input
									type="hidden" name="surname" value="${surname}" /> <input
									type="hidden" name="address" value="${address}" /> <input
									type="hidden" name="address2" value="${address2}" /> <input
									type="hidden" name="phone" value="${phone}" /> <input
									type="hidden" name="mobile" value="${mobile}" /> <input
									type="hidden" name="email" value="${email}" /> <input
									type="hidden" name="cardnumber" value="${cardnumber}" /> <input
									type="hidden" name="category" value="${category_id}" /> <input
									type="hidden" name="branch" value="${library_id}" /> <input
									type="hidden" name="dateenrolled" value="${dateenrolled}" /> <input
									type="hidden" name="userid" value="${userid}" /> <input
									type="hidden" name="password" value="${password}" /> <input
									type="hidden" name="patron_attributes"
									value="${patronAttributes}" /> <br>
								<p>Student ID: ${studentId}</p>
								<br>
								<p>First name: ${firstname}</p>
								<br>
								<p>Surname: ${surname}</p>
								<br>
								<p>Email: ${email}</p>
								<br>
								<p>Branch: ${library_id}</p>
								<br>
								<p>Borrower No: ${rollNo}</p>
								<br>
								<p>Category: ${category_id}</p>
								<!-- RED MESSAGE IF category_id == 'FAC' -->
								<c:if test="${category_id == 'FAC'}">
									<p style="color: #800000; font-weight: demibold;">
										&nbsp;&nbsp;&nbsp;&nbsp; This employee will be added as a <b>Faculty
											member</b>.<br> &nbsp;&nbsp;&nbsp;&nbsp; If he/she is not a
										faculty member, Please ensure that correct category is updated
										in Library Database.
									</p>
								</c:if>
								<br> <b>Do you want to register this member? </b><br>
								<br> <input type="submit" value=" Yes, Register Member " />
								<button type="button"
									onclick="window.location.href='<%=request.getContextPath()%>/patron/studentAddUpdate.jsp'">&nbsp;
									No, Return &nbsp;</button>
							</form>
						</c:when>

						<c:when test="${status == 'not_in_koha_invalid_email'}">

							<form action="<%=request.getContextPath()%>/InsertKohaServlet" method="POST">
								<input type="hidden" name="studentId" value="${studentId}" /> <input
									type="hidden" name="firstname" value="${firstname}" /> <input
									type="hidden" name="surname" value="${surname}" /> <input
									type="hidden" name="address" value="${address}" /> <input
									type="hidden" name="address2" value="${address2}" /> <input
									type="hidden" name="phone" value="${phone}" /> <input
									type="hidden" name="mobile" value="${mobile}" /> <input
									type="hidden" name="email" value="${email}" /> <input
									type="hidden" name="cardnumber" value="${cardnumber}" /> <input
									type="hidden" name="category" value="${category_id}" /> <input
									type="hidden" name="branch" value="${library_id}" /> <input
									type="hidden" name="dateenrolled" value="${dateenrolled}" /> <input
									type="hidden" name="userid" value="${userid}" /> <input
									type="hidden" name="password" value="${password}" /> <input
									type="hidden" name="patron_attributes"
									value="${patronAttributes}" />
								<p>Student ID: ${studentId}</p>
								<br>
								<p>First name: ${firstname}</p>
								<br>
								<p>Surname: ${surname}</p>
								<br>
								<p>Branch: ${library_id}</p>
								<br>
								<p>Borrower No: ${rollNo}</p>
								<br>
								<p>Category: ${category_id}</p>

								<!-- RED MESSAGE IF category_id == 'FAC' -->
								<c:if test="${category_id == 'FAC'}">
									<p style="color: #800000; font-weight: demibold;">
										&nbsp;&nbsp;&nbsp;&nbsp; This employee will be added as a <b>Faculty
											member</b>. <br> &nbsp;&nbsp;&nbsp;&nbsp; If he/she is not a
										faculty member, Please ensure that correct category is updated
										in Library Database.
									</p>
								</c:if>
								<br>

								<p>Email: ${email}</p>
								<p style="color: #800000; font-weight: demibold;">
									&nbsp;&nbsp;&nbsp;&nbsp; Member does not have an Email Account
									or a valid Workplace Email Account!! <br>
									&nbsp;&nbsp;&nbsp;&nbsp; Will not be able to login for online
									Services using Google SignIn!
									</style>
								</p>

								<br> Are you sure you want to register this member? <br>
								<br> <input type="submit" value=" Yes, Register Member " />
								<button type="button"
									onclick="window.location.href='<%=request.getContextPath()%>/patron/studentAddUpdate.jsp'">&nbsp;
									No, Return &nbsp;</button>
							</form>
						</c:when>


						<c:when test="${status == 'not_found'}">
							<p>Member cannot be found in NED University Database. Please
								re-confirm ID.</p>
							<br>
							<button type="button"
								onclick="window.location.href='<%=request.getContextPath()%>/patron/studentAddUpdate.jsp'">&nbsp;
								Add Another Member &nbsp;</button>
						</c:when>
					</c:choose>
				</div>
			</section>
			<section class="announcements"></section>
		</main>
	</div>

	<%@ include file="/common/footer.jsp"%>
</body>
</html>



