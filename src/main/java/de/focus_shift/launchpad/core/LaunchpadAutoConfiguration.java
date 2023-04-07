package de.focus_shift.launchpad.core;

import de.focus_shift.launchpad.api.LaunchpadAppUrlCustomizer;
import java.net.URL;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Configuration
@EnableConfigurationProperties(LaunchpadConfigProperties.class)
@ConditionalOnProperty(prefix = "launchpad", name = "name-default-locale")
public class LaunchpadAutoConfiguration {

  @Conditional(LaunchpadAppsCondition.class)
  static class LaunchpadConfig {

    @Bean
    LaunchpadControllerAdvice launchpadControllerAdvice(LaunchpadService launchpadService) {
      return new LaunchpadControllerAdvice(launchpadService);
    }

    @Bean
    LaunchpadService launchpadService(
        LaunchpadConfigProperties launchpadConfigProperties,
        LaunchpadAppUrlCustomizer appUrlCustomizer) {
      return new LaunchpadServiceImpl(launchpadConfigProperties, appUrlCustomizer);
    }

    @Bean
    @ConditionalOnMissingBean
    LaunchpadAppUrlCustomizer appUrlCustomizer() {
      return URL::new;
    }
  }

  /** Checks if 'launchpad.apps' are configured appropriate. */
  // copied and adjusted from
  // org.springframework.boot.autoconfigure.condition.OnPropertyListCondition
  static class LaunchpadAppsCondition extends SpringBootCondition {
    private static final Bindable<List<LaunchpadConfigProperties.App>> APP_LIST =
        Bindable.listOf(LaunchpadConfigProperties.App.class);
    private static final String PROPERTY_NAME = "launchpad.apps";

    @Override
    public ConditionOutcome getMatchOutcome(
        ConditionContext context, AnnotatedTypeMetadata metadata) {
      final BindResult<?> property =
          Binder.get(context.getEnvironment()).bind(PROPERTY_NAME, APP_LIST);
      final ConditionMessage.Builder messageBuilder = ConditionMessage.forCondition("apps");
      if (property.isBound()) {
        return ConditionOutcome.match(messageBuilder.found("property").items(PROPERTY_NAME));
      }
      return ConditionOutcome.noMatch(messageBuilder.didNotFind("property").items(PROPERTY_NAME));
    }
  }
}
