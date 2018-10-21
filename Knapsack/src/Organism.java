
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author aidanchandra
 */
public interface Organism<T> {

    public boolean isLegal();

    public ArrayList<T> genome();

    public int getFitness();
}
