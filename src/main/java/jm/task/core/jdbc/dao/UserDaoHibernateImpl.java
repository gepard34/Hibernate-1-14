package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory factory = Util.getSessionFactory();
    private final Logger logger = Logger.getLogger(UserDaoHibernateImpl.class.getName());
    private Transaction transaction;
    private Session session;

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Users " +
                "(ID BIGINT NOT NULL AUTO_INCREMENT, name VARCHAR(30) NOT NULL," +
                " lastName VARCHAR(50) NOT NULL , age INT NOT NULL , PRIMARY KEY (id))";

        try {
            session = factory.getCurrentSession();
            transaction = session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            throw new RuntimeException();
        } finally {
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS Users";

        try {
            session = factory.getCurrentSession();
            transaction = session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            throw new RuntimeException();
        } finally {
            session.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        try {
            session = factory.getCurrentSession();
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
            logger.log(Level.INFO, "User с именем – {0} добавлен в базу данных", name);
        } catch (HibernateException e) {
            transaction.rollback();
            throw new RuntimeException();
        } finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {

        try {
            session = factory.getCurrentSession();
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            throw new RuntimeException();
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            session = factory.getCurrentSession();
            transaction = session.beginTransaction();
            users = session.createQuery("FROM User")
                    .getResultList();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            throw new RuntimeException();
        } finally {
            session.close();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE Users";
        try {
            session = factory.getCurrentSession();
            transaction = session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            throw new RuntimeException();
        } finally {
            session.close();
        }
    }
}

