<html>
	<body style="text-align: center;">
		<% String errorCode = request.getParameter("errorCode");
			if( errorCode == null ) { %>
			<h2>MOBILE SERVICES</h2>
		<%} else if( "400".equals(errorCode) ) { %>
			<h2>Bad Request.</h2>
		<%} else if( "404".equals(errorCode) ) { %>
			<h2>Not Found.</h2>
		<%} else if( "405".equals(errorCode) ) { %>
			<h2>Method Not Allowed.</h2>
		<%} else if( "500".equals(errorCode) ) { %>
			<h2>Internal Server Error.</h2>
		<%} else if( "502".equals(errorCode) ) { %>
			<h2>Bad Gateway.</h2>
		<%} %>
	</body>
</html>	
