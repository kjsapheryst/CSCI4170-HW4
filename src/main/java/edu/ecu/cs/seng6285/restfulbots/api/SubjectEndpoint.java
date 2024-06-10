package edu.ecu.cs.seng6285.restfulbots.api;

import com.google.cloud.datastore.Key;
import edu.ecu.cs.seng6285.restfulbots.datastore.SubjectService;
import edu.ecu.cs.seng6285.restfulbots.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/subjects")
public class SubjectEndpoint {
    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping(value = "{subjectId}")
    public Subject getSubject(@PathVariable long subjectId) throws Exception {
    	//creates object to look to see if there are other values that match subjectId,
    	Subject subject = subjectService.getSubject(subjectId);
        if(subject != null) { //if found, return subjectId
        	return subject;
        }
        
        else { //if not found, throw exception error message
        	throw new Exception("Error: SubjectId not found");
        }
    }
    

    @DeleteMapping(value = "{subjectId}")
    public void deleteSubject(@PathVariable long subjectId) {
        // TODO: Add code to delete a specific subject here
    	subjectService.deleteSubject(subjectId); //allows user to delete a subject
    }

    @PostMapping
    public Subject addSubject(@RequestBody Subject subject) {
        // TODO: Add code to add a new subject here
    	Key key = subjectService.createSubject(subject); //allows user to add a subject
        subject.setId(key.getId());
        return subject;

        // TODO: Remove this once you can return a real object
        //return null; **removed**
    }

    @PatchMapping(value = "{subjectId}")
    public Subject updateSubject(@RequestBody Subject subject, @PathVariable long subjectId) throws Exception {
        // TODO: Add code to update a specific subject here
    	//create object of existing subject to see if subjectId does exist
    	Subject prevSubj = subjectService.getSubject(subjectId);
    	
    	//if subjectId does exists then update subjectId
        if (prevSubj != null) {
            Subject updateSubject = subjectService.updateSubject(prevSubj);
            return updateSubject;
        } 
        
        //if subjectId does not exists, throw exception
        else {
            throw new Exception("Error: SubjectId not found");
        }
        // TODO: Remove this once you can return a real object
        //return null; **removed**
    }

    @GetMapping(value = "/init")
    public boolean initSubjects() {
        // Create some sample subjects
        List<Subject> subjects = new ArrayList<>();

        subjects.add(new Subject.Builder().withSubjectName("Computer Science").build());
        subjects.add(new Subject.Builder().withSubjectName("Mathematics").build());
        subjects.add(new Subject.Builder().withSubjectName("English").build());
        subjects.add(new Subject.Builder().withSubjectName("History").build());

        for (Subject s : subjects) {
            Key key = subjectService.createSubject(s);
            s.setId(key.getId());
        }

        return true;
    }
}
