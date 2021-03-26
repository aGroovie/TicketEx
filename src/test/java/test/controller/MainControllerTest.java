package test.controller;

import ex.tickets.TicketsApplication;
import ex.tickets.controller.MainController;
import ex.tickets.entity.TicketRequest;
import ex.tickets.enums.Status;
import ex.tickets.exception.IncorrectIdException;
import ex.tickets.service.TicketService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ex.tickets.enums.Status.FINISHED;
import static ex.tickets.enums.Status.IN_PROCESS;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TicketsApplication.class})
@WebAppConfiguration
public class MainControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private MainController mainController;

    private MockMvc mockMvc;


    TicketRequest ticketWithStatusInProcess;

    TicketRequest ticketWithStatusError;

    TicketRequest ticketWithStatusFinished;

    List<TicketRequest> tickets;

    @Before
    public void setup() {
        String date = "2020-06-02 15:45";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime departureDate = LocalDateTime.parse(date, formatter);

        ticketWithStatusInProcess = new TicketRequest(25, departureDate, IN_PROCESS);
        ticketWithStatusInProcess.setTicketId(8L);

        ticketWithStatusError = new TicketRequest(250, departureDate, Status.ERROR);

        ticketWithStatusFinished = new TicketRequest(56, departureDate, FINISHED);

        tickets = List.of(ticketWithStatusInProcess, ticketWithStatusError, ticketWithStatusFinished);

        MockitoAnnotations.openMocks(this);


        mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();

    }

    @Test
    public void getAllTickets_shouldReturnCorrect() {
        when(ticketService.getAllTickets()).thenReturn(tickets);
        List<TicketRequest> allTickets = mainController.getAllTickets();

        Assertions.assertEquals(tickets, allTickets);
        verify(ticketService, times(1)).getAllTickets();
        verifyNoMoreInteractions(ticketService);

    }


    @Test
    public void getTicketStatusById_ReturnsCorrect() throws Exception {

        Long id = 67L;
        when(ticketService.getTicketStatusById(id)).thenReturn(FINISHED);

        mockMvc.perform(get("/ticket/" + id.toString())).andExpect(status().isOk()).
                andExpect(content().json("\"FINISHED\""));

        verify(ticketService, times(1)).getTicketStatusById(id);
    }


    @Test
    public void registerTicket_SavesCorrect() throws Exception {
        when(ticketService.saveTicket(ticketWithStatusError)).thenReturn(100L);

        String json = "{\n" +
                "    \"routeNumber\": 250,\n" +
                "    \"departureDate\": \"2020-06-02 15:45\",\n" +
                "    \"status\": \"ERROR\"\n" +
                "}";

        mockMvc.perform(post("/ticket").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andReturn();
        verify(ticketService, times(1)).saveTicket(ticketWithStatusError);
    }


    @Test
    public void handleIdException_ReceivesException() throws Exception {

        when(ticketService.getTicketStatusById(0L)).thenThrow(IncorrectIdException.class);

        mockMvc.perform(get("/ticket/0")).andExpect(status().isNotFound());
    }

    @Test
    public void handleArgumentException_ReceivesException() throws Exception {

        when(ticketService.saveTicket(ticketWithStatusInProcess)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(post("/ticket")).andExpect(status().isBadRequest());
    }
}
