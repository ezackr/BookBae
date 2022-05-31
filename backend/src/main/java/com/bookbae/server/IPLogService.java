package com.bookbae.server;

public interface IPLogService {
    public int addAndCheck(String remoteAddress, long nanoTime);
}