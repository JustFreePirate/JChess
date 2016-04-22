package ru.jchess.servlets;

import Util.Color;
import Util.Game;
import Util.Move;
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
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            Writer writer = response.getWriter();
            GameContainer gameContainer = MainServlet.getCurrentGameContainer(request.getSession());
            if (gameContainer != null) { //Если игрок сейчас играет партию
                Game game = gameContainer.getGame();
                String action = request.getParameter("action");
                if (WAIT_OPPONENT_MOVE.equals(action)) {
                    Object myMonitor = gameContainer.getMyMonitor();
                    //ждем хода оппонента
                    Color myColor = gameContainer.getMyColor();
                    synchronized (myMonitor) {
                        while (myColor != game.getColor()) { //пока мой цвет не станет цветом того, кто должен ходить
                            try {
                                myMonitor.wait();
                            } catch (InterruptedException e) {}
                        }
                        Move move = game.getLastMove();
                        String responseAction;
                        if (game.isCheckmate()) {
                            responseAction = CHECKMATE;
                        } if (game.isDraw()) {
                            responseAction = DRAW;
                        } else {
                            responseAction = MOVE;
                        }
                        JsonPackMove jsonPackMove = new JsonPackMove(move,responseAction);
                        writer.write(new Gson().toJson(jsonPackMove));
                        writer.close();
                    }
                } else if (MOVE.equals(action)) {
                    //Проверим наш ли сейчас ход

                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
