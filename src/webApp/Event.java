package webApp;

public class Event {	
	private String competitionType;
	private String startTime;	
	private String status;	
	private String firstTeam;
	private String secondTeam;
	private String result;
	private boolean liveStatus;
	
	public String getCompetitionType() {
		return competitionType;
	}
	public void setCompetitionType(String competitionType) {
		this.competitionType = competitionType;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFirstTeam() {
		return firstTeam;
	}
	public void setFirstTeam(String firstTeam) {
		this.firstTeam = firstTeam;
	}
	public String getSecondTeam() {
		return secondTeam;
	}
	public void setSecondTeam(String secondTeam) {
		this.secondTeam = secondTeam;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public boolean getLiveStatus() {
		return liveStatus;
	}
	public void setLiveStatus(boolean liveStatus) {
		this.liveStatus = liveStatus;
	}

}
