package com.avance.sip.asclepio_storage_service.Config;

import com.avance.sip.asclepio_storage_service.Categoria.CategoriaService;
import com.avance.sip.asclepio_storage_service.Categoria.dto.CategoriaRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CategoriaDataInitializer implements CommandLineRunner {

    private final CategoriaService categoriaService;

    public CategoriaDataInitializer(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Override
    public void run(String... args) {

        List<CategoriaRequest> categoriasPadrao = List.of(

                new CategoriaRequest("Medicamentos", "Produtos farmacêuticos", "pill", null),

                new CategoriaRequest("Beleza", "Produtos de beleza e cosméticos", "sparkles", null),

                new CategoriaRequest("Higiene", "Produtos de higiene pessoal", "soap", null),

                new CategoriaRequest("Infantil", "Produtos para bebês e crianças", "baby", null),

                new CategoriaRequest("Vitaminas", "Vitaminas e suplementos", "heart", null));

        categoriasPadrao.forEach(dto -> {

            try {

                categoriaService.criar(dto);

                System.out.println(" Categoria criada: " + dto.nomeCategoria());

            } catch (Exception e) {

                System.out.println("ℹ Categoria já existe: " + dto.nomeCategoria());
            }

        });
    }
}