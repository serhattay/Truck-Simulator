# CMPE Truck Simulator Using Self-Implemented Data Structures

## Overview

The **CMPE Truck Simulator** is a simulation program designed to manage a truck fleet in a logistics company. This program models various operations involved in managing parking lots, trucks, and the transportation of goods. It allows users to simulate actions such as adding and removing trucks, managing parking lot capacities, and efficiently handling the loading and unloading of trucks.

## Features

- **Parking Lot Management**: Create, delete, and manage parking lots with defined capacities.
- **Truck Management**: Add trucks with varying load capacities to parking lots.
- **Load Handling**: Load trucks with goods, transferring loads between parking lots when necessary.
- **Efficient Distribution**: Simulate the efficient distribution of loads across the fleet based on parking lot and truck capacities.

## Data Structures Used

- **Queue**: Used to manage truck arrivals and departures in parking lots in a first-in-first-out (FIFO) manner.
- **AVL Tree**: Employed for maintaining an ordered list of trucks in parking lots, ensuring efficient insertion, deletion, and retrieval operations.
- **Object-Oriented Programming (OOP)**: The simulator is built using OOP principles, including classes, inheritance, polymorphism, and encapsulation, to create a modular and maintainable system.

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/serhattay/cmpe-truck-simulator.git
    ```

2. Navigate to the project directory:
    ```bash
    cd cmpe-truck-simulator
    ```

3. Compile and run the program using your preferred Java environment.

## Usage

Once the program is running, you can interact with the system through the provided commands to manage parking lots, add trucks, load goods, and simulate transfers. Detailed instructions on using each function are available in the description pdf.
