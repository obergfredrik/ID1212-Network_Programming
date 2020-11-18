<% String message = request.getParameter("response"); %>

<html>
    <head>
        <title>WebQuiz</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
      
        <h2>The WebQuiz</h2>
        <form name="quizanswers" method="POST">
            Question 1
            <br>
            <br>
            Where was the fortune cookie invented?
            <br>
            <input name="q1" type="radio" value="a"/> San Francisco
            <input name="q1" type="radio" value="b"/> Shanghai
            <input name="q1" type="radio" value="c"/> Tokyo
            <input name="q1" type="radio" value="d"/> Paris
            <br>
            <br>
            <br>
            Question 2
            <br>
            <br>
            If you dug a hole through the centre of the earth starting from Wellington in New Zealand, which European country would you end up in?
            <br>
            <input name="q2" type="radio" value="a"/> England
            <input name="q2" type="radio" value="b"/> Spain
            <input name="q2" type="radio" value="c"/> Schweiz
            <input name="q2" type="radio" value="d"/> Norway
            <br>
            <br>
            <br>
            Question 3
            <br>
            <br>
            Native to the Caribbean, what sort of animal is the mountain chicken?
            <br>
            <input name="q3" type="radio"  value="a"/> Turtle
            <input name="q3" type="radio"  value="b"/> Lizard
            <input name="q3" type="radio"  value="c"/> Snake
            <input name="q3" type="radio"  value="d"/> Frog
            <br>
            <br>
            <br>
            Question 4
            <br>
            <br>
             It's illegal in Texas to put what on your neighbour’s Cow?
            <br>
            <input name="q4" type="radio"  value="a"/> T-Shirts
            <input name="q4" type="radio"  value="b"/> Grafitti
            <input name="q4" type="radio"  value="c"/> Scare Crow
            <input name="q4" type="radio"  value="d"/> Flowers
            <br>
            <br>
            <br>
            <input name="submit" type="submit" value="Submit"/>
            <% if(message != null){%>
            <%=message%>
            <%}%> 
        </form>
      
    </body>
</html>
