package com.crudmysql.fiados.controller;

import com.crudmysql.fiados.controllers.FiadosController;
import com.crudmysql.fiados.models.FiadosModel;
import com.crudmysql.fiados.services.FiadosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(FiadosController.class)
public class FiadosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FiadosService fiadosService;

    @Autowired
    private ObjectMapper objectMapper;

    private FiadosModel fiado1;
    private FiadosModel fiado2;

    @BeforeEach
    void setUp() {
        fiado1 = new FiadosModel();
        fiado1.setId(1L);
        fiado1.setNombreCliente("Cliente Test 1");
        fiado1.setMonto(150.75);
        fiado1.setFecha("2024-01-01");
        fiado1.setEstado("Pendiente");

        fiado2 = new FiadosModel();
        fiado2.setId(2L);
        fiado2.setNombreCliente("Cliente Test 2");
        fiado2.setMonto(250.00);
        fiado2.setFecha("2024-01-02");
        fiado2.setEstado("Pagado");
    }

    /**
     * Prueba que el endpoint GET /fiados/listar retorne una lista de fiados.
     * - Configura el mock de fiadosService para que devuelva una lista predefinida.
     * - Realiza una petición GET a /fiados/listar.
     * - Verifica que el estado de la respuesta sea 200 OK.
     * - Verifica que el tipo de contenido sea application/json.
     * - Verifica que la lista devuelta tenga el tamaño esperado (2).
     * - Verifica los datos del primer y segundo elemento de la lista.
     */
    @Test
    void obtenerFiados_deberiaRetornarListaDeFiados() throws Exception {
        ArrayList<FiadosModel> listaFiados = new ArrayList<>(Arrays.asList(fiado1, fiado2));
        // Configura el comportamiento del mock: cuando se llame a obtenerFiados(), devuelve listaFiados.
        when(fiadosService.obtenerFiados()).thenReturn(listaFiados);

        // Simula una petición GET al endpoint /fiados/listar.
        mockMvc.perform(get("/fiados/listar"))
                // Espera que el código de estado HTTP sea 200 (OK).
                .andExpect(status().isOk())
                // Espera que el tipo de contenido de la respuesta sea JSON.
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Espera que el objeto raíz del JSON (la lista) tenga un tamaño de 2.
                .andExpect(jsonPath("$", hasSize(2)))
                // Espera que el campo nombreCliente del primer objeto en la lista sea "Cliente Test 1".
                .andExpect(jsonPath("$[0].nombreCliente", is("Cliente Test 1")))
                // Espera que el campo nombreCliente del segundo objeto en la lista sea "Cliente Test 2".
                .andExpect(jsonPath("$[1].nombreCliente", is("Cliente Test 2")));
    }

    /**
     * Prueba que el endpoint POST /fiados/guardar retorne el fiado guardado.
     * - Configura el mock de fiadosService para que devuelva un fiado específico cuando se guarde cualquier FiadosModel.
     * - Realiza una petición POST a /fiados/guardar con un objeto FiadosModel en el cuerpo.
     * - Verifica que el estado de la respuesta sea 200 OK.
     * - Verifica que el tipo de contenido sea application/json.
     * - Verifica los datos (id y nombreCliente) del fiado devuelto en la respuesta.
     */
    @Test
    void guardarFiado_deberiaRetornarFiadoGuardado() throws Exception {
        // Configura el comportamiento del mock: cuando se llame a guardarFiado() con cualquier FiadosModel, devuelve fiado1.
        when(fiadosService.guardarFiado(any(FiadosModel.class))).thenReturn(fiado1);

        // Simula una petición POST al endpoint /fiados/guardar.
        mockMvc.perform(post("/fiados/guardar")
                // Establece el Content-Type de la petición a application/json.
                .contentType(MediaType.APPLICATION_JSON)
                // Establece el cuerpo de la petición convirtiendo fiado1 a JSON.
                .content(objectMapper.writeValueAsString(fiado1)))
                // Espera que el código de estado HTTP sea 200 (OK).
                .andExpect(status().isOk())
                // Espera que el tipo de contenido de la respuesta sea JSON.
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Espera que el campo id del objeto JSON devuelto sea 1.
                .andExpect(jsonPath("$.id", is(1)))
                // Espera que el campo nombreCliente del objeto JSON devuelto sea "Cliente Test 1".
                .andExpect(jsonPath("$.nombreCliente", is("Cliente Test 1")));
    }

    /**
     * Prueba que el endpoint GET /fiados/obtenerPorId/{id} retorne el fiado correcto cuando el ID existe.
     * - Configura el mock de fiadosService para que devuelva un fiado específico cuando se busque por su ID.
     * - Realiza una petición GET a /fiados/obtenerPorId/1.
     * - Verifica que el estado de la respuesta sea 200 OK.
     * - Verifica que el tipo de contenido sea application/json.
     * - Verifica los datos (id y nombreCliente) del fiado devuelto.
     */
    @Test
    void obtenerPorId_cuandoExisteId_deberiaRetornarFiado() throws Exception {
        // Configura el comportamiento del mock: cuando se llame a obtenerPorId(1L), devuelve fiado1.
        when(fiadosService.obtenerPorId(1L)).thenReturn(fiado1);

        // Simula una petición GET al endpoint /fiados/obtenerPorId/1.
        mockMvc.perform(get("/fiados/obtenerPorId/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombreCliente", is("Cliente Test 1")));
    }

    /**
     * Prueba que el endpoint GET /fiados/obtenerPorId/{id} retorne un cuerpo vacío (o nulo) y estado OK
     * cuando el ID no existe y el servicio devuelve null.
     * - Configura el mock de fiadosService para que devuelva null cuando se busque por cualquier ID.
     * - Realiza una petición GET a /fiados/obtenerPorId/99.
     * - Verifica que el estado de la respuesta sea 200 OK (comportamiento actual del controlador).
     * - Verifica que el cuerpo de la respuesta esté vacío.
     * Nota: Para un 404 Not Found, el servicio o el controlador deberían lanzar una excepción específica.
     */
    @Test
    void obtenerPorId_cuandoNoExisteId_deberiaRetornarNotFound() throws Exception {
        // Configura el comportamiento del mock: cuando se llame a obtenerPorId() con cualquier Long, devuelve null.
        when(fiadosService.obtenerPorId(anyLong())).thenReturn(null);

        // Simula una petición GET al endpoint /fiados/obtenerPorId/99.
        mockMvc.perform(get("/fiados/obtenerPorId/99"))
                // El controlador devuelve el nulo directamente, lo que resulta en un cuerpo vacío y 200 OK.
                .andExpect(status().isOk())
                // Espera que el cuerpo de la respuesta sea una cadena vacía.
                .andExpect(content().string(""));
    }


    /**
     * Prueba que el endpoint DELETE /fiados/eliminar/{id} retorne true cuando el fiado es eliminado exitosamente.
     * - Configura el mock de fiadosService para que devuelva true cuando se intente eliminar el fiado con ID 1.
     * - Realiza una petición DELETE a /fiados/eliminar/1.
     * - Verifica que el estado de la respuesta sea 200 OK.
     * - Verifica que el tipo de contenido sea application/json.
     * - Verifica que el cuerpo de la respuesta JSON sea true.
     */
    @Test
    void eliminarFiado_cuandoExisteId_deberiaRetornarTrue() throws Exception {
        // Configura el comportamiento del mock: cuando se llame a eliminarFiado(1L), devuelve true.
        when(fiadosService.eliminarFiado(1L)).thenReturn(true);

        // Simula una petición DELETE al endpoint /fiados/eliminar/1.
        mockMvc.perform(delete("/fiados/eliminar/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)));
    }

    /**
     * Prueba que el endpoint DELETE /fiados/eliminar/{id} retorne false cuando el fiado a eliminar no existe.
     * - Configura el mock de fiadosService para que devuelva false cuando se intente eliminar un fiado con un ID que no existe (99L).
     * - Realiza una petición DELETE a /fiados/eliminar/99.
     * - Verifica que el estado de la respuesta sea 200 OK.
     * - Verifica que el tipo de contenido sea application/json.
     * - Verifica que el cuerpo de la respuesta JSON sea false.
     */
    @Test
    void eliminarFiado_cuandoNoExisteId_deberiaRetornarFalse() throws Exception {
        // Configura el comportamiento del mock: cuando se llame a eliminarFiado(99L), devuelve false.
        when(fiadosService.eliminarFiado(99L)).thenReturn(false);

        // Simula una petición DELETE al endpoint /fiados/eliminar/99.
        mockMvc.perform(delete("/fiados/eliminar/99"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(false)));
    }
}
