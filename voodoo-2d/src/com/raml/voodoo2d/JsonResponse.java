package com.raml.voodoo2d;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.*;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResponse
{
    public enum ResponseStatus
    {
        None(0),
        Success(200),
        Error(400);
        
        private int value;
        
        private ResponseStatus(int value)
        {
            this.value = value;
        }
        
        @JsonValue
        public int value()
        {
            return this.value;
        }
    }
    
    private static ObjectMapper mapper = new ObjectMapper();
    private static JsonResponse serializationError = new JsonResponse(ResponseStatus.Error, "unable to serialize JsonResponse");
    private static JsonResponse ioError = new JsonResponse(ResponseStatus.Error, "unable to access object for serialization");
    
    @JsonProperty
    ResponseStatus status;
    @JsonProperty
    String error;    
    @JsonProperty
    Object payload;
    
    public JsonResponse()
    {
        this.status = ResponseStatus.None;
    }
    
    public JsonResponse(ResponseStatus status, String payload)
    {
        this.status = status;
        if (status == ResponseStatus.Error)
        {
            this.error = payload;
        }
        else
        {
            this.payload = payload;            
        }
    }
    
    public JsonResponse(ResponseStatus status, Object payload)
    {
        this.status = status;
        this.payload = payload;
    }
    
    public JsonResponse(Object payload)
    {
        this.status = ResponseStatus.Success;
        this.payload = payload;
    }
    
    public String toJSON()
    {  
        String json = null;
        
        try
        {
            json = mapper.writeValueAsString(this);
        }
        catch (JsonMappingException exc)
        {
            if (this != serializationError)
            {
                json = serializationError.toJSON();
            }
        }
        catch (JsonGenerationException exc)
        {
            if (this != serializationError)
            {
                json = serializationError.toJSON();
            }
        }
        catch (IOException exc)
        {
            if (this != ioError)
            {
                json = ioError.toJSON();
            }
        }
        
        return json;          
    }
    
    public void respondWithJSON(HttpServletResponse response)
    {        
        try
        {
            response.getWriter().append(this.toJSON());
            response.setStatus(this.status.value);
            response.setContentType("application/json");
        }
        catch (IOException e)
        {
            response.setStatus(400);
        } 
    }
}
