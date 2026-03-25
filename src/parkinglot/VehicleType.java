package parkinglot;

/**
 * PARKING LOT LLD
 * ================
 * Key Concepts:
 *  - Strategy Pattern: Different parking strategies (nearest spot, floor-based)
 *  - Factory Pattern: Vehicle creation
 *  - Singleton: ParkingLot instance
 *  - Observer: Notify when lot is full/available
 *
 * Classes:
 *  VehicleType     -> Enum for BIKE, CAR, TRUCK
 *  Vehicle         -> Represents a vehicle with type & license plate
 *  ParkingSpot     -> A single spot that can hold a vehicle
 *  ParkingFloor    -> A floor with multiple spots
 *  ParkingLot      -> Singleton managing multiple floors
 *  Ticket          -> Issued when a vehicle is parked
 *  ParkingStrategy -> Interface for spot-finding algorithm
 */
public enum VehicleType {
    BIKE,
    CAR,
    TRUCK
}

