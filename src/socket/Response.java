package socket;

public class Response {
    public enum ResponseStatus{
        SUCCESS,
        FAILURE
    }

    private ResponseStatus status;
    private String message;

    public Response(){

    }
    public Response(ResponseStatus status, String message){
        this.status = status;
        this.message = message;
    }

    public ResponseStatus getStatus(){
        return status;
    }
    public void setStatus(ResponseStatus status){
        this.status = status;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }



}

