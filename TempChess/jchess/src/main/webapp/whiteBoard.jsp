<html>
    <head>
        <title>Game page</title>
        <script src="//code.jquery.com/jquery-1.12.0.min.js"></script>
        <script type="text/javascript" src="/jchess/js/chess_board_whitePlayer.js"></script>
        <link rel="stylesheet" href="css/style.css">



    </head>
    <body onload='draw();'>
    		<div>
    			<canvas id="chess" width="800" height="800" border="0"></canvas>
    		</div>
    		<div id="MoveTab">
    		<table border=1 id="qandatbl" align="center">
                <tr>
                <th class="col1">Question No</th>
                <th class="col2">Option Type</th>
                <th class="col1">Duration</th>
                </tr>

               <tbody>
                <tr>
                <td class='qid'></td>
                <td class="options"></td>
                <td class="duration"></td>
                </tr>
                </tbody>
            </table>
    		</div>

    	</body>
</html>