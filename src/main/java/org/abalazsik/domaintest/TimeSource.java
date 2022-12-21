package org.abalazsik.domaintest;

import java.time.LocalDateTime;

public interface TimeSource {

    LocalDateTime now();
}
