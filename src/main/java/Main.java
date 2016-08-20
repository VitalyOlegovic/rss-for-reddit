import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.JobBuilder.*;

public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String ... args){

        try {
            Properties properties = readProperties();

            SchedulerFactory sf = new StdSchedulerFactory();
            Scheduler sched = sf.getScheduler();
            JobDetail job = newJob(OrchestratorJob.class)
                    .withIdentity("job1", "group1")
                    .build();

            CronTrigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .withSchedule(cronSchedule(properties.getProperty("scheduler.cron.expression")))
                    .build();

            sched.scheduleJob(job, trigger);
            sched.start();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    private static Properties readProperties() throws IOException {
        Properties prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("scheduler.properties");
        prop.load(stream);
        return prop;
    }

}
