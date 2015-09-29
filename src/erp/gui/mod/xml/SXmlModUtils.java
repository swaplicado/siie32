package erp.gui.mod.xml;

import sa.lib.SLibConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class SXmlModUtils {
    
    public static int resolveAmbit(String ambit) throws Exception {
        int id = SLibConsts.UNDEFINED;
        
        switch (ambit) {
            case SXmlModConsts.AMBIT_SYS:
                id = SXmlModConsts.AMBIT_SYS_ID;
                break;
            case SXmlModConsts.AMBIT_COM:
                id = SXmlModConsts.AMBIT_COM_ID;
                break;
            case SXmlModConsts.AMBIT_USR:
                id = SXmlModConsts.AMBIT_USR_ID;
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return id;
    }
}
