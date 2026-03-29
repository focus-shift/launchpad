package de.focus_shift.launchpad.core;

import de.focus_shift.launchpad.api.HasLaunchpad;
import java.util.Locale;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = {HasLaunchpad.class})
public class LaunchpadControllerAdvice {

  private final LaunchpadModelAttributeAppender modelAttributeAppender;

  LaunchpadControllerAdvice(LaunchpadModelAttributeAppender modelAttributeAppender) {
    this.modelAttributeAppender = modelAttributeAppender;
  }

  @ModelAttribute
  public void addAttributes(Model model, Locale locale, Authentication authentication) {
    modelAttributeAppender.addModelAttribute(model, locale, authentication);
  }
}
