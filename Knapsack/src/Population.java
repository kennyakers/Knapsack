
import java.util.Comparator;
import java.util.PriorityQueue;

public class Population {

    private PriorityQueue<Knapsack> knapsacks;

    public Population(int initialSize, Knapsack startSack) {
        this.knapsacks = new PriorityQueue<>(initialSize, Knapsack.comparator());
        for (int i = 0; i < this.knapsacks.size(); i++) {
            this.knapsacks.add(startSack);
        }
    }
    
    public Population(int initialSize, PriorityQueue<Knapsack> startKnapsacks) {
        this.knapsacks = new PriorityQueue<>(initialSize, new Comparator<Knapsack>() {
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
        });
        for (Knapsack k : startKnapsacks) {
            this.knapsacks.add(k);
        }
    }

    public PriorityQueue<Knapsack> getKnapsacks() {
        return this.knapsacks;
    }

}
