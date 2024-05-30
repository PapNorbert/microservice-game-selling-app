package edu.ubb.consolegamesales.backend.controller.rest;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.OrderCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.OrderListDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.OrderListWithPaginationDto;
import edu.ubb.consolegamesales.backend.controller.exception.NotFoundException;
import edu.ubb.consolegamesales.backend.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
    public OrderListWithPaginationDto findPaginated(
            @RequestParam @Positive Long buyerId,
            @RequestParam(defaultValue = "1", required = false) @Positive int page,
            @RequestParam(defaultValue = "5", required = false) @Positive int limit) {
        LOGGER.info("GET paginated orders of user with id {} at orders api, "
                + "page: {}, limit: {}", buyerId, page, limit);
        return orderService.findAllOrdersOfUser(buyerId, page, limit);
    }

    @GetMapping("/{id}")
    public OrderListDto findById(@PathVariable("id") Long id, Authentication authentication) throws NotFoundException {
        LOGGER.info("GET order at orders/{} api", id);
        return orderService.findOrderById(id, authentication);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedObjectDto create(@RequestBody @Valid OrderCreationDto orderCreationDto,
                                   Authentication authentication) {
        LOGGER.info("POST request at orders api");
        return orderService.createOrder(orderCreationDto, authentication);
    }
}
