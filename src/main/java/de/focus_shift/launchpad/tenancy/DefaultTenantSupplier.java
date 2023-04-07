package de.focus_shift.launchpad.tenancy;

class DefaultTenantSupplier implements TenantSupplier {

  private final String id;

  DefaultTenantSupplier(String id) {
    this.id = id;
  }

  @Override
  public String get() {
    return id;
  }
}
