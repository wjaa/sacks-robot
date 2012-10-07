package br.com.wjaa.sackdroidweb.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class MainController
 */
public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doProcess(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doProcess(request, response);
	}
	
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String keyworkd = request.getParameter("keyword");
		String valor = request.getParameter("valor");
		Integer filterType = Integer.valueOf(request.getParameter("filtertype"));
		Integer orderType = Integer.valueOf(request.getParameter("ordertype"));
		
		SacksRobot sb = new SacksRobot(keyworkd.split(","), Double.valueOf(valor), 
				FilterType.getFilter(filterType), OrderType.getOrder(orderType) );
		List<Produto> produtos = sb.letsGo();
		request.setAttribute("produtos", produtos);
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/result.jsp");
		response.setContentType("UTF-8");
		request.setCharacterEncoding("UTF-8");
		rd.forward(request, response);
		
	}

}
