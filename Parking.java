public class Parking {
    private static final int MAX_CAPACITY = 501_000;
    public static final ParkingLot[] wholeParking = new ParkingLot[MAX_CAPACITY];

    private static AvlTree<Integer> allParkingLots = new AvlTree<>();
    private static AvlTree<Integer> allAvailableParkingLots = new AvlTree<>(); // Not full ones
    private static AvlTree<Integer> waitingParkingLots = new AvlTree<>();
    private static AvlTree<Integer> readyParkingLots = new AvlTree<>();

    private Parking() {
        throw new UnsupportedOperationException("This is a static class.");
    }



    public static ParkingLot[] getParking() {
        return wholeParking;
    }

    // Returns null if no parking lot with that capacity exists, otherwise returns the parking lot
    public static ParkingLot getParkingLot(int capacity) {
        return getParking()[capacity];
    }

    public static void addParkingLot(int capacityConstraint, int truckLimit) {
        if (getParkingLot(capacityConstraint) == null) {
            addParkingLot(new ParkingLot(capacityConstraint, truckLimit), capacityConstraint);
        }
    }

    // This method is to add the parking lot to the given capacity if this capacity is empty
    public static void addParkingLot(ParkingLot parkingLot, int capacity) {
        if (wholeParking[capacity] == null) {
            wholeParking[capacity] = parkingLot;
            allParkingLots.insert(capacity);
            if (parkingLot.truckLimit > 0) {
                allAvailableParkingLots.insert(capacity);
            }
        }
    }

    public static void deleteParkingLot(int capacityConstraint) {
        if (wholeParking[capacityConstraint] != null) {
            wholeParking[capacityConstraint] = null;
            allParkingLots.remove(capacityConstraint);
            allAvailableParkingLots.remove(capacityConstraint);
            waitingParkingLots.remove(capacityConstraint);
            readyParkingLots.remove(capacityConstraint);
        }
    }
    public static int addTruck(int truckId, int capacity) {
        return addTruck(truckId, capacity, 0);
    }

    public static int addTruck(int truckId, int capacity, int currentLoad) {
        int suitableCapacity = getSmallerLargest(capacity - currentLoad, allAvailableParkingLots); // If not lot is found it returns -1
        if (suitableCapacity != -1) {
            Truck truckToAdd = new Truck(truckId, capacity, currentLoad);
            truckToAdd.currentCapacity = suitableCapacity;
            wholeParking[suitableCapacity].waitingSection.enqueue(truckToAdd);
            wholeParking[suitableCapacity].numberOfTrucks += 1;
            wholeParking[suitableCapacity].numberOfWaiting += 1;
            adjustTotalTrucksBacktrack(suitableCapacity);
            checkIfParkingLotFull(suitableCapacity);
        } // Else do nothing

        return suitableCapacity;
    }

    public static void adjustTotalTrucksBacktrack(int suitableCapacity) {
        adjustTotalTrucksBacktrack(suitableCapacity, allParkingLots.root);
    }

    private static Integer adjustTotalTrucksBacktrack(Integer suitableCapacity, AvlTree.AvlNode<Integer> rost) {
        if (rost == null) {
            return 0;
        }

        int compareResult = suitableCapacity.compareTo(rost.element);
        ParkingLot parkingLot = Parking.wholeParking[rost.element];

        int leftSize;
        int rightSize;
        if (compareResult < 0) {
            leftSize = adjustTotalTrucksBacktrack(suitableCapacity, rost.left);
            rightSize = (rost.right == null ? 0 : rost.right.totalTrucks);
        } else if (compareResult > 0) {
            leftSize = (rost.left == null ? 0 : rost.left.totalTrucks);
            rightSize = adjustTotalTrucksBacktrack(suitableCapacity, rost.right);
        } else {
            leftSize = (rost.left == null ? 0 : rost.left.totalTrucks);
            rightSize = (rost.right == null ? 0 : rost.right.totalTrucks);
        }
        rost.totalTrucks = leftSize + rightSize + parkingLot.numberOfTrucks;

        return rost.totalTrucks;
    }

    // A function to check if the parking lot is full and to check whether it was added to waitingParkingLots before
    // if not add it to there
    private static void checkIfParkingLotFull(int capacity) {
        ParkingLot parkingLot = wholeParking[capacity];
        // Check if the parking lot is full
        if (parkingLot.truckLimit <= parkingLot.numberOfTrucks) {
            allAvailableParkingLots.remove(capacity);
        }

        if (parkingLot.waitingSection.numberOfElements == 1) {
            waitingParkingLots.insert(capacity);
        }
    }


    // Driver for getSuitableCapacity
    private static int getSmallerLargest(int capacity, AvlTree<Integer> tree) {
        return getSmallerLargest(capacity, tree.root, -1);

    }

    // Be careful if there is no such parking lot this function will return -1, because we used -1 as the indicator
    private static int getSmallerLargest(int givenCapacity, AvlTree.AvlNode<Integer> currentNode,
                                         int bestCurrentCapacity) {
        // When the search is done, base case
        if (currentNode == null) {
            return bestCurrentCapacity;
        }
        if (givenCapacity > currentNode.element) {
            bestCurrentCapacity = currentNode.element;
            return getSmallerLargest(givenCapacity, currentNode.right, bestCurrentCapacity);
        } else if (givenCapacity < currentNode.element) {
            return getSmallerLargest(givenCapacity, currentNode.left, bestCurrentCapacity);
        } else {
            return givenCapacity;
        }
    }

    public static int count(int capacity) {
        AvlTree.AvlNode<Integer> node = allParkingLots.root;
        return recursiveSum(capacity, node);
    }

    private static int recursiveSum(int capacity, AvlTree.AvlNode<Integer> currentNode) {
        if (currentNode == null) {
            return 0;
        }
        if (capacity > currentNode.element) {
            return recursiveSum(capacity, currentNode.right);
        } else if (capacity < currentNode.element) {
            return (currentNode.totalTrucks - currentNode.getTotalOfLeftSubtree()) +
                    recursiveSum(capacity, currentNode.left);
        } else {
            return currentNode.getTotalOfRightSubTree();
        }
    }

    public static String readyCommand(int capacity) {
        int suitableCapacity = getLargerSmallest(capacity, waitingParkingLots);
        if (suitableCapacity != -1) {
            Truck truckToBeReady = wholeParking[suitableCapacity].waitingSection.dequeue();
            wholeParking[suitableCapacity].numberOfWaiting -= 1;
            wholeParking[suitableCapacity].readySection.enqueue(truckToBeReady);
            wholeParking[suitableCapacity].numberOfReady += 1;

            if (wholeParking[suitableCapacity].numberOfWaiting == 0) {
                waitingParkingLots.remove(suitableCapacity);
            }

            if (wholeParking[suitableCapacity].numberOfReady == 1) {
                readyParkingLots.insert(suitableCapacity);
            }

            return String.valueOf(truckToBeReady.id) + " " + String.valueOf(suitableCapacity);
        }

        return String.valueOf(suitableCapacity);

    }

    private static int getLargerSmallest(int capacity, AvlTree<Integer> tree) {
        return getLargerSmallest(capacity, tree.root, -1);
    }

    private static int getLargerSmallest(int givenCapacity, AvlTree.AvlNode<Integer> currentNode,
                                          int bestCurrentCapacity) {
        // When the search is done, base case
        if (currentNode == null) {
            return bestCurrentCapacity;
        }
        if (givenCapacity < currentNode.element) {
            bestCurrentCapacity = currentNode.element;
            return getLargerSmallest(givenCapacity, currentNode.left, bestCurrentCapacity);
        } else if (givenCapacity > currentNode.element) {
            return getLargerSmallest(givenCapacity, currentNode.right, bestCurrentCapacity);
        } else {
            return givenCapacity;
        }
    }

    public static String receivingLoad(int capacity, int loadAmount) {
        int newPlace;
        int suitableCapacity;
        StringBuilder returnString = new StringBuilder();
        while (loadAmount > 0) {
            suitableCapacity = getLargerSmallest(capacity, readyParkingLots);
            if (suitableCapacity != -1) {
                ParkingLot parkingLot = wholeParking[suitableCapacity];

                while (parkingLot.readySection.hasNext()) {

                    Truck currentTruck = parkingLot.readySection.dequeue();
                    addParkingLotFullToAvailable(suitableCapacity);
                    adjustTotalTrucksBacktrack(suitableCapacity);

                    if (currentTruck.currentCapacity < loadAmount) {
                        loadAmount -= currentTruck.currentCapacity;
                        currentTruck.currentLoad += currentTruck.currentCapacity;
                        if (currentTruck.maxCapacity > currentTruck.currentLoad) {
                            newPlace = addTruck(currentTruck.id, currentTruck.maxCapacity, currentTruck.currentLoad);
                        } else {
                            newPlace = addTruck(currentTruck.id, currentTruck.maxCapacity);
                        }

                        if (!returnString.isEmpty()) {
                            returnString.append(" - ");
                        }
                        returnString.append(currentTruck.id).append(" ").append(newPlace);
                    } else if (currentTruck.currentCapacity == loadAmount) {
                        loadAmount = 0;
                        currentTruck.currentLoad += currentTruck.currentCapacity;
                        if (currentTruck.maxCapacity > currentTruck.currentLoad) {
                            newPlace = addTruck(currentTruck.id, currentTruck.maxCapacity, currentTruck.currentLoad);
                        } else {
                            newPlace = addTruck(currentTruck.id, currentTruck.maxCapacity);
                        }

                        if (!returnString.isEmpty()) {
                            returnString.append(" - ");
                        }
                        returnString.append(currentTruck.id).append(" ").append(newPlace);
                    } else {
                        currentTruck.currentLoad += loadAmount;
                        newPlace = addTruck(currentTruck.id, currentTruck.maxCapacity, currentTruck.currentLoad);

                        if (!returnString.isEmpty()) {
                            returnString.append(" - ");
                        }
                        returnString.append(currentTruck.id).append(" ").append(newPlace);

                        return returnString.toString();
                    }
                }
                readyParkingLots.remove(suitableCapacity);

            } else {
                if (!returnString.isEmpty()) {
                    return returnString.toString();
                } else {
                    return String.valueOf(-1);
                }
            }
        }

        return returnString.toString();
    }

    private static void addParkingLotFullToAvailable(int suitableCapacity) {
        ParkingLot parkingLot = wholeParking[suitableCapacity];
        if (parkingLot.truckLimit == parkingLot.numberOfTrucks) {
            allAvailableParkingLots.insert(suitableCapacity);
        }

        parkingLot.numberOfReady -= 1;
        parkingLot.numberOfTrucks -= 1;
    }
}
