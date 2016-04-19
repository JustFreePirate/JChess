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
                        alert('Incorrect login/password');
                        //TODO: Replace alert
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
                        alert('Trouble');
                    }
                })
            }
        } else {
            alert('Passwords is not equal');
            //TODO: Replace alert
        }
    })

    $('.toggle').on('click', function() {
        $('.container').stop().addClass('active');
    });

    $('.close').on('click', function() {
        $('.container').stop().removeClass('active');
    });

});