
/**
 * Class: EarthquakeList
 * ---------------------
         * This class implements a Singly Linked List specifically for EarthquakeNode objects.
        * It manages the list of recent earthquakes, ensuring they are ordered by time.
        * It provides functionality to add new nodes, remove old ones (cleanup), and query stats.
        */
public class EarthquakeList {

    // Reference to the first node in the list (the oldest earthquake).
    // In your structure, this is an EarthquakeNode object directly.
    private EarthquakeNode head;

    // Reference to the last node in the list (the newest earthquake).
    // We keep this to add new earthquakes to the end in O(1) time.
    private EarthquakeNode tail;

    /**
     * Constructor: EarthquakeList
     * ---------------------------
     * Initializes an empty list.
     */
    public EarthquakeList() {
        this.head = null;
        this.tail = null;
    }

    /**
     * Method: add
     * -----------
     * Adds a new earthquake node to the end of the list.
     * Since inputs are chronological, adding to the end preserves time order.
     *
     * @param newNode The EarthquakeNode object to add.
     */
    public void add(EarthquakeNode newNode) {
        // Since we are adding an existing node, ensure its next pointer is null
        newNode.next = null;

        if (head == null) {
            // Case 1: List is empty. Head and Tail point to the new node.
            head = newNode;
            tail = newNode;
        } else {
            // Case 2: List is not empty.
            // Link the current tail to the new node.
            tail.next = newNode;
            // Update the tail pointer to be the new node.
            tail = newNode;
        }

        // REQUIRED OUTPUT: Print confirmation message.
        System.out.println("Earthquake " + newNode.place + " is inserted into the earthquake-list");
    }

    /**
     * Method: removeOldRecords
     * ------------------------
     * Checks the beginning of the list (oldest records) and removes any earthquake
     * that occurred more than 6 hours before the current simulation time.
     *
     * @param currentTime The current simulation time.
     */
    public void removeOldRecords(double currentTime) {
        // Iterate as long as the list is not empty AND the oldest earthquake is too old.
        // We access 'head.time' directly because your class has public fields.
        while (head != null && (currentTime - head.time > 6.0)) {

            // Move head to the next node.
            // This effectively unlinks (deletes) the current head node.
            head = head.next;

            // If the list becomes empty after removal, we must fix the tail pointer.
            if (head == null) {
                tail = null;
            }
        }
    }

    /**
     * Method: queryLargest
     * --------------------
     * Traverses the list to find the earthquake with the highest magnitude.
     * Prints the details of the largest earthquake found.
     */
    public void queryLargest() {
        if (head == null) {
            System.out.println("No earthquakes in past 6 hours.");
            return;
        }

        EarthquakeNode current = head;
        EarthquakeNode maxEq = head; // Assume the first one is the largest initially

        // Traverse the whole list
        while (current != null) {
            // Compare magnitudes
            if (current.magnitude > maxEq.magnitude) {
                maxEq = current;
            }
            current = current.next;
        }

        // REQUIRED OUTPUT: Print result.
        System.out.println("Largest earthquake in past 6 hours:");
        System.out.println("Magnitude " + maxEq.magnitude + " at " + maxEq.place);
    }
}

