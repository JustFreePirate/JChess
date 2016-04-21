$(document).ready(function(){
    $('#Search').on('click',function(){
        var dataToServer = {
            action: 'search'
        };
        $.post('main',$.param(dataToServer),function(color){
            if(color === 'white'){
                window.location.href = 'whiteBoard.jsp';
            }
            if(color === 'black'){
                window.location.href = 'blackBoard.jsp';
            }
        });
        return false;
    });
    
});