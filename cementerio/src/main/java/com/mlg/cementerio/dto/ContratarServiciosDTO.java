package com.mlg.cementerio.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratarServiciosDTO {
    private Integer idDifunto;
    private List<Integer> idsCementerioServicios;
}