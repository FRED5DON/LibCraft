package android.app.sagosoft.com.libcraft;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    private JSONObject json;

    @Test
    public void addition_isCorrect() throws Exception {
        json = JSON.parseObject("{\"id\":123}");

        Bundle bundle = new Bundle();
        bundle.putInt("id", 1234);
        assertEquals(bundle.getString("id"), "1234");
    }
}