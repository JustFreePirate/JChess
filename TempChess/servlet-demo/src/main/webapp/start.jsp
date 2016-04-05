<html>
    <head>
        <title>Start</title>
         <script src="//code.jquery.com/jquery-1.12.0.min.js"></script>
         <script type="text/javascript" src="/servlet-demo/js/authorization.js"></script>
         <script type="text/javascript">
            function authorizationScript(){
                var login = document.getElementById("login");
                var password = document.getElementById("password");
                var req = new XMLHttpRequest();
                var data = {
                    login: login,
                    password: password
                }
                var str = JSON.stringify(data);
                alert(str);

            }
         </script>
    </head>

    <body>
        <p> Login: <input type="text" name="login"> </p>
        <p> Password: <input type="text" name="password"> </p>
        <input type="button" value="SignIn" name="SignIn">
    </body>
</html>