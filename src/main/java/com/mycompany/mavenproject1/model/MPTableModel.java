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
public class MPTableModel extends AbstractTableModel {

    /*private String[] columnNames = {"No. Factura asociada",
        "Subpartida",
        "Complementario",
        "Suplementario",
        "Número de serie",
        "Número de ítem",
        "Código",
        "Subpartida",
        "Complementario",
        "Suplementario",
        "Descripción",
        "Tipo Unidad",
        "Cantidad Transformado",
        "Cantidad de Desperdicio",
        "Cantidad de Merma"};*/
    private String[] columnNames = {"Material","Cód. Insumo","Descripción","Empresa","",""};
    private Object[][] data ;
    public MPTableModel(Object[][]data){
        this.data=data;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }


 
}
