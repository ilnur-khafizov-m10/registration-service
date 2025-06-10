package com.b10.registration_service.core.fsm.state;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Состояния процесса регистрации")
public enum RegistrationState {

    @Schema(description = "Ожидание подписи документа о получении налоговых данных")
    WAIT_SIGNATURE_TAX,

    @Schema(description = "Данные из налоговой запрошены")
    TAX_DATA_REQUESTED,

    @Schema(description = "Ожидание ввода номера телефона")
    WAIT_PHONE,

//    @Schema(description = "Видеозвонок поставлен в очередь")
//    VIDEO_QUEUE,
//
//    @Schema(description = "Видеозвонок зарезервирован оператором")
//    VIDEO_RESERVED,

    @Schema(description = "Происходит видеозвонок")
    VIDEO_CALL,

//    @Schema(description = "Видеозвонок завершен (подтвержден/отклонен)")
//    VIDEO_FINISHED,
//
//    @Schema(description = "Ожидание подписи документов FATCA и др.")
//    WAIT_SIGNATURE_DOCS,
//
//    @Schema(description = "Данные Asan Finance запрошены")
//    ASAN_FINANCE_REQUESTED,

    @Schema(description = "Процесс регистрации полностью завершен")
    FINISHED
}
