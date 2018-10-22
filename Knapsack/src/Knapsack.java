
import java.util.ArrayList;
import java.util.Comparator;

public class Knapsack implements Organism<Item> {

    private ArrayList<Item> items;
    private final int maxWeight;
    private boolean isElite;

    public Knapsack(int maxWeight, ArrayList<Item> genome) {
        this.maxWeight = maxWeight;
        this.items = genome;
        this.isElite = false;
    }

    public int getMaxWeight() {
        return this.maxWeight;
    }

    public void setElite(boolean value) {
        this.isElite = value;
    }

    public boolean isElite() {
        return this.isElite;
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

    public static Comparator<Knapsack> comparator() {
        return new Comparator<Knapsack>() {
            @Override
            public int compare(Knapsack k1, Knapsack k2) {
                int result = 0;
                if (k1.getFitness() > k2.getFitness()) {
                    result = -1;
                } else if (k1.getFitness() < k2.getFitness()) {
                    result = 1;
                }
                return result;
            }
        };
    }
}
