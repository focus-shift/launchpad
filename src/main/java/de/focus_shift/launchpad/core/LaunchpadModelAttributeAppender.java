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
        launchpad.getApps().stream()
            .map(
                app ->
                    new AppDto(
                        app.getUrl().toString(), app.getAppName().get(locale), app.getIcon()))
            .toList();

    if (!appDtos.isEmpty()) {
      model.addAttribute("launchpad", new LaunchpadDto(appDtos));
    }
  }
}
