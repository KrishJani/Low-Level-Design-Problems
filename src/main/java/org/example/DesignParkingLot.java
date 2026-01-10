package org.example;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DesignParkingLot {
    List<Level> levels;

    DesignParkingLot(List<Level> levels){
        this.levels = levels;
    }

    void addLevel(Level level){

    }

    Ticket parkVehicle(Vehicle vehicle){

    }

    double unparkVehicle(Ticket ticket){

    }

    int getAvailableSpots(VehicleType type){

    }
}

// Core Entity: Ticket
class Ticket{
    String ticketId;
    ParkingSpot spot;
    LocalDateTime entryTime;

    Ticket(String ticketId, ParkingSpot spot){
        this.ticketId = ticketId;
        this.spot = spot;
        entryTime = LocalDateTime.now();
    }

    double calculateFee(){
        return ParkingFeeCalculator.calculateFee(entryTime, LocalDateTime.now());
    }
}

// Fee Calculation logic
class ParkingFeeCalculator{
    private static final double FIRST_HOUR_RATE = 5.0;
    private static final double SUBSEQUENT_HOUR_RATE = 3.0;

    public static double calculateFee(LocalDateTime entryTime, LocalDateTime exitTime){
        Duration duration = Duration.between(entryTime, exitTime);
        long minutes = duration.toMinutes();

        if(minutes <= 60){
            return FIRST_HOUR_RATE;
        }

        long hours = (minutes + 59)/60;
        return FIRST_HOUR_RATE + (hours-1) * SUBSEQUENT_HOUR_RATE;
    }
}

// Display Board for showing available Spots
class ParkingDisplayBoard{
    public void ShowAvailableSpots(int level, Map<SpotType, Integer> availableSpots){
        System.out.println("Level " + level + " Availability");
        for(Map.Entry<SpotType, Integer> entry : availableSpots.entrySet()){
            System.out.println("For spot type " + entry.getKey() + ", " + entry.getValue() + " spots are available");
        }
    }
}

// Core Entity: Level
class Level{
    int level;
    List<ParkingSpot> spots;
    ParkingStrategy parkingStrategy;
    private final Lock lock = new ReentrantLock();

    Level(int level, int compactSpots, int largeSpots, int handicappedSpots, int bikeSpots){
        this.level = level;
        this.spots = new ArrayList<>();
        this.parkingStrategy = new NearestFirstStrategy();

        System.out.println(compactSpots + " Compact Spots Added at level " + level);
        System.out.println(largeSpots + " Large Spots Added at level " + level);
        System.out.println(handicappedSpots + " Handicapped Spots Added at level " + level);
        System.out.println(bikeSpots + " Bike Spots Added at level " + level);
    }

    boolean parkVehicle(Vehicle vehicle){
        if(lock.tryLock()){
            try{
                ParkingSpot spot = parkingStrategy.findAvailability(spots, vehicle);
                return spot != null && spot.assignVehicle(vehicle);
            }
            finally {
                lock.unlock();
            }
        }
        return false;
    }

    boolean releaseSpot(String spotId){

    }

    int getAvailableSpots(VehicleType type){

    }
}

// Strategy Pattern: Parking allocation strategy
interface ParkingStrategy{
    ParkingSpot findAvailability(List<ParkingSpot> spots, Vehicle vehicle);
}

class ParkingSpot{
    String spotId;
    SpotType spotType;
    boolean isOccupied;
    Vehicle vehicle;

    public ParkingSpot(String spotId, SpotType spotType){
        this.spotId = spotId;
        this.spotType = spotType;
        this.isOccupied = false;
    }

    public boolean canFitVehicle(Vehicle vehicle){

    }

    public boolean assignVehicle(Vehicle vehicle){

    }

    public void removeVehicle(){

    }
}

class Vehicle{
    String licensePlate;
    VehicleType vehicleType;

    public Vehicle(String licensePlate, VehicleType vehicleType) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
    }
}

enum VehicleType{
    CAR, BIKE, TRUCK
}

enum SpotType{
    COMPACT, LARGE, HANDICAPPED, BIKE
}

class Car extends Vehicle{
    public Car(String licensePlate){
        super(licensePlate, VehicleType.CAR);
    }
}
class Bike extends Vehicle{
    public Bike(String licensePlate){
        super(licensePlate, VehicleType.BIKE);
    }
}
class Truck extends Vehicle{
    public Truck(String licensePlate){
        super(licensePlate, VehicleType.TRUCK);
    }
}
