/**
 * cdp4j - Chrome DevTools Protocol for Java
 * Copyright © 2017, 2018 WebFolder OÜ (support@webfolder.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.webfolder.cdp;

import io.webfolder.cdp.exception.CdpException;
import io.webfolder.cdp.session.SessionFactory;
import io.webfolder.cdp.session.SessionInfo;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.lang.Thread.sleep;
import static java.util.Collections.emptyList;

abstract class AbstractLauncher {
    protected final SessionFactory factory;

    public AbstractLauncher(final SessionFactory factory) {
        this.factory = factory;
    }

    protected List<String> getCommonParameters(String chromeExecutablePath, List<String> arguments) {
        List<String> list = new ArrayList<>();
        list.add(chromeExecutablePath);

        if (factory.getPort() > 0) {
            list.add(format("--remote-debugging-port=%d", factory.getPort()));
        }

        // Disable built-in Google Translate service
        list.add("--disable-translate");
        // Disable all chrome extensions entirely
        list.add("--disable-extensions");
        // Disable various background network services, including extension updating,
        // safe browsing service, upgrade detector, translate, UMA
        list.add("--disable-background-networking");
        // Disable fetching safebrowsing lists, likely redundant due to disable-background-networking
        list.add("--safebrowsing-disable-auto-update");
        // Disable syncing to a Google account
        list.add("--disable-sync");
        // Disable reporting to UMA, but allows for collection
        list.add("--metrics-recording-only");
        // Disable installation of default apps on first run
        list.add("--disable-default-apps");
        // Mute any audio
        list.add("--mute-audio");
        // Skip first run wizards
        list.add("--no-first-run");
        list.add("--no-default-browser-check");
        list.add("--disable-plugin-power-saver");
        list.add("--disable-popup-blocking");

        if (!arguments.isEmpty()) {
            list.addAll(arguments);
        }
        return list;
    }

    public boolean launched() {
        List<SessionInfo> list = emptyList();
        try {
            int timeout = 1000; // milliseconds
            list = factory.list(timeout);
        } catch (Throwable t) {
            // ignore
        }
        return !list.isEmpty() ? true : false;
    }

    public abstract String findChrome();

    public final SessionFactory launch() {
        return launch(findChrome(), emptyList());
    }

    public final SessionFactory launch(List<String> arguments) {
        return launch(findChrome(), arguments);
    }

    public final SessionFactory launch(String chromeExecutablePath, List<String> arguments){
        if (launched()) {
            return factory;
        }

        if (chromeExecutablePath == null || chromeExecutablePath.trim().isEmpty()) {
            throw new CdpException("chrome not found");
        }

        List<String> list = getCommonParameters(chromeExecutablePath, arguments);

        internalLaunch(list, arguments);

        if (!launched()) {
            int counter = 0;
            final int maxCount = 20;
            while (!launched() && counter < maxCount) {
                try {
                    sleep(500);
                    counter += 1;
                } catch (InterruptedException e) {
                    break;
                }
            }
        }

        if (!launched()) {
            throw new CdpException("Unable to connect to the chrome remote debugging server [" +
                    factory.getHost() + ":" + factory.getPort() + "]");
        }
        return factory;
    }

    protected abstract void internalLaunch(List<String> list, List<String> params);

    public abstract void kill();
}
