// Program sistem ATM - Kelompok 4 - TI-1B
// Fitur yang tersedia: autentifikasi pengguna, transfer, tarik tunai, setor tunai, 
// pembayaran lain-lain, riwayat transaksi, cek saldo, ubah PIN, dan EXIT

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
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
			{ "1234567", "1234", "7000000", "aman" },
			{ "7654321", "5678", "4000000", "aman" },
			{ "7777777", "7777", "10000000", "aman" },
			{ "0000000", "0000", "900000000", "aman" },
			{ "1", "1", "10000000", "aman" }, // for quick try
			{ "2", "2", "22222222", "aman" },
	};

	// array listrikData menampung ID PLN & tagihan
	static int[][] listrikData = {
			{ 123123123, 100000 },
			{ 123456789, 70000 },
			{ 333444555, 80000 },
	};

	// array pendidikanData menampung virtual account (VA) & tagihan
	static int[][] pendidikanData = {
			{ 232323, 1000000 },
			{ 454545, 2500000 },
			{ 909090, 5000000 },
	};

	// array tagihan pada pdam menampung virtual account (VA) & tagihan
	static int[][] tagihanAirData = {
			{ 232323, 100000 },
			{ 454545, 250000 },
			{ 909090, 500000 },
	};

	// array tagihan pada BPJS menampung virtual accounr (VA) & tagihan
	static int[][] BPJSdata = {
			{ 232323, 125000 },
			{ 454545, 250000 },
			{ 909090, 500000 },
	};

	// 'Login' feature variables
	static String inputUser_AccountNumber;
	static int accountLineIndex = 0, loginAttempts = 1;
	static boolean isAccountValid = false, isAccountNumberValid = false,
			isAccountFind = false;
	static final int MAX_LOGIN_ATTEMPTS = 3;

	// 'Transfer' feature variables
	static int transferAmount;
	static String inputTarget_AccountNumber;
	static boolean isTargetAccountValid = false;

	// 'Tarik tunai' variables
	static int cashWithdrawalAmount;

	// 'Setor tunai' variables
	static int cashDepositAmount;

	// 'Cek saldo' variables
	static int userBalance = Integer.parseInt(accountData[accountLineIndex][2]);

	// 'Riwayat transaksi' features variables
	static ArrayList<String> transactionHistory = new ArrayList<>();

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
			userConfirmation;
	static boolean isContinueTransaction = false;

	// 'Validasi PIN' variables
	static String inputPin;

	// Formatting time and date to the system
	static LocalDateTime currentDateTime = LocalDateTime.now();
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
			"dd-MM-yyyy                                                                         HH:mm:ss");
	static String formattedDateTime = currentDateTime.format(formatter);

	// Variables for choose languange feature
	static int currentLanguange = 0;
	static String[][] langOutputs = {
			{ "  PLEASE SELECT A MENU BELOW   ", "SILAKAN PILIH MENU DI BAWAH INI" }, // 0
			{ "TRANSFER", "TRANSFER" }, // 1
			{ "CASH WITHDRAWAL", "TARIK TUNAI    " }, // 2
			{ "CASH DEPOSIT", "SETOR TUNAI " }, // 3
			{ "PAYMENT   ", "PEMBAYARAN" }, // 4
			{ "HISTORY", "RIWAYAT" }, // 5
			{ "BALANCE INQUIRY", "CEK SALDO      " }, // 6
			{ "CHANGE PIN", "UBAH PIN  " }, // 7
			{ "EXIT  ", "KELUAR" }, // 8
			{ "HELP", "BANTUAN" }, // 9
			{ "ANOTHER ACTION?", "TINDAKAN LAIN?" }, // 10 NEW
			{ "YES", "YA " }, // 11 
			{ "NO   ", "TIDAK" }, // 12
			{ "[                                 INVALID INPUT. OPTION NOT AVAILABLE                             ]\n",
			  "[                                 INVALID INPUT. PILIHAN TIDAK TERSEDIA                             ]\n" }, // 13
			{ "Enter the destination account number : ", "Masukkan nomor rekening tujuan : "}, // 14
			{ "input amount = ", "Masukkan nominal transfer : Rp " }, // 15
			{ "$$$  - Detail TRANSFER - $$$",
			 "$$$  - RINCIAN TRANSFER - $$$" }, // 16 
			{ "Account : %s\t\t\n", "Rekening tujuan: %s\t\t\n"}, // 17
			{ "Amount  : %s\t\t\t\n", "Nominal transfer: %s\t\t\t\n"}, // 18
			{ "Your bank balance : ", "Sisa saldo anda : " }, // 19 
			{ "[  (!) Failed Transaction. Invalid Account(!)  ]",
			  "[  (!) Transaksi gagal. Nomor rekening tujuan invalid (!)  ]" }, // 20 new
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
		"[    " + formattedDateTime + "    ]\n" +
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
		"[     @:-@@@@@. ***####%%####*** . @@@@@-:@                    FOR SAFETY AND COMFORT               ]\n"+
		"[      @:.@@@@@ ################ @@@@@.:@                PLEASE CHANGE YOUR PIN REGULARLY           ]\n"+
		"[       @: @@@@@@+:.  ----  .:+@@@@@@ :@                 ________________________________           ]\n"+
		"[         @:* @@@ =@@@@@@@@@@@= @@@ :@                     DEMI KEAMANAN DAN KENYAMANAN             ]\n"+
		"[          @*  :*@@@@@@@@@@@@@@*:  *@                         SILAKAN GANTI PIN ANDA                ]\n"+
		"[              @@@#+-:....:-+#@@@                                 SECARA BERKALA                    ]\n"+
		"[                                                                                                   ]\n"+
		"[===================================================================================================]"
		);
	}

	public static boolean Login() {
		loginAttempts = 0;
		boolean tryToLogin = false;

		do {
			System.out.print("[  ACCOUNT NUMBER: ");
			inputUser_AccountNumber = scanner1.nextLine();

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
				"[===================================================================================================]\n"
				);
				EnterForContinue();
				ClearScreen();
				return false;
			}

			isAccountNumberValid = false;
			if (isAccountFind) {
				if (accountData[accountLineIndex][3].equals("aman")) {
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
						"[           ______________                          [  " +loginAttempts+ "  ]    /    [  " + MAX_LOGIN_ATTEMPTS + "  ]                         ]\n" +
						"[           PERCOBAAN MASUK                         [=====]   /     [=====]                         ]\n"+
						"[===================================================================================================]"
						);
						EnterForContinue();
						ClearScreen();
					}
				}
			} else {
				tryToLogin = true;
				ClearScreen();
				System.out.println(
				"[===================================================================================================]\n"+
				"[           YOUR ACCOUNT (" +inputUser_AccountNumber+ ") HAS BEEN BLOCKED. PLEASE INPUT ANOTHER ACCOUNT NUMBER            ]\n"+
				"[  [!] _______________________________________________________________________________________ [!]  ]\n"+
				"[      NOMOR REKENING ANDA (" +inputUser_AccountNumber+ ") TELAH DIBLOKIR. SILAKAN MASUKKAN NOMOR REKENING YANG LAIN      ]\n"+
				"[===================================================================================================]"
				);
				EnterForContinue();
				ClearScreen();
				return false;
			}

			// If the maximum login attempts are reached and status akun will change to
			// "TERBLOKIR"
			if (loginAttempts > MAX_LOGIN_ATTEMPTS) {
				System.out.println(
				"[===================================================================================================]\n"+
				"[          YOU HAVE INPUT YOUR PIN INCORRECTLY 3 TIMES. SORRY, WE HAVE BLOCKED YOUR ACCOUNT         ]\n"+
				"[    [!]   ________________________________________________________________________________   [!]   ]\n"+
				"[   ANDA TELAH SALAH MEMASUKKAN PIN SEBANYAK 3 KALI. MOHON MAAF, NOMOR REKENING ANDA KAMI BLOKIR    ]\n"+
				"[===================================================================================================]"
				);
				accountData[accountLineIndex][3] = "TERBLOKIR";
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
		"[===================================================================================================]"
		);
	}

	public static void chooseLanguange() {
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
			"[===================================================================================================]"
		);
		System.out.print("[  ==> ");
		currentLanguange = scanner1.nextInt();
		currentLanguange -= 1;
	}

	public static void Menu() {
		// Perulangan menu berdasarkan continueTransaction user
		do {
			isGoToMainMenu = false;
			ClearScreen();
			System.out.println(
				"[===================================================================================================]\n"+
				"[                                    " + langOutputs[0][currentLanguange]+ "                                ]\n" +
				"[===================================================================================================]\n"+
				"[                                                                                                   ]\n"+
				"[                            [1] " + langOutputs[1][currentLanguange] + "                  "+ "[5] " + langOutputs[5][currentLanguange] + "                              ]\n" +
				"[                                                                                                   ]\n"+
				"[                            [2] " + langOutputs[2][currentLanguange] + "           " + "[6] "+ langOutputs[6][currentLanguange] + "                      ]\n" +
				"[                                                                                                   ]\n"+
				"[                            [3] " + langOutputs[3][currentLanguange] + "              " + "[7] "+ langOutputs[7][currentLanguange] + "                           ]\n" +
				"[                                                                                                   ]\n"+
				"[                            [4] " + langOutputs[4][currentLanguange] + "                " + "[8] "+ langOutputs[8][currentLanguange] + "                               ]\n" +
				"[                                                                                                   ]\n"+
				"[                                                          [9] " + langOutputs[9][currentLanguange]+ "      	                    ]\n" +
				"[                                                                                                   ]\n"+
				"[===================================================================================================]"
			);
			System.out.print("[  ==> ");
			int userChoiceMenu = scanner2.nextInt();

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
					RiwayatTransaksi();
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
					String displayMoreTransaction = "[===================================================================================================]\n"
							+
							"[  " + langOutputs[10][currentLanguange]
							+ "                                                                                ]\n" +
							"[                                                                                                   ]\n"
							+
							"[  [1] " + langOutputs[11][currentLanguange]
							+ "                                                                                          ]\n"
							+
							"[                                                                                                   ]\n"
							+
							"[  [2] " + langOutputs[12][currentLanguange]
							+ "                                                                                        ]\n"
							+
							"[                                                                                                   ]\n"
							+
							"[===================================================================================================]";
					System.out.println(displayMoreTransaction);
					boolean isLoopConfirm = false;

					do {
						System.out.print("[  ==> ");
						continueTransaction = scanner2.next().charAt(0);
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
								System.out.println(
										"[===================================================================================================]\n"
												+
												langOutputs[13][currentLanguange] +
												"[===================================================================================================]");
								isLoopConfirm = true;
								break;
						}
					} while (isLoopConfirm);
				}
			}
		} while (isContinueTransaction);
	}

	public static void displayHeaderTransfer() {
		String displayHeaderTransfer = "[===================================================================================================]\n"
				+
				"[ - - - - - - - - - - - - - - - - - - -╔╦╗╦═╗╔═╗╔╗╔╔═╗╔═╗╔═╗╦═╗- - - - - - - - - - - - - - - - - - -]\n"
				+
				"[ - - - - - - - - - - - - - - - - - - - ║ ╠╦╝╠═╣║║║╚═╗╠╣ ║╣ ╠╦╝- - - - - - - - - - - - - - - - - - -]\n"
				+
				"[ - - - - - - - - - - - - - - - - - - - ╩ ╩╚═╩ ╩╝╚╝╚═╝╚  ╚═╝╩╚═- - - - - - - - - - - - - - - - - - -]\n"
				+
				"[===================================================================================================]";
		System.out.println(displayHeaderTransfer);
	}

	public static void Transfer() {
		displayHeaderTransfer();
		System.out.print("" + langOutputs[14][currentLanguange]);
		inputTarget_AccountNumber = scannerTF.nextLine();
		// Pengecekan apakah nomor rekening tujuan ada di database
		isTargetAccountValid = false;
		for (int i = 0; i < accountData.length; i++) {
			if ((inputTarget_AccountNumber.equals(accountData[i][0]))
					&& (!inputTarget_AccountNumber.equals(inputUser_AccountNumber))) {
				isTargetAccountValid = true;
				break;
			}
		}
		// Kondisi jika isTargetAccountValid true
		if (isTargetAccountValid) {
			// System.out.print("\t-- Masukkan nominal transfer : Rp "); // User input
			// nominal transfer
			transferAmount = validateNonNegativeIntegerInput("" + langOutputs[15][currentLanguange]);
			ClearScreen();
			// Konversi nilai output ke rupiah
			String transferAmountRupiah = currencyFormat.format(transferAmount);
			System.out.println(
					"    ============================================================================================");
			System.out.println("    [  _______________________________________________  ]");
			System.out.println(langOutputs[16][currentLanguange]);
			System.out.printf(langOutputs[17][currentLanguange], inputTarget_AccountNumber);
			System.out.printf(langOutputs[18][currentLanguange], transferAmountRupiah);
			System.out.println("    [ ------------------------------------------------- ]");
			System.out.println(
					"    ============================================================================================");
			// Konfirmasi persetujuan transaksi
			UserConfirmation();
			ClearScreen();
			System.out.println(
					"    ============================================================================================");

			// Konfirmasi transaksi
			if (userConfirmation == 'y' || userConfirmation == 'Y') {
				// Pengecekan apakah input PIN sesuai dengan database
				if (PinValidation()) {
					if (transferAmount < userBalance) {
						userBalance -= transferAmount; // Pengurangan saldo pengguna dengan
						// nominal
						// transfer

						// Formatting penulisan rupiah pada output
						String userBalanceRupiah = currencyFormat.format(userBalance);
						viewTransactionSuccess();
						System.out.println("============================================================================================");
						System.out.println("[  _______________________________________________  ]");
						System.out.println(langOutputs[16][currentLanguange]);
						System.out.printf(langOutputs[17][currentLanguange], inputTarget_AccountNumber);
						System.out.printf(langOutputs[18][currentLanguange], transferAmountRupiah);
						System.out.println(langOutputs[19][currentLanguange] + userBalanceRupiah); // your remaining
						System.out.println("============================================================================================");
						EnterForContinue();
						ClearScreen();
						isTargetAccountValid = false;
						// Pencatatan riwawayat transaksi
						transactionHistory.add("Telah melakukan transaksi ke rekening: " + inputTarget_AccountNumber + " sebesar " + transferAmountRupiah);
					} else {
						// Kondisi jika nominal transfer melebihi jumlah saldo
						viewBalanceIsNotEnough();
					}
				} else {
					// Kondisi jika pengguna input PIN tidak sesuai dengan array accountData
					viewWrongPin();
				}
			} else {
				// Kondisi jika pengguna input 't' atau 'T'
				viewTransactionCancelled();
			}
		} else {
			// Kondisi jika isTargetAccountValid bernilai FALSE
			ClearScreen();
			System.out.println(
					"    ============================================================================================");
			System.out.println(
					"    --------------------------------------------------------------------------------------------");
			System.out.println(
					red + langOutputs[20][currentLanguange]
							+ reset);
			System.out.println(
					"    --------------------------------------------------------------------------------------------");
			System.out.println(
					"    ============================================================================================");
		}
	}

	public static void displayHeaderCashWithdrawal() {
		String displayHeaderCashWithdrawal = "[===================================================================================================]\n"
				+
				"[- - - - - - - - - - - - - - - - - - -╔╦╗╔═╗╦═╗╦╦╔═  ╔╦╗╦ ╦╔╗╔╔═╗╦ - - - - - - - - - - - - - - - - -]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - ║ ╠═╣╠╦╝║╠╩╗   ║ ║ ║║║║╠═╣║ - - - - - - - - - - - - - - - - -]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - ╩ ╩ ╩╩╚═╩╩ ╩   ╩ ╚═╝╝╚╝╩ ╩╩ - - - - - - - - - - - - - - - - -]\n"
				+
				"[===================================================================================================]";
		System.out.println(displayHeaderCashWithdrawal);
	}

	public static void TarikTunai() {
		displayHeaderCashWithdrawal();
		// System.out.print("\t-- Masukkan nominal tarik tunai : Rp "); // User input
		// nominal tarik
		// tunai
		cashWithdrawalAmount = validateNonNegativeIntegerInput("\t-- Masukkan nominal tarik tunai : Rp ");

		ClearScreen();

		System.out.println(
				"    ============================================================================================");
		// Konversi nilai output ke Rupiah
		String cashWitdrawalRupiah = currencyFormat.format(cashWithdrawalAmount);
		System.out.println("\t-- Konfirmasi Tarik tunai dengan nominal " + cashWitdrawalRupiah + " ? ");
		// Persetujuan konfirmasi transaksi
		UserConfirmation();
		ClearScreen();
		if (userConfirmation == 'y' || userConfirmation == 'Y') {
			// Pengecekan apakah input PIN sesuai dengan database
			if (PinValidation()) {
				if (cashWithdrawalAmount < userBalance) {
					// Pengecekan apakah nominal tarik kurang dari saldo pengguna
					if (cashWithdrawalAmount <= 5000000) {
						// Kondisi jika cashWithdrawalAmount < userBalance dan
						// cashWithdrawalAmount <= 5000000
						userBalance -= cashWithdrawalAmount;
						// Formating penulisan rupiah pada output
						String userBalanceRupiah = currencyFormat.format(userBalance);
						viewTransactionSuccess();
						System.out.println("\t-- Sisa saldo anda : " + userBalanceRupiah);
						System.out.println(
								"    ============================================================================================");

						// Pencatatan riwayat transaksi
						transactionHistory.add("Telah melakukan tarik tunai sebesar "
								+ cashWitdrawalRupiah);

						EnterForContinue();

						ClearScreen();
					} else {
						// Kondisi jika nominal Tarik > 5.000.000
						System.out.println(
								"    ============================================================================================");
						System.out.println(
								"    --------------------------------------------------------------------------------------------");
						System.out.println(
								"           [  (!) Transaksi gagal, anda melebihi batas maksimum nominal tarik tunai (!)  ]");
						System.out.println(
								"    --------------------------------------------------------------------------------------------");
						System.out.println(
								"    ============================================================================================");
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

	public static void displayHeaderCashDeposit() {
		String displayHeaderCashDeposit = "[===================================================================================================]\n"
				+
				"[ - - - - - - - - - - - - - - - - - -╔═╗╔═╗╔╦╗╔═╗╦═╗  ╔╦╗╦ ╦╔╗╔╔═╗╦- - - - - - - - - - - - - - - - -]\n"
				+
				"[ - - - - - - - - - - - - - - - - - -╚═╗║╣  ║ ║ ║╠╦╝   ║ ║ ║║║║╠═╣║- - - - - - - - - - - - - - - - -]\n"
				+
				"[ - - - - - - - - - - - - - - - - - -╚═╝╚═╝ ╩ ╚═╝╩╚═   ╩ ╚═╝╝╚╝╩ ╩╩- - - - - - - - - - - - - - - - -]\n"
				+
				"[===================================================================================================]";
		System.out.println(displayHeaderCashDeposit);
	}

	public static void SetorTunai() {
		displayHeaderCashDeposit();
		// System.out.print("\t-- Masukkan nominal setor tunai : Rp ");
		cashDepositAmount = validateNonNegativeIntegerInput("\t-- Masukkan nominal setor tunai : Rp ");

		ClearScreen();

		System.out.println(
				"    ============================================================================================");
		String cashDepositRupiah = currencyFormat.format(cashDepositAmount);
		System.out.println("\t-- Konfirmasi setor tunai dengan nominal " + cashDepositRupiah + " ? ");
		UserConfirmation();

		ClearScreen();
		if (userConfirmation == 'y' || userConfirmation == 'Y') {
			ClearScreen();
			if (PinValidation()) {
				userBalance += cashDepositAmount; // Penjumlahan saldo dengan nominal setor yang telah
				// dilakukan
				viewTransactionSuccess();
				// Formatting penulisan rupiah pada output
				String userBalanceRupiah = currencyFormat.format(userBalance);

				System.out.println("\t-- Sisa saldo anda : " + userBalanceRupiah);
				System.out.println(
						"    ============================================================================================");

				// Pencatatan riwayat transaksi
				transactionHistory.add("Telah melakukan setor tunai sebesar " + cashDepositRupiah);

				EnterForContinue();

				ClearScreen();
			} else {
				viewWrongPin();
			}
		} else {
			viewTransactionCancelled();
		}
	}

	public static void displayHeaderPayments() {
		String displayHeaderPayments = "[===================================================================================================]\n"
				+
				"[- - - - - - - - - - - -╔═╗╔═╗╔╦╗╔╗ ╔═╗╦ ╦╔═╗╦═╗╔═╗╔╗╔  ╦  ╔═╗╦╔╗╔╔╗╔╦ ╦╔═╗- - - - - - - - - - - - -]\n"
				+
				"[- - - - - - - - - - - -╠═╝║╣ ║║║╠╩╗╠═╣╚╦╝╠═╣╠╦╝╠═╣║║║  ║  ╠═╣║║║║║║║╚╦╝╠═╣- - - - - - - - - - - - -]\n"
				+
				"[- - - - - - - - - - - -╩  ╚═╝╩ ╩╚═╝╩ ╩ ╩ ╩ ╩╩╚═╩ ╩╝╚╝  ╩═╝╩ ╩╩╝╚╝╝╚╝ ╩ ╩ ╩- - - - - - - - - - - - -]\n"
				+
				"[===================================================================================================]";
		System.out.println(displayHeaderPayments);
	}

	public static void PembayaranLainnya() {
		displayHeaderPayments();
		System.out.println(
				"[===================================================================================================]\n"
						+
						"[                            SILAKAN PILIH TRANSAKSI PEMBAYARAN BERIKUT                             ]\n"
						+
						"[===================================================================================================]\n"
						+
						"[                       [1] PULSA                              [4] PDAM                             ]\n"
						+
						"[                                                                                                   ]\n"
						+
						"[                       [2] LISTRIK                            [5] BPJS                             ]\n"
						+
						"[                                                                                                   ]\n"
						+
						"[                       [3] PENDIDIKAN                         [6] KEMBALI KE                       ]\n"
						+
						"[                                                                  MENU UTAMA                       ]\n"
						+
						"[===================================================================================================]");
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
		String displayHeaderPulsa = "[===================================================================================================]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - - - -╔═╗╦ ╦╦  ╔═╗╔═╗ - - - - - - - - - - - - - - - - - - - - ]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - - - -╠═╝║ ║║  ╚═╗╠═╣ - - - - - - - - - - - - - - - - - - - - ]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - - - -╩  ╚═╝╩═╝╚═╝╩ ╩ - - - - - - - - - - - - - - - - - - - - ]\n"
				+
				"[===================================================================================================]";
		System.out.println(displayHeaderPulsa);
	}

	public static void Pulsa() {
		displayHeaderPulsa();
		String nomorTelepon;
		int nomPulsa;
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    [ Pilih operator seluler:                                                                  ]");
		System.out.println(
				"    [  ____________                                                                            ]");
		System.out.println(
				"    [ |1._Indosat__|                                                                           ]");
		System.out.println(
				"    [  ____________                                                                            ]");
		System.out.println(
				"    [ |2._XL_______|                                                                           ]");
		System.out.println(
				"    [  _____________                                                                           ]");
		System.out.println(
				"    [ |3._Telkomsel_|                                                                          ]");
		System.out.println(
				"    ============================================================================================");
		System.out.print("\t-- Operator yang anda pilih: ");
		String operatorPulsa = scanner2.next();
		boolean isOperatorValid = false;
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
				System.out.println("Operator yang dipilih invalid!");
				break;
		}

		if (isOperatorValid) {
			Scanner scannerPulsa = new Scanner(System.in);
			System.out.print("\t-- Input nomor telepon anda: "); // User input nomor telepon
			nomorTelepon = scannerPulsa.nextLine();
			scanner1.nextLine();
			// System.out.print("\t-- Input nominal pulsa: Rp "); // User input nominal
			// pulsa
			nomPulsa = validateNonNegativeIntegerInput("\t-- Input nominal pulsa: Rp ");
			int totalPayment = nomPulsa + adminFee;
			String totalPaymentRp = currencyFormat.format(totalPayment);
			ClearScreen();
			System.out.println(
					"    ============================================================================================");
			// Konversi nilai nomPulsa ke rupiah
			String nomPulsaRP = currencyFormat.format(nomPulsa);
			// Menampilkan informasi transaksi sementara
			System.out
					.println("    [  _________________________________________________\t]");
			System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t|\t]");
			System.out.printf("    [ |  Operator seluler\t: %s\t\t|\t]\n", operatorPulsa);
			System.out.printf("    [ |  Nomor telepon\t\t: %s\t\t|\t]\n", nomorTelepon);
			System.out.printf("    [ |  Nominal pulsa\t\t: %s\t\t|\t]\n", nomPulsaRP);
			System.out.printf("    [ |  Biaya admin\t\t: %s\t\t|\t]\n", adminFeeRp);
			System.out
					.println("    [  -------------------------------------------------\t]");
			System.out.println(
					"    ============================================================================================");
			System.out.println("-- Konfirmasi transaksi ?");
			UserConfirmation();
			ClearScreen();
			if (userConfirmation == 'Y' || userConfirmation == 'y') {
				ClearScreen();
				if (PinValidation()) {
					if (nomPulsa < userBalance) {
						userBalance -= totalPayment;
						String saldoRupiah2 = currencyFormat.format(userBalance);

						// Menampilkan output transaksi berhasil
						viewTransactionSuccess();
						System.out.println(
								"    [  _________________________________________________\t]");
						System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t|\t]");
						System.out.printf("    [ |  Operator seluler\t: %s\t\t|\t]\n",
								operatorPulsa);
						System.out.printf("    [ |  Nomor telepon\t\t: %s\t\t|\t]\n",
								nomorTelepon);
						System.out.printf("    [ |  Nominal pulsa\t\t: %s\t\t|\t]\n",
								nomPulsaRP);
						System.out.printf("    [ |  Biaya admin\t\t: %s\t\t|\t]\n", adminFeeRp);
						System.out.printf("    [ |  Sisa saldo anda\t: %s\t|\t]\n",
								saldoRupiah2);
						System.out.println(
								"    [  -------------------------------------------------\t]");
						System.out.println(
								"    ============================================================================================");
						// Pencatatan riwayat transaksi
						transactionHistory.add("Telah melakukan pembelian pulsa ke nomor "
								+ nomorTelepon + " sebesar " + totalPaymentRp);

						EnterForContinue();
						ClearScreen();
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
		String displayHeaderElectricity = "[===================================================================================================]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - - - ╦  ╦╔═╗╔╦╗╦═╗╦╦╔═ - - - - - - - - - - - - - - - - - - - -]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - - - ║  ║╚═╗ ║ ╠╦╝║╠╩╗ - - - - - - - - - - - - - - - - - - - -]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - - - ╩═╝╩╚═╝ ╩ ╩╚═╩╩ ╩ - - - - - - - - - - - - - - - - - - - -]\n"
				+
				"[===================================================================================================]";
		System.out.println(displayHeaderElectricity);
	}

	public static void Listrik() {
		displayHeaderElectricity();
		System.out.print("\t-- Masukkan ID pelanggan PLN/Nomor meter: ");
		int inputPLN = scanner4.nextInt();
		ClearScreen();
		// Pengecekan data ID pelanggan
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
			// Formatting output ke Rupiah
			String tagihanListrikRP = currencyFormat.format(listrikData[indexListrik][1]);
			int totalPayment = listrikData[indexListrik][1] + adminFee;
			String totalPaymentRp = currencyFormat.format(totalPayment);
			System.out.println(
					"    ============================================================================================");
			System.out.println(
					"    [  _________________________________________________________\t]");
			System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
			System.out.printf("    [ |  ID PLN\t\t\t: %d\t\t\t|\t]\n", inputPLN);
			System.out.printf("    [ |  Tagihan listrik\t\t: %s\t\t\t|\t]\n", tagihanListrikRP);
			System.out.printf("    [ |  Biaya admin\t\t: %s\t\t\t|\t]\n", adminFeeRp);
			System.out.println(
					"    [  ---------------------------------------------------------\t]");
			System.out.println(
					"    ============================================================================================");
			System.out.println("-- Konfirmasi transaksi ?");
			UserConfirmation();
			ClearScreen();
			if (userConfirmation == 'Y' || userConfirmation == 'y') {
				if (PinValidation()) {
					if (listrikData[indexListrik][1] < userBalance) {
						userBalance -= totalPayment;
						// Formatting saldo pengguna ke Rupiah
						String saldoRupiah3 = currencyFormat.format(userBalance);
						viewTransactionSuccess();
						System.out.println("    [  _________________________________________________________\t]");
						System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
						System.out.printf("    [ |  ID PLN\t\t\t: %d\t\t\t|\t]\n", inputPLN);
						System.out.printf("    [ |  Tagihan listrik\t\t: %s\t\t\t|\t]\n", tagihanListrikRP);
						System.out.printf("    [ |  Biaya admin\t\t: %s\t\t\t|\t]\n", adminFeeRp);
						System.out.printf("    [ |  Sisa saldo anda\t: %s\t\t|\t]\n", saldoRupiah3);
						System.out.println(
								"    [  ---------------------------------------------------------\t]");
						System.out.println(
								"    ============================================================================================");

						// Pencatatan riwayat transaksi
						transactionHistory.add(
								"Telah melakukan pembayaran tagihan listrik sebesar "
										+ totalPaymentRp);

						EnterForContinue();
						ClearScreen();
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
			viewPaymentCodeInvalid();
		}
	}

	public static void displayHeaderEducationBill() {
		String displayHeaderEducationBill = "[===================================================================================================]\n"
				+
				"[- - - - - - - - - - - - - - - - - - -╔═╗╔═╗╔╗╔╔╦╗╦╔╦╗╦╦╔═╔═╗╔╗╔ - - - - - - - - - - - - - - - - - -]\n"
				+
				"[- - - - - - - - - - - - - - - - - - -╠═╝║╣ ║║║ ║║║ ║║║╠╩╗╠═╣║║║ - - - - - - - - - - - - - - - - - -]\n"
				+
				"[- - - - - - - - - - - - - - - - - - -╩  ╚═╝╝╚╝═╩╝╩═╩╝╩╩ ╩╩ ╩╝╚╝ - - - - - - - - - - - - - - - - - -]\n"
				+
				"[===================================================================================================]";
		System.out.println(displayHeaderEducationBill);
	}

	public static void Pendidikan() {
		displayHeaderEducationBill();
		System.out.print("\t-- Masukkan nomor virtual account : ");
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
			String tagihanPendidikanRP = currencyFormat.format(pendidikanData[indexPendidikan][1]);
			int totalPayment = pendidikanData[indexPendidikan][1] + adminFee;
			String totalPaymentRp = currencyFormat.format(totalPayment);
			System.out.println("    ============================================================================================");
			System.out.println("    [  _________________________________________________________\t]");
			System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
			System.out.printf("    [ |  Nomor VA\t\t: %d\t\t\t|\t]\n", inputVA);
			System.out.printf("    [ |  Tagihan Pendidikan\t\t: %s\t\t|\t]\n", tagihanPendidikanRP);
			System.out.printf("    [ |  Biaya admin\t\t: %s\t\t\t|\t]\n", adminFeeRp);
			System.out.println("    [  ---------------------------------------------------------\t]");
			System.out.println("    ============================================================================================");
			System.out.println("-- Konfirmasi transaksi ?");
			UserConfirmation();
			ClearScreen();
			if (userConfirmation == 'Y' || userConfirmation == 'y') {
				if (PinValidation()) {
					if (pendidikanData[indexPendidikan][1] < userBalance) {
						userBalance -= totalPayment;
						// Formatting output ke Rupiah
						String saldoRupiah3 = currencyFormat.format(userBalance);
						viewTransactionSuccess();
						System.out.println("    [  _________________________________________________________\t]");
						System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
						System.out.printf("    [ |  Nomor VA\t\t: %d\t\t\t|\t]\n", inputVA);
						System.out.printf("    [ |  Tagihan Pendidikan\t\t: %s\t\t|\t]\n", tagihanPendidikanRP);
						System.out.printf("    [ |  Biaya admin\t\t: %s\t\t\t|\t]\n", adminFeeRp);						
						System.out.printf("    [ |  Sisa saldo anda\t: %s\t\t|\t]\n", saldoRupiah3);
						System.out.println("    [  ---------------------------------------------------------\t]");
						System.out.println("    ============================================================================================");

						// Pencatatan riwayat transaksi
						transactionHistory.add("Telah melakukan pembayaran tagihan pendidikan sebesar " + tagihanPendidikanRP);

						EnterForContinue();
						ClearScreen();
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
			viewPaymentCodeInvalid();
		}

	}

	public static void displayHeaderWaterBill() {
		String displayHeaderWaterBill = "[===================================================================================================]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - - - - ╔═╗╔╦╗╔═╗╔╦╗- - - - - - - - - - - - - - - - - - - - - -]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - - - - ╠═╝ ║║╠═╣║║║- - - - - - - - - - - - - - - - - - - - - -]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - - - - ╩  ═╩╝╩ ╩╩ ╩- - - - - - - - - - - - - - - - - - - - - -]\n"
				+
				"[===================================================================================================]";
		System.out.println(displayHeaderWaterBill);
	}

	public static void Pdam() {
		displayHeaderWaterBill();
		System.out.print("\t-- Masukkan nomor tagihan : ");
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
			String tagihanPdamRp = currencyFormat.format(tagihanAirData[indexPdam][1]);
			int totalPayment = tagihanAirData[indexPdam][1] + adminFee;
			String totalPaymentRp = currencyFormat.format(totalPayment);
			System.out.println("    ============================================================================================");
			System.out.println("    [  _________________________________________________________\t]");
			System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
			System.out.printf("    [ |  Nomor tagihan\t\t: %d\t\t\t|\t]\n", inputVA);
			System.out.printf("    [ |  Tagihan PDAM\t\t: %s\t\t|\t]\n", tagihanPdamRp);
			System.out.printf("    [ |  Biaya admin\t\t: %s\t\t|\t]\n", adminFeeRp);
			System.out.println("    [  ---------------------------------------------------------\t]");
			System.out.println("    ============================================================================================");
			System.out.println("-- Konfirmasi transaksi ?");
			UserConfirmation();
			ClearScreen();
			if (userConfirmation == 'Y' || userConfirmation == 'y') {
				if (PinValidation()) {
					if (tagihanAirData[indexPdam][1] < userBalance) {
						userBalance -= totalPayment;
						// Formatting output ke Rupiah
						String saldoRupiah3 = currencyFormat.format(userBalance);
						viewTransactionSuccess();
						System.out.println("    [  _________________________________________________________\t]");
						System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
						System.out.printf("    [ |  Nomor VA\t\t: %d\t\t\t|\t]\n", inputVA);
						System.out.printf("    [ |  Tagihan PDAM\t\t: %s\t\t|\t]\n", tagihanPdamRp);
						System.out.printf("    [ | Biaya admin\t\t: %s\t\t|\t]\n", adminFeeRp);
						System.out.printf("    [ |  Sisa saldo anda\t: %s\t\t|\t]\n", saldoRupiah3);
						System.out.println("    [  ---------------------------------------------------------\t]");
						System.out.println("    ============================================================================================");

						transactionHistory.add("Telah melakukan pembayaran tagihan PDAM sebesar " + totalPaymentRp);

						EnterForContinue();
						ClearScreen();
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
			viewPaymentCodeInvalid();
		}
	}

	public static void displayHeaderBpjs() {
		String displayHeaderBpjs = "[===================================================================================================]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - - - - -╔╗ ╔═╗╦╔═╗- - - - - - - - - - - - - - - - - - - - - - ]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - - - - -╠╩╗╠═╝║╚═╗- - - - - - - - - - - - - - - - - - - - - - ]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - - - - -╚═╝╩ ╚╝╚═╝- - - - - - - - - - - - - - - - - - - - - - ]\n"
				+
				"[===================================================================================================]";
		System.out.println(displayHeaderBpjs);
	}

	public static void Bpjs() {
		displayHeaderBpjs();
		System.out.print("\t-- Masukkan nomor tagihan : ");
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
			String tagihanBpjsRp = currencyFormat.format(BPJSdata[indexBpjs][1]);
			int totalPayment = BPJSdata[indexBpjs][1] + adminFee;
			String totalPaymentRp = currencyFormat.format(totalPayment);
			System.out.println("    ============================================================================================");
			System.out.println("    [  _________________________________________________________\t]");
			System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
			System.out.printf("    [ |  Nomor tagihan\t\t: %d\t\t\t|\t]\n", inputVA);
			System.out.printf("    [ |  Tagihan BPJS\t\t: %s\t\t|\t]\n", tagihanBpjsRp);
			System.out.printf("    [ | Biaya admin\t\t: %s\t\t|\t]\n", adminFeeRp);
			System.out.println("    [  ---------------------------------------------------------\t]");
			System.out.println("    ============================================================================================");
			System.out.println("-- Konfirmasi transaksi ?");

			UserConfirmation();
			ClearScreen();
			if (userConfirmation == 'Y' || userConfirmation == 'y') {
				if (PinValidation()) {
					if (BPJSdata[indexBpjs][1] < userBalance) {
						userBalance -= totalPayment;
						// Formatting output ke Rupiah
						String saldoRupiah3 = currencyFormat.format(userBalance);
						viewTransactionSuccess();
						System.out.println("    [  _________________________________________________________\t]");
						System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
						System.out.printf("    [ |  Nomor VA\t\t: %d\t\t\t|\t]\n", inputVA);
						System.out.printf("    [ |  Tagihan BPJS\t\t: %s\t\t|\t]\n", tagihanBpjsRp);
						System.out.printf("    [ | Biaya admin\t\t: %s\t\t|\t]\n", adminFeeRp);
						System.out.printf("    [ |  Sisa saldo anda\t: %s\t\t|\t]\n", saldoRupiah3);
						System.out.println("    [  ---------------------------------------------------------\t]");
						System.out.println("    ============================================================================================");

						// Pencatatan riwayat transaksi
						transactionHistory.add("Telah melakukan pembayaran tagihan BPJS sebesar " + totalPaymentRp);

						EnterForContinue();
						ClearScreen();
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
			viewPaymentCodeInvalid();
		}
	}

	public static void returnToMainMenu() {
		isGoToMainMenu = true;
		isContinueTransaction = true;
	}

	public static void displayHeaderTransactionHistory() {
		String displayHeaderTransactionHistory = "[===================================================================================================]\n"
				+
				"[- - - - - - - - - - - - - ╦═╗╦╦ ╦╔═╗╦ ╦╔═╗╔╦╗  ╔╦╗╦═╗╔═╗╔╗╔╔═╗╔═╗╦╔═╔═╗╦- - - - - - - - - - - - - -]\n"
				+
				"[- - - - - - - - - - - - - ╠╦╝║║║║╠═╣╚╦╝╠═╣ ║    ║ ╠╦╝╠═╣║║║╚═╗╠═╣╠╩╗╚═╗║- - - - - - - - - - - - - -]\n"
				+
				"[- - - - - - - - - - - - - ╩╚═╩╚╩╝╩ ╩ ╩ ╩ ╩ ╩    ╩ ╩╚═╩ ╩╝╚╝╚═╝╩ ╩╩ ╩╚═╝╩- - - - - - - - - - - - - -]\n"
				+
				"[===================================================================================================]";
		System.out.println(displayHeaderTransactionHistory);
	}

	public static void RiwayatTransaksi() {
		displayHeaderTransactionHistory();
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"\t|                         ________________________________                        |");
		System.out.println(
				"\t|                         \\RIWAYAT TRANSAKSI TERBARU ANDA/                        |");
		System.out.println(
				"\t|                          ------------------------------                         |");

		if (transactionHistory.size() > 10) {
			transactionHistory.remove(0);
		}

		for (int i = 0; i < transactionHistory.size(); i++) {
			System.out.printf("\t\t%d. %s%n", (i + 1), transactionHistory.get(i));
		}

		System.out.println(
				"         ---------------------------------------------------------------------------------");
		System.out.println(
				"    ============================================================================================");

		EnterForContinue();
		ClearScreen();
	}

	public static void displayHeaderBalanceInquiry() {
		String displayHeaderBalanceInquiry = "[===================================================================================================]\n"
				+
				"[- - - - - - - - - - - - - - - - - - ╔═╗╔═╗╦╔═  ╔═╗╔═╗╦  ╔╦╗╔═╗ - - - - - - - - - - - - - - - - - - ]\n"
				+
				"[- - - - - - - - - - - - - - - - - - ║  ║╣ ╠╩╗  ╚═╗╠═╣║   ║║║ ║ - - - - - - - - - - - - - - - - - - ]\n"
				+
				"[- - - - - - - - - - - - - - - - - - ╚═╝╚═╝╩ ╩  ╚═╝╩ ╩╩═╝═╩╝╚═╝ - - - - - - - - - - - - - - - - - - ]\n"
				+
				"[===================================================================================================]";
		System.out.println(displayHeaderBalanceInquiry);
	}

	public static void CekSaldo() {
		displayHeaderBalanceInquiry();
		String saldoRupiah3 = currencyFormat.format(userBalance);
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    [            ________________________________________________________________              ]");
		System.out.printf("    [           | Saldo anda sebesar %s\t\t\t\t |\t       ]\n",
				saldoRupiah3);
		System.out.println(
				"    [            ----------------------------------------------------------------              ]");
		System.out.println(
				"    ============================================================================================");

		EnterForContinue();

		ClearScreen();
	}

	public static void displayHeaderChangePin() {
		String displayHeaderChangePin = "[===================================================================================================]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - ╦ ╦╔╗ ╔═╗╦ ╦  ╔═╗╦╔╗╔- - - - - - - - - - - - - - - - - - - - ]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - ║ ║╠╩╗╠═╣╠═╣  ╠═╝║║║║- - - - - - - - - - - - - - - - - - - - ]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - ╚═╝╚═╝╩ ╩╩ ╩  ╩  ╩╝╚╝- - - - - - - - - - - - - - - - - - - - ]\n"
				+
				"[===================================================================================================]";
		System.out.println(displayHeaderChangePin);
	}

	public static void UbahPin() {
		displayHeaderChangePin();
		String userRekening = accountData[accountLineIndex][0];
		System.out.print("    Masukkan PIN anda : "); // Input PIN pengguna
		String inputPin7 = scanner1.nextLine();
		scanner1.nextLine();

		ClearScreen();

		if (inputPin7.equals(inputPin)) {
			System.out.println(
					"    ============================================================================================");
			System.out.print("    Masukkan PIN baru: ");
			String inputNewPin = getValidatedPin(scanner1);
			System.out.print("    Konfirmasi PIN baru: ");
			String confirmedNewPin = getValidatedPin(scanner1);
			if (inputNewPin.equals(confirmedNewPin)) {
				int indeksNoRek = 0;
				accountData[accountLineIndex][1] = confirmedNewPin;
				// transactionHistory[maxTransactionHistory
				// - transactionCount] = "Telah melakukan pengubahan pin";
				// transactionCount--;
				// Menghapus output yang telah ditampilkan
				ClearScreen();

				System.out.println(
						"    ============================================================================================");
				System.out.println(
						"    --------------------------------------------------------------------------------------------");
				System.out.println(
						"    ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~PIN BERHASIL DIRUBAH~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
				System.out.println(
						"    --------------------------------------------------------------------------------------------");
				System.out.println(
						"    ============================================================================================");
			} else {
				System.out.println(
						"    ============================================================================================");
				System.out.println(
						"                          [  (!) PIN BARU DAN KONFIRMASI TIDAK SAMA (!) ]");
				System.out.println(
						"    ============================================================================================");
			}
		} else {
			System.out.println(
					"    ============================================================================================");
			System.out.println(
					"                                    [  (!) PIN TIDAK SESUAI (!)  ]");
			System.out.println(
					"    ============================================================================================");
		}
	}

	public static void displayHeaderExit() {
		String displayHeaderExit = "[===================================================================================================]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - - ╦╔═╔═╗╦  ╦ ╦╔═╗╦═╗ - - - - - - - - - - - - - - - - - - - - ]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - - ╠╩╗║╣ ║  ║ ║╠═╣╠╦╝ - - - - - - - - - - - - - - - - - - - - ]\n"
				+
				"[- - - - - - - - - - - - - - - - - - - - ╩ ╩╚═╝╩═╝╚═╝╩ ╩╩╚═ - - - - - - - - - - - - - - - - - - - - ]\n"
				+
				"[===================================================================================================]";
		System.out.println(displayHeaderExit);
	}

	public static boolean Exit() {
		displayHeaderExit();
		System.out.println("\t-- Apakah anda yakin untuk keluar?");
		UserConfirmation();
		ClearScreen();
		if (userConfirmation == 'Y' || userConfirmation == 'y') {
			displayClosing();
			return isStopTransaction = true;
		} else {
			return isContinueTransaction = true;
		}
	}

	public static void displayHeaderHelp() {
		System.out.println(
				"[===================================================================================================]\n"
						+
						"[- - - - - - - - - - - - - - - - - - - - - -╦ ╦╔═╗╦  ╔═╗- - - - - - - - - - - - - - - - - - - - - - ]\n"
						+
						"[- - - - - - - - - - - - - - - - - - - - - -╠═╣║╣ ║  ╠═╝- - - - - - - - - - - - - - - - - - - - - - ]\n"
						+
						"[- - - - - - - - - - - - - - - - - - - - - -╩ ╩╚═╝╩═╝╩- - - - - - - - - - - - - - - - - - - - - - - ]\n"
						+
						"[===================================================================================================]");
	}

	public static void displayHelp() {
		displayHeaderHelp();
		System.out.println(
				"[===================================================================================================]\n"
						+
						"[                                PETUNJUK MENGGUNAKAN ATM POLINEMA                                  ]\n"
						+
						"[                                                                                                   ]\n"
						+
						"[  Q: Bagaimana cara menggunakannya?                                                                ]\n"
						+
						"[  A: Anda bisa memulai program dengan menekan angka yang sesuai dengan menu                        ]\n"
						+
						"[                                                                                                   ]\n"
						+
						"[  Q: Apakah saya bisa melakukan transaksi lebih dari 1 kali?                                       ]\n"
						+
						"[  A: Bisa. Setelah anda melakukan konfirmasi, akan ada pemberitahuan mengenai konfirmasi transaksi ]\n"
						+
						"[     lagi. Anda bisa memilih menu yang tersedia kembali.                                           ]\n"
						+
						"[                                                                                                   ]\n"
						+
						"[===================================================================================================]");

		EnterForContinue();
		ClearScreen();
	}

	public static void defaultCaseMenu() {
		ClearScreen();
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				red + "                   [  (!) Input tidak sesuai. Periksa kembali inputan anda (!)  ]"
						+ reset);
		System.out.println(
				"    ============================================================================================");
	}

	public static void viewBalanceIsNotEnough() {
		ClearScreen();
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    --------------------------------------------------------------------------------------------");
		System.out.println(
				"                     [  (!) Transaksi gagal. Saldo anda tidak mencukupi (!)  ]");
		System.out.println(
				"    --------------------------------------------------------------------------------------------");
		System.out.println(
				"    ============================================================================================");
	}

	public static void viewWrongPin() {
		ClearScreen();
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    --------------------------------------------------------------------------------------------");
		System.out.println(
				red + "                                      [  (!) PIN SALAH! (!)  ]" + reset);
		System.out.println(
				"    --------------------------------------------------------------------------------------------");
		System.out.println(
				"    ============================================================================================");
	}

	public static void viewTransactionSuccess() {
		ClearScreen();
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    --------------------------------------------------------------------------------------------");
		System.out.println(
				"     ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ TRANSAKSI BERHASIL ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ");
		System.out.println(
				"    --------------------------------------------------------------------------------------------");
		System.out.println(
				"    ============================================================================================");
	}

	public static void viewTransactionCancelled() {
		ClearScreen();
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    --------------------------------------------------------------------------------------------");
		System.out.println(
				red + "                                 [  (!) TRANSAKSI DIBATALKAN (!)  ]" + reset);
		System.out.println(
				"    --------------------------------------------------------------------------------------------");
		System.out.println(
				"    ============================================================================================");
	}

	public static void viewPaymentCodeInvalid() {
		ClearScreen();
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    --------------------------------------------------------------------------------------------");
		System.out.println(
				red + "               [  (!) Kode pembayaran invalid. Silakan input ulang nomor VA anda! (!)  ]"
						+ reset);
		System.out.println(
				"    --------------------------------------------------------------------------------------------");
		System.out.println(
				"    ============================================================================================");
	}

	public static void displayClosing() {
		ClearScreen();
		System.out.println(
				"[===================================================================================================]\n"
						+
						"[~ ~ ~ ~ ~ ~ ~ ~ ~ Terimakasih telah bertransaksi! Semoga harimu selalu bahagia :) ~ ~ ~ ~ ~ ~ ~ ~ ~]\n"
						+
						"[===================================================================================================]\n"
						+
						"\n" +
						"* * * [   This program is made by:                      ] * * * * * * * * * * * * * * * * * * * * * *\n"
						+
						"* * * [   The students of State Polytechnic of Malang   ] * * * * * * * * * * * * * * * * * * * * * *\n"
						+
						"* * * [   - Atabik Mutawakilalallah                     ] * * * * * * * * * * * * * * * * * * * * * *\n"
						+
						"* * * [   - Farrel Augusta Dinata                       ] * * * * * * * * * * * * * * * * * * * * * *\n"
						+
						"* * * [   - Innama Maesa Putri                          ] * * * * * * * * * * * * * * * * * * * * * *\n"
						+
						"* * * [   \u00A92023\n" +
						"[====================================================================================================]");
	}

	public static char UserConfirmation() {
		System.out.println("Konfirmasi transaksi?");
		System.out.print("Tekan 'Y' untuk YA. Tekan 'T' untuk TIDAK --> ");
		userConfirmation = scanner2.next().charAt(0);
		return userConfirmation;
	}

	public static boolean PinValidation() {
		Scanner scannerPin = new Scanner(System.in);
		System.out.print("[  Masukkan PIN anda: ");
		inputPin = scannerPin.nextLine();
		ClearScreen();
		if (inputPin.equals(accountData[accountLineIndex][1])) {
			return true;
		} else {
			return false;
		}
	}

	public static void EnterForContinue() {
		System.out.print("Klik enter untuk melanjutkan --> ");
		scanner5.nextLine();
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
				System.out.println("\t-- Input yang diberikan tidak valid. Silahkan ulangi kembali");
				System.out.print(prompt);
				scanner3.next(); // Hapus input yang tidak valid
			}

			userInput = scanner3.nextInt();

			if (userInput < 0) {
				System.out.println("Nilai harus lebih besar atau sama dengan 0.");
			}
		} while (userInput < 0);

		return userInput;
	}

	public static String getValidatedPin(Scanner scanner) {
		String pin;
		do {
			pin = scanner.nextLine();
			if (!pin.matches("\\d{4}")) {
				System.out.println(
						"    ============================================================================================");
				System.out.println(
						"                          [  (!) PIN HANYA TERDIRI DARI 4 DIGIT ANGKA (!) ]");
				System.out.println(
						"    ============================================================================================");

				System.out.print("    Masukkan PIN baru (4 digit angka): ");
			}
		} while (!pin.matches("\\d{4}"));
		return pin;
	}

}
