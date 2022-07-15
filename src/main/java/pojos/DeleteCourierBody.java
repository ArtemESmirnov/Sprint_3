package pojos;

public class DeleteCourierBody {
    private String id;

    public DeleteCourierBody(){
    }

    public DeleteCourierBody(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
