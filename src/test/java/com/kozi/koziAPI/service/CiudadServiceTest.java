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

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.kozi.koziAPI.model.Ciudad;
import com.kozi.koziAPI.model.Region;
import com.kozi.koziAPI.repository.CiudadRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class CiudadServiceTest {

    @Autowired
    private CiudadService ciudadService;

    @MockBean
    private CiudadRepository ciudadRepository;

    @MockBean
    private ComunaService comunaService;

    // Helpers 

    private Region createRegion(Long id, String nombre) {
        Region r = new Region();
        r.setId(id);
        r.setNombre(nombre);
        return r;
    }

    private Ciudad createCiudad(Long id, String nombre, Long regionId) {
        Ciudad c = new Ciudad();
        c.setId(id);
        c.setNombre(nombre);
        c.setRegion(createRegion(regionId, "Region " + regionId));
        return c;
    }

    // Tests

    @Test
    void findAll_returnsListOfCiudades() {
        when(ciudadRepository.findAll())
                .thenReturn(List.of(
                        createCiudad(1L, "Ciudad 1", 10L),
                        createCiudad(2L, "Ciudad 2", 20L)
                ));

        List<Ciudad> result = ciudadService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ciudadRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsCiudad() {
        Ciudad ciudad = createCiudad(1L, "Santiago", 7L);
        when(ciudadRepository.findById(1L)).thenReturn(Optional.of(ciudad));

        Ciudad result = ciudadService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Santiago", result.getNombre());
        assertEquals(7L, result.getRegion().getId());
        verify(ciudadRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(ciudadRepository.findById(99L)).thenReturn(Optional.empty());

        Ciudad result = ciudadService.findById(99L);

        assertNull(result);
        verify(ciudadRepository, times(1)).findById(99L);
    }

    @Test
    void save_persistsCiudad() {
        Ciudad nueva = createCiudad(null, "Nueva Ciudad", 5L);

        when(ciudadRepository.save(any(Ciudad.class)))
                .thenAnswer(invocation -> {
                    Ciudad c = invocation.getArgument(0);
                    c.setId(50L);
                    return c;
                });

        Ciudad result = ciudadService.save(nueva);

        assertNotNull(result.getId());
        assertEquals("Nueva Ciudad", result.getNombre());
        assertEquals(5L, result.getRegion().getId());
        verify(ciudadRepository, times(1)).save(any(Ciudad.class));
    }

    @Test
    void patchCiudad_updatesNombreWhenExists() {
        Ciudad existente = createCiudad(1L, "Nombre Viejo", 3L);
        Ciudad patch = new Ciudad();
        patch.setId(1L);
        patch.setNombre("Nombre Nuevo");

        when(ciudadRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(ciudadRepository.save(any(Ciudad.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Ciudad result = ciudadService.patchCiudad(patch);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Nombre Nuevo", result.getNombre());
        assertEquals(3L, result.getRegion().getId());
        verify(ciudadRepository, times(1)).findById(1L);
        verify(ciudadRepository, times(1)).save(existente);
    }

    @Test
    void patchCiudad_whenNotExists_returnsNull() {
        Ciudad patch = new Ciudad();
        patch.setId(99L);
        patch.setNombre("LoQueSea");

        when(ciudadRepository.findById(99L)).thenReturn(Optional.empty());

        Ciudad result = ciudadService.patchCiudad(patch);

        assertNull(result);
        verify(ciudadRepository, times(1)).findById(99L);
        verify(ciudadRepository, never()).save(any(Ciudad.class));
    }

    @Test
    void deleteById_callsCascadeAndRepository() {
        doNothing().when(comunaService).deleteByCiudadId(1L);
        doNothing().when(ciudadRepository).deleteById(1L);

        ciudadService.deleteById(1L);

        verify(comunaService, times(1)).deleteByCiudadId(1L);
        verify(ciudadRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByRegionId_deletesOnlyCiudadesOfThatRegion() {
        Ciudad c1 = createCiudad(1L, "Ciudad A", 10L);
        Ciudad c2 = createCiudad(2L, "Ciudad B", 20L); 
        Ciudad c3 = createCiudad(3L, "Ciudad C", 10L); 

        when(ciudadRepository.findAll())
                .thenReturn(List.of(c1, c2, c3));

        ciudadService.deleteByRegionId(10L);

        verify(ciudadRepository, times(1)).findAll();
        verify(ciudadRepository, times(1)).deleteById(1L);
        verify(ciudadRepository, times(1)).deleteById(3L);
        verify(ciudadRepository, never()).deleteById(2L);
    }
}
