package com.raml.voodoo2d;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityFactoryWrapper
{
    private static final EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory("transactions-optional");
    
    private EntityFactoryWrapper()
    {
        // nop
    }
    
    public static EntityManagerFactory get()
    {
        return emfInstance;
    }
}
