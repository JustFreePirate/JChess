package ru.jchess.servlets;

import Util.Game;
import ru.jchess.model.GameContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static ru.jchess.model.Constants.*;

/**
 * Created by dima on 09.04.16.
 */
public class GameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        if (ajax && LoginServlet.isSignedIn(request)) {
            GameContainer gameContainer = MainServlet.getCurrentGameContainer(request.getSession());
            if (gameContainer != null) { //Если игрок сейчас играет партию
                Game game = gameContainer.getGame();
                String action = request.getParameter("action");
                if (WAIT_OPPONENT_MOVE.equals(action)) {
                    Object myMonitor = gameContainer.getMyMonitor();
                    //Проверим вдруг оппонент уже сходил

                    //оппонент еще не сходил, ожидаем
                    synchronized (myMonitor) {
                        while () { //TODO

                        }
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
