package io.asktech.payout.utils.RandomNameGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RandomName {
    @Autowired
    RandomNameRepo randomNameRepo;
    static Logger logger = LoggerFactory.getLogger(RandomName.class);
    public String getRandomName() {
       
       String name = randomNameRepo.getRandomName();
       logger.info("getRandomName::"+name);
        return name;
    }
}
