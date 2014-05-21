package controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.*;

import helper.*;
import action.*;

@WebServlet("/search")
@SuppressWarnings("serial")
public class ProcessaQuery extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	String prossimaPagina = "/mostraRisultati.jsp";		
		Helper help = new SearchHelper();
		if (help.convalida(request.getParameter("query"))){
			Action azione = new SearchAction();
			if (azione.esegui(request).equals("OK")){
				int numPagina = 0;
				if (request.getParameter("page") != null)
					numPagina = Integer.parseInt(request.getParameter("page"));
				request.getSession().setAttribute("paginaCorrente", numPagina);
			}
		} else {
			request.getSession().setAttribute("risultato", null);
		}
		ServletContext application  = getServletContext();
		RequestDispatcher rd = application.getRequestDispatcher(prossimaPagina);
		rd.forward(request, response);
	}
}
