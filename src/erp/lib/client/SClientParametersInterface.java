/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.client;

/**
 *
 * @author Sergio Flores
 */
public interface SClientParametersInterface {

    public int getDecimalsValue();
    public int getDecimalsValueUnitary();
    public int getDecimalsExchangeRate();
    public int getDecimalsPercentage();
    public int getDecimalsDiscount();
    public int getDecimalsQuantity();
    public int getDecimalsUnitsContained();
    public int getDecimalsUnitsVirtual();
    public int getDecimalsNetContent();
    public int getDecimalsLength();
    public int getDecimalsSurface();
    public int getDecimalsVolume();
    public int getDecimalsMass();
    public int getDecimalsWeigthGross();
    public int getDecimalsWeightDelivery();

    public int getLanguageId();
    public int getCurrencyId();
    public int getFormatDateTypeId();
    public int getFormatDatetimeTypeId();
    public int getFormatTimeTypeId();
    public int getDbmsTypeId();
}
