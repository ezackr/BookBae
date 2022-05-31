package com.bookbae.server;

import com.azure.storage.blob.BlobClient;

/**
 * This interface defines the contract for accessing Azure Blob Storage.
 * Root resource classes and providers may use an injected instance of this
 * interface to access a {@link com.azure.storage.blob.BlobClient BlobClient} to upload blobs to Azure.
 * @see jakarta.inject.Inject
 */ 
public interface BlobService {
    /**
     * Gets a client for a blob with a given name
     * @param blobName the blob to construct a client for
     * @return a {@link com.azure.storage.blob.BlobClient BlobClient} that refers to that blob
     */
    public BlobClient getClient(String blobName);
}