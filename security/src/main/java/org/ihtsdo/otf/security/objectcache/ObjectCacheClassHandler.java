package org.ihtsdo.otf.security.objectcache;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class to create objects into the Object Cache.
 * 
 * @author Adam Flinton
 * 
 */
public final class ObjectCacheClassHandler {

    /** The Constant LOG. */
    private static final Logger LOG = Logger
            .getLogger(ObjectCacheClassHandler.class.getName());

    /**
     * Private constructor.
     */
    private ObjectCacheClassHandler() {
        super();
    }

    /**
     * Returns a object created from it's classname.
     * 
     * @param classname
     *            The name of the class.
     * @return The object.
     */
    public static Object getInstClass(final String classname) {
        Object obj = null;

        if (ObjectCache.INSTANCE.get(classname) != null) {
            // log.error("Classname found in cache "+Classname);
            obj = (Object) ObjectCache.INSTANCE.get(classname);
        }

        if (ObjectCache.INSTANCE.get(classname) == null) {
            // log.error("Classname not found in cache "+Classname);
            obj = getNewInstClass(classname);
            ObjectCache.INSTANCE.put(classname, obj);
        }
        return obj;
    }

    /**
     * Gets a new Instance of a given class
     **/

    public static Object getNewInstClass(final String classname) {
        Object obj = null;
        try {
            obj = instantiateClass(classname);
            checkState(classname, obj);
        } catch (Exception excep) {
            LOG.log(Level.SEVERE, "Error instantiating a class called "
                    + classname, excep);
        }
        return obj;
    }

    /**
     * Instantiates a class from it's (String) class name.
     * 
     * @param className
     *            The class name of the object to be created.
     * @return The object created or null if fails.
     * @throws ClassNotFoundException
     *             If the class is not found/
     * @throws IllegalAccessException
     *             If an illegal access.
     * @throws InstantiationException
     *             If there was a problem instantiating the class.
     */
    private static Object instantiateClass(final String className)
            throws ClassNotFoundException, IllegalAccessException,
            InstantiationException {
        // AceLog.getAppLog().info("instantiateClass called className = "
        // + className);
        if (className == null) {
            return null;
        }
        return Class.forName(className).newInstance();
    }

    /**
     * Checks to see that a class has been loaded correctly.
     * 
     * @param interfaceName
     *            The name of the interface
     * @param interfaceObject
     *            The object to be checked against the interface.
     */
    private static void checkState(final String interfaceName,
            final Object interfaceObject) {
        if (interfaceObject == null) {
            throw new IllegalStateException("Name of class that implements "
                    + interfaceName + " not set.");
        }
    }

}
