$(document).ready(function(){

    $('#SignIn').on('click', function(){
        var login = document.getElementById('loginSignIn');
        var password = document.getElementById('passwordSignIn');
        var req = new XMLHttpRequest();
        var data = {
            type: 'SignIn',
            login: login.value,
            password: password.value
        }
        var str = JSON.stringify(data);
        alert(str);
        req.open("POST",'/main',true);
        req.send(data);
        req.onreadystatechange = function() {
            if (req.readyState === 4 && req.status === 200){
                answer =  JSON.parse(req.responseText);
            }
                else return;
        }
        if(answer.response === 'Ok'){
            window.location.href = '/jchess/main.jsp';
        } else {
            alert('Incorrect login/password');
        }

    });

    $('#SignUp').on('click',function(){
        var login = document.getElementById('loginSignUp');
        var password = document.getElementById('passwordSignUp');
        var repeatPassword = document.getElementById('repeatPasswordSignUp');
        if(password.value === repeatPassword.value){
            var req = new XMLHttpRequest();
            var data = {
                type: 'SignUp',
                login: login.value,
                password: password.value
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
        } else {
            alert('Passwords is not equal');
        }
    })

    $('.toggle').on('click', function() {
      $('.container').stop().addClass('active');
    });

    $('.close').on('click', function() {
      $('.container').stop().removeClass('active');
    });

});