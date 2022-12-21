package org.abalazsik.domaintest.data;

import java.time.LocalDateTime;

public class CandidateCreationTime {
    private LocalDateTime value;

    public CandidateCreationTime(LocalDateTime value) {
        if (value == null) {
            throw new IllegalArgumentException("Creation time cannot be null!");
        }
        this.value = value;
    }

    public LocalDateTime getValue() {
        return this.value;
    }
}
