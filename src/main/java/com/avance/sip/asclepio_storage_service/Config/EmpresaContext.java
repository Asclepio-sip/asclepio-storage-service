package com.avance.sip.asclepio_storage_service.Config;

import org.springframework.stereotype.Component;

@Component
public class EmpresaContext {

    private static final ThreadLocal<Long> EMPRESA_ID = new ThreadLocal<>();

    public void setEmpresaId(Long empresaId) {
        EMPRESA_ID.set(empresaId);
    }

    public Long getEmpresaId() {
        Long empresaId = EMPRESA_ID.get();

        if (empresaId == null) {
            throw new IllegalStateException("Empresa não encontrada no token");
        }

        return empresaId;
    }

    public void limpar() {
        EMPRESA_ID.remove();
    }
}