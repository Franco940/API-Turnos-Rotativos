package com.neorislab.tp2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true) // Hace que los setters devuelvan "this" en lugar de void
@Getter
@Setter
public class ErroresValidacionResponse {
    private List<String> error = new ArrayList<>();

    public void agregarError(String error) {
        this.error.add(error);
    }
}
