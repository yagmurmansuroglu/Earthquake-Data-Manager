/**
 * Class: EarthquakeNode
 * ---------------------
 * This class represents a single earthquake record in the linked list.
 * It stores all the detailed information provided in the earthquake input file
 * (id, time, location, coordinates, magnitude) and a reference to the next node.
 */
public class EarthquakeNode {
    // --- Member Variables (Data Fields) ---

    // The unique identifier for the earthquake (e.g., "001").
    public String id;

    // The timestamp of the earthquake (hours passed since start).
    // Stored as double to handle precise timing if necessary, though input looks like integer.
    public double time;

    // A descriptive string for the location (e.g., "4km East of San Francisco, CA").
    public String place;

    // Coordinates: Latitude and Longitude are crucial for distance calculation.
    public double latitude;
    public double longitude;

    // Depth is provided in the input coordinates (3rd value), so we store it as well.
    public double depth;

    // The magnitude of the earthquake (e.g., 3.9714).
    // This is used to calculate the impact radius (Magnitude^3).
    public double magnitude;

    // --- Pointers ---

    // Reference to the next earthquake record in the list.
    // The assignment describes this as "another linked list"[cite: 57],
    // usually implying a Singly Linked List unless "Doubly" is specified like for Watchers.
    public EarthquakeNode next;

    /**
     * Constructor: EarthquakeNode
     * ---------------------------
     * Initializes a new earthquake node with all the parsed data.
     * * @param id        The ID of the earthquake.
     * @param time      The occurrence time.
     * @param place     The location description.
     * @param latitude  The latitude coordinate.
     * @param longitude The longitude coordinate.
     * @param depth     The depth of the earthquake.
     * @param magnitude The magnitude of the earthquake.
     */
    public EarthquakeNode(String id, double time, String place, double latitude, double longitude, double depth, double magnitude) {
        this.id = id;
        this.time = time;
        this.place = place;
        this.latitude = latitude;
        this.longitude = longitude;
        this.depth = depth;
        this.magnitude = magnitude;

        // Initialize the next pointer to null.
        this.next = null;
    }
}


