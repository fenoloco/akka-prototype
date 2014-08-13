/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.java.rest.performance;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author Mauri
 */
@Path("api")
public class ApiResource {

    /**
     * Retrieves representation of an instance of
     * com.test.java.rest.performance.ApiResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        return "[]";
    }

}
