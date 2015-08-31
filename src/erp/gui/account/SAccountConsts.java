/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.account;

/**
 *
 * @author Sergio Flores
 */
public abstract class SAccountConsts {

    public static final int LEVELS = 8;
    public static final int LENGTH_LEVEL = 6;
    public static final int LENGTH_ACCOUNT = LENGTH_LEVEL * LEVELS;

    public static final int TYPE_ACCOUNT = 1;
    public static final int TYPE_COST_CENTER = 2;

    public static final String NUM_ACCOUNT = "Número de cuenta";
    public static final String NUM_COST_CENTER = "Número de centro";

    public static final String NAME_ACCOUNT = "Cuenta contable";
    public static final String NAME_COST_CENTER = "Centro de costo";

    public static final String TXT_TERMINAL = "de captura";
    public static final String TXT_EXISTING = "existente";
    public static final String TXT_EXISTING_NON = "inexistente";
}
