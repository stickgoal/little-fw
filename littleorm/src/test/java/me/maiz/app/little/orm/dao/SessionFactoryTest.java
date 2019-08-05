package me.maiz.app.little.orm.dao;

import lombok.extern.slf4j.Slf4j;
import me.maiz.app.little.orm.main.entity.User;
import me.maiz.app.little.orm.meta.model.Configuration;
import me.maiz.app.little.orm.meta.model.Mapping;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

@Slf4j
public class SessionFactoryTest {


    @Test
    public void testInit(){
        final SessionFactory sessionFactory = new SessionFactory("/orm.properties");

        sessionFactory.configure();
        log.info("sessionFactory ： {}",sessionFactory);
        final Configuration configuration = sessionFactory.getConfiguration();
        assertNotNull(configuration);
        assertEquals("jdbc:mysql://localhost:3306/test",configuration.getUrl());
        assertEquals("root",configuration.getUsername());
        assertEquals("root123",configuration.getPassword());
        assertEquals("com.mysql.cj.jdbc.Driver",configuration.getDriverClassName());
        final Map<String, Mapping> metaMap = sessionFactory.getMetaMap();
        assertNotNull(metaMap);
        assertEquals(1,metaMap.size());
        final Mapping mapping = metaMap.get("User");
        assertNotNull(mapping);
        assertEquals("test_user", mapping.getTableName());
        assertEquals(User.class, mapping.getEntityClass());
        assertEquals("user_id", mapping.getPkName());
        assertNotNull(mapping.getColumnMappings());
        assertEquals(3,mapping.getColumnMappings().size());
        assertNotNull(mapping.getColumnNameMapping());
        assertEquals(3,mapping.getColumnNameMapping().size());

        final Session session = sessionFactory.openSession();
        assertNotNull(session);
        

    }

}