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
			{ "1", "1", "10000000", "aman" }// for quick try
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

	// Format nilai uang Indonesia Rupiah (IDR)
	static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

	// Formatting time and date to the system
	static LocalDateTime currentDateTime = LocalDateTime.now();
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
			"dd-MM-yyyy                                                                         HH:mm:ss");
	static String formattedDateTime = currentDateTime.format(formatter);

	// Variables for choose languange feature
	static int currentLanguange = 0;
	static String[][] langOutputs = {
		{"  PLEASE SELECT A MENU BELOW   ", "SILAKAN PILIH MENU DI BAWAH INI"}, //0
		{"TRANSFER", "TRANSFER"}, //1
		{"CASH WITHDRAWAL", "TARIK TUNAI    "}, //2
		{"CASH DEPOSIT", "SETOR TUNAI "}, //3
		{"PAYMENT   ", "PEMBAYARAN"}, //4
		{"HISTORY", "RIWAYAT"}, //5
		{"BALANCE INQUIRY", "CEK SALDO      "}, //6
		{"CHANGE PIN", "UBAH PIN  "}, //7
		{"EXIT  ", "KELUAR"}, //8
		{"ANOTHER TRANSACTION?", "TRANSAKSI LAGI?  "}, //9
		{"YES", "YA "}, //10
		{"NO   ", "TIDAK"},//11
		{"\t-- Enter the destination account number : ", "\t-- Masukkan nomor rekening tujuan : "}, //12
		{"You have made a transfer to an account ", " Telah melakukan transfer ke rekening "},//13
		{"\t-- input amount = ", "\t-- Masukkan nominal transfer : Rp "},//14
		{"Press Enter for the next page ==>","[  Enter untuk melanjutkan ==>  "},//15
		{ "                    [  (!) Failed Transaction. Invalid Account(!)  ]", "                    [  (!) Transaksi gagal. Nomor rekening tujuan invalid (!)  ]"}, //16
		{"    [ |  $$$  - Detail TRANSFER - $$$\t\t      | ]", "    [ |  $$$  - RINCIAN TRANSFER - $$$\t\t      | ]"},//17
		{"    [ |  Account : %s\t\t      | ]\n", "    [ |  Rekening tujuan: %s\t\t      | ]\n"},//18
		{"    [ |  Amount  : %s\t\t\t| ]\n", "    [ |  Nominal transfer: %s\t\t\t| ]\n"},//19
		{"\t-- Confirm account ", "\t-- Konfirmasi transfer ke rekening "},//20
		{" amount "," sebesar "}//21

		// langOutputs[][currentLanguange]
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
		String displayPageMenu = "[===================================================================================================]\n"
				+
				"[    " + formattedDateTime + "    ]\n" +
				"[===================================================================================================]\n"
				+
				"[  █████╗ ████████╗███╗   ███╗    ██████╗  ██████╗ ██╗     ██╗███╗   ██╗███████╗███╗   ███╗ █████╗  ]\n"
				+
				"[ ██╔══██╗╚══██╔══╝████╗ ████║    ██╔══██╗██╔═══██╗██║     ██║████╗  ██║██╔════╝████╗ ████║██╔══██╗ ]\n"
				+
				"[ ███████║   ██║   ██╔████╔██║    ██████╔╝██║   ██║██║     ██║██╔██╗ ██║█████╗  ██╔████╔██║███████║ ]\n"
				+
				"[ ██╔══██║   ██║   ██║╚██╔╝██║    ██╔═══╝ ██║   ██║██║     ██║██║╚██╗██║██╔══╝  ██║╚██╔╝██║██╔══██║ ]\n"
				+
				"[ ██║  ██║   ██║   ██║ ╚═╝ ██║    ██║     ╚██████╔╝███████╗██║██║ ╚████║███████╗██║ ╚═╝ ██║██║  ██║ ]\n"
				+
				"[ ╚═╝  ╚═╝   ╚═╝   ╚═╝     ╚═╝    ╚═╝      ╚═════╝ ╚══════╝╚═╝╚═╝  ╚═══╝╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝ ]\n"
				+
				"[===================================================================================================]\n"
				+
				"[                                                                                                   ]\n"
				+
				"[                    .@@@@.                       _    _ _____ _     _____ ________  ___ _____      ]\n"
				+
				"[                @:=:@@@@@@:=:@                  | |  | |  ___| |   /  __ \\  _  |  \\/  ||  ___|     ]\n"
				+
				"[             @:-:@@@@====@@@@:-:@               | |  | | |__ | |   | /  \\/ | | | .  . || |__       ]\n"
				+
				"[          @:-@@@::::::::::::::@@@-:@            | |/\\| |  __|| |   | |   | | | | |\\/| ||  __|      ]\n"
				+
				"[        @:-@@::@@@@@@@@@@@@@@@@::@@-:@          \\  /\\  / |___| |___| \\__/\\ \\_/ / |  | || |___      ]\n"
				+
				"[      @:-@@::@@@@@ ******** @@@@@::@@-:@         \\/  \\/\\____/\\_____/\\____/\\___/\\_|  |_/\\____/      ]\n"
				+
				"[    @:-@@@:@@@@: ***++**++*** :@@@:@@@-:@                                                          ]\n"
				+
				"[   @:-@@::@@@: ***++- !! -++*** :@@@::@@-:@                                                        ]\n"
				+
				"[   @:@@::@@@@ ***++- .||. -++*** @@@@::@@:@            PLEASE INPUT YOUR ACCOUNT NUMBER            ]\n"
				+
				"[   @:@@@@@@@@ **+++- .||. -+++** @@@@@@@@:@                      AND YOUR PIN                      ]\n"
				+
				"[   @:#@@@@: ***+++=- .||. -=+++*** :@@@@#:@             ________________________________           ]\n"
				+
				"[   @:+@@@@ ****++--. +||+ .--++**** @@@@+:@             SILAKAN MASUKKAN NOMOR REKENING            ]\n"
				+
				"[   :@:@@@@. ***-++.. +--+ ..++-*** .@@@@:@:                      DAN PIN ANDA                      ]\n"
				+
				"[    @:*@@@@@@ ***** *#::#* ***** @@@@@@*:@                                                         ]\n"
				+
				"[    @:-@@@@@@ ****  ======  **** @@@@@@-:@                                                         ]\n"
				+
				"[     @:-@@@@@. ***####%%####*** . @@@@@-:@                    FOR SAFETY AND COMFORT               ]\n"
				+
				"[      @:.@@@@@ ################ @@@@@.:@                PLEASE CHANGE YOUR PIN REGULARLY           ]\n"
				+
				"[       @: @@@@@@+:.  ----  .:+@@@@@@ :@                 ________________________________           ]\n"
				+
				"[         @:* @@@ =@@@@@@@@@@@= @@@ :@                     DEMI KEAMANAN DAN KENYAMANAN             ]\n"
				+
				"[          @*  :*@@@@@@@@@@@@@@*:  *@                         SILAKAN GANTI PIN ANDA                ]\n"
				+
				"[              @@@#+-:....:-+#@@@                                 SECARA BERKALA                    ]\n"
				+
				"[                                                                                                   ]\n"
				+
				"[===================================================================================================]";
		System.out.println(displayPageMenu);
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
				String displayAccountIsNotFind = "[===================================================================================================]\n"
						+
						"[               ACCOUNT NUMBER IS NOT FOUND. PLEASE INPUT YOUR CORRECT ACCOUNT NUMBER               ]\n"
						+
						"[         [!]   _____________________________________________________________________   [!]         ]\n"
						+
						"[              NOMOR REKENING TIDAK DITEMUKAN. MOHON MASUKKAN NOMOR REKENING YANG BENAR             ]\n"
						+
						"[===================================================================================================]\n";
				System.out.println(displayAccountIsNotFind);
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
						String displayLoginAttempts = "[===================================================================================================]\n"
								+
								"[           LOGIN ATTEMPTS                          [=====]     /   [=====]                         ]\n"
								+
								"[           ______________                          [  " + loginAttempts
								+ "  ]    /    [  " + MAX_LOGIN_ATTEMPTS + "  ]                         ]\n" +
								"[           PERCOBAAN MASUK                         [=====]   /     [=====]                         ]\n"
								+
								"[===================================================================================================]";
						System.out.println(displayLoginAttempts);
						EnterForContinue();
						ClearScreen();
					}
				}
			} else {
				tryToLogin = true;
				ClearScreen();
				String displayAccountBlockedWarning = "[===================================================================================================]\n"
						+
						"[           YOUR ACCOUNT (" + inputUser_AccountNumber
						+ ") HAS BEEN BLOCKED. PLEASE INPUT ANOTHER ACCOUNT NUMBER            ]\n" +
						"[  [!] _______________________________________________________________________________________ [!]  ]\n"
						+
						"[      NOMOR REKENING ANDA (" + inputUser_AccountNumber
						+ ") TELAH DIBLOKIR. SILAKAN MASUKKAN NOMOR REKENING YANG LAIN      ]\n" +
						"[===================================================================================================]";
				System.out.println(displayAccountBlockedWarning);
				EnterForContinue();
				ClearScreen();
				return false;
			}

			// If the maximum login attempts are reached and status akun will change to
			// "TERBLOKIR"
			if (loginAttempts > MAX_LOGIN_ATTEMPTS) {
				String displayMaxLoginAttempts = "[===================================================================================================]\n"
						+
						"[          YOU HAVE INPUT YOUR PIN INCORRECTLY 3 TIMES. SORRY, WE HAVE BLOCKED YOUR ACCOUNT         ]\n"
						+
						"[    [!]   ________________________________________________________________________________   [!]   ]\n"
						+
						"[   ANDA TELAH SALAH MEMASUKKAN PIN SEBANYAK 3 KALI. MOHON MAAF, NOMOR REKENING ANDA KAMI BLOKIR    ]\n"
						+
						"[===================================================================================================]";
				System.out.println(displayMaxLoginAttempts);
				accountData[accountLineIndex][3] = "TERBLOKIR";
				EnterForContinue();
			}
		} while (tryToLogin || !isAccountFind);
		return false;
	}

	public static void WrongPin() {
		String displayWrongPin = "[===================================================================================================]\n"
				+
				"[                             LOGIN FAILED. PLEASE CHECK YOUR PIN AGAIN                             ]\n"
				+
				"[                       [!]   _________________________________________   [!]                       ]\n"
				+
				"[                             GAGAL MASUK. SILAKAN CEK PIN ANDA KEMBALI                             ]\n"
				+
				"[===================================================================================================]";
		System.out.println(displayWrongPin);
	}

	public static void chooseLanguange() {
		String chooseLanguange = "[===================================================================================================]\n"
				+
				"[                               PLEASE SELECT THE LANGUAGE TO BE USED                               ]\n"
				+
				"[                               ______________________________________                              ]\n"
				+
				"[                               MOHON PILIH BAHASA YANG AKAN DIGUNAKAN                              ]\n"
				+
				"[===================================================================================================]\n"
				+
				"[                                                                                                   ]\n"
				+
				"[  [1] [ENGLISH]                                                                                    ]\n"
				+
				"[                                                                                                   ]\n"
				+
				"[  [2] [BAHASA INDONESIA]                                                                           ]\n"
				+
				"[                                                                                                   ]\n"
				+
				"[===================================================================================================]";
		System.out.println(chooseLanguange);
		System.out.print("[ ==> ");
		currentLanguange = scanner1.nextInt();
		currentLanguange -= 1;
	}

	public static void Menu() {
		// Perulangan menu berdasarkan continueTransaction user
		do {
			isGoToMainMenu = false;
			ClearScreen();
			String menuOutput = 
			"[===================================================================================================]\n"+
			"[                                    "+langOutputs[0][currentLanguange]+"                                ]\n"+
			"[===================================================================================================]\n"+
			"[                                                                                                   ]\n"+
			"[                            [1] "+langOutputs[1][currentLanguange]+"                  "+"[5] "+langOutputs[5][currentLanguange]+"                              ]\n"+
			"[                                                                                                   ]\n"+
			"[                            [2] "+langOutputs[2][currentLanguange]+"           "+"[6] "+langOutputs[6][currentLanguange]+"                      ]\n"+
			"[                                                                                                   ]\n"+
			"[                            [3] "+langOutputs[3][currentLanguange]+"              "+"[7] "+langOutputs[7][currentLanguange]+"                           ]\n"+
			"[                                                                                                   ]\n"+
			"[                            [4] "+langOutputs[4][currentLanguange]+"                "+"[8] "+langOutputs[8][currentLanguange]+"                               ]\n"+
			"[                                                                                                   ]\n"+
			"[                                                          [9] "+langOutputs[9][currentLanguange]+"                              ]\n"+
			"[                                                                                                   ]\n"+
			"[===================================================================================================]";
			System.out.println(menuOutput);
			System.out.print("[ ==> ");
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
					String displayMoreTransaction =
					"[===================================================================================================]\n"+
					"[  "+langOutputs[10][currentLanguange]+"                                                                                ]\n"+
					"[                                                                                                   ]\n"+
					"[  [1] "+langOutputs[11][currentLanguange]+"                                                                                          ]\n"+
					"[                                                                                                   ]\n"+
					"[  [2] "+langOutputs[12][currentLanguange]+"                                                                                        ]\n"+
					"[                                                                                                   ]\n"+
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
								"[===================================================================================================]\n"+
								"[                                 INVALID INPUT. PILIHAN TIDAK TERSEDIA                             ]\n"+
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
		System.out.print(""+langOutputs[13][currentLanguange]);
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
			transferAmount = validateNonNegativeIntegerInput(""+langOutputs[15][currentLanguange]);
			ClearScreen();
			// Konversi nilai output ke rupiah
			String transferAmountRupiah = currencyFormat.format(transferAmount);
			System.out.println(
					"    ============================================================================================");
			System.out.println("    [  _______________________________________________  ]");
			System.out.println(langOutputs[17][currentLanguange]);
			System.out.printf(langOutputs[18][currentLanguange], inputTarget_AccountNumber);
			System.out.printf(langOutputs[19][currentLanguange], transferAmountRupiah);
			System.out.println("    [ ------------------------------------------------- ]");
			System.out.println(
					"    ============================================================================================");
			// Konfirmasi persetujuan transaksi
			System.out.println( inputTarget_AccountNumber
					+ langOutputs[21][currentLanguange] + transferAmountRupiah + " ?");
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
						System.out.println("\t-- Sisa saldo anda : " + userBalanceRupiah); //your remaining balance
						System.out.println(
								"    ============================================================================================");
						EnterForContinue();
						ClearScreen();
						isTargetAccountValid = false;
						// Pencatatan riwawayat transaksi
						transactionHistory.add(langOutputs[20][currentLanguange]
								+ inputTarget_AccountNumber + langOutputs[21][currentLanguange]
								+ transferAmountRupiah);
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
					red + langOutputs[16][currentLanguange]
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
				"    ============================================================================================");
		System.out.println(
				"    [  Silakan Pilih transaksi pembayaran berikut:                                             ]");
		System.out.println(
				"    [   _____________                                                                          ]");
		System.out.println(
				"    [  |_1._Pulsa____|                                                                         ]");
		System.out.println(
				"    [   _____________                                                                          ]");
		System.out.println(
				"    [  |_2._Listrik__|                                                                         ]");
		System.out.println(
				"    [   _______________                                                                        ]");
		System.out.println(
				"    [  |_3._Pendidikan_|                                                                       ]");
		System.out.println(
				"    [   _______________                                                                        ]");
		System.out.println(
				"    [  |_4._PDAM_______|                                                                       ]");
		System.out.println(
				"    [   _______________                                                                        ]");
		System.out.println(
				"    [  |_5._BPJS_______|                                                                       ]");
		System.out.println(
				"    [   ____________________________                                                           ]");
		System.out.println(
				"    [  |_6._Kembali ke menu utama___|                                                          ]");
		System.out.println(
				"    ============================================================================================");
		System.out.print("\t-- Menu yang anda pilih (angka): ");
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
			System.out.print("\t-- Input nomor telepon anda: "); // User input nomor telepon
			nomorTelepon = scanner1.nextLine();
			// System.out.print("\t-- Input nominal pulsa: Rp "); // User input nominal
			// pulsa
			nomPulsa = validateNonNegativeIntegerInput("\t-- Input nominal pulsa: Rp ");
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
			System.out
					.println("    [  -------------------------------------------------\t]");
			System.out.println(
					"    ============================================================================================");
			System.out.printf(
					"\t-- Konfirmasi transaksi ke nomor telepon %s dengan nominal %s\n",
					nomorTelepon, nomPulsaRP);
			UserConfirmation();
			ClearScreen();
			if (userConfirmation == 'Y' || userConfirmation == 'y') {
				ClearScreen();
				if (PinValidation()) {
					if (nomPulsa < userBalance) {
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
						System.out.printf("    [ |  Sisa saldo anda\t: %s\t|\t]\n",
								saldoRupiah2);
						System.out.println(
								"    [  -------------------------------------------------\t]");
						System.out.println(
								"    ============================================================================================");
						// Pencatatan riwayat transaksi
						transactionHistory.add("Telah melakukan pembelian pulsa ke nomor "
								+ nomorTelepon + " sebesar " + nomPulsaRP);

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
			String tagihanListrikRP = currencyFormat
					.format(listrikData[indexListrik][1]);
			System.out.println(
					"    ============================================================================================");
			System.out.println(
					"    [  _________________________________________________________\t]");
			System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
			System.out.printf("    [ |  ID PLN\t\t\t: %d\t\t\t|\t]\n", inputPLN);
			System.out.printf("    [ |  Total tagihan\t\t: %s\t\t\t|\t]\n",
					tagihanListrikRP);
			System.out.println(
					"    [  ---------------------------------------------------------\t]");
			System.out.println(
					"    ============================================================================================");
			System.out.printf(
					"\t-- Konfirmasi transaksi tagihan listrik dengan ID %s sebesar %s\n",
					inputPLN, tagihanListrikRP);
			UserConfirmation();
			ClearScreen();
			if (userConfirmation == 'Y' || userConfirmation == 'y') {
				if (PinValidation()) {
					if (listrikData[indexListrik][1] < userBalance) {
						userBalance -= listrikData[indexListrik][1];
						// Formatting saldo pengguna ke Rupiah
						String saldoRupiah3 = currencyFormat.format(userBalance);
						viewTransactionSuccess();
						System.out.println(
								"    [  _________________________________________________________\t]");
						System.out.println(
								"    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
						System.out.printf("    [ |  ID PLN\t\t\t: %d\t\t\t|\t]\n",
								inputPLN);
						System.out.printf("    [ |  Total tagihan\t\t: %s\t\t\t|\t]\n",
								tagihanListrikRP);
						System.out.printf("    [ |  Sisa saldo anda\t: %s\t\t|\t]\n",
								saldoRupiah3);
						System.out.println(
								"    [  ---------------------------------------------------------\t]");
						System.out.println(
								"    ============================================================================================");

						// Pencatatan riwayat transaksi
						transactionHistory.add(
								"Telah melakukan pembayaran tagihan listrik sebesar "
										+ tagihanListrikRP);

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
			String tagihanPendidikanRP = currencyFormat
					.format(pendidikanData[indexPendidikan][1]);
			System.out.println(
					"    ============================================================================================");
			System.out.println(
					"    [  _________________________________________________________\t]");
			System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
			System.out.printf("    [ |  Nomor VA\t\t: %d\t\t\t|\t]\n", inputVA);
			System.out.printf("    [ |  Total tagihan\t\t: %s\t\t|\t]\n",
					tagihanPendidikanRP);
			System.out.println(
					"    [  ---------------------------------------------------------\t]");
			System.out.println(
					"    ============================================================================================");
			System.out.printf(
					"\t-- Konfirmasi transaksi tagihan biaya pendidikan sebesar %s\n",
					tagihanPendidikanRP);
			UserConfirmation();
			ClearScreen();
			if (userConfirmation == 'Y' || userConfirmation == 'y') {
				if (PinValidation()) {
					if (pendidikanData[indexPendidikan][1] < userBalance) {
						userBalance -= pendidikanData[indexPendidikan][1];
						// Formatting output ke Rupiah
						String saldoRupiah3 = currencyFormat.format(userBalance);
						viewTransactionSuccess();
						System.out.println(
								"    [  _________________________________________________________\t]");
						System.out.println(
								"    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
						System.out.printf("    [ |  Nomor VA\t\t: %d\t\t\t|\t]\n",
								inputVA);
						System.out.printf("    [ |  Total tagihan\t\t: %s\t\t|\t]\n",
								tagihanPendidikanRP);
						System.out.printf("    [ |  Sisa saldo anda\t: %s\t\t|\t]\n",
								saldoRupiah3);
						System.out.println(
								"    [  ---------------------------------------------------------\t]");
						System.out.println(
								"    ============================================================================================");

						// Pencatatan riwayat transaksi
						transactionHistory.add(
								"Telah melakukan pembayaran tagihan pendidikan sebesar "
										+ tagihanPendidikanRP);

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
			String tagihanPdamRp = currencyFormat
					.format(tagihanAirData[indexPdam][1]);
			System.out.println(
					"    ============================================================================================");
			System.out.println(
					"    [  _________________________________________________________\t]");
			System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
			System.out.printf("    [ |  Nomor tagihan\t\t: %d\t\t\t|\t]\n", inputVA);
			System.out.printf("    [ |  Total tagihan\t\t: %s\t\t|\t]\n",
					tagihanPdamRp);
			System.out.println(
					"    [  ---------------------------------------------------------\t]");
			System.out.println(
					"    ============================================================================================");
			System.out.printf(
					"\t-- Konfirmasi transaksi tagihan biaya PDAM sebesar %s\n",
					tagihanPdamRp);
			UserConfirmation();
			ClearScreen();
			if (userConfirmation == 'Y' || userConfirmation == 'y') {
				if (PinValidation()) {
					if (tagihanAirData[indexPdam][1] < userBalance) {
						userBalance -= tagihanAirData[indexPdam][1];
						// Formatting output ke Rupiah
						String saldoRupiah3 = currencyFormat.format(userBalance);
						viewTransactionSuccess();
						System.out.println(
								"    [  _________________________________________________________\t]");
						System.out.println(
								"    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
						System.out.printf("    [ |  Nomor VA\t\t: %d\t\t\t|\t]\n",
								inputVA);
						System.out.printf("    [ |  Total tagihan\t\t: %s\t\t|\t]\n",
								tagihanPdamRp);
						System.out.printf("    [ |  Sisa saldo anda\t: %s\t\t|\t]\n",
								saldoRupiah3);
						System.out.println(
								"    [  ---------------------------------------------------------\t]");
						System.out.println(
								"    ============================================================================================");

						transactionHistory
								.add("Telah melakukan pembayaran tagihan PDAM sebesar "
										+ tagihanPdamRp);

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
			String tagihanBpjsRp = currencyFormat
					.format(BPJSdata[indexBpjs][1]);
			System.out.println(
					"    ============================================================================================");
			System.out.println(
					"    [  _________________________________________________________\t]");
			System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
			System.out.printf("    [ |  Nomor tagihan\t\t: %d\t\t\t|\t]\n", inputVA);
			System.out.printf("    [ |  Total tagihan\t\t: %s\t\t|\t]\n",
					tagihanBpjsRp);
			System.out.println(
					"    [  ---------------------------------------------------------\t]");
			System.out.println(
					"    ============================================================================================");
			System.out.printf(
					"\t-- Konfirmasi transaksi tagihan biaya BPJS sebesar %s\n",
					tagihanBpjsRp);

			UserConfirmation();
			ClearScreen();
			if (userConfirmation == 'Y' || userConfirmation == 'y') {
				if (PinValidation()) {
					if (BPJSdata[indexBpjs][1] < userBalance) {
						userBalance -= BPJSdata[indexBpjs][1];
						// Formatting output ke Rupiah
						String saldoRupiah3 = currencyFormat.format(userBalance);
						viewTransactionSuccess();
						System.out.println(
								"    [  _________________________________________________________\t]");
						System.out.println(
								"    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
						System.out.printf("    [ |  Nomor VA\t\t: %d\t\t\t|\t]\n",
								inputVA);
						System.out.printf("    [ |  Total tagihan\t\t: %s\t\t|\t]\n",
								tagihanBpjsRp);
						System.out.printf("    [ |  Sisa saldo anda\t: %s\t\t|\t]\n",
								saldoRupiah3);
						System.out.println(
								"    [  ---------------------------------------------------------\t]");
						System.out.println(
								"    ============================================================================================");

						// Pencatatan riwayat transaksi
						transactionHistory
								.add("Telah melakukan pembayaran tagihan BPJS sebesar "
										+ tagihanBpjsRp);

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
			System.out.printf("%d. %s%n", (i + 1), transactionHistory.get(i));
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

		ClearScreen();

		if (inputPin7.equals(inputPin)) {
			System.out.println(
					"    ============================================================================================");
			System.out.print("    Masukkan PIN baru: ");
			String inputNewPin = scanner1.nextLine();
			System.out.print("    Konfirmasi PIN baru: ");
			String confirmedNewPin = scanner1.nextLine();
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
		"[===================================================================================================]\n"+
		"[- - - - - - - - - - - - - - - - - - - - - -╦ ╦╔═╗╦  ╔═╗- - - - - - - - - - - - - - - - - - - - - - ]\n"+
		"[- - - - - - - - - - - - - - - - - - - - - -╠═╣║╣ ║  ╠═╝- - - - - - - - - - - - - - - - - - - - - - ]\n"+
		"[- - - - - - - - - - - - - - - - - - - - - -╩ ╩╚═╝╩═╝╩- - - - - - - - - - - - - - - - - - - - - - - ]\n"+
		"[===================================================================================================]"
		);
	}

	public static void displayHelp() {
		displayHeaderHelp();
		System.out.println(
		"[===================================================================================================]\n"+
		"[                                PETUNJUK MENGGUNAKAN ATM POLINEMA                                  ]\n"+
		"[                                                                                                   ]\n"+
		"[  Q: Bagaimana cara menggunakannya?                                                                ]\n"+
		"[  A: Anda bisa memulai program dengan menekan angka yang sesuai dengan menu                        ]\n"+
		"[                                                                                                   ]\n"+
		"[  Q: Apakah saya bisa melakukan transaksi lebih dari 1 kali?                                       ]\n"+
		"[  A: Bisa. Setelah anda melakukan konfirmasi, akan ada pemberitahuan mengenai konfirmasi transaksi ]\n"+
		"[     lagi. Anda bisa memilih menu yang tersedia kembali.                                           ]\n"+
		"[                                                                                                   ]\n"+
		"[===================================================================================================]"
		);
		EnterForContinue();
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
		"[===================================================================================================]\n"+
		"[~ ~ ~ ~ ~ ~ ~ ~ ~ Terimakasih telah bertransaksi! Semoga harimu selalu bahagia :) ~ ~ ~ ~ ~ ~ ~ ~ ~]\n"+
		"[===================================================================================================]\n"+
		"\n" +
		"* * * [   This program is made by:                      ] * * * * * * * * * * * * * * * * * * * * * *\n"+
		"* * * [   The students of State Polytechnic of Malang   ] * * * * * * * * * * * * * * * * * * * * * *\n"+
		"* * * [   - Atabik Mutawakilalallah                     ] * * * * * * * * * * * * * * * * * * * * * *\n"+
		"* * * [   - Farrel Augusta Dinata                       ] * * * * * * * * * * * * * * * * * * * * * *\n"+
		"* * * [   - Innama Maesa Putri                          ] * * * * * * * * * * * * * * * * * * * * * *\n"+
		"* * * [   \u00A92023\n" +
		"[====================================================================================================]"
		);
	}

	public static char UserConfirmation() {
		System.out.print("Tekan 'Y' untuk IYA. Tekan 'T' untuk TIDAK --> ");
		userConfirmation = scanner2.next().charAt(0);
		return userConfirmation;
	}

	public static boolean PinValidation() {
		System.out.print("[  Masukkan PIN anda: ");
		inputPin = scanner4.nextLine();
		ClearScreen();
		if (inputPin.equals(accountData[accountLineIndex][1])) {
			return true;
		} else {
			return false;
		}
	}

	public static void EnterForContinue() {
		System.out.print(""+langOutputs[16][currentLanguange]);
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

}
