public class AnimatedColorTextCLI {
    public static void main(String[] args) {
        String[] colors = {"\u001B[31m", "\u001B[32m", "\u001B[34m"};
        String resetColor = "\u001B[0m";

        while (true) {
            for (String color : colors) {
                System.out.print(color + "Animated Text" + resetColor);
                try {
                    Thread.sleep(1000); // Sleep for one second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("\r"); // Move the cursor back to the beginning of the line
            }
        }
    }
}
