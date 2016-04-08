$(document).ready(function(){
    var login = document.getElementById('login');
    var password = document.getElementById('password');
    $('#SignIn').on('click', function(){
        var req = new XMLHttpRequest();
        var data = {
        login: login.value,
        password: password.value
        }
        var str = JSON.stringify(data);
        alert(str);
        //req.open("POST","home of server",true);
        //req.send(data);


    });

    $('.toggle').on('click', function() {
      $('.container').stop().addClass('active');
    });

    $('.close').on('click', function() {
      $('.container').stop().removeClass('active');
    });

});