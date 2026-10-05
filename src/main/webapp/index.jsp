<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>KohaPlugIns Home</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/styles.css">
    
<link rel="stylesheet" href="css/consolidated-styles.css">
</head>
<body>
    <%@ include file="/common/header.jsp" %>
    <%@ include file="/common/searchbox.jsp"%>
    
    <!-- Main Layout: Sidebar and Content -->
	<div class="main-layout">
		<!-- Sidebar Section -->
		<%@ include file="/common/sidebar.jsp"%>

		<!-- Main Content Section -->
		<main class="content">
			<section class="forms-section">
    <div >
        <h2>Welcome to Koha PlugIns</h2>
        <div class="button-grid">
            <a class="btn" href="${pageContext.request.contextPath}/DefaulterListServlet" target="_blank" rel="noopener noreferrer">
                Block Semester Registration
            </a>
            <a class="btn" href="<%=request.getContextPath()%>/ListBlockedDefaulterServlet" target="_blank" rel="noopener noreferrer">
                Unblock Semester Registration
            </a>
            <a class="btn" href="<%=request.getContextPath()%>/kohaPluginLogin.jsp?route=patron" target="_blank" rel="noopener noreferrer">
                Student Add/Update
            </a>
            <a class="btn" href="<%=request.getContextPath()%>/kohaPluginLogin.jsp?route=qrcheckout" target="_blank" rel="noopener noreferrer">
                QR Code Check-Out
            </a>
            <a class="btn" href="<%=request.getContextPath()%>/kohaPluginLogin.jsp?route=bbkrent" target="_blank" rel="noopener noreferrer">
                Book Bank Rent
            </a>
            <a class="btn" href="<%=request.getContextPath()%>/kohaPluginLogin.jsp?route=pr" target="_blank" rel="noopener noreferrer">
                Price Recovery
            </a>
<%--             <a href="<%=request.getContextPath()%>/GenNextSBarcodeServlet"> --%>
            <a class="btn" href="${pageContext.request.contextPath}/GenNextSBarcodeServlet" target="_blank" rel="noopener noreferrer">
                Generate Next Serial BarCode
            </a>
            <a class="btn" href="https://eakl.neduet.edu.pk:8001" target="_blank" rel="noopener noreferrer">
               -> EXIT to Koha
            </a>
        </div>
    </div>
    </section>
    <section class="message-section">	</section>
			
    </main>
    </div>
    	<%@ include file="/common/footer.jsp"%>
</body>
</html>