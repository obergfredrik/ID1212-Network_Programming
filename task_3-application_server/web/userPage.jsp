<!--/*
* Author: Fredrik Öberg
* Date of creation: 201118
* Updated: 201213
*
* The code represents the users private page in the WebQuiz application 
* and presents the data of the users interaction with the application. It 
* receives the parameters "name", "average", "answer" and "quizzes" which 
* is the data bein shown to the user if that data is present in teh application 
* database. data  
*
*/-->


<% String quizzes = request.getParameter("quizzes"); %>
<% String average = request.getParameter("average"); %>
<% String answer = request.getParameter("answer"); %>
<% String name = request.getParameter("name");%>
<% String questions = request.getParameter("questions");%>

<html>
    <head>
        <title>WebQuiz</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <h1>Hello <%=name%></h1>
        <h2>You have taken <%=quizzes%> quiz(es)</h2>
        <%if (!quizzes.equals("0")) {%>
        <h2>Your last quiz had <%=answer%> correct answers out of <%=questions%></h2>
        <%}%>
        <h2>Your average correct answers are <%=average%></h2>
        <form method="POST" action="">
            <input name="newquiz" type="submit" value="Take New Quiz"/>
        </form>
        <form method="POST">
            <input  name="logout" type="submit" value="Log Out"/>
        </form>
    </body>
</html>                                    