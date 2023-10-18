package server;

public class SocketServer {
    public static int PORT = 5000;
    public static void main(String[] args){
        SocketServer socketServer = new SocketServer();
        socketServer.setup();
        socketServer.startAcceptingRequest();
    }

    public SocketServer(){
        this(PORT);
    }
    public SocketServer(int PORT){
        SocketServer.PORT = PORT;
    }

    public void setup(){

    }
    public void startAcceptingRequest(){

    }
    public int getPort(){
        return PORT;
    }
}
