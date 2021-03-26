package test.tickets.serviceImpl;

import ex.tickets.TicketsApplication;
import ex.tickets.enums.Status;
import ex.tickets.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TicketsApplication.class})
public class PaymentServiceImplTest {


    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {


        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRandomStatus_ReturnsCorrect() {

        Status status = paymentService.getRandomStatus();

        Assertions.assertNotNull(status);
    }


}
