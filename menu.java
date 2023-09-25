import java.util.Scanner;

public class menu {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Pilih menu :");
        System.out.println("1. Transfer");
        System.out.println("2. Tarik tunai");
        System.out.println("3. Setor tunai");
        System.out.println("4. Pembayaran lain-lain");
        System.out.println("Menu yang dipilih :");

        int menu = input.nextInt();

        System.out.println("**************************");

        switch (menu) {
            case 1:
                System.out.println("Anda memilih menu transfer");
                break;
            case 2:
                System.out.println("Anda memilih menu tarik tunai");
                break;
            case 3:
                System.out.println("Anda memilih menu setor tunai");
                break;
            case 4:
                System.out.println("Anda memilih menu pembayaran lain lain");
                break;
            default:
                System.out.println("Periksa kembali inputan anda");
                break;
        }
    }
}
