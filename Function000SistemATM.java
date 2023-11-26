// Program sistem ATM - Kelompok 4 - TI-1B
// Fitur yang tersedia: autentifikasi pengguna, transfer, tarik tunai, setor tunai, 
// pembayaran lain-lain, riwayat transaksi, cek saldo, ubah PIN, dan EXIT
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

	static boolean isAccountValid = false, isAccountNumberValid = false,
			isAccountFind = false, isTargetAccountValid = false;
	static final int MAX_LOGIN_ATTEMPTS = 3; 

	static boolean isTransactionExit = true;

	static String pressEnter;

	static String inputTarget_AccountNumber;
	static char continueTransaction = 'y', userChoice = 't';

	static int transferAmount, cashWithdrawalAmount, cashDepositAmount;
	static int userBalance = Integer.parseInt(accountData[accountLineIndex][2]);
	static String inputUser_AccountNumber;
	static String inputPin;

	// Format nilai uang Indonesia Rupiah (IDR)
	static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

	public static void main(String[] args) {
		while (!isAccountValid) {
			PageMenu();
			Login();
		}

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

	public static boolean Login() {
		loginAttempts = 0;
		boolean tryToLogin = false;
	
		do {
			System.out.print("Masukkan nomor rekening anda: ");
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
				System.out.println("Nomor rekening tidak ditemukan. Mohon input nomor rekening anda lagi!");
				System.out.print("Enter untuk melanjutkan --> ");
				pressEnter = scanner1.nextLine();
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
						System.out.println("Percobaan login " + loginAttempts + "/" + MAX_LOGIN_ATTEMPTS);
						System.out.print("Enter untuk melanjutkan --> ");
						pressEnter = scanner1.nextLine();
						ClearScreen();
					}
				}
			} else {
				tryToLogin = true;
				ClearScreen();
				System.out.printf("Nomor rekening anda (%s) telah diblokir. Silakan masukkan nomor rekening yang lain\n", inputUser_AccountNumber);
				System.out.print("Enter untuk melanjutkan --> ");
				pressEnter = scanner1.nextLine();
				ClearScreen();
				return false;
			}
	
			// If the maximum login attempts are reached and status akun will change to "TERBLOKIR"
			if (loginAttempts > MAX_LOGIN_ATTEMPTS) {
				System.out.println("Anda telah salah memasukkan PIN sebanyak 3 kali. Mohon maaf, nomor rekening Anda kami blokir.");
				accountData[accountLineIndex][3] = "TERBLOKIR";
				System.out.println("STATUS AKUN ANDA : " + accountData[accountLineIndex][3]);
				System.out.print("Enter untuk melanjutkan --> ");
				pressEnter = scanner1.nextLine();
			}
		} while (tryToLogin || !isAccountFind);
		return false;
	}
	

	public static void WrongPin() {
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
	}

	public static void Menu() {
		// Perulangan menu berdasarkan continueTransaction user
		do {
			ClearScreen();
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
				default:
					break;
			}
		} while (continueTransaction == 'y' || continueTransaction == 'Y');
	}

	public static void TransferView() {
		System.out.println(
						"    ============================================================================================");
		System.out.println(
						"    [- - - - - - - - - - - - - - - - -╔╦╗╦═╗╔═╗╔╗╔╔═╗╔═╗╔═╗╦═╗- - - - - - - - - - - - - - - - -]");
		System.out.println(
						"    [- - - - - - - - - - - - - - - - - ║ ╠╦╝╠═╣║║║╚═╗╠╣ ║╣ ╠╦╝- - - - - - - - - - - - - - - - -]");
		System.out.println(
						"    [- - - - - - - - - - - - - - - - - ╩ ╩╚═╩ ╩╝╚╝╚═╝╚  ╚═╝╩╚═- - - - - - - - - - - - - - - - -]");
		System.out.println(
						"    ============================================================================================");
	}

	public static void Transfer() {
		TransferView();
		System.out.print("\t-- Masukkan nomor rekening tujuan : ");
		inputTarget_AccountNumber = scannerTF.nextLine();
		// Pengecekan apakah nomor rekening tujuan ada di database
		for (int i = 0; i < accountData.length; i++) {
				if ((inputTarget_AccountNumber.equals(accountData[i][0]))
								&& (!inputTarget_AccountNumber.equals(inputUser_AccountNumber))) {
						isTargetAccountValid = true;
						break;
				}
		}
		// Kondisi jika isTargetAccountValid true
		if (isTargetAccountValid) {
			System.out.print("\t-- Masukkan nominal transfer : Rp "); // User input nominal transfer
			transferAmount = scanner4.nextInt();
			ClearScreen();
			// Konversi nilai output ke rupiah
			String transferAmountRupiah = currencyFormat.format(transferAmount);
			System.out.println(
							"    ============================================================================================");
			System.out.println("    [  _______________________________________________  ]");
			System.out.println("    [ |  $$$  - RINCIAN TRANSFER - $$$\t\t      | ]");
			System.out.printf("    [ |  Rekening tujuan: %s\t\t      | ]\n", inputTarget_AccountNumber);
			System.out.printf("    [ |  Nominal transfer: %s\t\t\t| ]\n", transferAmountRupiah);
			System.out.println("    [ ------------------------------------------------- ]");
			System.out.println(
							"    ============================================================================================");
			// Konfirmasi persetujuan transaksi
			System.out.println("\t-- Konfirmasi transfer ke rekening " + inputTarget_AccountNumber
							+ " dengan nominal " + transferAmountRupiah + " ?");
			System.out.print("\t-- Tekan 'Y' untuk Ya. Tekan 'T' untuk tidak.  -->  ");
			userChoice = scanner4.next().charAt(0);
			ClearScreen();
			System.out.println(
							"    ============================================================================================");

			// Konfirmasi transaksi
			if (userChoice == 'y' || userChoice == 'Y') {
				// Pengecekan apakah input PIN sesuai dengan database
				if (PinValidation()) {
					if (transferAmount < userBalance) {
							userBalance -= transferAmount; // Pengurangan saldo pengguna dengan
															// nominal
															// transfer

						// Formatting penulisan rupiah pada output
						String userBalanceRupiah = currencyFormat.format(userBalance);
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
						System.out.println("\t-- Sisa saldo anda : " + userBalanceRupiah);
						System.out.println(
										"    ============================================================================================");

						System.out.print("Enter untuk melanjutkan -->  ");
						pressEnter = scanner1.nextLine();
						ClearScreen();
						isTargetAccountValid = false;
						// Pencatatan riwayat transaksi
						transactionHistory[maxTransactionHistory
						- transactionCount] = "Telah melakukan transaksi ke rekening "
						+ inputTarget_AccountNumber
						+ " sebesar "
						+ transferAmountRupiah;
						transactionCount--;
					} else {
							// Kondisi jika nominal transfer melebihi jumlah saldo
							isTargetAccountValid = false; // Reset nilai isTargetAccountValid
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
				} else {
					// Kondisi jika pengguna input PIN tidak sesuai dengan array accountData
					isTargetAccountValid = false; // Reset nilai isTargetAccountValid
					System.out.println(
									"    ============================================================================================");
					System.out.println(
									"    --------------------------------------------------------------------------------------------");
					System.out.println(
									"                                      [  (!) PIN SALAH! (!)  ]");
					System.out.println(
									"    --------------------------------------------------------------------------------------------");
					System.out.println(
									"    ============================================================================================");
				}
			} else {
				// Kondisi jika pengguna input 't' atau 'T'
				isTargetAccountValid = false; // Reset nilai isTargetAccountValid
				System.out.println(
								"    ============================================================================================");
				System.out.println(
								"    --------------------------------------------------------------------------------------------");
				System.out.println(
								"                                 [  (!) TRANSAKSI DIBATALKAN (!)  ]");
				System.out.println(
								"    --------------------------------------------------------------------------------------------");
				System.out.println(
								"    ============================================================================================");
			}
		} else {
				// Kondisi jika isTargetAccountValid bernilai FALSE
				System.out.println(
								"    ============================================================================================");
				System.out.println(
								"    --------------------------------------------------------------------------------------------");
				System.out.println(
								"                    [  (!) Transaksi gagal. Nomor rekening tujuan invalid (!)  ]");
				System.out.println(
								"    --------------------------------------------------------------------------------------------");
				System.out.println(
								"    ============================================================================================");
		}
	}

	public static void TarikTunaiView() {
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - ╔╦╗╔═╗╦═╗╦╦╔═  ╔╦╗╦ ╦╔╗╔╔═╗╦ - - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - -║ ╠═╣╠╦╝║╠╩╗   ║ ║ ║║║║╠═╣║ - - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - -╩ ╩ ╩╩╚═╩╩ ╩   ╩ ╚═╝╝╚╝╩ ╩╩ - - - - - - - - - - - - - - -]");
		System.out.println(
				"    ============================================================================================");
	}
	public static void TarikTunai() {
		TarikTunaiView();
		System.out.print("\t-- Masukkan nominal tarik tunai : Rp ");
		cashWithdrawalAmount = scanner3.nextInt();
	}

	public static void SetorTunaiView() {
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    [- - - - - - - - - - - - - - - -╔═╗╔═╗╔╦╗╔═╗╦═╗  ╔╦╗╦ ╦╔╗╔╔═╗╦- - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - -╚═╗║╣  ║ ║ ║╠╦╝   ║ ║ ║║║║╠═╣║- - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - -╚═╝╚═╝ ╩ ╚═╝╩╚═   ╩ ╚═╝╝╚╝╩ ╩╩- - - - - - - - - - - - - - -]");
		System.out.println(
				"    ============================================================================================");

	}
	public static void SetorTunai() {
		SetorTunaiView();
		System.out.print("\t-- Masukkan nominal setor tunai : Rp ");
		cashDepositAmount = scanner3.nextInt();
	}

	public static void PembayaranLainnyaView() {
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    [- - - - - - - - - -╔═╗╔═╗╔╦╗╔╗ ╔═╗╦ ╦╔═╗╦═╗╔═╗╔╗╔  ╦  ╔═╗╦╔╗╔╔╗╔╦ ╦╔═╗- - - - - - - - - - ]");
		System.out.println(
				"    [- - - - - - - - - -╠═╝║╣ ║║║╠╩╗╠═╣╚╦╝╠═╣╠╦╝╠═╣║║║  ║  ╠═╣║║║║║║║╚╦╝╠═╣- - - - - - - - - - ]");
		System.out.println(
				"    [- - - - - - - - - -╩  ╚═╝╩ ╩╚═╝╩ ╩ ╩ ╩ ╩╩╚═╩ ╩╝╚╝  ╩═╝╩ ╩╩╝╚╝╝╚╝ ╩ ╩ ╩- - - - - - - - - - ]");
		System.out.println(
				"    ============================================================================================");
	}
	public static void PembayaranLainnya() {
		PembayaranLainnyaView();
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
				"    ============================================================================================");
		System.out.print("\t-- Menu yang anda pilih (angka): "); 
		int menuBayar = scanner3.nextInt();
		ClearScreen();
		switch(menuBayar) {
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
			default:
				break;
		}
	}

	public static void PulsaView() {
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - - -╔═╗╦ ╦╦  ╔═╗╔═╗ - - - - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - - -╠═╝║ ║║  ╚═╗╠═╣ - - - - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - - -╩  ╚═╝╩═╝╚═╝╩ ╩ - - - - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    ============================================================================================");
	}

	public static void Pulsa() {
		PulsaView();
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
	}

	public static void ListrikView() {
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - - -╦  ╦╔═╗╔╦╗╦═╗╦╦╔═ - - - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - - -║  ║╚═╗ ║ ╠╦╝║╠╩╗ - - - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - - -╩═╝╩╚═╝ ╩ ╩╚═╩╩ ╩ - - - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    ============================================================================================");
	}

	public static void Listrik() {
		ListrikView();
		System.out.print("\t-- Masukkan ID pelanggan PLN/Nomor meter: ");
		int inputPLN = scanner4.nextInt();
	}

	public static void PendidikanView() {
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - ╔═╗╔═╗╔╗╔╔╦╗╦╔╦╗╦╦╔═╔═╗╔╗╔ - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - ╠═╝║╣ ║║║ ║║║ ║║║╠╩╗╠═╣║║║ - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - ╩  ╚═╝╝╚╝═╩╝╩═╩╝╩╩ ╩╩ ╩╝╚╝ - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    ============================================================================================");
	}

	public static void Pendidikan() {
		PendidikanView();
		System.out.print("\t-- Masukkan nomor virtual account : ");
		int inputVA = scanner4.nextInt();
	}

	public static void PdamView() {
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - - - -╔═╗╔╦╗╔═╗╔╦╗- - - - - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - - - -╠═╝ ║║╠═╣║║║- - - - - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - - - -╩  ═╩╝╩ ╩╩ ╩- - - - - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    ============================================================================================");
	}

	public static void Pdam() {
		PdamView();
		System.out.print("\t-- Masukkan nomor tagihan : ");
		int inputVA = scanner4.nextInt();
	}

	public static void BpjsView() {
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - - - - ╔╗ ╔═╗╦╔═╗- - - - - - - - - - - - - - - - - - - - ]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - - - - ╠╩╗╠═╝║╚═╗- - - - - - - - - - - - - - - - - - - - ]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - - - - ╚═╝╩ ╚╝╚═╝- - - - - - - - - - - - - - - - - - - - ]");
		System.out.println(
				"    ============================================================================================");
	}

	public static void Bpjs() {
		BpjsView();
		System.out.print("\t-- Masukkan nomor tagihan : ");
		int inputVA = scanner4.nextInt();
	}

	public static void RiwayatTransaksiView() {
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    [- - - - - - - - - - -╦═╗╦╦ ╦╔═╗╦ ╦╔═╗╔╦╗  ╔╦╗╦═╗╔═╗╔╗╔╔═╗╔═╗╦╔═╔═╗╦- - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - -╠╦╝║║║║╠═╣╚╦╝╠═╣ ║    ║ ╠╦╝╠═╣║║║╚═╗╠═╣╠╩╗╚═╗║- - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - -╩╚═╩╚╩╝╩ ╩ ╩ ╩ ╩ ╩    ╩ ╩╚═╩ ╩╝╚╝╚═╝╩ ╩╩ ╩╚═╝╩- - - - - - - - - - - -]");
		System.out.println(
				"    ============================================================================================");
	}
	public static void RiwayatTransaksi() {
		RiwayatTransaksiView();
		System.out.print("Enter untuk melanjutkan -->  ");
		pressEnter = scanner1.nextLine();
	}

	public static void CekSaldoView() {
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - ╔═╗╔═╗╦╔═  ╔═╗╔═╗╦  ╔╦╗╔═╗ - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - ║  ║╣ ╠╩╗  ╚═╗╠═╣║   ║║║ ║ - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - ╚═╝╚═╝╩ ╩  ╚═╝╩ ╩╩═╝═╩╝╚═╝ - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    ============================================================================================");
	}
	public static void CekSaldo() {
		CekSaldoView();
		System.out.print("Enter untuk melanjutkan -->  ");
		pressEnter = scanner1.nextLine();
	}

	public static void UbahPinView() {
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - ╦ ╦╔╗ ╔═╗╦ ╦  ╔═╗╦╔╗╔- - - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - ║ ║╠╩╗╠═╣╠═╣  ╠═╝║║║║- - - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - ╚═╝╚═╝╩ ╩╩ ╩  ╩  ╩╝╚╝- - - - - - - - - - - - - - - - - -]");
		System.out.println(
				"    ============================================================================================");
	}
	public static void UbahPin() {
		UbahPinView();
		System.out.print("    Masukkan nomor rekening : ");
		String inputUser_AccountNumber7 = scanner1.nextLine();
	}

	public static void ExitView() {
		System.out.println(
				"    ============================================================================================");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - - ╦╔═╔═╗╦  ╦ ╦╔═╗╦═╗ - - - - - - - - - - - - - - - - - - ]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - - ╠╩╗║╣ ║  ║ ║╠═╣╠╦╝ - - - - - - - - - - - - - - - - - - ]");
		System.out.println(
				"    [- - - - - - - - - - - - - - - - - - ╩ ╩╚═╝╩═╝╚═╝╩ ╩╩╚═ - - - - - - - - - - - - - - - - - - ]");
		System.out.println(
				"    ============================================================================================");
	}	
	public static char Exit() {
		ExitView();
		System.out.println("\t-- Apakah anda yakin untuk keluar?");
		System.out.print("\t-- Tekan 'Y' untuk Ya. Tekan 'T' untuk tidak.  -->  ");
		char userTryExit = scanner1.next().charAt(0);
		ClearScreen();
		if (userTryExit == 'Y' || userTryExit == 'y') {
			continueTransaction = 't';
			System.out.println(
				"    ============================================================================================");
			System.out.println(
				"     ~ ~ ~ ~ ~ ~ ~ Terimakasih telah bertransaksi! Semoga harimu selalu bahagia :) ~ ~ ~ ~ ~ ~ ~");
			System.out.println(
				"    ============================================================================================");
		} else {
			continueTransaction = 'y' ;
		}
		return continueTransaction;
	}

	public static void FinalExit() {

	}

	public static boolean PinValidation() {
		System.out.print("Masukkan PIN anda: ");
		inputPin = scanner1.nextLine();	
		if (inputPin.equals(accountData[accountLineIndex][1])) {
			return true;
		} else {
			return false;
		}
	}

	public static void ClearScreen() {
		System.out.println("\033[H\033[2J");
		System.out.flush();
	}

}