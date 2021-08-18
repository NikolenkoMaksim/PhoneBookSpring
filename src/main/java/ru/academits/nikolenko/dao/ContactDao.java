package ru.academits.nikolenko.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.academits.nikolenko.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ContactDao implements PhoneBookDao {
    private static final Logger logger = LoggerFactory.getLogger(ContactDao.class);

    private final List<Contact> contactList = new ArrayList<>();
    private final AtomicInteger idSequence = new AtomicInteger(0);

    public ContactDao() {
        Contact contact = new Contact();
        contact.setId(getNewId());
        contact.setFirstName("Иван");
        contact.setLastName("Иванов");
        contact.setPhone("9123456789");
        contactList.add(contact);
    }

    private int getNewId() {
        return idSequence.addAndGet(1);
    }

    public List<Contact> getAllContacts() {
        return contactList;
    }

    public List<Contact> getFilteredContacts(String filter) {
        if (filter.equals("")) {
            return new ArrayList<>();
        }

        List<Contact> result = new ArrayList<>();
        String upperCaseFilter = filter.toUpperCase();

        for (Contact contact : contactList) {
            String upperCaseFirstName = contact.getFirstName().toUpperCase();
            String upperCaseLastName = contact.getLastName().toUpperCase();
            String upperCasePhone = contact.getPhone().toUpperCase();

            if (upperCaseFirstName.contains(upperCaseFilter) || upperCaseLastName.contains(upperCaseFilter) || upperCasePhone.contains(upperCaseFilter)) {
                result.add(contact);
            }
        }

        logger.info("Find filtered contacts count = " + result.size());

        return result;
    }

    public void add(Contact contact) {
        contact.setId(getNewId());
        contactList.add(contact);
        logger.info("Contact added. Contacts count = " + contactList.size());
    }

    public boolean deleteContact(int contactId) {
        Contact contact = findContact(contactId);

        boolean result = contactList.remove(contact);

        logger.info("Delete contact with id = " + contactId + " deleted: " + result + ". Contacts count = " + contactList.size());

        return result;
    }

    private Contact findContact(int contactId) {
        for (Contact currentContact : contactList) {
            if (contactId == currentContact.getId()) {
                return currentContact;
            }
        }

        return null;
    }

    public void deleteAnyContact() {
        if (contactList.size() > 0) {
            int randomContactIndex = (int) (Math.random() * contactList.size());

            Contact deletedContact = contactList.remove(randomContactIndex);

            if (deletedContact != null) {
                logger.info("Deleted contact with id = " + deletedContact.getId() + ". Contacts count = " + contactList.size());
            }
        }
    }
}
