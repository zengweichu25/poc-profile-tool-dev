package com.profiletool.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "keys")
public record KeyManagementProperties(Aes aes, Rsa rsa) {

    public record Aes(Resource path) { }

    public record Rsa(Keystore keystore) {

        public record Keystore(Resource path, Password password) {

            public record Password(Resource path) { }
        }
    }
}

