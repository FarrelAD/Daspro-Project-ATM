import java.util.Scanner;

public class transfer {
    public static void main(String[] args) {
        int nom_transfer, sisa_saldo, saldo_awal = 50000000;
        String no_rek_tujuan;

        Scanner sc = new Scanner(System.in);
        Scanner baru = new Scanner(System.in);

        System.out.println("Masukkan nomor rekening tujuan :");
        no_rek_tujuan = baru.nextLine();
        System.out.println("Masukkan nominal transfer : ");
        nom_transfer = sc.nextInt();
        if (nom_transfer > saldo_awal) {
            System.out.println("Transaksi gagal");
        } else {
            sisa_saldo = saldo_awal - nom_transfer;
            System.out.println("Transfer ke nomor " + no_rek_tujuan + " berhasil dilakukan");
            System.out.println("Sisa saldo anda : " + sisa_saldo);
        }
    }
}
