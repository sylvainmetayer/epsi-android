package fr.sylvainmetayer.creationcontact;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE name LIKE :name LIMIT 1")
    User findByName(String name);

    @Query("SELECT * FROM user WHERE name LIKE :name OR email LIKE :email")
    List<User> findByNameOrEmail(String name, String email);

    @Insert
    void insertAll(User... users);

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);
}