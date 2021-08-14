package ru.academits.nikolenko;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.academits.nikolenko.dao.ContactDao;
import ru.academits.nikolenko.model.Contact;
import ru.academits.nikolenko.model.ContactValidation;
import ru.academits.nikolenko.model.DeleteResults;
import ru.academits.nikolenko.model.Filter;
import ru.academits.nikolenko.phonebook.PhoneBookController;
import ru.academits.nikolenko.service.ContactService;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PhoneBookControllerTest extends Assert {
    private static final Logger logger = LoggerFactory.getLogger(PhoneBookControllerTest.class);

    private Contact testContact;
    private ContactDao contactDao;
    private ContactService service;
    private PhoneBookController controller;

    @Before
    public void createContactDao() {
        contactDao = new ContactDao();
        service = new ContactService(contactDao);
        controller = new PhoneBookController(service);
        testContact = new Contact("Name1", "LastName1", "54654");
    }

    @Test
    public void getAllContactsTest() {
        Assertions.assertEquals(contactDao.getAllContacts(), controller.getAllContacts());
    }

    @Test
    public void addContactTest() {
        Assertions.assertEquals(1, controller.getAllContacts().size());

        ContactValidation result = controller.addContact(testContact);
        Assertions.assertTrue(result.isValid());
        Assertions.assertEquals(2, contactDao.getAllContacts().size());

        result = controller.addContact(testContact);
        Assertions.assertFalse(result.isValid());
        Assertions.assertEquals("Номер телефона не должен дублировать другие номера в телефонной книге.", result.getError());

        testContact.setFirstName("");
        result = controller.addContact(testContact);
        Assertions.assertFalse(result.isValid());
        Assertions.assertEquals("Поле Имя должно быть заполнено.", result.getError());

        testContact.setFirstName("B");
        testContact.setLastName("");
        result = controller.addContact(testContact);
        Assertions.assertFalse(result.isValid());
        Assertions.assertEquals("Поле Фамилия должно быть заполнено.", result.getError());

        testContact.setLastName("B");
        testContact.setPhone("");
        result = controller.addContact(testContact);
        Assertions.assertFalse(result.isValid());
        Assertions.assertEquals("Поле Номер телефона должно быть заполнено.", result.getError());

        Assertions.assertEquals(2, contactDao.getAllContacts().size());

        logger.info("addContactTest passed");
    }

    @Test
    public void deleteContactsTest() {
        DeleteResults results = controller.deleteContacts(null);
        Assertions.assertFalse(results.getIsDeleted());
        Assertions.assertEquals("Не найдем массив с id контактов", results.getError());

        results = controller.deleteContacts(new int[0]);
        Assertions.assertFalse(results.getIsDeleted());
        Assertions.assertEquals("Массив с id контактов пуст", results.getError());

        results = controller.deleteContacts(new int[]{0, 1, 3});
        Assertions.assertFalse(results.getIsDeleted());
        Assertions.assertEquals("Не удалось удалить контакты со следующими id: 0, 3", results.getError());

        controller.addContact(testContact);
        results = service.deleteContacts(new int[]{1});
        Assertions.assertFalse(results.getIsDeleted());
        Assertions.assertEquals("Не удалось удалить контакты со следующими id: 1", results.getError());

        controller.addContact(testContact);
        results = service.deleteContacts(new int[]{2});
        Assertions.assertTrue(results.getIsDeleted());
        Assertions.assertEquals("", results.getError());

        logger.info("deleteContactsTest passed");
    }

    @Test
    public void getFilteredContactsTest() {
        controller.addContact(testContact);

        List<Contact> filteredContacts = controller.getFilteredContacts(new Filter("и"));
        Assertions.assertEquals(1, filteredContacts.size());
        Assertions.assertEquals(1, filteredContacts.get(0).getId());

        filteredContacts = controller.getFilteredContacts(new Filter(""));
        Assertions.assertEquals(0, filteredContacts.size());

        filteredContacts = controller.getFilteredContacts(new Filter("m"));
        Assertions.assertEquals(1, filteredContacts.size());
        Assertions.assertEquals(2, filteredContacts.get(0).getId());

        filteredContacts = controller.getFilteredContacts(new Filter("z"));
        Assertions.assertEquals(0, filteredContacts.size());

        filteredContacts = controller.getFilteredContacts(new Filter("5"));
        Assertions.assertEquals(2, filteredContacts.size());

        logger.info("getFilteredContactsTest passed");
    }
}
