package com.raml.voodoo2d.server;

import com.raml.voodoo2d.World;
import com.raml.voodoo2d.JsonResponse;
import com.raml.voodoo2d.World.WorldSize;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WorldEndpoint extends HttpServlet 
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        World world = new World(WorldSize.Demo);
        JsonResponse response = new JsonResponse(world);
        response.respondWithJSON(resp);       
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        Map<?, ?> params = req.getParameterMap();
        ObjectMapper mapper = new ObjectMapper();
        
        String json = mapper.writeValueAsString(params);
        
        resp.getWriter().append(json);
        resp.setStatus(200);
    }
}
