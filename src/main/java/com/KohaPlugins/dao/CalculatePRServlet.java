package com.KohaPlugins.dao;

import com.KohaPlugins.service.KohaBiblioService;
import com.KohaPlugins.service.KohaBiblioFunctionService;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/CalculatePRServlet")
public class CalculatePRServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//Session expiry check 
		
		  HttpSession session = request.getSession(false); if (session == null ||
		  session.getAttribute("kohaUserid") == null) {
		  response.sendRedirect(request.getContextPath() +
		  "/kohaPluginLogin.jsp?route=pr"); return; }
		 

		String recoveryOption = request.getParameter("recoveryOption");
		String barcode = request.getParameter("barcode");
		String marketPriceStr = request.getParameter("marketPrice");
		String error = null;
		int biblio_id = 0;
		int recoveryAmount = 0;
		BigDecimal price = BigDecimal.ZERO;
		String acqYearStr = null;
		String acqYear = null;

		String title = "", author = "", edition = "";

		KohaBiblioService biblioService = new KohaBiblioService();

		if (barcode == null || barcode.trim().isEmpty()) {
			error = "Barcode is required.";
		} else {
			// KohaBiblioService biblioService = new KohaBiblioService();
			JSONObject itemObj = biblioService.getKohaBiblioItemByBarcode(barcode);
			if (itemObj == null || !itemObj.has("biblio_id")) {
				error = "Book not found for provided barcode.";
			} else {
				biblio_id = itemObj.optInt("biblio_id", 0);
				price = BigDecimal.valueOf(itemObj.optDouble("purchase_price", 0.0));
				acqYearStr = itemObj.optString("replacement_price_date", "");
				// String acqYear = "";
				if (acqYearStr != null && acqYearStr.length() >= 4) {
					acqYear = acqYearStr.substring(0, 4);
				}

				JSONObject biblioObj = biblioService.getKohaBiblioByBiblioID(biblio_id);
				if (biblioObj != null) {
					title = biblioObj.optString("title", "");
					author = biblioObj.optString("author", "");
					edition = biblioObj.optString("edition_statement", "");
				}

				if ("market".equals(recoveryOption)) {
					if (marketPriceStr == null || marketPriceStr.trim().isEmpty()) {
						error = "Market price is required for Market Price option.";
					} else {
						try {
							// double marketPrice = Double.parseDouble(marketPriceStr);
							int marketPrice = (int) Double.parseDouble(marketPriceStr); // truncates the decimal

							recoveryAmount = marketPrice * 2;
						} catch (NumberFormatException ex) {
							error = "Invalid market price entered.";
						}
					}
				} else if ("outofprint".equals(recoveryOption)) {
					try {
						recoveryAmount = KohaBiblioFunctionService.calRecoveryPrice(barcode, acqYear, price);

					} catch (Exception ex) {
						error = "Unable to process Out of Print option: " + ex.getMessage();
					}
				} else {
					error = "Invalid recovery option selected.";
				}

			}
		}
		if (error != null) {
			request.setAttribute("error", error);
		} else {
			request.setAttribute("recoveryAmount", recoveryAmount);
			request.setAttribute("acc_no", barcode);
			request.setAttribute("title", title);
			request.setAttribute("author", author);
			request.setAttribute("edition_statement", edition);
			request.setAttribute("acq_year", acqYearStr);
			request.setAttribute("price", price);

		}
		request.getRequestDispatcher("/circulation/calculatePR.jsp").forward(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/circulation/calculatePR.jsp").forward(request, response);
	}
}