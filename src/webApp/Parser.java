package webApp;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup; 
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser  {
	
	public ArrayList<Event> parseEvents(String path) {	//"http://www.sport-express.ru/live/" + date + "/football/"
		ArrayList<Event> actualEventList = new ArrayList<Event>();
		Element football = null;
		try {
			Document doc = Jsoup.connect(path).get();
			football = doc.select("div.football").select("div.results_border").get(0);
		} catch (IOException e2) {
			e2.printStackTrace();
		}	
		for (int i = 0; i < football.select("div.toggled_results").size(); i++) {
			String competitionType = football.select("div.toggled_results").get(i).text();
			if (competitionType.indexOf("сейчас ") != -1)
				competitionType = competitionType.substring(0, competitionType.indexOf("сейчас "));
			if (competitionType.indexOf(". Группа ") != -1)
				competitionType = competitionType.substring(0, competitionType.indexOf(". Группа "));
			if (competitionType.indexOf("-й раунд") != -1)
				competitionType = competitionType.substring(0, competitionType.indexOf("-й раунд") - 3);
			
			Elements allEvents = football.select("div.white_block").get(i).select("table.table_score tbody tr");
			for (Element game : allEvents) {
				Event event = new Event();								
				event.setCompetitionType(competitionType.replaceAll("[&\']", ""));
				event.setStartTime(game.select("td.ph_20 div.fs_20").get(0).text());
				event.setStatus(game.select("td.ph_20 div.t_left").get(0).text());
				event.setFirstTeam(game.select("td.t_right span").get(0).text().replaceAll("[&\']", ""));
				event.setResult(game.select("td.fs_22 span").get(0).text());
				event.setSecondTeam(game.select("td.t_left span").get(0).text().replaceAll("[&\']", ""));
				
				if ((event.getStatus().indexOf("1-й тайм") == 0) || 
						(event.getStatus().indexOf("2-й тайм") == 0) ||
						(event.getStatus().indexOf("Перерыв") == 0) ||
						(event.getStatus().indexOf("Дополнительное время") == 0))
						
				{
					event.setLiveStatus(true);
				}
				else event.setLiveStatus(false);
				
				actualEventList.add(event);
			}
		}
		//this.printList(actualEventList);
		return actualEventList;
	}
	
	
	/*private*/ void printList(ArrayList<Event> list) {
		for (Event event : list) {
			System.out.print(event.getCompetitionType() + "  ");
			System.out.print(event.getStartTime() + "  ");
			System.out.print(event.getStatus() + "  ");
			System.out.print(event.getFirstTeam() + "  ");
			System.out.print(event.getResult() + "  ");
			System.out.print(event.getSecondTeam() + "  ");
			System.out.println("\n");
		}
	}
	
}
