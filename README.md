# Launchpad

## Requirements

This project requires Java 21 compiler.


## Usage

- Include Launchpad to your project:
  ```xml
  <dependency>
    <groupId>de.focus-shift</groupId>
    <artifactId>launchpad</artifactId>
    <version>...</version>
  </dependency>
  ```
  - Choose the right version of Launchpad for your Spring Boot version:
    - 3.x is using spring boot 4.x as a parent
    - 1.x is using spring boot 3.x as a parent
    - 0.x is using spring boot 2.x as a parent
- Configure launchpad apps in your `application.yml`
  ```yaml
  launchpad:
    name-default-locale: de
    apps[0]:
      icon: data:image/png;base64,xxxxxxx
      name:
        de: name-1-de
        en: name-1-en
      url: https://link-1
    apps[1]:
      icon: /static/images/app-icon.png
      name:
        de: name-2-de
        en: name-2-en
      url: https://link-2
  ```
- Assign `HasLaunchpad` to all Controllers you want the launchpad to be available

### Advanced

#### Launchpad on Error Pages

Create a custom `ErrorControllerAdvice` and use the `LaunchpadModelAttributeAppender`
to prepare the required launchpad model attributes.

Example:

```java
@ControllerAdvice(assignableTypes = ErrorController.class)
@Conditional(LaunchpadAutoConfiguration.LaunchpadAppsCondition.class)
class ErrorControllerAdvice {

    private final LaunchpadModelAttributeAppender launchpadModelAttributeAppender;

    ErrorControllerAdvice(LaunchpadModelAttributeAppender launchpadModelAttributeAppender) {
        this.launchpadModelAttributeAppender = launchpadModelAttributeAppender;
    }

    @ModelAttribute
    public void addAttributes(Model model, Locale locale, Authentication authentication) {
        launchpadModelAttributeAppender.addModelAttribute(model, locale, authentication);
    }
}
```

You can inject `HttpServletRequest` to check the requested URL with
`request.getAttribute("jakarta.servlet.error.request_uri")` and only call the
`LaunchpadModelAttributeAppender` if the requested URL matches your condition.


## License

Launchpad is Open Source software released under the [Apache License 2.0](LICENSE).
