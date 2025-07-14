package com.example.card_cost_api.representation;

import com.example.card_cost_api.domain.ClearingCost;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ClearingCostMapper {
    public abstract ClearingCostRepresentation toRepresentation(ClearingCost entity);

    @Mapping(target = "id", ignore = true)
    public abstract ClearingCost toModel(ClearingCostRepresentation representation);

    public abstract List<ClearingCostRepresentation> toRepresentationList(List<ClearingCost> list);
}
