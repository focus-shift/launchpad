package de.focus_shift.launchpad.core;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class AppName {

  private final String defaultName;
  private final Map<Locale, String> names;

  AppName(String defaultName, Map<Locale, String> names) {
    this.defaultName = defaultName;
    this.names = names;
  }

  public String get(Locale locale) {
    // browsers send e.g. en-US while names are configured as en
    return names.getOrDefault(
        locale, names.getOrDefault(Locale.forLanguageTag(locale.getLanguage()), defaultName));
  }

  @Override
  public String toString() {
    return "AppName{" + "defaultName='" + defaultName + '\'' + ", names=" + names + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AppName appName = (AppName) o;
    return Objects.equals(defaultName, appName.defaultName) && Objects.equals(names, appName.names);
  }

  @Override
  public int hashCode() {
    return Objects.hash(defaultName, names);
  }
}
