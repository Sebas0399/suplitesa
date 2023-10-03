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
import org.apache.poi.ss.usermodel.CellType;
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

    private static String obtenerRuc(Map<Integer, List<String>> factura) {
        return factura.get(2).get(0).split(" - ")[1];
    }

    private void generarFacturaExcel(List<Map<Integer, List<String>>> archivoGeneral) {
        // System.out.println(archivoGeneral);
        Map<String, List<Map<Integer, List<String>>>> rucFacturas = new HashMap<>();
        var c = 0;
        for (Map<Integer, List<String>> factura : archivoGeneral) {
            //c++;
            if (factura.containsKey(2)) {
                String ruc = obtenerRuc(factura);
                if (!rucFacturas.containsKey(ruc)) {
                    rucFacturas.put(ruc, new ArrayList<>());
                }
                rucFacturas.get(ruc).add(factura);
            }
        }
        //generarMP(rucFacturas);

        for (Map.Entry<String, List<Map<Integer, List<String>>>> entry : rucFacturas.entrySet()) {
            //System.out.println(entry.);
            c++;
            String ruc = entry.getKey();
            List<Map<Integer, List<String>>> facturas = entry.getValue();
            // System.out.println(ruc);

            //List<List<String>> valore = new ArrayList<>();
            List<List<String>> valore = new ArrayList<>();
            for (Map<Integer, List<String>> factura : facturas) {
                int rowNumber = 0;
                //System.out.println(factura.get(5).get(1));
                //System.out.println(factura.get(1));
                //generarMP(factura);
                var si = false;
                var suma = 0.0;
                for (int i = 0; i <= factura.size() + 1; i++) {
                    var elem = factura.get(i);

                    if (elem != null) {
                        if (elem.contains("Número DAU")) {
                            si = true;
                            i++;
                            elem = factura.get(i);
                        }
                        if (si) {
                            if (elem.size() > 7) {
                                // System.out.println(elem.get(3));

                                suma = suma + Double.parseDouble(elem.get(3));
                            }

                        }
                    }
                }
                var p = List.of(String.valueOf(factura.get(5).get(1)), Constantes.SUBPARTIDA_FC, Constantes.COMPLEMENTARIO_FC, "nroSerie", "nro item", "codigo", "subProd", "complementarioProd", "SuplementarioProd", "CERA PREPARADA", "KG", String.valueOf(suma * 0.0728), "0", "0");
                var q = List.of(String.valueOf(factura.get(5).get(1)), Constantes.SUBPARTIDA_FC, Constantes.COMPLEMENTARIO_FC, "nroSerie", "nro item", "codigo", "subProd", "complementarioProd", "SuplementarioProd", "ALMIDON", "KG", String.valueOf(suma * 0.00551), "0", "0");

                valore.add(p);
                valore.add(q);
                //  System.out.println("Total=" + suma);
                //System.out.println("Cera=" + suma * 0.00551);
                //System.out.println("Almidon" + suma * 0.0728);
                generarExcel(valore, ruc);
            }
            // System.out.println(valore);
            //System.out.println(valore.size());
        }

        //var listadoRUC = archivoGeneral.stream().map(x -> (x.get(2).get(0))).distinct().toList();
        //var listRuc = obtenerRucs(listadoRUC);
        // var listadoTotal = listRuc.stream().map(x -> getFacturasPorRuc(x, archivoGeneral)).toList();

        /*listadoTotal.forEach(x -> {

            var lFinal = new ArrayList<List<String>>();

            //var contador = 0;
            for (var y : x) {
                System.out.println(y);
                var seguir = true;
                var captarDatos = false;
                var contadorFila = 0;
                var indexProductos = 1;
                var suma = 0.0;
                while (contadorFila < y.size()) {
                    var fila = y.get(contadorFila);
                    if (fila.contains("TOTAL")) {

                        captarDatos = true;
                        contadorFila++;
                        //contadorItem = 0;
                        // contadorSerie++;
                    } else {
                        //var p = List.of(fila.get(1), String.valueOf(contadorSerie), String.valueOf(contadorItem), Constantes.SUBPARTIDA_FC, Constantes.COMPLEMENTARIO_FC, Constantes.SUPLEMENTARIO_FC, fila.get(6), "U", fila.get(10));
                        //lFinal.add(p);
                        // contadorItem++;
                        contadorFila++;
                    }
                    if (captarDatos == true) {
                        contadorFila++;

                        if (fila.size() > 7) {
                            System.out.println(fila.get(3));
                            suma = suma + Double.valueOf(fila.get(3));
                        }
                    }

                }
                System.out.println(suma);
                //var p = List.of(Constantes.SUBPARTIDA_FC, Constantes.COMPLEMENTARIO_FC, Constantes.SUPLEMENTARIO_FC, l.get(1), l.get(3));

                //  lFinal.stream().forEach(ab -> System.out.println(ab));
                //generarExcel(lFinal, x.get(0).get(2).get(0));
            }
        });*/
        System.out.println(c);
    }

    private void generarMP(Map<String, List<Map<Integer, List<String>>>> rucFacturas) {
        for (Map.Entry<String, List<Map<Integer, List<String>>>> entry : rucFacturas.entrySet()) {
            String ruc = entry.getKey();
            List<Map<Integer, List<String>>> facturas = entry.getValue();

            System.out.println("RUC: " + ruc);

            // Iterar sobre la lista de mapas
            for (Map<Integer, List<String>> factura : facturas) {
                // Iterar sobre los mapas anidados
                for (Map.Entry<Integer, List<String>> facturaEntry : factura.entrySet()) {
                    Integer numeroFactura = facturaEntry.getKey();
                    List<String> detalles = facturaEntry.getValue();

                    System.out.println("  Número de factura: " + numeroFactura);

                    // Iterar sobre la lista de detalles
                    for (String detalle : detalles) {
                        System.out.println("    Detalle: " + detalle);
                    }
                }
            }
        }

    }

    private void generarExcel(List<List<String>> lFinal, String ruc) {
        System.out.println("Reporte-----------------");
        System.out.println(ruc);

        System.out.println("Factura--------------");
        System.out.println(lFinal);
        System.out.println(lFinal.size());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<List<String>> cabeceras = List.of(
                List.of("No. Factura asociada", "Complementario", "Suplementario", "el número de serie", "Numero de item", "Código", "Subpartida", "Complementario", "Suplementario", "Descripción", "Tipo Unidad", "Cantidad Transformado", "Cantidad de Desperdicio", "Cantidad de Merma"
                ),
                List.of("csgd_ntfc_no", "prdt_hs_part_cd", "prdt_hs_cpmt_cd", "prdt_hs_spmt_cd", "prdt_sn", "csgd_item_sn", "csgd_cmdt_cd", "csgd_hs_part_cd", "csgd_hs_cpmt_cd", "csgd_hs_spmt_cd", "csgd_prdt_desc", "csgd_ut_tp_cd	csgd_trsm_use_qt", "csgd_duse_qt", "csgd_use_ips_duse_qt"
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

        String fileLocation = "CRA_MP_" + ruc + ".xls";
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
                Map<Integer, List<String>> hojaData = new HashMap<>();  // Crear un Map para cada hoja

                for (Row row : sheet) {
                    List<String> rowData = new ArrayList<>();

                    for (Cell cell : row) {
                        switch (cell.getCellType()) {
                            case STRING ->
                                rowData.add(cell.getRichStringCellValue().getString());
                            case NUMERIC -> {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    rowData.add(sdf.format(cell.getDateCellValue()));
                                } else {
                                    rowData.add(String.valueOf(cell.getNumericCellValue()));
                                }
                            }
                            case BOOLEAN ->
                                rowData.add(String.valueOf(cell.getBooleanCellValue()));
                            case FORMULA ->
                                rowData.add(cell.getCellFormula());
                            default ->
                                rowData.add(" ");
                        }
                    }

                    hojaData.put(row.getRowNum(), rowData);  // Agregar la lista a la posición del mapa
                }

                archivoGeneral.add(hojaData); // Agregar el mapa de la hoja al listado general
            }

            generarFacturaExcel(archivoGeneral);
        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(null, "Cargue el reporte de producción");
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
