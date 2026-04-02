
import java.io.IOException;

public class Parser {
    private LexicalAnalyzer lexer;
    private CToken lookahead;
    private ErrorList errorList;
    private final SymbolTable symbolTable = new SymbolTable();

    public Parser(LexicalAnalyzer lexer, ErrorList errorList) throws IOException {
        this.lexer = lexer;
        this.lookahead = lexer.yylex();
        this.errorList = errorList;
    }

    private void consume(String expected) throws IOException {
        if (lookahead == null) {
            throw new RuntimeException("Fim inesperado");
        }

        if (lookahead.name.equals(expected)) {
            lookahead = lexer.yylex();
        } else {
            error("Esperado: " + expected + ", encontrado: " + lookahead.name);
            lookahead = lexer.yylex();
        }
    }

    private void error(String msg) {
        if (lookahead != null) {
            errorList.addError("Erro sintático: " + msg, lookahead.line, lookahead.column);
        } else {
            errorList.addError("Erro sintático: " + msg, -1, -1);
        }
    }

    public void parse() throws IOException {
        programa();
        System.out.println("Análise sintática concluída.\n");

        if (errorList.hasErrors()) {
            errorList.printErrors();
        } else {
            printSymbols();
        }
    }

    private void programa() throws IOException {
        while (lookahead != null) {
            comando();
        }
    }

    private void comando() throws IOException {
        if (lookahead == null) return;

        switch (lookahead.name) {
            case "ID":
                atribuicao();
                break;
            case "print":
                print();
                break;
            default:
                error("Comando inválido: " + lookahead.name);
                lookahead = lexer.yylex();
                break;
        }
    }

    private void atribuicao() throws IOException {
        String varName = lookahead.value;

        if (!symbolTable.exists(varName)) {
            symbolTable.declare(varName, "var");
        }

        consume("ID");
        consume("IGUAL");
        expr();
    }

    private void print() throws IOException {
        consume("print");
        consume("parenteseesquerdo");
        expr();
        consume("parentesedireito");
    }

    private void expr() throws IOException {
        termo();

        while (lookahead != null && lookahead.name.equals("SOMA")) {
            consume("SOMA");
            termo();
        }
    }

    private void termo() throws IOException {
        fator();
    }

    private void fator() throws IOException {
        if (lookahead == null) {
            error("Fim inesperado na expressão");
            return;
        }

        switch (lookahead.name) {
            case "ID":
                if (!symbolTable.exists(lookahead.value)) {
                    error("Variável não declarada: " + lookahead.value);
                }
                consume("ID");
                break;

            case "inteiro":
                consume("inteiro");
                break;

            case "STRING":
                consume("STRING");
                break;

            case "parenteseesquerdo":
                consume("parenteseesquerdo");
                expr();
                consume("parentesedireito");
                break;

            case "input":
                inputFunc();
                break;

            case "int":
                intFunc();
                break;

            case "str":
                strFunc();
                break;

            default:
                error("Esperado: ID, inteiro, STRING, input, int, str ou '('");
                lookahead = lexer.yylex();
                break;
        }
    }

    private void inputFunc() throws IOException {
        consume("input");
        consume("parenteseesquerdo");

        if (lookahead != null && lookahead.name.equals("STRING")) {
            consume("STRING");
        } else {
            error("Esperado STRING dentro de input()");
        }

        consume("parentesedireito");
    }

    private void intFunc() throws IOException {
        consume("int");
        consume("parenteseesquerdo");
        expr();
        consume("parentesedireito");
    }

    private void strFunc() throws IOException {
        consume("str");
        consume("parenteseesquerdo");
        expr();
        consume("parentesedireito");
    }

    public void printSymbols() {
        symbolTable.printTable();
    }
}