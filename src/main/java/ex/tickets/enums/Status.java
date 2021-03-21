package ex.tickets.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    FINISHED("FINISHED"),
    ERROR("ERROR"),
    IN_PROCESS("IN_PROCESS");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }

}
