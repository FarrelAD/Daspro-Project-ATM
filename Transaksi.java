import java.util.Scanner;

public class Transaksi {
    public static void main(String[] args) {
        int menu, nom_transfer, nom_setor, nom_tarik, sisa_saldo, saldo_awal = 50000000;
        String no_rek_tujuan;

        Scanner sc = new Scanner(System.in);
        Scanner baru = new Scanner(System.in);

        System.out.println("Pilih menu :");
        System.out.println("1. Transfer");
        System.out.println("2. Tarik tunai");
        System.out.println("3. Setor tunai");
        System.out.println("Menu yang dipilih :");
        menu = sc.nextInt();
        if (menu == 1) {
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
        } else if (menu == 2) {
            System.out.println("Masukkan nominal tarik tunai : ");
            nom_tarik = sc.nextInt();
            if (nom_tarik > saldo_awal) {
                System.out.println("Transaksi gagal");
            } else {
                sisa_saldo = saldo_awal - nom_tarik;
                System.out.println("Tarik tunai berhasil dilakukan");
                System.out.println("Sisa saldo anda : " + sisa_saldo);
            }
        } else if (menu == 2) {
            System.out.println("Masukkan nominal setor tunai : ");
            nom_setor = sc.nextInt();
            sisa_saldo = saldo_awal + nom_setor;
            System.out.println("Setor tunai berhasil dilakukan");
            System.out.println("Sisa saldo anda : " + sisa_saldo);
        } else {
            System.out.println("Menu yang dimasukkan tidak valid");
        }
    }
}
