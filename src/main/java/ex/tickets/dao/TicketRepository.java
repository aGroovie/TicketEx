package ex.tickets.dao;

import ex.tickets.enums.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ex.tickets.entity.TicketRequest;

import java.util.List;

@Repository
public interface TicketRepository extends CrudRepository<TicketRequest,Long > {

    List<TicketRequest>  findTicketRequestByStatus(Status status);




}
