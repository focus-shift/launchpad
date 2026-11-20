# Launchpad

## Requirements

| Launchpad | Spring Boot | Java |
|-----------|-------------|------|
| 4.x       | 4.x         | 25+  |
| 3.x       | 4.x         | 17+  |
| 1.x       | 3.x         | 17+  |
| 0.x       | 2.x         | 11+  |


## Usage

- Include Launchpad to your project:
  ```xml
  <dependency>
    <groupId>de.focus-shift</groupId>
    <artifactId>launchpad</artifactId>
    <version>...</version>
  </dependency>
  ```
  Choose the version matching your Spring Boot and Java version, see [Requirements](#requirements).
  Launchpad is a Spring Boot auto-configuration, no `@Import` or component scan is needed.
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
- Render the model attribute `launchpad` in your view. It has a list `apps`, each with `url`, `name`
  (in the request's language) and `icon`. The attribute is missing when the user may see no app.
  ```html
  <nav th:if="${launchpad}">
    <a th:each="app : ${launchpad.apps}" th:href="${app.url}">
      <img th:src="${app.icon}" alt=""> <span th:text="${app.name}"></span>
    </a>
  </nav>
  ```

App names are looked up by the request's locale, then by its language (`en-US` finds `en`), then
`name-default-locale` is used.

### Advanced

#### Restrict Apps to an Authority

An app with an `authority` is only shown to users having this authority. Users who are not logged in
only see apps without an `authority`.

```yaml
launchpad:
  apps[0]:
    authority: ROLE_ADMIN
```

#### Tenant-specific URLs

With tenancy enabled, `{tenantId}` in an app URL is replaced by the id of the current tenant.

```yaml
launchpad:
  tenant:
    enabled: true
    default-supplier:
      id: my-tenant   # defaults to "default"
  apps[0]:
    url: https://{tenantId}.example.org
```

To determine the tenant yourself, set `launchpad.tenant.default-supplier.enabled: false` and provide a
`TenantSupplier` bean. To build app URLs completely on your own, provide a `LaunchpadAppUrlCustomizer`
bean instead. Apps whose URL cannot be built are left out and logged as a warning.

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


## Upgrading from 3.x to 4.x

- Java 25 is required.
- Launchpad registers itself as Spring Boot auto-configuration. Remove any `@Import` or
  `@ComponentScan` of `LaunchpadAutoConfiguration` or `LaunchpadTenantConfiguration`.
- `AppDto` is a record: Java code calling `getUrl()`, `getName()` or `getIcon()` has to use `url()`,
  `name()` and `icon()`. Templates using `app.url` etc. keep working.


## License

Launchpad is Open Source software released under the [Apache License 2.0](LICENSE).
