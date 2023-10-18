package socket;

public class GamingResponse extends Response {
    private int move;
    private boolean active;

    public GamingResponse(){
        super();
    }
    public GamingResponse(ResponseStatus status, String message, int move, boolean active){
        super(status,message);
        this.move = move;
        this.active = active;
    }
    public int getMove(){
        return move;
    }
    public boolean getActive(){
        return active;
    }

    public void setMove(int move){
        this.move = move;
    }
    public void setActive(boolean active){
        this.active = active;
    }



}
