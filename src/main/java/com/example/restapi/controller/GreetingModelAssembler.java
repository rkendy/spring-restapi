package com.example.restapi.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.example.restapi.model.Greeting;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class GreetingModelAssembler implements RepresentationModelAssembler<Greeting, EntityModel<Greeting>> {

    @Override
    public EntityModel<Greeting> toModel(Greeting greeting) {
        return EntityModel.of(greeting,
                linkTo(methodOn(GreetingController.class).oneGreeting(greeting.getId())).withSelfRel());
        // linkTo(methodOn(GreetingController.class).greeting()).withRel("greetings"));
    }

}