
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #
 *
 */
public class GeneticAlgo {

    public static int NUM_ELITE_KNAPSACKS;

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

    public Population crossoverPopulation(Population population) {
        Population crossoverPopulation = new Population(population.getKnapsacks().size(), this.initialKnapsack);
        for (int i = 1; i < NUM_ELITE_KNAPSACKS; i++) { // Put the elite routes in the crossover population.
            crossoverPopulation.getKnapsacks().add(population.getKnapsacks().poll());
        }
        for (int i = NUM_ELITE_KNAPSACKS; i < crossoverPopulation.getRoutes().size(); i++) {
            // Now for the rest of the population, use Tournament Selection to select the best.
            // The route at index 0 should be the fittest because we're sorting by fitness.
            Route route1 = this.selectPopulation(population).getRoutes().get(0);
            Route route2 = this.selectPopulation(population).getRoutes().get(0);
            crossoverPopulation.getRoutes().set(i, this.crossoverRoute(route1, route2));
        }
        return crossoverPopulation;
    }

    public Population mutatePopulation(Population population) {

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
        
        toMutate.genome().sort(toMutate.comparator()); // Sort based on Value-Weight ratio. Better Items have higher ratios and are therefore at the end.
        
        // Weight checking
        while (toMutate.getWeight() > toMutate.getMaxWeight()) {
            
            
            
            
        }
            
            
            
        int minValueToWeight = Integer.MAX_VALUE;
        int indexOfMinValueToWeight = 0;
        if (toMutate.getWeight() > toMutate.getMaxWeight()) {
            // Find least important/useful item
            for (int i = 0; i < toMutate.genome().size(); i++) {
                int ratio = toMutate.genome().get(i).getValue() / toMutate.genome().get(i).getWeight();
                if (ratio < minValueToWeight) {
                    minValueToWeight = ratio;
                    indexOfMinValueToWeight = i;
                }
            }
            
            int weightDelta = toMutate.getMaxWeight() - toMutate.getWeight();
            int quantityDelta = weightDelta / toMutate.genome().get(indexOfMinValueToWeight).getUsed();
        }
    }

}
