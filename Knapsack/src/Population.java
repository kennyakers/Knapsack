
import java.util.ArrayList;

public class Population {

    private ArrayList<Knapsack> knapsacks;

    public Population(int initialSize, Knapsack startSack) {
        //System.out.println("Start sack: " + startSack.toString());
        //System.out.println("END OF START SACK");
        this.knapsacks = new ArrayList<>();
        for (int i = 0; i < initialSize; i++) {
            this.knapsacks.add(startSack);
        }
        //System.out.println("Knapsacks: " + this.knapsacks.toString());
    }

    public Population(int initialSize, ArrayList<Knapsack> startKnapsacks) {
        this.knapsacks = new ArrayList<>();
        for (Knapsack k : startKnapsacks) {
            this.knapsacks.add(k);
        }
    }

    public ArrayList<Knapsack> getKnapsacks() {
        return this.knapsacks;
    }

    public void sortKnapsacksByFitness() {
        this.knapsacks.sort(Knapsack.comparator());
    }

    @Override
    public String toString() {
        return this.knapsacks.toString();
    }

}
