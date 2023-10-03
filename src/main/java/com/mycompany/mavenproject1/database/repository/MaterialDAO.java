/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.database.repository;

import com.mycompany.mavenproject1.database.model.Cliente;
import com.mycompany.mavenproject1.database.model.Material;
import com.mycompany.mavenproject1.database.model.MaterialReporte;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Sebas
 */
public class MaterialDAO implements IMaterialDAO {

    private SessionFactory sessionFactory;
public MaterialDAO() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Material.class).buildSessionFactory();
    }
    @Override
    public void create(Material material) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            session.persist(material);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<Material> readAll() {
        Session session = sessionFactory.getCurrentSession();
        List<Material> materiales = new ArrayList<>();
        try {
            session.beginTransaction();
            materiales = session.createQuery("SELECT m FROM Material m", Material.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
        return materiales;
    }

    // Otros métodos según tus necesidades...

    @Override
    public Boolean delete(String subpartida) {
 Session session = sessionFactory.openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            Material reporte = readBySubpartida(subpartida);
            if (reporte != null) {
                session.delete(reporte);
                transaction.commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public Material readBySubpartida(String codigo) {
Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Material material = null;

        try {
            transaction = session.beginTransaction();
            material = session.createQuery("SELECT m FROM Material m WHERE m.codigo = :codigo", Material.class)
                    .setParameter("codigo", codigo)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return material;
    }
}
