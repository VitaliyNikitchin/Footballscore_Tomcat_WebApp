<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration</title>
<style type="text/css">
	form {
		width: 310px;
		height: 380px;
		margin: 100px auto;
		padding: 10px 25px;
		border: 1px solid black;
	}
	form p input {	
		width: 300px;				
	}
</style>
<script type="text/javascript">
	function validateForm() {	
		var login = document.getElementById("login").value;
		var email = document.getElementById("email").value;
		var surname = document.getElementById("surname").value;
		var name = document.getElementById("name").value;
		var p1 = document.getElementById("password").value;
		var p2 = document.getElementById("passwordRepeat").value;
		if ((login.length == 0) || (email.length == 0) || 
				(p1.length == 0) || (p2.length == 0) || 
				(surname.length == 0) || (name.length == 0)) 
		{
			alert("Заполните все поля формы");
			return false;
		}
		if (!((p1.indexOf(p2) == 0) && (p2.indexOf(p1) == 0))) {
			alert("Пароли не совпадают");
			return false;
		}
	}
	
	window.onload = function() {
		<% 	String login = (String) request.getAttribute("loginName");%>
		var loginName = "<%= login %>";		
		if (loginName != "null") {
			alert("Введенный вами логин \"" + loginName + "\" уже занят.\nПопробуйте зарегистрироваться под другим именем");
		}		
	}
</script>
</head>
<body>
	<form id="registration" onsubmit="return validateForm()" method="POST" action="RegistrationServlet">
		<p>
			<label>Логин</label>
			<br>
			<input id="login" type="text" name="login" />
		</p>
		<p>
			<label>Имя</label>
			<br>
			<input id="name" type="text" name="name" />
		</p>		
		<p>
			<label>Фамилия</label>
			<br>
			<input id="surname" type="text" name="surname" />
		</p>				
		<p>
			<label>E-mail</label>
			<br>
			<input id="email" type="text" name="email" />
		</p>
		<p>
			<label>Пароль</label>
			<br>
			<input id="password" type="password" name="password" />
		</p>
		<p>
			<label>Повторите пароль</label>
			<br>
			<input id="passwordRepeat" type="password" name="repeatPassword" />
		</p>
		<input type="submit" />
	</form>						
</body>
</html>