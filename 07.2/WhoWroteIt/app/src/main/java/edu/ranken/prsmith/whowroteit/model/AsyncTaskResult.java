package edu.ranken.prsmith.whowroteit.model;

public class AsyncTaskResult<Result> {
    public final Result result;
    public final Exception error;

    public AsyncTaskResult(Result result) {
        this.result = result;
        this.error = null;
    }

    public AsyncTaskResult(Exception error) {
        this.error = error;
        this.result = null;
    }
}
