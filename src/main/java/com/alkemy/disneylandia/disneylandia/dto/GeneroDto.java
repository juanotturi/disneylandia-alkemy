package com.alkemy.disneylandia.disneylandia.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GeneroDto {
    private Long id;
    private String nombre;
    private String imagen;
    private List<PeliculaSerieDto> peliculasSeries;
}