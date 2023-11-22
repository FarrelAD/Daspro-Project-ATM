
// Program sistem ATM - Kelompok 4 - TI-1B
// Fitur yang tersedia: autentifikasi pengguna, transfer, tarik tunai, setor tunai, pembayaran lain-lain, riwayat transaksi, cek saldo, ubah PIN, dan EXIT
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class Function000SistemATM {
    static Scanner scanner1 = new Scanner(System.in);
    static Scanner scanner2 = new Scanner(System.in);
    static Scanner scanner3 = new Scanner(System.in);
    static Scanner scanner4 = new Scanner(System.in);
    static Scanner scanner5 = new Scanner(System.in);
    static Scanner scannerTF = new Scanner(System.in);

    // inisialisasi dan deklarasi variabel yang dibutuhkan
    static String[][] accountData = {
            { "1234567", "1234", "7000000", "aman" },
            { "7654321", "5678", "4000000", "aman" },
            { "7777777", "7777", "10000000", "aman" },
            { "0000000", "0000", "900000000", "aman" }
    };
    static int maxTransactionHistory = 10, transactionCount = 10,
            accountLineIndex = 0, loginAttempts = 1; // variabel penunjang pencatatan riwayat transaksi dan
                                                     // counter percobaan login
    static String[] transactionHistory = new String[maxTransactionHistory]; // array riwayat transaksi
    static boolean isAccountValid = false,
            isTargetAccountValid = false; // variabel autentikasi login dan validasi nomor rekening
    static final int MAX_LOGIN_ATTEMPTS = 3; // maksimal login yang bisa dilakukan pengguna
    static boolean isTransactionExit = true;
    static String pressEnter;

    public static void main(String[] args) {
        // ASCII-UNICODE CHARACTER
        System.setProperty("file.encoding", "UTF-8");
        /*
         * char[] charASCII = {'\u26A0', '\u2714'};
         * String charWarning = new String(new char[] {charASCII[0]});
         * String charChecklist = new String(new char[] {charASCII[1]});
         */
        // String [] charASCII = {"\u26A0", "\u2714"};

        // Menghapus output yang telah ditampilkan
        System.out.println("\033[H\033[2J");
        System.out.flush();

        // Halaman utama sistem ATM
        System.out.println(
                "=====================================================================================================");
        System.out.println(
                "[  █████╗ ████████╗███╗   ███╗    ██████╗  ██████╗ ██╗     ██╗███╗   ██╗███████╗███╗   ███╗ █████╗  ]");
        System.out.println(
                "[ ██╔══██╗╚══██╔══╝████╗ ████║    ██╔══██╗██╔═══██╗██║     ██║████╗  ██║██╔════╝████╗ ████║██╔══██╗ ]");
        System.out.println(
                "[ ███████║   ██║   ██╔████╔██║    ██████╔╝██║   ██║██║     ██║██╔██╗ ██║█████╗  ██╔████╔██║███████║ ]");
        System.out.println(
                "[ ██╔══██║   ██║   ██║╚██╔╝██║    ██╔═══╝ ██║   ██║██║     ██║██║╚██╗██║██╔══╝  ██║╚██╔╝██║██╔══██║ ]");
        System.out.println(
                "[ ██║  ██║   ██║   ██║ ╚═╝ ██║    ██║     ╚██████╔╝███████╗██║██║ ╚████║███████╗██║ ╚═╝ ██║██║  ██║ ]");
        System.out.println(
                "[ ╚═╝  ╚═╝   ╚═╝   ╚═╝     ╚═╝    ╚═╝      ╚═════╝ ╚══════╝╚═╝╚═╝  ╚═══╝╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝ ]");
        System.out.println(
                "[ ================================================================================================= ]");
        System.out.println(
                "[                         _    _ _____ _     _____ ________  ___ _____                              ]");
        System.out.println(
                "[                        | |  | |  ___| |   /  __ \\  _  |  \\/  ||  ___|                             ]");
        System.out.println(
                "[                        | |  | | |__ | |   | /  \\/ | | | .  . || |__                               ]");
        System.out.println(
                "[                        | |/\\| |  __|| |   | |   | | | | |\\/| ||  __|                              ]");
        System.out.println(
                "[                        \\  /\\  / |___| |___| \\__/\\ \\_/ / |  | || |___                              ]");
        System.out.println(
                "[                         \\/  \\/\\____/\\_____/\\____/\\___/\\_|  |_/\\____/                              ]");
        System.out.println(
                "=====================================================================================================");
        System.out.println("");
        // Proses logi
        while (loginAttempts <= MAX_LOGIN_ATTEMPTS) {
            if (TryToLogin()) {
                System.out.println("Berhasil login");
            } else {
                LoginGoesWrong();
            }
        }
        Menu();
        Transfer();
        TarikTunai();
        SetorTunai();
        PembayaranLainnya();
        CekSaldo();
        UbahPin();
        Exit();
    }

    public static boolean TryToLogin() {
        System.out.println(
                "    ============================================================================================");
        System.out.print("    [  Masukkan nomor rekening : "); // Input nomor rekening
        String inputUser_AccountNumber = scanner1.nextLine();

        System.out.print("    [  Masukkan PIN anda : "); // Input PIN pengguna
        String inputPin = scanner1.nextLine();
        System.out.println(
                "    ============================================================================================");
        System.out.println("");

        // Menghapus output yang telah ditampilkan
        System.out.println("\033[H\033[2J");
        System.out.flush();

        // Pengecekan kesesuaian nomor rekening dan PIN untuk login
        for (int i = 0; i < accountData.length; i++) {
            if (inputUser_AccountNumber.equals(accountData[i][0]) && inputPin.equals(accountData[i][1])
                    && accountData[i][3].equals("aman")) {
                isAccountValid = true; // autentifikasi
                accountLineIndex = i; // session
            }
        }
        return isAccountValid;
    }

    public static void LoginGoesWrong() {
        if (loginAttempts < MAX_LOGIN_ATTEMPTS) {
            System.out.println(
                    "    ============================================================================================");
            System.out.println(
                    "    --------------------------------------------------------------------------------------------");
            System.out.println(
                    "               [  (!) Gagal login, periksa kembali nomor rekening dan PIN anda (!)  ]");
            System.out.println(
                    "    --------------------------------------------------------------------------------------------");
            System.out.println(
                    "    ============================================================================================");
            loginAttempts++;
            System.out.print("Enter untuk melanjutkan -->  ");
            pressEnter = scanner1.nextLine();

            // Menghapus output yang telah ditampilkan
            System.out.println("\033[H\033[2J");
            System.out.flush();
        } else {
            System.out.println(
                    "    ============================================================================================");
            System.out.println(
                    "    --------------------------------------------------------------------------------------------");
            System.out.println(
                    "               [  (!) Anda telah gagal lebih dari 3 kali. Akun anda diblokir. (!)  ]");
            System.out.println(
                    "    --------------------------------------------------------------------------------------------");
            System.out.println(
                    "    ============================================================================================");
            accountData[accountLineIndex][3] = "diblokir";
            loginAttempts++;
        }
    }

    public static void Menu() {

    }

    public static void Transfer() {

    }

    public static void TarikTunai() {

    }

    public static void SetorTunai() {

    }

    public static void PembayaranLainnya() {

    }

    public static void RiwayatTransaksi() {

    }

    public static void CekSaldo() {

    }

    public static void UbahPin() {

    }

    public static void Exit() {

    }

}
