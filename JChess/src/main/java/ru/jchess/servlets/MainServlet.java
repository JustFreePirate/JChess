package ru.jchess.servlets;

import ru.jchess.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import static ru.jchess.model.Constants.*;

/**
 * Created by dima on 09.04.16.
 */
public class MainServlet extends HttpServlet {
    public static String ACTION_SEARCH = "search";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        if (ajax) {
            String action = request.getParameter("action");
            if (ACTION_SEARCH.equals(action)) {
                Writer writer = response.getWriter();
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {}
                writer.write("white");
                writer.close();
            }
        }
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
