package com.datawarehouse.model.servicios;

import com.datawarehouse.model.dao.ArchivosDao;
import com.datawarehouse.model.dao.ProgramacionDao;
import com.datawarehouse.model.entity.Archivos;
import com.datawarehouse.model.entity.Programacion;
import com.datawarehouse.view.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service("CargaDatosServicios")
public class CargaDatosServicios {

    @Autowired
    private ProgramacionDao programacionDao;

    @Autowired
    private ArchivosDao archivosDao;

    @Autowired
    private ProcessorUtils processorUtils;

    @Autowired
    private CargaExpedicionesServicio cargaExpedicionesServicio;

    @Autowired
    private CargaBusesServicio cargaBusesServicio;

    @Autowired
    private CargaTablaHorarioServicio cargaTablaHorarioServicio;

    @Autowired
    private CargaTracelogServicio cargaTracelogServicio;


    public boolean existeProgramacion(String identificador) {
        Programacion programacion = programacionDao.encontrarProgramacion(identificador);
        if(programacion!=null) return true;
        return false;
    }

    public void agregarProgramacion(Programacion programacion) {
        programacionDao.addProgramacion(programacion);
    }

    public String copyFile(String fileName, InputStream in){
        processorUtils.copyFile(fileName,in, PathFiles.PATH);
        return fileName;
    }

    public List<LogDatos> cargarArchivoNuevo(Archivos archivo, List<LogDatos> logDatos, Programacion programacion) {
        if(archivo.getGrupo().equals(TipoArchivo.expediciones)){

        }else if(archivo.getGrupo().equals(TipoArchivo.buses)){
              logDatos = cargaBusesServicio.agregarInformacionBuses(programacion,archivo,logDatos);
        }else if(archivo.getGrupo().equals(TipoArchivo.tracelog)){
              logDatos = cargaTracelogServicio.agregarDatosTracelog(programacion,archivo,logDatos);
        }else if(archivo.getGrupo().equals(TipoArchivo.distribuciones)){

        }else if(archivo.getGrupo().equals(TipoArchivo.tablaHorario)){
               logDatos = cargaTablaHorarioServicio.agregarInformacionTablaHorario(programacion,archivo,logDatos);
        }else if(archivo.getGrupo().equals(TipoArchivo.matrizDistancia)){

        }
        return logDatos;
    }



    public String incluirFilaArchivo(String nombre, Programacion nuevaProgramacion, String tipo,String modo) {
        if(tipo.equals(FormatoArchivo.CSV_COMMA)){
            return cargaExpedicionesServicio.incluirFilaArchivo(nombre,nuevaProgramacion,modo);
        }
        return cargaExpedicionesServicio.incluirFilaArchivoPuntoComa(nombre,nuevaProgramacion,modo);

    }

    public void cargarArchivoExpediciones(String nombre) throws Exception {
        cargaExpedicionesServicio.cargarArchivoExpediciones(nombre);
    }

    public void eliminarDatosTemporales(Programacion nuevaProgramacion) {
        cargaExpedicionesServicio.eliminarDatosTemporales(nuevaProgramacion);
    }

    public List<Programacion> getProgramaciones(Date fechaInicio, Date fechaFin, String tipoDia,String modo) {
        return programacionDao.getProgramaciones(fechaInicio,fechaFin,tipoDia,modo);
    }

    public void agregarArchivo(Archivos archivo) {
        archivosDao.addArchivos(archivo);
    }

    public List<Archivos> obtenerArchivosLista(String progr) {
        Programacion programacion = programacionDao.encontrarProgramacion(progr);
        return archivosDao.encontrarArchivos(programacion);
    }

    public Programacion obtenerProgramacion(String progr) {
        return programacionDao.encontrarProgramacion(progr);
    }
}
