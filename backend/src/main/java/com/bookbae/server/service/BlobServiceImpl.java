package com.bookbae.server.service;

import jakarta.enterprise.context.ApplicationScoped;
import com.bookbae.server.BlobService;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@ApplicationScoped
public class BlobServiceImpl implements BlobService {
    private BlobContainerClient container;

    public BlobServiceImpl() {
        container = new BlobServiceClientBuilder()
            .connectionString(System.getProperty("bookbae.blob_connection_string"))
            .buildClient()
            .getBlobContainerClient("userphotos");
    }

    public synchronized BlobClient getClient(String blobName) {
        return container.getBlobClient(blobName);
    }    
}