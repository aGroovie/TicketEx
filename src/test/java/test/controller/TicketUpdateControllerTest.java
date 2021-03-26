package test.controller;

import ex.tickets.TicketsApplication;
import ex.tickets.controller.TicketUpdateController;
import ex.tickets.enums.Status;
import ex.tickets.service.impl.PaymentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TicketsApplication.class})
@WebAppConfiguration
public class TicketUpdateControllerTest {

    @Mock
    private PaymentServiceImpl paymentService;


    @InjectMocks
    private TicketUpdateController ticketUpdateController;


    private MockMvc mockMvc;


    @Before
    public void setup() {

        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(ticketUpdateController).build();
    }

    @Test
    public void getNewStatus_ReturnsCorrect() throws Exception {


        when(paymentService.getRandomStatus()).thenReturn(Status.ERROR);

        mockMvc.perform(post("/update-ticket/" + "20")).andExpect(status().isOk());

        verify(paymentService, times(1)).getRandomStatus();
        Assertions.assertNotNull(ticketUpdateController.getNewStatus("20"));
    }

}
