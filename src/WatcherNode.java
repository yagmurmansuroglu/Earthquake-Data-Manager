/* Since this is a doubly linked list, it maintains references to both
 * the next and the previous nodes.
 */
public class WatcherNode {
    // --- Member Variables (Fields) ---
    String name; // Name of the watcher (e.g., "Tom", "Jane")
     double latitude; // Latitude of the watcher's location
     double longitude; // Longitude of the watcher's location

    WatcherNode next; // Pointer to the next watcher in the list
    WatcherNode prev; // Pointer to the previous watcher (Required for Doubly Linked List)
    /**
     * Constructor: WatcherNode
     * ------------------------
     * Initializes a new watcher node with the specific location and name.
     * * @param name The name of the watcher.
     * * @param latitude The latitude coordinate of the watcher.
     * @param longitude The longitude coordinate of the watcher.
     */
    public WatcherNode(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude =longitude;

        // Pointers are initialized to null by default
        this.next = null;
        this.prev = null;
    }
}
