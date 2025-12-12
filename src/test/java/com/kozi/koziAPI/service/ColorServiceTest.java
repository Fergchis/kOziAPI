package com.kozi.koziAPI.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.kozi.koziAPI.model.Color;
import com.kozi.koziAPI.repository.ColorRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class ColorServiceTest {

    @Autowired
    private ColorService colorService;

    @MockBean
    private ColorRepository colorRepository;

    @MockBean
    private ColoresService coloresService; 

    // Helpers

    private Color createColor(Long id, String nombre) {
        Color c = new Color();
        c.setId(id);
        c.setNombre(nombre);
        return c;
    }

    // Tests

    @Test
    void findAll_returnsListOfColors() {
        when(colorRepository.findAll())
                .thenReturn(List.of(
                        createColor(1L, "Rojo"),
                        createColor(2L, "Negro")
                ));

        List<Color> result = colorService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(colorRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsColor() {
        Color color = createColor(1L, "Azul");
        when(colorRepository.findById(1L)).thenReturn(Optional.of(color));

        Color result = colorService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Azul", result.getNombre());
        verify(colorRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(colorRepository.findById(99L)).thenReturn(Optional.empty());

        Color result = colorService.findById(99L);

        assertNull(result);
        verify(colorRepository, times(1)).findById(99L);
    }

    @Test
    void save_persistsColor() {
        Color nuevo = createColor(null, "Verde");

        when(colorRepository.save(any(Color.class)))
                .thenAnswer(invocation -> {
                    Color c = invocation.getArgument(0);
                    c.setId(10L);
                    return c;
                });

        Color result = colorService.save(nuevo);

        assertNotNull(result.getId());
        assertEquals("Verde", result.getNombre());
        verify(colorRepository, times(1)).save(any(Color.class));
    }

    @Test
    void patchColor_updatesFieldsCorrectly() {
        Color existente = createColor(1L, "ViejoColor");
        Color patch = createColor(1L, "NuevoColor");

        when(colorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(colorRepository.save(any(Color.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Color result = colorService.patchColor(patch);

        assertNotNull(result);
        assertEquals("NuevoColor", result.getNombre());
        verify(colorRepository, times(1)).findById(1L);
        verify(colorRepository, times(1)).save(existente);
    }

    @Test
    void patchColor_whenNotExists_returnsNull() {
        Color patch = createColor(99L, "Cualquiera");

        when(colorRepository.findById(99L)).thenReturn(Optional.empty());

        Color result = colorService.patchColor(patch);

        assertNull(result);
        verify(colorRepository, times(1)).findById(99L);
        verify(colorRepository, never()).save(any(Color.class));
    }

    @Test
    void deleteById_callsDeleteAndCascade() {
        doNothing().when(coloresService).deleteByColorId(1L);
        doNothing().when(colorRepository).deleteById(1L);

        colorService.deleteById(1L);

        verify(coloresService, times(1)).deleteByColorId(1L);
        verify(colorRepository, times(1)).deleteById(1L);
    }
}
