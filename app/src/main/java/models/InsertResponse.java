package models;

public class InsertResponse {
    private boolean success;
    private int data;

    public InsertResponse() {
    }

    public InsertResponse(boolean success, int data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
