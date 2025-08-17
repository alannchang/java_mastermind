package org.alanc.mastermind.ui;

public final class GameText {

    public enum Messages {
        STARTUP_BANNER("""
                ___  ___          _                      _           _\s
                |  \\/  |         | |                    (_)         | |
                | .  . | __ _ ___| |_ ___ _ __ _ __ ___  _ _ __   __| |
                | |\\/| |/ _` / __| __/ _ \\ '__| '_ ` _ \\| | '_ \\ / _` |
                | |  | | (_| \\__ \\ ||  __/ |  | | | | | | | | | | (_| |
                \\_|  |_/\\__,_|___/\\__\\___|_|  |_| |_| |_|_|_| |_|\\__,_|
                """),
        OPTIONS_BANNER("""
                 _____       _   _                \s
                |  _  |     | | (_)               \s
                | | | |_ __ | |_ _  ___  _ __  ___\s
                | | | | '_ \\| __| |/ _ \\| '_ \\/ __|
                \\ \\_/ / |_) | |_| | (_) | | | \\__ \\
                 \\___/| .__/ \\__|_|\\___/|_| |_|___/
                      | |                         \s
                      |_|                         \s
                """),
        ABOUT_BANNER("""
                  ___  _                 _  \s
                 / _ \\| |               | | \s
                / /_\\ \\ |__   ___  _   _| |_\s
                |  _  | '_ \\ / _ \\| | | | __|
                | | | | |_) | (_) | |_| | |_\s
                \\_| |_/_.__/ \\___/ \\__,_|\\__|
                """),
        YOU_WIN_BANNER("""
                __   _______ _   _   _    _ _____ _   _ _ _ _\s
                \\ \\ / /  _  | | | | | |  | |_   _| \\ | | | | |
                 \\ V /| | | | | | | | |  | | | | |  \\| | | | |
                  \\ / | | | | | | | | |/\\| | | | | . ` | | | |
                  | | \\ \\_/ / |_| | \\  /\\  /_| |_| |\\  |_|_|_|
                  \\_/  \\___/ \\___/   \\/  \\/ \\___/\\_| \\_(_|_|_)
                """),
        YOU_LOSE_BANNER("""
                __   _______ _   _   _     _____ _____ _____      __
                \\ \\ / /  _  | | | | | |   |  _  /  ___|  ___|  _ / /
                 \\ V /| | | | | | | | |   | | | \\ `--.| |__   (_) |\s
                  \\ / | | | | | | | | |   | | | |`--. \\  __|    | |\s
                  | | \\ \\_/ / |_| | | |___\\ \\_/ /\\__/ / |___   _| |\s
                  \\_/  \\___/ \\___/  \\_____/\\___/\\____/\\____/  (_) |\s
                                                                 \\_\\
                """),
        MAIN_MENU("""
                ******************************************
                
                1) START NEW GAME
                2) OPTIONS
                3) ABOUT
                4) QUIT
                
                ******************************************
                """),
        OPTIONS_MENU("""
                ******************************************
                
                1) CHANGE NUMBER OF ATTEMPTS
                2) CHANGE SECRET CODE LENGTH
                3) CHANGE SECRET CODE NUMBER RANGE
                4) CHECK RANDOM.ORG API QUOTA
                5) RESET TO DEFAULT SETTINGS
                6) RETURN TO MAIN MENU
                
                ******************************************
                """),
        ENDGAME_MENU("""
                ******************************************
                
                WOULD YOU LIKE TO PLAY AGAIN?
                
                1) PLAY AGAIN
                2) RETURN TO MAIN MENU
                
                ******************************************
                """),
        ABOUT_INFO("""
                ******************************************
                
                v1.0
                created by Alan Chang, for LinkedIn Reach
                Github: https://github.com/alannchang
                
                ******************************************
                """),
        QUIT_INFO("***Thanks for playing!***"),
        SEPARATOR("\n\n\n\n")
        ;

        private final String text;

        Messages(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    public static void printUI(Messages message) {
        System.out.println(message.getText());
    }

    private GameText() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
