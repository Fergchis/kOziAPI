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

import com.kozi.koziAPI.model.Region;
import com.kozi.koziAPI.repository.RegionRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class RegionServiceTest {

    @Autowired
    private RegionService regionService;

    @MockBean
    private RegionRepository regionRepository;

    @MockBean
    private CiudadService ciudadService;

    // Helpers

    private Region createRegion(Long id, String nombre) {
        Region r = new Region();
        r.setId(id);
        r.setNombre(nombre);
        return r;
    }

    // Tests

    @Test
    void findAll_returnsRegionList() {
        when(regionRepository.findAll())
                .thenReturn(List.of(
                        createRegion(1L, "Región 1"),
                        createRegion(2L, "Región 2")
                ));

        List<Region> result = regionService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Región 1", result.get(0).getNombre());
        verify(regionRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsRegion() {
        Region region = createRegion(1L, "Región 1");
        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));

        Region result = regionService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Región 1", result.getNombre());
        verify(regionRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(regionRepository.findById(99L)).thenReturn(Optional.empty());

        Region result = regionService.findById(99L);

        assertNull(result);
        verify(regionRepository, times(1)).findById(99L);
    }

    @Test
    void save_persistsRegionThroughRepository() {
        Region nueva = createRegion(null, "Nueva Región");

        when(regionRepository.save(any(Region.class)))
                .thenAnswer(invocation -> {
                    Region r = invocation.getArgument(0);
                    r.setId(10L);
                    return r;
                });

        Region saved = regionService.save(nueva);

        assertNotNull(saved.getId());
        assertEquals("Nueva Región", saved.getNombre());
        verify(regionRepository, times(1)).save(any(Region.class));
    }

    @Test
    void patchRegion_updatesNombreWhenExists() {
        Region existing = createRegion(1L, "Viejo Nombre");

        when(regionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(regionRepository.save(any(Region.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Region patch = new Region();
        patch.setId(1L);
        patch.setNombre("Nuevo Nombre");

        Region result = regionService.patchRegion(patch);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Nuevo Nombre", result.getNombre());
        verify(regionRepository, times(1)).findById(1L);
        verify(regionRepository, times(1)).save(any(Region.class));
    }

    @Test
    void patchRegion_whenNotFound_returnsNull() {
        when(regionRepository.findById(99L)).thenReturn(Optional.empty());

        Region patch = new Region();
        patch.setId(99L);
        patch.setNombre("Irrelevante");

        Region result = regionService.patchRegion(patch);

        assertNull(result);
        verify(regionRepository, times(1)).findById(99L);
        verify(regionRepository, never()).save(any(Region.class));
    }

    @Test
    void deleteById_callsCiudadServiceAndRepository() {
        doNothing().when(ciudadService).deleteByRegionId(1L);
        doNothing().when(regionRepository).deleteById(1L);

        regionService.deleteById(1L);

        verify(ciudadService, times(1)).deleteByRegionId(1L);
        verify(regionRepository, times(1)).deleteById(1L);
    }
}
