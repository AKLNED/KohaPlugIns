package com.KohaPlugins.dao;

import com.KohaPlugins.service.KohaBiblioFunctionService;
import com.KohaPlugins.service.KohaBiblioService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
//import java.math.BigDecimal;

import org.json.JSONObject;

@WebServlet("/GenNextSBarcodeServlet")

public class GenNextSBarcodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// private final KohaBiblioFunctionService kohaFunction = new
	// KohaBiblioFunctionService();
	private final KohaBiblioService kohaBiblio = new KohaBiblioService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Session expiry check 
		
		  HttpSession session = req.getSession(false); if (session == null ||
		  session.getAttribute("kohaUserid") == null) {
		  resp.sendRedirect(req.getContextPath() +
		  "/kohaPluginLogin.jsp?route=nextser"); return; }
		 

		int lBarcode = 0;
		int nBarcode = 0;
		String error = null;
		
		String barcode = null;
	
		JSONObject kohaItem = null;
		JSONObject kohaBib = null;

		
		try {
			lBarcode = KohaBiblioFunctionService.lastSerialBarcode();
		} catch (Exception ex) {
			error = ex.getMessage();
		}		
		
		
	
		int biblio_id = 0;
		String acquisition_date = "";
		String title = "";
		String serial_issue_number = "";

		
		if (lBarcode != 0 ) {
			barcode =  "S"+ lBarcode;
			
			// getting item information using barcode
			// and title information using biblio_id
			
			kohaItem = kohaBiblio.getKohaBiblioItemByBarcode(barcode);

			if (kohaItem != null) {

				acquisition_date = kohaItem.optString("acquisition_date");
				serial_issue_number = kohaItem.optString("serial_issue_number");
			
				biblio_id = kohaItem.optInt("biblio_id");
				kohaBib = kohaBiblio.getKohaBiblioByBiblioID(biblio_id);

				if (kohaBib != null) {

					title = kohaBib.optString("title");
						
							}

					 else {
							error = "Cannot Find Bar Code! ";
						}
				
				nBarcode = lBarcode +1;
		}
		}
			
			
			if (error != null) {
				req.setAttribute("error", error);
			} else {
				req.setAttribute("lBarcode", barcode);
				req.setAttribute("nBarcode", "S"+nBarcode);
				req.setAttribute("title", title);
				req.setAttribute("acquisition_date", acquisition_date);
				req.setAttribute("serial_issue_number", serial_issue_number);
				
			}
			 req.getRequestDispatcher("/biblio/genNextSBarcode.jsp").forward(req, resp);

		
		
	}

}