package test.tickets.serviceImpl;

import ex.tickets.TicketsApplication;
import ex.tickets.dao.TicketRepository;
import ex.tickets.entity.TicketRequest;
import ex.tickets.enums.Status;
import ex.tickets.exception.IncorrectIdException;
import ex.tickets.exception.NoTicketsToUpdateException;
import ex.tickets.service.impl.TicketServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TicketsApplication.class})
public class TicketServiceImplTest {


    @InjectMocks
    TicketServiceImpl ticketService;

    @Mock
    TicketRepository ticketRepository;

    @Mock
    RestTemplate restTemplate;

    TicketRequest ticketStatusInProcess;

    TicketRequest ticketStatusError;

    TicketRequest ticketStatusFinished;

    List<TicketRequest> tickets;


    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        String date = "2020-06-02 15:45";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime departureDate = LocalDateTime.parse(date, formatter);

        ticketStatusInProcess = new TicketRequest(25, departureDate, Status.IN_PROCESS);
        ticketStatusInProcess.setTicketId(8L);

        ticketStatusError = new TicketRequest(30, departureDate, Status.ERROR);
        ticketStatusFinished = new TicketRequest(56, departureDate, Status.FINISHED);

        tickets = List.of(ticketStatusInProcess, ticketStatusError, ticketStatusFinished);

    }

    @Test
    void getTicketById_ReturnsCorrect() throws IncorrectIdException {
        when(ticketRepository.findById(8L)).thenReturn(Optional.ofNullable(ticketStatusInProcess));

        Assertions.assertEquals(ticketService.getTicketById(8L), ticketStatusInProcess);
    }


    @Test
    void getTicketById_ThrowsIncorrectIdException() {

        when(ticketRepository.findById(0L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IncorrectIdException.class, () -> ticketService.getTicketById(0L));

        String expectedMessage = "No ticket with such id:" + 0L;
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);

    }


    @Test
    void getAllTickets_ReturnsCorrect() {
        when(ticketRepository.findAll()).thenReturn(tickets);

        List<TicketRequest> tickets = ticketService.getAllTickets();

        Assertions.assertEquals(3, tickets.size());

        verify(ticketRepository, times(1)).findAll();

    }

    @Test
    void updateTicketStatus_UpdatesStatusCorrect() {
        when(ticketRepository.save(ticketStatusInProcess)).thenReturn(ticketStatusInProcess);

        ticketService.updateTicketStatus(Status.FINISHED, ticketStatusInProcess);

        Assertions.assertEquals(Status.FINISHED, ticketStatusInProcess.getStatus());

        verify(ticketRepository, times(1)).save(ticketStatusInProcess);

    }


    @Test
    void findTicketInProcess_ReturnsCorrect() throws NoTicketsToUpdateException {
        when(ticketRepository.findFirstByStatus(Status.IN_PROCESS)).thenReturn(Optional.ofNullable(ticketStatusInProcess));

        TicketRequest ticketRequestStatusInProgress = ticketService.getTicketInProcess();

        Assertions.assertEquals(Status.IN_PROCESS, ticketRequestStatusInProgress.getStatus());

        verify(ticketRepository, times(1)).findFirstByStatus(Status.IN_PROCESS);
    }


    @Test
    void findTicketInProcess_Throws_NoTicketsToUpdateException() {
        when(ticketRepository.findFirstByStatus(Status.IN_PROCESS)).thenReturn(Optional.empty());

        assertThrows(NoTicketsToUpdateException.class, () -> Optional
                .ofNullable(ticketService.getTicketInProcess())
                .orElseThrow(() ->
                        new NoTicketsToUpdateException("There are no tickets to update, please add tickets with 'IN_PROCESS' status to database!")));

        verify(ticketRepository, times(1)).findFirstByStatus(Status.IN_PROCESS);

    }


    @Test
    void getTicketStatusById_ReturnsCorrect() throws IncorrectIdException {
        when(ticketRepository.findById(8L)).thenReturn(Optional.ofNullable(ticketStatusInProcess));

        Status statusFromDb = ticketService.getTicketStatusById(8L);
        Status expectedStatus = Status.IN_PROCESS;

        Assertions.assertEquals(expectedStatus, statusFromDb);
        verify(ticketRepository, times(1)).findById(8L);

    }

    @Test
    void getTicketStatusById_Throws_IncorrectIdException() {

        Long id = 0L;
        assertThrows(IncorrectIdException.class, () -> Optional
                .ofNullable(ticketService.getTicketStatusById(id))
                .orElseThrow(() ->
                        new IncorrectIdException("No ticket with such id:", id)));

        verify(ticketRepository, times(1)).findById(id);
    }


    @Test
    void getStatus_ReturnsCorrect() {

        ticketStatusError.setTicketId(60L);
        String ticketId = ticketStatusError.getTicketId().toString();
        final String url = "http://localhost:8080/update-ticket/" + ticketId;


        when(restTemplate.postForObject(url, String.class, Status.class)).thenReturn(Status.ERROR);

        Status statusFromRest = ticketService.getStatus(ticketStatusError);

        Assertions.assertEquals(Status.ERROR, statusFromRest);

        verify(restTemplate, times(1)).postForObject(url, String.class, Status.class);


    }
}
