package com.cfecweb.leon.client;

public interface TokenHandler {
    void onToken(String token);
    void onError(String message);
}
