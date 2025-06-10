package com.b10.registration_service.core.repository;

import com.b10.registration_service.core.entity.RegistrationProcessEntity;
import com.b10.registration_service.core.fsm.state.RegistrationState;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationProcessRepository extends CrudRepository<RegistrationProcessEntity, String> {

    /**
     * Найти процессы по business_id
     */
    List<RegistrationProcessEntity> findByBusinessId(String businessId);

    /**
     * Найти процессы по user_id
     */
    List<RegistrationProcessEntity> findByUserId(String userId);

    /**
     * Найти процессы по состоянию
     */
    List<RegistrationProcessEntity> findByCurrentState(RegistrationState currentState);

    /**
     * Найти процессы по business_id и user_id
     */
    List<RegistrationProcessEntity> findByBusinessIdAndUserId(String businessId, String userId);

    /**
     * Найти активные процессы (не в состоянии FINISHED)
     */
    @Query("SELECT * FROM registration_process WHERE current_state != 'FINISHED'")
    List<RegistrationProcessEntity> findActiveProcesses();

    /**
     * Найти процессы созданные после определенной даты
     */
    List<RegistrationProcessEntity> findByCreatedAtAfter(LocalDateTime createdAt);

    /**
     * Найти процессы обновленные после определенной даты
     */
    List<RegistrationProcessEntity> findByUpdatedAtAfter(LocalDateTime updatedAt);

    /**
     * Найти процессы по версии процесса
     */
    List<RegistrationProcessEntity> findByProcessVersion(String processVersion);

    /**
     * Найти последний процесс для бизнеса
     */
    @Query("SELECT * FROM registration_process WHERE business_id = :businessId ORDER BY created_at DESC LIMIT 1")
    Optional<RegistrationProcessEntity> findLatestByBusinessId(@Param("businessId") String businessId);

    /**
     * Посчитать процессы в определенном состоянии
     */
    long countByCurrentState(RegistrationState currentState);

    /**
     * Найти "висячие" процессы (не обновлялись долгое время)
     */
    @Query("SELECT * FROM registration_process WHERE updated_at < :threshold AND current_state != 'FINISHED'")
    List<RegistrationProcessEntity> findStaleProcesses(@Param("threshold") LocalDateTime threshold);

    /**
     * Поиск по переменной в JSON (PostgreSQL specific)
     */
    @Query("SELECT * FROM registration_process WHERE variables->>'operatorId' = :operatorId")
    List<RegistrationProcessEntity> findByOperatorId(@Param("operatorId") String operatorId);

    /**
     * Поиск процессов с определенной переменной
     */
    @Query("SELECT * FROM registration_process WHERE variables ? :variableName")
    List<RegistrationProcessEntity> findByVariableExists(@Param("variableName") String variableName);
}
