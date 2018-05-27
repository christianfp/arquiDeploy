package controller;

import view.View;

import java.util.ArrayList;
import java.util.List;

public class Observers implements IObservers {

    private List<View> observables;

    public Observers() {
        observables=new ArrayList<View>();
    }

    public void addObservable(View observable)
    {
        observables.add(observable);

    }

    public void updateObservers(String message)
    {
        for (View observer:observables) {
            observer.update(message);
        }
    }
}
