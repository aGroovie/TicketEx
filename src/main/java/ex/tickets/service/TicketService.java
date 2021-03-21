package ex.tickets.service;

import ex.tickets.entity.TicketRequest;
import ex.tickets.enums.Status;
import ex.tickets.exception.IncorrectIdException;
import ex.tickets.exception.NoTicketsToUpdateException;

import java.util.List;


public interface TicketService {

    Long saveTicket(TicketRequest ticket);

    TicketRequest getTicketById(Long id) throws IncorrectIdException;

    Iterable<TicketRequest> getAllTickets();

    void updateTicketStatus(Status status, TicketRequest ticket);

    List<TicketRequest> findTicketByStatus(Status status);

    Status getTicketStatusById(Long id) throws IncorrectIdException;

    Status getStatus(TicketRequest ticketRequest);


    TicketRequest getTicketInProcess() throws NoTicketsToUpdateException;


}
