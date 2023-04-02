package ru.inrtu.backend.service;

/**
 * Сервис для реализации безопасности
 */
public interface SecurityService {

    String findLoggedInUserName();

    void autoLogin(String userName, String password);
}
