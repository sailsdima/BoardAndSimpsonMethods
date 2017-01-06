package Models;

/**
 * Created by sails on 12.12.2016.
 */

public class ResultInfo {

    boolean success = true;
    String errorMessage = "no errors";
    String countingMessage = "";
    String winner;

    public ResultInfo() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {

        success = false;
        this.errorMessage = errorMessage;
    }

    public String getCountingMessage() {
        return countingMessage;
    }

    public void setCountingMessage(String countingMessage) {
        this.countingMessage = countingMessage;
    }

    public void appendCountingMessage(String message){
        this.countingMessage += message;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void clearInfo() {
        this.success = true;
        this.winner = "";
        this.errorMessage = "";
        this.countingMessage = "";
    }
}
