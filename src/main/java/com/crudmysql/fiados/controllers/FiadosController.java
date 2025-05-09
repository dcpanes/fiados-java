package com.crudmysql.fiados.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crudmysql.fiados.models.FiadosModel;
import com.crudmysql.fiados.services.FiadosService;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/fiados")
public class FiadosController {

    @Autowired
    private FiadosService fiadosService;

    @GetMapping("/listar")
    public ArrayList<FiadosModel> obtenerFiados() {
        return fiadosService.obtenerFiados();
    }

    @PostMapping("/guardar")
    public FiadosModel guardarFiado(@RequestBody FiadosModel fiado) {
        return fiadosService.guardarFiado(fiado);
    }

    @GetMapping("/obtenerPorId/{id}")
    public FiadosModel obtenerPorId(@PathVariable Long id) {
        return fiadosService.obtenerPorId(id);
    }

    @DeleteMapping("/eliminar/{id}")
    public boolean eliminarFiado(@PathVariable Long id) {
        return fiadosService.eliminarFiado(id);
    }

}
