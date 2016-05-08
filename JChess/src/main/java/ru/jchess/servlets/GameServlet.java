package ru.jchess.servlets;

import Util.*;
import com.google.gson.Gson;
import ru.jchess.model.GameContainer;
import ru.jchess.model.JsonPackMove;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

import static ru.jchess.model.Constants.*;

/**
 * Created by dima on 09.04.16.
 */
public class GameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        if (ajax && LoginServlet.isSignedIn(request)) {
            Writer writer = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            GameContainer gameContainer = MainServlet.getCurrentGameContainer(request.getSession());
            Color myColor = gameContainer.getMyColor();
            if (gameContainer != null) { //Если игрок сейчас играет партию
                Game game = gameContainer.getGame();
                String action = request.getParameter("action");
                if (WAIT_OPPONENT_MOVE.equals(action)) { //Ждем чужого хода
                    Object myMonitor = gameContainer.getMyMonitor();
                    //ждем хода оппонента
                    synchronized (myMonitor) {
                        while (myColor != game.getColor() && !game.isGameOver()) { //пока мой цвет не станет цветом того, кто должен ходить
                            try {
                                getServletContext().log("waiting"); //log
                                myMonitor.wait();
                                getServletContext().log("achieved move"); //log
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (game.isGameOver()) {
                            processionGameOver(request);
                        }
                        Move move = game.getLastMove();
                        String responseAction = getBoardState(game);
                        JsonPackMove jsonPackMove = new JsonPackMove(move, responseAction);
                        response.setContentType("application/json");
                        writer.write(new Gson().toJson(jsonPackMove));
                        writer.close();
                        getServletContext().log(new Gson().toJson(jsonPackMove)); //log
                    }
                } else if (MOVE.equals(action) || PROMOTION.equals(action)) { //делаем свой ход
                    String from = request.getParameter("from");
                    String to = request.getParameter("to");
                    Person person = getMyColorPerson(gameContainer.getMyColor());
                    Move move;
                    if (MOVE.equals(action)) {
                        move = Move.goFromTo(person, Cell.valueOf(from), Cell.valueOf(to));
                    } else {
                        move = Move.promotion(person, ChessPiece.valueOf(request.getParameter("chessPiece")));
                    }

                    //сформировали ход
                    String respString;
                    try {
                        game.doIt(move);
                        respString = getBoardState(game);
                        getServletContext().log(respString); //log
                    } catch (Exception e) {
                        //ход был некорректен
                        getServletContext().log(e.getMessage()); //log
                        respString = MOVE_NOT_CORRECT;
                    }
                    if (game.isGameOver()) {
                        processionGameOver(request);
                    }
                    writer.write(respString);
                    writer.close();
                    //сообщим оппоненту, что походили
                    synchronized (gameContainer.getOpponentMonitor()) {
                        if (respString != PROMOTION) { //не ожидаем ещё подтверждения фигуры
                            gameContainer.getOpponentMonitor().notify();
                        }
                    }
                } else {
                    getServletContext().log("unexpected action" + action); //log
                }
            } else {
                getServletContext().log("gameContainer == null"); //log
            }

        } else {
            getServletContext().log("not ajax or not signed in"); //log
        }
    }

    private String getBoardState(Game game) {
        String state;
        if (game.isCheckmate()) {
            state = CHECKMATE;
        } else if (game.isDraw()) {
            state = DRAW;
        } else if (game.checkCheck()) {
            state = CHECK;
        } else if (game.checkPawnOnTheEdge()) {
            state = PROMOTION;
        } else {
            state = MOVE;
        }

        return state;
    }

    private Person getMyColorPerson(Color color) {
        switch (color) {
            case BLACK:
                return BLACK_PERSON;
            case WHITE:
                return WHITE_PERSON;
            default:
                throw new RuntimeException("No person for this color");
        }
    }

    private void processionGameOver(HttpServletRequest request) {
        //TODO statistic
        MainServlet.setCurrentGameContainer(request.getSession(), null);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
