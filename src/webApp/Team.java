package webApp;

public class Team {
	private String name;
	private String league;
	
	public String getName() {		
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLeague() {
		return league;
	}
	public void setLeague(String league) {
		this.league = league;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
	    if (obj == this) return true;
	    if (!(obj instanceof Team)) return false;
	    
	    Team team = (Team) obj;
		if (name.equals(team.getName()) && league.equals(team.getLeague())) {
			return true;
		}
		else return false;
	}
	
	@Override
	public int hashCode() {
	    return name.hashCode() + league.hashCode();
	}
}
