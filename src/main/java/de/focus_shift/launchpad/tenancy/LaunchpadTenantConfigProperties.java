package de.focus_shift.launchpad.tenancy;

import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "launchpad.tenant")
class LaunchpadTenantConfigProperties {

  private boolean enabled = false;

  @NotNull private DefaultTenantSupplierConfigProperties defaultSupplier;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public DefaultTenantSupplierConfigProperties getDefaultSupplier() {
    return defaultSupplier;
  }

  public void setDefaultSupplier(DefaultTenantSupplierConfigProperties defaultSupplier) {
    this.defaultSupplier = defaultSupplier;
  }

  static class DefaultTenantSupplierConfigProperties {
    @NotNull private String id = "default";

    private boolean enabled = false;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }
  }
}
