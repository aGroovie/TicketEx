package ex.tickets.service.impl;

import ex.tickets.enums.Status;
import ex.tickets.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class PaymentServiceImpl implements PaymentService {


    @Override
    public Status getRandomStatus() {
        List<Status> statuses = Arrays.asList(Status.IN_PROCESS, Status.ERROR, Status.FINISHED);
        return statuses.get(new Random().nextInt(statuses.size()));
    }
}
