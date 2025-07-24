package com.hubsi.authmicroservice.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Component
public class GlobalAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    /**
     * Método que lida com exceções não capturadas em métodos assíncronos.
     * Registra o erro no log com o nome do método e a exceção.
     *
     * @param ex     A exceção não capturada
     * @param method O método onde a exceção ocorreu
     * @param params Parâmetros do método (se houver)
     */
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("[ASYNC-ERROR] method: {} exception: {}", method.getName(), ex);
    }
}