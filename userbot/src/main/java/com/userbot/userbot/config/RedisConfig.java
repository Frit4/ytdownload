package com.userbot.userbot.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SslOptions;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.password}")
    private String redisPassword;

//    @Value("${redis.tls.password}")
//    private String tlsPassword;

    @Bean(destroyMethod = "shutdown")
    public RedisClient redisClient() throws Exception {
//        KeyStore trustStore = KeyStore.getInstance("JKS");
//        try (InputStream is = Thread.currentThread()
//                .getContextClassLoader()
//                .getResourceAsStream("redis-truststore.jks")) {
//
//            if (is == null) {
//                throw new IllegalStateException("truststore отсутствует в classpath");
//            }
//            trustStore.load(is, tlsPassword.toCharArray());
//        }
//
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(
//                TrustManagerFactory.getDefaultAlgorithm()
//        );
//        tmf.init(trustStore);
//
//        SslOptions sslOptions = SslOptions.builder()
//                .jdkSslProvider()
//                .trustManager(tmf)
//                .build();
//
//        ClientOptions clientOptions = ClientOptions.builder()
//                .sslOptions(sslOptions)
//                .build();

        RedisURI redisUri = RedisURI.builder()
                .withHost(host)
                .withPort(port)
                .withSsl(false)
                .withTimeout(Duration.ofSeconds(3))
                .withPassword(redisPassword.toCharArray())
                .build();

        RedisClient client = RedisClient.create(redisUri);
//        client.setOptions(clientOptions); // <-- ОБЯЗАТЕЛЬНО
        return client;
    }

    @Bean(destroyMethod = "close")
    public StatefulRedisConnection<String, String> redisConnection(RedisClient redisClient) {
        return redisClient.connect();
    }

    @Bean
    public RedisCommands<String, String> redisCommands(StatefulRedisConnection<String, String> redisConnection) {
        return redisConnection.sync();
    }
}