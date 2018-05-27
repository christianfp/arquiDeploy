package controller;

public interface IState {
    void dial(String command);
    void hangup();
}
