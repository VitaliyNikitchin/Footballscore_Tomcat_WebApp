package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import webApp.Team;
import dbConnection.DBConnector;

/**
 * Servlet implementation class PersonalAccount
 */
@WebServlet("/personalAccountPath")
public class PersonalAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		response.setContentType("text/html;charset=utf8");
		if (request.getSession() == null || request.getSession().getAttribute("userId") == null) {
			response.sendError(401); 
			return;	
		}
	
		PrintWriter out = response.getWriter();
		DBConnector dbConnector = new DBConnector();
		
		int userId = Integer.parseInt((String) request.getSession().getAttribute("userId"));
		String param = request.getParameter("parameter");
		switch (param) {
			case "getAllTeams": {
				String xml = dbConnector.teamsToXML(dbConnector.getExistingRelationsTeamLeague());
				out.print(xml);
				break;
			}
			case "getMyTeams": {
				String xml = dbConnector.teamsToXML(dbConnector.getUserTeams(userId));
				out.print(xml);	
				break;
			}
			case "addTeamToMyTeams": {								
				try {
					Set<Team> curentTeams = new HashSet<Team>();
					JSONArray checkedTeams = new JSONArray(request.getParameter("checkedTeams"));
					for (int i = 0; i < checkedTeams.length(); i++) {
						JSONObject jsonObj = checkedTeams.getJSONObject(i);
						Team team = new Team();
						team.setLeague(jsonObj.getString("league_name"));
						team.setName(jsonObj.getString("team_name"));
						curentTeams.add(team);
					}						
					dbConnector.addToUserTeams(new ArrayList<Team>(curentTeams), userId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
			case "getTeamInfo": {
				String team_name = request.getParameter("team");
				String xml = dbConnector.getTeamPlayers(team_name);
				out.print(xml);
				break;
			}
		}
		dbConnector.closeConnection();			
	}
}
