package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Envio;
import com.kozi.koziAPI.repository.EnvioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class EnvioService {
    
    @Autowired
    private EnvioRepository envioRepository;

    public List<Envio> findAll() {
        return envioRepository.findAll();
    }

    public Envio findById(Long id) {
        Envio envio = envioRepository.findById(id).orElse(null);
        return envio;
    }

    public Envio save(Envio envio) {
        return envioRepository.save(envio);
    }

    public Envio patchEnvio(Envio envio) {
        Envio existingEnvio = envioRepository.findById(envio.getId()).orElse(null);
        if (existingEnvio != null) {
            if (envio.getMetodoEnvio() != null) {
                existingEnvio.setMetodoEnvio(envio.getMetodoEnvio());
            }
            return envioRepository.save(existingEnvio);
        }
        return null;
    }

    public void deleteById(Long id) {
        envioRepository.deleteById(id);
    }
}
