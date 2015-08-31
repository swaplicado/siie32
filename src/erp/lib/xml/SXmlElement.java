/*
 * Copyright 2010-2013 Software Aplicado SA de CV
 * All rights reserved.
 */

package erp.lib.xml;

import java.util.Vector;

/**
 *
 * @author Sergio Flores
 */
public class SXmlElement implements java.io.Serializable {

    protected String msName;
    protected Vector<SXmlAttribute> mvXmlAttributes;

    public SXmlElement(String name) {
        msName = name;
        mvXmlAttributes = new Vector<>();
    }

    public void setName(String s) { msName = s; }

    public String getName() { return msName; }

    public Vector<SXmlAttribute> getXmlAttributes() { return mvXmlAttributes; }

    public void clear() {
        mvXmlAttributes.clear();
    }

    public String getXmlString() {
        String aux = "";
        String xml = "<" + msName;

        for (SXmlAttribute attribute : mvXmlAttributes) {
            aux = attribute.getXmlString();
            xml += aux.length() == 0 ? "" : " " + aux;
        }

        xml += "/>";

        return xml;
    }

    public SXmlAttribute getXmlAttribute(final String name) {
        SXmlAttribute attribute = null;

        for (SXmlAttribute attributeAux : mvXmlAttributes) {
            if (name.compareTo(attributeAux.getName()) == 0) {
                attribute = attributeAux;
                break;
            }
        }

        return attribute;
    }
}
