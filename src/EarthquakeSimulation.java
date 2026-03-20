import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
/**
 * Class: EarthquakeSimulation
 * ---------------------------
 * This is the MAIN driver class for the assignment.
 * It implements a discrete event simulation by processing two input streams (files).
 *
 * CRITICAL LOGIC NOTE:
 * Based on the output analysis (Henry scenario):
 * If an earthquake and a watcher request occur at the EXACT SAME TIME,
 * the Watcher Request is processed FIRST. This ensures that a user added
 * at time T receives a notification for an earthquake that happens at time T.
 */
public class EarthquakeSimulation {

    /**
     * Main Method
     * -----------
     * Entry point of the program.
     * Asks user for filenames, creates scanners, and starts the simulation.
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // 1. Get input filenames from the user
        // We do NOT hardcode filenames as per instructions.
        System.out.print("Enter earthquake filename: ");
        String eqFileName = input.next();

        System.out.print("Enter watcher filename: ");
        String watcherFileName = input.next();

        try {
            // 2. Start the simulation process
            processSimulation(eqFileName, watcherFileName);
        } catch (FileNotFoundException e) {
            // Handle file errors gracefully
            System.out.println("Error: Input file not found. (" + e.getMessage() + ")");
        }
    }

    /**
     * Method: processSimulation
     * -------------------------
     * The core engine of the discrete event simulation.
     * Merges two sorted input files based on timestamps.
     *
     * @param eqFile      The path to the earthquake data file.
     * @param watcherFile The path to the watcher request file.
     */
    public static void processSimulation(String eqFile, String watcherFile) throws FileNotFoundException {
        // Initialize Scanners for reading the files
        Scanner scEq = new Scanner(new File(eqFile));
        Scanner scWatcher = new Scanner(new File(watcherFile));

        // Initialize the Data Structures
        EarthquakeList earthquakeList = new EarthquakeList();
        WatcherList watcherList = new WatcherList();

        // --- PRE-FETCHING ---
        // Read the first event from both files to initialize the comparison loop.
        EarthquakeNode nextEq = parseEarthquake(scEq);
        Request nextReq = parseRequest(scWatcher);

        double currentTime = 0;

        // --- SIMULATION LOOP ---
        // Continue processing as long as at least one file has data left.
        while (nextEq != null || nextReq != null) {

            boolean processEq = false;

            // --- TIE-BREAKING & ORDERING LOGIC ---
            if (nextEq != null && nextReq != null) {
                // CASE: Both files have data. Compare timestamps.

                // CRITICAL FIX:
                // If timestamps are EQUAL, we must process the Watcher Request FIRST.
                // We only process the earthquake if its time is STRICTLY LESS (<) than the request.
                if (nextEq.time < nextReq.time) {
                    processEq = true;
                } else {
                    processEq = false; // If times are equal or req is smaller, process req.
                }
            } else if (nextEq != null) {
                // CASE: Only earthquake data remains.
                processEq = true;
            } else {
                // CASE: Only watcher data remains.
                processEq = false;
            }

            // --- EXECUTE THE SELECTED EVENT ---
            if (processEq) {
                // ==============================
                // PROCESS EARTHQUAKE
                // ==============================

                // 1. Update Simulation Time
                currentTime = nextEq.time;

                // 2. CLEANUP: Remove old records BEFORE adding the new one.
                // Rule: Remove earthquakes older than 6 hours relative to 'currentTime'.
                earthquakeList.removeOldRecords(currentTime);

                // 3. Add the new earthquake to the list
                earthquakeList.add(nextEq);

                // 4. Notify watchers
                // Check all current watchers to see if they are close to this new earthquake.
                watcherList.notifyWatchers(nextEq);

                // 5. Fetch the next earthquake from the file
                nextEq = parseEarthquake(scEq);

            } else {
                // ============================================================
                // PROCESS WATCHER REQUEST
                // ============================================================

                // 1. Update Simulation Time
                currentTime = nextReq.time;

                // 2. CLEANUP: Time has moved forward, so we must check for expired earthquakes.
                // This ensures that query-largest returns correct results for the new time.
                earthquakeList.removeOldRecords(currentTime);

                // 3. Handle the specific type of request
                if (nextReq.type.equals("add")) {
                    // Add new watcher to the list
                    watcherList.add(nextReq.name, nextReq.lat, nextReq.lon);
                }
                else if (nextReq.type.equals("delete")) {
                    // Remove watcher from the list
                    watcherList.delete(nextReq.name);
                }
                else if (nextReq.type.equals("query-largest")) {
                    // Query the earthquake list for statistics
                    earthquakeList.queryLargest();
                }

                // 4. Fetch the next request from the file
                nextReq = parseRequest(scWatcher);
            }
        }

        // Close the scanners to prevent resource leaks
        scEq.close();
        scWatcher.close();
    }

    /**
     * Inner Class: Request
     * --------------------
     * A temporary data holder (Struct-like) for parsed watcher file lines.
     */
    static class Request {
        int time;        // Timestamp
        String type;     // "add", "delete", "query-largest"
        double lat;      // Latitude (for add)
        double lon;      // Longitude (for add)
        String name;     // Watcher Name
    }
    /**
     * Helper Method: parseRequest
     * ---------------------------
     * Parses a single line from the watcher input file.
     * * LOGIC EXPLANATION:
     * This method handles different request types ("add", "delete", "query-largest").
     * * COORDINATE FIX:
     * For "add" requests, the input line is: "timestamp add <val1> <val2> <name>".
     * Example: "0 add -105.7 -24.3 Tom".
     * Since -105.7 is invalid as a Latitude, <val1> is treated as Longitude
     * and <val2> is treated as Latitude.
     *
     * @param sc The Scanner object for the watcher file.
     * @return A Request object containing the parsed command data.
     */
    private static Request parseRequest(Scanner sc) {
        if (!sc.hasNext()) return null;

        Request req = new Request();

        // 1. Read Timestamp
        if (sc.hasNextInt()) {
            req.time = sc.nextInt();
        } else {
            return null; // Stop if timestamp is missing
        }

        // 2. Read Request Type
        req.type = sc.next();

        // 3. Parse arguments based on type
        if (req.type.equals("add")) {
            // Reading coordinates: First number is Longitude, Second is Latitude
            double val1 = Double.parseDouble(sc.next());
            double val2 = Double.parseDouble(sc.next());

            req.lon = val1; // -105.7 is Longitude
            req.lat = val2; // -24.3 is Latitude

            // Read the rest of the line as the name
            req.name = sc.nextLine().trim();
        }
        else if (req.type.equals("delete")) {
            // Format: time delete name
            req.name = sc.nextLine().trim();
        }
        // "query-largest" requires no extra parsing beyond the type.

        return req;
    }


    /**
     * Method: parseEarthquake
     * -----------------------
     * Parses the next earthquake record from the input stream (XML format).
     *
     * LOGIC EXPLANATION:
     * The input file contains earthquake data spanning multiple lines within
     * <earthquake> tags. This method uses a while-loop to read line-by-line
     * until the closing tag is found.
     *
     * COORDINATE FIX:
     * The input data format is: "-115.5808, 33.0187, 9.5".
     * Since -115.58 exceeds the valid range for Latitude (+-90), the first number
     * MUST be Longitude. Therefore, we parse index 0 as Longitude and index 1 as Latitude.
     *
     * @param sc The Scanner object for the earthquake file.
     * @return A fully populated EarthquakeNode, or null if end-of-file is reached.
     */
    private static EarthquakeNode parseEarthquake(Scanner sc) {
        if (!sc.hasNext()) return null;

        // Initialize variables with default values
        String id = "";
        double time = 0;
        String place = "";
        double latitude = 0;
        double longitude = 0;
        double depth = 0;
        double magnitude = 0;

        boolean insideEarthquakeBlock = false;

        // Loop through the file until the earthquake block ends
        while (sc.hasNext()) {
            String line = sc.nextLine().trim();

            if (line.startsWith("<earthquake>")) {
                insideEarthquakeBlock = true;
            }
            else if (line.startsWith("</earthquake>")) {
                // End of block: Create and return the node with parsed data
                return new EarthquakeNode(id, time, place, latitude, longitude, depth, magnitude);
            }
            else if (insideEarthquakeBlock) {
                // --- Parse ID ---
                if (line.startsWith("<id>")) {
                    // Extract value by removing tags
                    id = line.replace("<id>", "").replace("</id>", "").trim();
                }
                // --- Parse Time ---
                else if (line.startsWith("<time>")) {
                    String timeStr = line.replace("<time>", "").replace("</time>", "").trim();
                    time = Double.parseDouble(timeStr);
                }
                // --- Parse Place ---
                else if (line.startsWith("<place>")) {
                    place = line.replace("<place>", "").replace("</place>", "").trim();
                }
                // --- Parse Magnitude ---
                else if (line.startsWith("<magnitude>")) {
                    String magStr = line.replace("<magnitude>", "").replace("</magnitude>", "").trim();
                    magnitude = Double.parseDouble(magStr);
                }
                // --- Parse Coordinates ---
                else if (line.startsWith("<coordinates>")) {
                    String coordsRaw = line.replace("<coordinates>", "").replace("</coordinates>", "").trim();
                    String[] parts = coordsRaw.split(",");

                    if (parts.length >= 3) {
                        // CRITICAL FIX: The file order is Longitude, Latitude, Depth.
                        // We must parse them in this specific order to ensure distance calculations are correct.
                        longitude = Double.parseDouble(parts[0].trim()); // First value is Longitude
                        latitude = Double.parseDouble(parts[1].trim());  // Second value is Latitude
                        depth = Double.parseDouble(parts[2].trim());     // Third value is Depth
                    }
                }
            }
        }
        return null;
    }

    }


