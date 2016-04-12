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
                if(answer.color === 'white'){
                    window.location.href = '/jchess/whiteBoard.jsp';
                }
                if(answer.color === 'black'){
                    window.location.href = '/jchess/blackBoard.jsp';
                }
            }
            else {return;}
        }
    })
})