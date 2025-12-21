import java.util.LinkedHashMap;
public class Model {
    private String name;
    private double price;
    private LinkedHashMap<String, Integer> stock;

    public Model(String name, double price) {
        this.name = name;
        this.price = price;
        this.stock = new LinkedHashMap<>();
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public LinkedHashMap<String, Integer> getStock() { return stock; }

    public void setPrice(double price) { this.price = price; }

    public int getStockForOutlet(String outlet) {
        return stock.getOrDefault(outlet, 0);
    }

    public void setStockForOutlet(String outlet, int qty) {
        stock.put(outlet, qty);
    }

    public void changeStockForOutlet(String outlet, int delta) {
        int cur = stock.getOrDefault(outlet, 0);
        stock.put(outlet, Math.max(0, cur + delta));
    }
}
