/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.wss4j.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Load resources (or images) from various sources.
 * <p/>
 */
public class Loader {
    private static final org.slf4j.Logger LOG =
            org.slf4j.LoggerFactory.getLogger(Loader.class);

    /**
     * This method will search for <code>resource</code> in different
     * places. The search order is as follows:
     * <ol>
     * <p><li>Search for <code>resource</code> using the thread context
     * class loader under Java2.
     * <p><li>Try one last time with
     * <code>ClassLoader.getSystemResource(resource)</code>, that is is
     * using the system class loader in JDK 1.2 and virtual machine's
     * built-in class loader in JDK 1.1.
     * </ol>
     * <p/>
     *
     * @param resource
     * @return the url to the resource or null if not found
     */
    public static URL getResource(String resource) {
        URL url = null;
        try {
            ClassLoader classLoader = getTCL();
            if (classLoader != null) {
                LOG.debug("Trying to find [" + resource + "] using " + classLoader + " class loader.");
                url = classLoader.getResource(resource);
                if (url == null && resource.startsWith("/")) {
                    //certain classloaders need it without the leading /
                    url = classLoader.getResource(resource.substring(1));
                }
                if (url != null) {
                    return url;
                } 
            }
        } catch (Exception e) {
            LOG.warn("Caught Exception while in Loader.getResource. This may be innocuous.", e);
        }
    
        ClassLoader cluClassloader = Loader.class.getClassLoader();
        if (cluClassloader == null) {
            cluClassloader = ClassLoader.getSystemClassLoader();
        }
        url = cluClassloader.getResource(resource);
        if (url == null && resource.startsWith("/")) {
            //certain classloaders need it without the leading /
            url = cluClassloader.getResource(resource.substring(1));
        }
        if (url != null) {
            return url;
        }
        
        // Last ditch attempt: get the resource from the class path. It
        // may be the case that clazz was loaded by the Extension class
        // loader which the parent of the system class loader. Hence the
        // code below.
        LOG.debug("Trying to find [" + resource + "] using ClassLoader.getSystemResource().");
        return ClassLoader.getSystemResource(resource);
    }


    /**
     * This method will search for <code>resource</code> in different
     * places. The search order is as follows:
     * <ol>
     * <p><li>Search for <code>resource</code> using the supplied class loader.
     * If that fails, search for <code>resource</code> using the thread context
     * class loader.
     * <p><li>Try one last time with
     * <code>ClassLoader.getSystemResource(resource)</code>, that is is
     * using the system class loader in JDK 1.2 and virtual machine's
     * built-in class loader in JDK 1.1.
     * </ol>
     * <p/>
     *
     * @param resource
     * @return the url to the resource or null if not found
     */
    public static URL getResource(ClassLoader loader, String resource) {
        URL url = null;
        try {
            if (loader != null) {
                LOG.debug("Trying to find [" + resource + "] using " + loader + " class loader.");
                url = loader.getResource(resource);
                if (url == null && resource.startsWith("/")) {
                    //certain classloaders need it without the leading /
                    url = loader.getResource(resource.substring(1));
                }
                if (url != null) {
                    return url;
                }
            }
        } catch (Exception e) {
            LOG.warn("Caught Exception while in Loader.getResource. This may be innocuous.", e);
        }
        return getResource(resource);
    }
    
    /**
     * This is a convenience method to load a resource as a stream. <p/> The
     * algorithm used to find the resource is given in getResource()
     * 
     * @param resourceName The name of the resource to load
     */
    public static InputStream getResourceAsStream(String resourceName) {
        URL url = getResource(resourceName);

        try {
            return (url != null) ? url.openStream() : null;
        } catch (IOException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e.getMessage(), e);
            }
            return null;
        }
    }

    /**
     * Get the Thread context class loader.
     * <p/>
     *
     * @return the Thread context class loader
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static ClassLoader getTCL() throws IllegalAccessException, InvocationTargetException {
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }

    /**
     * Get the class loader of the class argument
     * <p/>
     *
     * @return the class loader of the argument
     */
    public static ClassLoader getClassLoader(final Class<?> clazz) {
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            public ClassLoader run() {
                return clazz.getClassLoader();
            }
        });
    }

    /**
     * Try the specified classloader and then fall back to the loadClass
     * <p/>
     *
     * @param loader
     * @param clazz
     * @return Class
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClass(ClassLoader loader, String clazz) throws ClassNotFoundException {
        try {
            if (loader != null) {
                Class<?> c = loader.loadClass(clazz);
                if (c != null) {
                    return c;
                }
            }
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
        return loadClass(clazz, true);
    }

    /**
     * Try the specified classloader and then fall back to the loadClass
     * <p/>
     *
     * @param loader
     * @param clazz
     * @param type
     * @return Class
     * @throws ClassNotFoundException
     */
    public static <T> Class<? extends T> loadClass(ClassLoader loader,
                                                   String clazz,
                                                   Class<T> type) throws ClassNotFoundException {
        try {
            if (loader != null) {
                Class<?> c = loader.loadClass(clazz);
                if (c != null) {
                    return c.asSubclass(type);
                }
            }
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
        return loadClass(clazz, true, type);
    }

    /**
     * If running under JDK 1.2 load the specified class using the
     * <code>Thread</code> <code>contextClassLoader</code> if that
     * fails try Class.forname.
     * <p/>
     *
     * @param clazz
     * @return the class
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClass(String clazz) throws ClassNotFoundException {
        return loadClass(clazz, true);
    }

    /**
     * If running under JDK 1.2 load the specified class using the
     * <code>Thread</code> <code>contextClassLoader</code> if that
     * fails try Class.forname.
     * <p/>
     *
     * @param clazz
     * @param type  Type to cast it to
     * @return the class
     * @throws ClassNotFoundException
     */
    public static <T> Class<? extends T> loadClass(String clazz, Class<T> type)
            throws ClassNotFoundException {
        return loadClass(clazz, true, type);
    }

    public static <T> Class<? extends T> loadClass(String clazz,
                                                   boolean warn,
                                                   Class<T> type) throws ClassNotFoundException {
        return loadClass(clazz, warn).asSubclass(type);
    }

    public static Class<?> loadClass(String clazz, boolean warn) throws ClassNotFoundException {
        try {
            ClassLoader tcl = getTCL();

            if (tcl != null) {
                Class<?> c = tcl.loadClass(clazz);
                if (c != null) {
                    return c;
                }
            }
        } catch (Exception e) {
            if (warn) {
                LOG.warn(e.getMessage(), e);
            } else {
                LOG.debug(e.getMessage(), e);
            }
        }

        return loadClass2(clazz, null);
    }
    
    private static Class<?> loadClass2(String className, Class<?> callingClass)
        throws ClassNotFoundException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            try {
                if (Loader.class.getClassLoader() != null) {
                    return Loader.class.getClassLoader().loadClass(className);
                }
            } catch (ClassNotFoundException exc) {
                if (callingClass != null && callingClass.getClassLoader() != null) {
                    return callingClass.getClassLoader().loadClass(className);
                }
            }
            throw ex;
        }
    }
}
