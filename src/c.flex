package src;

import java_cup.runtime.*;

%%

%{
    private ErrorList listaErros;

    public LexicalAnalyzer(java.io.FileReader in, ErrorList listaErros){
        this(in);
        this.listaErros = listaErros;
    }

    public ErrorList getListaErros() {
        return listaErros;
    }

    private CToken createToken(String name, String value) {
        return new CToken(name, value, yyline, yycolumn);
    }
%}

%public
%class LexicalAnalyzer
%type CToken
%line
%column

// Expressões regulares
inteiro = 0|[1-9][0-9]*
ID = [a-zA-Z_][a-zA-Z0-9_]*
STRING = \"([^\"\\]|\\.)*\"

// Palavras reservadas / funções
INT = "int"
STR = "str"
PRINT = "print"
INPUT = "input"

// Operadores e símbolos
IGUAL = "="
SOMA = \+
abrePar = \(
fechaPar = \)
virgula = ","

// Espaços e quebras de linha
brancos = [ \t\r]+
novaLinha = \n+

%%

// Palavras reservadas / funções
{INT}           { return createToken("int", yytext()); }
{STR}           { return createToken("str", yytext()); }
{PRINT}         { return createToken("print", yytext()); }
{INPUT}         { return createToken("input", yytext()); }

// Literais
{STRING}        { return createToken("STRING", yytext()); }
{inteiro}       { return createToken("inteiro", yytext()); }

// Operadores e símbolos
{IGUAL}         { return createToken("IGUAL", yytext()); }
{SOMA}          { return createToken("SOMA", yytext()); }
{abrePar}       { return createToken("parenteseesquerdo", yytext()); }
{fechaPar}      { return createToken("parentesedireito", yytext()); }
{virgula}       { return createToken("virgula", yytext()); }

// Identificadores
{ID}            { return createToken("ID", yytext()); }

// Ignorar espaços
{brancos}       { /* ignora */ }
{novaLinha}     { /* ignora */ }

// Erro léxico
. {
    listaErros.addError("Caractere inválido: '" + yytext() + "'", yyline, yycolumn);
}