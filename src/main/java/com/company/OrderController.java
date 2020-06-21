package com.company;

import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController {
    private final MovieRepository movieRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderEntityAdapter orderEntityAdapter;
    private final CustomerEntityAdapter customerEntityAdapter;
    private final OrderDTOFactory orderDTOFactory;

    public OrderController(CustomerEntityAdapter customerEntityAdapter,MovieRepository movieRepository, OrderRepository orderRepository, CustomerRepository customerRepository, OrderEntityAdapter orderEntityAdapter, OrderDTOFactory orderDTOFactory) {
        this.movieRepository = movieRepository;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderEntityAdapter = orderEntityAdapter;
        this.orderDTOFactory = orderDTOFactory;
        this.customerEntityAdapter = customerEntityAdapter;
    }

    // GET
    @RequestMapping(value = "/orders")
    ResponseEntity<CollectionModel<EntityModel<Order>>>
    getAllOrders() {
        return ResponseEntity.ok(orderEntityAdapter.toCollectionModel(orderRepository.findAll()));
    }

    @RequestMapping(value = "/orders", params = "price")
    ResponseEntity<CollectionModel<EntityModel<Order>>> getOrderByPrice(@RequestParam Double price) {
        List<Order> orders = orderRepository.findByPrice(price).orElseThrow(
                ()-> new OrderNotFoundException(price)
        );

        return ResponseEntity.ok(orderEntityAdapter.toCollectionModel(orders));
    }

    @GetMapping("/orders/{id}")
    ResponseEntity<EntityModel<Order>>
    getSingleOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return ResponseEntity.ok(orderEntityAdapter.toModel(order));
    }

    // DELETE
    @DeleteMapping("/orders/{id}")
    ResponseEntity<CollectionModel<EntityModel<Order>>>
    deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/orders/{orderid}/movies/{movieid}")
    ResponseEntity<CollectionModel<EntityModel<Order>>>
    deleteMovieFromOrder(@PathVariable Long orderid, @PathVariable Long movieid) {
        Order order = orderRepository.findById(orderid).orElseThrow(() -> new OrderNotFoundException(orderid));
        Movie movie = movieRepository.findById(movieid).orElseThrow(() -> new MovieNotFoundException(movieid));
        order.removeMovieFromList(movie);
        orderRepository.save(order);
        return ResponseEntity.noContent().build();
    }

    // POST newOrder
    @PostMapping("/orders")
    ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order order){
        Order savedOrder = orderRepository.save(order);
        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).getSingleOrder(savedOrder.getId())).toUri())
                .body(orderEntityAdapter.toModel(savedOrder));
    }


    @PostMapping("/orders/{orderid}/customers/{customerid}")
    ResponseEntity<EntityModel<Customer>> customerPurchaseOrder(@PathVariable Long orderid, @PathVariable Long customerid){
        Order order = orderRepository.findById(orderid).orElseThrow(() -> new OrderNotFoundException(orderid));
        Customer customer = customerRepository.findById(customerid).orElseThrow(() -> new CustomerNotFoundException(customerid));
        if(order.getCustomer() != null){
            throw new OrderNotFoundException(orderid , order.getCustomer().getFirstName() + "  " + order.getCustomer().getLastName() );
        }

        order.setCustomer(customer);
       orderRepository.save(order);
        Customer newCustomer = customerRepository.findById(customerid).orElseThrow(() -> new CustomerNotFoundException(customerid));
        return  ResponseEntity
                .created(linkTo(methodOn(OrderController.class).customerPurchaseOrder(orderid, customerid)).toUri())
                .body(customerEntityAdapter.toModel(newCustomer));
    }


    @PostMapping("/orders/{orderid}/movies/{movieid}")
    ResponseEntity<EntityModel<Order>> addExistingMovieToOrder(@PathVariable Long orderid, @PathVariable Long movieid){
        Order order = orderRepository.findById(orderid).orElseThrow(() -> new OrderNotFoundException(orderid));
        Movie movie = movieRepository.findById(movieid).orElseThrow(() -> new MovieNotFoundException(movieid));
        order.addMovieToList(movie);
        order = orderRepository.save(order);
        return  ResponseEntity
                .created(linkTo(methodOn(OrderController.class).addExistingMovieToOrder(orderid, movieid)).toUri())
                .body(orderEntityAdapter.toModel(order));
    }

    @PostMapping("/orders/{id}")
    ResponseEntity<EntityModel<Order>>
    createMovieAndAddToOrder(@PathVariable Long id, @RequestBody Movie movie) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        if (movie.getPrice() == null) {
            movie.setPrice(Math.random() * 100 + 20);
        }

        order.addMovieToList(movie);
        movieRepository.save(movie);
        order = orderRepository.save(order);

        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).createMovieAndAddToOrder(id, movie)).toUri())
                .body(orderEntityAdapter.toModel(order));
    }

    // PUT
    @PutMapping("/orders/{id}")
    ResponseEntity<EntityModel<Order>>
    updateOrder(@PathVariable Long id, @RequestBody Order order) {
        Order oldOrder = orderRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));

        if (order.getId() != null) {
            oldOrder.setId(order.getId());
        }

        if (order.getTitle() != null) {
            oldOrder.setTitle(order.getTitle());
        }

        if (order.getPurchaseDate() != null) {
            oldOrder.setPurchaseDate(order.getPurchaseDate());
        }

        if (order.getCustomer() != null) {
            oldOrder.setCustomer(order.getCustomer());
        }

        if (order.getPrice() != null) {
            oldOrder.setPrice(order.getPrice());
        }

        orderRepository.save(oldOrder);

        return ResponseEntity.created(linkTo(methodOn(OrderController.class).getSingleOrder(oldOrder.getId())).toUri())
                .body(orderEntityAdapter.toModel(oldOrder));
    }


    @RequestMapping(value = "/orders", params = {"fromPrice", "toPrice"})
    ResponseEntity<CollectionModel<EntityModel<Order>>> getOrdersPriceRange(@RequestParam int fromPrice, @RequestParam int toPrice) {
        ArrayList<Order> orders = (ArrayList<Order>) orderRepository.findAll();
        ArrayList<Order> filteredOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getPrice() >= fromPrice &&
                    order.getPrice() <= toPrice) {
                filteredOrders.add(order);
            }
        }
        return ResponseEntity.ok(orderEntityAdapter.toCollectionModel(filteredOrders));
    }

    @GetMapping("/orders/{id}/singleDTO")
    public ResponseEntity<EntityModel<OrderDTO>>
    formattedOrderDTO(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        OrderDTO orderDTO = new OrderDTO(order);
        return ResponseEntity.ok(orderDTOFactory.toModel(orderDTO));
    }

    @GetMapping("/orders/allDTO")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<CollectionModel<EntityModel<OrderDTO>>> getAllOrdersDTO() {
        return ResponseEntity.ok(orderDTOFactory.toCollectionModel(convertOrdersToDTO(orderRepository.findAll())));
    }

    ArrayList<OrderDTO> convertOrdersToDTO(List<Order> orders) {
        ArrayList<OrderDTO> orderDTOS = new ArrayList<>();
        for (Order order : orders) {
            orderDTOS.add(new OrderDTO(order));
        }
        return orderDTOS;
    }
    /////////// END OF DTO FUNCS
}