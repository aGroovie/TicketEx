package test.tickets.serviceImpl;

import ex.tickets.TicketsApplication;
import ex.tickets.entity.TicketRequest;
import ex.tickets.enums.Status;
import ex.tickets.exception.NoTicketsToUpdateException;
import ex.tickets.service.TicketService;
import ex.tickets.service.impl.TicketRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TicketsApplication.class})
public class TicketRequestServiceImplTest {


    @Mock
    private TicketService ticketService;

    @Mock
    private TicketRequest ticketStatusInProcess;


    @InjectMocks
    private TicketRequestServiceImpl ticketRequestService;


    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        String date = "2020-06-02 15:45";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime departureDate = LocalDateTime.parse(date, formatter);

        ticketStatusInProcess = new TicketRequest(25, departureDate, Status.IN_PROCESS);
        ticketStatusInProcess.setTicketId(9L);

    }

    @Test
    void ticketRequestProcess_GetsTicket() throws NoTicketsToUpdateException {
        when(ticketService.getTicketInProcess()).thenReturn(ticketStatusInProcess);
        when(ticketService.getStatus(ticketStatusInProcess)).thenReturn(Status.FINISHED);
        when(ticketService.getStatus(ticketStatusInProcess)).thenReturn(Status.ERROR);

        ticketRequestService.ticketRequestProcess();

        verify(ticketService, times(1)).getTicketInProcess();
        verify(ticketService, times(1)).getStatus(ticketStatusInProcess);
        verify(ticketService, times(1)).updateTicketStatus(Status.ERROR, ticketStatusInProcess);

    }


    @Test
    void ticketRequestProcess_ThrowsNoTicketsToUpdateException() throws NoTicketsToUpdateException {
        when(ticketService.getTicketInProcess()).thenThrow(NoTicketsToUpdateException.class);

        ticketRequestService.ticketRequestProcess();

        assertThrows(NoTicketsToUpdateException.class, () -> ticketService.getTicketInProcess());
    }
}
