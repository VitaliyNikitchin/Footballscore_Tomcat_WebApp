package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbConnection.DBConnector;

/**
 * Servlet implementation class Registration
 */
@WebServlet("/RegistrationServlet")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String login = request.getParameter("login");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String surname = request.getParameter("surname");
		String name = request.getParameter("name");
						
		DBConnector dbConnector = new DBConnector();
		boolean loginOccupancy = dbConnector.checkLoginOccupancy(login);
		if (loginOccupancy) {
			//request.getSession().setAttribute("loginName", login);
			//response.sendRedirect("registrationForm.jsp");
			
			request.setAttribute("loginName", login);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("registrationForm.jsp");
			requestDispatcher.forward(request, response);
		}
		else {
			dbConnector.addNewUser(login, surname, name, password, email);
			
		}
		dbConnector.closeConnection();	
	}

}
