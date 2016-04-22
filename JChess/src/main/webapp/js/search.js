$(document).ready(function(){
    $('#Search').on('click',function(){
        var dataToServer = {
            action: 'search'
        };
        $.post('main',$.param(dataToServer), function (data) {
            if(data === 'WHITE'){
                window.location.href = '/jchess/whiteBoard.jsp';
            }
            if(data === 'BLACK'){
                window.location.href = '/jchess/blackBoard.jsp';
            }
        });
        return false;
    });
    
});