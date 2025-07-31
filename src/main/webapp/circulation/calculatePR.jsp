<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>EAKL - Price Recovery PlugIn</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/styles.css">
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
         .form-section { margin: 2em auto; max-width: 600px; } 
/* .form-section { display: none; margin-top: 20px; } */
.radio-group { margin: 20px 0; }
        .form-title { color: #428bca; font-size: 1.5em; margin-bottom: 1em; }
        .label { font-weight: bold; }
        .error { color: red; font-weight: bold; }
        .result { margin-top: 2em; background: #f3f3f3;  border-radius: 5px; }
        .book-info { margin-top: 1em; background: white; }
    </style>
    <script>
    function toggleMarketPriceRequired() {
        const mpInput = document.getElementById('marketPrice');
        const marketRadio = document.getElementById('market');
        mpInput.required = marketRadio.checked;
        mpInput.disabled = !marketRadio.checked;
        if (!marketRadio.checked) mpInput.value = '';
    }
    function clearForm() {
    	document.getElementById('resultSection').style.display = 'none';
        document.getElementById('errorSection').style.display = 'none';
    	document.getElementById('prForm').reset();
        document.getElementById('marketPrice').disabled = false;
        document.getElementById('resultSection').style.display = 'none';
        document.getElementById('errorSection').style.display = 'none';
    }
    window.onload = function() {
        toggleMarketPriceRequired();
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
    
    <main class="content">
			<section class="new-arrivals">
				<div class="container">
    
    <h3 class="style3">Calculate Price Recovery Based On</h3>
    <div class="radio-group">
<!--         <div class="form-title">Calculate Price Recovery for</div> -->
        <form id="prForm" method="POST" action="/KohaPlugins/CalculatePRServlet" autocomplete="off">
            <div ><b>
<!--                 <label class="label">Recovery Option:</label><br> -->
                <input type="radio" id="market" name="recoveryOption" value="market" onclick="toggleMarketPriceRequired()" checked>
                <label for="market">Market Price</label>
                &nbsp;&nbsp;
                <input type="radio" id="oop" name="recoveryOption" value="outofprint" onclick="toggleMarketPriceRequired()">
                <label for="oop">Out of Print</label>
            </b></div>
            <br>
            <div>
                <label for="barcode" >Barcode of Book:</label>
                <input type="text" id="barcode" name="barcode" required>
            </div>
            <br>
            <div>
                <label for="marketPrice" >Market Price:</label>
                <input type="number" id="marketPrice" name="marketPrice" min="0" step="0.01">
            </div>
            <br>
            <div>
                <button type="submit">Calculate</button>
                <button type="button" onclick="clearForm()">Clear</button>
            </div>
        </form>
        <div id="errorSection" class="error" style="display: <%= request.getAttribute("error") != null ? "block" : "none" %>;">
            <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
        </div>
        <div id="resultSection" class="result" style="display: <%= request.getAttribute("recoveryAmount") != null ? "block" : "none" %>;">
            <div><strong><br>Price Recovery Amount:</strong> <%= "Rs. " + request.getAttribute("recoveryAmount")+".0" %></div>
            <div class="book-info">
                <u>Book Information:</u><br>
                Accession No: <%= request.getAttribute("acc_no") != null ? request.getAttribute("acc_no") : "" %><br>
                Title: <%= request.getAttribute("title") != null ? request.getAttribute("title") : "" %><br>
                Author: <%= request.getAttribute("author") != null ? request.getAttribute("author") : "" %><br>
                Edition: <%= request.getAttribute("edition_statement") != null ? request.getAttribute("edition_statement") : "" %><br>
                Acquisition Year: <%= request.getAttribute("acq_year") != null ? request.getAttribute("acq_year") : "" %><br>
                Price: Rs. <%= request.getAttribute("price") != null ? request.getAttribute("price") : "" %>
            </div>
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