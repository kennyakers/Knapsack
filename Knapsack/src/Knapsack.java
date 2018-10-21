/**
 * Kenny Akers
 * Mr. Paige
 * Homework #
 *
 */
public class Knapsack implements Organism{

    public Item[] items;
    public final int maxWeight;
    
    public Knapsack(int maxWeight, int[] genome){
        this.maxWeight = maxWeight;
        
        
    }
    
    public static void main(String[] args) {
        
    }
    
    public int fitness(){
        int sum = 0;
        for(Item i : items){
            sum += i.value * i.used;
        }
        return sum;
    }
    
    public int[] genome(){
        int[] arr = new int[items.length];
        for(int i = 0; i < arr.length; i++){
            arr[i] = items[i].used;
        }
        return arr;
    }
    
    public boolean isLegal(){
        int sum = 0;
        for(Item i : items)
            sum += i.used * i.weight;
        
        return sum <= maxWeight;
    }

    public class Item{
        String name;
        public int value;
        public int weight;
        public int available;
        public int used;
        public Item(String name, int value, int weight, int available, int used){
            this.name = name;
            this.value = value;
            this.weight = weight;
            this.available = available;
            this.used = used;
        }
    }
    
}
