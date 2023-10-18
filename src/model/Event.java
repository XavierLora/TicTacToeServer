package model;

public class Event {
    public enum EventStatus{
        PENDING,
        DECLINED,
        ACCEPTED,
        PLAYING,
        COMPLETED,
        ABORTED
    }
    private int eventId;
    private String sender;
    private String opponent;
    private EventStatus status;
    private String turn;
    private int move;

    public Event(){

    }
    public Event(int eventId, String sender, String opponent, EventStatus status, String turn, int move){
        this.eventId = eventId;
        this.sender = sender;
        this.opponent = opponent;
        this.status = status;
        this.turn = turn;
        this.move = move;
    }

    public int getEventId(){
        return eventId;
    }
    public String getSender(){
        return sender;
    }
    public String getOpponent(){
        return opponent;
    }
    public EventStatus getStatus(){
        return status;
    }
    public String getTurn(){
        return turn;
    }
    public int getMove(){
        return move;
    }

    public void setEventId(int eventId){
        this.eventId = eventId;
    }
    public void setSender(String sender){
        this.sender = sender;
    }
    public void setOpponent(String opponent){
        this.opponent = opponent;
    }
    public void setStatus(EventStatus status){
        this.status = status;
    }
    public void setTurn(String turn){
        this.turn = turn;
    }
    public void setMove(int move){
        this.move = move;
    }
    public boolean equals(Object o){
        if(this == o){
            return true;
        }else if(o==null || getClass() != o.getClass()){
            return false;
        }
        Event event = (Event) o;
        return eventId == event.eventId;
    }


}
