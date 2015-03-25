<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>List of images</title>
</head>
<body>

	<h1>List of images</h1>

	<c:forEach var="elem" items="${listNames}">
		<c:out value="${ elem}" />
		<br>
	</c:forEach>

	<sf:form method="GET" action="/AntSpringMVC1/images/showCreateForm">
		<input name="commit" type="submit" value="Add new image" />
	</sf:form>

</body>
</html>