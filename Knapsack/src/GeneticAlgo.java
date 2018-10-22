
public class GeneticAlgo {

    public static int NUM_ELITE_KNAPSACKS;
    public static int TOURNAMENT_SELECTION_SIZE;
    
    public static enum CrossoverMethod {
        BEST_BIASED,
        FIFTY_FIFTY,
        RANDOM
    }

    private Knapsack initialKnapsack;

    public GeneticAlgo(Knapsack initialKnapsack) {
        this.initialKnapsack = initialKnapsack;
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
            Knapsack knapsack1 = this.selectPopulation(population).getKnapsacks().peek();
            Knapsack knapsack2 = this.selectPopulation(population).getKnapsacks().peek();
            knapsack1.setElite(false);
            knapsack2.setElite(false);
            crossoverPopulation.getKnapsacks().add(this.crossoverKnapsacks(knapsack1, knapsack2));
        }
        return crossoverPopulation;
    }

    public Knapsack crossoverKnapsacks(Knapsack k1, Knapsack k2, CrossoverMethod method) {
        // Use java -ea to enable assertions.
        assert k1.getMaxWeight() == k2.getMaxWeight(); //k1 and k2's max weight should be the same
        Knapsack crossover = new Knapsack(k1.getMaxWeight(), k1.genome()); // Start with k1's genome.
        
        switch (method) {
            
        }
        
// Keep more of the more valuable parent
        // Prioritize reducing items with lowest value/weight ratio
        // Prioritize the best (highest value/weight ratio) items of each parent when inserting.
        
        return crossover;
    }

    // TODO:
    // Selects the population using Tournament Selection
    public Population selectPopulation(Population population) {
        Population tournament = new Population(TOURNAMENT_SELECTION_SIZE, this.initialKnapsack);
        for (int i = 0; i < TOURNAMENT_SELECTION_SIZE; i++) {
            //tournament.getKnapsacks().add(i, population.getRoutes().get((int) (Math.random() * population.getKnapsacks().size())));
        }
        return tournament;
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

        // Quantity checking
        if (toMutate.genome().get(randomIndex1).getUsed() > toMutate.genome().get(randomIndex1).getAvailable()) { // Using too many of that item
            toMutate.genome().get(randomIndex1).setUsed(toMutate.genome().get(randomIndex1).getAvailable()); // Bring down to max
        }

        if (toMutate.genome().get(randomIndex2).getUsed() > toMutate.genome().get(randomIndex2).getAvailable()) { // Using too many of that item
            toMutate.genome().get(randomIndex2).setUsed(toMutate.genome().get(randomIndex2).getAvailable()); // Bring down to max
        }

        toMutate.genome().sort(Item.comparator()); // Sort based on Value-Weight ratio. Better Items have higher ratios and are therefore at the end.

        // Weight checking
        for (Item item : toMutate.genome()) { // For each item (worst first)
            while (item.getUsed() > 0 && toMutate.getWeight() > toMutate.getMaxWeight()) {
                item.setUsed(item.getUsed() - 1); // Decrement the amount of that item
            }
        }

        // At this point, we should have a valid (quantity and weight), mutated Knapsack 
        return toMutate;
    }

}
