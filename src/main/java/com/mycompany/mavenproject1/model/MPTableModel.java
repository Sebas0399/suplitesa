/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.model;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Sebas
 */
public class MPTableModel extends AbstractTableModel{
    private Object[][] datos;
    private String[] columnas;

    public MPTableModel(Object[][] datos, String[] columnas) {
        this.datos = datos;
        this.columnas = columnas;
    }

    @Override
    public int getRowCount() {
        return datos.length;
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return datos[rowIndex][columnIndex];
    }
    
}
