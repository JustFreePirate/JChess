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
        }
        if(dataToServer.login != '' && dataToServer.password != ''){
            $.post('main', $.param(dataToServer), function(data){
                    if(data === 'correct'){
                        window.location.href = '/jchess/main.jsp';
                    } else {
                        login.setCustomValidity("Incorrect login/password");
                    }

            });
        }
    });

    $('#SignUp').on('click',function(){
/*        var login = $('#loginSignUp');
        var password = $('#passwordSignUp');
        var repeatPassword = $('#repeatPasswordSignUp');*/
        var login = document.getElementById('loginSignIn');
        var password = document.getElementById('passwordSignIn');
        var repeatPassword = document.getElementById('repeatPasswordSignUp');
        if(password.value === repeatPassword.value){
            var dataToServer = {
                action: 'SignUp',
                login: login.value,
                password: password.value
            }
            if(dataToServer.login != '' && dataToServer.password != ''){
                $.post('main',dataToServer,function(data){
                    if(data === 'correct'){
                        window.location.href = '/jchess/main.jsp';
                    } else {
                        login.setCustomValidity("Ooops. We have some trouble. Try again.");
                    }
                })
            }
        } else {
            repeatPassword.setCustomValidity("Passwords are not equal");
        }
    })

    $('.toggle').on('click', function() {
        $('.container').stop().addClass('active');
    });

    $('.close').on('click', function() {
        $('.container').stop().removeClass('active');
    });

});