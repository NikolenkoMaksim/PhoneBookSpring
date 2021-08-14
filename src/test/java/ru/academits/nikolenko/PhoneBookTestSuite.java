package ru.academits.nikolenko;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({ContactDaoTest.class, ContactServiceTest.class, PhoneBookControllerTest.class})
@RunWith(Suite.class)
public class PhoneBookTestSuite {
}
