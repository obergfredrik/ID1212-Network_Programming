<!--/*
* Author: Fredrik Öberg
* Date of creation: 201118
*
* The code represents the register page in the WebQuiz application. It receives the parameter "response" 
* when the user has tried to register a user name already in the application database. 
*
*/-->


<% String message = request.getParameter("response"); %>

<html>
    <head>
        <title>WebQuiz</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <h1>Welcome to the WebQuiz</h1>
        <h2>Register to gain access</h2>
        <form method="POST" action="">
            <input name="name" type="text" value="Name"/> 
            <% if (message != null) {%>
            <%=message%>
            <%}%>         
            <input name="email" type="text" value="E-Mail"/>
            <input name="password" type="text" value="Password"/>
            <input name="register" type="submit" value="Register"/>
        </form>
        <h2>Login if you already are a user</h2>
        <form method="POST">
            <input  name="tologin" type="submit" value="Log In"/>
        </form>
    </body>
</html>
