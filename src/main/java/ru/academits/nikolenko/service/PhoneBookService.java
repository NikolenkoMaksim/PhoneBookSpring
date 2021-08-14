package ru.academits.nikolenko.service;

import ru.academits.nikolenko.model.Contact;
import ru.academits.nikolenko.model.ContactValidation;
import ru.academits.nikolenko.model.DeleteResults;

import java.util.List;

public interface PhoneBookService {
    ContactValidation addContact(Contact contact);

    List<Contact> getAllContacts();

    DeleteResults deleteContacts(int[] contactsIds);

    List<Contact> getFilteredContacts(String filter);

    void deleteAnyContact();
}
