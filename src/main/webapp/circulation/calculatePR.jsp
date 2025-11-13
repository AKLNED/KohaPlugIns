<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>EAKL - Price Recovery PlugIn</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/consolidated-styles.css">
    
       
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
<link rel="stylesheet" href="css/consolidated-styles.css">
</head>
<body>
    <%@ include file="/common/header.jsp"%>
    <%@ include file="/common/searchbox.jsp"%>
    
    	<!-- Main Layout: Sidebar and Content -->
	<div class="main-layout">
		<!-- Sidebar Section -->
		<%@ include file="/common/sidebar.jsp"%>
    
    <main class="content">
			<section class="forms-section">
				<div class="container">
    
    <h3>Calculate Price Recovery Amount</h3>
        
	
        <div class="forms-section">
        <form  id="prForm" method="POST" action="/KohaPlugins/CalculatePRServlet" autocomplete="off" class="forms-standard">
        	<h3> Based On: </h3>
        <div class="radio-group">
   
                <input type="radio" id="market" name="recoveryOption" value="market" onclick="toggleMarketPriceRequired()" checked>
                <label for="market">Book Market Price</label>
                                
                <input type="radio" id="oop" name="recoveryOption" value="outofprint" onclick="toggleMarketPriceRequired()">
                <label for="oop">Book Out of Print</label> &nbsp;
            </div>
            
            <br>          
            <div class="form-row">
                <label for="barcode" >Barcode of Book:</label>
                <input type="text" id="barcode" name="barcode" required>
            </div>
            
            <br>
            
            <div class="form-row">
                <label for="marketPrice" >Market Price:</label>
                <input type="number" id="marketPrice" name="marketPrice" >
            </div>
            
            <br>
            
            <div class="button-row">
                <button type="submit">Calculate</button>
                <button type="button" onclick="clearForm()">Clear</button>
            </div>
        </form>
        </div>
        </div>
        </section>
        
        
        <section class="message-section">
        <div class = "container">
        
        <div id="resultSection" style="display: <%= request.getAttribute("recoveryAmount") != null ? "block" : "none" %>;">
            
            <div class="info">
                
                <u>Book Information:</u><br>
                <br>
                Accession No: <%= request.getAttribute("acc_no") != null ? request.getAttribute("acc_no") : "" %><br>
                Title: <%= request.getAttribute("title") != null ? request.getAttribute("title") : "" %><br>
                Author: <%= request.getAttribute("author") != null ? request.getAttribute("author") : "" %><br>
                Edition: <%= request.getAttribute("edition_statement") != null ? request.getAttribute("edition_statement") : "" %><br>
                Acquisition Year: <%= request.getAttribute("acq_year") != null ? request.getAttribute("acq_year") : "" %><br>
                Price: Rs. <%= request.getAttribute("price") != null ? request.getAttribute("price") : "" %>
            </div>
            
            <div class="info">
            <br>
            <strong>Price Recovery Amount:</strong> <%= "Rs. " + request.getAttribute("recoveryAmount")+".0" %>
            </div>
            
         
        </div>
        
        <div id="errorSection" class="error"  style="display: <%= request.getAttribute("error") != null ? "block" : "none" %>;">
            <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
        </div>
    
    </div>
				</section>
			
		</main>
    </div>
    <%@ include file="/common/footer.jsp"%>
</body>
</html>