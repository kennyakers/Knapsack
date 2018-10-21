
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #
 *
 */
public class Item {

    private String name;
    private int value;
    private int weight;
    private int available;
    private int used;

    public Item(String name, int value, int weight, int available, int used) {
        this.name = name;
        this.value = value;
        this.weight = weight;
        this.available = available;
        this.used = used;
    }

    public String getName() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getAvailable() {
        return this.available;
    }

    public int getUsed() {
        return this.used;
    }

    public void setUsed(int used) {
        this.used = used;
    }
    
    public double getValToWeightRatio() {
        return this.getValue() / this.getWeight();
    }

}
