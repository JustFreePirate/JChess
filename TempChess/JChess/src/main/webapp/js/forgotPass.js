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
                        alert('Oooops. We have some trouble. Please, try again.');
                    }
                })
            }
        } else {
            alert('Incorrect email!');
        }
    })


    function isEmail(email){
        return (email.indexOf('@') != -1)
    }
})