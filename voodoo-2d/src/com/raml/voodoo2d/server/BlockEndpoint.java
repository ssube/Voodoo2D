package com.raml.voodoo2d.server;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raml.voodoo2d.BlockType;
import com.raml.voodoo2d.ObjectMapperWrapper;
import com.raml.voodoo2d.PersistenceFactoryWrapper;
import com.raml.voodoo2d.JsonResponse;
import com.raml.voodoo2d.JsonResponse.ResponseStatus;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class BlockEndpoint extends HttpServlet
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        // return valid blocks
        PersistenceManager pm = PersistenceFactoryWrapper.get().getPersistenceManager();
        JsonResponse response;
        
        if (req.getParameter("example") != null)
        {
            response = new JsonResponse(ResponseStatus.Success, BlockType.example);
        }
        else
        {            
            try
            {
                String blockEnv = req.getParameter("environment");
                
                Query q = pm.newQuery(BlockType.class);
                if (blockEnv != null && !blockEnv.isEmpty())
                {
                    q.setFilter("environment ==  blockEnv");
                    q.declareParameters("String blockEnv");
                }
                
                @SuppressWarnings("unchecked")
                List<BlockType> results = (List<BlockType>) q.execute(blockEnv);
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
            BlockType newType = mapper.readValue(req.getInputStream(), BlockType.class);
            
            System.out.println("BlockType.Sprite: " + newType.getSprite());
            
            pm.makePersistent(newType);
            
            System.out.println("BlockType.Sprite: " + newType.getSprite());
            
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
            BlockType newType = mapper.readValue(req.getInputStream(), BlockType.class);
            
            BlockType oldType = pm.getObjectById(BlockType.class, newType.getId());
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
