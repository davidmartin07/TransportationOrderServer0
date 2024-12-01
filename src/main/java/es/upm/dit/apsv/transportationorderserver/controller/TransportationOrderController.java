package es.upm.dit.apsv.transportationorderserver.controller;



import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.upm.dit.apsv.transportationorderserver.model.TransportationOrder;
import es.upm.dit.apsv.transportationorderserver.repository.TransportationOrderRepository;

@RestController
public class TransportationOrderController {

    private final TransportationOrderRepository repository;
    public static final Logger log = LoggerFactory.getLogger(TransportationOrderController.class);

    public TransportationOrderController(TransportationOrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/transportationorders")
    public List<TransportationOrder> all() {
        return (List<TransportationOrder>) repository.findAll();
    }

    @PostMapping("/transportationorders")
    public TransportationOrder newOrder(@RequestBody TransportationOrder newOrder) {
        return repository.save(newOrder);
    }

    @GetMapping("/transportationorders/{truck}")
    public ResponseEntity<TransportationOrder> getByTruck(@PathVariable String truck) {
        Optional<TransportationOrder> ot = repository.findById(truck);
        return ot.map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                 .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/transportationorders")
    public ResponseEntity<TransportationOrder> update(@RequestBody TransportationOrder updatedOrder) {
        TransportationOrder savedOrder = repository.save(updatedOrder);
        if (savedOrder == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(savedOrder, HttpStatus.OK);
    }

    @DeleteMapping("/transportationorders/{truck}")
    public void deleteOrder(@PathVariable String truck) {
        repository.deleteById(truck);
    }
}
