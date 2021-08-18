package ru.academits.nikolenko.phonebook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.academits.nikolenko.model.Contact;
import ru.academits.nikolenko.model.ContactValidation;
import ru.academits.nikolenko.model.DeleteResults;
import ru.academits.nikolenko.model.Filter;
import ru.academits.nikolenko.service.ContactService;
import ru.academits.nikolenko.service.PhoneBookService;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/phoneBook/rpc/api/v1")
public class PhoneBookController implements PhoneBookInterface {
    private static final Logger logger = LoggerFactory.getLogger(PhoneBookController.class);

    private final PhoneBookService contactService;

    public PhoneBookController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(value = "getAllContacts", method = RequestMethod.GET)
    @ResponseBody
    public List<Contact> getAllContacts() {
        logger.info("Called method getAllContacts in PhoneBookController");
        return contactService.getAllContacts();
    }

    @RequestMapping(value = "addContact", method = RequestMethod.POST)
    @ResponseBody
    public ContactValidation addContact(@RequestBody Contact contact) {
        logger.info("Called method addContact in PhoneBookController. contact = " + contact.toString());
        return contactService.addContact(contact);
    }

    @RequestMapping(value = "deleteContacts", method = RequestMethod.POST)
    @ResponseBody
    public DeleteResults deleteContacts(@RequestBody int[] contactsIds) {
        logger.info("Called method deleteContacts in PhoneBookController. contactsIds = " + Arrays.toString(contactsIds));
        return contactService.deleteContacts(contactsIds);
    }

    @RequestMapping(value = "getFilteredContacts", method = RequestMethod.POST)
    @ResponseBody
    public List<Contact> getFilteredContacts(@RequestBody Filter filter) {
        logger.info("Called method getFilteredContacts in PhoneBookController. filter = " + filter.getFilter());
        return contactService.getFilteredContacts(filter.getFilter());
    }
}


