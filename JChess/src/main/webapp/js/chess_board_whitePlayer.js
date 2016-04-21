var NUMBER_OF_COLS = 8,
    NUMBER_OF_ROWS = 8,
    BLOCK_SIZE = 100;
var HD_HEIGHT = 1080;


var BLOCK_COLOUR_1 = '#f0d9b5',
    BLOCK_COLOUR_2 = '#b58863',
    HIGHLIGHT_COLOUR = '#bbd26b';

var canvasCoef = 1;
var piecePositions = null;
var answer;  // TODO: replace to JSON

var PIECE_PAWN = 0,
    temp = 0,
    PIECE_CASTLE = 1,
    PIECE_ROUKE = 2,
    PIECE_BISHOP = 3,
    PIECE_QUEEN = 4,
    PIECE_KING = 5,
    IN_PLAY = 0,
    TAKEN = 1,
    pieces = null,
    ctx = null,
    json = null,
    canvas = null,
    BLACK_TEAM = 0,
    WHITE_TEAM = 1,
    SELECT_LINE_WIDTH = 3,
    currentTurn = WHITE_TEAM,
    selectedPiece = null;

function convertToBadCoordinate(coordinate) {
    if (coordinate[0] === 'H') return getPieceAtBlock({row: 8 - coordinate[1], col: 7}, json.white);
    if (coordinate[0] === 'G') return getPieceAtBlock({row: 8 - coordinate[1], col: 6}, json.white);
    if (coordinate[0] === 'F') return getPieceAtBlock({row: 8 - coordinate[1], col: 5}, json.white);
    if (coordinate[0] === 'E') return getPieceAtBlock({row: 8 - coordinate[1], col: 4}, json.white);
    if (coordinate[0] === 'D') return getPieceAtBlock({row: 8 - coordinate[1], col: 3}, json.white);
    if (coordinate[0] === 'C') return getPieceAtBlock({row: 8 - coordinate[1], col: 2}, json.white);
    if (coordinate[0] === 'B') return getPieceAtBlock({row: 8 - coordinate[1], col: 1}, json.white);
    if (coordinate[0] === 'A') return getPieceAtBlock({row: 8 - coordinate[1], col: 0}, json.white);
}


function convertToStdCoordinate(coordinate) {
    if (coordinate.col === 7) return 'H' + (8 - coordinate.row);
    if (coordinate.col === 6) return 'G' + (8 - coordinate.row);
    if (coordinate.col === 5) return 'F' + (8 - coordinate.row);
    if (coordinate.col === 4) return 'E' + (8 - coordinate.row);
    if (coordinate.col === 3) return 'D' + (8 - coordinate.row);
    if (coordinate.col === 2) return 'C' + (8 - coordinate.row);
    if (coordinate.col === 1) return 'B' + (8 - coordinate.row);
    if (coordinate.col === 0) return 'A' + (8 - coordinate.row);
}

function sendToServer(json) {
    $.post('game', json, function (data) {
        answer = data;
    })
}

function screenToBlock(x, y) {
    var block = {
        "row": Math.floor(y / BLOCK_SIZE),
        "col": Math.floor(x / BLOCK_SIZE)
    };

    return block;
}

function getPieceAtBlockForTeam(teamOfPieces, clickedBlock) {

    var curPiece = null,
        iPieceCounter = 0,
        pieceAtBlock = null;

    for (iPieceCounter = 0; iPieceCounter < teamOfPieces.length; iPieceCounter++) {

        curPiece = teamOfPieces[iPieceCounter];

        if (curPiece.status === IN_PLAY &&
            curPiece.col === clickedBlock.col &&
            curPiece.row === clickedBlock.row) {
            curPiece.position = iPieceCounter;

            pieceAtBlock = curPiece;
            iPieceCounter = teamOfPieces.length;
        }
    }

    return pieceAtBlock;
}

function blockOccupiedByEnemy(clickedBlock, team) {
    return getPieceAtBlockForTeam(team, clickedBlock);
}


function blockOccupied(clickedBlock) {
    return (getPieceAtBlockForTeam(json.white, clickedBlock) !== null);
}


function canSelectedMoveToBlock(selectedPiece, clickedBlock, enemyPiece) {

    var jsonToServer;
    jsonToServer = {
        action: "move",
        from: convertToStdCoordinate(selectedPiece),
        to: convertToStdCoordinate(clickedBlock)
    }
    //sendToServer(jsonToServer);
    answer = 'checkmate';
    if (answer === 'correct' || answer === 'check' || answer === 'checkmate') {
        return (true);
    }


}

function getPieceAtBlock(clickedBlock, team) {


    return getPieceAtBlockForTeam(team, clickedBlock);
}

function getBlockColour(iRowCounter, iBlockCounter) {
    var cStartColour;

    // Alternate the block colour
    if (iRowCounter % 2) {
        cStartColour = (iBlockCounter % 2 ? BLOCK_COLOUR_1 : BLOCK_COLOUR_2);
    } else {
        cStartColour = (iBlockCounter % 2 ? BLOCK_COLOUR_2 : BLOCK_COLOUR_1);
    }

    return cStartColour;
}

function drawBlock(iRowCounter, iBlockCounter) {
    // Set the background
    ctx.fillStyle = getBlockColour(iRowCounter, iBlockCounter);


    // Draw rectangle for the background
    ctx.fillRect(iRowCounter * BLOCK_SIZE, iBlockCounter * BLOCK_SIZE,
        BLOCK_SIZE, BLOCK_SIZE);
    ctx.lineWidth = 3;
    ctx.strokeStyle = "#000000";
    ctx.strokeRect(0, 0,
        NUMBER_OF_ROWS * BLOCK_SIZE,
        NUMBER_OF_COLS * BLOCK_SIZE);

    ctx.stroke();
}

function getImageCoords(pieceCode, bBlackTeam) {

    var imageCoords = {
        "x": pieceCode * 100,
        "y": (bBlackTeam ? 0 : 100)
    };

    return imageCoords;
}

function drawPiece(curPiece, bBlackTeam) {

    var imageCoords = getImageCoords(curPiece.piece, bBlackTeam);

    // Draw the piece onto the canvas
    ctx.drawImage(pieces,
        imageCoords.x, imageCoords.y,
        100, 100,
        curPiece.col * BLOCK_SIZE, curPiece.row * BLOCK_SIZE,
        BLOCK_SIZE, BLOCK_SIZE);
}

function removeSelection(selectedPiece) {
    drawBlock(selectedPiece.col, selectedPiece.row);
    drawPiece(selectedPiece, false);
}

function drawTeamOfPieces(teamOfPieces, bBlackTeam) {
    var iPieceCounter;

    // Loop through each piece and draw it on the canvas
    for (iPieceCounter = 0; iPieceCounter < teamOfPieces.length; iPieceCounter++) {
        drawPiece(teamOfPieces[iPieceCounter], bBlackTeam);
    }
}

function drawPieces() {
    drawTeamOfPieces(json.black, true);
    drawTeamOfPieces(json.white, false);
}

function drawRow(iRowCounter) {
    var iBlockCounter;

    // Draw 8 block left to right
    for (iBlockCounter = 0; iBlockCounter < NUMBER_OF_ROWS; iBlockCounter++) {
        drawBlock(iRowCounter, iBlockCounter);
    }
}

function drawBoard() {
    var iRowCounter;

    for (iRowCounter = 0; iRowCounter < NUMBER_OF_ROWS; iRowCounter++) {
        drawRow(iRowCounter);
    }

    // Draw outline
    ctx.lineWidth = 3;
    ctx.strokeRect(0, 0,
        NUMBER_OF_ROWS * BLOCK_SIZE,
        NUMBER_OF_COLS * BLOCK_SIZE);
}

function defaultPositions() {
    json = {
        "black": [
            {
                "piece": PIECE_CASTLE,
                "row": 0,
                "col": 0,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_ROUKE,
                "row": 0,
                "col": 1,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_BISHOP,
                "row": 0,
                "col": 2,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_QUEEN,
                "row": 0,
                "col": 3,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_KING,
                "row": 0,
                "col": 4,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_BISHOP,
                "row": 0,
                "col": 5,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_ROUKE,
                "row": 0,
                "col": 6,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_CASTLE,
                "row": 0,
                "col": 7,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 1,
                "col": 0,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 1,
                "col": 1,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 1,
                "col": 2,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 1,
                "col": 3,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 1,
                "col": 4,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 1,
                "col": 5,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 1,
                "col": 6,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 1,
                "col": 7,
                "status": IN_PLAY
            }
        ],
        "white": [
            {
                "piece": PIECE_CASTLE,
                "row": 7,
                "col": 0,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_ROUKE,
                "row": 7,
                "col": 1,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_BISHOP,
                "row": 7,
                "col": 2,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_QUEEN,
                "row": 7,
                "col": 3,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_KING,
                "row": 7,
                "col": 4,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_BISHOP,
                "row": 7,
                "col": 5,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_ROUKE,
                "row": 7,
                "col": 6,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_CASTLE,
                "row": 7,
                "col": 7,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 6,
                "col": 0,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 6,
                "col": 1,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 6,
                "col": 2,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 6,
                "col": 3,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 6,
                "col": 4,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 6,
                "col": 5,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 6,
                "col": 6,
                "status": IN_PLAY
            },
            {
                "piece": PIECE_PAWN,
                "row": 6,
                "col": 7,
                "status": IN_PLAY
            }
        ]
    };
}

function selectPiece(pieceAtBlock) {
    // Draw outline
    ctx.lineWidth = SELECT_LINE_WIDTH;
    ctx.strokeStyle = HIGHLIGHT_COLOUR;
    ctx.strokeRect((pieceAtBlock.col * BLOCK_SIZE) + SELECT_LINE_WIDTH,
        (pieceAtBlock.row * BLOCK_SIZE) + SELECT_LINE_WIDTH,
        BLOCK_SIZE - (SELECT_LINE_WIDTH * 2),
        BLOCK_SIZE - (SELECT_LINE_WIDTH * 2));

    selectedPiece = pieceAtBlock;
}

function checkIfPieceClicked(clickedBlock, team) {
    var pieceAtBlock = getPieceAtBlock(clickedBlock, team);

    if (pieceAtBlock !== null) {
        selectPiece(pieceAtBlock);
    }
}

function movePiece(clickedBlock, enemyPiece) {
    // Clear the block in the original position

    drawBlock(selectedPiece.col, selectedPiece.row);

    var team = json.white,
        opposite = json.black;


    team[selectedPiece.position].col = clickedBlock.col;
    team[selectedPiece.position].row = clickedBlock.row;

    if (enemyPiece !== null) {
        // Clear the piece your about to take
        drawBlock(enemyPiece.col, enemyPiece.row);
        opposite[enemyPiece.position].status = TAKEN;
    }
    selectedPiece.col = clickedBlock.col;
    selectedPiece.row = clickedBlock.row;

    // Draw the piece in the new position
    drawPiece(selectedPiece, BLACK_TEAM);


    selectedPiece = null;
}

function movePieceForEnemy(clickedBlock, enemyPiece) {

    drawBlock(selectedPiece.col, selectedPiece.row);

    var team = json.black,
        opposite = json.white;

    team[selectedPiece.position].col = clickedBlock.col;
    team[selectedPiece.position].row = clickedBlock.row;

    if (enemyPiece !== null) {
        // Clear the piece your about to take
        drawBlock(enemyPiece.col, enemyPiece.row);
        opposite[enemyPiece.position].status = TAKEN;
    }
    selectedPiece.col = clickedBlock.col;
    selectedPiece.row = clickedBlock.row;
    // Draw the piece in the new position
    drawPiece(selectedPiece, WHITE_TEAM);


    selectedPiece = null;

}


function processMove(clickedBlock) {
    var pieceAtBlock = getPieceAtBlock(clickedBlock, json.white),
        enemyPiece = blockOccupiedByEnemy(clickedBlock, json.black);

    if (pieceAtBlock !== null) {
        removeSelection(selectedPiece);
        checkIfPieceClicked(clickedBlock, json.white);
    } else if (canSelectedMoveToBlock(selectedPiece, clickedBlock, enemyPiece) === true) {
       
        if ((selectedPiece.piece === PIECE_PAWN) && Math.abs(selectedPiece.col - clickedBlock.col) === 1
            && Math.abs(selectedPiece.row - clickedBlock.row) === 1 && enemyPiece === null) {
            addToTable(convertToStdCoordinate(selectedPiece),
                convertToStdCoordinate(clickedBlock), 'white');
            movePiece(clickedBlock, enemyPiece);
            clickedBlock.row += 1;
            enemyPiece = blockOccupiedByEnemy(clickedBlock, json.black);
            drawBlock(enemyPiece.col, enemyPiece.row);
            json.black[enemyPiece.position].status = TAKEN;
        }
        if (selectedPiece.piece === PIECE_KING && Math.abs(selectedPiece.col - clickedBlock.col) === 2) {
            if (selectedPiece.col - clickedBlock.col === 2) {
                addToTable('0-0-0', '', 'white');
                longCastling(clickedBlock, enemyPiece);
            } else {
                addToTable('0-0', '', 'white');
                shortCastling(clickedBlock, enemyPiece);
            }
        } else {
            addToTable(convertToStdCoordinate(selectedPiece),
                convertToStdCoordinate(clickedBlock), 'white');
            movePiece(clickedBlock, enemyPiece);

        }
        if (answer === 'checkmate') {
            ctx.lineWidth = SELECT_LINE_WIDTH;
            ctx.strokeStyle = '#ff0000';
            ctx.strokeRect((json.black[4].col * BLOCK_SIZE) + SELECT_LINE_WIDTH,
                (json.black[4].row * BLOCK_SIZE) + SELECT_LINE_WIDTH,
                BLOCK_SIZE - (SELECT_LINE_WIDTH * 2),
                BLOCK_SIZE - (SELECT_LINE_WIDTH * 2));
            endGame(true);
        } else {
            if (answer === 'check') {
                ctx.lineWidth = SELECT_LINE_WIDTH;
                ctx.strokeStyle = '#ff0000';
                ctx.strokeRect((json.black[4].col * BLOCK_SIZE) + SELECT_LINE_WIDTH,
                    (json.black[4].row * BLOCK_SIZE) + SELECT_LINE_WIDTH,
                    BLOCK_SIZE - (SELECT_LINE_WIDTH * 2),
                    BLOCK_SIZE - (SELECT_LINE_WIDTH * 2));
            }
            answer = '';
            currentTurn = BLACK_TEAM;
            WaitingEnemyMove();
        }
    }
}

function shortCastling(clickedBlock, enemyPiece) {
    movePiece(clickedBlock, enemyPiece);
    clickedBlock.col = clickedBlock.col - 1;
    selectedPiece = {
        piece: PIECE_CASTLE,
        row: 7,
        col: 7,
        status: IN_PLAY,
        position: 7
    }
    movePiece(clickedBlock, enemyPiece);
}

function longCastling(clickedBlock, enemyPiece) {
    movePiece(clickedBlock, enemyPiece);
    clickedBlock.col = clickedBlock.col + 1;
    selectedPiece = {
        piece: PIECE_CASTLE,
        row: 7,
        col: 0,
        status: IN_PLAY,
        position: 0
    }
    movePiece(clickedBlock, enemyPiece);
}


function processMoveForEnemy(clickedBlock) {
    var enemyPiece = blockOccupiedByEnemy(clickedBlock, json.white);

    if ((selectedPiece.piece === PIECE_PAWN) && Math.abs(selectedPiece.col - clickedBlock.col) === 1
        && Math.abs(selectedPiece.row - clickedBlock.row) === 1 && enemyPiece === null) {
        addToTable(convertToStdCoordinate(selectedPiece),
            convertToStdCoordinate(clickedBlock), 'black');
        movePiece(clickedBlock, enemyPiece);
        clickedBlock.row -= 1;
        enemyPiece = blockOccupiedByEnemy(clickedBlock, json.black);
        drawBlock(enemyPiece.col, enemyPiece.row);
        json.white[enemyPiece.position].status = TAKEN;
    }
    if (selectedPiece.piece === PIECE_KING && Math.abs(selectedPiece.col - clickedBlock.col) === 2) {
        if (selectedPiece.col - clickedBlock.col === 2) {
            addToTable('0-0-0', '', 'black');
            longCastlingForEnemy(clickedBlock, enemyPiece);
        } else {
            addToTable('0-0', '', 'black');
            shortCastlingForEnemy(clickedBlock, enemyPiece);
        }
    } else {
        addToTable(convertToStdCoordinate(selectedPiece), convertToStdCoordinate(clickedBlock), 'black');
        movePieceForEnemy(clickedBlock, enemyPiece);
    }
    currentTurn = WHITE_TEAM;
}

function shortCastlingForEnemy(clickedBlock, enemyPiece) {
    movePieceForEnemy(clickedBlock, enemyPiece);
    clickedBlock.col = clickedBlock.col - 1;
    selectedPiece = {
        piece: PIECE_CASTLE,
        row: 0,
        col: 7,
        status: IN_PLAY,
        position: 7
    }
    movePieceForEnemy(clickedBlock, enemyPiece);
}

function longCastlingForEnemy(clickedBlock, enemyPiece) {
    movePiece(clickedBlock, enemyPiece);
    clickedBlock.col = clickedBlock.col + 1;
    selectedPiece = {
        piece: PIECE_CASTLE,
        row: 0,
        col: 0,
        status: IN_PLAY,
        position: 0
    }
    movePiece(clickedBlock, enemyPiece);
}

function endGame(bool) {
    canvas.removeEventListener('click', board_click);
    var modal = document.getElementById('myModal');
    var text = document.getElementById('endText');
    if (bool) {
        jQuery.noConflict();
        text.innerHTML = 'Win!';
        modal.modal('show');
    } else {
        text.innerHTML = 'Lose =(';
        modal.modal("show");
    }
}

function WaitingEnemyMove() {
    canvas.removeEventListener('click', board_click);
    $.post('game', {action: 'getEnemyMove'}, function (data) {
        canvas.addEventListener('click', board_click, false);
        var move = JSON.parse(data);
        if (data.action === 'move') {
            selectedPiece = convertToBadCoordinate(move.from);
            ctx.lineWidth = SELECT_LINE_WIDTH;
            if ((move.from.row + move.from.col) % 2 === 1) {
                ctx.strokeStyle = '#b58863';
            } else {
                ctx.strokeStyle = '#f0d9b5';
            }
            ctx.strokeRect((json.black[4].col * BLOCK_SIZE) + SELECT_LINE_WIDTH,
                (json.black[4].row * BLOCK_SIZE) + SELECT_LINE_WIDTH,
                BLOCK_SIZE - (SELECT_LINE_WIDTH * 2),
                BLOCK_SIZE - (SELECT_LINE_WIDTH * 2));
            processMoveForEnemy(convertToBadCoordinate(move.to));

        } else {
            if (data.action === 'check') {
                ctx.lineWidth = SELECT_LINE_WIDTH;
                ctx.strokeStyle = '#ff0000';
                ctx.strokeRect((json.white[4].col * BLOCK_SIZE) + SELECT_LINE_WIDTH,
                    (json.white[4].row * BLOCK_SIZE) + SELECT_LINE_WIDTH,
                    BLOCK_SIZE - (SELECT_LINE_WIDTH * 2),
                    BLOCK_SIZE - (SELECT_LINE_WIDTH * 2));
                selectedPiece = convertToBadCoordinate(move.from);
                processMoveForEnemy(convertToBadCoordinate(move.to));
            } else {
                if (data.action === 'checkmate') {
                    ctx.lineWidth = SELECT_LINE_WIDTH;
                    ctx.strokeStyle = '#ff0000';
                    ctx.strokeRect((json.white[4].col * BLOCK_SIZE) + SELECT_LINE_WIDTH,
                        (json.white[4].row * BLOCK_SIZE) + SELECT_LINE_WIDTH,
                        BLOCK_SIZE - (SELECT_LINE_WIDTH * 2),
                        BLOCK_SIZE - (SELECT_LINE_WIDTH * 2));
                    endGame(false);

                }
            }
        }
    });


    /*if (temp === 2) {
     selectedPiece = {
     piece: PIECE_KING,
     row: 0,
     col: 4,
     position: 4,
     status: 0
     };
     var clickedBlock = {
     row: 0,
     col: 6
     }
     temp++;
     }
     if (temp === 1) {
     selectedPiece = {
     piece: PIECE_BISHOP,
     row: 0,
     col: 5,
     position: 5,
     status: 0
     };
     var clickedBlock = {
     row: 5,
     col: 5
     }
     temp++;
     }
     if (temp === 0) {
     selectedPiece = {
     piece: PIECE_ROUKE,
     row: 0,
     col: 6,
     position: 6,
     status: 0
     };
     var clickedBlock = {
     row: 6,
     col: 6
     }
     temp++;
     }*/

}

function board_click(ev) {
    var x = ev.clientX - canvas.offsetLeft,
        y = ev.clientY - canvas.offsetTop,
        clickedBlock = screenToBlock(x, y);


    if (selectedPiece === null) {
        checkIfPieceClicked(clickedBlock, json.white);
    } else {

        processMove(clickedBlock);
    }

}

function addToTable(selectedBlock, moveBlock, currentColor) {
    var table = document.getElementById('table').getElementsByTagName('tbody')[0];

    if (currentColor === 'white') {
        var row = table.insertRow(table.rows.length);
        var cell1 = row.insertCell(0);
        if (moveBlock != '')
            cell1.innerHTML = selectedBlock + ' - ' + moveBlock;
        else
            cell1.innerHTML = selectedBlock;
    }
    if (currentColor === 'black') {
        var row = table.rows.item(table.rows.length - 1);
        var cell2 = row.insertCell(1);
        if (moveBlock != '')
            cell2.innerHTML = selectedBlock + ' - ' + moveBlock;
        else
            cell2.innerHTML = selectedBlock;
    }
    document.getElementById('table').rows.item(table.rows.length - 1).scrollIntoView(true);


}

function draw() {
    // Main entry point got the HTML5 chess board example

    canvas = document.getElementById('chess');
    var ImageABC = document.getElementById('abc');
    var Image123 = document.getElementById('1234');

    // Canvas supported?
    if (canvas.getContext) {
        ctx = canvas.getContext('2d');

        canvasCoef = screen.availHeight / HD_HEIGHT;

        if (canvas.height > screen.availHeight) {
            canvas.height = canvas.height * canvasCoef;
            canvas.width = canvas.width * canvasCoef;

            ImageABC.height = ImageABC.height * (canvas.width / ImageABC.width);
            ImageABC.width = canvas.width;

            Image123.width = Image123.width * (canvas.height / Image123.height);
            Image123.height = canvas.height;
        }

        // Calculate the precise block size
        BLOCK_SIZE = canvas.height / NUMBER_OF_ROWS;

        // Draw the background
        drawBoard();

        defaultPositions();

        // Draw pieces
        pieces = new Image();
        pieces.src = 'pieces.png';
        pieces.onload = drawPieces;


        canvas.addEventListener('click', board_click, false);


    } else {
        alert("Canvas not supported!");
    }


}