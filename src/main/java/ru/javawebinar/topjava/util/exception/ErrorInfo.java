package ru.javawebinar.topjava.util.exception;

public class ErrorInfo {
    private final String url;
    private final String cause;
    private final String detail;
    private String localizedMessage;

    public ErrorInfo(CharSequence url, Throwable ex) {
        this.url = url.toString();
        this.cause = ex.getClass().getSimpleName();
        this.detail = ex.getLocalizedMessage();
    }

    public ErrorInfo(CharSequence url, Throwable ex, String localizedMessage) {
        this(url, ex);
        this.localizedMessage = localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
        this.localizedMessage = localizedMessage;
    }

}