package de.focus_shift.launchpad.core;

import java.util.List;
import java.util.Locale;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

public class LaunchpadModelAttributeAppender {

  private final LaunchpadService launchpadService;

  LaunchpadModelAttributeAppender(LaunchpadService launchpadService) {
    this.launchpadService = launchpadService;
  }

  public void addModelAttribute(Model model, Locale locale, Authentication authentication) {

    final Launchpad launchpad = launchpadService.getLaunchpad(authentication);

    final List<AppDto> appDtos =
        launchpad.apps().stream()
            .map(app -> new AppDto(app.url().toString(), app.appName().get(locale), app.icon()))
            .toList();

    if (!appDtos.isEmpty()) {
      model.addAttribute("launchpad", new LaunchpadDto(appDtos));
    }
  }
}
