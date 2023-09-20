/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author Sebas
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Sebas
 */
public class MPGenerador {

    private Map<String, String> paths;

    public MPGenerador(Map<String, String> paths) {
        this.paths = paths;
    }

    private List<String> extraerNumeros(String input) {
        List<String> numeros = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d{13}");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            numeros.add(matcher.group());
        }

        return numeros;
    }

    private List<String> obtenerRucs(List<String> rucs) {
        List<String> numeros = new ArrayList<>();
        for (var input : rucs) {
            numeros.addAll(extraerNumeros(input));
        }
        return numeros;
    }

    private List<Map<Integer, List<String>>> getFacturasPorRuc(String ruc, List<Map<Integer, List<String>>> archivoGeneral) {
        List<Map<Integer, List<String>>> res = new ArrayList<>();
        for (var factura : archivoGeneral) {
            var r = factura.get(2).get(0).split("-");

            if (r[r.length - 1].trim().equals(ruc.trim())) {
                res.add(factura);
            }
        }

        return res;
    }

    private void generarFacturaExcel(List<Map<Integer, List<String>>> archivoGeneral) {
        var listadoRUC = archivoGeneral.stream().map(x -> (x.get(2).get(0))).distinct().toList();
        var listRuc = obtenerRucs(listadoRUC);

        var listadoTotal = listRuc.stream().map(x -> getFacturasPorRuc(x, archivoGeneral)).toList();

        listadoTotal.forEach(x -> {
            if (x.size() > 1) {
                var lFinal = new ArrayList<List<String>>();

                var contador = 0;

                for (var y : x) {
                    System.out.println(y);
                    //var p = List.of(Constantes.SUBPARTIDA_FC, Constantes.COMPLEMENTARIO_FC, Constantes.SUPLEMENTARIO_FC, l.get(1), l.get(3));
                }
                //  lFinal.stream().forEach(ab -> System.out.println(ab));
                //generarExcel(lFinal, x.get(0).get(2).get(0));
            }
        });
    }

    private void generarExcel(List<List<String>> lFinal, String nombreRuc) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<List<String>> cabeceras = List.of(
                List.of("No. Factura asociada", "el número de serie", "Numero de item", "Subpartida", "Complementario", "Suplementario", "Descripción", "Tipo Unidad", "Cantidad Transformado"
                ),
                List.of("ntfc_prdt_cl_cd", "sn", "prdt_item_sn", "prdt_hs_part_cd", "prdt_hs_cpmt_cd", "prdt_hs_spmt_cd", "prdt_prdt_desc", "prdt_ut_tp_cd", "prdt_trsm_use_qt"
                )
        );

        int rowNumber = 0;
        for (List<String> cabecera : cabeceras) {
            Row header = sheet.createRow(rowNumber++);
            for (int i = 0; i < cabecera.size(); i++) {
                header.createCell(i).setCellValue(cabecera.get(i));
            }
        }

        for (List<String> x : lFinal) {
            Row r = sheet.createRow(rowNumber++);
            int k = 0;
            for (String j : x) {
                r.createCell(k++).setCellValue(j);
            }
        }

        String fileLocation = "CRA_PT_" + nombreRuc + ".xls";
        try (FileOutputStream outputStream = new FileOutputStream(fileLocation)) {
            workbook.write(outputStream);
        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            workbook.close();
        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarFacturas() {
        try {
            List<Map<Integer, List<String>>> archivoGeneral = new ArrayList<>();
            FileInputStream file = new FileInputStream(new File(this.paths.get("RP")));
            Workbook workbook = new XSSFWorkbook(file);
            int numHojas = workbook.getNumberOfSheets();

            for (int i = 0; i <= numHojas - 1; i++) {
                Sheet sheet = workbook.getSheetAt(i);

                Map<Integer, List<String>> data = new HashMap<>();
                int j = 0;
                for (Row row : sheet) {
                    data.put(j, new ArrayList<String>());
                    for (Cell cell : row) {
                        switch (cell.getCellType()) {
                            case STRING:
                                data.get(new Integer(j)).add(cell.getRichStringCellValue().getString());
                                break;
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    data.get(j).add(sdf.format(cell.getDateCellValue()) + "");
                                } else {
                                    data.get(j).add(cell.getNumericCellValue() + "");
                                }
                                break;
                            case BOOLEAN:
                                data.get(j).add(cell.getBooleanCellValue() + "");
                                break;
                            case FORMULA:
                                data.get(j).add(cell.getCellFormula() + "");
                                break;
                            default:
                                data.get(new Integer(j)).add(" ");
                        }
                    }
                    j++;
                }
                archivoGeneral.add(data);
            }
            generarFacturaExcel(archivoGeneral);
        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(null, "Cargue el reporte de produccion");
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
