package com.example.restapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.example.restapi.exception.GreetingNotFoundException;
import com.example.restapi.model.Greeting;

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

    private final AtomicLong counter = new AtomicLong();
    private final List<Greeting> greetings = new ArrayList<>();

    @GetMapping("/greeting")
    public List<Greeting> greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return greetings;
    }

    @PostMapping("/greeting")
    public Greeting newGreeting(@RequestBody Greeting greeting) {
        Greeting g = new Greeting(counter.incrementAndGet(), "New Greeting: " + greeting.getContent());
        greetings.add(g);
        return g;
    }

    @GetMapping("/greeting/{id}")
    public Greeting oneGreeting(@PathVariable Long id) {
        Greeting g = findGreeting(id);
        return g;
    }

    @PutMapping("/greeting")
    Greeting updateGreeting(@RequestBody Greeting greeting) {
        Greeting g = findGreeting(greeting.getId());
        if (g != null) {
            g.setContent(greeting.getContent());
        }
        return g;
    }

    @DeleteMapping("/greeting/{id}")
    void deleteGreeting(@PathVariable Long id) {
        greetings.removeIf(e -> e.getId() == id);
    }

    @GetMapping("/greeting-error")
    void someGreetingMethod() {
        try {
            int x = 10 / 0;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Greeting Exception Message", e);
        }
    }

    @GetMapping("/greeting/filter/{q}")
    List<Greeting> someFilterGreetingMethod(@RequestParam(value = "content", defaultValue = "") String content) {
        return greetings.stream().filter(e -> e.getContent().startsWith(content)).collect(Collectors.toList());
    }

    private Greeting findGreeting(Long id) {
        return greetings.stream().filter(e -> e.getId() == id).findFirst()
                .orElseThrow(() -> new GreetingNotFoundException(id));
    }

}