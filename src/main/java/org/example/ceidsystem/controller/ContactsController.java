package org.example.ceid_v2.controller;

import org.example.ceid_v2.repository.ContactPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactsController {

    @Autowired
    private ContactPersonRepository contactPersonRepository;

    @GetMapping("/contacts")
    public String contacts(Model model){
        model.addAttribute("contacts", contactPersonRepository.findAllByOrderByDepartmentAscNameAsc());
        return "contacts/list";
    }
}




