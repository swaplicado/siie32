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
public class SXmlElementParent extends SXmlElement {

    protected Vector<SXmlElement> mvXmlElements;

    public SXmlElementParent(String name) {
        super(name);
        mvXmlElements = new Vector<>();
    }

    public Vector<SXmlElement> getXmlElements() { return mvXmlElements; }

    public Vector<SXmlElement> getXmlElements(final String name) {
        Vector<SXmlElement> elements = new Vector<>();

        for (SXmlElement element : mvXmlElements) {
            if (name.compareTo(element.getName()) == 0) {
                elements.add(element);
            }
        }

        return elements;
    }

    public void clear() {
        super.clear();
        mvXmlElements.clear();
    }

    @Override
    public String getXmlString() {
        String aux = "";
        String xml = "<" + msName;

        for (SXmlAttribute attribute : mvXmlAttributes) {
            aux = attribute.getXmlString();
            xml += aux.length() == 0 ? "" : " " + aux;
        }

        xml += ">";

        for (SXmlElement element : mvXmlElements) {
            aux = element.getXmlString();
            xml += aux.length() == 0 ? "" : "\n" + aux;
        }

        xml += "\n</" + msName + ">";

        return xml;
    }
}
