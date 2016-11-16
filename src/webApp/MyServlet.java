package webApp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbConnection.DBConnector;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/myServletPath")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf8");
		PrintWriter out = response.getWriter();			
		
		String req = request.getParameter("request");
		switch (req) {
			case "getAllEvents": 
			{
				DBConnector dbConnector = new DBConnector();
				String xml = dbConnector.getAllEventsToday();
				out.print(xml);
				dbConnector.closeConnection();
			}
		}
	}
	
	

}
