function Contact(contactData) {
    this.firstName = contactData[0];
    this.lastName = contactData[1];
    this.phone = contactData[2];
    this.checked = false;
    this.shown = true;
}

function Filter(filter) {
    this.filter = filter;
}

new Vue({
    el: "#app",

    created: function () {
        this.loadData();
    },

    data: {
        contactsDataInputs: [
            {
                id: "lastNameInput",
                name: "Фамилия",
                text: "",
                isInvalid: false,
                errorMessage: "Пожалуйста, заполните поле"
            },
            {
                id: "firstNameInput",
                name: "Имя",
                text: "",
                isInvalid: false,
                errorMessage: "Пожалуйста, заполните поле"
            },
            {
                id: "phoneInput",
                name: "Номер телефона",
                text: "",
                isInvalid: false,
                errorMessage: "Пожалуйста, заполните поле"
            }
        ],
        validation: false,
        contacts: [],
        serverError: "",
        filterInput: "",
        isDeleteContactsButtonShow: false
    },

    computed: {
        deleteAllCheckboxValue: function () {
            let checkedContactsCount = 0;

            this.contacts.forEach(c => {
                if (c.isChecked) {
                    checkedContactsCount++;
                }
            });

            return checkedContactsCount === this.contacts.length && checkedContactsCount !== 0;
        }
    },

    methods: {
        convertContactList(contactListFromServer) {
            return contactListFromServer.map((contact, i) => {
                return {
                    id: contact.id,
                    firstName: contact.firstName,
                    lastName: contact.lastName,
                    phone: contact.phone,
                    checked: false,
                    shown: true,
                    number: i + 1
                };
            });
        },

        addContact() {
            let isValidationFailed = false;

            this.contactsDataInputs.forEach((contactDataInput) => {
                if (contactDataInput.text.trim() === "") {
                    isValidationFailed = true;
                    contactDataInput.isInvalid = true;
                    contactDataInput.text = "";
                } else {
                    contactDataInput.isInvalid = false;
                }
            });

            if (isValidationFailed) {
                this.contactsDataInputs[2].errorMessage = "Пожалуйста, заполните поле";
                return;
            }

            this.contacts.forEach(contact => {
                if (contact.phone === this.contactsDataInputs[2].text) {
                    this.contactsDataInputs[2].errorMessage = "Контакт с таким номером уже существует";
                    this.contactsDataInputs[2].isInvalid = true;
                    isValidationFailed = true;
                }
            });

            if (isValidationFailed) {
                return;
            }

            let contactData = [];

            this.contactsDataInputs.forEach((contactDataInput, i) => {
                contactData[i] = contactDataInput.text;
                contactDataInput.text = "";
            });

            const contact = new Contact(contactData);

            $.ajax({
                type: "POST",
                url: "/phoneBook/rpc/api/v1/addContact",
                contentType: "application/json",
                data: JSON.stringify(contact)
            }).fail((ajaxRequest) => {
                const contactValidation = JSON.parse(ajaxRequest.responseText);
                this.serverError = contactValidation.error;
                console.log("Ошибка. " + this.serverError);
            }).always(() => {
                this.loadData();
                this.filterInput = "";
            });
        },

        loadData() {
            $.get("/phoneBook/rpc/api/v1/getAllContacts").done(response => {
                this.contacts = this.convertContactList(response);
            });
        },

        loadFilteredContacts() {
            if (this.filterInput.trim() === "") {
                return;
            }

            const filter = new Filter(this.filterInput.trim());

            $.ajax({
                type: "POST",
                url: "/phoneBook/rpc/api/v1/getFilteredContacts",
                contentType: "application/json",
                data: JSON.stringify(filter)
            }).done(response => {
                this.contacts = this.convertContactList(response);
            });
        },

        clearSearch() {
            this.filterInput = "";
            this.loadData();
        },

        deleteContacts() {
            let contactsIds = [];

            this.contacts.forEach(c => {
                if (c.isChecked) {
                    contactsIds.push(c.id);
                }
            });

            if (contactsIds.length === 0) {
                alert("Не выбрано ни одного контакта");
                return;
            }

            $.ajax({
                type: "POST",
                url: "/phoneBook/rpc/api/v1/deleteContacts",
                contentType: "application/json",
                data: JSON.stringify(contactsIds)
            }).fail(responseData => {
                this.serverError = responseData.error;
                console.log("Ошибка. " + this.serverError);
            }).always(() => {
                this.loadData();
                this.filterInput = "";
            });
        },

        deleteContact(contact) {
            const contactsIds = [contact.id];

            $.ajax({
                type: "POST",
                url: "/phoneBook/rpc/api/v1/deleteContacts",
                contentType: "application/json",
                data: JSON.stringify(contactsIds)
            }).fail(responseData => {
                this.serverError = responseData.error;
                console.log("Ошибка. " + this.serverError);
            }).always(() => {
                this.loadData();
                this.filterInput = "";
            });
        },

        chooseAll() {
            const currentState = !this.deleteAllCheckboxValue;

            this.contacts.forEach(c => {
                c.isChecked = currentState;
            });
        },

        checkIsAllChecked() {
            this.contacts.forEach(c => {
                if(!c.isChecked) {
                    this.isDeleteContactsButtonShow = false;
                    return;
                }
            });

            this.isDeleteContactsButtonShow = true;
        }
    }
});

