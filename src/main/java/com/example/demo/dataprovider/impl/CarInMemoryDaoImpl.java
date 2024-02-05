package com.example.demo.dataprovider.impl;

import com.example.demo.dataprovider.CarDao;
import com.example.demo.domain.model.Car;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Repository
public class CarInMemoryDaoImpl implements CarDao {

    private static final Map<String, List<String>> portifolio = buildPortifolio();

    public Car getRandomCar() {
        final var brand = getBrand();
        final var car = portifolio.get(brand)
                .get(getRandomIndex(portifolio.get(brand).size()));

        return Car.builder()
                .id(new Random().nextLong())
                .model(car)
                .brand(brand)
                .build();
    }

    private static String getBrand() {
        return portifolio.keySet()
                .stream()
                .toList()
                .get(getRandomIndex(portifolio.size()));
    }

    private static int getRandomIndex(int maxSize) {
        return Double.valueOf(Math.floor(Math.random() * (maxSize))).intValue();
    }


    private static Map<String, List<String>> buildPortifolio() {
        return Map.of(
                "Fiat", List.of("Argo", "Mobi", "Palio", "Fastback", "Punto", "Strada", "Toro"),
                "Chevrolet", List.of("Onix", "Corsa", "Celta", "Tracker", "Montana", "S10"),
                "Volkswagen", List.of("Gol", "Golf", "Nivus", "T-Cross", "Polo", "Fox", "Amarok", "Up!"),
                "Toyota", List.of("Corolla", "Hilux", "SW4", "Etios", "Yaris"),
                "Nissan", List.of("March", "Kicks", "Frontier", "Versa")
        );
    }


}
