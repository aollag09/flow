/*
 * Copyright 2000-2014 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.ui;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Extension to {@link AbstractComponent} that defines the default
 * implementation for the methods in {@link ComponentContainer}. Basic UI
 * components that need to contain other components inherit this class to easily
 * qualify as a component container.
 *
 * @author Vaadin Ltd
 * @since 3.0
 */
@SuppressWarnings("serial")
public abstract class AbstractComponentContainer
        extends AbstractJavaScriptComponent implements ComponentContainer {

    /**
     * Constructs a new component container.
     */
    public AbstractComponentContainer() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.ui.ComponentContainer#addComponents(com.vaadin.ui.Component[])
     */
    @Override
    public void addComponents(Component... components) {
        for (Component c : components) {
            addComponent(c);
        }
    }

    /**
     * Removes all components from the container. This should probably be
     * re-implemented in extending classes for a more powerful implementation.
     */
    @Override
    public void removeAllComponents() {
        final LinkedList<Component> l = new LinkedList<Component>();

        // Adds all components
        for (final Iterator<Component> i = iterator(); i.hasNext();) {
            l.add(i.next());
        }

        // Removes all component
        for (final Iterator<Component> i = l.iterator(); i.hasNext();) {
            removeComponent(i.next());
        }
    }

    /*
     * Moves all components from an another container into this container. Don't
     * add a JavaDoc comment here, we use the default documentation from
     * implemented interface.
     */
    @Override
    public void moveComponentsFrom(ComponentContainer source) {
        final LinkedList<Component> components = new LinkedList<Component>();
        for (final Iterator<Component> i = source.iterator(); i.hasNext();) {
            components.add(i.next());
        }

        for (final Iterator<Component> i = components.iterator(); i
                .hasNext();) {
            final Component c = i.next();
            source.removeComponent(c);
            addComponent(c);
        }
    }

    /**
     * This only implements the events and component parent calls. The extending
     * classes must implement component list maintenance and call this method
     * after component list maintenance.
     *
     * @see com.vaadin.ui.ComponentContainer#addComponent(Component)
     */
    @Override
    public void addComponent(Component c) {
        // Make sure we're not adding the component inside it's own content
        if (isOrHasAncestor(c)) {
            throw new IllegalArgumentException(
                    "Component cannot be added inside it's own content");
        }

        if (c.getParent() != null) {
            // If the component already has a parent, try to remove it
            AbstractSingleComponentContainer.removeFromParent(c);
        }

        c.setParent(this);
        markAsDirty();
    }

    /**
     * This only implements the events and component parent calls. The extending
     * classes must implement component list maintenance and call this method
     * before component list maintenance.
     *
     * @see com.vaadin.ui.ComponentContainer#removeComponent(Component)
     */
    @Override
    public void removeComponent(Component c) {
        if (equals(c.getParent())) {
            c.setParent(null);
            markAsDirty();
        }
    }

}
