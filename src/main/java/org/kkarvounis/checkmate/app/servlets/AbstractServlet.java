package org.kkarvounis.checkmate.app.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class AbstractServlet extends HttpServlet {
    static void jsonOut(HttpServletResponse response, HashMap<String, Object> output) throws IOException {
        // TODO remove cors allowance in production?
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonOut = (new ObjectMapper()).writeValueAsString(output);
        PrintWriter writer = response.getWriter();
        writer.print(jsonOut);
        writer.flush();
    }
}
