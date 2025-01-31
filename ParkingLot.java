public class ParkingLot {
    int capacityConstraint; // Capacity of the trucks here
    int truckLimit; // Number of trucks this parking lot can hold
    Queue<Truck> waitingSection; // Trucks in the waiting area
    int numberOfWaiting; // Number of trucks in the waiting area
    Queue<Truck> readySection; // Trucks in the ready area
    int numberOfReady; // Number of trucks in the ready area
    int numberOfTrucks; // Total number of trucks = numberOfReady + numberOfWaiting

    public ParkingLot(int capacityConstraint, int truckLimit) {
        this.capacityConstraint = capacityConstraint;
        this.truckLimit = truckLimit;
        waitingSection = new Queue<>();
        numberOfWaiting = 0;
        readySection = new Queue<>();
        numberOfReady = 0;
        numberOfTrucks = 0;
    }
}
