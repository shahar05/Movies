package com.company;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerEntityAdapter customerEntityAdapter;
    private final OrderRepository orderRepository;
    private final MovieRepository movieRepository;
    private final MovieEntityAdapter movieEntityAdapter;

    public CustomerController(CustomerRepository customerRepository, CustomerEntityAdapter customerEntityAdapter, OrderRepository orderRepository, MovieRepository movieRepository, MovieEntityAdapter movieEntityAdapter) {
        this.customerRepository = customerRepository;
        this.customerEntityAdapter = customerEntityAdapter;
        this.orderRepository = orderRepository;
        this.movieRepository = movieRepository;
        this.movieEntityAdapter = movieEntityAdapter;
    }


    @RequestMapping(value = "/customers")
    ResponseEntity<CollectionModel<EntityModel<Customer>>> getAllCustomers() {
        return ResponseEntity.ok(customerEntityAdapter.toCollectionModel(customerRepository.findAll()));
    }


    @GetMapping("/customers/{id}")
    ResponseEntity<EntityModel<Customer>> getSingleCustomer(@PathVariable Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        return ResponseEntity.ok(customerEntityAdapter.toModel(customer));
    }

    @RequestMapping(value = "/customers", params = "age")
    ResponseEntity<CollectionModel<EntityModel<Customer>>> getCustomersByAge(@RequestParam int age) {
        List<Customer> customers = customerRepository.findByAge(age).orElseThrow(
                ()-> new CustomerNotFoundException(age)
        );
        return ResponseEntity.ok(customerEntityAdapter.toCollectionModel(customers));
    }

    @DeleteMapping("/customers/{id}")
    ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/customers/{id}")
    public ResponseEntity<EntityModel<Customer>> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {

        Customer oldCustomer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));

        if (customer.getAge() != null) {
            oldCustomer.setAge(customer.getAge());
        }
        if (customer.getFirstName() != null) {
            oldCustomer.setFirstName(customer.getFirstName());
        }
        if (customer.getLastName() != null) {
            oldCustomer.setLastName(customer.getLastName());
        }

        customerRepository.save(oldCustomer);

        return ResponseEntity.created(linkTo(methodOn(CustomerController.class).getSingleCustomer(oldCustomer.getId())).toUri())
                .body(customerEntityAdapter.toModel(oldCustomer));
    }

    @PostMapping("/customers")
    public ResponseEntity<EntityModel<Customer>> addCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).getSingleOrder(savedCustomer.getId())).toUri())
                .body(customerEntityAdapter.toModel(savedCustomer));
    }

    @RequestMapping(value = "/customers", params = {"fromAge", "toAge"})
    ResponseEntity<CollectionModel<EntityModel<Customer>>> getCustomersAgeRange(@RequestParam int fromAge, @RequestParam int toAge) {
        List<Customer> customers = customerRepository.findAll().stream()
                .filter(p -> p.getAge() >= fromAge && p.getAge() <= toAge)
                .sorted()
                .collect(Collectors.toList());

        return ResponseEntity.ok(customerEntityAdapter.toCollectionModel(customers));
    }

    @PostMapping(value = "/customers/{id}", params = {"title"})
    ResponseEntity<EntityModel<Customer>>
    addOrderToCustomer(@PathVariable Long id, @RequestParam String title)
    {
        Order order = new Order(title);
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        order.setCustomer(customer);
        orderRepository.save(order);
        customerRepository.save(customer);

        return ResponseEntity
                .created(linkTo(methodOn(CustomerController.class).addOrderToCustomer(id, title)).toUri())
                .body(customerEntityAdapter.toModel(customer));
    }

    @GetMapping("/customers/{id}/recommend")
    ResponseEntity<EntityModel<Movie>> recommendMovie(@PathVariable Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        int sum = 0, avg = 0, count = 0, delta = 0;

        for (Order order : customer.getOrderList())
        {
            for (Movie movie : order.getMovieList())
            {
                System.out.println("["+count+"]"+" "+movie.getYear());
                sum += movie.getYear();
                count++;
            }
        }

        avg = sum / count;

        System.out.println("Recommending movies from around " +avg);

        boolean foundMovie = false;
        while (!foundMovie && delta < 10)
        {
            System.out.println("Looking for movies between" + (avg - delta) + " and" + (avg + delta));
            for (Movie movie : movieRepository.findAll())
            {
                if (movie.getYear() <= avg + delta &&
                        movie.getYear() >= avg - delta)
                    return ResponseEntity.ok(movieEntityAdapter.toModel(movie));
            }
            delta++;
        }

        throw new MovieNotFoundException(avg);
    }
}



//        List<Customer>  = customerRepository.findAll().stream()
//                .filter(p -> p.getAge() == age)
//                .sorted()
//                .collect(Collectors.toList());
