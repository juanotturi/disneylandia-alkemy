package com.alkemy.disneylandia.disneylandia.entity;

import lombok.Setter;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "peliculaserie")
@Getter
@Setter
//@SQLDelete(sql = "UPDATE peliculaserie SET deleted = true WHERE id=?")
//@Where(clause = "deleted=false")
public class PeliculaSerieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String imagen;
    private String titulo;
//    private boolean deleted = Boolean.FALSE;

    @Column(name = "fecha_creacion")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate fechaCreacion;
    private Long calificacion;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "genero_id", insertable = false, updatable = false)
    private GeneroEntity genero;

    @Column(name = "genero_id", nullable = false)
    private Long generoId;

    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(
            name = "personaje_peliculaserie",
            joinColumns = @JoinColumn(name = "peliculaserie_id"),
            inverseJoinColumns = @JoinColumn(name = "personaje_id"))
    private Set<PersonajeEntity> personajes = new HashSet<>();

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final PeliculaSerieEntity other = (PeliculaSerieEntity) obj;
        return other.id == this.id;
    }

    public void addPersonaje(PersonajeEntity personaje) {
        this.personajes.add(personaje);
    }

    public void removePersonaje(PersonajeEntity personaje) {
        this.personajes.remove(personaje);
    }

    @PrePersist
    void persist() {
        setFechaCreacion(LocalDate.now());
    }
}