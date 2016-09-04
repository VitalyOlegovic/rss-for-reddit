package reddit_bot.service;

import org.junit.Test;
import reddit_bot.AbstractTest;

public class MigrationServiceTest extends AbstractTest {

    MigrationService migrationService = configurableApplicationContext.getBean(MigrationService.class);

    @Test
    public void migrate(){
        migrationService.migrate();
    }

}
