package de.focus_shift.launchpad.core;

import static java.util.stream.Collectors.toList;

import de.focus_shift.launchpad.api.HasLaunchpad;
import java.util.List;
import java.util.Locale;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = {HasLaunchpad.class})
public class LaunchpadControllerAdvice {

  private final LaunchpadService launchpadService;

  LaunchpadControllerAdvice(LaunchpadService launchpadService) {
    this.launchpadService = launchpadService;
  }

  @ModelAttribute
  public void addAttributes(Model model, Locale locale, Authentication authentication) {

    final Launchpad launchpad = launchpadService.getLaunchpad(authentication);

    final List<AppDto> appDtos =
        launchpad.getApps().stream()
                .map(app -> new AppDto(app.getUrl().toString(), app.getAppName().get(locale), app.getIcon()))
            .collect(toList());

    if (!appDtos.isEmpty()) {
      model.addAttribute("launchpad", new LaunchpadDto(appDtos));
    }
  }
}
