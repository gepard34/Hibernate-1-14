package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        User user1 = new User("Vadim","Ivanov",(byte) 25);
        User user2 = new User("Sergey","Petrov",(byte) 21);
        User user3 = new User("Kate","Sidorova",(byte) 25);
        User user4 = new User("Elena","Alekseeva",(byte) 25);

        UserServiceImpl userService = new UserServiceImpl();

        userService.createUsersTable();
        userService.saveUser(user1.getName(), user1.getLastName(), user1.getAge());
        userService.saveUser(user2.getName(), user2.getLastName(), user2.getAge());
        userService.saveUser(user3.getName(), user3.getLastName(), user3.getAge());
        userService.saveUser(user4.getName(), user4.getLastName(), user4.getAge());
        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();



    }
}
