
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/program.py";
        ErrorList errorList = new ErrorList();

        try {
            System.out.println("Iniciando análise...");

            FileReader reader = new FileReader(filePath);
            LexicalAnalyzer lexer = new LexicalAnalyzer(reader, errorList);
            Parser parser = new Parser(lexer, errorList);

            parser.parse();

            System.out.println("\nFinalizado.");

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro geral: " + e.getMessage());
            e.printStackTrace();
        }
    }
}