<% String message = request.getParameter("response"); %>

<html>
    <head>
        <title>WebQuiz</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <h1>Welcome to the WebQuiz</h1>
        <h2>Login to start a quiz</h2>
        <form method="POST" action="">
            <input name="name" type="text" value="Name"/>
            <input name="password" type="text" value="Password"/>
            <input name="login" type="submit"  value="Log In"/>
        </form>
        <% if(message != null){%>
            <%=message%>
        <%}%> 
        <h2>Register if you are a new user</h2>
        <form method="POST">
             <input name="toregister" type="submit" value="Register"/>
        </form>
    </body>
</html>
