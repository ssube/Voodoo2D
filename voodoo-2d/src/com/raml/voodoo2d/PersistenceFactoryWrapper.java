package com.raml.voodoo2d;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class PersistenceFactoryWrapper
{
    private static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
    
    private PersistenceFactoryWrapper()
    {
        // nop
    }
    
    public static PersistenceManagerFactory get()
    {
        return pmfInstance;
    }
}
