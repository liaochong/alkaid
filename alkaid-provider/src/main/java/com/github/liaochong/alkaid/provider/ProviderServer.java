package com.github.liaochong.alkaid.provider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liaochong
 * @version 1.0
 */
public class ProviderServer {

    private static boolean initialized = false;

    private ExecutorService executorService;

    public static synchronized ProviderServer getInstance() {
        if (initialized) {
            throw new IllegalStateException();
        }
        ProviderServer providerServer = new ProviderServer();
        providerServer.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        initialized = true;
        return providerServer;
    }

    public static synchronized ProviderServer getInstance(ExecutorService executorService) {
        if (initialized) {
            throw new IllegalStateException();
        }
        ProviderServer providerServer = new ProviderServer();
        providerServer.executorService = executorService;
        initialized = true;
        return providerServer;
    }

    private ProviderServer() {

    }

    public void start(List<Provider> providers) throws IOException {
        if (Objects.isNull(providers) || providers.isEmpty()) {
            return;
        }
        // 1.发布提供者，采用zookeeper作为注册中心
        this.publish(providers);
        // 2.接收请求
        this.acceptRequest();
    }

    private void publish(List<Provider> providers) {

    }

    private void acceptRequest() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        while (true) {
            Socket socket = serverSocket.accept();
            this.processing(socket);
        }
    }

    private void processing(Socket socket) {
        Objects.requireNonNull(socket);
        executorService.submit(() -> {
            try {
                dealingRequest(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void dealingRequest(Socket socket) throws IOException {
        socket.getInputStream();
    }
}
