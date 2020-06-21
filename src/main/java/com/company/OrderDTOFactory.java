package com.company;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class OrderDTOFactory implements RepresentationModelAssembler<OrderDTO, EntityModel<OrderDTO>> {

    @Override
    public EntityModel<OrderDTO> toModel(OrderDTO orderDTO) {

        return new EntityModel<>(orderDTO,
                linkTo(methodOn(OrderController.class).formattedOrderDTO(orderDTO.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllOrdersDTO()).withRel("orders"));
    }
}