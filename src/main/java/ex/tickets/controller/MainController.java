package ex.tickets.controller;

import ex.tickets.entity.TicketRequest;
import ex.tickets.enums.Status;
import ex.tickets.exception.IncorrectIdException;
import ex.tickets.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.format.DateTimeParseException;

@Validated
@RestController
public class MainController {

    @Autowired
    TicketService ticketService;


    @RequestMapping(value = "/tickets", //
            method = RequestMethod.GET, //
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Iterable<TicketRequest> getTickets() {

        return ticketService.getAllTickets();
    }


    @RequestMapping(value = "/ticket/{ticketId}", //
            method = RequestMethod.GET, //
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Status getTicketStatusById(@PathVariable("ticketId") String ticketId) throws IncorrectIdException {

        return ticketService.getTicketStatusById(Long.parseLong(ticketId));
    }


    @RequestMapping(value = "/ticket", //
            method = RequestMethod.POST, //
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Long registerTicket(@Valid @RequestBody TicketRequest ticket) {

        Long id = ticketService.saveTicket(ticket);
        return id;
    }


    @ExceptionHandler(IncorrectIdException.class)
    @ResponseBody
    public IncorrectIdException handleIdException(IncorrectIdException ex, HttpServletResponse response) throws IOException {

        response.sendError(HttpServletResponse.SC_NOT_FOUND);

        return ex;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, DateTimeParseException.class})
    public MethodArgumentNotValidException handleArgumentException(MethodArgumentNotValidException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return ex;
    }

}
