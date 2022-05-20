package com.bookbae.server;

import com.bookbae.server.security.SecuredResource;
import com.bookbae.server.json.PhotoResponse;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.inject.Inject;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.AccessTier;


@SecuredResource
@Path("/photos")
public class Photos {
    private BlobService blob;

    @Inject
    public Photos(BlobService blob) {
        this.blob = blob;
    }

    @POST
    @Consumes("image/jpeg")
    @Produces("application/json")
    public Response putPhoto(@Context SecurityContext ctx, @Context HttpHeaders headers, InputStream stream) {
        //TODO: jpeg sanitization?
        BlobClient client = this.blob.getClient(ctx.getUserPrincipal().getName());
        BufferedInputStream bstream = new BufferedInputStream(stream);
        long length = headers.getLength();
        BlobHttpHeaders blobHeaders = new BlobHttpHeaders().setContentType("image/jpeg");
        client.uploadWithResponse(bstream, length, null,
                                  blobHeaders, null, AccessTier.HOT, null, 
                                  Duration.of(1l, ChronoUnit.MINUTES), null);
        //TODO: error handling for that
        PhotoResponse resp = new PhotoResponse();
        resp.url = client.getBlobUrl();
        return Response.ok(resp).build();
    }

    /*@GET
    @Produces("application/json")
    public Response getPhoto(@Context SecurityContext ctx) {

    }*/
}