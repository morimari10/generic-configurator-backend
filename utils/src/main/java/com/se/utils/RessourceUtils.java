package com.se.utils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Optional;

/**
 * Resource util class.
 */
public final class RessourceUtils {

    private RessourceUtils() {
        throw new AssertionError("Constructor should not be called directly");
    }

    /**
     * Get IO stream for resource from class path.
     * Please pay attention for this URLCONNECTION_SSRF_FD findbugs warning.
     * So please don't use this method for getting not authorized/verified files.
     *
     * @param clazz        the class.
     * @param resourceName the manifest name.
     * @return IO stream for resource
     * @throws IOException if specific resource is not found.
     */
    @SuppressFBWarnings("URLCONNECTION_SSRF_FD")
    public static InputStream getClassPathResourceIOStream(Class<?> clazz, String resourceName) throws IOException {
        Optional<URL> optionalURL = Optional.ofNullable(clazz)
                .map(valueClazz -> (URLClassLoader) clazz.getClassLoader())
                .map(value -> value.findResource(resourceName));
        if (optionalURL.isPresent()) {
            return optionalURL.get().openStream();
        } else {
            throw new IOException(String.format("The following resource %s is not found", resourceName));
        }
    }

    /**
     * Get IO stream for resource from class path.
     * Please pay attention for this URLCONNECTION_SSRF_FD findbugs warning.
     * So please don't use this method for getting not authorized/verified files.
     *
     * @param clazz        the class.
     * @param resourceName the manifest name.
     * @return IO stream for resource
     * @throws IOException if specific resource is not found.
     */
    @SuppressFBWarnings("URLCONNECTION_SSRF_FD")
    public static InputStream getResourceAsStream(Class<?> clazz, String resourceName) throws IOException {
        Optional<InputStream> optionalInputStream = Optional.ofNullable(clazz)
                .map(Class::getClassLoader)
                .map(value -> value.getResourceAsStream(resourceName));
        if (optionalInputStream.isPresent()) {
            return optionalInputStream.get();
        } else {
            throw new IOException(String.format("The following resource %s is not found", resourceName));
        }
    }
}
