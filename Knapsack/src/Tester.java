
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Tester {

    public static Knapsack initialKnapsack;

    public static void main(String[] args) {

        Console console = System.console();
        if (console == null) {
            Scanner sc = new Scanner(System.in); // Use scanner instead

            // Get number of generations.
            System.out.println("Number of generations to breed: ");
            GeneticAlgo.NUM_GENERATIONS = sc.nextInt();

            // Get size of population.
            System.out.println("Size of population: ");
            GeneticAlgo.POPULATION_SIZE = sc.nextInt();

            // Get mutation rate
            System.out.println("Mutation rate: ");
            GeneticAlgo.MUTATION_RATE = sc.nextDouble();

            System.out.println("Tournament selection size: ");
            GeneticAlgo.TOURNAMENT_SELECTION_SIZE = sc.nextInt();

            // Get number of elite knapsacks
            System.out.println("Number of elite knapsacks: ");
            GeneticAlgo.NUM_ELITE_KNAPSACKS = sc.nextInt();

            System.out.println("Crossover method:");
            for (int i = 0; i < GeneticAlgo.CrossoverMethod.values().length; i++) {
                System.out.println(i + ": " + GeneticAlgo.CrossoverMethod.values()[i].name());
            }
            GeneticAlgo.CROSSOVER_METHOD = GeneticAlgo.CrossoverMethod.values()[sc.nextInt()];

            System.out.println("Print knapsacks (will disable timing)? (true/false): ");
            GeneticAlgo.VERBOSE = sc.nextBoolean();
        } else { // Use the console/terminal
            // Get number of generations.
            String line = console.readLine("Number of generations to breed: ").trim();
            String response = getCommand(line);
            GeneticAlgo.NUM_GENERATIONS = Integer.parseInt(response);

            // Get size of population.
            line = console.readLine("Size of population: ").trim();
            response = getCommand(line);
            GeneticAlgo.POPULATION_SIZE = Integer.parseInt(response);

            // Get mutation rate
            line = console.readLine("Mutation rate: ").trim();
            response = getCommand(line);
            GeneticAlgo.MUTATION_RATE = Double.parseDouble(response);

            line = console.readLine("Tournament selection size: ").trim();
            response = getCommand(line);
            GeneticAlgo.TOURNAMENT_SELECTION_SIZE = Integer.parseInt(response);

            // Get number of elite knapsacks
            line = console.readLine("Number of elite knapsacks: ").trim();
            response = getCommand(line);
            GeneticAlgo.NUM_ELITE_KNAPSACKS = Integer.parseInt(response);

            line = console.readLine("Crossover method: ").trim();
            for (int i = 0; i < GeneticAlgo.CrossoverMethod.values().length; i++) {
                System.out.println(i + ": " + GeneticAlgo.CrossoverMethod.values()[i].name());
            }
            response = getCommand(line);
            GeneticAlgo.CROSSOVER_METHOD = GeneticAlgo.CrossoverMethod.values()[Integer.parseInt(response)];

            line = console.readLine("Print knapsacks (will disable timing)? (true/false): ").trim();
            response = getCommand(line);
            GeneticAlgo.VERBOSE = Boolean.parseBoolean(response);
        }
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        ArrayList<Item> initialGenome = new ArrayList<>();
        int maxWeight = 0;
        try (FileReader reader = new FileReader("knapsack.json")) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONObject file = (JSONObject) obj;
            maxWeight = Math.toIntExact((long) file.get("Max Weight"));
            JSONArray itemList = (JSONArray) file.get("Items");

            if (GeneticAlgo.VERBOSE) {
                System.out.println("Items:");
            }

            for (int i = 0; i < itemList.size(); i++) {
                JSONObject item = (JSONObject) itemList.get(i);
                Item itemToAdd = new Item((String) item.get("Name"),
                        Math.toIntExact((long) item.get("Value")),
                        Math.toIntExact((long) item.get("Weight")),
                        Math.toIntExact((long) item.get("Quantity")), 0);
                initialGenome.add(itemToAdd);
                if (GeneticAlgo.VERBOSE) {
                    //System.out.println(itemToAdd);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not find knapsack .json file.");
        } catch (IOException | ParseException e) {
            System.out.println("Error while parsing.");
        }
        //Test config
        for (Item item : initialGenome) {
            int amount = (int) (Math.random() * item.getAvailable());
            item.setUsed(amount);
        }
        initialKnapsack = new Knapsack(maxWeight, initialGenome);
        

        double start = System.currentTimeMillis();

        Population population = new Population(GeneticAlgo.POPULATION_SIZE, initialKnapsack); // Create a population based on this initial knapsack.
        GeneticAlgo algo = new GeneticAlgo(initialKnapsack);
        int numGenerations = 0;
        if (GeneticAlgo.VERBOSE) {
            printPopulation(population);
        }

        while (numGenerations++ < GeneticAlgo.NUM_GENERATIONS) { // Iterate through generations, evolving each time.
            population = algo.evolve(population); // Evolve the population
            population.sortKnapsacksByFitness();
            if (GeneticAlgo.VERBOSE) {
                printPopulation(population); // Print the current population
                printBestKnapsack(population.getKnapsacks().get(0), numGenerations, false); // Print the best one
            }
        }
        printBestKnapsack(population.getKnapsacks().get(0), numGenerations, true);

        if (!GeneticAlgo.VERBOSE) {
            double secondsTaken = (System.currentTimeMillis() - start) / 1000;
            System.out.println("\nFinished in " + (int) (secondsTaken / 60) + " minutes " + (int) (secondsTaken % 60) + " seconds");
        }

    }

    /**
     *
     * @param knapsack the best knapsack
     * @param genNum the number of the current generation
     * @param summary whether or not we are done with this simulation and should
     * print the best overall
     */
    private static void printBestKnapsack(Knapsack knapsack, int genNum, boolean summary) {
        if (summary) {
            System.out.println("\nBest knapsack (Weight: " + knapsack.getWeight() + " | Max weight: " + knapsack.getMaxWeight() + ") after " + genNum + " generations [$" + knapsack.getFitness() + "]: ");
            for (int i = 0; i < knapsack.genome().size(); i++) { // Print the knapsack
                System.out.println(knapsack.genome().get(i).toString());
            }
        } else {
            System.out.print("\nGENERATION #" + genNum + " BEST KNAPSACK [" + (int) knapsack.getFitness() + "]: ");
            for (int i = 0; i < knapsack.genome().size(); i++) { // Print the knapsack
                System.out.println(knapsack.genome().get(i).toString());
            }
        }
    }

    public static void printPopulation(Population population) {
        for (Knapsack x : population.getKnapsacks()) {
            for (int i = 0; i < x.genome().size(); i++) { // Print the knapsack
                System.out.println("\t" + x.genome().get(i).toString() + ", ");
            }
            System.out.println(x.getFitness());
        }
    }

    private static String getArgument(String line, int index) {
        String[] words = line.split("\\s");
        return words.length > index ? words[index] : "";
    }

    private static String getCommand(String line) {
        return getArgument(line, 0);
    }

}
