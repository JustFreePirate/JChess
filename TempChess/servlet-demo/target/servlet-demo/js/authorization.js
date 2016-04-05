$(document).ready(function(){
    var login = document.getElementById("login");
    var password = document.getElementById("password");
    $("SignIn").on('click', function(){
        var req = new XMLHttpRequest();
        var data = {
        login: login,
        password: password
        }
        var str = JSON.stringify(data);
        alert(str);
        //req.open("POST","home of server",true);
        //req.send(data);


    });
});