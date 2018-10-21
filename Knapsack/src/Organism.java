/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aidanchandra
 */
public interface Organism {
    public boolean isLegal();
    public int[] genome();
    public int fitness();
}
