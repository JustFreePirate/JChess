$(document).ready(function(){
    $('#Send').on('click',function(){
        //var email = $('#email');
        var email = document.getElementById('email');
        if(isEmail(email.value)){
            var dataToServer = {
                action: 'forgotPassword',
                email: email.value
            }
            if(dataToServer.email != ''){
                $.post('main',dataToServer,function(data){
                    if(data === 'correct'){
                        window.location.href = '/jchess/SignInAndSignUp.jsp';
                    } else {
                        email.setCustomValidity("Oooops. We have some trouble. Please, try again.");
                    }
                })
            }
        } else {
            email.setCustomValidity("Incorrect email");
        }
        return false
    });


    function isEmail(email){
        return (email.indexOf('@') != -1)
    }
})