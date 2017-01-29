package gr.eap.kouziami.lampapp.services;

/**
 * Subclass of RuntimeException so as to avoid
 * dealing with checked exceptions throughout the app
 *
 * Created by KouziaMi on 22/11/2015.
 */
public class DeviceServiceException extends RuntimeException {
    public DeviceServiceException(String detailMessage) {
        super(detailMessage);
    }

    public DeviceServiceException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
