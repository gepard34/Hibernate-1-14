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

    private final SessionFactory factory = Util.getInstance().getSessionFactory();
    private final Logger logger = Logger.getLogger(UserDaoHibernateImpl.class.getName());


    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Users " +
                "(ID BIGINT NOT NULL AUTO_INCREMENT, name VARCHAR(30) NOT NULL," +
                " lastName VARCHAR(50) NOT NULL , age INT NOT NULL , PRIMARY KEY (id))";
        Transaction tx = null;
        try (Session session = factory.getCurrentSession()) {
            tx = session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw new RuntimeException();
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS Users";
        Transaction tx = null;
        try (Session session = factory.getCurrentSession()) {
            tx = session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw new RuntimeException();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction tx = null;
        try (Session session = factory.getCurrentSession()) {
            tx = session.beginTransaction();
            session.save(new User(name, lastName, age));
            tx.commit();
            logger.log(Level.INFO, "User с именем – {0} добавлен в базу данных", name);
        } catch (HibernateException e) {
            tx.rollback();
            throw new RuntimeException();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction tx = null;
        try (Session session = factory.getCurrentSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw new RuntimeException();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Transaction tx = null;
        try (Session session = factory.getCurrentSession()) {
            tx = session.beginTransaction();
            users = session.createQuery("FROM User")
                    .getResultList();
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw new RuntimeException();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE Users";
        Transaction tx = null;
        try (Session session = factory.getCurrentSession()) {
            tx = session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw new RuntimeException();
        }
    }
}

