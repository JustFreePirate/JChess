$(document).ready(function(){

    $('#SignIn').on('click', function(){
        /*var login = $('#loginSignIn');
        var password = $('#passwordSignIn');
        alert(login.value + ' ' + password.value);*/
        var login = document.getElementById('loginSignIn');
        var password = document.getElementById('passwordSignIn');

        var dataToServer = {
            action: 'SignIn',
            login: login.value,
            password: password.value
        };
        
        if(dataToServer.login != '' && dataToServer.password != ''){
            $.post('login', $.param(dataToServer), function(data){
                    alert(data);
                    login.value = "correct"
                    if(data === 'correct'){
                        alert("sign in correct");
                        window.location.href = 'main.jsp';
                    } else {
                        alert("sign in not correct");
                        login.setCustomValidity("Incorrect login/password");
                    }

            });
        }
        return false;
    });

    $('#SignUp').on('click',function(){
/*        var login = $('#loginSignUp');
        var password = $('#passwordSignUp');
        var repeatPassword = $('#repeatPasswordSignUp');*/
        var login = document.getElementById('loginSignUp');
        var password = document.getElementById('passwordSignUp');
        var repeatPassword = document.getElementById('repeatPasswordSignUp');
        if(password.value === repeatPassword.value){
            var dataToServer = {
                action: 'SignUp',
                login: login.value,
                password: password.value
            };
            if(dataToServer.login != '' && dataToServer.password != ''){
                $.post('login',$.param(dataToServer),function(data){
                    if(data === 'correct'){
                        alert("sign up correct");
                        window.location.href = 'main.jsp';
                    } else {
                        alert("sign up not correct");
                        login.setCustomValidity("Ooops. We have some trouble. Try again.");
                    }
                });
            }
        } else {
            repeatPassword.setCustomValidity("Passwords are not equal");
        }
        return false;
    });

    $('.toggle').on('click', function() {
        $('.container').stop().addClass('active');
    });

    $('.close').on('click', function() {
        $('.container').stop().removeClass('active');
    });

});