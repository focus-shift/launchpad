package de.focus_shift.launchpad.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.expression.spel.standard.SpelExpressionParser;

class LaunchpadDtoTest {

  // templates read the model attribute with property syntax, e.g. ${launchpad.apps[0].url}
  @Test
  void canBeReadWithPropertySyntaxInTemplates() {
    final LaunchpadDto launchpad = new LaunchpadDto(List.of(new AppDto("url", "name", "icon")));

    assertThat(
            new SpelExpressionParser()
                .parseExpression("apps[0].url + apps[0].name + apps[0].icon")
                .getValue(launchpad))
        .isEqualTo("urlnameicon");
  }
}
