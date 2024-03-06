/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import java.util.ArrayList;

/**
 *
 * @author Edwin Carmona
 */
public class SStockValuationConfiguration {
    
    ArrayList<int[]> iogTpmovsIn;
    ArrayList<int[]> iogTpmovsOut;
    int finTpAccItemAsset;
    int finTpAccItemPur;
    int maxRecEntries;
    String textPurEntries;
    String textAssetEntries;

    public ArrayList<int[]> getIogTpmovsIn() {
        return iogTpmovsIn;
    }

    public void setIogTpmovsIn(ArrayList<int[]> iogTpmovsIn) {
        this.iogTpmovsIn = iogTpmovsIn;
    }

    public ArrayList<int[]> getIogTpmovsOut() {
        return iogTpmovsOut;
    }

    public void setIogTpmovsOut(ArrayList<int[]> iogTpmovsOut) {
        this.iogTpmovsOut = iogTpmovsOut;
    }

    public int getFinTpAccItemAsset() {
        return finTpAccItemAsset;
    }

    public void setFinTpAccItemAsset(int finTpAccItemAsset) {
        this.finTpAccItemAsset = finTpAccItemAsset;
    }

    public int getFinTpAccItemPur() {
        return finTpAccItemPur;
    }

    public void setFinTpAccItemPur(int finTpAccItemPur) {
        this.finTpAccItemPur = finTpAccItemPur;
    }

    public int getMaxRecEntries() {
        return maxRecEntries;
    }

    public void setMaxRecEntries(int maxRecEntries) {
        this.maxRecEntries = maxRecEntries;
    }

    public String getTextPurEntries() {
        return textPurEntries;
    }

    public void setTextPurEntries(String textPurEntries) {
        this.textPurEntries = textPurEntries;
    }

    public String getTextAssetEntries() {
        return textAssetEntries;
    }

    public void setTextAssetEntries(String textAssetEntries) {
        this.textAssetEntries = textAssetEntries;
    }
    
}
