/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.lib.form;

import erp.lib.data.SDataRegistry;
import sa.lib.SLibMethod;

/**
 *
 * @author Sergio Flores
 */
public interface SFormExtendedInterface extends SFormInterface {

    public SLibMethod getPostSaveMethod(SDataRegistry registry);
}
