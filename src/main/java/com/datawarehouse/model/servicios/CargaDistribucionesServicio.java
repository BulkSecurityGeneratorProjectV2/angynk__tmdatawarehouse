package com.datawarehouse.model.servicios;

import com.datawarehouse.model.dao.ArchivosDao;
import com.datawarehouse.model.dao.DistribucionesDao;
import com.datawarehouse.model.entity.Archivos;
import com.datawarehouse.model.entity.Cuadro;
import com.datawarehouse.model.entity.Programacion;
import com.datawarehouse.view.util.FormatoArchivo;
import com.datawarehouse.view.util.LogDatos;
import com.datawarehouse.view.util.PathFiles;
import com.datawarehouse.view.util.TipoLog;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class CargaDistribucionesServicio {

    @Autowired
    private ArchivosDao archivosDao;

    @Autowired
    private DistribucionesDao distribucionesDao;


    public CargaDistribucionesServicio() {
    }


    public List<LogDatos> agregarDatosDistribuciones(Programacion programacion, Archivos archivo, List<LogDatos> logDatos) {
        String nombre = archivo.getNombre();
        if(archivo.getTipo().equals(FormatoArchivo.CSV_COMMA)){

        }else if (archivo.getTipo().equals(FormatoArchivo.CSV_PUNTO_COMMA)){
            nombre = incluirFilasArchivo(programacion,nombre,archivo.getCuadro());
        }

        try {
            distribucionesDao.cargarArchivoDistribuciones(nombre);
            archivosDao.addArchivos(archivo);
        } catch (Exception e) {
            logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
        }

        return logDatos;
    }

    private String incluirFilasArchivo(Programacion programacion, String nombre, Cuadro cuadro) {
        String csvFile = PathFiles.PATH+"/"+nombre;
        String csvFileOut = PathFiles.PATH+"/out_"+nombre;
        CSVWriter writer = null;
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(csvFile),';');
            writer = new CSVWriter(new FileWriter(csvFileOut), ',');
            String[] entries = null;
            while ((entries = reader.readNext()) != null) {
                ArrayList<String> list = new ArrayList(Arrays.asList(entries));
//                list.set(9,list.get(9).replace(",","."));
//                if(list.size()>12){
//                    list.remove(list.size()-1);
//                }
                list.add(programacion.getIdentificador());
                list.add(cuadro.getNumero());
                entries =  list.toArray(new String[list.size()]);
                writer.writeNext(entries);
            }
            writer.close();
        } catch (IOException e) {
        }
        return csvFileOut;
    }
}
