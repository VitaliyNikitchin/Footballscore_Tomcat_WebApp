package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import webApp.Event;
import webApp.Team;

public class DBConnector {
	private static String username = "root";
	private static String password = "";
	private static String connectionString = "jdbc:mysql://localhost:3306/footballscore_java_tomcat";
	
	private static Connection connection;
	private static Statement command;
	

	public DBConnector() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			connection = DriverManager.getConnection(connectionString, username, password);
			command = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check existing of given loggin name in DB. If such login name is already exist returns false, else return true. 
	 * @param login
	 * @return
	 */
	public boolean checkLoginOccupancy(String login) {
		boolean occupy = false;
		ResultSet resultSet;
		 try {
			resultSet =  command.executeQuery("SELECT login FROM users");
			while(resultSet.next()){				
				if (resultSet.getString("login").equalsIgnoreCase(login)) {
					occupy = true;		
					break;
				} 
			}
			resultSet.close();		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return occupy;
	}
	
	/**
	 * add to database data of new user
	 * @param login
	 * @param surname
	 * @param name
	 * @param password
	 * @param email
	 */
	public void addNewUser(String login, String surname, String name, String password, String email) {
		try {
			command.execute("INSERT INTO users VALUES " + 
					"(NULL, '" + login
					+ "', '" + surname + "', '" + name + "', '" + password
					+ "', '" + email + "');");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public int authenticate(String login, String password) {
		ResultSet resultSet;
		int userId = 0;
		try {
			resultSet = command.executeQuery("SELECT id, login, password FROM users");
			while(resultSet.next()) {
				if (login.equals(resultSet.getString("login")) && (password.equals(resultSet.getString("password")))) {
					userId = resultSet.getInt("id");
					break;
				}
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return userId;
	}

	public Set<String> getExistingLeagues() {
		Set<String> leagues = new LinkedHashSet<String>();
		ResultSet resultSet;
		try {
			resultSet = command.executeQuery("SELECT `league_name` FROM `football_leagues`");
			while(resultSet.next()) {
				leagues.add(resultSet.getString("league_name"));
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return leagues;
	}
	
	
	public Set<String> getExistingTeams() {
		Set<String> teams = new LinkedHashSet<String>();
		ResultSet resultSet;
		try {
			resultSet = command.executeQuery("SELECT `team_name` FROM `teams`");
			while(resultSet.next()) {
				teams.add(resultSet.getString("team_name"));
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return teams;
	}
	
	public String getTeamPlayers(String team) {
		String res = "";
		Set<String> players = new LinkedHashSet<String>();
		ResultSet resultSet;
		try {
			resultSet = command.executeQuery("SELECT name AS player_name, number, teams.team_name AS team_name, positions.position AS position "
					+ "FROM `players` "
					+ "INNER JOIN teams ON players.team_id=teams.id INNER "
					+ "JOIN positions ON positions.id=players.position "
					+ "WHERE team_name='" + team + "';");
			
			res += "<Players>";
			while(resultSet.next()) {
				res += "<player>";
				res += "<player_name>" + resultSet.getString("player_name") + "</player_name>";
				res += "<number>" + resultSet.getString("number") + "</number>";
				res += "<position>" + resultSet.getString("position") + "</position>";
				res += "<team_name>" + resultSet.getString("team_name") + "</team_name>";	
				res += "</player>";	
			}
			res += "</Players>";
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	/**
	 * get all available teams stored in system
	 * @return Team
	 */
	public Set<Team> getExistingRelationsTeamLeague() {
		ResultSet resultSet;
		Set<Team> teams = new LinkedHashSet<Team>();
		try {
			resultSet = command.executeQuery("SELECT teams.team_name, football_leagues.league_name "
					+ "FROM `relation_team_league` "
					+ "INNER JOIN teams ON teams.id=relation_team_league.team_id "
					+ "INNER JOIN football_leagues ON football_leagues.id=relation_team_league.league_id");
			
			while(resultSet.next()) {
				Team team = new Team();
				team.setName(resultSet.getString("team_name"));
				team.setLeague(resultSet.getString("league_name"));
				teams.add(team);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return teams;
	}
	
	
	/**
	 * get all teams from DB table associated with their IDs in this table
	 * @return Map<String, Integer> teams
	 */
	private Map<String, Integer> getTeamsId() {
		Map<String, Integer> teams = new HashMap<String, Integer>();
		ResultSet teamResultSet;

		try {
			teamResultSet = command.executeQuery("SELECT * FROM `teams`");
			while(teamResultSet.next()) {
				teams.put(teamResultSet.getString("team_name"), new Integer(teamResultSet.getInt("id")));
			}
			teamResultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return teams;
	}
	
	
	/**
	 * get all leagues from DB table associated with their IDs in this table
	 * @return Map<String, Integer> leagues
	 */
	private Map<String, Integer> getLeaguesId() {
		Map<String, Integer> leagues = new HashMap<String, Integer>();
		ResultSet leagueResultSet;
		
		try {
			leagueResultSet = command.executeQuery("SELECT * FROM football_leagues");
			while(leagueResultSet.next()) {
				leagues.put(leagueResultSet.getString("league_name"), new Integer(leagueResultSet.getInt("id")));
			}
			leagueResultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return leagues;
	}
	

	public void addRelationsTeamLeague(Set<Team> set) {
		Map<String, Integer> leagues = this.getLeaguesId();
		Map<String, Integer> teams = this.getTeamsId();	
		try {
			for (Team newTeam: set) {
				command.execute ("INSERT INTO relation_team_league (team_id, league_id) "
						+ "VALUES (" + teams.get(newTeam.getName()) + ", " + leagues.get(newTeam.getLeague()) + ");"
				);	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addFootballLeagues(Set<String> set) {		
		try {
			for (String element: set) {
				command.execute ("INSERT INTO `football_leagues` (`league_name`) VALUES " + 
						"('" + element + "');"
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	public void addFootballTeams(Set<String> set) {		
		try {
			for (String element: set) {
				command.execute ("INSERT INTO `teams` (`team_name`) VALUES " + 
						"('" + element + "');"
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * get all available teams for user personal account from system
	 * @return string <xml> format
	 */
	public String teamsToXML(Set<Team> existingTeams) {
		String res = "";
		Set<String> leagues = new LinkedHashSet<String>();
		Set<String> teams = new HashSet<String>();
		Map<String, Set<String>> hashMap = new HashMap<String, Set<String>>();
		
		for (Team t: existingTeams) {
			String league_name = t.getLeague();
			String team_name = t.getName();
					
			if (leagues.isEmpty()) {
				leagues.add(league_name);
				teams.add(team_name);
					
			}
			else {
				if (!leagues.contains(league_name)) {
					hashMap.put((String) leagues.toArray()[leagues.size() - 1], new HashSet<String>(teams));	
					teams.clear();
					leagues.add(league_name);
				}
				teams.add(team_name);	
			}
		}
		if (!existingTeams.isEmpty()) {
			hashMap.put((String) leagues.toArray()[leagues.size() - 1], new HashSet<String>(teams));	
			res += "<Data>";
			for (Entry<String, Set<String>> entry: hashMap.entrySet()) {
				res += "<league>";
				res += "<league_name>" + entry.getKey() + "</league_name>";
				res += "<team>";
				for (String team : entry.getValue()) {
					res += "<team_name>" + team + "</team_name>";
				}
				res += "</team>";
				res += "</league>";
			}
			res += "</Data>";	
		}
		return res;
	}
	

	/**
	 * select user's teams from BD
	 * @return set of user's teams
	 */
	public Set<Team> getUserTeams(int userId) {
		Set<Team> teams = new LinkedHashSet<Team>();
		ResultSet resultSet;
		try {
			resultSet = command.executeQuery("SELECT relation_user_team.user, teams.team_name, football_leagues.league_name "
					+ "FROM `relation_user_team` "
					+ "INNER JOIN relation_team_league ON relation_team_league.id=relation_user_team.team "
					+ "INNER JOIN teams ON teams.id=relation_team_league.team_id "
					+ "INNER JOIN football_leagues ON football_leagues.id=relation_team_league.league_id "
					+ "WHERE relation_user_team.user=" + userId);
			
			while(resultSet.next()) {
				Team team = new Team();
				team.setName(resultSet.getString("team_name"));
				team.setLeague(resultSet.getString("league_name"));
				teams.add(team);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return teams;
	}

	public void addToUserTeams(ArrayList<Team> myTeams, int useId) {
		ResultSet resultSet;
		String sqlCondition = "WHERE ";
		for (int i = 0; i < myTeams.size(); i++) {
			sqlCondition += "(teams.team_name='" + myTeams.get(i).getName() + "' AND ";
			sqlCondition += "football_leagues.league_name='" + myTeams.get(i).getLeague() + "')";
			if (i != myTeams.size() - 1) {
				sqlCondition += " OR ";
			}
		}

		try {
			ArrayList<Integer> team_id = new ArrayList<Integer>();
			resultSet = command.executeQuery("SELECT relation_team_league.id "
					+ "FROM `relation_team_league` "
					+ "INNER JOIN teams ON teams.id=relation_team_league.team_id "
					+ "INNER JOIN football_leagues ON relation_team_league.league_id=football_leagues.id "
					+ sqlCondition);
			
			while(resultSet.next()) {
				team_id.add(new Integer(resultSet.getString("id")));							
			}
			
			String sqlInsert = "INSERT INTO relation_user_team (user, team) VALUES ";			
			for (int i = 0; i < team_id.size(); i++) {
				sqlInsert += "(" + useId + "," + team_id.get(i) + ")";
				if (i != team_id.size() - 1) sqlInsert += ", ";
				else sqlInsert += ";";
			}
			command.execute(sqlInsert);
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	
	public void insertAllEvents(ArrayList<Event> events) {
		Map<String, Integer> leagues = this.getLeaguesId();
		Map<String, Integer> teams = this.getTeamsId();	
		
		try {
			command.execute("TRUNCATE TABLE events_today");			
			for (Event event: events) {									
				command.execute ("INSERT INTO `events_today` (`competition_type`, `start_time`, `status`, `first_team`, `result`, `second_team`, `live_status`) "
						+ "VALUES ("
						+ leagues.get(event.getCompetitionType()) + ", '"  
						+ event.getStartTime() + "', '" 
						+ event.getStatus() + "', " 
						+ teams.get(event.getFirstTeam()) + ", '"
						+ event.getResult() + "', " 
						+ teams.get(event.getSecondTeam()) + ", " 
						+ event.getLiveStatus() + ");"
				);
				/*
				command.execute ("INSERT INTO events_today VALUES " +
						"(NULL, 'q', 'w', 'e', 'r', 't', 'y', 22);"
				);	
				*/
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}	
	}
	
	public void updateEvents(ArrayList<Event> event) {
		
	}
	
	public String getAllEventsToday() {
		String res = "";
		ResultSet resultSet;
		try {
			resultSet = command.executeQuery("SELECT "
					+ "football_leagues.league_name AS competition_type, "
					+ "start_time, "
					+ "status, "					
					+ "t1.team_name AS first_team, "
					+ "result, "
					+ "t2.team_name AS second_team, "
					+ "live_status "
					+ "FROM events_today "
					+ "INNER JOIN football_leagues ON events_today.competition_type=football_leagues.id "
					+ "INNER JOIN teams AS t1 ON t1.id=events_today.first_team "
					+ "INNER JOIN teams AS t2 ON t2.id=events_today.second_team");						
			
			res += "<Events>";		
			while(resultSet.next()) {
				res += "<event>";		
				res += "<competition>" + resultSet.getString("competition_type") + "</competition>"; 
				res += "<start_time>"  + resultSet.getString("start_time") + "</start_time>";
				res += "<status>"      + resultSet.getString("status") + "</status>";
				res += "<first_team>"  + resultSet.getString("first_team") + "</first_team>";
				res += "<result>"      + resultSet.getString("result") + "</result>";
				res += "<second_team>" + resultSet.getString("second_team") + "</second_team>";
				res += "<live_status>" + resultSet.getInt("live_status") + "</live_status>";		
				res += "</event>";						
			}
			res += "</Events>";
			resultSet.close();		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * close data base connection
	 */
	public void closeConnection() {
		try {
			command.close();
			connection.close();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}