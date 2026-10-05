<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">


<title>EAKL - Patron PlugIn</title>
<!-- <link rel="stylesheet" href="common/styles.css"> -->


<link rel="stylesheet" href="<%=request.getContextPath()%>/css/consolidated-styles.css">
<script type="text/javascript">

<!-- Protects sensitive JSPs and servlets with a session check (e.g., check for an attribute like kohaUserid in session).
If not present, redirect to your login page before any API call is attempted. -->
<%
if (session.getAttribute("kohaUserid") == null) {
	response.sendRedirect(request.getContextPath()+"/kohaPluginLogin.jsp");
	return;
}
%>

</script>

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
				<div >
					<h3 >Add / Update Patrons to Koha</h3>
					
					<c:choose>
                        
                        <%-- -----------------------%>
						<%-- Case 1: Found in Koha. --%>
						<%-- -------------------- --%>
						
						<c:when test="${status == 'found_same'}">
							<div class = "forms-standard">
							<p class="info">Member is already registered in the library database.</p>
							
							<div class="button-row"><button type="button"
								onclick="window.location.href='<%=request.getContextPath()%>/patron/studentAddUpdate.jsp'">&nbsp;
								Add New Member &nbsp;</button></div>
								</div>
						</c:when>
						
						<%-- ---------------------------------------------- --%>
						<%-- Case 2: Found in Koha with different attributes. --%>
						<%-- ---------------------------------------------- --%>
						
						<c:when test="${status == 'found_different'}">
							<p class="info">
								<b>Same Member with different information found in Library
									!!</b>
							</p>
							<form action="<%=request.getContextPath()%>/UpdateKohaServlet" method="POST" class="forms-standard">
								
								<div>Student ID: ${studentId}</div>
																
								<div>-> Existing Koha Borrower No: ${kohaRollNo}</div>
								
								<div>-> Updated Portal SIS Roll No: ${rollNo}</div>
								
								<div></div>
									<b>Do you want to update member information with the above
										?</b>
								<div></div>
								
								<div class="button-row"> <input type="hidden" name="studentId"
									value="${studentId}" /> <input type="hidden"
									name="patron_attributes" value="${patronAttributes}" /> <input
									type="submit" value=" Yes, Update Member " />
								<button type="button"
									onclick="window.location.href='<%=request.getContextPath()%>/patron/studentAddUpdate.jsp'">&nbsp;
									No, Return &nbsp;</button></div>
							</form>
						</c:when>

                        <%-- ----------------------------------------------------------------------------------------------------------- --%>
						<%-- Case 3: Not Found in Koha. Inform about category in case of non teaching and confirm Insertion --%>
						<%-- ----------------------------------------------------------------------------------------------------------- --%>
						
						<c:when test="${status == 'not_in_koha'}">
						
							<p>
								<b>Member is found with the following Information:</b>
							</p>
							<form action="<%=request.getContextPath()%>/InsertKohaServlet" method="POST" class="forms-standard">
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
								<div>Student ID: ${studentId}</div>
								
								<div>First name: ${firstname}</div>
								
								<div>Surname: ${surname}</div>
								
								<div>Email: ${email}</div>
								
								<div>Branch: ${library_id}</div>
								
								<div>Borrower No: ${rollNo}</div>
								
								<div>Category: ${category_id}</div>
								<!-- RED MESSAGE IF category_id == 'FAC' -->
								<c:if test="${category_id == 'FAC'}">
									<div class="info">
										&nbsp;&nbsp;&nbsp;&nbsp; This employee will be added as a <b>Faculty
											member</b>.<br> &nbsp;&nbsp;&nbsp;&nbsp; If he/she is not a
										faculty member, Please ensure that correct category is updated
										in Library Database.
									</div>
								</c:if>
								<div></div>
								<div> <b>Do you want to register this member? </b></div>
								<div></div>
								<div class="button-row"> <input type="submit" value=" Yes, Register Member " />
								<button type="button"
									onclick="window.location.href='<%=request.getContextPath()%>/patron/studentAddUpdate.jsp'">&nbsp;
									No, Return &nbsp;</button></div>
							</form>
						</c:when>

                        <%-- ----------------------------------------------------------------------------------------------------------- --%>
						<%-- Case 4: Not Found in Koha. Inform about invalid email, category in case of non teaching and confirm Insertion --%>
						<%-- ------------------------------------------------------------------------------------------------------------ --%>
						<c:when test="${status == 'not_in_koha_invalid_email'}">
						
							<p>
								<b>Member is found with the following Information:</b>
							</p>
							
							<form action="<%=request.getContextPath()%>/InsertKohaServlet" method="POST" class="forms-standard">
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
								<div>Student ID: ${studentId}</div>
								<div>First name: ${firstname}</div>
								<div>Surname: ${surname}</div>
								
								<div>Branch: ${library_id}</div>
								
								<div>Borrower No: ${rollNo}</div>
								
								<div>Category: ${category_id}</div>

								<!-- RED MESSAGE IF category_id == 'FAC' -->
								<c:if test="${category_id == 'FAC'}">
									<div class="info">
										&nbsp;&nbsp;&nbsp;&nbsp; This employee will be added as a <b>Faculty
											member</b>. <br> &nbsp;&nbsp;&nbsp;&nbsp; If he/she is not a
										faculty member, Please ensure that correct category is updated
										in Library Database.
									</div>
								</c:if>
								<div>Email: ${email}</div>
								<div class="info">
									&nbsp;&nbsp;&nbsp;&nbsp; Member either does not have an Email Account or the email account is not a valid Workplace Email !! <br>
									&nbsp;&nbsp;&nbsp;&nbsp; Member will not be able to login for Online Services using Google SignIn!
								</div>
<div></div>
								<div><b> Are you sure you want to register this member?</b> </div>
								<div></div>
								<div class="button-row"> <input type="submit" value=" Yes, Register Member " />
								<button type="button"
									onclick="window.location.href='<%=request.getContextPath()%>/patron/studentAddUpdate.jsp'">&nbsp;
									No, Return &nbsp;</button></div>
							</form>
						</c:when>

 						<%-- -----------------------------------%>
						<%-- Case 5: Not Found in Portal SIS. --%>
						<%-- -------------------------------- --%>
						
						<c:when test="${status == 'not_found'}">
						<div class = "forms-standard">
							<p class="error"> &nbsp;Member cannot be found in NED University Database. &nbsp;
							<br>&nbsp;Please re-confirm ID! &nbsp;</p>
							
							<div class="button-row"><button type="button"
								onclick="window.location.href='<%=request.getContextPath()%>/patron/studentAddUpdate.jsp'">&nbsp;
								Add Another Member &nbsp;</button></div>
								</div>
						</c:when>
					</c:choose>
				</div>
			</section>
			<section class="message-section"></section>
		</main>
	</div>

	<%@ include file="/common/footer.jsp"%>
</body>
</html>



