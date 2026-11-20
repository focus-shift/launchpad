package de.focus_shift.launchpad.core;

import java.net.URL;

/**
 * @param authority required to see the app, or {@code null} if everyone may see it
 */
public record App(URL url, AppName appName, String icon, String authority) {}
