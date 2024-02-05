package com.example.demo.usecase;

import com.example.demo.dataprovider.CarDao;
import com.example.demo.domain.model.Car;
import com.example.grpc.CarResponse;
import com.example.grpc.CarServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

@GrpcService
public class CarService extends CarServiceGrpc.CarServiceImplBase {

    private static final Integer MAX_STREAMING_COUNT = 10;
    private final CarDao carDao;

    public CarService(@Autowired CarDao carDao) {
        super();
        this.carDao = carDao;
    }

    @Override
    public void getRandomCar(Empty req, StreamObserver<CarResponse> responseObserver) {
        responseObserver.onNext(from(carDao.getRandomCar()));
        responseObserver.onCompleted();
    }

    @Override
    public void serverSideStreamingGetRandomCar(Empty req, StreamObserver<CarResponse> responseObserver) {
        for (int i = 0; i < new Random().nextInt(1, MAX_STREAMING_COUNT); i++) {
            responseObserver.onNext(from(carDao.getRandomCar()));
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Empty> bidirectionalStreamingGetRandomCar(StreamObserver<CarResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(Empty value) {
                for (int i = 0; i < new Random().nextInt(1, MAX_STREAMING_COUNT); i++) {
                    responseObserver.onNext(from(carDao.getRandomCar()));
                }
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    private static CarResponse from(Car car) {
        return CarResponse.newBuilder()
                .setId(car.getId())
                .setBrand(car.getBrand())
                .setModel(car.getModel())
                .build();
    }

}
