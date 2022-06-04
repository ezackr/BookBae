package com.bookbae.server;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Our application's {@link jakarta.ws.rs.core.Application Application} subclass which may define
 * configuration information.
 * Empty as the defaults are suitable for now
 */
@ApplicationPath("/v1")
public class RestApplication extends Application {}