<!--/*
* Author: Fredrik Öberg
* Date of creation: 201213
*
* The code represents the form in the WebQuiz application and is dynamically changed based on 
* what questions are stored and retreived from the database. It receives the parameter "response" 
* when the user has not checkad all the available answers. 
*
*/-->

<% String message = request.getParameter("response"); %>
<% String quiz = request.getParameter("quiz"); %>

<html>
    <head>
        <title>WebQuiz</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>

        <h2>The WebQuiz</h2>
        <form name="quizanswers" method="POST">
            <%=quiz%>
            <input name="submit" type="submit" value="Submit"/>
            <% if (message != null) {%>
            <%=message%>
            <%}%> 
        </form>

    </body>
</html>
