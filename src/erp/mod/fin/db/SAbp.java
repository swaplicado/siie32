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
public interface SAbp {

    public HashMap<Integer, SAbpRegistry> getAbpRows(final SGuiSession session);
    public void setAbpRows(final HashMap<Integer, SAbpRegistry> rows);
}
