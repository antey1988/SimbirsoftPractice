package com.example.SimbirsoftPractice.rest.domain;

public enum StatusTask implements Verificable {
    BACKLOG,
    IN_PROGRESS,
    DONE;

    @Override
    public int getRank() {
        return ordinal();
    }

    @Override
    public StatusTask getEnum() {
        return this;
    }

    //возвращает номер проверки
    @Override
    public int getNumberVerification() {
        return ordinal() + 1;
    }
    //необходима дополнительная проверка или нет
    @Override
    public boolean isVerificable() {
        return getNumberVerification() != -1;
    }
}
