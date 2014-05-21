<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0);
%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>Rooma 3 search engine</title>
	<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
	<link href="css/index.css" rel="stylesheet">
</head>
<body>
	<div class="container">
      <div class="jumbotron">
        <img id="img_main" src="media/index_img.png" width="450">
        <div id="menu">
			<form method="get" action="search">
				<p><input id="query" name="query" type="text" class="form-control" autocomplete="on" placeholder="Enter a query!"></p>
				<p><button class="btn btn-primary btn-sm" type="submit">Search</button></p>
			</form>
		</div>
      </div>
     </div>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>
</body>
</html>