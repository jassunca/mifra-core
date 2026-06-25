package org.mifra.core.api.models.domain;

/**
 * Wrapper class used for fetching and comparison of saga step outcomes.
 */
public final class SagaStepOutcome {
    private final String value;

    private SagaStepOutcome(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Saga outcome status value cannot be empty");
        }
        this.value = value.toUpperCase().trim();
    }

    public static SagaStepOutcome of(String value) {
        return new SagaStepOutcome(value);
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SagaStepOutcome that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() { return value.hashCode(); }

    @Override
    public String toString() { return value; }
}