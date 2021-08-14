package ru.academits.nikolenko.dao;

import ru.academits.nikolenko.model.Contact;

import java.util.List;

public interface PhoneBookDao {
    List<Contact> getAllContacts();

    List<Contact> getFilteredContacts(String filter);

    void add(Contact contact);

    boolean deleteContact(int contactId);

    void deleteAnyContact();
}
