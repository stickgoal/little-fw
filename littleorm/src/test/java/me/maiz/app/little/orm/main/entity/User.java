package me.maiz.app.little.orm.main.entity;

import lombok.Data;
import me.maiz.app.little.orm.annotation.Column;
import me.maiz.app.little.orm.annotation.Entity;
import me.maiz.app.little.orm.annotation.Id;

import java.util.Date;

@Entity(tableName = "test_user")
@Data
public class User {

    @Id
    @Column(columnName="user_id")
    private int userId;

    @Column(columnName="username")
    private String username;

    @Column(columnName="password")
    private String password;

    @Column(columnName = "reg_date")
    private Date registerDate;

}
