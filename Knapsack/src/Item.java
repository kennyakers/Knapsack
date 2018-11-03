
import java.util.Comparator;

/**
 * Kenny Akers Mr. Paige Homework #
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
        double ratio = ((double) this.value) / ((double) this.weight);
        if (GeneticAlgo.VERBOSE) {
            System.out.println(this.name + "'s ratio: " + this.value + " / " + this.weight + " = " + ratio);
        }
        return ratio;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + "\n\t"
                + "Quantity: " + this.available + "\n\t"
                + "Used: " + this.used + " \n\t"
                + "Value: " + this.value + "\n\t"
                + "Weight: " + this.weight + "\n";
    }

    public static Comparator<Item> comparator() {
        return new Comparator<Item>() {
            @Override
            // Higher Value-Weight Ratio = "better"
            public int compare(Item k1, Item k2) {
                int result = 0;
                if (k1.getValToWeightRatio() < k2.getValToWeightRatio()) {
                    result = -1;
                } else if (k1.getValToWeightRatio() > k2.getValToWeightRatio()) {
                    result = 1;
                }
                return result;
            }
        };
    }

}
