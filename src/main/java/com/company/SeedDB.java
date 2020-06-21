package com.company;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@Slf4j
public class SeedDB {
    @Bean
    CommandLineRunner initDatabase(CustomerRepository customerRepository, MovieRepository movieRepository, OrderRepository orderRepository) {

        return args -> {


            Movie movie1 = new Movie("Aladdin", 1992, "Ron Clements, John Musker", 8.0, "Animation, Adventure, Comedy, Family, Fantasy, Musical, Romance", "A kindhearted street urchin and a power-hungry Grand Vizier vie for a magic lamp that has the power to make their deepest wishes come true.", 55.5);
            Movie movie2 = new Movie("Inception", 2010, "Christopher Nolan", 8.8, "Action, Adventure, Sci-Fi, Thriller", "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.", 12.2);
            Movie movie3 = new Movie("The Lion King", 1994, "Roger Allers, Rob Minkoff", 8.5, "Animation, Adventure, Drama, Family, Musical", "A Lion cub crown prince is tricked by a treacherous uncle into thinking he caused his father's death and flees into exile in despair, only to learn in adulthood his identity and his responsibilities", 87.2);
            Order order4 = new Order("Disneyland Hits");
            Order order5 = new Order("Classics");

            Set<Order> orderList = new HashSet<>();
            Set<Movie> movieList = new HashSet<>();
            orderList.add(order4);
            orderList.add(order5);

            movieList.add(movie1);
            movieList.add(movie2);
            movieList.add(movie3);

            order4.setMovieList(movieList);

            Customer customer = customerRepository.save(new Customer("Maharan", "Lala", 23));

            order4.setCustomer(customer);
            order5.setCustomer(customer);

            // Movies Must Created Before Orders!!!
            movieRepository.saveAll(movieList);
            orderRepository.saveAll(orderList);

        };
    }
}