<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
		<link href="css/mostraRisultati.css" rel="stylesheet">
	</head>
	<body>
		<div id="header">
			<div class="col-lg-9">
				<div id="header-img-container"class="col-lg-2">
					<a href="index.jsp">
						<img id="header-img" src="media/search_header_img.png">	
					</a>
				</div>
				<div class="col-lg-7">
					<form method="get" action="search">
						<div class="input-group">
			      			<input type="text" class="form-control" name="query" placeholder="Enter a query!">
			      			<span class="input-group-btn">
			        			<button class="btn btn-primary" type="submit">Search</button>
		      				</span>
		    			</div>
		    		</form>
				</div>
			</div>
  		</div>

		<%@page import="javax.servlet.*, java.util.*, resource.*, java.net.*"					%>
		<% ResultsOfSearch ros = (ResultsOfSearch) session.getAttribute("risultato");			%>
		<% int numeroPagina = (Integer) session.getAttribute("paginaCorrente"); 				%>
		<div id="main-area" class="col-md-12">
			<div id="message" class="col-md-12">
		<%		if (ros == null) {																%>
					<h3 id="error-message">Incorrect search.</h3>
		<%		} else { 																		%>
		<% 		if (ros.thereIsNoResult()) { 													%>
				<h2>No results for: <%=ros.getQuery()%></h2>
		<%		} else  {																						%>
		<%			String queryString = ros.isSuggestedSearch() ? ros.getSuggestedQuery() : ros.getQuery();	%>
		<%			if (ros.isSuggestedSearch()) {																%>
		<%			String ref = "search?query="+URLEncoder.encode(ros.getQuery(),"UTF-8")+"&forced=true";		%>
					<h1>Maybe did you mean: <em><%=ros.getSuggestedQuery()%></em>. 
						<small>
							Or <em><a href=<%=ref%>><%=ros.getQuery() %></a></em> was correct?
						</small>
					</h1>
		<%			} else {																					%>
					<h1>Search results for: <em><%=ros.getQuery()%></em></h1>
		<%			}																							%>
			</div>
			<div id="results-area" class="col-md-7">
		<%
			DocumentInfo[][] docs = ros.getDocResArray();
		%>
		<%
			numeroPagina = numeroPagina >= docs.length || numeroPagina < 0 ? 0 : numeroPagina;
		%>			
		<%
						for (int i = 0; i < docs[numeroPagina].length ; i++) {
					%>
		<%
			if (docs[numeroPagina][i] != null) {
		%>
		<%
			DocumentInfo doc = docs[numeroPagina][i];
		%>
									<div id="result">
										<div id="result-title">
											<a href="<%=doc.getRelativePath()%>"><%=doc.getTitle()%></a>
										</div>
										<div id="result-highlights">
											<%=doc.getHighlights()%>
										</div>
		<%
			if (doc.hasSimilarDocs()) {
		%>
											<span class="similar-pages-title">More like this:</span>
											<div class="similar-pages">
												<table class="table">
													<tr>
		<%
			for (DocumentInfo simDoc : doc.getRelatedDocument()) {
		%>
															<td class="tablecell">
																<a href="<%=simDoc.getRelativePath()%>"><%=simDoc.getTitle()%></a>
															</td>
		<%												}																							%>
													</tr>
												</table>
											</div>
		<%								}																											%>
									</div>
		<%						}																													%>
		<%					}																														%>
			</div>
			<div id="footer" class="col-md-7">
				<div class="if-container-head">
					<img class="if-img" src="media/footer_paging_first_part.png">&nbsp
				</div>
		<%		for (int i = 0; i<docs.length; i++) {																									%>
		<%			String ref = "search?query="+URLEncoder.encode(queryString, "UTF-8")+"&forced="+ros.isForced()+"&page="+i;							%>
					<div class="if-container-o">
						<a href=<%=ref%>>
		<%				if (i == numeroPagina) {																										%>			
							<img class="if-img" src="media/footer_paging_curr_page.png">
		<%				} else {																														%>
							<img class="if-img" src="media/footer_paging_other_pages.png">
		<%				}																																%>			
						<%=(i+1)%></a>
					</div>
		<%		}																																		%>
				<div class="if-container-tail">
					<img class="if-img" src="media/footer_paging_last_part.png">&nbsp
				</div>
			</div>
		<%		}																																		%>
		<%		}																																		%>
			
		</div>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
		<script src="bootstrap/js/bootstrap.min.js"></script>
	</body>
</html>

<span class="csb gbil ch" style="background:url(/images/nav_logo193_hr.png) no-repeat;background-position:-74px 0;background-size:167px;width:20px"></span>