package de.focus_shift.launchpad.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

class LaunchpadServiceImplTest {

  private final LaunchpadServiceImpl sut =
      service(app("https://public.example.org", null), app("https://admin.example.org", "ADMIN"));

  @Test
  void ignoresAppsWhoseUrlCannotBeBuilt() {
    final LaunchpadServiceImpl service =
        service(app("no-scheme.example.org", null), app("https://public.example.org", null));

    assertThat(appUrls(service, new TestingAuthenticationToken("user", "", "USER")))
        .containsExactly("https://public.example.org");
  }

  @Test
  void showsAppsWithAuthorityToUsersWithThatAuthority() {
    assertThat(appUrls(sut, new TestingAuthenticationToken("admin", "", "ADMIN")))
        .containsExactly("https://public.example.org", "https://admin.example.org");
  }

  @Test
  void hidesAppsWithAuthorityFromUsersWithoutThatAuthority() {
    assertThat(appUrls(sut, new TestingAuthenticationToken("user", "", "USER")))
        .containsExactly("https://public.example.org");
  }

  @Test
  void hidesAppsWithAuthorityFromAnonymousUsers() {
    assertThat(appUrls(sut, null)).containsExactly("https://public.example.org");
  }

  private static List<String> appUrls(LaunchpadServiceImpl service, Authentication authentication) {
    return service.getLaunchpad(authentication).getApps().stream()
        .map(app -> app.getUrl().toString())
        .toList();
  }

  private static LaunchpadServiceImpl service(LaunchpadConfigProperties.App... apps) {
    final LaunchpadConfigProperties properties = new LaunchpadConfigProperties();
    properties.setNameDefaultLocale(Locale.GERMAN);
    properties.setApps(List.of(apps));
    return new LaunchpadServiceImpl(properties, url -> URI.create(url).toURL());
  }

  private static LaunchpadConfigProperties.App app(String url, String authority) {
    final LaunchpadConfigProperties.App app = new LaunchpadConfigProperties.App();
    app.setUrl(url);
    app.setName(Map.of(Locale.GERMAN, "Anwendung"));
    app.setIcon("icon");
    app.setAuthority(authority);
    return app;
  }
}
