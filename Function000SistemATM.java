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
            { "0000000", "0000", "900000000", "aman"}
        };
    static int maxTransactionHistory = 10,  transactionCount = 10, 
           accountLineIndex = 0, loginAttempts = 1;
    static String[] transactionHistory = new String[maxTransactionHistory];
    static boolean isAccountValid = false, 
            isTargetAccountValid = false;
    static final int MAX_LOGIN_ATTEMPTS = 3; 
    static boolean isTransactionExit = true;
    static String pressEnter;

    public static void main(String[] args) {
        PageMenu();
        Login();
        Menu();
    }

    public static void PageMenu() {
        ClearScreen();

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
    }

    public static  boolean Login() {
        while (loginAttempts <= MAX_LOGIN_ATTEMPTS) {
			System.out.println(
					"    ============================================================================================");
			System.out.print("    [  Masukkan nomor rekening : ");
			String inputUser_AccountNumber = scanner1.nextLine();

			System.out.print("    [  Masukkan PIN anda : ");
			String inputPin = scanner1.nextLine();
			System.out.println(
					"    ============================================================================================");
			System.out.println("");
			
            ClearScreen();

			// Pengecekan kesesuaian nomor rekening dan PIN untuk login
			for (int i = 0; i < accountData.length; i++) {
				if (inputUser_AccountNumber.equals(accountData[i][0]) && inputPin.equals(accountData[i][1])
						&& accountData[i][3].equals("aman")) {
					isAccountValid = true;
					accountLineIndex = i;
                    break;
				}
			}
            if (isAccountValid) {
                return true;
            } else {
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
                    
                    ClearScreen();
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
        } 
        return false;
    }

    public static void Menu() {
        String inputTarget_AccountNumber;
        char continueTransaction = 'y', userChoice = 't';
        int transferAmount, cashWithdrawalAmount, cashDepositAmount;
        int userBalance = Integer.parseInt(accountData[accountLineIndex][2]);

        // Format nilai uang Indonesia Rupiah (IDR)
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        // Perulangan menu berdasarkan continueTransaction user
        do {
            System.out.println(
                    "    ============================================================================================");
            System.out.println(
                    "    [  Silakan memilih menu dibawah ini :                                                      ]");
            System.out.println(
                    "    [   _________________                                                                      ]");
            System.out.println(
                    "    [  |_1._Transfer_____|                                                                     ]");
            System.out.println(
                    "    [   _________________                                                                      ]");
            System.out.println(
                    "    [  |_2._Tarik tunai__|                                                                     ]");
            System.out.println(
                    "    [   ________________                                                                       ]");
            System.out.println(
                    "    [  |_3._Setor tunai_|                                                                      ]");
            System.out.println(
                    "    [   __________________________                                                             ]");
            System.out.println(
                    "    [  |_4._Pembayaran lain-lain__|                                                            ]");
            System.out.println(
                    "    [   _______________________                                                                ]");
            System.out.println(
                    "    [  |_5._Riwayat transaksi__|                                                               ]");
            System.out.println(
                    "    [   ________________                                                                       ]");
            System.out.println(
                    "    [  |_6._Cek Saldo___|                                                                      ]");
            System.out.println(
                    "    [   ________________                                                                       ]");
            System.out.println(
                    "    [  |_7._Ubah PIN____|                                                                      ]");
            System.out.println(
                    "    [   ______________                                                                         ]");
            System.out.println(
                    "    [  |_8. Keluar____|                                                                        ]");
            System.out.println(
                    "    ============================================================================================");
            System.out.print("\tMenu yang dipilih (angka) : "); // User input pilihan menu berupa angka
            int menu = scanner2.nextInt();

            ClearScreen();
            switch (menu) {
                case 1:
                     Transfer();
                    break;
                case 2:
                    TarikTunai();
                    break;
                case 3:
                    SetorTunai();
                    break;
                case 4:
                    PembayaranLainnya();
                    break;
                case 5:
                    CekSaldo();
                    break;
                case 6:
                    UbahPin();
                    break;
                case 7:
                    Exit();
                default:
                    break;
            }
        } while (continueTransaction == 'y' || continueTransaction == 'Y');
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

    public static void ClearScreen() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

}
