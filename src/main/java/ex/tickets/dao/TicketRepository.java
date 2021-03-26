package ex.tickets.dao;

import ex.tickets.enums.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ex.tickets.entity.TicketRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends CrudRepository<TicketRequest, Long> {


    Optional<TicketRequest> findFirstByStatus(Status status);

    List<TicketRequest> findAll();


}
