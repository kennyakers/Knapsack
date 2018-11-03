
import java.util.ArrayList;

public class GeneticAlgo {

    public static int NUM_ELITE_KNAPSACKS;
    public static int POPULATION_SIZE; // Number of knapsacks in a generation.
    public static double MUTATION_RATE; // Probability that a chromosome (knapsack) gene (item in that knapsack) will randomly mutate (0-1).
    public static int TOURNAMENT_SELECTION_SIZE;// Using Tournament selection to do chromosome (knapsack) crossover selection.
    public static int NUM_GENERATIONS; // Number of generations
    public static boolean VERBOSE;
    public static CrossoverMethod CROSSOVER_METHOD;

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
        Population crossoverPopulation = new Population(GeneticAlgo.POPULATION_SIZE, this.initialKnapsack);
        for (int i = 0; i < NUM_ELITE_KNAPSACKS; i++) { // Put the elite knapsacks in the crossover population.
            Knapsack eliteKnapsack = population.getKnapsacks().get(i);
            eliteKnapsack.setElite(true);
            crossoverPopulation.getKnapsacks().set(i, eliteKnapsack);
        }
        for (int i = GeneticAlgo.NUM_ELITE_KNAPSACKS; i < GeneticAlgo.POPULATION_SIZE; i++) {
            // Now for the rest of the population, use Tournament Selection to select the best.
            // The knapsack at index 0 should be the fittest because we're sorting by fitness.
            //System.out.println("Thing:" + population.getKnapsacks().toString());
            Knapsack knapsack1 = this.selectPopulation(population).getKnapsacks().get(0);
            Knapsack knapsack2 = this.selectPopulation(population).getKnapsacks().get(0);
            knapsack1.setElite(false);
            knapsack2.setElite(false);
            Knapsack crossed = this.crossoverKnapsacks(knapsack1, knapsack2);
            if (VERBOSE) {
                System.out.println("Population size: " + population.getKnapsacks().size());
                System.out.println("knapsack1: " + knapsack1);
                System.out.println("knapsack2: " + knapsack2);
                System.out.println("Crossed knapsack: " + crossed);
            }
            crossoverPopulation.getKnapsacks().set(i, crossed);
        }
        return crossoverPopulation;
    }

    public Knapsack crossoverKnapsacks(Knapsack k1, Knapsack k2) {
        // Use java -ea to enable assertions.
        assert k1.getMaxWeight() == k2.getMaxWeight(); // k1 and k2's max weight should be the same.
        assert k1.genome().size() == k2.genome().size(); // k1 and k2's genome length should be the same.
        ArrayList<Item> newGenome = new ArrayList<>(k1.genome()); // Create copy so as not to reference k1's genome.
        Knapsack crossover = new Knapsack(k1.getMaxWeight(), newGenome); // Start with k1's genome.

        switch (CROSSOVER_METHOD) {
            case FIFTY_FIFTY:
                for (int i = 0; i < k1.genome().size() / 2; i++) {
                    crossover.genome().set(i, k1.genome().get(i));
                }
                for (int i = k1.genome().size() / 2; i < k1.genome().size(); i++) {
                    crossover.genome().set(i, k2.genome().get(i));
                }
                break;
            case RANDOM:
                int randomSplitLine = (int) (Math.random() * k1.genome().size());
                for (int i = 0; i < randomSplitLine; i++) {
                    crossover.genome().set(i, k1.genome().get(i));
                }
                for (int i = randomSplitLine; i < k1.genome().size(); i++) {
                    crossover.genome().set(i, k2.genome().get(i));
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
                if (Math.random() < MUTATION_RATE) {
                    sack = this.mutateKnapsack(sack);
                }
            }
        }
        if (VERBOSE) {
            System.out.println("Mutated population to return: " + population);
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

        // Mutate the amounts of two Items
        randomIndex1 = (int) (Math.random() * toMutate.genome().size());
        randomIndex2 = (int) (Math.random() * toMutate.genome().size());
        int newAmount1 = (int) (Math.random() * toMutate.genome().get(randomIndex1).getAvailable());
        int newAmount2 = (int) (Math.random() * toMutate.genome().get(randomIndex2).getAvailable());
        toMutate.genome().get(randomIndex1).setUsed(newAmount1);
        toMutate.genome().get(randomIndex2).setUsed(newAmount2);

        if (VERBOSE) {
            System.out.println("Before checking: " + toMutate.toString());
        }

        // Quantity + Weight checking
        toMutate.checkKnapsack();

        if (VERBOSE) {
            System.out.println("After checking: " + toMutate.toString());
        }

        // At this point, we should have a valid (quantity and weight), mutated Knapsack 
        return toMutate;
    }

    // Selects the population using Tournament Selection
    public Population selectPopulation(Population population) {
        Population tournament = new Population(TOURNAMENT_SELECTION_SIZE, this.initialKnapsack);
        for (int i = 0; i < TOURNAMENT_SELECTION_SIZE; i++) {
            tournament.getKnapsacks().add(population.getKnapsacks().get((int) (Math.random() * population.getKnapsacks().size())));
        }
        tournament.sortKnapsacksByFitness();
        return tournament;
    }

}
