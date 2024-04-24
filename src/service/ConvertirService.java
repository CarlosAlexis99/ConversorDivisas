package service;
import com.google.gson.Gson;
import dto.Codes;
import dto.Conversion;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class ConvertirService {
    private Scanner scan = new Scanner(System.in);
    private String apiKey = "222a2a0c667e1b70fe2f93b2";
    public static String MENU=
            "Bienvenido al conversor de monedas\n\n" +
            "1. ARS - Peso argentino ==> BOB - Boliviano boliviano\n" +
            "2. BOB - Boliviano boliviano ==> CLP - Peso chileno\n" +
            "3. BRL - Real brasileño ==> USD - Dólar estadounidense\n" +
            "4. Elegir entre otras divisas\n" +
            "5. Salir\n" +

            "Selecciona una opción";

    public String convertirMenu(int opcion) throws IOException, InterruptedException {
        String resultado;
        String mensaje = "";
        double valor = 0.0;

        switch (opcion){
            case 1:
                System.out.println("Ingresa el valor que deseas convertir");
                try{
                    valor = scan.nextDouble();
                    scan.nextLine();
                }catch (InputMismatchException e){
                    System.out.println("Opcion no valida, inténtalo de nuevo");
                    scan.nextLine();
                    break;
                }
                resultado = convertir("ARS", "BOB", valor);
                mensaje = "La cantidad de " +valor+" ARS equivale a "+resultado+" BOB";
                return mensaje;
            case 2:
                System.out.println("Ingresa el valor que deseas convertir");
                try{
                    valor = scan.nextDouble();
                    scan.nextLine();
                }catch (InputMismatchException e){
                    System.out.println("Opcion no valida, inténtalo de nuevo");
                    scan.nextLine();
                    break;
                }
                resultado = convertir("BOB", "CLP", valor);
                mensaje = "La cantidad de " +valor+" BOB equivale a "+resultado+" CLP";
                return mensaje;
            case 3:
                System.out.println("Ingresa el valor que deseas convertir");
                try{
                    valor = scan.nextDouble();
                    scan.nextLine();
                }catch (InputMismatchException e){
                    System.out.println("Opcion no valida, inténtalo de nuevo");
                    scan.nextLine();
                    break;
                }
                resultado = convertir("BRL", "USD", valor);
                mensaje = "La cantidad de " +valor+" BRL equivale a "+resultado+" USD";
                return mensaje;
            case 4:
                System.out.println(mostrarCodes());
                System.out.println("Slecciona el código de la divisa que quieres convertir");
                String divisa = null;
                try{
                    divisa = scan.nextLine();
                }catch (InputMismatchException e){
                    System.out.println("Opcion no valida, inténtalo de nuevo");
                    scan.nextLine();
                    break;
                }
                System.out.println("Ingresa el valor que deseas convertir");
                try{
                    valor = scan.nextDouble();
                    scan.nextLine();
                }catch (InputMismatchException e){
                    System.out.println("Opcion no valida, inténtalo de nuevo");
                    scan.nextLine();
                    break;
                }
                System.out.println("Slecciona el código de la divisa a la que quieres convertir");
                String divisaRequerida = scan.nextLine();
                resultado = convertir(divisa, divisaRequerida, valor);
                return mensaje = "La cantidad de " +valor+" "+divisa+" equivale a "+resultado+" "+divisaRequerida;
            case 5:
                return null;
            default:
                return "Opción no válida, vuelve a intentarlo";
        }
        return "";
    }

    public String convertir(String divisa, String divisaRequerida, double amount) throws IOException, InterruptedException {
        String solicitud = "https://v6.exchangerate-api.com/v6/"+apiKey+"/pair/"+divisa+"/"+divisaRequerida+"/"+amount;
        String json = request(solicitud);
        Gson gson = new Gson();
        Conversion con = gson.fromJson(json, Conversion.class);

        return String.valueOf(con.conversion_result());
    }

    public String mostrarCodes(){
        String mensaje = "****************************************\n" +
                "Estas son las divas disponibles\n\n";
        int contador = 0;
        HashMap<String, String> codes = codes();

        for(Map.Entry<String,String> entry : codes.entrySet()){
            if(contador == 3){
                contador = 0;
                mensaje += "\n";
            }else{
                contador ++;
            }
            String divisaInfo = entry.getKey() + " - " +entry.getValue();
            while(divisaInfo.length() < 33 ){
                divisaInfo += " ";
            }
            mensaje += divisaInfo +"\t";
        }
        mensaje += "\n****************************************";
        return mensaje;
    }


    public HashMap<String, String> codes(){
        HashMap<String, String> codesMap = new HashMap<>();
        String uri = "https://v6.exchangerate-api.com/v6/"+apiKey+"/codes";
        try {
            String json = request(uri);
            Gson gson = new Gson();
            Codes codes =  gson.fromJson(json, Codes.class);

            for (String[] e: codes.supported_codes()){
                codesMap.put(e[0],e[1]);
            }

            return codesMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String request(String uri) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
