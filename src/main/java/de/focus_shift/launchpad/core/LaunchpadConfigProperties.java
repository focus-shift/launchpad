package de.focus_shift.launchpad.core;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for the Launchpad application.
 *
 * <p>Bound from properties prefixed with {@code launchpad}. Supports validation via Jakarta Bean
 * Validation annotations.
 */
@Validated
@ConfigurationProperties(prefix = "launchpad")
class LaunchpadConfigProperties {

  /** Default locale used for app names when no locale-specific name is available. */
  @NotNull private Locale nameDefaultLocale;

  /** List of apps registered in the launchpad. */
  private List<App> apps = List.of();

  public Locale getNameDefaultLocale() {
    return nameDefaultLocale;
  }

  public void setNameDefaultLocale(Locale nameDefaultLocale) {
    this.nameDefaultLocale = nameDefaultLocale;
  }

  List<App> getApps() {
    return apps;
  }

  void setApps(List<App> apps) {
    this.apps = apps;
  }

  /**
   * Represents a single app registered in the Launchpad.
   */
  @Validated
  static class App {

    /** Absolute URL of the app (must be HTTP or HTTPS). */
    @NotNull private String url;

    /** Localized names for the app, keyed by locale. */
    @NotNull private Map<Locale, String> name;

    /** Icon identifier for the app. Must not be empty. */
    @NotEmpty private String icon;

    /** Optional authority required to access the app. */
    private String authority;

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public Map<Locale, String> getName() {
      return name;
    }

    public void setName(Map<Locale, String> name) {
      this.name = name;
    }

    public String getIcon() {
      return icon;
    }

    public void setIcon(String icon) {
      this.icon = icon;
    }

    public String getAuthority() {
      return authority;
    }

    public void setAuthority(String authority) {
      this.authority = authority;
    }
  }
}
