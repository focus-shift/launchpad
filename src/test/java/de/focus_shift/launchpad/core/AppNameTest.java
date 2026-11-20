package de.focus_shift.launchpad.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.Test;

class AppNameTest {

  private final AppName sut =
      new AppName("Anwendung", Map.of(Locale.GERMAN, "Anwendung", Locale.ENGLISH, "App"));

  @Test
  void usesNameOfLocale() {
    assertThat(sut.get(Locale.ENGLISH)).isEqualTo("App");
  }

  @Test
  void usesNameOfLanguageWhenLocaleHasCountry() {
    assertThat(sut.get(Locale.US)).isEqualTo("App");
  }

  @Test
  void usesDefaultNameForUnknownLanguage() {
    assertThat(sut.get(Locale.FRENCH)).isEqualTo("Anwendung");
  }
}
