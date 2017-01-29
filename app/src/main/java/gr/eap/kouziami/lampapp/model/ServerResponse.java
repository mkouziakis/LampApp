package gr.eap.kouziami.lampapp.model;

/**
 * Represents a server response of an execution without network or other problems
 *
 * Created by KouziaMi on 22/11/2015.
 */
public class ServerResponse {
    public enum Result{
        Success,
        Failure
    }

    private Result result;
    private String errorMessage;

    public ServerResponse(String result){
        this.result = Result.valueOf(result);
    }

    public boolean isSuccess(){
        return Result.Success.equals(result);
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
