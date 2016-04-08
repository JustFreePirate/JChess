$(document).ready(function(){
    var login = document.getElementById('login').value;
    var password = document.getElementById('password').value;
    $('#SignIn').on('click', function(){
        var req = new XMLHttpRequest();
        var data = {
            type: "SingIn",
            login: login,
            password: password
        }
        var str = JSON.stringify(data);
        alert(str);
        /*req.open("POST","home of server",true);
        req.send(data);
        req.onreadystatechange = function() {
            if (req.readyState === 4 && req.status === 200){
                answer =  JSON.parse(req.responseText);
            }
                else return;
        }
        if(answer === 'Ok'){
            //TODO: GOTO to next jsp
        }*/

    });
});