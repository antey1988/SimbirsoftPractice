package com.example.SimbirsoftPractice.rest.domain;

public enum StatusProject implements Verificable {
    CREATED,
    OPEN,
    CLOSED;

    @Override
    public int getRank() {
        return ordinal();
    }

    @Override
    public StatusProject getEnum() {
        return this;
    }

    //возвращает номер проверки
    @Override
    public int getNumberVerification() {
        return (this.ordinal() < CLOSED.ordinal() ? -1 : ordinal());
    }
    //необходима дополнительная проверка или нет
    @Override
    public boolean isVerificable() {
        return getNumberVerification() != -1;
    }



}
