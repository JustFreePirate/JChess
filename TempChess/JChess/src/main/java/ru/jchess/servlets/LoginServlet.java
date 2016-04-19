package ru.jchess.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.jchess.model.SignInRequestBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dima on 09.04.16.
 */
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        Map<String,String> headers = getHeadersInfo(request);


        for (Map.Entry<String,String> entry : headers.entrySet()) {
            writer.println(entry.getKey() + ": " + entry.getValue());
        }
        writer.println("\n\n");

//        StringBuffer jb = new StringBuffer();
//        String line = null;
//        try {
//            BufferedReader reader = request.getReader();
//            while ((line = reader.readLine()) != null)
//                jb.append(line);
//        } catch (Exception e) { writer.println("error reading"); }
//
//        Gson gson = new Gson();
//
//        writer.println(jb.toString());
        //BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        SignInRequestBean myBean = new SignInRequestBean();
        myBean.setLogin("myLogin");
        myBean.setPassword("myPass");
        String json = gson.toJson(myBean);
        writer.println(json);



        try {
            //JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
            //SignInRequestBean SIRBean = gson.fromJson(request.getReader(), SignInRequestBean.class);
            writer.println(request.getParameter("foo"));
            writer.println(request.getParameter("bar"));
            writer.println(request.getParameter("baz"));
        } catch (Exception e) {
            writer.println(e.getMessage());
        }
       // writer.println(SIRBean.toString());
        writer.close();
    }
    //get request headers
    private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                map.put(key, value);
            }
        }

        return map;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/SignInAndSignUp.jsp").forward(request,response);
    }
}
