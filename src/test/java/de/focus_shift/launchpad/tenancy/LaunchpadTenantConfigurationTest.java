package de.focus_shift.launchpad.tenancy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class LaunchpadTenantConfigurationTest {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(LaunchpadTenantConfiguration.class)
          .withPropertyValues("launchpad.tenant.enabled=true");

  @Test
  void usesDefaultTenantIdWithoutFurtherConfiguration() {
    runner.run(
        context -> assertThat(context.getBean(TenantSupplier.class).get()).isEqualTo("default"));
  }

  @Test
  void usesConfiguredTenantId() {
    runner
        .withPropertyValues("launchpad.tenant.default-supplier.id=acme")
        .run(context -> assertThat(context.getBean(TenantSupplier.class).get()).isEqualTo("acme"));
  }
}
