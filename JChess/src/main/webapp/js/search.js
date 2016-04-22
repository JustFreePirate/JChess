$(document).ready(function(){
    $('#Search').on('click',function(){
        var dataToServer = {
            action: 'search'
        };
        $.post('main',$.param(dataToServer));
        return false;
    });
    
});