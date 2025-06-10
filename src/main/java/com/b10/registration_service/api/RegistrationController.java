package com.b10.registration_service.api;


import com.b10.registration_service.core.fsm.RegistrationProcessService;
import com.b10.registration_service.core.fsm.state.RegistrationState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/registration")
@Tag(name = "Registration Process", description = "API для управления процессами регистрации бизнесов")
public class RegistrationController {

    @Autowired
    private RegistrationProcessService registrationProcessService;

    @Operation(
            summary = "Запуск нового процесса регистрации",
            description = """
            Создает и запускает новый процесс регистрации бизнеса.
            Процесс начинается с состояния WAIT_SIGNATURE_TAX.
            
            Возвращает уникальный идентификатор процесса, который используется 
            для дальнейшего взаимодействия с процессом.
            """,
            operationId = "startRegistration"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Процесс регистрации успешно запущен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StartRegistrationResponse.class),
                            examples = @ExampleObject(
                                    name = "Успешный запуск",
                                    summary = "Пример успешного ответа",
                                    value = """
                        {
                            "processId": "reg_business_123_user_456_a1b2c3d4",
                            "currentState": "WAIT_SIGNATURE_TAX",
                            "message": "Registration process started successfully"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка в параметрах запроса или при создании процесса",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StartRegistrationResponse.class),
                            examples = @ExampleObject(
                                    name = "Ошибка валидации",
                                    summary = "Пример ошибки при невалидных данных",
                                    value = """
                        {
                            "processId": null,
                            "currentState": null,
                            "message": "Failed to start registration process: Process version is required"
                        }
                        """
                            )
                    )
            )
    })
    @PostMapping("/start")
    public ResponseEntity<StartRegistrationResponse> startRegistration(
            @Parameter(
                    description = "Данные для запуска процесса регистрации",
                    required = true,
                    schema = @Schema(implementation = StartRegistrationRequest.class)
            )
            @Valid @RequestBody StartRegistrationRequest request) {

        try {
            String processId = registrationProcessService.startRegistrationProcess(
                    request.processVersion(),
                    request.businessId(),
                    request.userId()
            );

            RegistrationState currentState = registrationProcessService.getCurrentState(processId);

            return ResponseEntity.ok(new StartRegistrationResponse(
                    processId,
                    currentState,
                    "Registration process started successfully"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new StartRegistrationResponse(
                    null,
                    null,
                    "Failed to start registration process: " + e.getMessage()
            ));
        }
    }

    @Operation(
            summary = "Получение статуса процесса регистрации",
            description = """
            Возвращает текущее состояние процесса регистрации по его идентификатору.
            
            Возможные состояния:
            - WAIT_SIGNATURE_TAX: Ожидание подписи документа о получении налоговых данных
            - TAX_DATA_REQUESTED: Запрошены данные из налоговой
            - WAIT_PHONE: Ожидание ввода номера телефона
            - VIDEO_QUEUE: Звонок поставлен в очередь
            - VIDEO_RESERVED: Звонок зарезервирован оператором
            - VIDEO_CALL: Происходит видеозвонок
            - VIDEO_FINISHED: Видеозвонок завершен
            - WAIT_SIGNATURE_DOCS: Ожидание подписи документов
            - ASAN_FINANCE_REQUESTED: Запрошены данные Asan Finance
            - FINISHED: Процесс полностью завершен
            """,
            operationId = "getProcessStatus"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Статус процесса получен успешно",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StartRegistrationResponse.class),
                            examples = @ExampleObject(
                                    name = "Получение статуса",
                                    summary = "Пример успешного получения статуса",
                                    value = """
                        {
                            "processId": "reg_business_123_user_456_a1b2c3d4",
                            "currentState": "WAIT_PHONE",
                            "message": "Process status retrieved successfully"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Процесс не найден или ошибка при получении статуса",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StartRegistrationResponse.class),
                            examples = @ExampleObject(
                                    name = "Процесс не найден",
                                    summary = "Пример ошибки при несуществующем процессе",
                                    value = """
                        {
                            "processId": "invalid_process_id",
                            "currentState": null,
                            "message": "Failed to get process status: Process not found"
                        }
                        """
                            )
                    )
            )
    })
    @GetMapping("/status/{processId}")
    public ResponseEntity<StartRegistrationResponse> getProcessStatus(
            @Parameter(
                    description = "Уникальный идентификатор процесса регистрации",
                    required = true,
                    example = "reg_business_123_user_456_a1b2c3d4",
                    schema = @Schema(type = "string", pattern = "^reg_[a-zA-Z0-9_]+$")
            )
            @PathVariable String processId) {

        try {
            RegistrationState currentState = registrationProcessService.getCurrentState(processId);

            return ResponseEntity.ok(new StartRegistrationResponse(
                    processId,
                    currentState,
                    "Process status retrieved successfully"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new StartRegistrationResponse(
                    processId,
                    null,
                    "Failed to get process status: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/{processId}/event")
    @Operation(
            summary = "Отправка события в процесс",
            description = "Отправляет событие в указанный процесс регистрации для перехода в следующее состояние"
    )
    public ResponseEntity<SendEventResponse> sendEvent(
            @PathVariable String processId,
            @Valid @RequestBody SendEventRequest request) {

        try {
            RegistrationState previousState = registrationProcessService.getCurrentState(processId);

            RegistrationState newState = request.eventData() != null ?
                    registrationProcessService.sendEvent(processId, request.event(), request.eventData()) :
                    registrationProcessService.sendEvent(processId, request.event());

            return ResponseEntity.ok(new SendEventResponse(
                    processId,
                    previousState,
                    newState,
                    "Event processed successfully"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new SendEventResponse(
                    processId,
                    null,
                    null,
                    "Failed to process event: " + e.getMessage()
            ));
        }
    }
}