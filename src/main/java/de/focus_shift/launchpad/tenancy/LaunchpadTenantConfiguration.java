package de.focus_shift.launchpad.tenancy;

import de.focus_shift.launchpad.api.LaunchpadAppUrlCustomizer;
import java.net.URI;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LaunchpadTenantConfigProperties.class)
@ConditionalOnProperty(prefix = "launchpad.tenant", name = "enabled", havingValue = "true")
public class LaunchpadTenantConfiguration {

  @Bean
  LaunchpadAppUrlCustomizer appUrlCustomizer(TenantSupplier tenantSupplier) {
    return url -> URI.create(url.replace("{tenantId}", tenantSupplier.get())).toURL();
  }

  @Bean
  @ConditionalOnProperty(
      prefix = "launchpad.tenant.default-supplier",
      name = "enabled",
      havingValue = "true",
      matchIfMissing = true)
  TenantSupplier tenantSupplier(LaunchpadTenantConfigProperties launchpadTenantConfigProperties) {
    String tenantId = launchpadTenantConfigProperties.getDefaultSupplier().getId();
    return new DefaultTenantSupplier(tenantId);
  }
}
