<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create image form.jsp</title>
</head>
<body>

	<h1>Create image FORM jsp</h1>

	
	------------------------------------

<!-- 	sf:errors path="*" cssClass="error" / -->

	<sf:form method="POST" enctype="multipart/form-data"
		action="/AntSpringMVC1/images/create" modelAttribute="imageModel">
		<sf:errors path="*" cssClass="error" />
		<br>
		<label for="image_description"> Image description: </label>
		<sf:input path="description" id="image_description" />
		<sf:errors path="description" cssClass="error" />

		<br>
		<label for="imageFile">Profile image: </label>
		<input name="imageFile" type="file" />

		<input name="commit" type="submit" value="Send..." />



	</sf:form>



</body>
</html>