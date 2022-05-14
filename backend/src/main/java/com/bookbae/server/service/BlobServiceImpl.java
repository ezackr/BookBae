package com.bookbae.server.service;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlobServiceImpl implements BlobService {
    private String connectionString;

    public BlobServiceImpl() {
        this.connectionString = System.getProperty("bookbae.blob_connection_string");
    }

    public BlobClient getClient(String blobName) {
        return new BlobClientBuilder()
            .connectionString(connectionString)
            .endpoint("https://bookbaephotos.blob.core.windows.net/userphotos")
            .blobName(blobName)
            .buildClient();
    }    
}