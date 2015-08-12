package com.vaadin.hummingbird.kernel;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.vaadin.ui.Component;

public class Element {

    private ElementTemplate template;
    private StateNode node;

    public Element(String tag) {
        this(BasicElementTemplate.get(),
                BasicElementTemplate.createBasicElementModel(tag));
    }

    private static Logger getLogger() {
        return Logger.getLogger(Element.class.getName());
    }

    private Element(ElementTemplate template, StateNode node) {
        // Private constructor to force using the static getter that might
        // enable caching at some point
        if (!template.supports(node)) {
            getLogger().warning(
                    "Template " + template + " does not support node " + node);
        }
        this.template = template;
        this.node = node;
    }

    public String getTag() {
        return template.getTag(node);
    }

    public ElementTemplate getTemplate() {
        return template;
    }

    public StateNode getNode() {
        return node;
    }

    public Element setAttribute(String name, String value) {
        assert validAttribute(name);
        template.setAttribute(name, value, node);
        return this;
    }

    private boolean validAttribute(String name) {
        if ("#text".equals(getTag())) {
            assert"content".equals(name) : "Attribute " + name
                    + " is not supported for text nodes";
        }
        return true;
    }

    public Element setAttribute(String name, boolean value) {
        assert validAttribute(name);
        template.setAttribute(name, Boolean.toString(value), node);
        return this;
    }

    public String getAttribute(String name) {
        return template.getAttribute(name, node);
    }

    public boolean hasAttribute(String name) {
        return getAttribute(name) != null;
    }

    public Element addEventListener(String type, EventListener listener) {
        template.addListener(type, listener, node);
        return this;
    }

    public Element removeEventListener(String type, EventListener listener) {
        template.removeListener(type, listener, node);
        return this;
    }

    public int getChildCount() {
        return template.getChildCount(node);
    }

    public Element getChild(int index) {
        return template.getChild(index, node);
    }

    public Element insertChild(int index, Element child) {
        template.insertChild(index, child, node);
        return this;
    }

    public Element appendChild(Element child) {
        insertChild(getChildCount(), child);
        return this;
    }

    /**
     * Removes the element from its parent.
     * <p>
     * Fires a detach event when the element is removed.
     * <p>
     * Has not effect if the element does not have a parent.
     *
     * @return this element
     */
    public Element removeFromParent() {
        Element parent = getParent();
        if (parent != null) {
            parent.template.removeChild(parent.getNode(), this);
        }
        return this;
    }

    public Element getParent() {
        return template.getParent(node);
    }

    public static Element getElement(ElementTemplate template, StateNode node) {
        return new Element(template, node);
    }

    @Override
    public String toString() {
        return getOuterHTML();
    }

    public Collection<String> getAttributeNames() {
        return template.getAttributeNames(node);
    }

    public String getOuterHTML() {
        StringBuilder b = new StringBuilder();
        getOuterHTML(b);
        return b.toString();
    }

    private void getOuterHTML(StringBuilder b) {
        String tag = getTag();
        if ("#text".equals(tag)) {
            String content = getAttribute("content");
            if (content != null) {
                b.append(content);
            }
        } else {
            b.append('<');
            b.append(tag);
            for (String attribute : getAttributeNames()) {
                String value = getAttribute(attribute);
                if (value != null) {
                    b.append(' ');
                    b.append(attribute);
                    b.append("=\"");
                    b.append(value);
                    b.append('\"');
                }
            }
            b.append('>');

            for (int i = 0; i < getChildCount(); i++) {
                getChild(i).getOuterHTML(b);
            }
            b.append("</");
            b.append(tag);
            b.append('>');
        }
    }

    public static Element createText(String content) {
        Element element = new Element("#text");
        element.setAttribute("content", content);
        return element;
    }

    public Element removeAllChildren() {
        while (getChildCount() > 0) {
            getChild(0).removeFromParent();
        }

        return this;
    }

    /**
     * Adds the given attribute value to the given attribute, which consists of
     * a list of values separated by the given separator
     * <p>
     * Has no effect is the attribute already contains the given value
     *
     * @param name
     *            The attribute name
     * @param valueToAdd
     *            the value to add to the attribute
     * @param separator
     *            the separator used between the attribute values
     * @return this element
     */
    protected Element addAttributeValue(String name, String valueToAdd,
            String separator) {
        if (!hasAttribute(name)) {
            return setAttribute(name, valueToAdd);
        } else {
            if (hasAttributeValue(name, valueToAdd, separator)) {
                // Already has the given attribute
                return this;
            }

            return setAttribute(name,
                    getAttribute(name) + separator + valueToAdd);
        }
    }

    /**
     * Checks the given attribute value exists in the given attribute, which
     * consists of a list of values separated by the given separator
     *
     * @param name
     *            The attribute name
     * @param valueToAdd
     *            the value to check for in the attribute
     * @param separator
     *            the separator used between the attribute values
     * @return true if the value exists in the attribute, false otherwise
     */
    protected boolean hasAttributeValue(String name, String valueToAdd,
            String separator) {
        if (hasAttribute(name)) {
            String[] currentValues = getAttribute(name).split(separator);
            for (String s : currentValues) {
                if (s.equals(valueToAdd)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes the given attribute value from the given attribute, which
     * consists of a list of values separated by the given separator
     * <p>
     * Has no effect is the attribute does not contain the given value
     *
     * @param name
     *            The attribute name
     * @param valueToRemove
     *            the value to remove from the attribute
     * @param separator
     *            the separator used between the attribute values
     * @return this element
     */
    protected Element removeAttributeValue(String name, String valueToRemove,
            String separator) {
        if (!hasAttribute(name)) {
            return this;
        }

        String newValue = Arrays.stream(getAttribute(name).split(separator))
                .filter(value -> {
                    return !value.equals(valueToRemove);
                }).collect(Collectors.joining(separator));
        return setAttribute(name, newValue);
    }

    public boolean hasChild(Element element) {
        return equals(element.getParent());
    }

    public int getChildIndex(Element element) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChild(i).equals(element)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Element)) {
            return false;
        }

        Element other = (Element) obj;

        return node.equals(other.node) && template.equals(other.template);
    }

    @Override
    public int hashCode() {
        return node.hashCode() + 31 * template.hashCode();
    }

    public Element removeAttribute(String name) {
        setAttribute(name, null);
        return this;
    }

    /**
     * Removes the given class from the element.
     * <p>
     * Modifies the "class" attribute.
     * <p>
     * Has no effect if the element does not have the given class attribute
     *
     * @param className
     *            the class to remove
     * @return this element
     */
    public Element removeClass(String className) {
        return removeAttributeValue("class", className, " ");
    }

    /**
     * Adds the given class to the element.
     * <p>
     * Modifies the "class" attribute.
     * <p>
     * Has no effect if the element already has the given class name
     *
     * @param className
     *            the class name to add
     * @return this element
     */
    public Element addClass(String className) {
        if (!hasAttribute("class")) {
            setAttribute("class", className);
        } else {
            addAttributeValue("class", className, " ");
        }
        return this;
    }

    /**
     * Checks if the element has the given class
     *
     * @param className
     *            the class name to check for
     * @return true if the class name is set, false otherwise
     */
    public boolean hasClass(String className) {
        return hasAttributeValue("class", className, " ");
    }

    public Element setComponent(Component component) {
        template.setComponent(component, node);
        return this;
    }

    public Component getComponent() {
        return template.getComponent(node);
    }

}
