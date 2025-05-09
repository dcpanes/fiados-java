package com.crudmysql.fiados.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crudmysql.fiados.models.FiadosModel;
import com.crudmysql.fiados.repositories.IFiadosRepository;

@Service
public class FiadosService {

    @Autowired
    private IFiadosRepository fiadosRepository;

    public ArrayList<FiadosModel> obtenerFiados() {
        return (ArrayList<FiadosModel>) fiadosRepository.findAll();
    }

    public FiadosModel guardarFiado(FiadosModel fiado) {
        return fiadosRepository.save(fiado);
    }

    public FiadosModel obtenerPorId(Long id) {
        return fiadosRepository.findById(id).orElse(null);
    }

    public boolean eliminarFiado(Long id) {
        try {
            fiadosRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
