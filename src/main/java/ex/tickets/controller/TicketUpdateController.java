package ex.tickets.controller;

import ex.tickets.enums.Status;
import ex.tickets.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class TicketUpdateController {


    @Autowired
    private PaymentService paymentService;


    @RequestMapping(value = "/update-ticket/{ticketId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Status getNewStatus(@PathVariable String ticketId) {
        return paymentService.getRandomStatus();
    }

}
