package ru.academits.nikolenko.service;

import org.springframework.stereotype.Service;
import ru.academits.nikolenko.dao.ContactDao;
import ru.academits.nikolenko.dao.PhoneBookDao;
import ru.academits.nikolenko.model.Contact;
import ru.academits.nikolenko.model.ContactValidation;
import ru.academits.nikolenko.model.DeleteResults;

import java.util.List;

@Service
public class ContactService implements PhoneBookService {
    private final PhoneBookDao contactDao;

    public ContactService(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    private boolean isExistContactWithPhone(String phone) {
        List<Contact> contactList = contactDao.getAllContacts();

        for (Contact contact : contactList) {
            if (contact.getPhone().equals(phone)) {
                return true;
            }
        }

        return false;
    }

    private ContactValidation validateContact(Contact contact) {
        ContactValidation contactValidation = new ContactValidation();
        contactValidation.setValid(true);


        if (contact.getFirstName().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Поле Имя должно быть заполнено.");
            return contactValidation;
        }

        if (contact.getLastName().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Поле Фамилия должно быть заполнено.");
            return contactValidation;
        }

        if (contact.getPhone().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Поле Номер телефона должно быть заполнено.");
            return contactValidation;
        }

        if (isExistContactWithPhone(contact.getPhone())) {
            contactValidation.setValid(false);
            contactValidation.setError("Номер телефона не должен дублировать другие номера в телефонной книге.");
            return contactValidation;
        }

        return contactValidation;
    }

    public ContactValidation addContact(Contact contact) {
        ContactValidation contactValidation = validateContact(contact);

        if (contactValidation.isValid()) {
            contactDao.add(contact);
        }

        return contactValidation;
    }

    public List<Contact> getAllContacts() {
        return contactDao.getAllContacts();
    }

    public DeleteResults deleteContacts(int[] contactsIds) {
        if (contactsIds == null) {
            return new DeleteResults(false, "Не найдем массив с id контактов");
        }

        if (contactsIds.length == 0) {
            return new DeleteResults(false, "Массив с id контактов пуст");
        }

        StringBuilder error = new StringBuilder();
        error.append("Не удалось удалить контакты со следующими id: ");

        boolean deletedAll = true;

        for (int contactId : contactsIds) {
            if (!contactDao.deleteContact(contactId)) {
                deletedAll = false;
                error.append(contactId).append(", ");
            }
        }

        if (deletedAll) {
            return new DeleteResults(true, "");
        }

        return new DeleteResults(false, error.delete(error.length() - 2, error.length()).toString());
    }

    public List<Contact> getFilteredContacts(String filter) {
        return contactDao.getFilteredContacts(filter);
    }

    public void deleteAnyContact() {
        contactDao.deleteAnyContact();
    }
}
