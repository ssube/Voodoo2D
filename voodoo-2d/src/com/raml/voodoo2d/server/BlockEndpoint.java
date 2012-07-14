package com.raml.voodoo2d.server;

import com.raml.voodoo2d.BlockType;
import com.raml.voodoo2d.EntityFactoryWrapper;
import com.raml.voodoo2d.JsonResponse;
import com.raml.voodoo2d.JsonResponse.ResponseStatus;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
        EntityManager em = EntityFactoryWrapper.get().createEntityManager();
        JsonResponse response;
        
        try
        {
            Query q = em.createQuery("select from " + BlockType.class.getName());
            List<?> results = q.getResultList();
            response = new JsonResponse(results);
        }
        catch (Throwable exc)
        {
            response = new JsonResponse(ResponseStatus.Error, exc.getMessage());
        }
        
        response.respondWithJSON(resp);
    }
}
