<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
//Session expiry check
if (session == null || session.getAttribute("kohaUserid") == null) {
	response.sendRedirect(request.getContextPath() + "/kohaPluginLogin.jsp?route=bbkrent");
	return;
}    

String rentResult = (String) request.getAttribute("rentResult");  // set by CalculateRentServlet.java
String biblio_id = (String) request.getAttribute("biblio_id");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">

<title>Calculate Rent for Book Bank Titles</title>
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
        .form-section { display: none; margin-top: 20px; }
        .active { display: block; }
        .container { margin: 0 auto; font-family: Arial, sans-serif; }
        h2 { color: #305080; }
        .result { margin-top: 10px; }
        .radio-group { margin: 20px 0; }
    </style>
<script>
        function showForm(formNo) {
            document.getElementById('form1').classList.remove('active');
            document.getElementById('form2').classList.remove('active');
            if (formNo === 1) document.getElementById('form1').classList.add('active');
            else document.getElementById('form2').classList.add('active');
        }
        window.onload = function() {
            showForm(<%= (request.getAttribute("formType") != null && request.getAttribute("formType").equals("existing")) ? "2" : "1" %>);
        };
        
     // Clear function for New Title form
        function clearNewForm() {
            document.querySelector('#form1 form').reset();
            document.querySelector('#form1 .result').innerHTML = "";
        }

        // Clear function for Existing Title form
        function clearExistingForm() {
            document.querySelector('#form2 form').reset();
            document.querySelector('#form2 .result').innerHTML = "";
        }
        
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
			<section class="new-arrivals">
				<div class="container">
<!-- 				<div class="style2"> -->
					<!--     <h1> -->
					<h3 class="style3">Calculate Rent for Book Bank Titles</h3>
					<!--     </h1> -->
					<div class="radio-group">
						<label> <input type="radio" name="formRadio" value="new"
							onclick="showForm(1)"
							<%= (request.getAttribute("formType") == null || !"existing".equals(request.getAttribute("formType"))) ? "checked" : "" %> />
							Rent of New Title
						</label> &nbsp;&nbsp; <label> <input type="radio" name="formRadio"
							value="existing" onclick="showForm(2)"
							<%= "existing".equals(request.getAttribute("formType")) ? "checked" : "" %> />
							Rent of Existing Title
						</label>
					</div>

					<!-- Form 1: Rent of New Title -->
					<div id="form1"
						class="form-section <%= (request.getAttribute("formType") == null || !"existing".equals(request.getAttribute("formType"))) ? "active" : "" %>">
						<h4><u>Rent of New Title</u></h4></br>
						<form method="post"
							action="<%=request.getContextPath()%>/CalculateRentServlet">
							<input type="hidden" name="action" value="new" /> <label>Cost
								of Book: <input type="text" name="bookCost" required
								pattern="^\d+(\.\d{1,2})?$" title="Enter a valid number" />
							</label>
							<button type="submit">&nbsp;Calculate Rent&nbsp;</button>
							<button type="button" onclick="clearNewForm()">&nbsp;Clear&nbsp;</button>
						</form>
						<div class="result">
						
							<% if (rentResult != null && (request.getAttribute("formType") == null || !"existing".equals(request.getAttribute("formType")))) 
            { out.println(rentResult); } %>
						</div>
					</div>

					<!-- Form 2: Rent of Existing Title -->
					<div id="form2"
						class="form-section <%= "existing".equals(request.getAttribute("formType")) ? "active" : "" %>">
						<h4><u>Rent of Existing Title</u></h4></br>
						<form method="post"
							action="<%=request.getContextPath()%>/CalculateRentServlet">
							<input type="hidden" name="action" value="existing" /> <label>Barcode
								of Book: <input type="text" name="barcode" maxlength="10"
								required />
							</label> 
							<button type="submit">&nbsp;Calculate Rent & Show Book Details&nbsp;
							</button>
							<button type="button" onclick="clearExistingForm()">&nbsp;Clear&nbsp;</button>
							<div class="result">
								<% if (biblio_id != null && (request.getAttribute("formType") == "existing" )) 
            { out.println(rentResult);
            	out.println(biblio_id); } %>
							</div>
						</form>
					</div>
				</div>
				</section>
			<section class="announcements"></section>
		</main>
	</div>
				<%@ include file="/common/footer.jsp"%>

</body>
</html>