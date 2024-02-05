package com.example.demo.config;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class GrpcConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final ApplicationContext applicationContext;

    @Bean
    public Server server() {
        final var grpcServices =  applicationContext.getBeansWithAnnotation(GrpcService.class)
                        .values()
                        .stream()
                        .map(it -> (BindableService) it);
        final var serverBuilder = ServerBuilder.forPort(9090)
                .intercept(new GrpcInterceptor());
        grpcServices.forEach(serverBuilder::addService);

        return serverBuilder.build();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final var server = event.getApplicationContext().getBean(Server.class);
        try {
            server.start();
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
