/*
 * Copyright 2000-2016 Vaadin Ltd.
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
package com.vaadin.hummingbird.dom.impl;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.vaadin.hummingbird.StateNode;
import com.vaadin.hummingbird.dom.ClassList;
import com.vaadin.hummingbird.dom.DomEventListener;
import com.vaadin.hummingbird.dom.Element;
import com.vaadin.hummingbird.dom.ElementStateProvider;
import com.vaadin.hummingbird.dom.EventRegistrationHandle;
import com.vaadin.hummingbird.dom.Style;
import com.vaadin.hummingbird.namespace.ComponentMappingNamespace;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;

import elemental.json.JsonValue;

/**
 * Abstract element state provider for text nodes. Operations that are not
 * applicable for text nodes throw {@link UnsupportedOperationException}.
 *
 * @since
 * @author Vaadin Ltd
 */
public abstract class AbstractTextElementStateProvider
        implements ElementStateProvider {

    @Override
    public boolean isTextNode(StateNode node) {
        return true;
    }

    @Override
    public String getTag(StateNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttribute(StateNode node, String attribute, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAttribute(StateNode node, String attribute) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasAttribute(StateNode node, String attribute) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttribute(StateNode node, String attribute) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stream<String> getAttributeNames(StateNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getChildCount(StateNode node) {
        return 0;
    }

    @Override
    public Element getChild(StateNode node, int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertChild(StateNode node, int index, Element child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeChild(StateNode node, int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeChild(StateNode node, Element child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAllChildren(StateNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EventRegistrationHandle addEventListener(StateNode node,
            String eventType, DomEventListener listener,
            String[] eventDataExpressions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getProperty(StateNode node, String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setProperty(StateNode node, String name, Serializable value,
            boolean emitChange) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setJsonProperty(StateNode node, String name, JsonValue value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeProperty(StateNode node, String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasProperty(StateNode node, String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stream<String> getPropertyNames(StateNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClassList getClassList(StateNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Style getStyle(StateNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getSynchronizedProperties(StateNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getSynchronizedPropertyEvents(StateNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setComponent(StateNode node, Component component) {
        assert node != null;
        assert component != null;
        node.getNamespace(ComponentMappingNamespace.class)
                .setComponent(component);
    }

    @Override
    public Optional<Component> getComponent(StateNode node) {
        assert node != null;
        return node.getNamespace(ComponentMappingNamespace.class)
                .getComponent();
    }

    @Override
    public void setAttribute(StateNode node, String attribute,
            StreamResource resource) {
        throw new UnsupportedOperationException();
    }

}