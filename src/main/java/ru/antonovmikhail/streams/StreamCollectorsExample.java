package ru.antonovmikhail.streams;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamCollectorsExample {

    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("Laptop", 1200.0),
                new Order("Smartphone", 800.0),
                new Order("Laptop", 1500.0),
                new Order("Tablet", 500.0),
                new Order("Smartphone", 900.0)
        );
        orders.stream()
                .collect(Collectors.groupingBy(Order::getProduct))
                .entrySet()
                .stream()
                .map(e -> Map.entry(e.getKey(), e.getValue().stream().mapToDouble(Order::getCost).sum()))
                .sorted((e1, e2) -> (int) (e2.getValue() - e1.getValue()))
                .limit(3)
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

    }
}

class Order {
    private String product;
    private double cost;

    public Order(String product, double cost) {
        this.product = product;
        this.cost = cost;
    }

    public String getProduct() {
        return product;
    }

    public double getCost() {
        return cost;
    }
}
