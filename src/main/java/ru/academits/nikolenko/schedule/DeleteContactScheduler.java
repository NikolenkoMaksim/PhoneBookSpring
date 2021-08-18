package ru.academits.nikolenko.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.academits.nikolenko.service.PhoneBookService;

@EnableAutoConfiguration
@EnableScheduling
@Component
public class DeleteContactScheduler {
    private static final Logger logger = LoggerFactory.getLogger(DeleteContactScheduler.class);
    private final int delay = 3600000;

    private final PhoneBookService contactService;

    public DeleteContactScheduler(PhoneBookService contactService) {
        this.contactService = contactService;
    }

    @Scheduled(fixedDelay = delay, initialDelay = delay)
    public void deleteAnyContact() {
        logger.info("Called scheduled function deleteAnyContact is called in DeleteContactScheduler");
        contactService.deleteAnyContact();
    }
}
