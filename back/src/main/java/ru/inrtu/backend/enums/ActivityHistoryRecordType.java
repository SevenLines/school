package ru.inrtu.backend.enums;

public enum ActivityHistoryRecordType {

    /**
     * Школьник зарегистрировался на мероприятие
     */
    REGISTERED("registered"),
    /**
     * Школьник проходит мероприятие
     */
    IN_PROGRESS("in progress"),
    /**
     * Школьник завершил мероприятие
     */
    ATTENDED("attended");

    private ActivityHistoryRecordType(String name){
        this.name = name;
    }

    private final String name;

    public String getName(){
        return name;
    }


}
