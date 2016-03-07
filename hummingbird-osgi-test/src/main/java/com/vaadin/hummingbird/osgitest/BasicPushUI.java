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
package com.vaadin.hummingbird.osgitest;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.hummingbird.dom.Element;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Push
public class BasicPushUI extends UI {

    @WebServlet(asyncSupported = true, urlPatterns = { "/" })
    @VaadinServletConfiguration(ui = BasicPushUI.class, productionMode = false)
    public static class PushTestServlet extends VaadinServlet {

    }

    public static final String CLIENT_COUNTER_ID = "clientCounter";

    public static final String STOP_TIMER_ID = "stopTimer";

    public static final String START_TIMER_ID = "startTimer";

    public static final String SERVER_COUNTER_ID = "serverCounter";

    public static final String INCREMENT_BUTTON_ID = "incrementCounter";

    private final Timer timer = new Timer(true);

    private TimerTask task;
    private int clientCounter = 0;
    private int serverCounter = 0;

    private Element serverCounterElement;

    @Override
    protected void init(VaadinRequest request) {
        getReconnectDialogConfiguration().setDialogModal(false);
        spacer();

        // Client counter
        getElement().appendChild(new Element("div").setTextContent(
                "Client counter (click 'increment' to update):"));
        Element lbl = new Element("div").setAttribute("id", CLIENT_COUNTER_ID);
        lbl.setTextContent(clientCounter + "");
        getElement().appendChild(lbl);

        Element button = new Element("button").setAttribute("id",
                INCREMENT_BUTTON_ID);
        button.setTextContent("Increment");
        button.addEventListener("click", e -> {
            clientCounter++;
            lbl.setTextContent(clientCounter + "");
        });

        getElement().appendChild(button);
        spacer();

        /*
         * Server counter
         */
        getElement().appendChild(new Element("div").setTextContent(
                "Server counter (updates each second by server thread):"));
        serverCounterElement = new Element("div").setAttribute("id",
                SERVER_COUNTER_ID);
        serverCounterElement.setTextContent(serverCounter + "");
        getElement().appendChild(serverCounterElement);

        Element startTimer = new Element("button").setAttribute("id",
                START_TIMER_ID);

        startTimer.setTextContent("Start timer");
        startTimer.addEventListener("click", e -> {
            serverCounter = 0;
            if (task != null) {
                task.cancel();
            }
            task = new TimerTask() {
                @Override
                public void run() {
                    access(() -> {
                        serverCounter++;
                        serverCounterElement.setTextContent(serverCounter + "");
                    });
                }
            };
            timer.scheduleAtFixedRate(task, 1000, 1000);
        });
        getElement().appendChild(startTimer);

        Element stopTimer = new Element("button").setAttribute("id",
                STOP_TIMER_ID);
        stopTimer.setTextContent("Stop timer");
        stopTimer.addEventListener("click", e -> {
            if (task != null) {
                task.cancel();
                task = null;
            }
        });
        getElement().appendChild(stopTimer);

    }

    protected void spacer() {
        Element hr = new Element("hr");
        getElement().appendChild(hr);
    }

    @Override
    public void detach() {
        super.detach();
        timer.cancel();
    }

}
