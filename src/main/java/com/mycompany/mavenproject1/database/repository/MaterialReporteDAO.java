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
@Transactional
public class MaterialReporteDAO implements IMaterialReporteDAO {

    private SessionFactory sessionFactory;

    public MaterialReporteDAO() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(MaterialReporte.class).buildSessionFactory();
    }

    @Override
    public void create(MaterialReporte material) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            session.persist(material);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public List<MaterialReporte> readAll() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<MaterialReporte> materiales = new ArrayList<>();
        
        try {
            transaction = session.beginTransaction();
            materiales = session.createQuery("SELECT m FROM MaterialReporte m", MaterialReporte.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        
        return materiales;
    }

    @Override
    public Boolean delete(String codigo) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            MaterialReporte reporte = readByCodigo(codigo);
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
    public MaterialReporte readByCodigo(String codigo) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        MaterialReporte reporte = null;

        try {
            transaction = session.beginTransaction();
            reporte = session.createQuery("SELECT m FROM MaterialReporte m WHERE m.codigo = :codigo", MaterialReporte.class)
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

        return reporte;
    }
}

