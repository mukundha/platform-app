package com.avgjoe;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;



@ApplicationPath("/")
public class Application extends javax.ws.rs.core.Application{

	@Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        // register root resource
        classes.add(Identity.class);
        
        return classes;
    }

}
