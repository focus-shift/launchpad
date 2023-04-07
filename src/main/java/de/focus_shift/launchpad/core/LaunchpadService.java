package de.focus_shift.launchpad.core;

import org.springframework.security.core.Authentication;

interface LaunchpadService {

  Launchpad getLaunchpad(Authentication authentication);
}
