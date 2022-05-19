package com.bookbae.server;

import com.azure.storage.blob.BlobClient;

public interface BlobService {
    public BlobClient getClient(String blobName);
}