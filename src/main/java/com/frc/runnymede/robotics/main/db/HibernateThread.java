package com.frc.runnymede.robotics.main.db;
import com.frc.runnymede.robotics.main.Main;
import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;

/**
 * The type Hibernate thread.
 */
public class HibernateThread implements Runnable {

    public static Configuration configuration;


    public static ArrayList<Class> managedEntities = new ArrayList<>();


        //Classes to add to managed entities
    static {


    }


    /**
     * Hibernate thread object
     */

    public static Thread hibernateThread;
    /**
     * The constant ourSessionFactory.
     */
    public static SessionFactory ourSessionFactory;
        /**
         * Logger object
         */
        private static Logger logger = LoggerFactory.getLogger(HibernateThread.class);

    /**
     * Gets session.
     *
     * @return the session
     * @throws HibernateException the hibernate exception
     */
    public static SessionFactory getSessionFactory() throws HibernateException {
        return ourSessionFactory;
    }


    /**
     * Build session factory session factory.ii
     *
     * @return the session factory
     */
    public static SessionFactory buildSessionFactory() {
        try {
            logger.info("Session factory built, hibernate thread");
            configuration = new Configuration()
                    .setProperty("hibernate.connection.url", Main.config.mysql_url)
                    .setProperty("hibernate.connection.driver_class", Main.config.mysql_driver)
                    .setProperty("hibernate.connection.username", Main.config.mysql_user)
                    .setProperty("hibernate.connection.password", Main.config.mysql_password)
                    .setProperty("hibernate.show_sql", "true")
                    .setProperty("hibernate.c3p0.acquire_increment","1")
                    .setProperty("hibernate.c3p0.idle_test_period","100")
                    .setProperty("hibernate.c3p0.max_size","100")
                    .setProperty("hibernate.c3p0.max_statements","100")
                    .setProperty("hibernate.c3p0.min_size","10")
                    .setProperty("hibernate.c3p0.timeout","180")
                    .setProperty("hibernate.hbm2ddl.auto", "update");

            for (Class entityClass : managedEntities) {
                configuration.addAnnotatedClass(entityClass);
            }
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
            logger.info("DB MANAGEMENT THREAD IS OPERATIONAL !!");
            return ourSessionFactory;
        }
        catch (Exception e){
            e.printStackTrace();
            logger.error("HIBERNATE SESSION FACTORY HAS THROWN AN ERROR AND THUS IS NOT ACTIVE !!!");
        }
        throw new RuntimeException("HIBERNATE THREAD ERROR!!!!!");
    }

    /**
     * Shut down.
     */
    public static void shutDown() {
        logger.info("Hibernate session factory closed");
        ourSessionFactory.close();
    }

    public static void initHibernateThread() {
        logger.info("Hibernate thread initialized");
        HibernateThread.buildSessionFactory();
        hibernateThread = new Thread(new HibernateThread());
        hibernateThread.start();
    }

    @Override
    public void run() {
        final Session session = getSessionFactory().openSession();
        try {
            logger.info("querying all the managed entities...");
            final Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                final String entityName = entityType.getName();
                final Query query = session.createQuery("from " + entityName);
                logger.info("executing: " + query.getQueryString());
                for (Object o : query.list()) {
                    System.out.println("  " + o.toString());
                }
            }
        } finally {
            session.close();
        }


    }

}