package org.alanc.mastermind.util;

import org.slf4j.Logger;

public final class ErrorHandler {

    public static String createMessage(String operation, String reason, String context, String userGuidance) {
        StringBuilder message = new StringBuilder("Failed to ").append(operation);

        if (reason != null && !reason.isEmpty()) {
            message.append(": ").append(reason);
        }

        if (context != null && !context.isEmpty()) {
            message.append(" (").append(context).append(")");
        }

        if (userGuidance != null && !userGuidance.isEmpty()) {
            message.append(". ").append(userGuidance);
        }

        return message.toString();
    }

    public static String createMessage(String operation, String reason) {
        return createMessage(operation, reason, null, null);
    }

    public static void handleInputValidationError(Logger logger, String userInput, String errorMessage) {
        String contextualMessage = createMessage(
                "validate user input",
                errorMessage,
                "input: '" + userInput + "'",
                "Please check your input format and try again"
        );
        logger.debug("Input validation failed: {}", contextualMessage);
        System.out.println(errorMessage); // User-friendly message only
    }

    public static void handleNetworkError(Logger logger, String service, Exception cause, boolean isRetryable) {
        String userGuidance = isRetryable
                ? "This is usually temporary - please retry again at a later time"
                : "Please check your internet connection or try again later";

        String message = createMessage(
                "connect to " + service,
                cause.getClass().getSimpleName() + ": " + cause.getMessage(),
                "external service communication",
                userGuidance
        );

        logger.error("Network error: {}", message, cause);
    }

    public static IllegalArgumentException invalidParameter(String parameterName, Object value, String constraint) {
        String message = createMessage(
                "set " + parameterName,
                "value '" + value + "' violates constraint: " + constraint,
                "configuration validation",
                "Please use a valid value within the allowed range"
        );
        return new IllegalArgumentException(message);
    }

    public static IllegalStateException invalidGameState(String operation, String currentState, String requiredState) {
        String message = createMessage(
                operation,
                "game is in '" + currentState + "' state",
                "required state: " + requiredState,
                "Please start a new game or check the current game status"
        );
        return new IllegalStateException(message);
    }

    // Private constructor to prevent instantiation
    private ErrorHandler() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
