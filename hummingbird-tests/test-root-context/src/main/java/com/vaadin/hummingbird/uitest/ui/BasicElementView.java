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
package com.vaadin.hummingbird.uitest.ui;

import java.util.Set;

import com.vaadin.hummingbird.dom.Element;
import com.vaadin.hummingbird.dom.ElementFactory;
import com.vaadin.hummingbird.dom.Style;

public class BasicElementView extends AbstractDivView {

    @Override
    protected void onShow() {
        Element mainElement = getElement();
        mainElement.getStyle().set("margin", "1em");

        Element button = ElementFactory.createButton("Click me");

        Element input = ElementFactory.createInput()
                .setAttribute("placeholder", "Synchronized on change event")
                .setSynchronizedProperties("value")
                .setSynchronizedPropertiesEvents("change");

        button.addEventListener("click", e -> {
            String buttonText = e.getEventData()
                    .getString("element.textContent");

            Element greeting = ElementFactory
                    .createDiv("Thank you for clicking at \"" + buttonText
                            + "\"! The field value is "
                            + input.getProperty("value"));
            greeting.setAttribute("class", "thankYou");
            greeting.addEventListener("click",
                    e2 -> greeting.removeFromParent());

            mainElement.appendChild(greeting);
        } , "element.textContent");

        Element helloWorldElement = ElementFactory.createDiv("Hello world");

        Set<String> spanClasses = helloWorldElement.getClassList();

        helloWorldElement.setProperty("id", "hello-world");
        spanClasses.add("hello");
        helloWorldElement.addEventListener("click", e -> {
            helloWorldElement.setTextContent("Stop touching me!");
            spanClasses.clear();
        });
        Style s = helloWorldElement.getStyle();
        s.set("color", "red");
        s.set("fontWeight", "bold");

        mainElement.appendChild(helloWorldElement, button, input);
    }

}