import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DataManager {
    public static ArrayList<Model> models = new ArrayList<>();
    public static void loadAll() {
        loadModels();
    }
    public static void saveAll() {
        saveModels();
    }
    public static void loadModels() {
        models.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("model.csv"))) {
            String line = br.readLine(); // header

            String[] header = line.split(",");

            while ((line = br.readLine()) != null) {
                String[] s = line.split(",");
                Model m = new Model(s[0], Double.parseDouble(s[1]));

                // Load outlet stocks
                for (int i = 2; i < s.length; i++) {
                    m.getStock().put(header[i], Integer.parseInt(s[i]));
                }

                models.add(m);
            }
        } catch (Exception e) {
            System.out.println("Error loading models.csv");
        }
    }

    public static void saveModels() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("model.csv"))) {
            // header
            pw.print("model,price");
            for (String outlet : models.get(0).getStock().keySet()) {
                pw.print("," + outlet);
            }
            pw.println();

            // data
            for (Model m : models) {
                pw.print(m.getName() + "," + m.getPrice());
                for (String outlet : m.getStock().keySet()) {
                    pw.print("," + m.getStock().get(outlet));
                }
                pw.println();
            }
        } catch (Exception e) {
            System.out.println("Error saving models.csv");
        }
    }
}