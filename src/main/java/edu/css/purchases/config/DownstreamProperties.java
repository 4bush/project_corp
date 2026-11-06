package edu.css.purchases.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.downstream")
public record DownstreamProperties(String baseUrl, Duration timeout) {}
