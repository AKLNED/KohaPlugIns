<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
//Session expiry check 
if (session == null || session.getAttribute("kohaUserid") == null) {
	response.sendRedirect(request.getContextPath() + "/kohaPluginLogin.jsp?route=bbkrent");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">

<title>EAKL - Book Bank Rent PlugIn</title>
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
<script>
	function showForm(formNo) {
		document.getElementById('form1').classList.remove('active');
		document.getElementById('form2').classList.remove('active');
		if (formNo === 1)
			document.getElementById('form1').classList.add('active');
		else
			document.getElementById('form2').classList.add('active');
	}
	window.onload = function() {
		showForm(
<%=(request.getAttribute("formType") != null && request.getAttribute("formType").equals("existing")) ? "2" : "1"%>
	);
	};

	// Clear function for New Title form
	function clearNewForm() {
		document.querySelector('#form1 form').reset();
		document.querySelector('#form1 .result').innerHTML = "";
		document.getElementById('resultSection').style.display = 'none';
        document.getElementById('errorSection').style.display = 'none';
	}

	// Clear function for Existing Title form
	function clearExistingForm() {
		document.querySelector('#form2 form').reset();
		document.querySelector('#form2 .result').innerHTML = "";
		document.getElementById('resultSection').style.display = 'none';
        document.getElementById('errorSection').style.display = 'none';
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

					<h3 class="style3">Calculate Rent for Book Bank Titles</h3>

					<div class="radio-group">
						<label> <input type="radio" name="formRadio" value="new"
							onclick="showForm(1)"
							<%=(request.getAttribute("formType") == null || !"existing".equals(request.getAttribute("formType"))) ? "checked"
		: ""%> />
							Rent of New Title
						</label> &nbsp;&nbsp; <label> <input type="radio" name="formRadio"
							value="existing" onclick="showForm(2)"
							<%="existing".equals(request.getAttribute("formType")) ? "checked" : ""%> />
							Rent of Existing Title
						</label>
					</div>

					<!-- Form 1: Rent of New Title -->
					<div id="form1"
						class="form-section <%=(request.getAttribute("formType") == null || !"existing".equals(request.getAttribute("formType"))) ? "active"
		: ""%>">
						<h4>
							<u>Rent of New Title</u>
						</h4>
						</br>
						<form method="post"
							action="<%=request.getContextPath()%>/CalculateRentServlet">
							<input type="hidden" name="action" value="new" /> <label>Cost of Book: <input type="text" name="bookCost" required
								pattern="^\d+(\.\d{1,2})?$" title="Enter a valid number" />
							</label> <br>
							<br>
							<button type="submit">&nbsp;Calculate Rent&nbsp;</button>
							<button type="button" onclick="clearNewForm()">&nbsp;Clear&nbsp;</button>
						</form>
					</div>



					<!-- Form 2: Rent of Existing Title -->
					<div id="form2"
						class="form-section <%="existing".equals(request.getAttribute("formType")) ? "active" : ""%>">
						<h4>
							<u>Rent of Existing Title</u>
						</h4>
						</br>
						<form method="post"
							action="<%=request.getContextPath()%>/CalculateRentServlet">
							<input type="hidden" name="action" value="existing" /> <label>Barcode
								of Book: <input type="text" name="barcode" maxlength="10"
								required />
							</label> <br>
							<br>
							<button type="submit">&nbsp;Calculate Rent & Show Book
								Details&nbsp;</button>
							<button type="button" onclick="clearExistingForm()">&nbsp;Clear&nbsp;</button>
						</form>



					</div>
					<div id="errorSection" class="error"
						style="display: <%=request.getAttribute("error") != null ? "block" : "none"%>;">
						<%=request.getAttribute("error") != null ? request.getAttribute("error") : ""%>
					</div>
					<div id="resultSection" class="result"
						style="display: <%=request.getAttribute("rentResult") != null ? "block" : "none"%>;">
						<div>
							<strong><br>Calculated:</strong>
							<%="Rs. " + request.getAttribute("rentResult") + ".0"%></div>
						<% if ("existing".equals(request.getAttribute("formType"))){%>
						<div class="book-info">
							<u>Book Information:</u><br> Accession No:
							<%=request.getAttribute("acc_no") != null ? request.getAttribute("acc_no") : ""%><br>
							Title:
							<%=request.getAttribute("title") != null ? request.getAttribute("title") : ""%><br>
							Author:
							<%=request.getAttribute("author") != null ? request.getAttribute("author") : ""%><br>
							Edition:
							<%=request.getAttribute("edition_statement") != null ? request.getAttribute("edition_statement") : ""%><br>
							Stock Type:
							<%=request.getAttribute("coded_location_qualifier") != null ? request.getAttribute("coded_location_qualifier"): ""%><br>
							Acquisition Year:
							<%=request.getAttribute("acquisition_date") != null ? request.getAttribute("acquisition_date") : ""%><br>
							Price: Rs.
							<%=request.getAttribute("purchase_price") != null ? request.getAttribute("purchase_price") : ""%>
						</div>
						<%} %>
					</div>
				</div>
			</section>
			<section class="announcements"></section>
		</main>
	</div>
	<%@ include file="/common/footer.jsp"%>

</body>
</html>