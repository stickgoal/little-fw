package me.maiz.app.little.orm.main.entity;

import lombok.Data;
import me.maiz.app.little.orm.annotation.Entity;

@Entity(tableName = "table_user")
@Data
public class User {

    private String username;

    private String password;

}
