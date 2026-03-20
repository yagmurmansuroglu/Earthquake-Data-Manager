# Earthquake Simulation System 

This project is a discrete event simulation designed to manage a dynamic network of earthquake observers. It synchronizes two different data streams—real-time earthquake records and watcher requests—to provide precise monitoring and proximity-based notifications.

 Core Functionality & Purpose
The system acts as a high-performance hub that processes incoming seismic data and matches it against active watchers.
* **Event Merging:** Synchronizes earthquake reports (XML) and watcher commands (add, delete, query) based on chronological timestamps.
* **Proximity Alerts:** Automatically notifies watchers if they are within a danger radius, calculated as $2 \times Magnitude^3$ from the epicenter.
* **Auto-Cleanup:** To maintain data accuracy and memory efficiency, it prunes earthquake records older than 6 hours during every processing cycle.

 Technical Stack & Data Structures
To ensure maximum control and performance, this system was built using custom-engineered data structures rather than standard libraries:
* **Earthquake List (Singly Linked List):** Utilizes a tail pointer for **O(1)** insertions, ensuring chronological data is added instantly.
* **Watcher Network (Doubly Linked List):** Designed for rapid **O(1)** deletions once a node is identified, maintaining a flexible observer list.
* **Custom XML Parsing:** A manual, robust parsing logic that extracts ID, Time, Place, Magnitude, and Coordinates without any external dependencies.

 Engineering Decisions
The project focuses on solving critical edge cases that demonstrate strong logical reasoning:
* **Tie-Breaking Logic:** If an earthquake and a watcher request occur at the exact same timestamp, the watcher is processed **first**. This ensures no notification is missed for newly added observers.
* **Coordinate Validation:** Includes built-in logic to detect and correct swapped Longitude and Latitude values (e.g., when values exceed the ±90 range), ensuring mathematical consistency in distance calculations.

 Getting Started
1.  Clone the Repository:
    ```bash
    git clone [https://github.com/yagmurmansuroglu/EarthquakeSimulation.git](https://github.com/yagmurmansuroglu/EarthquakeSimulation.git)
    ```
2.  Compile & Run:
    ```bash
    javac *.java
    java EarthquakeSimulation
    ```
3.  **Input:** When prompted, provide your data files (e.g., `1-earthquake-file` and `1-watcher-file`) to start the simulation log.

---
*Developed as part of Data Structures Coursework - March 2026*
