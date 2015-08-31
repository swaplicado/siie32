/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import java.util.HashMap;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public interface SAbpCategory {

    public HashMap<Integer, SAbpRegistry> getAbpRows(final SGuiSession session, final int category);
    public void setAbpRows(final int category, final HashMap<Integer, SAbpRegistry> rows);
}
