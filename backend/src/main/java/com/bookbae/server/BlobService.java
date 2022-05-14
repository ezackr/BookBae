package com.bookbae.server;

public interface BlobService {
    public BlobClient getClient(String blobName);
}