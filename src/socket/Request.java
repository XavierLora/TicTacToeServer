package socket;

public class Request {
    public enum RequestType{
        LOGIN,
        REGISTER,
        UPATE_PAIRING,
        SEND_INVITATION,
        ACCEPT_INVITATION,
        DECLINE_INVITATION,
        ACKNOWLEDGE_RESPONSE,
        REQUEST_MOVE,
        SEND_MOVE,
        ABORT_GAME,
        COMPLETE_GAME
    }
    private RequestType type;
    private String data;

    public Request(){
    }
    public Request(RequestType type, String data){
        this.type = type;
        this.data = data;
    }
    public RequestType getType(){
        return type;
    }
    public void setType(RequestType type){
        this.type = type;
    }
    public String getData(){
        return data;
    }
    public void setData(String data){
        this.data = data;
    }

}
