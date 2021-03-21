package ex.tickets.service.impl;

import ex.tickets.entity.TicketRequest;
import ex.tickets.enums.Status;
import ex.tickets.exception.NoTicketsToUpdateException;
import ex.tickets.service.TicketRequestService;
import ex.tickets.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TicketRequestServiceImpl implements TicketRequestService {

    @Autowired
    TicketService ticketService;

    @Override
    @Scheduled(fixedRate = 10000)
    public void ticketRequestProcess() {

        TicketRequest ticket;
        try {
            ticket = ticketService.getTicketInProcess();

            Status status = ticketService.getStatus(ticket);

            ticketService.updateTicketStatus(status, ticket);
        } catch (NoTicketsToUpdateException e) {
            System.out.println(e.getMessage());
        }


    }

}
