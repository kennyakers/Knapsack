
import java.util.ArrayList;

public class GeneticAlgo {

    public static int NUM_ELITE_KNAPSACKS;
    public static int TOURNAMENT_SELECTION_SIZE;

    public static enum CrossoverMethod {
        BEST_BIASED,
        FIFTY_FIFTY,
        RANDOM
    }

    private Knapsack initialKnapsack;
    private final CrossoverMethod crossoverMethod;

    public GeneticAlgo(Knapsack initialKnapsack, CrossoverMethod method) {
        this.initialKnapsack = initialKnapsack;
        this.crossoverMethod = method;
    }

    public Knapsack getInitialKnapsack() {
        return this.initialKnapsack;
    }

    public Population evolve(Population population) {
        return this.mutatePopulation(this.crossoverPopulation(population));
    }

    // TODO:
    public Population crossoverPopulation(Population population) {
        Population crossoverPopulation = new Population(population.getKnapsacks().size(), this.initialKnapsack);
        for (int i = 0; i < NUM_ELITE_KNAPSACKS; i++) { // Put the elite knapsaks in the crossover population.
            Knapsack eliteKnapsack = population.getKnapsacks().poll();
            eliteKnapsack.setElite(true);
            crossoverPopulation.getKnapsacks().add(eliteKnapsack);
        }
        for (int i = NUM_ELITE_KNAPSACKS; i < crossoverPopulation.getKnapsacks().size(); i++) {
            // Now for the rest of the population, use Tournament Selection to select the best.
            // The knapsack at index 0 should be the fittest because we're sorting by fitness.
            Knapsack knapsack1 = population.getKnapsacks().peek();
            Knapsack knapsack2 = population.getKnapsacks().peek();
            knapsack1.setElite(false);
            knapsack2.setElite(false);
            crossoverPopulation.getKnapsacks().add(this.crossoverKnapsacks(knapsack1, knapsack2));
        }
        return crossoverPopulation;
    }

    public Knapsack crossoverKnapsacks(Knapsack k1, Knapsack k2) {
        // Use java -ea to enable assertions.
        assert k1.getMaxWeight() == k2.getMaxWeight(); // k1 and k2's max weight should be the same.
        assert k1.genome().size() == k2.genome().size(); // k1 and k2's genome length should be the same.
        Knapsack crossover = new Knapsack(k1.getMaxWeight(), k1.genome()); // Start with k1's genome.

        switch (this.crossoverMethod) {
            case FIFTY_FIFTY:
                for (int i = 0; i < k1.genome().size() / 2; i++) {
                    crossover.genome().add(k1.genome().get(i));
                }
                for (int i = k1.genome().size() / 2; i < k1.genome().size(); i++) {
                    crossover.genome().add(k2.genome().get(i));
                }
                break;
            case RANDOM:
                int randomSplitLine = (int) (Math.random() * k1.genome().size());
                for (int i = 0; i < randomSplitLine; i++) {
                    crossover.genome().add(k1.genome().get(i));
                }
                for (int i = randomSplitLine; i < k1.genome().size(); i++) {
                    crossover.genome().add(k2.genome().get(i));
                }
                break;
            case BEST_BIASED:
                // Keep more of the more valuable parent
                // Prioritize reducing items with lowest value/weight ratio
                // Prioritize the best (highest value/weight ratio) items of each parent when inserting.

                Knapsack betterParent;
                int betterParentBias = 80; //0-100. 0 being we use 50% of the best parent, 50% of the second best, 100 being we use 100% of the best parent

                if (Knapsack.comparator().compare(k1, k2) > 0) {
                    betterParent = k1;
                } else {
                    betterParent = k2;
                }

                ArrayList<Item> betterParentGenome = betterParent.genome();
                betterParentGenome.sort(Item.comparator());

                ArrayList<Item> secondParentGenome = betterParent.genome();
                secondParentGenome.sort(Item.comparator());

                ArrayList<Item> resultingChildGenome = new ArrayList();
                for (int i = 0; i < (int) Math.ceil(0.5 + betterParentBias / 200) * betterParentGenome.size(); i++) {
                    resultingChildGenome.add(betterParentGenome.remove(0));
                }
                //Now we have the child filled with the best desired amount of the best parent

                //Next we fill the child with the best of whatevers left of the second parent not already in it
                while (resultingChildGenome.size() != k1.genome().size()) {
                    Item toAdd = secondParentGenome.get(0);
                    for (Item a : resultingChildGenome) {
                        if (toAdd.getName().equals(a.getName())) {
                            secondParentGenome.remove(0);
                            continue;
                        }
                    }
                    resultingChildGenome.add(secondParentGenome.remove(0));
                }

                crossover.genome().clear();
                crossover.genome().addAll(resultingChildGenome);

                break;
        }

        return crossover;
    }

    public Population mutatePopulation(Population population) {
        for (Knapsack sack : population.getKnapsacks()) {
            if (!sack.isElite()) { // If it isn't an elite sack, mutate it.
                this.mutateKnapsack(sack);
            }
        }
        return population;
    }

    public Knapsack mutateKnapsack(Knapsack toMutate) {
        // Indices of the two Items whose quantities we're going to swap.
        int randomIndex1 = (int) (Math.random() * toMutate.genome().size());
        int randomIndex2 = (int) (Math.random() * toMutate.genome().size());

        // Swap the amounts of those two Items.
        Item temp = toMutate.genome().get(randomIndex1);
        toMutate.genome().get(randomIndex1).setUsed(toMutate.genome().get(randomIndex2).getUsed());
        toMutate.genome().get(randomIndex2).setUsed(temp.getUsed());

        // Quantity + Weight checking
        toMutate = this.checkKnapsack(toMutate);

        // At this point, we should have a valid (quantity and weight), mutated Knapsack 
        return toMutate;
    }

    private Knapsack checkKnapsack(Knapsack sack) {
        for (Item item : sack.genome()) {
            if (item.getUsed() > item.getAvailable()) { // Using too many of that item
                item.setUsed(item.getAvailable()); // Bring down to max
            }
        }

        sack.genome().sort(Item.comparator()); // Sort based on Value-Weight ratio. Better Items have higher ratios and are therefore at the end.
        for (Item item : sack.genome()) { // For each item (worst first)
            while (item.getUsed() > 0 && sack.getWeight() > sack.getMaxWeight()) {
                item.setUsed(item.getUsed() - 1); // Decrement the amount of that item
            }
        }
        return sack;
    }

}
