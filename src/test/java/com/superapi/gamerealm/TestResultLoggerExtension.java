package com.superapi.gamerealm;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestResultLoggerExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        if (context.getExecutionException().isPresent()) {
            System.out.println("Test failed: " + context.getDisplayName());
        } else {
            System.out.println("Test passed: " + context.getDisplayName());
        }
    }
}
