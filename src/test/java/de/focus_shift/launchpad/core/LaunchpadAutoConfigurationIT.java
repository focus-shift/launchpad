package de.focus_shift.launchpad.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.ui.ExtendedModelMap;

class LaunchpadAutoConfigurationIT {

  // no component scan: the launchpad must be set up by auto-configuration alone
  @Configuration(proxyBeanMethods = false)
  @EnableAutoConfiguration
  static class ConsumerApp {}

  private final WebApplicationContextRunner runner =
      new WebApplicationContextRunner()
          .withUserConfiguration(ConsumerApp.class)
          .withPropertyValues(
              "launchpad.name-default-locale=de",
              "launchpad.apps[0].name.de=Anwendung",
              "launchpad.apps[0].icon=icon");

  @Test
  void addsLaunchpad() {
    runner
        .withPropertyValues("launchpad.apps[0].url=https://example.org")
        .run(context -> assertThat(firstAppUrl(context)).isEqualTo("https://example.org"));
  }

  @Test
  void usesTenantIdWhenTenancyIsEnabled() {
    runner
        .withPropertyValues(
            "launchpad.apps[0].url=https://{tenantId}.example.org",
            "launchpad.tenant.enabled=true",
            "launchpad.tenant.default-supplier.id=acme")
        .run(context -> assertThat(firstAppUrl(context)).isEqualTo("https://acme.example.org"));
  }

  private static String firstAppUrl(ApplicationContext context) {
    final ExtendedModelMap model = new ExtendedModelMap();
    context
        .getBean(LaunchpadModelAttributeAppender.class)
        .addModelAttribute(model, Locale.GERMAN, new TestingAuthenticationToken("user", ""));
    return ((LaunchpadDto) model.get("launchpad")).apps().get(0).getUrl();
  }
}
