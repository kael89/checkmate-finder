package org.kkarvounis.checkmate.app.servlets;

import org.kkarvounis.checkmate.app.api.GetTreeApi;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(
        name = "GetTreeServlet",
        urlPatterns = {"/api/get-tree"}
)
public class GetTreeServlet extends AbstractServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HashMap<String, String> input = new HashMap<String, String>() {{
            put("type", request.getParameter("type"));
            put("board", request.getParameter("board"));
            put("startingColor", request.getParameter("startingColor"));
            put("depth", request.getParameter("depth"));
        }};

        HashMap<String, Object> output= (new GetTreeApi()).run(input);
        jsonOut(response, output);
    }
}
