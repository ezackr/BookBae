package com.bookbae.server.security;

import com.bookbae.server.IPLogService;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.PreMatching;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
@PreMatching
public class IPAccessFilter implements ContainerRequestFilter {

    @Inject
    private IPLogService log;

    @Context
    HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        int req = log.addAndCheck(request.getRemoteAddr(), System.nanoTime());
        System.err.println("Requests in buffer for address " + request.getRemoteAddr() + ": " + req);
        if(req > 10) {
            ctx.abortWith(Response.status(Response.Status.TOO_MANY_REQUESTS).build());
        }
    }
}