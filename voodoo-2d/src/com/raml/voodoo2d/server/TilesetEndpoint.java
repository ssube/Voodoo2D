package com.raml.voodoo2d.server;

import com.raml.voodoo2d.Tileset;
import com.raml.voodoo2d.JsonResponse;
import com.raml.voodoo2d.JsonResponse.ResponseStatus;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TilesetEndpoint extends HttpServlet
{    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        String idStr = req.getParameter("id");
        JsonResponse response = null;
        
        // If no id given, list tilesets
        if (idStr == null || idStr.isEmpty())
        {
            response = new JsonResponse(new Tileset());
        }
        else
        {
            try
            {
                int id = Integer.parseInt(idStr);
                response = new JsonResponse(new Tileset(id));
            }
            catch (NumberFormatException exc)
            {
                response = new JsonResponse(ResponseStatus.Error, exc.getMessage());
            }
        }
        
        response.respondWithJSON(resp);     
    }
}
