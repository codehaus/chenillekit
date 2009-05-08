/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.hibernate.daos;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import org.chenillekit.hibernate.AbstractHibernateTest;
import org.chenillekit.hibernate.entities.Address;
import org.chenillekit.hibernate.entities.Pseudonym;
import org.chenillekit.hibernate.entities.User;
import org.chenillekit.hibernate.factories.TestDAOFactory;
import org.chenillekit.hibernate.utils.QueryParameter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @version $Id$
 */
public class TestUserDAO extends AbstractHibernateTest
{
    protected TestDAOFactory daoFactory;
    protected Session session;

    @BeforeTest
    public void setup()
    {
        super.setup();
        session = sessionFactory.openSession();
        daoFactory = new TestDAOFactory(session);
    }

    @Test
    public void persist_user_entity()
    {
        Transaction transaction = session.beginTransaction();
        UserDAO userDAO = (UserDAO) daoFactory.getUserDAO();

        User user = new User("homburgs", "password");
        user.setLastLogin(new Date());
        user.setActive(true);

        Address address = new Address();
        address.setName1("Sven");
        address.setName1("Homburg");
        address.setStreet("WhereEver");
        address.setCity("SmallTown");
        address.setZip("11111");
        address.setEmail("homburgs@gmail.com");

        user.setAddress(address);
        user.getPseudonyms().add(new Pseudonym(user, "jolli"));
        user.getPseudonyms().add(new Pseudonym(user, "trugoy"));
        user.getPseudonyms().add(new Pseudonym(user, "hombi"));

        userDAO.doSave(user);
        transaction.commit();
    }

    @Test(dependsOnMethods = {"persist_user_entity"})
    public void find_user_entity()
    {
        UserDAO userDAO = daoFactory.getUserDAO();

        List entityList = userDAO.findByQuery("FROM User WHERE loginName = :loginName",
                                              QueryParameter.newInstance("loginName", "homburgs"));

        assertEquals(entityList.size(), 1);
    }

    @Test(dependsOnMethods = {"persist_user_entity"})
    public void remove_pseudonym_entity()
    {
        Transaction transaction = session.beginTransaction();
        UserDAO userDAO = daoFactory.getUserDAO();

        List<User> entityList = userDAO.findByQuery("FROM User WHERE loginName = :loginName",
                                              QueryParameter.newInstance("loginName", "homburgs"));

        for (User user : entityList)
        {
            for (Pseudonym pseudonym : user.getPseudonyms())
            {
                user.getPseudonyms().remove(pseudonym);
                break;
            }
            userDAO.doSave(user);
            assertEquals(user.getPseudonyms().size(), 2);
        }

        transaction.commit();
    }

    @Test(dependsOnMethods = {"persist_user_entity"})
    public void group_by_loginname()
    {
        UserDAO userDAO = daoFactory.getUserDAO();

        String result = (String) userDAO.aggregateOrGroup("SELECT loginName FROM User GROUP BY loginName");

        assertEquals(result, "homburgs");
    }

    @Test(dependsOnMethods = {"persist_user_entity"})
    public void max_id()
    {
        UserDAO userDAO = daoFactory.getUserDAO();

        long result = (Long) userDAO.aggregateOrGroup("SELECT MAX(id) FROM User");

        assertEquals(result, 1);
    }

    @AfterTest
    public void afterTest()
    {
        sessionFactory.close();
    }
}