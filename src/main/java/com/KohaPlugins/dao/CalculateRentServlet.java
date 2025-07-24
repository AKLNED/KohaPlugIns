package com.KohaPlugins.dao;
import com.KohaPlugins.service.KohaBiblioFunctionService;
import com.KohaPlugins.service.KohaBiblioService;
//import com.KohaPlugins.service.OracleMemberService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

import org.json.JSONObject;

@WebServlet("/CalculateRentServlet")

public class CalculateRentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//private final KohaBiblioFunctionService kohaFunction = new KohaBiblioFunctionService();
    private final KohaBiblioService kohaBiblio = new KohaBiblioService();
    
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
		String action = req.getParameter("action");
			
		String barcode = req.getParameter("barcode");
		String cPrice = req.getParameter("bookCost");
		
		String error = null; 
		
		int rent = 0;
		JSONObject kohaItem = null;
		JSONObject kohaBib = null;
		
		int biblio_id=0;
        String acquisition_date="";
        int purchase_price=0;
        //BigDecimal purchase_price = BigDecimal.ZERO;
        String callnumber="";
        String coded_location_qualifier="";
        String title="";
        String author="";
        String edition_statement="";
				
		if(action.equals("new")) {		
		
		//String cPrice = req.getParameter("bookCost");
        
        
        if (cPrice != null && !cPrice.trim().isEmpty()) {
            try {
                rent = KohaBiblioFunctionService.calRentBbk(new BigDecimal(cPrice.trim()));
            } catch (Exception ex) {
                error = ex.getMessage();
            }
        } else {
            error = "Book cost is required.";
        }
        req.setAttribute("rentResult", error != null ? "Error: " + error : "Calculated rent: <strong>" + rent + "</strong>");
        req.setAttribute("formType", "new");
        req.getRequestDispatcher("/biblio/calculateRent.jsp").forward(req, resp);
    }
		
		
        
		if(action.equals("existing")) {		
			
			//String barcode = req.getParameter("barcode");
            //String error = null;
			
	        
         // Get rent from MySQL function
            if (barcode != null && !barcode.trim().isEmpty()) {
                try {
                	 rent = KohaBiblioFunctionService.policyRentBbk(barcode.trim());
                } catch (Exception ex) {
                    error = ex.getMessage();
                }
            } else {
                error = "Book Barcode is required.";
            }
            req.setAttribute("rentResult", error != null ? "Error: " + error : "<strong>Calculated rent: " + rent + "</strong></br>");
            
            
            //Get Book Item details from Koha API
			kohaItem = kohaBiblio.getKohaBiblioItemByBarcode(barcode);
			
			if (kohaItem != null) {
                
                biblio_id = kohaItem.optInt("biblio_id");
                acquisition_date = kohaItem.optString("acquisition_date");
                purchase_price = kohaItem.optInt("purchase_price");
                //purchase_price = BigDecimal.valueOf(kohaItem.getDouble("purchase_price"));
                callnumber = kohaItem.optString("callnumber");
                coded_location_qualifier = kohaItem.optString("coded_location_qualifier");
                
                if (coded_location_qualifier.equals("1")) {coded_location_qualifier = "Purchased";}
                else if (coded_location_qualifier.equals("2")) {coded_location_qualifier = "Complimentary";}
                else if (coded_location_qualifier.equals("3")) {coded_location_qualifier = "Donation";}
                else if (coded_location_qualifier.equals("4")) {coded_location_qualifier = "Replacement";}
                else if (coded_location_qualifier.equals("5")) {coded_location_qualifier = "C/D+COST";}
                
                kohaBib = kohaBiblio.getKohaBiblioByBiblioID(biblio_id);
                
                if (kohaBib != null) {
                
                	title = kohaBib.optString("title");
                	author = kohaBib.optString("author");
                	edition_statement = kohaBib.optString("edition_statement");
                
                }
  	
            } else {
            	error = "Incorrect Bar Code! ";
            	}
			String bookDetails = "</br><strong><u>Title Details: </u></strong></br> " + "</br>Accession No: " + barcode + "</br>TItle: " + title + "</br>Author Name: " + author + "</br>Edition: " + edition_statement + "</br>Call No: "+ callnumber + "</br>Purchase Type: "+ coded_location_qualifier + "</br>Purchase Date: "+ acquisition_date + "</br>Purchase Price: " + purchase_price;          
	        //req.setAttribute("biblio_id", bookDetails);
	        req.setAttribute("biblio_id", error != null ? "Error: " + error : bookDetails );
	        
	        req.setAttribute("formType", "existing");
	        req.getRequestDispatcher("/biblio/calculateRent.jsp").forward(req, resp);    
            }

		
        
}
}