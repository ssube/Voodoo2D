package com.raml.voodoo2d.server;

import com.raml.voodoo2d.Engine;
import com.raml.voodoo2d.JsonResponse;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Endpoint returning usually-static engine data. This is used by the client engine to get basic
 * information during bootstrapping and verify the existence of the server.
 * 
 * @author Sean
 */
public class EngineEndpoint extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        JsonResponse response = new JsonResponse(Engine.instance);
        response.respondWithJSON(resp);
    }
}
