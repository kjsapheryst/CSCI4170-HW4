package edu.ecu.cs.seng6285.restfulbots.api;

import com.google.cloud.datastore.Key;
import edu.ecu.cs.seng6285.restfulbots.datastore.TextbookService;
import edu.ecu.cs.seng6285.restfulbots.models.Textbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.*;
import java.io.*;

@RestController
@RequestMapping(value = "/textbooks")
public class TextbookEndpoint {
    @Autowired
    private TextbookService textbookService;

    @GetMapping
    public List<Textbook> getAllTextbooks(@RequestParam("subject") Optional<String> subject) {
        if (subject.isPresent()) {
            return textbookService.getAllTextbooksForSubject(subject.get());
        }
        return textbookService.getAllTextbooks();
    }

    @GetMapping(value = "{textbookId}")
    public Textbook getTextbook(@PathVariable long textbookId) {
        // TODO: Add code to get a specific textbook here
    	//code to find search textbook based on ID
    	ArrayList<Textbook> a = new ArrayList<Textbook>();
    	for(int i = 0; i < a.size(); i++) {
    		Textbook temp = a.get(i);
    		if(textbookId.equals(temp.getId())) {
    			System.out.println("Textbook found: " + a);
    		}
    		
    		else { //if textbook is not found, prints error 
        		System.out.println("Error: Textbook not found.");
        		System.exit(1);
        	}
    	}
    	
    }

    @DeleteMapping(value = "{textbookId}")
    public void deleteTextbook(@PathVariable long textbookId) {
        // TODO: Add code to delete a specific textbook here
    	textbookService.deleteTextbook(textbookId);
    }

    @PostMapping
    public Textbook createTextbook(@RequestBody Textbook textbook) {
        // TODO: Add code to add a new textbook here
    	Key mainKey = textbookService.createTextbook(textbook);
    	textbook.setId(mainKey.getId());
        // TODO: Remove this once you can return a real object
    	return textbook;
    }

    @PatchMapping(value = "{textbookId}")
    public Textbook updateTextbook(@RequestBody Textbook textbook, @PathVariable long textbookId) {
    	 Optional<Textbook> existingTextbook = textbookService.getTextbookById(textbookId);
         if (existingTextbook.isPresent()) {
             Textbook updatedTextbook = textbookService.updateTextbook(textbookId);
             return updatedTextbook;
         } 
         
         else {
             throw new ResourceNotFoundException("The following textbook does not exist: " + textbookId);
         }
    }

    @GetMapping(value = "/init")
    public boolean initTextbooks() {
        // Create some sample textbooks
        List<Textbook> textbooks = new ArrayList<>();

        textbooks.add(new Textbook.Builder()
                .withTitle("Operating System Concepts")
                .withAuthors("Abraham Silberschatz, Greg Gagne, Peter B. Galvin")
                .withSubject("Computer Science")
                .withPublisher("Wiley")
                .withYear(2018)
                .build());

        textbooks.add(new Textbook.Builder()
                .withTitle("Programming Language Pragmatics")
                .withAuthors("Michael Scott")
                .withSubject("Computer Science")
                .withPublisher("Elsevier")
                .withYear(2015)
                .build());

        textbooks.add(new Textbook.Builder()
                .withTitle("Concepts of Programming Languages")
                .withAuthors("Robert W. Sebesta")
                .withSubject("Computer Science")
                .withPublisher("Pearson")
                .withYear(2016)
                .build());

        textbooks.add(new Textbook.Builder()
                .withTitle("Programming Language Pragmatics")
                .withAuthors("Michael Scott")
                .withSubject("Computer Science")
                .withPublisher("Elsevier")
                .withYear(2015)
                .build());

        textbooks.add(new Textbook.Builder()
                .withTitle("An Introduction to Statistical Learning: with Applications in R")
                .withAuthors("Gareth James, Daniela Witten, Trevor Hastie, and Robert Tibshirani")
                .withSubject("Mathematics")
                .withPublisher("Springer")
                .withYear(2017)
                .build());

        for (Textbook t : textbooks) {
            Key key = textbookService.createTextbook(t);
            t.setId(key.getId());
        }

        return true;
    }
}
