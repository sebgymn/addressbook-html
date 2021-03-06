package com.sebworks.addressbook;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Can be tested by
 * curl -X POST -d '{"name": "rocky", "surname": "balboa", "city": "philadelphia", "phone": "18001234567"}' -v -H "Content-Type:application/json" http://localhost:8080/api/save
 * curl http://localhost:8080/api/all
 * <p/>
 * Created by seb on 12.08.2015.
 */
@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    ContactRepository repository;

    @ApiOperation("Returns all contacts in AddressBook")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Contact> findAll() {
        Iterable<Contact> all = repository.findAll();
        ArrayList list = new ArrayList();
        for (Contact contact : all) {
            list.add(contact);
        }
        return list;
    }

    @ApiOperation("Saves or updates a contact")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity save(@RequestBody Contact contact) {
        try {
            if (contact.getName().isEmpty() || contact.getSurname().isEmpty() || contact.getCity().isEmpty() || contact.getPhone().isEmpty()) {
                return ResponseEntity.badRequest().build(); //400
            }
            if (contact.getId() != null && contact.getId().trim().isEmpty())
                contact.setId(null);
            repository.save(contact);
            System.out.println("saved. current db:");
            repository.findAll().forEach(c -> System.out.println(c.toString()));
            return ResponseEntity.noContent().build(); //204
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @ApiOperation("Deletes given contact")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") String id) {
        System.out.println("delete: " + id);
        repository.delete(id);
        return ResponseEntity.noContent().build();
    }

}
