package com.example.restapi.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.example.restapi.exception.GreetingNotFoundException;
import com.example.restapi.model.Greeting;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class GreetingController {
    private final GreetingModelAssembler assembler;
    private final AtomicLong counter = new AtomicLong();
    private final List<Greeting> greetings = new ArrayList<>();

    GreetingController(final GreetingModelAssembler assembler) {
        this.assembler = assembler;
    }

    @GetMapping("/greeting")
    public CollectionModel<EntityModel<Greeting>> greeting() {
        final List<EntityModel<Greeting>> list = greetings.stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());
        return CollectionModel.of(list, linkTo(methodOn(GreetingController.class).greeting()).withSelfRel());
    }

    @PostMapping("/greeting")
    public EntityModel<Greeting> newGreeting(@RequestBody final Greeting greeting) {
        final Greeting g = new Greeting(counter.incrementAndGet(), "New Greeting: " + greeting.getContent());
        greetings.add(g);
        return assembler.toModel(g);
    }

    @GetMapping("/greeting/{id}")
    public EntityModel<Greeting> oneGreeting(@PathVariable final Long id) {
        return assembler.toModel(findGreeting(id));
    }

    @PutMapping("/greeting")
    EntityModel<Greeting> updateGreeting(@RequestBody final Greeting greeting) {
        final Greeting g = findGreeting(greeting.getId());
        if (g != null) {
            g.setContent(greeting.getContent());
        }
        return assembler.toModel(g);
    }

    @DeleteMapping("/greeting/{id}")
    void deleteGreeting(@PathVariable final Long id) {
        greetings.removeIf(e -> e.getId() == id);
    }

    @GetMapping("/greeting-error")
    void someGreetingMethod() {
        try {
            final int x = 10 / 0;
        } catch (final Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Greeting Exception Message", e);
        }
    }

    @GetMapping("/greeting/filter/{q}")
    List<Greeting> someFilterGreetingMethod(
            @RequestParam(value = "content", defaultValue = "default") final String content) {
        return greetings.stream().filter(e -> e.getContent().startsWith(content)).collect(Collectors.toList());
    }

    private Greeting findGreeting(final Long id) {
        return greetings.stream().filter(e -> e.getId() == id).findFirst()
                .orElseThrow(() -> new GreetingNotFoundException(id));
    }

}