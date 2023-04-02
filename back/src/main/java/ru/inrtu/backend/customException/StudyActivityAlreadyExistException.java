package ru.inrtu.backend.customException;

public class StudyActivityAlreadyExistException extends RuntimeException{

    public StudyActivityAlreadyExistException(String exceptionReason){
        super(exceptionReason);
    }
}
