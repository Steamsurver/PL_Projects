package EventManager;

//-------------------------------------------------------------------------------------------------------
public interface EventPublisher {//(ИЗДАТЕЛЬ)интерфейс, определяющий методы для добавления, удаления и оповещения наблюдателей;

    void registerObserver(EventObserver o); //регистрирует наблюдателя
    void removeObserver(EventObserver o);//удаляет наблюдателя

    void notifyObservers();//оповещение наблюдателя
}
