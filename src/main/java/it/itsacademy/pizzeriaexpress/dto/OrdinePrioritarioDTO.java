package it.itsacademy.pizzeriaexpress.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class OrdinePrioritarioDTO extends OrdineDTO {
    private Double sovrapprezzo;
}
