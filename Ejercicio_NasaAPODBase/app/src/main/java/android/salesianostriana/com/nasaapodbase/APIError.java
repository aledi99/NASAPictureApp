package android.salesianostriana.com.nasaapodbase;

import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class APIError {

    private String textError;
    private String urlError;

    public String getTextError() {
        textError = "API ERROR";
        return textError;
    }

    public String getUrlError() {
        urlError = "https://media.giphy.com/media/3osxY9kuM2NGUfvThe/giphy.gif";
        return urlError;
    }
}
