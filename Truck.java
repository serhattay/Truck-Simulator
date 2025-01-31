public class Truck {
    int id;
    int maxCapacity;
    int currentCapacity;
    int currentLoad;

    public Truck(int id, int maxCapacity) {
        this(id, maxCapacity, 0);
    }
    public Truck(int id, int maxCapacity, int currentLoad) {
        this.id = id;
        this.maxCapacity = maxCapacity;
        currentCapacity = maxCapacity;
        this.currentLoad = currentLoad;
    }
}
