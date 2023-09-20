/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Sebas
 */
public class FileUtils {
      public static String cargarData() {
        var j = new JFileChooser(FileSystemView.getFileSystemView());
        j.setFileFilter(new FileNameExtensionFilter("Excel","xlsx"));
        int r = j.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            return j.getSelectedFile().getAbsolutePath();

        } else {
            return null;
        }

    }
       public static String saveData() {
        var j = new JFileChooser(FileSystemView.getFileSystemView());
       // j.setFileFilter(new FileNameExtensionFilter("Excel","xlsx"));
       j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int r = j.showSaveDialog(null);
        
        if (r == JFileChooser.APPROVE_OPTION) {
            return j.getSelectedFile().getAbsolutePath();

        } else {
            return null;
        }

    }
}
