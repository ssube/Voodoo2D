package com.raml.voodoo2d.common;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException()
    {
        super();
    }

    public ResourceNotFoundException(String message)
    {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
