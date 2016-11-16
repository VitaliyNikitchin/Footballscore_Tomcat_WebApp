function getAllTeams() {
	$.ajax({
        url: 'personalAccountPath',
        type: 'POST',
        dataType: 'xml',
        data: {parameter: "getAllTeams"},
        success: function(xml) {
        	var $html = "";
        	$(xml).find("league").each(function() {
        		$html += "<div class='league'>";
        		$html += "<p class='league_name'>" + $(this).find("league_name").text() + "</p>";		
        		$html += "<div class='team_block'>";
        		$(this).find("team").find("team_name").each(function() {        			
        			$html +="<p><input type='checkbox' name='teamList' value='" + $(this).text() + "'>" + $(this).text() + "</p>";
        		});
        		$html += "</div>";
        		$html += "</div>";
        	});
        	$("#all_available_teams").append($html);
        	//alert("success");
        	getMyTeams();///////////////////////////////////////////////////////////////////////////////////        	
        },
        statusCode: {
            401: function() {
              alert("Необходима авторизацыя");
              document.location.replace("authenticationForm.jsp");
            }
        },
        error: function() {
            alert("response Error");
        }
	});
}

function getMyTeams() {
	$.ajax({
        url: 'personalAccountPath',
        type: 'POST',
        dataType: 'xml',
        data: {parameter: "getMyTeams"},
        success: function(xml) {
        	var $html = "";
        	$(xml).find("league").each(function() {
        		$html += "<div class='league'>";
        		$html += "<p class='league_name'>" + $(this).find("league_name").text() + "</p>";		
        		$html += "<div class='team_block'>";
        		$(this).find("team").find("team_name").each(function() {        			
        			$html +="<p class='myTeam'>" + $(this).text() + "</p>";
        		});
        		$html += "</div>";
        		$html += "</div>";
        	});
        	$("#my_teams").append($html);
        	getTeamInfo();
        },
        error: function() {
            alert("response Error");
        }
	});	
}

function addTeamToMyTeams() {
	var checkedTeams = [];
	var checkboxes =  $("#all_available_teams").find("input[name='teamList']:checked");
	if (checkboxes.length == 0) {
		alert("Выберите хотябы одну команду");
		return;
	}	
	for (var i=0; i < checkboxes.length; i++) {
		var league = checkboxes.eq(i).parent().parent().parent().find(".league_name").text();
		var team = checkboxes.eq(i).val();
		var element = {league_name : league, team_name : team};
		checkedTeams.push(element);
	}
	
	$.ajax({
        url: 'personalAccountPath',
        type: 'POST',
        dataType: 'xml',
        data: {parameter: 'addTeamToMyTeams', checkedTeams: JSON.stringify(checkedTeams)},
        success: function(data) {         
        	window.location.reload();     
        },
        error: function() {
            alert("send team Error");
        }
	});
}

function getTeamInfo() {
	$("#my_teams").find("p.myTeam").on("click", function() {
		var team = $(this).text();
		$.ajax({
	        url: 'personalAccountPath',
	        type: 'POST',
	        dataType: 'xml',
	        data: {parameter: "getTeamInfo", team: team},
	        success: function(xml) {
	        	var $html = "";
	        	
	        	$(xml).find("player").each(function() {
	        		var cell = addPlayer(players, 4).getElementsByTagName("td");
	        		cell[0].innerHTML = $(this).find("player_name").text();
	        		cell[1].innerHTML = $(this).find("position").text();
	        		
	        		
	        		/*$html += "<p>" + $(this).find("player_name").text() + "</p>";
	        		$html += "<p>" + $(this).find("position").text() + "</p>";
	        		$html += "<p>" + $(this).find("number").text() + "</p>";
	        		$html += "<p>" + $(this).find("team_name").text() + "</p>";
	        		*/
	        	});
	        	
	        	//$("#players").append($html);
	        	alert("getTeamInfo");
	        },
	        error: function() {
	            alert("teamInfo Error");
	        }
		});	
	});	
}

function addPlayer(table, cellNum) {
	var row = table.insertRow();
	for (var z = 0; z < cellNum; z++) {
		row.insertCell();
	}
	return row;
}
