
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Kenny Akers Mr. Paige Homework #
 *
 */
public class Knapsack implements Organism<Item> {

    private ArrayList<Item> items;
    private final int maxWeight;

    public Knapsack(int maxWeight, ArrayList<Item> genome) {
        this.maxWeight = maxWeight;
        this.items = genome;
    }

    public int getMaxWeight() {
        return this.maxWeight;
    }

    @Override
    public int getFitness() {
        int sum = 0;
        for (Item item : items) {
            sum += item.getValue() * item.getUsed();
        }
        return sum;
    }

    @Override
    public ArrayList<Item> genome() {
        return items;
    }

    @Override
    public boolean isLegal() {
        return this.getWeight() <= this.maxWeight;
    }

    public int getWeight() {
        int sum = 0;
        for (Item item : items) {
            sum += item.getWeight() * item.getUsed();
        }
        return sum;
    }

    public Comparator<Item> comparator() {
        return new Comparator<Item>() {
            @Override
            // Higher Value-Weight Ratio = "better"
            public int compare(Item k1, Item k2) {
                int result = 0;
                if (k1.getValToWeightRatio() < k2.getValToWeightRatio()) {
                    result = -1;
                } else if (k1.getValToWeightRatio()> k2.getValToWeightRatio()) {
                    result = 1;
                }
                return result;
            }
        };
    }
}
