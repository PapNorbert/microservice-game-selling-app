package edu.ubb.consolegamesales.backend.controller.rest;

import edu.ubb.consolegamesales.backend.dto.incoming.OrderCreationDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.OrderListDto;
import edu.ubb.consolegamesales.backend.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("${apiPrefix}/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void findPaginated(
            @RequestParam @Positive Long buyerId,
            @RequestParam(defaultValue = "1", required = false) @Positive int page,
            @RequestParam(defaultValue = "5", required = false) @Positive int limit) {
        LOGGER.info("GET paginated orders of user with id {} at orders api, "
                + "page: {}, limit: {}", buyerId, page, limit);
        orderService.requestOrdersOfBuyerPaginated(buyerId, page, limit);
        // response status is ACCEPTED, data will be sent later through websocket
    }

    @GetMapping("/{id}")
    public OrderListDto findById(@PathVariable("id") Long id, Authentication authentication) {
        LOGGER.info("GET order at orders/{} api", id);
        // returns the orders if necessary data is available
        // otherwise service requests the data, Accepted status code will be returned
        // and data is sent through websocket
        return orderService.loadOrderByID(id, authentication);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void create(@RequestBody @Valid OrderCreationDto orderCreationDto,
                                   Authentication authentication) {
        LOGGER.info("POST request at orders api");
        // creation is handled as transaction, result will be sent on websocket
        orderService.createOrder(orderCreationDto, authentication);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") @PositiveOrZero Long id,
                       Authentication authentication) {
        LOGGER.info("DELETE request at orders/{} api", id);
        // deletion is handled as transaction, result will be sent on websocket
        orderService.deleteOrderById(id, authentication);
    }

}
