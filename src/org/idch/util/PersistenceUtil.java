package org.idch.util;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Bootstrapping class for obtaining references to <tt>EntityManagerFactories</tt> and for 
 * making sure they are all shut down when the application ends. In a J2EE environment, 
 * this functionality is provided by the application server, but in a J2EE environment, the 
 * application is responsible for obtaining its own factory instances. This is an expensive 
 * process and should be done only once for each persistence context.    
 *    
 * @author Neal Audenaert
 */
public class PersistenceUtil {
    
    private static final Map<String, EntityManagerFactory> factories = 
    	new HashMap<String, EntityManagerFactory>();

    /**
     * Obtains an <tt>EntityManagerFactory</tt> for a persistence used defined in the
     * <tt>persistence.xml</tt> file.
     *  
     * @param persistenceUnit The persistence unit to retrieve
     * @return The desired <tt>EntityManagerFactory</tt>
     */
    public static EntityManagerFactory getEMFactory(String persistenceUnit) {
    	EntityManagerFactory emf = null;
    	synchronized (factories) {
    		emf = factories.get(persistenceUnit);
    		if (emf == null) {
    			emf = Persistence.createEntityManagerFactory(persistenceUnit);
    			
    			if (emf != null)
    				factories.put(persistenceUnit, emf);
    		}
    	}
    	
    	return emf;
    }
    
    /**
     * Closes all factories that were loaded by an application.
     */
    public static void shutdown() {
        // closes caches and connection pools
        synchronized (factories) {
        	for (String pUnit : factories.keySet()) {
        		try {
        			factories.get(pUnit).close();
        		} catch (Throwable err) {
        			// TODO Use logging system
        			System.err.println("Could not close EM factory (" + pUnit + "): " + 
        					err.getLocalizedMessage());
        			err.printStackTrace();
        		}
        	}
        }
    }
}

