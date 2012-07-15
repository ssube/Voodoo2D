package com.raml.voodoo2d.server;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raml.voodoo2d.JsonResponse;
import com.raml.voodoo2d.JsonResponse.ResponseStatus;
import com.raml.voodoo2d.ObjectMapperWrapper;
import com.raml.voodoo2d.PersistenceFactoryWrapper;
import com.raml.voodoo2d.Tileset;

public class TilesetEndpoint extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        // return valid blocks
        PersistenceManager pm = PersistenceFactoryWrapper.get().getPersistenceManager();
        JsonResponse response;
        
        if (req.getParameter("example") != null)
        {
            response = new JsonResponse(ResponseStatus.Success, Tileset.example);
        }
        else
        {            
            try
            {
                String setEnv = req.getParameter("environment");
                
                Query q = pm.newQuery(Tileset.class);
                if (setEnv != null && !setEnv.isEmpty())
                {
                    q.setFilter("environment == setEnv");
                    q.declareParameters("String setEnv");
                }
                
                @SuppressWarnings("unchecked")
                List<Tileset> results = (List<Tileset>) q.execute(setEnv);
                response = new JsonResponse(results);
            }
            catch (Throwable exc)
            {
                response = new JsonResponse(ResponseStatus.Error, exc.getMessage());
            }            
        }
        
        response.respondWithJSON(resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        PersistenceManager pm = PersistenceFactoryWrapper.get().getPersistenceManager();
        ObjectMapper mapper = ObjectMapperWrapper.get();
        
        JsonResponse response;
        try
        {
            Tileset newType = mapper.readValue(req.getInputStream(), Tileset.class);
            
            pm.makePersistent(newType);
            
            response = new JsonResponse(ResponseStatus.Success, newType);
        }
        catch (JsonParseException exc)
        {
            response = new JsonResponse(ResponseStatus.Error, exc.getMessage());
        }
        catch (JsonMappingException exc)
        {
            response = new JsonResponse(ResponseStatus.Error, exc.getMessage());
        }
        catch (IOException exc)
        {
            response = new JsonResponse(ResponseStatus.Error, exc.getMessage());
        }
        
        response.respondWithJSON(resp);
    }

    public void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        // return valid blocks
        PersistenceManager pm = PersistenceFactoryWrapper.get().getPersistenceManager();
        ObjectMapper mapper = ObjectMapperWrapper.get();
        JsonResponse response;
                  
        try
        {
            Tileset newType = mapper.readValue(req.getInputStream(), Tileset.class);
            
            Tileset oldType = pm.getObjectById(Tileset.class, newType.getId());
            oldType.copy(newType);
            
            pm.close();
            
            response = new JsonResponse(ResponseStatus.Success, oldType);
        }
        catch (JsonParseException exc)
        {
            response = new JsonResponse(ResponseStatus.Error, exc.getMessage());
        }
        catch (JsonMappingException exc)
        {
            response = new JsonResponse(ResponseStatus.Error, exc.getMessage());
        }
        catch (IOException exc)
        {
            response = new JsonResponse(ResponseStatus.Error, exc.getMessage());
        } 
        
        response.respondWithJSON(resp);
    }
}