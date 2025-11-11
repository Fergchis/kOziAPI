package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Pago;
import com.kozi.koziAPI.repository.PagoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class PagoService {
    
    @Autowired
    private PagoRepository pagoRepository;

    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    public Pago findById(Long id) {
        Pago pago = pagoRepository.findById(id).orElse(null);
        return pago;
    }

    public Pago save(Pago pago) {
        return pagoRepository.save(pago);
    }

    public Pago patchPago(Pago pago) {
        Pago existingPago = pagoRepository.findById(pago.getId()).orElse(null);
        if (existingPago != null) {
            if (pago.getTipoPago() != null) {
                existingPago.setTipoPago(pago.getTipoPago());
            }
            return pagoRepository.save(existingPago);
        }
        return null;
    }

    public void deleteById(Long id) {
        pagoRepository.deleteById(id);
    }
}
