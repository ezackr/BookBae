package com.bookbae.server;

import com.bookbae.server.security.SecuredResource;
import com.bookbae.server.json.PhotoResponse;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.inject.Inject;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.AccessTier;
import com.azure.storage.blob.specialized.AppendBlobClient;

/**
 * Allows a user to set their profile photo.
 */
@SecuredResource
@Path("/photos")
public class Photos {
    private BlobService blob;

    @Inject
    public Photos(BlobService blob) {
        this.blob = blob;
    }

    @POST
    @Consumes("multipart/form-data")
    @Produces("application/json")
    public Response putPhoto(@Context SecurityContext ctx, @FormParam("photo") InputStream stream) 
            throws IOException {
        AppendBlobClient client = this.blob.getClient(ctx.getUserPrincipal().getName())
            .getAppendBlobClient();
        BlobHttpHeaders blobHeaders = new BlobHttpHeaders().setContentType("image/jpeg");
        client.createWithResponse(blobHeaders, null, null, 
                                  Duration.of(1l, ChronoUnit.MINUTES), null);
        stream.transferTo(new BufferedOutputStream(client.getBlobOutputStream()));
        PhotoResponse resp = new PhotoResponse();
        resp.url = client.getBlobUrl();
        return Response.ok(resp).build();
    }
}