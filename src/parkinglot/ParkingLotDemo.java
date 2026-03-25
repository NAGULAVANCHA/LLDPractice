package parkinglot;

/**
 * Demo driver for the Parking Lot system.
 */
public class ParkingLotDemo {
    public static void main(String[] args) {
        ParkingLot.resetInstance();
        ParkingLot lot = ParkingLot.getInstance();

        // Setup: 2 floors, each with bike/car/truck spots
        ParkingFloor floor1 = new ParkingFloor(1);
        floor1.addSpot(new ParkingSpot(1, VehicleType.BIKE));
        floor1.addSpot(new ParkingSpot(2, VehicleType.CAR));
        floor1.addSpot(new ParkingSpot(3, VehicleType.CAR));
        floor1.addSpot(new ParkingSpot(4, VehicleType.TRUCK));

        ParkingFloor floor2 = new ParkingFloor(2);
        floor2.addSpot(new ParkingSpot(5, VehicleType.BIKE));
        floor2.addSpot(new ParkingSpot(6, VehicleType.CAR));
        floor2.addSpot(new ParkingSpot(7, VehicleType.TRUCK));

        lot.addFloor(floor1);
        lot.addFloor(floor2);

        // Park vehicles
        Vehicle bike1 = new Vehicle("BIKE-001", VehicleType.BIKE);
        Vehicle car1  = new Vehicle("CAR-001", VehicleType.CAR);
        Vehicle car2  = new Vehicle("CAR-002", VehicleType.CAR);
        Vehicle truck1 = new Vehicle("TRUCK-001", VehicleType.TRUCK);

        Ticket t1 = lot.parkVehicle(bike1);
        Ticket t2 = lot.parkVehicle(car1);
        Ticket t3 = lot.parkVehicle(car2);
        Ticket t4 = lot.parkVehicle(truck1);

        lot.displayStatus();

        // Unpark a car
        lot.unparkVehicle(t2.getTicketId());
        lot.displayStatus();

        // Park another car — should go to the now-free spot
        Vehicle car3 = new Vehicle("CAR-003", VehicleType.CAR);
        lot.parkVehicle(car3);
        lot.displayStatus();
    }
}

