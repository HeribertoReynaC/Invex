package com.heriberto.invex.controllers;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heriberto.invex.entities.Empleado;
import com.heriberto.invex.entities.EmpleadoDTO;
import com.heriberto.invex.exceptions.IdNotFound;
import com.heriberto.invex.services.EmpleadoServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class EmpleadoControllerTest {

    @Mock
    private EmpleadoServiceImpl empleadoService;

    @InjectMocks
    private EmpleadoController empleadoController;

    private MockMvc mockMvc;

    private Empleado Empleado1;
    private Empleado Empleado2;
    private EmpleadoDTO empleadoDTO1;
    private EmpleadoDTO empleadoDTO2;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(empleadoController).build();

        Empleado1 = new Empleado(1L, "Heriberto","" ,"Reyna", "Castro", 26, "Masculino", LocalDate.of(1998,07,8) , "Desarrollador");
        Empleado2 = new Empleado(2L, "Ana","Christina" ,"Reyna", "Castro", 26, "Femenino", LocalDate.of(1993,04,3) , "Desarrollador");
        
        empleadoDTO1 = new EmpleadoDTO();
        empleadoDTO1.setPrimer_nombre("Heriberto");
        empleadoDTO1.setApellido_paterno("Reyna");
        empleadoDTO1.setApellido_materno("Castro");
        empleadoDTO1.setEdad(26);
        empleadoDTO1.setSexo("Masculino");
        empleadoDTO1.setFecha_nacimiento("08-07-1998");
        empleadoDTO1.setPuesto("Desarrollador");

        empleadoDTO2 = new EmpleadoDTO();
        empleadoDTO2.setPrimer_nombre("Ana");
        empleadoDTO2.setApellido_paterno("Reyna");
        empleadoDTO2.setApellido_materno("Castro");
        empleadoDTO2.setEdad(30);
        empleadoDTO2.setSexo("Femenino");
        empleadoDTO2.setFecha_nacimiento("03-04-1993");
    }

    @Test
    void testObtenerEmpleados_Success() throws Exception {
        // Arrange
        List<Empleado> empleados = Arrays.asList(Empleado1, Empleado2);
        when(empleadoService.findAll()).thenReturn(empleados);

        // Act & Assert
        mockMvc.perform(get("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].primer_nombre").value("Heriberto"))
                .andExpect(jsonPath("$[1].primer_nombre").value("Ana"));

        verify(empleadoService, times(1)).findAll();
    }

    @Test
    void testObtenerEmpleados_InternalServerError() throws Exception {
        // Arrange
        when(empleadoService.findAll()).thenThrow(new RuntimeException("Error en el servidor"));

        // Act & Assert
        mockMvc.perform(get("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(empleadoService, times(1)).findAll();
    }

    @Test
    void testObtenerEmpleadoPorId_Success() throws Exception {
        // Arrange
        Long id = 1L;
        when(empleadoService.findById(id)).thenReturn(Empleado1);

        // Act & Assert
        mockMvc.perform(get("/api/empleados/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.primer_nombre").value("Heriberto"))
                .andExpect(jsonPath("$.apellido_paterno").value("Reyna"));

        verify(empleadoService, times(1)).findById(id);
    }

    @Test
    void testObtenerEmpleadoPorId_NotFound() throws Exception {
        // Arrange
        Long id = 1L;
        when(empleadoService.findById(id)).thenThrow(new IdNotFound("Empleado no encontrado con id: " + id));

        // Act & Assert
        mockMvc.perform(get("/api//empleados/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(empleadoService, times(1)).findById(id);
    }

    @Test
    void testObtenerEmpleadoPorId_InternalServerError() throws Exception {
        // Arrange
        Long id = 1L;
        when(empleadoService.findById(id)).thenThrow(new RuntimeException("Error interno"));

        // Act & Assert
        mockMvc.perform(get("/api/empleados/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(empleadoService, times(1)).findById(id);
    }

    @Test
    void testEliminarEmpleado_Success() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(empleadoService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api/empleados/{id}", id)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent()); // 204 No Content

        verify(empleadoService, times(1)).delete(id);
    }

    @Test
    void testEliminarEmpleado_NotFound() throws Exception {
        // Arrange
        Long id = 1L;
        doThrow(new IdNotFound("Empleado no encontrado con id: " + id)).when(empleadoService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api/empleados/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // 404 Not Found

        verify(empleadoService, times(1)).delete(id);
    }

    @Test
    void testEliminarEmpleado_InternalServerError() throws Exception {
        // Arrange
        Long id = 1L;
        doThrow(new RuntimeException("Error interno")).when(empleadoService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api//empleados/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()); // 500 Internal Server Error

        verify(empleadoService, times(1)).delete(id);
    }


    @Test
    void testInsertarEmpleados_EmptyList() throws Exception {
        // Arrange
        List<EmpleadoDTO> empleadosDTOList = Arrays.asList(); // Lista vacía

        // Act & Assert
        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(empleadosDTOList)))
                .andExpect(status().isBadRequest()); // 400 Bad Request

        verify(empleadoService, never()).insertarEmpleados(anyList());
    }
  
}
