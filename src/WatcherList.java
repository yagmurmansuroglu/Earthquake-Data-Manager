/**
 * Class: WatcherList
 * -------------------
 * This class implements a Doubly Linked List to store WatcherNode objects.
 * It is responsible for managing the watchers who registered for earthquake notifications.
 * * Key Features:
 * 1. Doubly Linked List: Nodes have references to both next and previous nodes.
 * 2. Order Preserved: Watchers are stored in the order they arrive (FIFO).
 * 3. Output Compliance: Prints specific messages upon adding/removing watchers.
 */
public class WatcherList {
    // --- Member Variables ---

    // 'head': Points to the first node in the list.
    // Used for traversing the list from the beginning.
    WatcherNode head;
    // 'tail': Points to the last node in the list.
    // Crucial for adding new watchers to the end in O(1) time efficiently.
    WatcherNode tail;

    /**
     * Constructor: WatcherList
     * ------------------------
     * Initializes an empty doubly linked list.
     * Both head and tail are set to null as there are no elements yet.
     */
    public WatcherList(){
        this.head = null;
        this.tail = null;
    }
    /**
     * Method: add
     * -----------
     * Creates a new WatcherNode and adds it to the END of the list.
     * This maintains the arrival order of the watchers.
     * * Output Requirement:
     * Prints "<name> is added to the watcher-list" to standard output.
     *  * @param name      The name of the watcher.
     * * @param latitude  The latitude of the watcher.
     * @param longitude The longitude of the watcher.
     */
    public void add(String name , double latitude, double longitude){
        // 1. Create the new node with the provided data
        WatcherNode newNode = new WatcherNode(name, latitude, longitude );
        // 2. Check if the list is currently empty
        if (this.head == null) {
            // Case: List is empty.
            // The new node becomes both the head and the tail.
            this.head = newNode;
            this.tail = newNode;
        } else {
            // Case: List is not empty.
            // We attach the new node to the end of the list using 'tail'.

            this.tail.next = newNode; // Current tail points forward to new node
            newNode.prev = this.tail; // New node points backward to current tail
            this.tail = newNode;      // Update tail to be the new node
        }

        // 3. Print the required output message EXACTLY as requested
        // Format: "Tom is added to the watcher-list"
        System.out.println(name + " is added to the watcher-list");
    }
    /**
     * Method: delete
     * --------------
     * Finds and removes a watcher by name from the list.
     * (We will implement this logic in the next step, as it requires traversal).
     * * Output Requirement:
     * Prints "<name> is removed from the watcher-list".
     */
    public void delete(String name) {
        WatcherNode current = this.head;

        // Traverse the list to find the node with the given name
        while (current != null) {
            if (current.name.equals(name)) {

                // FOUND IT! Now we remove 'current' node.

                // Step 1: Adjust the 'next' pointer of the PREVIOUS node
                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    // If prev is null, it means we are removing the HEAD.
                    this.head = current.next;
                }

                // Step 2: Adjust the 'prev' pointer of the NEXT node
                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    // If next is null, it means we are removing the TAIL.
                    this.tail = current.prev;
                }

                // REQUIRED OUTPUT: <name> is removed from the watcher-list
                System.out.println(name + " is removed from the watcher-list");

                return; // Exit after removing the first match
            }
            current = current.next; // Move to the next node
        }
    }
    /**
     * Method: notifyWatchers
     * ----------------------
     * Traverses the list of watchers and checks if they are close to the given earthquake.
     * If a watcher is within the danger zone, it prints a notification message.
     * * Requirement:
     * - "Distance < 2 * Magnitude^3"
     * - Output: "Earthquake <place> is close to <name>" [cite: 100]
     * * @param earthquake The earthquake event to check against.
     */
    public void notifyWatchers(EarthquakeNode earthquake) {
        WatcherNode current = this.head;

        // Traverse through the entire list of watchers
        while (current != null) {

            // 1. Calculate distance between Watcher and Earthquake
            // (Assuming Euclidean distance on Lat/Lon based on standard assignment practices)
            double dist = getDistance(current.latitude, current.longitude, earthquake.latitude, earthquake.longitude);

            // 2. Calculate the danger radius: 2 * Magnitude^3
            double dangerRadius = 2 * Math.pow(earthquake.magnitude, 3);

            // 3. Check if the watcher is within the danger radius
            if (dist < dangerRadius) {
                // REQUIRED OUTPUT: Earthquake <place> is close to <name>
                System.out.println("Earthquake " + earthquake.place + " is close to " + current.name);
            }

            current = current.next; // Move to the next watcher
        }
    }
    /**
     * Helper Method: getDistance
     * --------------------------
     * Calculates the Euclidean distance between two coordinate points (lat/lon).
     * Note: While Haversine is more accurate for Earth, standard CS assignments
     * usually expect Euclidean distance for simplicity unless specified otherwise.
     */
    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDiff = lat1 - lat2;
        double lonDiff = lon1 - lon2;
        return Math.sqrt((latDiff * latDiff) + (lonDiff * lonDiff));
    }
}