package ru.jchess.servlets;

import Util.Game;
import ru.jchess.model.GameContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dima on 09.04.16.
 */
public class GameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        if (ajax && LoginServlet.isSignedIn(request)) {
            GameContainer gameContainer = MainServlet.getCurrentGameContainer(request.getSession());
            if (gameContainer != null) {
                Game game = gameContainer.getGame();

            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
