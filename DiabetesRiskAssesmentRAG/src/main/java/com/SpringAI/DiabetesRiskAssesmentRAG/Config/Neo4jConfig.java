package com.SpringAI.DiabetesRiskAssesmentRAG.Config;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class Neo4jConfig {
    @Bean
    public Driver neo4jDriver(
            @Value("${spring.neo4j.uri}") String uri,
            @Value("${spring.neo4j.authentication.username}") String username,
            @Value("${spring.neo4j.authentication.password}") String password) {
        return GraphDatabase.driver(uri, AuthTokens.basic(username, password));
    }
}

