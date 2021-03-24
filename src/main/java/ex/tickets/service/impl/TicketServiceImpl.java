package ex.tickets.service.impl;

import ex.tickets.dao.TicketRepository;
import ex.tickets.entity.TicketRequest;
import ex.tickets.enums.Status;
import ex.tickets.exception.IncorrectIdException;
import ex.tickets.exception.NoTicketsToUpdateException;
import ex.tickets.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Long saveTicket(TicketRequest ticket) {
        if (ticket.getStatus() == null) {
            ticket.setStatus(Status.IN_PROCESS);
        }
        ticketRepository.save(ticket);
        return ticket.getTicketId();
    }

    @Override
    public TicketRequest getTicketById(Long id) throws IncorrectIdException {
        Optional<TicketRequest> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            return ticket.get();
        } else {
            throw new IncorrectIdException("No ticket with such id:", id);
        }
    }

    @Override
    public Iterable<TicketRequest> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public void updateTicketStatus(Status status, TicketRequest ticket) {
        ticket.setStatus(status);

        ticketRepository.save(ticket);

    }

    @Override
    public List<TicketRequest> findTicketByStatus(Status status) {
        return ticketRepository.findTicketRequestByStatus(status);
    }

    @Override
    public Status getTicketStatusById(Long id) throws IncorrectIdException {
        Optional<TicketRequest> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            return ticket.get().getStatus();
        } else {
            throw new IncorrectIdException("No ticket with such id:", id);
        }

    }


    @Override
    public Status getStatus(TicketRequest ticket) {

        String ticketId = ticket.getTicketId().toString();

        RestTemplate restTemplate = new RestTemplate();

        final String url = "http://localhost:8080/update-ticket/" + ticketId;


        Status statusFromRest = restTemplate.postForObject(url, String.class, Status.class);

        return statusFromRest;
    }


    @Override
    public TicketRequest getTicketInProcess() throws NoTicketsToUpdateException {
        List<TicketRequest> tickets = ticketRepository.findTicketRequestByStatus(Status.IN_PROCESS);
        if (tickets.size() == 0) {
            throw new NoTicketsToUpdateException("There are no tickets to update, please add tickets with 'IN_PROCESS' status to database!");
        }
        return tickets.get(new Random().nextInt(tickets.size()));
    }


}
