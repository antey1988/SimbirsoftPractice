package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.rest.dto.CustomerWithUUIDRequestDto;
import com.example.SimbirsoftPractice.rest.dto.PaymentProjectRequestDto;

public interface PaymentService {
    /**
     * Метод создания учетной записи клиента в платежном сервисе,
     * с расчетного счета которого в дальнейшем будет производиться списание денежных средств
     * @param customer
     */
    void createClient(CustomerWithUUIDRequestDto customer);

    /**
     * Метод списания денежных средств за работу по проекту
     * @param project
     */
    void payProject(PaymentProjectRequestDto project);
}
