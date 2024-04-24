import service.ConvertirService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String respues ="";
        int opcion;
        Scanner scan = new Scanner(System.in);
        ConvertirService con = new ConvertirService();
        while (respues != null) {
        try{
            System.out.println(ConvertirService.MENU);
            opcion = scan.nextInt();
            System.out.println(respues = con.convertirMenu(opcion));
        }catch (Exception e){
            System.out.println("Vuelve a intentarlo");
            scan.nextLine();
        }
    }
    }
}