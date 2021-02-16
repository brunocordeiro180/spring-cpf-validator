package com.ingrupo.cpfvalidator.dto;

public class IdentificadorDto {

    private String tipo = "CPF";
    private String value;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
