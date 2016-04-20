package ru.jchess.servlets;

import ru.jchess.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import static ru.jchess.model.Constants.*;

/**
 * Created by dima on 09.04.16.
 */
public class MainServlet extends HttpServlet {
    private final static String ACTION_SIGN_IN = "sign in";
    private final static String ACTION_SIGN_OUT = "sign out";


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//        HttpSession session = request.getSession();
//        String action = request.getParameter("action");
//
//
//        session.setAttribute("login", "Dima");
//        if (ACTION_SIGN_IN.equals(action)) {
//            String url = request.getContextPath() + "/login";
//            response.sendRedirect(url);
//        } else if (ACTION_SIGN_OUT.equals(action)) {
//            //do something to log out from session
//            //TODO
//            session.setAttribute("login", null);
//            request.getRequestDispatcher("/index.jsp").forward(request,response);
//        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //проверим залогинен ли. Если нет то посылаю на страницу логина
        if (isSignedIn(request)) {
            request.getRequestDispatcher("/main.jsp").forward(request, response);
        } else {
            response.sendRedirect("login");
        }

    }

    private boolean isSignedIn(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER_PROFILE);
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }
}
