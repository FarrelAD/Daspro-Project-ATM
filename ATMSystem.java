// Program sistem ATM - Kelompok 4 - TI-1B
// Fitur yang tersedia: autentifikasi pengguna, transfer, tarik tunai, setor tunai, 
// pembayaran lain-lain, riwayat transaksi, cek saldo, ubah PIN, dan EXIT

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;
import java.util.Scanner;

public class ATMSystem {
	static Scanner scanner1 = new Scanner(System.in);
	static Scanner scanner2 = new Scanner(System.in);
	static Scanner scanner3 = new Scanner(System.in);
	static Scanner scanner4 = new Scanner(System.in);
	static Scanner scanner5 = new Scanner(System.in);
	static Scanner scannerTF = new Scanner(System.in);

	private static final String red = "\u001B[31m";
	private static final String green = "\u001B[32m";
	private static final String reset = "\u001B[0m";

	// inisialisasi dan deklarasi variabel yang dibutuhkan
	// array akun di ATM POLINEMA
	static String[][] accountData = {
			{ "1234567", "1234", "ATABIK", "BANK POLINEMA", "7000000", "SECURE", "04-09-2023" },
			{ "7654321", "5678", "FARREL", "BANK JOSS", "4000000", "SECURE", "14-11-2023" },
			{ "7777777", "7777", "INNAMA", "RICH BANK", "10000000", "SECURE", "01-01-2001" },
			{ "0101010", "0001", "KARL", "BANK POLINEMA", "900000000", "SECURE", "05-05-2005" },
			{ "1122334", "4321", "PUTRI", "BANK POLINEMA", "23000000", "SECURE", "17-12-2023"},
			{ "1231231", "1122", "AGUS", "BANK HOKI", "12000000", "SECURE", "14-02-2019"},
			{ "1", "1", "RIO", "BANK JOSS", "10000000", "SECURE", "29-10-2010" }, // for quick try
			{ "2", "2", "TAYLOR", "RICH BANK", "22222222", "SECURE", "29-02-2020" } // for quick try
	};

	// array listrikData menampung ID PLN & tagihan
	static int[][] listrikData = {
			{ 123123123, 100000, 1 },
			{ 123456789, 70000, 1 },
			{ 333444555, 80000, 1 },
	};

	// array pendidikanData menampung virtual account (VA) & tagihan
	static int[][] pendidikanData = {
			{ 232323, 1000000, 1 },
			{ 454545, 2500000, 1 },
			{ 909090, 5000000, 1 },
	};

	// array tagihan pada pdam menampung virtual account (VA) & tagihan
	static int[][] tagihanAirData = {
			{ 232323, 100000, 1 },
			{ 454545, 250000, 1 },
			{ 909090, 500000, 1 },
	};

	// array tagihan pada BPJS menampung virtual accounr (VA) & tagihan
	static int[][] BPJSdata = {
			{ 232323, 125000, 1 },
			{ 454545, 250000, 1 },
			{ 909090, 500000, 1 },
	};

	// 'Login' feature variables
	static String inputUser_AccountNumber;
	static int accountLineIndex = 0, loginAttempts = 1;
	static boolean isAccountValid = false, isAccountNumberValid = false,
			isAccountFind = false;
	static final int MAX_LOGIN_ATTEMPTS = 3;

	static int userChoiceMenu = 0;

	// 'Transfer' feature variables
	static int transferAmount;
	static String inputTarget_AccountNumber;
	static boolean isTargetAccountValid = false;

	// 'Tarik tunai' variables
	static int cashWithdrawalAmount;
	static boolean isCashWithdrawalValid = false;

	// Limitations the amount of transactions
	static final int MAX_AMOUNT_TRANSACTION = 5000000;

	// Limitations the amount of transactions
	static final int MIN_AMOUNT_TRANSACTION = 50000;

	// 'Setor tunai' variables
	static int cashDepositAmount;

	// 'Cek saldo' variables
	static int userBalance = Integer.parseInt(accountData[accountLineIndex][4]);

	// 'Riwayat transaksi' features variables
	static ArrayList<ArrayList<String>> transactionHistoryList = new ArrayList<>();
	static ArrayList<ArrayList<String>> accountHistoryList = new ArrayList<>();

	// 'Exit' feature variables
	static boolean isStopTransaction = false;

	// Format nilai uang Indonesia Rupiah (IDR)
	static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

	// 'Payment' feature variable
	static int adminFee = 1000;
	static String adminFeeRp = currencyFormat.format(adminFee);

	// 'Listrik' feature variables
	static int indexListrik = 0;
	static boolean isListrikValid = false;

	// 'Pendidikan' feature variables
	static int indexPendidikan = 0;
	static boolean isPendidikanValid = false;

	// 'PDAM' feature variables
	static int indexPdam = 0;
	static boolean isPdamValid = false;

	// 'BPJS' feature variables
	static int indexBpjs = 0;
	static boolean isBpjsValid = false;

	// 'Return to main menu' feature variables
	static boolean isGoToMainMenu = false;

	// Konfirmasi transaksi ulang features variables
	static char continueTransaction = 't', userChoice = 't',
			userConfirmChar;
	static boolean isContinueTransaction = false;

	// 'Validasi PIN' variables
	static String inputPin;

	// Variables for choose languange feature
	static int currentLanguange = 0;
	static String[][] langOutputs = {
		{ "  PLEASE SELECT A MENU BELOW   ", "SILAKAN PILIH MENU DI BAWAH INI" }, // 0
		{ "TRANSFER", "TRANSFER" }, // 1
		{ "CASH WITHDRAWAL", "TARIK TUNAI    " }, // 2
		{ "CASH DEPOSIT", "SETOR TUNAI " }, // 3
		{ "PAYMENTS  ", "PEMBAYARAN" }, // 4
		{ "HISTORY", "RIWAYAT" }, // 5
		{ "BALANCE INQUIRY", "CEK SALDO      " }, // 6
		{ "CHANGE PIN", "UBAH PIN  " }, // 7
		{ "EXIT  ", "KELUAR" }, // 8
		{ "HELP", "BANTUAN" }, // 9
		{ "ANOTHER ACTION?", "TINDAKAN LAIN? " }, // 10
		{ "YES", "YA " }, // 11
		{ "NO   ", "TIDAK" }, // 12
		{ "   INVALID INPUT. OPTION NOT AVAILABLE   ", "INPUT TIDAK VALID. PILIHAN TIDAK TERSEDIA" }, // 13
		{ " PLEASE INPUT CORRECTLY ", "SILAKAN INPUT YANG BENAR" }, // 14
		{ "SELECT TRANSFER DESTINATION", "   PILIH TUJUAN TRANSFER   "}, // 15 
		{ "OTHER BANK  ", "BANK LAINNYA"}, // 16 
		{ "INPUT THE DESTINATION ACCOUNT NUMBER : ", "MASUKKAN NOMOR REKENING TUJUAN : " }, // 17
		{ "INPUT THE TRANSFER AMOUNT : Rp ", "MASUKKAN NOMINAL TRANSFER : Rp " }, // 18
		{ "DETAIL TRANSACTION", "RINCIAN TRANSAKSI " }, // 19 
		{ "MAKE SURE THE FOLLOWING DATA IS CORRECT", "     PASTIKAN DATA BERIKUT SESUAI      "}, //20
		{ "DESTINATION ACCOUNT    : ", "REKENING TUJUAN        : " }, // 21 
		{ "NAME                   : ", "NAMA                   : " }, // 22 
		{ "BANK                   : ", "BANK                   : " }, // 23 
		{ "TRANSFER AMOUNT        : ", "NOMINAL TRANSFER       : " }, // 24
		{ "ADMIN FEE              : ", "BIAYA ADMIN            : " }, // 25
		{ "YOUR REMAINING BALANCE : ", "SISA SALDO ANDA        : " }, // 26
		{ " [!]  FAILED TRANSACTION. INVALID DESTINATION ACCOUNT  [!] ",
		  "[!]  TRANSAKSI GAGAL. NOMOR REKENING TUJUAN TIDAK VALID [!]" }, // 27
		{ "SELECT THE NOMINAL AMOUNT", "  PILIH JUMLAH NOMINAL   "}, // 28
		{ "OTHER AMOUNT   ", "NOMINAL LAINNYA"}, //29
		{"INPUT CASH WITHDRAWAL AMOUNT : Rp","MASUKKAN NOMINAL TARIK TUNAI : Rp"}, //30
		{"CONFIRM CASH WITHDRAWAL : " ,"KONFIRMASI TARIK TUNAI : " },//31
		{"INPUT CASH DEPOSIT AMOUNT : Rp ", "MASUKKAN NOMINAL SETOR TUNAI : Rp "}, //32
		{ "CONFIRM CASH DEPOSIT : ", "KONFIRMASI SETOR TUNAI : "}, // 33
		{"[===================================================================================================]\n"+
		 "[                              CHOOSE THE PAYMENT TRANSACTION BELOW                                 ]\n"+
		 "[===================================================================================================]\n"+
		 "[                       [1] CREDIT                              [4] PDAM /                          ]\n"+
		 "[                                                                   WATER BILL                      ]\n"+
		 "[                                                                                                   ]\n"+
		 "[                       [2] PLN /                               [5] BPJS /                          ]\n"+
		 "[                           ELECTRIC BILL                           HEALTH INSURANCE                ]\n"+
		 "[                                                                                                   ]\n"+
		 "[                       [3] EDUCATION                           [6] BACK TO THE                     ]\n"+
		 "[                                                                   MAIN MENU                       ]\n"+
		 "[                                                                                                   ]\n"+
		 "[===================================================================================================]",
	     "[===================================================================================================]\n"+
		 "[                            SILAKAN PILIH TRANSAKSI PEMBAYARAN BERIKUT                             ]\n"+
	     "[===================================================================================================]\n"+
		 "[                       [1] PULSA                              [4] PDAM /                           ]\n"+
		 "[                                                                  TAGIHAN AIR                      ]\n"+
		 "[                                                                                                   ]\n"+
         "[                       [2] PLN /                              [5] BPJS /                           ]\n"+
         "[                           TAGIHAN LISTRIK                        ASURANSI KESEHATAN               ]\n"+
		 "[                                                                                                   ]\n"+
		 "[                       [3] PENDIDIKAN                         [6] KEMBALI KE                       ]\n"+
		 "[                                                                  MENU UTAMA                       ]\n"+
		 "[===================================================================================================]"}, //34
		{ "CHOOSE AN OPERATOR CELLULER", "  PILIH OPERATOR SELULER   " }, //35
		{ "THE OPERATOR YOU SELECTED IS INVALID", "OPERATOR YANG ANDA PILIH TIDAK VALID" }, //36
		{ "INPUT PHONE NUMBER : ", "MASUKKAN NOMOR TELEPON : "}, // 37
		{ "INPUT CREDIT AMOUNT : Rp", "MASUKKAN NOMINAL PULSA : Rp"}, //38
		{ "CELLULER PROVIDER      : ", "OPERATOR SELULER       : "}, // 39 
		{ "PHONE NUMBER           : ", "NOMOR TELEPON          : "}, // 40
		{ "CREDIT AMOUNT          : ", "NOMINAL PULSA          : "}, // 41
		{ "INPUT PAYMENT CODE : ", "MASUKKAN KODE PEMBAYARAN : "}, // 42
		{ "PAYMENT CODE           : ", "KODE PEMBAYARAN        : "}, //43
		{ "AMOUNT OF BILL         : ", "JUMLAH TAGIHAN         : "}, // 44
		{ "TRANSACTION HISTORY", "RIWAYAT TRANSAKSI  "}, // 45
		{ "ACCOUNT HISTORY", "RIWAYAT AKUN   "}, // 46
		{ "YOUR RECENT TRANSACTION HISTORY", "RIWAYAT TRANSAKSI TERBARU ANDA "}, // 47
		{ " AMOUNT", "NOMINAL"}, // 48
		{ "TIME ", "WAKTU"}, // 49
		{ " DATE  ", "TANGGAL"}, //50
		{ "TRANSFER TO ACCOUNT ", "TRANSFER KE REKENING "}, // 51
		{ "CASH WITHDRAWAL                    ", "TARIK TUNAI                        " }, // 52
		{ "CASH DEPOSIT                       ", "SETOR TUNAI                        " }, //53
		{ "BUY CREDIT TO NUMBER ", "BELI PULSA KE NOMOR "}, //54
		{ "PAY ELECTRICITY BILL ", "BAYAR TAGIHAN LISTRIK "}, // 55
		{ "PAY EDUCATION COST ", "BAYAR BIAYA PENDIDIKAN "}, // 56
		{ "PAY WATER BILL ", "BAYAR TAGIHAN AIR "}, //57
		{ "PAY HEALTH INSURANCE ", "BAYAR ASURANSI KESEHATAN "}, //58
		{ "YOUR RECENT ACCOUNT HISTORY", " RIWAYAT AKUN TERBARU ANDA "}, //59
		{ "ACCOUNT CREATED", "AKUN DIBUAT    "}, //60
		{ "HAVE MADE A PIN CHANGE", "TELAH MELAKUKAN PERUBAHAN PIN"}, //61
		{ "YOUR BALANCE AMOUNT : ","SALDO ANDA SEBESAR  : "}, //62
		{ "ARE YOU SURE WANT TO EXIT?     ", "APAKAH ANDA YAKIN UNTUK KELUAR?" }, // 63
		{ "[                                ADD THE BANK CODE AT THE BEGINNING                                 ]\n"+
		  "[                                  WHEN INPUT THE ACCOUNT NUMBER                                    ]\n"+
		  "[                          -----------------------------------------------                          ]\n"+
		  "[                            INTERBANK TRANSFER WILL INCUR AN ADMIN FEE                             ]\n"+
		  "[                                    OF Rp1.500 PER TRANSACTION                                     ]\n", 
		  "[                                TAMBAHKAN KODE BANK PADA SAAT AWAL                                 ]\n"+
		  "[                                    MEMASUKKAN NOMOR REKENING                                      ]\n"+
		  "[                          -----------------------------------------------                          ]\n"+
		  "[                           TRANSFER ANTARBANK AKAN DIKENAKAN BIAYA ADMIN                           ]\n"+
		  "[                                  SEBESAR Rp1.500 TIAP TRANSAKSI                                   ]\n" }, // 64
		{ "VIEW BANK CODE ", "LIHAT CODE BANK"}, // 65
		{ "CONTINUE ", "LANJUTKAN"}, // 66
		{ "BACK   ", "KEMBALI"}, // 67
		{ "[                               INPUT THE DESTINATION ACCOUNT NUMBER                                ]\n"+
		  "[                                   STARTING WITH THE BANK CODE                                     ]\n",
		  "[                                  MASUKKAN NOMOR REKENING TUJUAN                                   ]\n"+
		  "[                                     DENGAN DIAWALI KODE BANK                                      ]\n" }, // 68
		{ "[                                   PLEASE INPUT THE SEVEN DIGIT                                    ]\n"+
		  "[                                    DESTINATION ACCOUNT NUMBER                                     ]\n", 
		  "[                                   SILAKAN MASUKKAN TUJUH DIGIT                                    ]\n"+
		  "[                                       NOMOR REKENING TUJUAN                                       ]\n"}, // 69
		{ " "," " }, // 70
		{ "INSERT YOUR PIN : ","MASUKKAN PIN ANDA : "}, // 71
		{ "INSERT A NEW PIN: ", "MASUKKAN PIN BARU: "}, // 72
		{ "CONFIRM A NEW PIN: ","KONFIRMASI PIN BARU: "}, // 73
		{ "    [!]  PIN CHANGED  [!]     ", "[!]  PIN BERHASIL DIRUBAH  [!]" }, // 74
		{ "[!]  A NEW PIN AND CONFIRM MUST BE THE SAME  [!]", "  [!]  PIN BARU DAN KONFIRMASI TIDAK SAMA  [!]  " }, // 75
		{ " [!]  INCORRECT PIN  [!]  ", "[!]  PIN TIDAK SESUAI  [!]" }, // 76
		{ "CONFIRM TRANSACTION ? ", "KONFIRMASI TRANSAKSI ?" }, // 77
		{ "[===================================================================================================]\n"+
		  "[                                    SORRY, INVALID TRANSACTION                                     ]\n"+
		  "[                      TRANSAKSI VALID MUST BE BETWEEN Rp50.000 - Rp5.000.000                       ]\n"+
		  "[===================================================================================================]",
		  "[===================================================================================================]\n"+
		  "[                                 MOHON MAAF, TRANSAKSI TIDAK VALID                                 ]\n"+
		  "[                     TRANSAKSI VALID JIKA NOMINAL Rp50.000 HINGGA Rp5.000.000                      ]\n"+
		  "[===================================================================================================]" }, // 78
		{ "[!]  TRANSACTION FAILED. YOUR BALANCE IS INSUFFICIENT  [!]", "  [!]  TRANSAKSI GAGAL. SALDO ANDA TIDAK MENCUKUPI  [!]   " }, // 79
		{ "[!]  WRONG PIN  [!]", "[!]  PIN SALAH [!] " }, // 80
		{ "[~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ TRANSACTION SUCCESS ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~]\n", 
		  "[~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ TRANSAKSI BERHASIL~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~]\n" }, // 81
		{ "[!]  CANCELED TRANSACTION  [!]", "[!]  TRANSAKSI DIBATALKAN  [!]" }, // 82
		{ "[                             [!]       INVALID PAYMENT CODE       [!]                              ]\n"+
		  "[                                 PLEASE RECHECK THE PAYMENT CODE                                   ]\n", 
		  "[                           [!]      KODE PEMBAYARAN TIDAK VALID      [!]                           ]\n"+
		  "[                                SILAKAN INPUT ULANG KODE PEMBAYARAN                                ]\n" }, // 83 
		{ "[                                 [!]  INVALID PAYMENT CODE  [!]                                    ]\n"+
		  "[                                       CODE HAS BEEN USED                                          ]\n",
		  "[                           [!]      KODE PEMBAYARAN TIDAK VALID      [!]                           ]\n"+
		  "[                                  KODE PEMBAYARAN TELAH DIGUNAKAN                                  ]\n" }, //84
		{ "Press 'Y' For yes. press 'T' for No --> ","Tekan 'Y' untuk YA. Tekan 'T' untuk TIDAK --> " }, //85
		{ "INSERT PIN : ","MASUKKAN PIN : " }, //86
		{ "ENTER FOR CONTINUE ==> ","ENTER UNTUK MELANJUTKAN ==> " }, //87
		{ "BACK TO THE", "KEMBALI KE "}, // 88
		{ "MAIN MENU ", "MENU UTAMA" }, // 89
		{ "[~ ~ ~ ~ ~ ~ ~ ~ ~ ~ THANK YOU FOR TRANSACTING! MAY YOUR DAY ALWAYS BE HAPPY :) ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ]\n", 
		  "[~ ~ ~ ~ ~ ~ ~ ~ ~TERIMA KASIH TELAH BERTRANSAKSI! SEMOGA HARIMU SELALU BAHAGIA :) ~ ~ ~ ~ ~ ~ ~ ~ ~]\n" }, // 90
		{ "[===================================================================================================]\n"+
		  "[                                INSTRUCTIONS FOR USING POLINEMA ATM                                ]\n"+
		  "[                                                                                                   ]\n"+
		  "[  Q: HOW TO MAKE A TRANSACTION?                                                                    ]\n"+
		  "[  A: YOU CAN SELECT TRANSACTION OPTIONS BY SELECTING THE NUMBER OPTIONS IN THE MENU                ]\n"+
		  "[                                                                                                   ]\n"+
		  "[  Q: CAN I TRANSACTION MORE THAN ONE TIME?                                                         ]\n"+
		  "[  A: OF COURSE. AFTER YOU CONFIRM, THERE WILL BE A NOTIFICATION REGARDING THE TRANSACTION          ]\n"+
		  "[     CONFIRMATION AGAIN. YOU CAN SELECT THE MENU THAT IS BACK AVAILABLE.                           ]\n"+
		  "[                                                                                                   ]\n"+
		  "[  IF THERE ARE OTHER PROBLEMS PLEASE CONTACT CALL CENTER 00112233                                  ]\n"+
		  "[  WE WILL BE HAPPY TO SERVE YOU                                                                    ]\n"+
		  "[===================================================================================================]",
		  "[===================================================================================================]\n"+
		  "[                                PETUNJUK MENGGUNAKAN ATM POLINEMA                                  ]\n"+
		  "[                                                                                                   ]\n"+
		  "[  Q: BAGAIMANA CARA MELAKUKAN TRANSAKSI?                                                           ]\n"+
		  "[  A: ANDA BISA MEMILIH OPSI TRANSAKSI DENGAN MEMILIH OPSI ANGKA YANG ADA DI MENU                   ]\n"+
		  "[                                                                                                   ]\n"+
		  "[  Q: APAKAH SAYA BISA TRANSAKSI LEBIH DARI 1 KALI?                                                 ]\n"+
		  "[  A: BISA. SETELAH ANDA MELAKUKAN KONFIRMASI, AKAN ADA PEMBERITAHUAN MENGENAI KONFIRMASI           ]\n"+
		  "[     TRANSAKSI LAGI. ANDA BISA MEMILIH MENU YANG TERSEDIA KEMBALI.                                 ]\n"+
		  "[                                                                                                   ]\n"+
		  "[  JIKA ADA KENDALA LAIN SILAKAN HUBUNGI CALL CENTER 00112233                                       ]\n"+
		  "[  KAMI DENGAN SENANG HATI AKAN MELAYANI ANDA                                                       ]\n"+
		  "[===================================================================================================]" }, // 91
		  { "[!] PIN ONLY CONSIST OF 4 DIGIT NUMBERS [!] ", "[!] PIN HANYA TERDIRI DARI 4 DIGIT ANGKA [!]" }, // 92
		  { "PIN IS STILL THE SAME AS BEFORE ", "PIN MASIH SAMA DENGAN SEBELUMNYA" } // 93
	};

	public static void main(String[] args) {
		while (!isAccountValid) {
			PageMenu();
			Login();
		}

		chooseLanguange();
		Menu();
	}

	public static void PageMenu() {
		ClearScreen();
		System.out.println(
			"[===================================================================================================]\n"+
			"[     " + formattedLocalDate() + "                                " + formattedLocalDay()+ "                                 " + formattedLocalTime() + "     ]\n" +
			"[===================================================================================================]\n"+
			"[  █████╗ ████████╗███╗   ███╗    ██████╗  ██████╗ ██╗     ██╗███╗   ██╗███████╗███╗   ███╗ █████╗  ]\n"+
			"[ ██╔══██╗╚══██╔══╝████╗ ████║    ██╔══██╗██╔═══██╗██║     ██║████╗  ██║██╔════╝████╗ ████║██╔══██╗ ]\n"+
			"[ ███████║   ██║   ██╔████╔██║    ██████╔╝██║   ██║██║     ██║██╔██╗ ██║█████╗  ██╔████╔██║███████║ ]\n"+
			"[ ██╔══██║   ██║   ██║╚██╔╝██║    ██╔═══╝ ██║   ██║██║     ██║██║╚██╗██║██╔══╝  ██║╚██╔╝██║██╔══██║ ]\n"+
			"[ ██║  ██║   ██║   ██║ ╚═╝ ██║    ██║     ╚██████╔╝███████╗██║██║ ╚████║███████╗██║ ╚═╝ ██║██║  ██║ ]\n"+
			"[ ╚═╝  ╚═╝   ╚═╝   ╚═╝     ╚═╝    ╚═╝      ╚═════╝ ╚══════╝╚═╝╚═╝  ╚═══╝╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝ ]\n"+
			"[===================================================================================================]\n"+
			"[                                                                                                   ]\n"+
			"[                    .@@@@.                       _    _ _____ _     _____ ________  ___ _____      ]\n"+
			"[                @:=:@@@@@@:=:@                  | |  | |  ___| |   /  __ \\  _  |  \\/  ||  ___|     ]\n"+
			"[             @:-:@@@@====@@@@:-:@               | |  | | |__ | |   | /  \\/ | | | .  . || |__       ]\n"+
			"[          @:-@@@::::::::::::::@@@-:@            | |/\\| |  __|| |   | |   | | | | |\\/| ||  __|      ]\n"+
			"[        @:-@@::@@@@@@@@@@@@@@@@::@@-:@          \\  /\\  / |___| |___| \\__/\\ \\_/ / |  | || |___      ]\n"+
			"[      @:-@@::@@@@@ ******** @@@@@::@@-:@         \\/  \\/\\____/\\_____/\\____/\\___/\\_|  |_/\\____/      ]\n"+
			"[    @:-@@@:@@@@: ***++**++*** :@@@:@@@-:@                                                          ]\n"+
			"[   @:-@@::@@@: ***++- !! -++*** :@@@::@@-:@                                                        ]\n"+
			"[   @:@@::@@@@ ***++- .||. -++*** @@@@::@@:@            PLEASE INPUT YOUR ACCOUNT NUMBER            ]\n"+
			"[   @:@@@@@@@@ **+++- .||. -+++** @@@@@@@@:@                      AND YOUR PIN                      ]\n"+
			"[   @:#@@@@: ***+++=- .||. -=+++*** :@@@@#:@             ________________________________           ]\n"+
			"[   @:+@@@@ ****++--. +||+ .--++**** @@@@+:@             SILAKAN MASUKKAN NOMOR REKENING            ]\n"+
			"[   :@:@@@@. ***-++.. +--+ ..++-*** .@@@@:@:                      DAN PIN ANDA                      ]\n"+
			"[    @:*@@@@@@ ***** *#::#* ***** @@@@@@*:@                                                         ]\n"+
			"[    @:-@@@@@@ ****  ======  **** @@@@@@-:@                                                         ]\n"+
			"[     @:-@@@@@. ***####%%####*** .@@@@@-:@                     FOR SAFETY AND COMFORT               ]\n"+
			"[      @:.@@@@@ ################ @@@@@.:@                PLEASE CHANGE YOUR PIN REGULARLY           ]\n"+
			"[       @: @@@@@@+:.  ----  .:+@@@@@@ :@                 ________________________________           ]\n"+
			"[         @:* @@@ =@@@@@@@@@@@= @@@ :@                     DEMI KEAMANAN DAN KENYAMANAN             ]\n"+
			"[          @*  :*@@@@@@@@@@@@@@*:  *@                         SILAKAN GANTI PIN ANDA                ]\n"+
			"[              @@@#+-:....:-+#@@@                                 SECARA BERKALA                    ]\n"+
			"[                                                                                                   ]\n"+
			"[===================================================================================================]");
	}

	public static boolean Login() {
		loginAttempts = 0;
		boolean tryToLogin = false;

		do {
			System.out.print("[  ACCOUNT NUMBER: ");
			inputUser_AccountNumber = scanner2.nextLine();

			// Pengecekan kesesuaian input nomor rekening dengan data yang ada
			isAccountFind = false;
			for (int i = 0; i < accountData.length; i++) {
				if (inputUser_AccountNumber.equals(accountData[i][0])) {
					isAccountFind = true;
					accountLineIndex = i;
					break;
				}
			}

			if (!isAccountFind) {
				System.out.println(
					"[===================================================================================================]\n"+
					"[               ACCOUNT NUMBER IS NOT FOUND. PLEASE INPUT YOUR CORRECT ACCOUNT NUMBER               ]\n"+
					"[         [!]   _____________________________________________________________________   [!]         ]\n"+
					"[              NOMOR REKENING TIDAK DITEMUKAN. MOHON MASUKKAN NOMOR REKENING YANG BENAR             ]\n"+
					"[===================================================================================================]\n");
				EnterForContinue();
				ClearScreen();
				return false;
			}

			isAccountNumberValid = false;
			if (isAccountFind) {
				if (accountData[accountLineIndex][5].equals("SECURE")) {
					isAccountNumberValid = true;
				}
			}

			if (isAccountNumberValid) {
				// Check the PIN with a maximum of MAX_LOGIN_ATTEMPTS attempts
				for (loginAttempts = 1; loginAttempts <= MAX_LOGIN_ATTEMPTS; loginAttempts++) {
					// Check if the entered PIN matches the account PIN
					if (PinValidation()) {
						isAccountValid = true;
						return true; // Successful login
					} else {
						ClearScreen();
						WrongPin();
						System.out.println(
							"[===================================================================================================]\n"+
							"[           LOGIN ATTEMPTS                          [=====]     /   [=====]                         ]\n"+
							"[           ______________                          [  " + loginAttempts+ "  ]    /    [  " + MAX_LOGIN_ATTEMPTS + "  ]                         ]\n" +
							"[           PERCOBAAN MASUK                         [=====]   /     [=====]                         ]\n"+
							"[===================================================================================================]");
						EnterForContinue();
					}
				}
			} else {
				tryToLogin = true;
				ClearScreen();
				System.out.println(
					"[===================================================================================================]\n"+
					"[           YOUR ACCOUNT (" + inputUser_AccountNumber+ ") HAS BEEN BLOCKED. PLEASE INPUT ANOTHER ACCOUNT NUMBER            ]\n" +
					"[  [!] _______________________________________________________________________________________ [!]  ]\n"+
					"[      NOMOR REKENING ANDA (" + inputUser_AccountNumber+ ") TELAH DIBLOKIR. SILAKAN MASUKKAN NOMOR REKENING YANG LAIN      ]\n" +
					"[===================================================================================================]");
				EnterForContinue();
				return false;
			}

			if (loginAttempts > MAX_LOGIN_ATTEMPTS) {
				System.out.println(
					"[===================================================================================================]\n"+
					"[          YOU HAVE INPUT YOUR PIN INCORRECTLY 3 TIMES. SORRY, WE HAVE BLOCKED YOUR ACCOUNT         ]\n"+
					"[    [!]   ________________________________________________________________________________   [!]   ]\n"+
					"[   ANDA TELAH SALAH MEMASUKKAN PIN SEBANYAK 3 KALI. MOHON MAAF, NOMOR REKENING ANDA KAMI BLOKIR    ]\n"+
					"[===================================================================================================]");
				accountData[accountLineIndex][5] = "BLOCKED";
				EnterForContinue();
			}
		} while (tryToLogin || !isAccountFind);
		return false;
	}

	public static void WrongPin() {
		System.out.println(
			"[===================================================================================================]\n"+
			"[                             LOGIN FAILED. PLEASE CHECK YOUR PIN AGAIN                             ]\n"+
			"[                       [!]   _________________________________________   [!]                       ]\n"+
			"[                             GAGAL MASUK. SILAKAN CEK PIN ANDA KEMBALI                             ]\n"+
			"[===================================================================================================]");
	}

	public static void chooseLanguange() {
		boolean isLoopConfirm = false;
		do {
			System.out.println(
				"[===================================================================================================]\n"+
				"[                               PLEASE SELECT THE LANGUAGE TO BE USED                               ]\n"+
				"[                               ______________________________________                              ]\n"+
				"[                               MOHON PILIH BAHASA YANG AKAN DIGUNAKAN                              ]\n"+
				"[===================================================================================================]\n"+
				"[                                                                                                   ]\n"+
				"[  [1] [ENGLISH]                                                                                    ]\n"+
				"[                                                                                                   ]\n"+
				"[  [2] [BAHASA INDONESIA]                                                                           ]\n"+
				"[                                                                                                   ]\n"+
				"[===================================================================================================]");
			System.out.print("[  ==> ");
			currentLanguange = scanner1.nextInt();
			if (currentLanguange >= 3) {
				isLoopConfirm = true;
				currentLanguange = 0;
				defaultCaseMenu();
				EnterForContinue();
			} else {
				currentLanguange -= 1;
				isLoopConfirm = false;
			}
		} while (isLoopConfirm);
	}

	public static void Menu() {
		do {
			isGoToMainMenu = false;
			ClearScreen();
			System.out.println(
				"[===================================================================================================]\n"+
				"[                                    " + langOutputs[0][currentLanguange]+ "                                ]\n" +
				"[===================================================================================================]\n"+
				"[                                                                                                   ]\n"+
				"[                            [1] " + langOutputs[1][currentLanguange]+ "                  " + "[5] " + langOutputs[5][currentLanguange]+ "                              ]\n" +
				"[                                                                                                   ]\n"+
				"[                            [2] " + langOutputs[2][currentLanguange] + "           "+ "[6] " + langOutputs[6][currentLanguange] + "                      ]\n" +
				"[                                                                                                   ]\n"+
				"[                            [3] " + langOutputs[3][currentLanguange] + "              "+ "[7] " + langOutputs[7][currentLanguange] + "                           ]\n" +
				"[                                                                                                   ]\n"+
				"[                            [4] " + langOutputs[4][currentLanguange] + "                "+ "[8] " + langOutputs[8][currentLanguange] + "                               ]\n" +
				"[                                                                                                   ]\n"+
				"[                                                          [9] "+ langOutputs[9][currentLanguange] + "      	                    ]\n" +
				"[                                                                                                   ]\n"+
				"[===================================================================================================]");
			System.out.print("[  ==> ");
			userChoiceMenu = scanner2.nextInt();

			ClearScreen();
			switch (userChoiceMenu) {
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
					History();
					break;
				case 6:
					CekSaldo();
					break;
				case 7:
					UbahPin();
					break;
				case 8:
					Exit();
					break;
				case 9:
					displayHelp();
					break;
				default:
					defaultCaseMenu();
					break;
			}

			if (isStopTransaction) {
				isContinueTransaction = false;
			}

			if (userChoiceMenu != 8) {
				if (!isGoToMainMenu) {
					boolean isLoopConfirm = false;
					do {
					System.out.println(
						"[===================================================================================================]\n"+
						"[  " + langOutputs[10][currentLanguange]+ "                                                                                  ]\n"+
						"[                                                                                                   ]\n"+
						"[  [1] " + langOutputs[11][currentLanguange]+ "                                                                                          ]\n"+
						"[                                                                                                   ]\n"+
						"[  [2] " + langOutputs[12][currentLanguange]+ "                                                                                        ]\n"+
						"[                                                                                                   ]\n"+
						"[===================================================================================================]");
						System.out.print("[  ==> ");
						continueTransaction = scanner2.next().charAt(0);
						ClearScreen();
						switch (continueTransaction) {
							case '1':
								isContinueTransaction = true;
								isLoopConfirm = false;
								break;
							case '2':
								displayClosing();
								isContinueTransaction = false;
								isLoopConfirm = false;
								break;
							default:
								defaultCaseMenu();
								EnterForContinue();
								isLoopConfirm = true;
								break;
						}
					} while (isLoopConfirm);
				}
			}
		} while (isContinueTransaction);
	}

	public static void displayHeaderTransfer() {
		System.out.println(
			"[===================================================================================================]\n"+
			"[ - - - - - - - - - - - - - - - - - - -╔╦╗╦═╗╔═╗╔╗╔╔═╗╔═╗╔═╗╦═╗- - - - - - - - - - - - - - - - - - -]\n"+
			"[ - - - - - - - - - - - - - - - - - - - ║ ╠╦╝╠═╣║║║╚═╗╠╣ ║╣ ╠╦╝- - - - - - - - - - - - - - - - - - -]\n"+
			"[ - - - - - - - - - - - - - - - - - - - ╩ ╩╚═╩ ╩╝╚╝╚═╝╚  ╚═╝╩╚═- - - - - - - - - - - - - - - - - - -]\n"+
			"[===================================================================================================]");
	}

	public static void Transfer() {
		displayHeaderTransfer();
		System.out.println(
			"[===================================================================================================]\n"+
			"[                                    "+langOutputs[15][currentLanguange]+"                                    ]\n"+
			"[===================================================================================================]\n"+
			"[                    [1] BANK POLINEMA                          [3] "+langOutputs[88][currentLanguange]+"                     ]\n"+
			"[                                                                   "+langOutputs[89][currentLanguange]+"                      ]\n"+
			"[                    [2] "+langOutputs[16][currentLanguange]+"                                                               ]\n"+
			"[===================================================================================================]");

		System.out.print("[  ==> ");
		int bankChoice = scanner1.nextInt();
		ClearScreen();

		switch (bankChoice) {
			case 1:
				transferSameBank();	
				EnterForContinue();
				break;
			case 2:
				transferOtherBank();
				EnterForContinue();
				break;
			case 3: 
				returnToMainMenu();
				break;
			default:
				defaultCaseMenu();
		}
	}

	public static void transferSameBank() {
		System.out.println(
			"[===================================================================================================]\n"+
			langOutputs[69][currentLanguange]+
			"[===================================================================================================]"
		);
		System.out.print("[  " + langOutputs[17][currentLanguange]);
		inputTarget_AccountNumber = scannerTF.nextLine();

		int indexTargetAccount = accountLineIndex;
		isTargetAccountValid = false;
		boolean isTryToCheckBank = false;

		for (int i = 0; i < accountData.length; i++) {
			if ((inputTarget_AccountNumber.equals(accountData[i][0])) && (!inputTarget_AccountNumber.equals(inputUser_AccountNumber))) {
				indexTargetAccount = i;
				break;
			}
		}

		if (indexTargetAccount == accountLineIndex) {
			isTryToCheckBank = false;
		} else {
			isTryToCheckBank = true;
		}

		if (isTryToCheckBank) {
			if (accountData[indexTargetAccount][0].equals(inputTarget_AccountNumber)) {
				if (accountData[indexTargetAccount][3].equals("BANK POLINEMA")) {
					isTargetAccountValid = true;
				}
			}
		}

		if (isTargetAccountValid) {
			int adminFeeTf = 0;
			String adminFeeTfRp = currencyFormat.format(adminFeeTf);

			transferAmount = validateNonNegativeIntegerInput("[  " + langOutputs[18][currentLanguange]);
			String transferAmountRupiah = currencyFormat.format(transferAmount);
			int totalTransfer = transferAmount + adminFeeTf;
			String totalTransferRp = currencyFormat.format(totalTransfer);
			ClearScreen();

			System.out.println(
				"[===================================================================================================]\n"+
				"[                                        " + langOutputs[19][currentLanguange]+ "                                         ]\n" +
				"[                              " + langOutputs[20][currentLanguange]+ "                              ]\n" +
				"[                              _______________________________________                              ]\n"+
				"[  -- " + langOutputs[21][currentLanguange] + inputTarget_AccountNumber + "\n" +
				"[  -- " + langOutputs[22][currentLanguange] + accountData[indexTargetAccount][2] + "\n"+
				"[  -- " + langOutputs[23][currentLanguange] + accountData[indexTargetAccount][3] + "\n"+
				"[  -- " + langOutputs[24][currentLanguange] + transferAmountRupiah + "\n" +
				"[  -- " + langOutputs[25][currentLanguange] + adminFeeTfRp + "\n" +
				"[===================================================================================================]\n"+
				"[                                      "+langOutputs[77][currentLanguange]+"                                       ]"
			);

			if (UserConfirmation()) {
				if (PinValidation()) {
					if (transferAmount < userBalance) {
						if (transferAmount <= MAX_AMOUNT_TRANSACTION && transferAmount >= MIN_AMOUNT_TRANSACTION) {
							userBalance -= totalTransfer;

							String userBalanceRupiah = currencyFormat.format(userBalance);
							viewTransactionSuccess();

							System.out.println(
								"[===================================================================================================]\n"+
								"[                                        "+ langOutputs[19][currentLanguange]+ "                                         ]\n" +
								"[                                      ______________________                                       ]\n"+
								"[  -- " + langOutputs[21][currentLanguange] + inputTarget_AccountNumber+ "\n" +
								"[  -- " + langOutputs[22][currentLanguange]+ accountData[indexTargetAccount][2] + "\n" +
								"[  -- " + langOutputs[23][currentLanguange]+ accountData[indexTargetAccount][3] + "\n" +
								"[  -- " + langOutputs[24][currentLanguange] + transferAmountRupiah+ "\n" +
								"[  -- " + langOutputs[25][currentLanguange] + adminFeeTfRp + "\n" +
								"[  -- " + langOutputs[26][currentLanguange] + userBalanceRupiah + "\n"+
								"[===================================================================================================]"
							);	

							// Recording transaction history
							transactionHistoryList.add(new ArrayList<>(List.of(langOutputs[51][currentLanguange] + inputTarget_AccountNumber + " ("+ accountData[indexTargetAccount][2] + ")", adjustNumCharHistory(totalTransferRp), formattedLocalTime(), formattedLocalDate())));
							recordTransactionHistory();
						} else {
							displayTransactionOverLimit();
						}
					} else {
						viewBalanceIsNotEnough();
					}
				} else {
					viewWrongPin();
				}
			} else {
				viewTransactionCancelled();
			}
		} else {
			ClearScreen();
			System.out.println(
				"[===================================================================================================]\n"+
				"[                    " + red + langOutputs[27][currentLanguange] + reset+ "                    ]\n" +
				"[===================================================================================================]"
			);
		}
	}

	public static void transferOtherBank() {
		Scanner scanTfOtherBank = new Scanner(System.in);
		System.out.println(
			"[===================================================================================================]\n"+
			langOutputs[64][currentLanguange]+
			"[===================================================================================================]\n"+
			"[         [1] "+langOutputs[65][currentLanguange]+"                                                                       ]\n"+
			"[                                                                                                   ]\n"+
			"[         [2] "+langOutputs[66][currentLanguange]+"                                                                             ]\n"+
			"[===================================================================================================]"
		);
		System.out.print("[  ==> ");
		userChoiceMenu = scanTfOtherBank.nextInt();
		ClearScreen();
		switch (userChoiceMenu) {
			case 1:
				displayBankCode();
				break;
			case 2:
				exeTransferOtherBank();
				break;
			default:
				defaultCaseMenu();
		}
	}

	public static void displayBankCode() {
		System.out.print(
			"[===================================================================================================]\n"+
			"[   001 - BANK JOSS                                                                                 ]\n"+
			"[   002 - RICH BANK                                                                                 ]\n"+
			"[   003 - GOOD BANK                                                                                 ]\n"+
			"[   004 - BANK HOKI                                                                                 ]\n"+
			"[===================================================================================================]\n"+
			"[   [1] "+langOutputs[67][currentLanguange]+"                                                                                     ]\n"+
			"[===================================================================================================]\n"+
			"[  ==> "
		);
		userChoiceMenu = scanner2.nextInt();
		ClearScreen();
		if (userChoiceMenu == 1) {
			transferOtherBank();
		} else {
			defaultCaseMenu();
		}
	}

	public static void exeTransferOtherBank() {
		System.out.println(
			"[===================================================================================================]\n"+
			langOutputs[68][currentLanguange]+
			"[===================================================================================================]"
		);
		System.out.print("[  " + langOutputs[17][currentLanguange]);
		inputTarget_AccountNumber = scannerTF.nextLine();
		String bankCode = inputTarget_AccountNumber.substring(0, 3);
		boolean isBankCodeValid = false;
		String targetAccountNumber = inputTarget_AccountNumber.substring(3);

		// Checking bank code availibility
		if (bankCode.equals("001") || bankCode.equals("002") || bankCode.equals("003") || bankCode.equals("004")) {
			isBankCodeValid = true;
		}

		// Checking target account availibility
		int indexTargetAccount = 0;
		isTargetAccountValid = false;
		if (isBankCodeValid) {
			for (int i = 0; i < accountData.length; i++) {
				if ((targetAccountNumber.equals(accountData[i][0])) && (!targetAccountNumber.equals(inputUser_AccountNumber))) {
					indexTargetAccount = i;
					break;
				}
			}

			if (bankCode.equals("001")) {
				if (accountData[indexTargetAccount][3].equals("BANK JOSS")) {
					isTargetAccountValid = true;
				}
			} else if (bankCode.equals("002")) {
				if (accountData[indexTargetAccount][3].equals("RICH BANK")) {
					isTargetAccountValid = true;
				}
			} else if (bankCode.equals("003")) {
				if (accountData[indexTargetAccount][3].equals("GOOD BANK")) {
					isTargetAccountValid = true;
				}
			} else if (bankCode.equals("004")) {
				if (accountData[indexTargetAccount][3].equals("BANK HOKI")) {
					isTargetAccountValid = true;
				}
			}
		}

		if (isTargetAccountValid) {
			int adminFeeTf = 1500;
			String adminFeeTfRp = currencyFormat.format(adminFeeTf);

			transferAmount = validateNonNegativeIntegerInput("[  " + langOutputs[18][currentLanguange]);
			String transferAmountRupiah = currencyFormat.format(transferAmount);
			int totalTransfer = transferAmount + adminFeeTf;
			String totalTransferRp = currencyFormat.format(totalTransfer);
			ClearScreen();

			System.out.println(
				"[===================================================================================================]\n"+
				"[                                        " + langOutputs[19][currentLanguange]+ "                                         ]\n" +
				"[                              " + langOutputs[20][currentLanguange]+ "                              ]\n" +
				"[                              _______________________________________                              ]\n"+
				"[  -- " + langOutputs[21][currentLanguange] + targetAccountNumber + "\n" +
				"[  -- " + langOutputs[22][currentLanguange] + accountData[indexTargetAccount][2] + "\n"+
				"[  -- " + langOutputs[23][currentLanguange] + accountData[indexTargetAccount][3] + "\n"+
				"[  -- " + langOutputs[24][currentLanguange] + transferAmountRupiah + "\n" +
				"[  -- " + langOutputs[25][currentLanguange] + adminFeeTfRp + "\n" +
				"[===================================================================================================]\n"+
				"[                                      "+langOutputs[77][currentLanguange]+"                                       ]"
			);

			if (UserConfirmation()) {
				if (PinValidation()) {
					if (transferAmount < userBalance) {
						if (transferAmount <= MAX_AMOUNT_TRANSACTION && transferAmount >= MIN_AMOUNT_TRANSACTION) {
							userBalance -= totalTransfer;

							String userBalanceRupiah = currencyFormat.format(userBalance);
							viewTransactionSuccess();

							System.out.println(
								"[===================================================================================================]\n"+
								"[                                        "+ langOutputs[19][currentLanguange]+ "                                         ]\n" +
								"[                                      ______________________                                       ]\n"+
								"[  -- " + langOutputs[21][currentLanguange] + targetAccountNumber+ "\n" +
								"[  -- " + langOutputs[22][currentLanguange]+ accountData[indexTargetAccount][2] + "\n" +
								"[  -- " + langOutputs[23][currentLanguange]+ accountData[indexTargetAccount][3] + "\n" +
								"[  -- " + langOutputs[24][currentLanguange] + transferAmountRupiah+ "\n" +
								"[  -- " + langOutputs[25][currentLanguange] + adminFeeTfRp + "\n" +
								"[  -- " + langOutputs[26][currentLanguange] + userBalanceRupiah + "\n"+
								"[===================================================================================================]"
							);	

							// Recording transaction history
							transactionHistoryList.add(new ArrayList<>(List.of(langOutputs[51][currentLanguange] + inputTarget_AccountNumber + " ("+ accountData[indexTargetAccount][2] + ")", adjustNumCharHistory(totalTransferRp), formattedLocalTime(), formattedLocalDate())));
							recordTransactionHistory();
						} else {
							displayTransactionOverLimit();
						}
					} else {
						viewBalanceIsNotEnough();
					}
				} else {
					viewWrongPin();
				}
			} else {
				viewTransactionCancelled();
			}
		} else {
			ClearScreen();
			System.out.println(
				"[===================================================================================================]\n"+
				"[                    " + red + langOutputs[27][currentLanguange] + reset+ "                    ]\n" +
				"[===================================================================================================]"
			);
		}
	}

	public static void displayHeaderCashWithdrawal() {
		if (currentLanguange == 0) {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - -╔═╗╔═╗╔═╗╦ ╦  ╦ ╦╦╔╦╗╦ ╦╔╦╗╦═╗╔═╗╦ ╦╔═╗╦ - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - -║  ╠═╣╚═╗╠═╣  ║║║║ ║ ╠═╣ ║║╠╦╝╠═╣║║║╠═╣║ - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - -╚═╝╩ ╩╚═╝╩ ╩  ╚╩╝╩ ╩ ╩ ╩═╩╝╩╚═╩ ╩╚╩╝╩ ╩╩═╝ - - - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		} else {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - - - -╔╦╗╔═╗╦═╗╦╦╔═  ╔╦╗╦ ╦╔╗╔╔═╗╦ - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - - ║ ╠═╣╠╦╝║╠╩╗   ║ ║ ║║║║╠═╣║ - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - - ╩ ╩ ╩╩╚═╩╩ ╩   ╩ ╚═╝╝╚╝╩ ╩╩ - - - - - - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		}
	}

	public static void TarikTunai() {
		displayHeaderCashWithdrawal();
		System.out.println(
			"[===================================================================================================]\n"+
			"[                                     " + langOutputs[28][currentLanguange]+ "                                     ]\n" +
			"[                                     _________________________                                     ]\n"+
			"[                       [1] Rp50.000                            [5] Rp500.000                       ]\n"+
			"[                                                                                                   ]\n"+
			"[                       [2] Rp100.000                           [6] Rp750.000                       ]\n"+
			"[                                                                                                   ]\n"+
			"[                       [3] Rp200.000                           [7] Rp1.000.000                     ]\n"+
			"[                                                                                                   ]\n"+
			"[                       [4] Rp300.000                           [8] "+ langOutputs[29][currentLanguange] + "                 ]\n" +
			"[===================================================================================================]");
		System.out.print("[  ==> ");
		int cashWithdrawalChoice = scanner1.nextInt();
		ClearScreen();

		isCashWithdrawalValid = false;
		switch (cashWithdrawalChoice) {
			case 1:
				cashWithdrawalAmount = 50000;
				isCashWithdrawalValid = true;
				break;
			case 2:
				cashWithdrawalAmount = 100000;
				isCashWithdrawalValid = true;
				break;
			case 3:
				cashWithdrawalAmount = 200000;
				isCashWithdrawalValid = true;
				break;
			case 4:
				cashWithdrawalAmount = 300000;
				isCashWithdrawalValid = true;
				break;
			case 5:
				cashWithdrawalAmount = 500000;
				isCashWithdrawalValid = true;
				break;
			case 6:
				cashWithdrawalAmount = 750000;
				isCashWithdrawalValid = true;
				break;
			case 7:
				cashWithdrawalAmount = 1000000;
				isCashWithdrawalValid = true;
				break;
			case 8:
				cashWithdrawalAmount = validateNonNegativeIntegerInput("[  " + langOutputs[30][currentLanguange]);
				isCashWithdrawalValid = true;
				break;
			default:
				defaultCaseMenu();
				EnterForContinue();
				isCashWithdrawalValid = false;
		}

		ClearScreen();

		// Conversion of output value to Rupiah
		String cashWithdrawalRupiah = currencyFormat.format(cashWithdrawalAmount);

		if (isCashWithdrawalValid) {
			System.out.println(
				"[===================================================================================================]\n"+	
				"[  " + langOutputs[31][currentLanguange] + cashWithdrawalRupiah + " ? ");

			if (UserConfirmation()) {
				if (PinValidation()) {
					if (cashWithdrawalAmount < userBalance) {
						if (cashWithdrawalAmount <= MAX_AMOUNT_TRANSACTION && cashWithdrawalAmount >= MIN_AMOUNT_TRANSACTION) {
							userBalance -= cashWithdrawalAmount;
							// Conversion of output value to Rupiah
							String userBalanceRupiah = currencyFormat.format(userBalance);
							viewTransactionSuccess();
							System.out.println("[  " + langOutputs[26][currentLanguange] + userBalanceRupiah);

							// Recording transaction history
							transactionHistoryList.add(new ArrayList<>(List.of(langOutputs[52][currentLanguange],
									adjustNumCharHistory(cashWithdrawalRupiah), formattedLocalTime(),
									formattedLocalDate())));
							recordTransactionHistory();

							EnterForContinue();
						} else {
							displayTransactionOverLimit();
						}
					} else {
						viewBalanceIsNotEnough();
					}
				} else {
					viewWrongPin();
				}
			} else {
				viewTransactionCancelled();
			}
		}
	}

	public static void displayHeaderCashDeposit() {
		if (currentLanguange == 0) {
			System.out.println(
				"[===================================================================================================]\n"+
				"[ - - - - - - - - - - - - - - - - ╔═╗╔═╗╔═╗╦ ╦  ╔╦╗╔═╗╔═╗╔═╗╔═╗╦╔╦╗ - - - - - - - - - - - - - - - - ]\n"+
				"[ - - - - - - - - - - - - - - - - ║  ╠═╣╚═╗╠═╣   ║║║╣ ╠═╝║ ║╚═╗║ ║  - - - - - - - - - - - - - - - - ]\n"+
				"[ - - - - - - - - - - - - - - - - ╚═╝╩ ╩╚═╝╩ ╩  ═╩╝╚═╝╩  ╚═╝╚═╝╩ ╩  - - - - - - - - - - - - - - - - ]\n"+
				"[===================================================================================================]");
		} else {
			System.out.println(
				"[===================================================================================================]\n"+
				"[ - - - - - - - - - - - - - - - - - -╔═╗╔═╗╔╦╗╔═╗╦═╗  ╔╦╗╦ ╦╔╗╔╔═╗╦- - - - - - - - - - - - - - - - -]\n"+
				"[ - - - - - - - - - - - - - - - - - -╚═╗║╣  ║ ║ ║╠╦╝   ║ ║ ║║║║╠═╣║- - - - - - - - - - - - - - - - -]\n"+
				"[ - - - - - - - - - - - - - - - - - -╚═╝╚═╝ ╩ ╚═╝╩╚═   ╩ ╚═╝╝╚╝╩ ╩╩- - - - - - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		}

	}

	public static void SetorTunai() {
		displayHeaderCashDeposit();
		cashDepositAmount = validateNonNegativeIntegerInput("[  " + langOutputs[32][currentLanguange]);

		ClearScreen();

		String cashDepositRupiah = currencyFormat.format(cashDepositAmount);
		System.out.println(
			"[===================================================================================================]\n"+
			"[  " + langOutputs[33][currentLanguange] + cashDepositRupiah + " ? ");

		if (UserConfirmation()) {
			ClearScreen();
			if (PinValidation()) {
				if (cashDepositAmount <= MAX_AMOUNT_TRANSACTION && cashDepositAmount >= MIN_AMOUNT_TRANSACTION) {
					userBalance += cashDepositAmount;
					viewTransactionSuccess();

					// Conversion of output value to Rupiah
					String userBalanceRupiah = currencyFormat.format(userBalance);

					System.out.println("[  " + langOutputs[26][currentLanguange] + userBalanceRupiah);

					// Recording transaction history
					transactionHistoryList.add(new ArrayList<>(List.of(langOutputs[53][currentLanguange],
							adjustNumCharHistory(cashDepositRupiah), formattedLocalTime(), formattedLocalDate())));
					recordTransactionHistory();

					EnterForContinue();
				} else {
					displayTransactionOverLimit();
				}
			} else {
				viewWrongPin();
			}
		} else {
			viewTransactionCancelled();
		}
	}

	public static void displayHeaderPayments() {
		if (currentLanguange == 0) {
			System.out.println(
				"[===================================================================================================]\n"+
				"[ - - - - - - - - - - - - - - - - - -╔═╗╔═╗╦ ╦╔╦╗╔═╗╔╗╔╔╦╗╔═╗ - - - - - - - - - - - - - - - - - - - ]\n"+
				"[ - - - - - - - - - - - - - - - - - -╠═╝╠═╣╚╦╝║║║║╣ ║║║ ║ ╚═╗ - - - - - - - - - - - - - - - - - - - ]\n"+
				"[ - - - - - - - - - - - - - - - - - -╩  ╩ ╩ ╩ ╩ ╩╚═╝╝╚╝ ╩ ╚═╝ - - - - - - - - - - - - - - - - - - - ]\n"+
				"[===================================================================================================]");
		} else {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - - ╔═╗╔═╗╔╦╗╔╗ ╔═╗╦ ╦╔═╗╦═╗╔═╗╔╗╔- - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - ╠═╝║╣ ║║║╠╩╗╠═╣╚╦╝╠═╣╠╦╝╠═╣║║║- - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - ╩  ╚═╝╩ ╩╚═╝╩ ╩ ╩ ╩ ╩╩╚═╩ ╩╝╚╝- - - - - - - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		}
	}

	public static void PembayaranLainnya() {
		displayHeaderPayments();
		System.out.println(langOutputs[34][currentLanguange]);
		System.out.print("[  ==> ");
		int menuBayar = scanner3.nextInt();
		ClearScreen();
		switch (menuBayar) {
			case 1:
				Pulsa();
				break;
			case 2:
				Listrik();
				break;
			case 3:
				Pendidikan();
				break;
			case 4:
				Pdam();
				break;
			case 5:
				Bpjs();
				break;
			case 6:
				returnToMainMenu();
				break;
			default:
				defaultCaseMenu();
				break;
		}
	}

	public static void displayHeaderPulsa() {
		if (currentLanguange == 0) {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - - - - - -╔═╗╦═╗╔═╗╔╦╗╦╔╦╗- - - - - - - - - - - - - - - - - - - - - ]\n"+
				"[- - - - - - - - - - - - - - - - - - - - -║  ╠╦╝║╣  ║║║ ║ - - - - - - - - - - - - - - - - - - - - - ]\n"+
				"[- - - - - - - - - - - - - - - - - - - - -╚═╝╩╚═╚═╝═╩╝╩ ╩ - - - - - - - - - - - - - - - - - - - - - ]\n"+
				"[===================================================================================================]");
		} else {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - - - - - -╔═╗╦ ╦╦  ╔═╗╔═╗- - - - - - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - - - -╠═╝║ ║║  ╚═╗╠═╣- - - - - - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - - - -╩  ╚═╝╩═╝╚═╝╩ ╩- - - - - - - - - - - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		}
	}

	public static void Pulsa() {
		Scanner scannerPulsa = new Scanner(System.in);
		displayHeaderPulsa();
		String nomorTelepon;
		int nomPulsa;

		boolean isOperatorValid = false;
		String operatorPulsa = null;
		do {
			System.out.print(
				"[===================================================================================================]\n"+
				"[                                     " + langOutputs[35][currentLanguange]+ "                                   ]\n" +
				"[===================================================================================================]\n"+
				"[           [1] INDOSAT                                                                             ]\n"+
				"[                                                                                                   ]\n"+
				"[           [2] XL                                                                                  ]\n"+
				"[                                                                                                   ]\n"+
				"[           [3] TELKOMSEL                                                                           ]\n"+
				"[===================================================================================================]\n"+
				"[  ==> ");
			operatorPulsa = scannerPulsa.nextLine();
			ClearScreen();
			switch (operatorPulsa) {
				case "1":
					operatorPulsa = "Indosat";
					isOperatorValid = true;
					break;
				case "2":
					operatorPulsa = "XL";
					isOperatorValid = true;
					break;
				case "3":
					operatorPulsa = "Telkomsel";
					isOperatorValid = true;
					break;
				default:
					System.out.println(
						"[===================================================================================================]\n"+
						"[                         "+red+"[!]  " + langOutputs[36][currentLanguange]+ "  [!]"+reset+"                            ]\n" +
						"[===================================================================================================]");
					EnterForContinue();
					isOperatorValid = false;
					break;
			}
		} while (!isOperatorValid);

		if (isOperatorValid) {
			System.out.print("[  " + langOutputs[37][currentLanguange]);
			nomorTelepon = scannerPulsa.nextLine();
			scanner1.nextLine();
			nomPulsa = validateNonNegativeIntegerInput("[  " + langOutputs[38][currentLanguange]);
			int totalPayment = nomPulsa + adminFee;
			String totalPaymentRp = currencyFormat.format(totalPayment);
			ClearScreen();

			// Conversion of output value to Rupiah
			String nomPulsaRp = currencyFormat.format(nomPulsa);
			// Menampilkan informasi transaksi sementara

			System.out.println(
				"[===================================================================================================]\n"+
				"[                                        " + langOutputs[19][currentLanguange]+ "                                         ]\n" +
				"[                                      ______________________                                       ]\n"+
				"[  -- " + langOutputs[39][currentLanguange] + operatorPulsa + "\n" +
				"[  -- " + langOutputs[40][currentLanguange] + nomorTelepon + "\n" +
				"[  -- " + langOutputs[41][currentLanguange] + nomPulsaRp + "\n" +
				"[  -- " + langOutputs[25][currentLanguange] + adminFeeRp + "\n" +
				"[===================================================================================================]");
			
			System.out.println(
				"[===================================================================================================]\n"+
				"[                                      "+langOutputs[77][currentLanguange]+"                                       ]"
			);

			if (UserConfirmation()) {
				ClearScreen();
				if (PinValidation()) {
					if (nomPulsa < userBalance) {
						userBalance -= totalPayment;
						String saldoRupiah2 = currencyFormat.format(userBalance);

						// Menampilkan output transaksi berhasil
						viewTransactionSuccess();
						System.out.println(
							"[===================================================================================================]\n"+
							"[                                        " + langOutputs[19][currentLanguange]+ "                                         ]\n" +
							"[                                      ______________________                                       ]\n"+
							"[  -- " + langOutputs[39][currentLanguange] + operatorPulsa + "\n" +
							"[  -- " + langOutputs[40][currentLanguange] + nomorTelepon + "\n" +
							"[  -- " + langOutputs[41][currentLanguange] + nomPulsaRp + "\n" +
							"[  -- " + langOutputs[25][currentLanguange] + adminFeeRp + "\n" +
							"[  -- " + langOutputs[26][currentLanguange] + saldoRupiah2 + "\n" +
							"[===================================================================================================]");

						// Pencatatan riwayat transaksi
						transactionHistoryList.add(new ArrayList<>(List.of(
								adjustNumCharHistory(langOutputs[54][currentLanguange] + nomorTelepon),
								adjustNumCharHistory(totalPaymentRp), formattedLocalTime(), formattedLocalDate())));
						recordTransactionHistory();

						EnterForContinue();
					} else {
						viewBalanceIsNotEnough();
					}
				} else {
					viewWrongPin();
				}
			} else {
				viewTransactionCancelled();
			}
		}
	}

	public static void displayHeaderElectricity() {
		System.out.println(
			"[===================================================================================================]\n"+
			"[- - - - - - - - - - - - - - - - - - - - - - -╔═╗╦  ╔╗╔- - - - - - - - - - - - - - - - - - - - - - -]\n"+
			"[- - - - - - - - - - - - - - - - - - - - - - -╠═╝║  ║║║- - - - - - - - - - - - - - - - - - - - - - -]\n"+
			"[- - - - - - - - - - - - - - - - - - - - - - -╩  ╩═╝╝╚╝- - - - - - - - - - - - - - - - - - - - - - -]\n"+
			"[===================================================================================================]");
	}

	public static void Listrik() {
		displayHeaderElectricity();
		System.out.print("[  " + langOutputs[42][currentLanguange]);
		int inputPLN = scanner4.nextInt();
		ClearScreen();

		// Checking payment code availability
		isListrikValid = false;
		for (int i = 0; i < listrikData.length; i++) {
			if (inputPLN == listrikData[i][0]) {
				indexListrik = i;
				isListrikValid = true;
				break;
			}
		}

		// Proses perhitungan tagihan listrik PLN
		if (isListrikValid) {
			if (listrikData[indexListrik][2] == 1) {
				// Formatting output ke Rupiah
				String tagihanListrikRP = currencyFormat.format(listrikData[indexListrik][1]);
				int totalPayment = listrikData[indexListrik][1] + adminFee;
				String totalPaymentRp = currencyFormat.format(totalPayment);
				System.out.println(
					"[===================================================================================================]\n"+
					"[                                        " + langOutputs[19][currentLanguange]+ "                                         ]\n" +
					"[                                      ______________________                                       ]\n"+
					"[  -- " + langOutputs[43][currentLanguange] + inputPLN + "\n" +
					"[  -- " + langOutputs[44][currentLanguange] + tagihanListrikRP + "\n" +
					"[  -- " + langOutputs[25][currentLanguange] + adminFeeRp + "\n" +
					"[===================================================================================================]");
				
				System.out.println(
					"[===================================================================================================]\n"+
					"[                                      "+langOutputs[77][currentLanguange]+"                                       ]"
				);

				if (UserConfirmation()) {
					if (PinValidation()) {
						if (listrikData[indexListrik][1] < userBalance) {
							userBalance -= totalPayment;
							listrikData[indexListrik][2] = 0;
							// Formatting saldo pengguna ke Rupiah
							String saldoRupiah3 = currencyFormat.format(userBalance);
							viewTransactionSuccess();
							System.out.println(
								"[===================================================================================================]\n"+
								"[                                        "+ langOutputs[19][currentLanguange]+ "                                         ]\n" +
								"[                                      ______________________                                       ]\n"+
								"[  -- " + langOutputs[43][currentLanguange] + inputPLN + "\n" +
								"[  -- " + langOutputs[44][currentLanguange] + tagihanListrikRP + "\n" +
								"[  -- " + langOutputs[25][currentLanguange] + adminFeeRp + "\n" +
								"[  -- " + langOutputs[26][currentLanguange] + saldoRupiah3 + "\n" +
								"[===================================================================================================]");

							// Recording transaction history
							transactionHistoryList.add(new ArrayList<>(List.of(
									adjustNumCharHistory(langOutputs[55][currentLanguange] + "(" + inputPLN + ")"),
									adjustNumCharHistory(totalPaymentRp), formattedLocalTime(), formattedLocalDate())));
							recordTransactionHistory();

							EnterForContinue();
						} else {
							viewBalanceIsNotEnough();
						}
					} else {
						viewWrongPin();
					}
				} else {
					viewTransactionCancelled();
				}
			} else {
				viewPaymentCodeAlreadyUSe();
			}
		} else {
			viewPaymentCodeInvalid();
		}
	}

	public static void displayHeaderEducationBill() {
		if (currentLanguange == 0) {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - - - -╔═╗╔╦╗╦ ╦╔═╗╔═╗╔╦╗╦╔═╗╔╗╔- - - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - -║╣  ║║║ ║║  ╠═╣ ║ ║║ ║║║║- - - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - -╚═╝═╩╝╚═╝╚═╝╩ ╩ ╩ ╩╚═╝╝╚╝- - - - - - - - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		} else {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - - - -╔═╗╔═╗╔╗╔╔╦╗╦╔╦╗╦╦╔═╔═╗╔╗╔ - - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - -╠═╝║╣ ║║║ ║║║ ║║║╠╩╗╠═╣║║║ - - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - -╩  ╚═╝╝╚╝═╩╝╩═╩╝╩╩ ╩╩ ╩╝╚╝ - - - - - - - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		}

	}

	public static void Pendidikan() {
		displayHeaderEducationBill();
		System.out.print("[  " + langOutputs[42][currentLanguange]);
		int inputVA = scanner4.nextInt();
		ClearScreen();

		// pengecekan data VA
		isPendidikanValid = false;
		for (int i = 0; i < pendidikanData.length; i++) {
			if (inputVA == pendidikanData[i][0]) {
				indexPendidikan = i;
				isPendidikanValid = true;
				break;
			}
		}

		if (isPendidikanValid) {
      if (pendidikanData[indexPendidikan][2] == 1) {
			String tagihanPendidikanRP = currencyFormat.format(pendidikanData[indexPendidikan][1]);
			int totalPayment = pendidikanData[indexPendidikan][1] + adminFee;
			String totalPaymentRp = currencyFormat.format(totalPayment);
			System.out.println(
				"[===================================================================================================]\n"+
				"[                                        " + langOutputs[19][currentLanguange]+ "                                         ]\n" +
				"[                                      ______________________                                       ]\n"+
				"[  -- " + langOutputs[43][currentLanguange] + inputVA + "\n" +
				"[  -- " + langOutputs[44][currentLanguange] + tagihanPendidikanRP + "\n" +
				"[  -- " + langOutputs[25][currentLanguange] + adminFeeRp + "\n" +
				"[===================================================================================================]");

			System.out.println(
				"[===================================================================================================]\n"+
				"[                                      "+langOutputs[77][currentLanguange]+"                                       ]"
			);

				if (UserConfirmation()) {
					if (PinValidation()) {
						if (pendidikanData[indexPendidikan][1] < userBalance) {
							userBalance -= totalPayment;
							pendidikanData[indexPendidikan][2] = 0;
							// Formatting output ke Rupiah
							String saldoRupiah3 = currencyFormat.format(userBalance);

						viewTransactionSuccess();
						System.out.println(
							"[===================================================================================================]\n"+
							"[                                        " + langOutputs[19][currentLanguange]+ "                                         ]\n" +
							"[                                      ______________________                                       ]\n"+
							"[  -- " + langOutputs[42][currentLanguange] + inputVA + "\n" +
							"[  -- " + langOutputs[44][currentLanguange] + tagihanPendidikanRP + "\n" +
							"[  -- " + langOutputs[25][currentLanguange] + adminFeeRp + "\n" +
							"[  -- " + langOutputs[26][currentLanguange] + saldoRupiah3 + "\n" +
							"[===================================================================================================]");

						// Recording Transaction History
						transactionHistoryList.add(new ArrayList<>(List.of(
								adjustNumCharHistory(langOutputs[56][currentLanguange] + "(" + inputVA + ")"),
								adjustNumCharHistory(totalPaymentRp), formattedLocalTime(), formattedLocalDate())));
						recordTransactionHistory();

							EnterForContinue();
						} else {
							viewBalanceIsNotEnough();
						}
					} else {
						viewWrongPin();
					}
				} else {
					viewTransactionCancelled();
				}
			} else {
				viewPaymentCodeAlreadyUSe();
			}
		} else {
			viewPaymentCodeInvalid();
		}

	}

	public static void displayHeaderWaterBill() {
		System.out.println(
			"[===================================================================================================]\n"+
			"[- - - - - - - - - - - - - - - - - - - - - -╔═╗╔╦╗╔═╗╔╦╗ - - - - - - - - - - - - - - - - - - - - - -]\n"+
			"[- - - - - - - - - - - - - - - - - - - - - -╠═╝ ║║╠═╣║║║ - - - - - - - - - - - - - - - - - - - - - -]\n"+
			"[- - - - - - - - - - - - - - - - - - - - - -╩  ═╩╝╩ ╩╩ ╩ - - - - - - - - - - - - - - - - - - - - - -]\n"+
			"[===================================================================================================]");
	}

	public static void Pdam() {
		displayHeaderWaterBill();
		System.out.print("[  " + langOutputs[42][currentLanguange]);
		int inputVA = scanner4.nextInt();
		ClearScreen();

		// pengecekan data VA
		isPdamValid = false;
		for (int i = 0; i < tagihanAirData.length; i++) {
			if (inputVA == tagihanAirData[i][0]) {
				indexPdam = i;
				isPdamValid = true;
				break;
			}
		}

		if (isPdamValid) {
  if (tagihanAirData[indexPdam][2] == 1) {
			String tagihanPdamRp = currencyFormat.format(tagihanAirData[indexPdam][1]);
			int totalPayment = tagihanAirData[indexPdam][1] + adminFee;
			String totalPaymentRp = currencyFormat.format(totalPayment);
			System.out.println(
				"[===================================================================================================]\n"+
				"[                                        " + langOutputs[19][currentLanguange]+ "                                         ]\n" +
				"[                                      ______________________                                       ]\n"+
				"[  -- " + langOutputs[43][currentLanguange] + inputVA + "\n" +
				"[  -- " + langOutputs[44][currentLanguange] + tagihanPdamRp + "\n" +
				"[  -- " + langOutputs[25][currentLanguange] + adminFeeRp + "\n" +
				"[===================================================================================================]");
			
			System.out.println(
				"[===================================================================================================]\n"+
				"[                                      "+langOutputs[77][currentLanguange]+"                                       ]"
			);

			if (UserConfirmation()) {
				if (PinValidation()) {
					if (tagihanAirData[indexPdam][1] < userBalance) {
						userBalance -= totalPayment;
            tagihanAirData[indexPdam][2] = 0;
						// Formatting output ke Rupiah
						String saldoRupiah3 = currencyFormat.format(userBalance);
						viewTransactionSuccess();
						System.out.println(
							"[===================================================================================================]\n"+
							"[                                        " + langOutputs[19][currentLanguange]+ "                                         ]\n" +
							"[                                      ______________________                                       ]\n"+
							"[  -- " + langOutputs[43][currentLanguange] + inputVA + "\n" +
							"[  -- " + langOutputs[44][currentLanguange] + tagihanPdamRp + "\n" +
							"[  -- " + langOutputs[25][currentLanguange] + adminFeeRp + "\n" +
							"[  -- " + langOutputs[26][currentLanguange] + saldoRupiah3 + "\n" +
							"[===================================================================================================]");

						// Recording Transaction History
						transactionHistoryList.add(new ArrayList<>(List.of(
								adjustNumCharHistory(langOutputs[57][currentLanguange] + "(" + inputVA + ")"),
								adjustNumCharHistory(totalPaymentRp), formattedLocalTime(), formattedLocalDate())));
						recordTransactionHistory();

							EnterForContinue();
						} else {
							viewBalanceIsNotEnough();
						}
					} else {
						viewWrongPin();
					}
				} else {
					viewTransactionCancelled();
				}
			} else {
				viewPaymentCodeAlreadyUSe();
			}
		} else {
			viewPaymentCodeInvalid();
		}
	}

	public static void displayHeaderBpjs() {
		System.out.println(
			"[===================================================================================================]\n"+
			"[- - - - - - - - - - - - - - - - - - - - - - ╔╗ ╔═╗╦╔═╗ - - - - - - - - - - - - - - - - - - - - - - ]\n"+
			"[- - - - - - - - - - - - - - - - - - - - - - ╠╩╗╠═╝║╚═╗ - - - - - - - - - - - - - - - - - - - - - - ]\n"+
			"[- - - - - - - - - - - - - - - - - - - - - - ╚═╝╩ ╚╝╚═╝ - - - - - - - - - - - - - - - - - - - - - - ]\n"+
			"[===================================================================================================]");
	}

	public static void Bpjs() {
		displayHeaderBpjs();
		System.out.print("[  " + langOutputs[42][currentLanguange]);
		int inputVA = scanner4.nextInt();
		ClearScreen();

		// pengecekan data VA
		isBpjsValid = false;
		for (int i = 0; i < BPJSdata.length; i++) {
			if (inputVA == BPJSdata[i][0]) {
				indexBpjs = i;
				isBpjsValid = true;
				break;
			}
		}

		if (isBpjsValid) {
  			if (BPJSdata[indexBpjs][2] == 1) {
			String tagihanBpjsRp = currencyFormat.format(BPJSdata[indexBpjs][1]);
			int totalPayment = BPJSdata[indexBpjs][1] + adminFee;
			String totalPaymentRp = currencyFormat.format(totalPayment);
			System.out.println(
				"[===================================================================================================]\n"+
				"[                                        " + langOutputs[19][currentLanguange]+ "                                         ]\n" +
				"[                                      ______________________                                       ]\n"+
				"[  -- " + langOutputs[43][currentLanguange] + inputVA + "\n" +
				"[  -- " + langOutputs[44][currentLanguange] + tagihanBpjsRp + "\n" +
				"[  -- " + langOutputs[25][currentLanguange] + adminFeeRp + "\n" +
				"[===================================================================================================]");

				System.out.println(
					"[===================================================================================================]\n"+
					"[                                      "+langOutputs[77][currentLanguange]+"                                       ]"
				);

				if (UserConfirmation()) {
					if (PinValidation()) {
						if (BPJSdata[indexBpjs][1] < userBalance) {
							userBalance -= totalPayment;
							BPJSdata[indexBpjs][2] = 0;
							// Formatting output ke Rupiah
							String saldoRupiah3 = currencyFormat.format(userBalance);
							viewTransactionSuccess();

						System.out.println(
							"[===================================================================================================]\n"+
							"[                                        " + langOutputs[19][currentLanguange]+ "                                         ]\n" +
							"[                                      ______________________                                       ]\n"+
							"[  -- " + langOutputs[43][currentLanguange] + inputVA + "\n" +
							"[  -- " + langOutputs[44][currentLanguange] + tagihanBpjsRp + "\n" +
							"[  -- " + langOutputs[25][currentLanguange] + adminFeeRp + "\n" +
							"[  -- " + langOutputs[26][currentLanguange] + saldoRupiah3 + "\n" +
							"[===================================================================================================]");

						// Recording Transaction History
						transactionHistoryList.add(new ArrayList<>(List.of(
								adjustNumCharHistory(langOutputs[58][currentLanguange] + "(" + inputVA + ")"),
								adjustNumCharHistory(totalPaymentRp), formattedLocalTime(), formattedLocalDate())));
						recordTransactionHistory();

							EnterForContinue();
						} else {
							viewBalanceIsNotEnough();
						}
					} else {
						viewWrongPin();
					}
				} else {
					viewTransactionCancelled();
				}
			} else {
				viewPaymentCodeAlreadyUSe();
			}
		} else {
			viewPaymentCodeInvalid();
		}
	}

	public static void returnToMainMenu() {
		isGoToMainMenu = true;
		isContinueTransaction = true;
	}

	public static void displayHeaderHistory() {
		if (currentLanguange == 0) {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - - - - - ╦ ╦╦╔═╗╔╦╗╔═╗╦═╗╦ ╦ - - - - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - - - ╠═╣║╚═╗ ║ ║ ║╠╦╝╚╦╝ - - - - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - - - ╩ ╩╩╚═╝ ╩ ╚═╝╩╚═ ╩  - - - - - - - - - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		} else {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - - - - - ╦═╗╦╦ ╦╔═╗╦ ╦╔═╗╔╦╗ - - - - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - - - ╠╦╝║║║║╠═╣╚╦╝╠═╣ ║  - - - - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - - - ╩╚═╩╚╩╝╩ ╩ ╩ ╩ ╩ ╩  - - - - - - - - - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		}
	}

	public static void History() {
		displayHeaderHistory();
		System.out.print(
			"[===================================================================================================]\n"+
			"[                                    " + langOutputs[0][currentLanguange]+ "                                ]\n" +
			"[===================================================================================================]\n"+
			"[   --  [1] " + langOutputs[45][currentLanguange]+ "                                                                     ]\n" +
			"[                                                                                                   ]\n"+
			"[   --  [2] " + langOutputs[46][currentLanguange]+ "                                                                         ]\n" +
			"[===================================================================================================]\n"+
			"[  ==> ");
		userChoiceMenu = scanner1.nextInt();
		ClearScreen();
		switch (userChoiceMenu) {
			case 1:
				transactionHistory();
				break;
			case 2:
				accountHistory();
				break;
			default:
				defaultCaseMenu();
		}
		EnterForContinue();
	}

	public static void displayHeaderTransactionHistory() {
		if (currentLanguange == 0) {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - -╔╦╗╦═╗╔═╗╔╗╔╔═╗╔═╗╔═╗╔╦╗╦╔═╗╔╗╔  ╦ ╦╦╔═╗╔╦╗╔═╗╦═╗╦ ╦ - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - ║ ╠╦╝╠═╣║║║╚═╗╠═╣║   ║ ║║ ║║║║  ╠═╣║╚═╗ ║ ║ ║╠╦╝╚╦╝ - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - ╩ ╩╚═╩ ╩╝╚╝╚═╝╩ ╩╚═╝ ╩ ╩╚═╝╝╚╝  ╩ ╩╩╚═╝ ╩ ╚═╝╩╚═ ╩  - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		} else {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - ╦═╗╦╦ ╦╔═╗╦ ╦╔═╗╔╦╗  ╔╦╗╦═╗╔═╗╔╗╔╔═╗╔═╗╦╔═╔═╗╦- - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - ╠╦╝║║║║╠═╣╚╦╝╠═╣ ║    ║ ╠╦╝╠═╣║║║╚═╗╠═╣╠╩╗╚═╗║- - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - ╩╚═╩╚╩╝╩ ╩ ╩ ╩ ╩ ╩    ╩ ╩╚═╩ ╩╝╚╝╚═╝╩ ╩╩ ╩╚═╝╩- - - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		}
	}

	public static void transactionHistory() {
		displayHeaderTransactionHistory();
		System.out.println(
			"[===================================================================================================]\n"+
			"[                                  " + langOutputs[47][currentLanguange]+ "                                  ]\n" +
			"[                                 _________________________________                                 ]\n"+
			"[                                                                                                   ]\n"+
			"[               INFO                                 " + langOutputs[48][currentLanguange]+ "              " + langOutputs[49][currentLanguange] + "     "+ langOutputs[50][currentLanguange] + "         ]");

		displayTransactionHistory();

		System.out.println(
				"[===================================================================================================]");
	}

	public static void recordTransactionHistory() {
		for (int i = transactionHistoryList.size(); i > 10; i--) {
			transactionHistoryList.remove(0);
		}
	}

	public static void displayTransactionHistory() {
		for (int i = 0; i < transactionHistoryList.size(); i++) {
			System.out.printf("[  %d. " + transactionHistoryList.get(i).get(0) + "\t| "
					+ transactionHistoryList.get(i).get(1) + "\t| " + transactionHistoryList.get(i).get(2) + "\t| "
					+ transactionHistoryList.get(i).get(3) + "\t    ]\n", (i + 1));
		}
	}

	public static String adjustNumCharHistory(String myString) {
		if (myString.charAt(0) == 'R') {
			if (myString.length() < 14) {
				if (myString.length() == 13) {
					myString = myString + " ";
				} else if (myString.length() == 12) {
					myString = myString + "  ";
				} else if (myString.length() == 11) {
					myString = myString + "   ";
				} else if (myString.length() == 10) {
					myString = myString + "    ";
				}
			}
		} else {
			if (myString.length() < 35) {
				if (myString.length() == 34) {
					myString = myString + " ";
				} else if (myString.length() == 33) {
					myString = myString + "  ";
				} else if (myString.length() == 32) {
					myString = myString + "   ";
				} else if (myString.length() == 31) {
					myString = myString + "    ";
				} else if (myString.length() == 30) {
					myString = myString + "     ";
				} else if (myString.length() == 29) {
					myString = myString + "      ";
				} else if (myString.length() == 28) {
					myString = myString + "       ";
				} else if (myString.length() == 27) {
					myString = myString + "         ";
				} else if (myString.length() == 26) {
					myString = myString + "          ";
				} else if (myString.length() == 25) {
					myString = myString + "           ";
				} else if (myString.length() == 24) {
					myString = myString + "            ";
				} else if (myString.length() == 23) {
					myString = myString + "             ";
				} else if (myString.length() == 22) {
					myString = myString + "              ";
				} else if (myString.length() == 21) {
					myString = myString + "               ";
				} else if (myString.length() == 20) {
					myString = myString + "                ";
				} else if (myString.length() == 19) {
					myString = myString + "                 ";
				} else if (myString.length() == 18) {
					myString = myString + "                  ";
				} else if (myString.length() == 17) {
					myString = myString + "                   ";
				} else if (myString.length() == 16) {
					myString = myString + "                    ";
				} else if (myString.length() == 15) {
					myString = myString + "                     ";
				} else if (myString.length() == 14) {
					myString = myString + "                      ";
				}
			}
		}
		return myString;

	}

	public static void displayHeaderAccountHistory() {
		if (currentLanguange == 0) {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - ╔═╗╔═╗╔═╗╔═╗╦ ╦╔╗╔╔╦╗  ╦ ╦╦╔═╗╔╦╗╔═╗╦═╗╦ ╦- - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - ╠═╣║  ║  ║ ║║ ║║║║ ║   ╠═╣║╚═╗ ║ ║ ║╠╦╝╚╦╝- - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - ╩ ╩╚═╝╚═╝╚═╝╚═╝╝╚╝ ╩   ╩ ╩╩╚═╝ ╩ ╚═╝╩╚═ ╩ - - - - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		} else {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - -╦═╗╦╦ ╦╔═╗╦ ╦╔═╗╔╦╗  ╔═╗╦╔═╦ ╦╔╗╔- - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - -╠╦╝║║║║╠═╣╚╦╝╠═╣ ║   ╠═╣╠╩╗║ ║║║║- - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - -╩╚═╩╚╩╝╩ ╩ ╩ ╩ ╩ ╩   ╩ ╩╩ ╩╚═╝╝╚╝- - - - - - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		}
	}

	public static void accountHistory() {
		displayHeaderAccountHistory();
		String accountStatus = accountData[accountLineIndex][5];
		if (currentLanguange == 1) {
			accountStatus = "AMAN  ";
		}
		System.out.println(
			"[===================================================================================================]\n"+
			"[                                     "+langOutputs[59][currentLanguange]+"                                   ]\n"+
			"[   - "+langOutputs[60][currentLanguange]+"\t: "+accountData[accountLineIndex][6]+"                                                                ]\n"+
			"[   - STATUS         \t: "+accountStatus+"                                                                    ]\n"+
			"[   _____________________________________________________________________________________________   ]"
		);

		displayAccountHistory();
		System.out.println("[===================================================================================================]");
	}

	public static void recordAccountHistory(String myInfo, String myTime, String myDate) {
		accountHistoryList.add(new ArrayList<>(List.of(myInfo, myTime, myDate)));
	}

	public static void displayAccountHistory() {
		ArrayList<String> accountHistory = new ArrayList<>();
		for (int i = 0; i < accountHistoryList.size(); i++) {
			accountHistory.add(accountHistoryList.get(i).get(0) + " " + accountHistoryList.get(i).get(1) + " "
					+ accountHistoryList.get(i).get(2));
		}

		if (currentLanguange == 0) {
			for (int i = 0; i < accountHistory.size(); i++) {
				accountHistory.set(i, accountHistory.get(i) + "        ");
			}
		} else {
			for (int i = 0; i < accountHistory.size(); i++) {
				accountHistory.set(i, accountHistory.get(i) + " ");
			}
		}

		for (int i = 0; i < accountHistory.size(); i++) {
			System.out.printf("[  %d. %s                                               ]\n", (i + 1),
					accountHistory.get(i));
		}
	}

	public static void displayHeaderBalanceInquiry() {
		if (currentLanguange == 0) {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - -╔╗ ╔═╗╦  ╔═╗╔╗╔╔═╗╔═╗  ╦╔╗╔╔═╗ ╦ ╦╦╦═╗╦ ╦- - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - -╠╩╗╠═╣║  ╠═╣║║║║  ║╣   ║║║║║═╬╗║ ║║╠╦╝╚╦╝- - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - -╚═╝╩ ╩╩═╝╩ ╩╝╚╝╚═╝╚═╝  ╩╝╚╝╚═╝╚╚═╝╩╩╚═ ╩ - - - - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		} else {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - - - ╔═╗╔═╗╦╔═  ╔═╗╔═╗╦  ╔╦╗╔═╗ - - - - - - - - - - - - - - - - - - ]\n"+
				"[- - - - - - - - - - - - - - - - - - ║  ║╣ ╠╩╗  ╚═╗╠═╣║   ║║║ ║ - - - - - - - - - - - - - - - - - - ]\n"+
				"[- - - - - - - - - - - - - - - - - - ╚═╝╚═╝╩ ╩  ╚═╝╩ ╩╩═╝═╩╝╚═╝ - - - - - - - - - - - - - - - - - - ]\n"+
				"[===================================================================================================]");
		}
	}

	public static void CekSaldo() {
		displayHeaderBalanceInquiry();
		String saldoRupiah3 = currencyFormat.format(userBalance);
		System.out.println(
			"[===================================================================================================]\n"+
			"[                 ----------------------------------------------------------------                  ]\n"+
			"[                 "+langOutputs[62][currentLanguange]+saldoRupiah3+"\t                                            ]\n"+
			"[                 ----------------------------------------------------------------                  ]\n"+
			"[===================================================================================================]"
		);

		EnterForContinue();
	}

	public static void displayHeaderChangePin() {
		if (currentLanguange == 0) {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - - - ╔═╗╦ ╦╔═╗╔╗╔╔═╗╔═╗  ╔═╗╦╔╗╔ - - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - ║  ╠═╣╠═╣║║║║ ╦║╣   ╠═╝║║║║- - - - - - - - - - - - - - - - - - ]\n"+
				"[- - - - - - - - - - - - - - - - - - ╚═╝╩ ╩╩ ╩╝╚╝╚═╝╚═╝  ╩  ╩╝╚╝- - - - - - - - - - - - - - - - - - ]\n"+
				"[===================================================================================================]");
		} else {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - - - - ╦ ╦╔╗ ╔═╗╦ ╦  ╔═╗╦╔╗╔- - - - - - - - - - - - - - - - - - - - ]\n"+
				"[- - - - - - - - - - - - - - - - - - - ║ ║╠╩╗╠═╣╠═╣  ╠═╝║║║║- - - - - - - - - - - - - - - - - - - - ]\n"+
				"[- - - - - - - - - - - - - - - - - - - ╚═╝╚═╝╩ ╩╩ ╩  ╩  ╩╝╚╝- - - - - - - - - - - - - - - - - - - - ]\n"+
				"[===================================================================================================]");
		}
	}

	public static void UbahPin() {
		Scanner scanNewPin = new Scanner(System.in);
		displayHeaderChangePin();
		String userRekening = accountData[accountLineIndex][0];
		System.out.print("[  " + langOutputs[71][currentLanguange]);
		String inputPin7 = scanNewPin.nextLine();

		if (inputPin7.equals(inputPin)) {
			String inputNewPin = getValidatedPin(scanNewPin, 1);
			String confirmedNewPin = getValidatedPin(scanNewPin, 2);

			ClearScreen();

			if (inputNewPin.equals(confirmedNewPin)) {
				int indeksNoRek = 0;
				accountData[accountLineIndex][1] = confirmedNewPin;
				inputPin = confirmedNewPin;

				recordAccountHistory(langOutputs[61][currentLanguange], formattedLocalTime(), formattedLocalDate());

				ClearScreen();

				System.out.println(
					"[===================================================================================================]\n"+
					"[                                  "+langOutputs[74][currentLanguange]+"                                   ]\n"+
					"[===================================================================================================]"
				);
			} else {
				System.out.println(
					"[===================================================================================================]\n"+
					"[                         "+langOutputs[75][currentLanguange]+"                          ]\n"+
					"[===================================================================================================]"
					);
			}
		} else {
			System.out.println(
				"[===================================================================================================]\n"+
				"[                                    "+langOutputs[76][currentLanguange]+"                                     ]\n"+
				"[===================================================================================================]"
				);
		}
	}

	public static void displayHeaderExit() {
		if (currentLanguange == 0) {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - - - - - - - ╔═╗═╗ ╦╦╔╦╗ - - - - - - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - - - - - ║╣ ╔╩╦╝║ ║  - - - - - - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - - - - - ╚═╝╩ ╚═╩ ╩  - - - - - - - - - - - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		} else {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - - - - - ╦╔═╔═╗╦  ╦ ╦╔═╗╦═╗ - - - - - - - - - - - - - - - - - - - - ]\n"+
				"[- - - - - - - - - - - - - - - - - - - - ╠╩╗║╣ ║  ║ ║╠═╣╠╦╝ - - - - - - - - - - - - - - - - - - - - ]\n"+
				"[- - - - - - - - - - - - - - - - - - - - ╩ ╩╚═╝╩═╝╚═╝╩ ╩╩╚═ - - - - - - - - - - - - - - - - - - - - ]\n"+
				"[===================================================================================================]");
		}
	}

	public static boolean Exit() {
		displayHeaderExit();
		System.out.println("[  " + langOutputs[63][currentLanguange] + "                                                                  ]");
		if (UserConfirmation()) {
			displayClosing();
			return isStopTransaction = true;
		} else {
			return isContinueTransaction = true;
		}
	}

	public static void displayHeaderHelp() {
		if (currentLanguange == 0) {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - - - - - - -╦ ╦╔═╗╦  ╔═╗- - - - - - - - - - - - - - - - - - - - - - ]\n"+
				"[- - - - - - - - - - - - - - - - - - - - - -╠═╣║╣ ║  ╠═╝- - - - - - - - - - - - - - - - - - - - - - ]\n"+
				"[- - - - - - - - - - - - - - - - - - - - - -╩ ╩╚═╝╩═╝╩- - - - - - - - - - - - - - - - - - - - - - - ]\n"+
				"[===================================================================================================]");
		} else {
			System.out.println(
				"[===================================================================================================]\n"+
				"[- - - - - - - - - - - - - - - - - - - -╔╗ ╔═╗╔╗╔╔╦╗╦ ╦╔═╗╔╗╔- - - - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - - -╠╩╗╠═╣║║║ ║ ║ ║╠═╣║║║- - - - - - - - - - - - - - - - - - - -]\n"+
				"[- - - - - - - - - - - - - - - - - - - -╚═╝╩ ╩╝╚╝ ╩ ╚═╝╩ ╩╝╚╝- - - - - - - - - - - - - - - - - - - -]\n"+
				"[===================================================================================================]");
		}
	}

	public static void displayHelp() {
		displayHeaderHelp();
		System.out.println(langOutputs[91][currentLanguange]);

		EnterForContinue();
	}

	public static void defaultCaseMenu() {
		ClearScreen();
		System.out.println(
			"[===================================================================================================]\n"+
			"[                      "+red+"[!]  " + langOutputs[13][currentLanguange]+ "     [!]"+reset+"                       ]\n" +
			"[                                    "+red+langOutputs[14][currentLanguange]+reset+ "                                       ]\n" +
			"[===================================================================================================]");
	}

	public static String formattedLocalDate() {
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedDate = currentDate.format(dateFormatter);
		return formattedDate;
	}

	public static String formattedLocalDay() {
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH);
		String formattedDay = currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toUpperCase();
		return adjustNumCharDay(formattedDay);
	}

	public static String adjustNumCharDay(String myString) {
		if (myString.equals("SUNDAY") || myString.equals("MONDAY") || myString.equals("FRIDAY")) {
			myString = " " + myString + "  ";
		} else if (myString.equals("TUESDAY")) {
			myString = " " + myString + " ";
		} else if (myString.equals("THURSDAY") || myString.equals("SATURDAY")) {
			myString = myString + " ";
		}
		return myString;
	}

	public static String formattedLocalTime() {
		LocalTime currentTime = LocalTime.now();
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		String formattedTime = currentTime.format(timeFormatter);
		return formattedTime;
	}

	public static void displayTransactionOverLimit() {
		ClearScreen();
		System.out.println(langOutputs[78][currentLanguange]);
	}

	public static void viewBalanceIsNotEnough() {
		ClearScreen();
		System.out.println(
			"[===================================================================================================]\n"+
			"[                    "+red+langOutputs[79][currentLanguange]+reset+"                     ]\n"+
			"[===================================================================================================]"
		);
	}

	public static void viewWrongPin() {
		ClearScreen();
		System.out.println(
			"[===================================================================================================]\n"+
			"[                                        "+red+langOutputs[80][currentLanguange]+reset+"                                        ]\n"+
			"[===================================================================================================]"
		);
	}

	public static void viewTransactionSuccess() {
		ClearScreen();
		System.out.println(
			"[===================================================================================================]\n"+
			langOutputs[81][currentLanguange]+
			"[===================================================================================================]"
		);
	}

	public static void viewTransactionCancelled() {
		ClearScreen();
		System.out.println(
			"[===================================================================================================]\n"+
			"[                                  "+red+langOutputs[82][currentLanguange]+reset+"                                  ]\n"+
			"[===================================================================================================]"
		);
	}

	public static void viewPaymentCodeInvalid() {
		ClearScreen();
		System.out.println(
			"[===================================================================================================]\n"+
			red+langOutputs[83][currentLanguange]+reset+
			"[===================================================================================================]"
		);
	}

	public static void viewPaymentCodeAlreadyUSe() {
		ClearScreen();
		System.out.println(
			"[===================================================================================================]\n"+
			red+langOutputs[84][currentLanguange]+reset+
			"[===================================================================================================]"
		);
	}

	public static void displayClosing() {
		ClearScreen();
		System.out.println(
			"[===================================================================================================]\n"+
			langOutputs[90][currentLanguange]+
			"[===================================================================================================]\n"+
			"\n" +
			"       -------------------------------------------------  \n"+
			"      [   This program is made by:                      ] \n"+
			"      [   The students of State Polytechnic of Malang   ] \n"+
			"      [   - Atabik Mutawakilalallah                     ] \n"+
			"      [   - Farrel Augusta Dinata                       ] \n"+
			"      [   - Innama Maesa Putri                          ] \n"+
			"      [   @2023                                         ] \n"+
			"       ------------------------------------------------- "
			);
	}

	public static boolean UserConfirmation() {
		boolean isLoopConfirm = false;
		boolean isValueTrue = false;
		do {
			System.out.print(
				"[===================================================================================================]\n"+
				"[                                                                                                   ]\n"+
				"[                    [1] "+langOutputs[11][currentLanguange]+"                                           [2] "+langOutputs[12][currentLanguange]+"                    ]\n"+
				"[                                                                                                   ]\n"+
				"[===================================================================================================]\n"+
				"[  ==> ");
			userConfirmChar = scanner2.next().charAt(0);
			ClearScreen();
			switch (userConfirmChar) {
				case '1':
					isValueTrue = true;
					isLoopConfirm = false;
					break;
				case '2':
					isValueTrue = false;
					isLoopConfirm = false;
					break;
				default:
					defaultCaseMenu();
					EnterForContinue();
					isLoopConfirm = true;
					break;
			}
		} while (isLoopConfirm);
		return isValueTrue;
	}

	public static boolean PinValidation() {
		Scanner scannerPin = new Scanner(System.in);
		System.out.print("[  "+langOutputs[86][currentLanguange]);
		inputPin = scannerPin.nextLine();
		ClearScreen();
		if (inputPin.equals(accountData[accountLineIndex][1])) {
			return true;
		} else {
			return false;
		}
	}

	public static void EnterForContinue() {
		System.out.print("[  "+langOutputs[87][currentLanguange]);
		scanner5.nextLine();
		ClearScreen();
	}

	public static void ClearScreen() {
		System.out.println("\033[H\033[2J");
		System.out.flush();
	}

	public static int validateNonNegativeIntegerInput(String prompt) {
		int userInput;

		do {
			System.out.print(prompt);

			while (!scanner3.hasNextInt()) {
				System.out.println("[  Input yang diberikan tidak valid. Silahkan ulangi kembali");
				System.out.print(prompt);
				scanner3.next();
			}

			userInput = scanner3.nextInt();

			if (userInput < 0) {
				System.out.println("Nilai harus lebih besar atau sama dengan 0");
			}
		} while (userInput < 0);

		return userInput;
	}

	public static String getValidatedPin(Scanner scanner, int x) {
		String pin;
		do {
			if (x == 1) {
				System.out.print("[  " + langOutputs[72][currentLanguange]);
			} else if (x == 2) {
				System.out.print("[  " + langOutputs[73][currentLanguange]);
			}

			pin = scanner.nextLine();
			if (pin.equals(accountData[accountLineIndex][1])) {
				ClearScreen();
				System.out.println(
					"[===================================================================================================]\n"+
					"[                                 "+red+langOutputs[93][currentLanguange]+reset+"                                  ]\n"+
					"[===================================================================================================]"
				);
				EnterForContinue();
			} else {
				if (!pin.matches("\\d{4}")) {
					ClearScreen();
					System.out.println(
						"[===================================================================================================]\n"+
						"[                       "+red+langOutputs[92][currentLanguange]+reset+"                      ]\n"+
						"[===================================================================================================]"
					);
					EnterForContinue();
				}
			}
		} while (!pin.matches("\\d{4}") || (pin.equals(accountData[accountLineIndex][1])));
		return pin;
	}

}