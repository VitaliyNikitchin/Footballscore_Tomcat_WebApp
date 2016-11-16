package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dbConnection.DBConnector;

/**
 * Servlet implementation class Authentication
 */
@WebServlet("/AuthenticationServlet")
public class Authentication extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		
		DBConnector dbConnector = new DBConnector();
		int userId = dbConnector.authenticate(login, password);
		//System.out.println(userId);
		
		if (userId == 0) {
			request.setAttribute("authenticationError", "true");
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("authenticationForm.jsp");
			requestDispatcher.forward(request, response);							
		}
		else {			
			HttpSession session = request.getSession();
			session.setAttribute("userId", Integer.toString(userId));
			response.sendRedirect("personal_account.html");
			
			System.out.println("sessionId: " + session.getId());
		}
		dbConnector.closeConnection();
	}

}
