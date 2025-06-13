package com.crudmysql.fiados.services;

import com.crudmysql.fiados.models.FiadosModel;
import com.crudmysql.fiados.repositories.IFiadosRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FiadosServiceTest {

    // @Mock crea un objeto simulado (un "doble de prueba") para IFiadosRepository.
    // Esto nos permite controlar cómo se comporta el repositorio durante las pruebas,
    // sin necesidad de una base de datos real.
    @Mock
    private IFiadosRepository fiadosRepository;

    // @InjectMocks crea una instancia real de FiadosService e intenta inyectar
    // los mocks creados con @Mock (como fiadosRepository) en ella.
    @InjectMocks
    private FiadosService fiadosService;

    // Variables para guardar objetos FiadosModel de prueba que usaremos en varios tests.
    private FiadosModel fiado1;
    private FiadosModel fiado2;

    // El método anotado con @BeforeEach se ejecuta antes de CADA método de prueba en esta clase.
    // Es útil para configurar el estado inicial común para las pruebas.
    @BeforeEach
    void setUp() {
        // Inicializa los mocks y las inyecciones para esta clase de prueba.
        MockitoAnnotations.openMocks(this);

        // Creamos un primer objeto FiadosModel de ejemplo.
        fiado1 = new FiadosModel();
        fiado1.setId(1L); // 'L' indica que es un número de tipo Long
        fiado1.setNombreCliente("Cliente 1");
        fiado1.setMonto(100.0);
        fiado1.setFecha("2024-07-30");
        fiado1.setEstado("Pendiente");

        // Creamos un segundo objeto FiadosModel de ejemplo.
        fiado2 = new FiadosModel();
        fiado2.setId(2L);
        fiado2.setNombreCliente("Cliente 2");
        fiado2.setMonto(200.0);
        fiado2.setFecha("2024-07-31");
        fiado2.setEstado("Pagado");
    }

    @Test // @Test marca este método como una prueba unitaria.
    void obtenerFiados_deberiaRetornarListaDeFiados() {
        // --- ARRANGE (Organizar/Preparar) ---
        // 1. Preparamos los datos que esperamos que el repositorio simulado devuelva.
        // Creamos una lista (ArrayList) que contiene nuestros dos fiados de ejemplo.
        ArrayList<FiadosModel> listaFiadosSimulada = new ArrayList<>(Arrays.asList(fiado1, fiado2));

        // 2. Configuramos el comportamiento del repositorio simulado (fiadosRepository).
        // "Cuando se llame al método findAll() en fiadosRepository, entonces devuelve nuestra listaFiadosSimulada".
        when(fiadosRepository.findAll()).thenReturn(listaFiadosSimulada);

        // --- ACT (Actuar) ---
        // 3. Llamamos al método del servicio que queremos probar.
        ArrayList<FiadosModel> resultadoObtenido = fiadosService.obtenerFiados();

        // --- ASSERT (Afirmar/Verificar) ---
        // 4. Verificamos que el resultado obtenido es el esperado.
        // Comprobamos que la lista devuelta tiene 2 elementos.
        assertEquals(2, resultadoObtenido.size(), "La lista debería contener 2 fiados.");
        // Comprobamos que el nombre del cliente del primer fiado en la lista es "Cliente 1".
        assertEquals("Cliente 1", resultadoObtenido.get(0).getNombreCliente(), "El nombre del primer cliente no es el esperado.");
        // Comprobamos que el nombre del cliente del segundo fiado en la lista es "Cliente 2".
        assertEquals("Cliente 2", resultadoObtenido.get(1).getNombreCliente(), "El nombre del segundo cliente no es el esperado.");


        // 5. Verificamos que el método findAll() del repositorio fue llamado exactamente una vez.
        // Esto asegura que nuestro servicio está interactuando con el repositorio como se espera.
        verify(fiadosRepository, times(1)).findAll();
    }

    @Test
    void guardarFiado_deberiaRetornarFiadoGuardado() {
        // --- ARRANGE ---
        // 1. Configuramos el comportamiento del repositorio simulado.
        // "Cuando se llame al método save() en fiadosRepository con CUALQUIER objeto FiadosModel
        // (any(FiadosModel.class)), entonces devuelve nuestro objeto fiado1".
        // Esto simula que la base de datos guardó el objeto y lo devolvió (quizás con un ID generado).
        when(fiadosRepository.save(any(FiadosModel.class))).thenReturn(fiado1);

        // --- ACT ---
        // 2. Llamamos al método del servicio que queremos probar, pasándole fiado1 para "guardar".
        FiadosModel fiadoGuardado = fiadosService.guardarFiado(fiado1);

        // --- ASSERT ---
        // 3. Verificamos el resultado.
        // Comprobamos que el objeto devuelto no es nulo.
        assertNotNull(fiadoGuardado, "El fiado guardado no debería ser nulo.");
        // Comprobamos que el nombre del cliente del fiado guardado es "Cliente 1".
        assertEquals("Cliente 1", fiadoGuardado.getNombreCliente(), "El nombre del cliente del fiado guardado no es el esperado.");

        // 4. Verificamos que el método save() del repositorio fue llamado exactamente una vez,
        // y específicamente con el objeto fiado1.
        verify(fiadosRepository, times(1)).save(fiado1);
    }

    @Test
    void obtenerPorId_cuandoExisteId_deberiaRetornarFiado() {
        // --- ARRANGE ---
        // 1. Configuramos el comportamiento del repositorio simulado.
        // "Cuando se llame al método findById() en fiadosRepository con el ID 1L,
        // entonces devuelve un Optional que contiene nuestro objeto fiado1".
        // Optional se usa porque un fiado podría no existir para un ID dado.
        when(fiadosRepository.findById(1L)).thenReturn(Optional.of(fiado1));

        // --- ACT ---
        // 2. Llamamos al método del servicio que queremos probar, pidiendo el fiado con ID 1L.
        FiadosModel fiadoEncontrado = fiadosService.obtenerPorId(1L);

        // --- ASSERT ---
        // 3. Verificamos el resultado.
        // Comprobamos que el objeto devuelto no es nulo (lo que significa que se encontró).
        assertNotNull(fiadoEncontrado, "El fiado encontrado no debería ser nulo.");
        // Comprobamos que el ID del fiado encontrado es 1L.
        assertEquals(1L, fiadoEncontrado.getId(), "El ID del fiado encontrado no es el esperado.");
        // Comprobamos que el nombre del cliente es el esperado.
        assertEquals("Cliente 1", fiadoEncontrado.getNombreCliente(), "El nombre del cliente no es el esperado.");


        // 4. Verificamos que el método findById() del repositorio fue llamado exactamente una vez con el ID 1L.
        verify(fiadosRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_cuandoNoExisteId_deberiaRetornarNull() {
        // --- ARRANGE ---
        // 1. Configuramos el comportamiento del repositorio simulado.
        // "Cuando se llame al método findById() en fiadosRepository con el ID 3L (un ID que no hemos configurado),
        // entonces devuelve un Optional vacío (Optional.empty())".
        // Esto simula que no se encontró ningún fiado con ese ID en la base de datos.
        when(fiadosRepository.findById(3L)).thenReturn(Optional.empty());

        // --- ACT ---
        // 2. Llamamos al método del servicio que queremos probar, pidiendo el fiado con ID 3L.
        FiadosModel fiadoEncontrado = fiadosService.obtenerPorId(3L);

        // --- ASSERT ---
        // 3. Verificamos el resultado.
        // Comprobamos que el objeto devuelto ES nulo, porque el servicio convierte un Optional.empty() a null.
        assertNull(fiadoEncontrado, "El fiado encontrado debería ser nulo si no existe.");

        // 4. Verificamos que el método findById() del repositorio fue llamado exactamente una vez con el ID 3L.
        verify(fiadosRepository, times(1)).findById(3L);
    }

    @Test
    void eliminarFiado_cuandoExisteId_deberiaRetornarTrue() {
        // --- ARRANGE ---
        // 1. Configuramos el comportamiento del repositorio simulado para el método deleteById.
        // Como deleteById devuelve void (nada), no usamos thenReturn().
        // doNothing() especifica que no se debe lanzar ninguna excepción cuando se llame.
        // "Cuando se llame al método deleteById() en fiadosRepository con el ID 1L, no hagas nada (simula éxito)".
        doNothing().when(fiadosRepository).deleteById(1L);

        // --- ACT ---
        // 2. Llamamos al método del servicio que queremos probar.
        boolean resultadoEliminacion = fiadosService.eliminarFiado(1L);

        // --- ASSERT ---
        // 3. Verificamos el resultado.
        // Comprobamos que el método devolvió true, indicando que la eliminación fue (simuladamente) exitosa.
        assertTrue(resultadoEliminacion, "La eliminación debería retornar true si tiene éxito.");

        // 4. Verificamos que el método deleteById() del repositorio fue llamado exactamente una vez con el ID 1L.
        verify(fiadosRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarFiado_cuandoOcurreExcepcion_deberiaRetornarFalse() {
        // --- ARRANGE ---
        // 1. Configuramos el comportamiento del repositorio simulado para que lance un error.
        // "Cuando se llame al método deleteById() en fiadosRepository con el ID 1L,
        // entonces lanza una nueva RuntimeException con el mensaje 'Error al eliminar'".
        // Esto simula un problema durante la eliminación en la capa de persistencia.
        doThrow(new RuntimeException("Error al eliminar")).when(fiadosRepository).deleteById(1L);

        // --- ACT ---
        // 2. Llamamos al método del servicio que queremos probar.
        boolean resultadoEliminacion = fiadosService.eliminarFiado(1L);

        // --- ASSERT ---
        // 3. Verificamos el resultado.
        // Comprobamos que el método devolvió false, porque el servicio debería capturar la excepción
        // y retornar false para indicar que la eliminación falló.
        assertFalse(resultadoEliminacion, "La eliminación debería retornar false si ocurre una excepción.");

        // 4. Verificamos que el método deleteById() del repositorio fue llamado exactamente una vez con el ID 1L,
        // incluso aunque haya lanzado una excepción.
        verify(fiadosRepository, times(1)).deleteById(1L);
    }
}
