package cn.feng.aluminium.ui.music.api.bean.login;

import com.google.gson.JsonObject;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class LoginResult {
    private final LoginState state;
    private final JsonObject response;

    public LoginResult(LoginState state, JsonObject response) {
        this.state = state;
        this.response = response;
    }

    public LoginState getState() {
        return state;
    }

    public JsonObject getResponse() {
        return response;
    }
}
