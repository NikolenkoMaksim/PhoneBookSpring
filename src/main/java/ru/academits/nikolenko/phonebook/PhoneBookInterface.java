package ru.academits.nikolenko.phonebook;

import org.springframework.web.bind.annotation.RequestBody;
import ru.academits.nikolenko.model.Contact;
import ru.academits.nikolenko.model.ContactValidation;
import ru.academits.nikolenko.model.DeleteResults;
import ru.academits.nikolenko.model.Filter;

import java.util.List;

public interface PhoneBookInterface {
    List<Contact> getAllContacts();

    ContactValidation addContact(@RequestBody Contact contact);

    DeleteResults deleteContacts(@RequestBody int[] contactsIds);

    List<Contact> getFilteredContacts(@RequestBody Filter filter);
}
