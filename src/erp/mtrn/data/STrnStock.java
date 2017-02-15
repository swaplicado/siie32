/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

/**
 *
 * @author Edwin Carmona
 */

public class STrnStock {
    
    private double mdMovementIn;
    private double mdMovementOut;
    
    private double mdSegregationIncreases;
    private double mdSegregationDecreases;

    STrnStock() {
        mdMovementIn = 0d;
        mdMovementOut = 0d;
        mdSegregationIncreases = 0d;
        mdSegregationDecreases = 0d;
    }
    
    public void setMovementIn(double d) { mdMovementIn = d; }
    public void setMovementOut(double d) { mdMovementOut = d; }    
    public void setSegregationIncreases(double d) { mdSegregationIncreases = d; }
    public void setSegregationDecreases(double d) { mdSegregationDecreases = d; }
    
    public double getMovementIn() { return mdMovementIn; }
    public double getMovementOut() { return mdMovementOut; }
    public double getSegregationIncreases() { return mdSegregationIncreases; }
    public double getSegregationDecreases() { return mdSegregationDecreases; }
    
    public double getStock() { return mdMovementIn - mdMovementOut; }
    public double getSegregatedStock() { return mdSegregationIncreases - mdSegregationDecreases; }
    
    public double getAvailableStock() { return getStock() - getSegregatedStock(); }
}
