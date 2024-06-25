package it.polimi.ingsw.core.controller;

import it.polimi.ingsw.core.model.message.request.MessageServer2Client;
import it.polimi.ingsw.network.socket.server.GameServer;
import it.polimi.ingsw.observers.GameObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;


public class HandlerTest implements GameObserver {

    @Override
    public void update(MessageServer2Client event) throws RemoteException {
        throw new RemoteException("error");
    }
}