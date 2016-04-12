$(document).ready(function(){
    $('#Send').on('click',function(){
        var email = document.getElementById('email');
        if(isEmail(email.value)){
            var req = new XMLHttpRequest();
            var data = {
                type: 'forgotPassword',
                email: email.value
            }
            var str = JSON.stringify(data);
            alert(str);
            req.open("POST","home of server",true);
            req.send(data);
            req.onreadystatechange = function() {
                if (req.readyState === 4 && req.status === 200){
                    answer =  JSON.parse(req.responseText);
                }
                    else return;
            }
            if(answer === 'Ok'){
                window.location.href = '/jchess/SignInAndSignUp.jsp';
            } else {
                alert('Oooops. We have some trouble. Please, try again.');
            }
        } else {
            alert('Incorrect email!');
        }
    })
})

function isEmail(email){
    for(i = 0, i < email.length, i++){
        //blablabla
    }
}