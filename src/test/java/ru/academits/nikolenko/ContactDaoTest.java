package ru.academits.nikolenko;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.academits.nikolenko.dao.ContactDao;
import ru.academits.nikolenko.model.Contact;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ContactDaoTest extends Assert {
    private static final Logger logger = LoggerFactory.getLogger(ContactDaoTest.class);

    private ContactDao contactDao;
    private final Contact testContact = new Contact("Name1", "LastName1", "54654");

    @Before
    public void createContactDao() {
        contactDao = new ContactDao();
    }

    @Test
    public void initialContactTest() {
        List<Contact> contacts = contactDao.getAllContacts();
        Assertions.assertEquals(1, contacts.size());

        Contact initialContact = contacts.get(0);
        Assertions.assertEquals(1, initialContact.getId());
        Assertions.assertEquals("Иван", initialContact.getFirstName());
        Assertions.assertEquals("Иванов", initialContact.getLastName());
        Assertions.assertEquals("9123456789", initialContact.getPhone());

        logger.info("initialContactTest passed");
    }

    @Test
    public void addContactTest() {
        contactDao.add(testContact);

        List<Contact> contacts = contactDao.getAllContacts();
        Assertions.assertEquals(2, contacts.size());

        Contact addedContact = contacts.get(1);
        Assertions.assertEquals(2, addedContact.getId());
        Assertions.assertEquals(testContact.getFirstName(), addedContact.getFirstName());
        Assertions.assertEquals(testContact.getLastName(), addedContact.getLastName());
        Assertions.assertEquals(testContact.getPhone(), addedContact.getPhone());

        logger.info("addContactTest passed");
    }

    @Test
    public void deleteContactTest() {
        Assertions.assertFalse(contactDao.deleteContact(0));
        Assertions.assertFalse(contactDao.deleteContact(2));
        Assertions.assertTrue(contactDao.deleteContact(1));
        Assertions.assertFalse(contactDao.deleteContact(1));

        logger.info("deleteContactTest passed");
    }

    @Test
    public void deleteAnyContactTest() {
        contactDao.add(testContact);
        Assertions.assertEquals(2, contactDao.getAllContacts().size());

        contactDao.deleteAnyContact();
        Assertions.assertEquals(1, contactDao.getAllContacts().size());

        contactDao.deleteAnyContact();
        Assertions.assertEquals(0, contactDao.getAllContacts().size());

        contactDao.deleteAnyContact();
        Assertions.assertEquals(0, contactDao.getAllContacts().size());

        logger.info("deleteAnyContactTest passed");
    }

    @Test
    public void getFilteredContactsTest() {
        contactDao.add(testContact);

        List<Contact> filteredContacts = contactDao.getFilteredContacts("и");
        Assertions.assertEquals(1, filteredContacts.size());
        Assertions.assertEquals(1, filteredContacts.get(0).getId());

        filteredContacts = contactDao.getFilteredContacts("");
        Assertions.assertEquals(0, filteredContacts.size());

        filteredContacts = contactDao.getFilteredContacts("m");
        Assertions.assertEquals(1, filteredContacts.size());
        Assertions.assertEquals(2, filteredContacts.get(0).getId());

        filteredContacts = contactDao.getFilteredContacts("z");
        Assertions.assertEquals(0, filteredContacts.size());

        filteredContacts = contactDao.getFilteredContacts("5");
        Assertions.assertEquals(2, filteredContacts.size());

        logger.info("getFilteredContactsTest passed");
    }
}
