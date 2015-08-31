/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.io.Serializable;

/**
 *
 * @author Sergio Flores
 */
public class STrnDiogComplement implements Serializable {

    protected int[] manDiogTypeKey;
    protected int[] manProdOrderKey;
    protected SDataDps moDpsSource;

    private STrnDiogComplement(int[] diogTypeKey, int[] prodOrderKey, SDataDps dpsSource) {
        manDiogTypeKey = diogTypeKey;
        manProdOrderKey = prodOrderKey;
        moDpsSource = dpsSource;
    }

    public STrnDiogComplement(int[] diogTypeKey) {
        this(diogTypeKey, null, null);
    }

    public STrnDiogComplement(int[] diogTypeKey, int[] prodOrderKey) {
        this(diogTypeKey, prodOrderKey, null);
    }

    public STrnDiogComplement(int[] diogTypeKey, SDataDps dpsSource) {
        this(diogTypeKey, null, dpsSource);
    }

    public void setDiogTypeKey(int[] key) { manDiogTypeKey = key; }
    public void setProdOrderKey(int[] key) { manProdOrderKey = key; }
    public void setDpsSource(SDataDps dps) { moDpsSource = dps; }

    public int[] getDiogTypeKey() { return manDiogTypeKey; }
    public int[] getProdOrderKey() { return manProdOrderKey; }
    public SDataDps getDpsSource() { return moDpsSource; }
}
