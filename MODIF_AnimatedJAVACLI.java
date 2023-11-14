public class MODIF_AnimatedJAVACLI {
    public static void main(String[] args) {
        String[] colors1 = {"\u001B[31m", "\u001B[33m", "\u001B[32m", "\u001B[34m", "\u001B[35m"};
        String[] colors2 = {"\u001B[33m", "\u001B[32m", "\u001B[34m", "\u001B[35m", "\u001B[31m"};
        String[] colors3 = {"\u001B[32m", "\u001B[34m", "\u001B[35m", "\u001B[31m", "\u001B[33m"};
        String resetColor = "\u001B[0m";

        /*
        while (true) {
            for (String color : colors1) {
                System.out.print(color+"WOW"+resetColor);
                try {
                    Thread.sleep(1000); // Sleep for one second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("\r"); // Move the cursor back to the beginning of the line
            }

            for (String color : colors2) {
                System.out.print(color+"I'M A SUPERHERO"+resetColor);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("\r");
            }

            for (String color : colors3) {
                System.out.print(color+"LET'S GO TO THE MARS!!"+resetColor);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("\r");
            }
            
        }
        */
        /* System.out.println("Hey");
        Thread thread1 = new Thread(() -> {
            while (true) {
                for (String color : colors1) {
                    System.out.print(color + "WOW" + resetColor);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.print("\r");
                }
            }
        });
        System.out.println("Halo");
        Thread thread2 = new Thread(() -> {
            while (true) {
                for (String color : colors2) {
                    System.out.print(color+"I'M A SUPERHERO"+resetColor);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.print("\r");
                }
            }
        });
        System.out.println("Sawadikap");
        */
        
        /*
        Thread thread3 = new Thread(() -> {
            while (true) {
                for (String color : colors3) {
                    System.out.print(color+"LET'S GO TO THE MARS!!"+resetColor);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.print("\r");
                }
            }
        });
        System.out.print("\r"); */

        // thread1.start();
        // thread2.start();
        // thread3.start();
        Thread thread11 = new Thread (() -> {
            System.out.println("My name is ALEX");
            System.out.println("I like football");
        });
        Thread thread2 = new Thread (() -> {
            System.out.println("I'm Tina");
            System.out.println("I'm Alex's friend");
        });
        System.out.println("HEY ALEX!!!");
        thread11.start();
        System.out.println("");
        System.out.println("Hey Tina!");
        thread2.start();

    }
}
