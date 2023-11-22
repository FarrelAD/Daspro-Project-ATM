
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
        TryToLogin();
        Menu();
        Transfer();
        TarikTunai();
        SetorTunai();
        PembayaranLainnya();
        CekSaldo();
        UbahPin();
        Exit();
    }

    public static void TryToLogin() {

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
