package com.alkemy.disneylandia.disneylandia.repository.specification;

import com.alkemy.disneylandia.disneylandia.dto.PersonajeFiltersDto;
import com.alkemy.disneylandia.disneylandia.entity.PeliculaSerieEntity;
import com.alkemy.disneylandia.disneylandia.entity.PersonajeEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonajeSpecification {

    public Specification<PersonajeEntity> getByFilters(PersonajeFiltersDto filtersDto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasLength(filtersDto.getNombre())) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("nombre")),
                                "%" + filtersDto.getNombre().toLowerCase() + "%"
                        )
                );
            }
            if (filtersDto.getEdad() != null) {
                predicates.add(criteriaBuilder.equal(root.get("edad"), filtersDto.getEdad()));
            }
            if (filtersDto.getPeso() != null) {
                predicates.add(criteriaBuilder.equal(root.get("peso"), filtersDto.getPeso()));
            }
            if (!CollectionUtils.isEmpty(filtersDto.getPeliculasSeries())) {
                Join<PeliculaSerieEntity, PersonajeEntity> join = root.join("peliculasSeries", JoinType.INNER);
                Expression<String> peliculasSeriesId = join.get("id");
                predicates.add(peliculasSeriesId.in(filtersDto.getPeliculasSeries()));
            }
            query.distinct(true);
            String orderByField = "nombre";
            query.orderBy(
                    filtersDto.isAsc() ?
                            criteriaBuilder.asc(root.get((orderByField))) : criteriaBuilder.desc(root.get(orderByField))
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}