$(document).ready(function () {

    $('#SignIn').on('click', function () {
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

        if (dataToServer.login != '' && dataToServer.password != '') {
            $.post('login', $.param(dataToServer), function (data) {
                if (data === 'sign_in_success') {
                    window.location.href = 'main.jsp';
                } else {
                    if (data === 'login_password_invalid') {
                        login.setCustomValidity("Incorrect login/password");
                    } else {
                        alert('Oooops. We have some trouble! =)');
                    }
                }

            });
        }
        return false;
    });

    $('#SignUp').on('click', function () {
        /*        var login = $('#loginSignUp');
         var password = $('#passwordSignUp');
         var repeatPassword = $('#repeatPasswordSignUp');*/
        var login = document.getElementById('loginSignUp');
        var password = document.getElementById('passwordSignUp');
        var repeatPassword = document.getElementById('repeatPasswordSignUp');
        if (password.value === repeatPassword.value) {
            var dataToServer = {
                action: 'SignUp',
                login: login.value,
                password: password.value
            };
            if (dataToServer.login != '' && dataToServer.password != '') {
                $.post('login', $.param(dataToServer), function (data) {
                    if (data === 'sign_up_success') {
                        window.location.href = 'main.jsp';
                    } else {
                        if(data === 'sign_up_filter_failure')
                            login.setCustomValidity("Invalid login");
                        else{
                            if(data === 'sign_up_user_already_exist'){
                                login.setCustomValidity("Email already exist");
                            } else {
                                alert('Oooops. We have some trouble! =)');
                            }
                        }
                    }
                });
            }
        } else {
            repeatPassword.setCustomValidity("Passwords are not equal");
        }
        return false;
    });

    $('.toggle').on('click', function () {
        $('.container').stop().addClass('active');
    });

    $('.close').on('click', function () {
        $('.container').stop().removeClass('active');
    });

});