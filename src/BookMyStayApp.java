import java.io.*;
import java.util.*;

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("System Recovery");

        RoomInventory inventory = new RoomInventory();
        FilePersistenceService persistence = new FilePersistenceService();

        String filePath = "inventory.txt";

        File file = new File(filePath);

        if (file.exists()) {
            persistence.loadInventory(inventory, filePath);
            System.out.println("Inventory loaded from file.");
        } else {
            System.out.println("No valid inventory data found. Starting fresh.");
        }

        System.out.println("\nCurrent Inventory:");
        Map<String, Integer> availability = inventory.getRoomAvailability();

        System.out.println("Single: " + availability.get("Single"));
        System.out.println("Double: " + availability.get("Double"));
        System.out.println("Suite: " + availability.get("Suite"));

        persistence.saveInventory(inventory, filePath);
        System.out.println("Inventory saved successfully.");
    }
}

class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void setRoomAvailability(String type, int count) {
        roomAvailability.put(type, count);
    }
}

class FilePersistenceService {

    public void saveInventory(RoomInventory inventory, String filePath) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {

            for (Map.Entry<String, Integer> entry :
                    inventory.getRoomAvailability().entrySet()) {

                writer.println(entry.getKey() + "=" + entry.getValue());
            }

        } catch (IOException e) {
            System.out.println("Error saving inventory.");
        }
    }

    public void loadInventory(RoomInventory inventory, String filePath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("=");

                if (parts.length == 2) {
                    String type = parts[0];
                    int count = Integer.parseInt(parts[1]);

                    inventory.setRoomAvailability(type, count);
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading inventory.");
        }
    }
}