package com.hbnet.fastsh.web.service.base;

import com.hbnet.fastsh.web.enums.OP;
import lombok.Data;

@Data
public class Predication<T> {

    private OP operator;
    private String name;
    private T value;

    private Predication() {
    }

    public static <T> Predication<T> get(OP operator, String name, T value) {
        return new Builder().operator(operator)
                .name(name).value(value).build();
    }

    public static class Builder<T> {
        private Predication p;

        public Builder() {
            this.p = new Predication();
        }

        public Builder operator(OP op) {
            this.p.operator = op;
            return this;
        }

        public Builder name(String name) {
            this.p.name = name;
            return this;
        }

        public Builder value(T value) {
            this.p.value = value;
            return this;
        }

        public <T> Predication<T> build() {
            return this.p;
        }

    }
}
