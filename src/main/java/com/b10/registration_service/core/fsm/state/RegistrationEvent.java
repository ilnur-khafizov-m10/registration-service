package com.b10.registration_service.core.fsm.state;

public enum RegistrationEvent {
    SIGNED_TAX,            // Подписан документ "Соглашение на получение данных из налоговой"
    TAX_DATA_RECEIVED,     // Получены данные из налоговой
    PHONE_ENTERED,         // Введён номер телефона
    VIDEO_SET_TO_QUEUE,    // Звонок поставлен в очередь
    VIDEO_RESERVED_BY_OP,  // Оператор зарезервировал звонок
    VIDEO_CALLED,          // Видеозвонок проведён
    VIDEO_APPROVED,        // Видеозвонок успешно завершён (разрешение регистрации)
    VIDEO_REJECTED,        // Видеозвонок отклонен (отклонение регистрации)
    SIGNED_DOCS,           // Подписаны документы FATCA и др.
    ASAN_FINANCE_DATA_RECEIVED // Получены данные Asan Finance
}
