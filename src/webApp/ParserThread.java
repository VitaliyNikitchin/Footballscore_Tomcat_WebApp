package webApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import dbConnection.DBConnector;

public class ParserThread implements Runnable {
	private int sleepTime;
	private Parser parser; 
	private DBConnector dbConnector;
	private ArrayList<Event> prevEventList = new ArrayList<Event>();
	private ArrayList<Event> updateEventList = new ArrayList<Event>();
	
	
	public ParserThread(int sleepTime) {
		this.sleepTime = sleepTime;
		this.parser = new Parser();
		this.dbConnector = new DBConnector();
	}

	@Override
	public void run() {
		for (int i = 0; i < 1; i++) {
			String path = "http://www.sport-express.ru/live/football/";
			ArrayList<Event> events;
			events = parser.parseEvents(path);
						
			this.updateFootballLeagues(events);
			this.updateFootballTeams(events);
			this.updateRelationTeamLeague(events);
			this.saveEvents(events);
			
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		dbConnector.closeConnection();
	}
	
	private void saveEvents(ArrayList<Event> actualEventList) {
		if (prevEventList.isEmpty()) {
			prevEventList.addAll(actualEventList.subList(0, actualEventList.size()));
			dbConnector.insertAllEvents(actualEventList);
		}
		else {
			if((prevEventList.size() == actualEventList.size()) && 
				(prevEventList.get(prevEventList.size()).getFirstTeam().equals(actualEventList.get(actualEventList.size()).getFirstTeam()) )) 
			{
				
			}
			else {
				dbConnector.insertAllEvents(actualEventList);
			}
		}		
	}
	
	/**
	 * Update list of football leagues in databases on server
	 * @param eventList -- list of football events today
	 */
	private void updateFootballLeagues(ArrayList<Event> eventList) {
		Set<String> newLeagues = new HashSet<String>();
		Set<String> curentLeagues = new HashSet<String>();		
		for (Event event: eventList) {
			curentLeagues.add(event.getCompetitionType());
		}
		
		Set<String> existingLeagues = dbConnector.getExistingLeagues();
		if (!existingLeagues.isEmpty()) {
			for (String s: curentLeagues) {
				if (!existingLeagues.contains(s)) {
					newLeagues.add(s);
				}
			}
			dbConnector.addFootballLeagues(newLeagues);
		}
		else {
			dbConnector.addFootballLeagues(curentLeagues);
		}
	}
	

	/**
	 * Update list of football teams in databases on server
	 * @param eventList -- list of football events today
	 */
	private void updateFootballTeams(ArrayList<Event> eventList) {
		Set<String> newTeams = new HashSet<String>();
		Set<String> curentTeams = new HashSet<String>();		
		for (Event event: eventList) {	
			curentTeams.add(event.getFirstTeam());			
			curentTeams.add(event.getSecondTeam());
		}
		
		Set<String> existingTeams = dbConnector.getExistingTeams();						
		if (!existingTeams.isEmpty()) {
			for (String s: curentTeams) {
				if (!existingTeams.contains(s)) {
					newTeams.add(s);
				}
			}
			dbConnector.addFootballTeams(newTeams);
		}
		else {
			dbConnector.addFootballTeams(curentTeams);
		}
	}
	
	
	private void updateRelationTeamLeague(ArrayList<Event> eventList) {	
		Set<Team> newRelations = new HashSet<Team>();
		Set<Team> curentRelations = new HashSet<Team>();		
		for (Event event: eventList) {
			Team team1 = new Team();
			team1.setName(event.getFirstTeam());
			team1.setLeague(event.getCompetitionType());			
			curentRelations.add(team1);
			
			Team team2 = new Team();
			team2.setName(event.getSecondTeam());
			team2.setLeague(event.getCompetitionType());			
			curentRelations.add(team2);
		}
		
		Set<Team> existingRelations = dbConnector.getExistingRelationsTeamLeague();						
		if (!existingRelations.isEmpty()) {
			for (Team team: curentRelations) {
				if (!existingRelations.contains(team)) {
					newRelations.add(team);
				}
			}
			dbConnector.addRelationsTeamLeague(newRelations);
		}
		else {
			dbConnector.addRelationsTeamLeague(curentRelations);
		}
	}
}
