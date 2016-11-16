/**
 * 
 */
var timerBlink;
var dateOfEnter;

function clearTable(table) {
	for (var i = table.rows.length - 1; i >= 0; i--) {
		table.deleteRow(i);
	}
}

function getFullDate(dayDifference) {
	var zeros = ["00", "0", ""];	
	var d = new Date();
	d.setDate(d.getDate()+dayDifference);		
	var dayString = new String(d.getDate());
	var day = zeros[dayString.length] + dayString;		
	var monthString = new String((parseInt(d.getMonth()) + 1).toString());
	var month = zeros[monthString.length] + monthString;
	return day+"-"+month+"-"+d.getFullYear();
}

function checkDate() {
	if (dateOfEnter.indexOf(getFullDate(0)) == -1)
		window.location.reload();
}

function getAllEvents(date, table) {
	$("#tableResultsLoadingAnimation").show();
	$.ajax({
        url: 'myServletPath',
        type: 'POST',
        dataType: 'xml',
        data: {request: 'getAllEvents', date: date},
        success: function(xml) { 
        	$("#tableResultsLoadingAnimation").hide();
        	$(xml).find('event').each(function(i) {
	        	//$("#footer").append($(this).find("first_team").text());        		        		
        		if (("").indexOf($(this).prev().find("competition").text()) == 0) {        			
        			addEventType($(this).find("competition").text(), table);  
        		} else if ($(this).find("competition").text().indexOf($(this).prev().find("competition").text()) == -1) {
        			addEventType($(this).find("competition").text(), table);
        		}  	
   	
        		var cell = addEvent(table, 7, "event").getElementsByTagName("td");
				changeStartTimeCell(cell[0], $(this).find("start_time").text());
				changeStatusCell(cell[1], $(this).find("status").text(), $(this).find("live_status").text());
				changeFirstTeamCell(cell[2], $(this).find("first_team").text(), $(this).find("live_status").text(), $(this).find("result").text());
				changeResultCell(cell[3], $(this).find("result").text(), $(this).find("live_status").text());
				changeSecondTeamCell(cell[4], $(this).find("second_team").text(), $(this).find("live_status").text(), $(this).find("result").text());
				changeLiveStatusCell(cell[5], $(this).find("live_status").text());    		
        	});    	
        	alert("success 1");
        },
        error: function() {
            alert("response Error myServletPath");
        }
	});
	
	/*
	var request = getXmlHttpRequest();
	request.open("POST", "server.php", true); 
	request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	//request.send("a=" + encodeURIComponent(a) + "&b=" + encodeURIComponent(b));
	request.send("date=" + encodeURIComponent(date) + "&clearDate=" + encodeURIComponent(getFullDate(-8)));
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {		
			$("#tableResultsLoadingAnimation").hide();
			var event = (request.responseText).split(":::");		
			for (var i = 0; i < event.length - 1; i++) {
				var eventElement = event[i].split(",,,");
					
				if (i == 0) {
					addEventType(eventElement[1], table);						
				} 
				else {
					var previousEventElement = event[i-1].split(",,,");
					if (previousEventElement[1].indexOf(eventElement[1]) == - 1) {
						addEventType(eventElement[1], table);
					}						
				}
				var cell = addEvent(table, 7, "event").getElementsByTagName("td");
				changeStartTimeCell(cell[0], eventElement[2]);
				changeStatusCell(cell[1], eventElement[3], eventElement[7]);
				changeFirstTeamCell(cell[2], eventElement[4], eventElement[7], eventElement[5]);
				changeResultCell(cell[3], eventElement[5], eventElement[7]);
				changeSecondTeamCell(cell[4], eventElement[6], eventElement[7], eventElement[5]);
				changeLiveStatusCell(cell[5], eventElement[7]);			
			}
		}
	};
	*/
}

function getChangedData() {
	checkDate();
	var request = getXmlHttpRequest();
	request.open("POST", "server.php", true); 
	request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	request.send("update=" + encodeURIComponent("") + "&dateForUpdate=" + encodeURIComponent(getFullDate(0)));
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {				
			if (request.responseText.trim().indexOf("updateAll") == 0) {
				alert("reload");
				window.location.reload();
				return;
			}	
			var table = document.getElementById("tableResults_today");
			var rows = table.getElementsByClassName("event");
			var event = (request.responseText).split(":::");			
/*
			alert(event.length - 1);
			if(event.length - 1 != 0) {
				var res;
				for (var z = 0; z < event.length - 1; z++) {
					res += event[z] + "\n\n";
				}
				alert("change: " + res);
			}
*/
			for (var i = 0; i < event.length - 1; i++) {
				var eventElement = event[i].split(",,,");
				var cell = rows[parseInt(eventElement[0])].getElementsByTagName("td");						
				changeStartTimeCell(cell[0], eventElement[2]);
				changeStatusCell(cell[1], eventElement[3], eventElement[7]);
				changeFirstTeamCell(cell[2], eventElement[4], eventElement[7], eventElement[5]);
				changeResultCell(cell[3], eventElement[5], eventElement[7]);
				changeSecondTeamCell(cell[4], eventElement[6], eventElement[7], eventElement[5]);
				changeLiveStatusCell(cell[5], eventElement[7]);
			}
			refreshBlik();
		}				
	};
}
/*
function getNews() {
	var request = getXmlHttpRequest();
	request.open("POST", "server.php", true); 
	request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	request.send("getNews=" + encodeURIComponent(""));
	request.onreadystatechange = function() {
		if (request.readyState == 4 && request.status == 200) {	
			var table = document.getElementById("fullNews");
			clearTable(table);
			document.getElementById("newsTitles").innerHTML = "";
			alert("news ok!!");		
			
			var newsItem = (request.responseText).split(":::");
			for (var i = 0; i < newsItem.length - 1; i++) {
				var newsElement = newsItem[i].split(",,,");
				document.getElementById("newsTitles").innerHTML += "<p>" + newsElement[0] + "</p>";
				
				var cell = addEvent(table, 2, "title").getElementsByTagName("td");
				cell[0].innerHTML = "<p>" + newsElement[0] + "</p>";
				cell[1].innerHTML = "<p>" + newsElement[1] + "</p>";
				
				var newsContentRow = table.insertRow();
				newsContentRow.className = "newsContent";
				var c = newsContentRow.insertCell();
				c.setAttribute("colspan", "2");
				c.innerHTML = "<p>" + newsElement[2] + "<br>" + newsElement[3] + "</p>";
			}
			showNews();
		}				
	};
}
*/
function addEventType(eventType, table) {
	var newRow = table.insertRow();
	newRow.className = "eventType";
	var newCell = newRow.insertCell();
	newCell.setAttribute("colspan", "7");
	newCell.innerHTML = "<p>" + eventType.toUpperCase() + "</p>";
}
function addEvent(table, cellNum, newClass) {
	var row = table.insertRow();
	row.className = newClass;
	for (var z = 0; z < cellNum; z++) {
		row.insertCell();
	}
	return row;
}
function changeStartTimeCell(cell, time) {
	cell.innerHTML = time;
}
function changeStatusCell(cell, status, liveStatus) {
	if (liveStatus.indexOf("0") == -1) {
		cell.style.color = "red";
	}
	else cell.style.color = "black";
	cell.innerHTML = status;
}
function changeFirstTeamCell(cell, team, liveStatus, score) {
	var res = score.split(":");
	if (liveStatus.indexOf("1") == -1 && (parseInt(res[0]) > parseInt(res[1]))) {
		cell.innerHTML = "<p><b>" + team + "</b></p>";
	}
	else {	
		cell.innerHTML = "<p>" + team + "</p>";
	}
}
function changeSecondTeamCell(cell, team, liveStatus, score) {
	var res = score.split(":");
	if (liveStatus.indexOf("1") == -1 && (parseInt(res[0]) < parseInt(res[1]))) {
		cell.innerHTML = "<p><b>" + team + "</b></p>";
	}
	else {	
		cell.innerHTML = "<p>" + team + "</p>";
	}
}
function changeResultCell(cell, res, liveStatus) {
	cell.innerHTML = res;
	if (liveStatus.indexOf("0") == -1) {
		cell.style.color = "red";
	}
	else cell.style.color = "black";
}
function changeLiveStatusCell(cell, liveStatus) {
	if (liveStatus.indexOf("0") == -1) {
		cell.innerHTML = "<blink><img src='images/live.png' alt='live'/></blink>";
	}
	else cell.innerHTML = "";
}


function blink() {
	for(var i = 0; i < document.getElementsByTagName("blink").length; i++){
		var s = document.getElementsByTagName("blink")[i];
		s.style.visibility=(s.style.visibility=='visible')?'hidden':'visible';
	}
}

function refreshBlik() {
	clearInterval(timerBlink);
	for(var i = 0; i < document.getElementsByTagName("blink").length; i++){
		var s = document.getElementsByTagName("blink")[i];
		s.style.visibility = "hidden";
	}
	timerBlink = setInterval(blink, 500);
}

