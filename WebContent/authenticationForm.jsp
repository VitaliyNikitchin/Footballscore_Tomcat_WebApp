<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Authentication</title>
<script src="js/jquery-2.1.4.min.js" type="text/javascript"></script>
<script type="text/javascript">
	function validateForm() {
		var login = document.getElementById("login").value;
		var password = document.getElementById("password").value;		
		if ((login.length == 0) || (password.length == 0)) 
		{
			alert("Заполните все поля формы");
			return false;
		}
	}
	
	window.onload = function() {
		<% 
		String authenticationError = (String) request.getAttribute("authenticationError"); 
		out.println("error ");
		%>		
	}
</script>
<style type="text/css">
	form {
		width: 310px;
		height: 160px;
		margin: 100px auto;
		padding: 10px 25px;
		border: 1px solid black;
	}
	form p input {	
		width: 300px;				
	}
</style>
</head>
<body>
	<form onsubmit="return validateForm()" method="POST" action="AuthenticationServlet">
		<p>
			<label>Логин</label>
			<br>
			<input id="login" type="text" name="login" />
		</p>
		<p>
			<label>Пароль</label>
			<br>
			<input id="password" type="password" name="password" />
		</p>				
		<input type="submit" value="Войти"/>
	</form>	
</body>
</html>