
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

    @Override
    public String toString() {
        String returnable = "[";
        for (Item item : this.items) {
            returnable += (item.getName() + ": " + item.getUsed() + " | ");
        }

        return returnable + "]";
    }
    
    public void checkKnapsack() {
        for (Item item : this.genome()) {
            if (item.getUsed() > item.getAvailable()) { // Using too many of that item
                if (GeneticAlgo.VERBOSE) {
                    System.out.println("Using too many of " + item.getName() + ": Used = " + item.getUsed() + ", Available = " + item.getAvailable());
                }
                item.setUsed(item.getAvailable()); // Bring down to max
            }
        }

        this.genome().sort(Item.comparator()); // Sort based on Value-Weight ratio. Better Items have higher ratios and are therefore at the end.
        for (Item item : this.genome()) { // For each item (worst first)
            while (item.getUsed() > 0 && this.getWeight() > this.getMaxWeight()) {
                if (GeneticAlgo.VERBOSE) {
                    System.out.print("OVERWEIGHT: Current: " + this.getWeight() + " | Max: " + this.getMaxWeight() + ". Decreasing quantity of " + item.getName() + " to " + (item.getUsed() - 1));
                }
                item.setUsed(item.getUsed() - 1); // Decrement the amount of that item
                if (GeneticAlgo.VERBOSE) {
                    System.out.println(". New weight: " + this.getWeight());
                }
            }
        }
    }
}
