package controller;

import view.View;

public interface IObservers {
    void addObservable(View observable);
    void updateObservers(String message);
}
