package ex.tickets.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ex.tickets.enums.Status;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "tickets")
public class TicketRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    @JsonIgnore
    private Long TicketId;

    @Column(name = "route_number")
    @Min(value = 1,message = "Route Number must not be empty!")
    private int routeNumber;

    @Column(name = "departure_date")
    @NotNull(message = "Date must not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Status status;

    public TicketRequest(int routeNumber, LocalDateTime departureDate, Status status) {
        this.routeNumber = routeNumber;
        this.departureDate = departureDate;
        this.status = status;
    }

    public TicketRequest() {
    }


    public Long getTicketId() {
        return TicketId;
    }

    public void setTicketId(Long ticketId) {
        TicketId = ticketId;
    }

    public int getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(int routeNumber) {
        this.routeNumber = routeNumber;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketRequest)) return false;
        TicketRequest that = (TicketRequest) o;
        return routeNumber == that.routeNumber && Objects.equals(TicketId, that.TicketId) && Objects.equals(departureDate, that.departureDate) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(TicketId, routeNumber, departureDate, status);
    }

    @Override
    public String toString() {
        return "TicketRequest{" +
                "TicketId=" + TicketId +
                ", routeNumber=" + routeNumber +
                ", departureDate=" + departureDate +
                ", status=" + status +
                '}';
    }
}
