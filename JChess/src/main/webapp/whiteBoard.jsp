<html>
    <head>
        <title>Game page</title>
        <script src="//code.jquery.com/jquery-1.12.0.min.js"></script>
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="/jchess/js/chess_board_whitePlayer.js"></script>
        <link rel="stylesheet" href="css/style.css">
        <link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet" >
        <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" rel="stylesheet">



    </head>
    <body onload='draw();'>
       		<div  class="row">
    		    <div class="col-sm-3">
    		    <img src="img/logo.png" class ="im" width="200" height="200">
    		    <img src="img/logo2.png" class ="im" width="200" height="200">
    		    </div>
    		    <div>
                    <div id="Board" class="pull-left">
                        <canvas id="chess" width="800" height="800" border="0"></canvas>
                        <div>
                        <img id="abc" src="img/abc.png" width="800" height="30" class="abc">
                        </div>
                    </div>

                </div>
                <div class="pull-left" width="30" >
                 <img id="1234" src="img/123.png" width="30" height="800" class="num">
                </div>
    		    <div class="col-md-3">
                    <table id="table" class="table table-striped">
                    <thead><tr><th>White</th><th>Black</th></tr></thead>
                    <tbody>

                      </tbody>
                    </table>



                      <!-- Modal -->
                      <div class="modal fade" id="myModal" role="dialog">
                        <div class="modal-dialog">

                          <!-- Modal content-->
                          <div class="modal-content">
                            <div class="modal-header">
                              <button type="button" class="close" data-dismiss="modal">&times;</button>
                              <h4 class=" title modal-title congrat-title">Game over</h4>
                            </div>
                            <div class="modal-body">
                              <p id="endText" class="congrat">You win or you lose. TODO - JS</p>
                            </div>
                            <div class="modal-footer button-container">
                              <a  href="main.jsp"><button class = "congrat-btn" type="button"><span>Go to main menu</span></button></a>
                            </div>
                          </div>

                        </div>
                      </div>
                </div>
            </div>

    </body>
</html>