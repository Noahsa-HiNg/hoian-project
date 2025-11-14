package com.hoianweb.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/detail/*")
public class DetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo(); // e.g. "/banh-mi-phuong"

        if (pathInfo == null || !pathInfo.matches("^/[a-zA-Z0-9-]+$")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String slug = pathInfo.substring(1); // remove "/"
        req.setAttribute("slug", slug);

        // FORWARD to protected JSP
        req.getRequestDispatcher("/WEB-INF/views/place.jsp").forward(req, resp);
    }
}