package com.bookbae.server.security;

import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.PreMatching;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@Provider
@Priority(Priorities.AUTHENTICATION)
@PreMatching
public class IPAccessFilter implements ContainerRequestFilter {

    @Inject
    private IPLogService log;

    @Context
    private HttpServletContext servletCtx;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        boolean accept = log.addAndCheck(servletCtx.getRemoteAddr());
    }
}