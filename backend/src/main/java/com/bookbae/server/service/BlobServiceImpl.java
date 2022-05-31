package com.bookbae.server.service;

import jakarta.enterprise.context.ApplicationScoped;
import com.bookbae.server.BlobService;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

/**
 * Provides an implementation of {@link com.bookbae.server.BlobService BlobService}.
 * This implementation is {@link jakarta.enterprise.context.ApplicationScoped ApplicationScoped},
 * so there is only one instance that is managed by the framework that will be shared by all
 * root resource classes and providers that require it.
 */
@ApplicationScoped
public class BlobServiceImpl implements BlobService {
    private BlobContainerClient container;

    public BlobServiceImpl() {
        container = new BlobServiceClientBuilder()
            .connectionString(System.getProperty("bookbae.blob_connection_string"))
            .buildClient()
            .getBlobContainerClient("userphotos");
    }

    @Override
    public synchronized BlobClient getClient(String blobName) {
        return container.getBlobClient(blobName);
    }    
}