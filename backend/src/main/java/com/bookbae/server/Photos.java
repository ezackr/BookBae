@SecuredResource
@Path("photos")
public class Photos {
    private BlobService blob;
    public Photos(BlobService blob) {
        this.blob = blob;
    }

    @POST
    @Consumes("image/jpeg")
    public Response putPhoto(@Context SecurityContext ctx, @Context HttpHeaders headers, InputStream stream) {
        //TODO: jpeg sanitization?
        BlobClient client = this.blob.getClient(ctx.getUserPrincipal().getName());
        BufferedInputStream bstream = new BufferedInputStream(stream);
        long length = headers.getLength();
        client.upload(bstream, length);
    }    
}