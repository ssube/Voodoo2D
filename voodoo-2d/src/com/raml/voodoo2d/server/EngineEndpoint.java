package com.raml.voodoo2d.server;

import com.raml.voodoo2d.Engine;
import com.raml.voodoo2d.JsonResponse;
import com.raml.voodoo2d.JsonResponse.ResponseStatus;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EngineEndpoint extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        // return engine info

        JsonResponse response = new JsonResponse(Engine.getInstance());
        response.respondWithJSON(resp);
    }
}
