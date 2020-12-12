<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<% String message = request.getParameter("response"); %>

<html>
    <head>
        <title>WebQuiz</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <h1>Admin page</h1>
        <h2>Enter the forms to add a new question to the WebQuiz database</h2>
        <h3>Type the question</h3>
         <form name="newquestion" method="POST">
            <input  name="question" type="text" value=""/>
            <h3>Set the answer choices</h3>
            A  <input name="c1" type="text" value=""/>       
            B  <input name="c2" type="text" value=""/>
            C  <input name="c3" type="text" value=""/>
            D  <input name="c4" type="text" value=""/>
            <h3>Set the correct answer</h3>
            A <input name="correct" type="radio" value="a"/>  
            B <input name="correct" type="radio" value="b"/> 
            C <input name="correct" type="radio" value="c"/> 
            D <input name="correct" type="radio" value="d"/> 
            <h3>Submit when finished</h3>
            <div>
              <% if (message != null) {%>
              <%=message%>
              <%}%> 
            </div>  
            <input  name="newquestion" type="submit" value="Submit"/>
        </form>
        <br>
        <form method="POST">
            <input name="logout" type="submit" value="Log Out"/>
        </form>
    </body>
</html>
