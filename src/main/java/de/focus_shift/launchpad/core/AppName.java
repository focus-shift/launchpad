package de.focus_shift.launchpad.core;

import java.util.Locale;
import java.util.Map;

public record AppName(String defaultName, Map<Locale, String> names) {

  public String get(Locale locale) {
    // browsers send e.g. en-US while names are configured as en
    return names.getOrDefault(
        locale, names.getOrDefault(Locale.of(locale.getLanguage()), defaultName));
  }
}
