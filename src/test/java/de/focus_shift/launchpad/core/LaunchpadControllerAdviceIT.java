package de.focus_shift.launchpad.core;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import de.focus_shift.launchpad.api.HasLaunchpad;
import de.focus_shift.launchpad.api.LaunchpadAppUrlCustomizer;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootTest(classes = {LaunchpadControllerAdvice.class})
@ContextConfiguration(classes = {LaunchpadControllerAdviceIT.TestConfig.class})
@AutoConfigureMockMvc
class LaunchpadControllerAdviceIT {

  @Autowired private LaunchpadControllerAdvice sut;

  @Autowired private TestConfig.LaunchpadController launchpadController;

  @Autowired private TestConfig.OtherController otherController;

  @MockBean private LaunchpadServiceImpl launchpadService;

  @Autowired private MockMvc mockMvc;

  @Test
  @WithMockUser
  void controllerWithLaunchpad() throws Exception {

    when(launchpadService.getLaunchpad(any(Authentication.class)))
        .thenReturn(
            new Launchpad(
                List.of(
                    new App(new URL("https://example.org"), new AppName("App 1", Map.of()), "icon"),
                    new App(
                        new URL("https://example-2.org"),
                        new AppName("App 2", Map.of()),
                        "icon-2"))));

    mockMvc
        .perform(get("/launchpad"))
        .andExpect(
            model()
                .attribute(
                    "launchpad",
                    new LaunchpadDto(
                        List.of(
                            new AppDto("https://example.org", "App 1", "icon"),
                            new AppDto("https://example-2.org", "App 2", "icon-2")))));
  }

  @Test
  @WithMockUser
  void controllerWithoutLaunchpad() throws Exception {

    mockMvc.perform(get("/no-launchpad")).andExpect(model().attributeDoesNotExist("launchpad"));

    verifyNoInteractions(launchpadService);
  }

  @TestConfiguration
  static class TestConfig {

    @Bean
    LaunchpadAppUrlCustomizer appUrlCustomizer() {
      return URL::new;
    }

    @Controller
    @RequestMapping("/launchpad")
    static class LaunchpadController implements HasLaunchpad {
      @GetMapping
      String launchpad() {
        return "some-view";
      }
    }

    @Controller
    @RequestMapping("/no-launchpad")
    static class OtherController {
      @GetMapping
      String noLaunchpad() {
        return "some-view";
      }
    }
  }
}
