$(document).ready(function(){
    $('Search').on('click',function(){
        var req = new XMLHttpRequest();
        var data = {
            Type: 'Search'
        }
        req.open("POST","home of server",true);
        req.send(data);
        req.onreadystatechange = function(){
            if(req.readyState === 4 && req.status === 200){
                var answer = JSON.parse(req.responseText);
                //TODO: Go to white or black board
            }
            else {return;}
        }
    })
})