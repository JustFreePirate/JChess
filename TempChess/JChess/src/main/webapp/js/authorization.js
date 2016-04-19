$(document).ready(function(){
    
    $('#SignIn').on('click', function(){
        var login = document.getElementById('j_login');
        var password = document.getElementById('j_password');
        var req = new XMLHttpRequest();
        
        // var data = {
        // login: login.value,
        // password: password.value
        // }
        // var jqxhr = $.post("login", $.param(data), function callbackfunc(data) {
        //     alert(data);
        // });

        var params = {
            foo: "fooValue",
            bar: "barValue",
            baz: "bazValue"
        };

        $.post("login", $.param(params), function(response) {
            alert(response)
        });

        

        //var str = JSON.stringify(data);
        //alert(str);
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

    $('.toggle').on('click', function() {
      $('.container').stop().addClass('active');
    });

    $('.close').on('click', function() {
      $('.container').stop().removeClass('active');
    });

});