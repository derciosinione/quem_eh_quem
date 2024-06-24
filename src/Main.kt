import java.io.File

var ids = emptyArray<Int>()
var nomes = emptyArray<String>()
var generos = emptyArray<String>()
var cabelo = emptyArray<String>()
var cor = emptyArray<String>()
var olhos = emptyArray<String>()
var oculos = emptyArray<String>()
var barba = emptyArray<String>()
var chapeu = emptyArray<String>()
var estaEmJogo = emptyArray<Boolean>()
var jogador = ""
var pontuacaoJogador = 0
var tentativas = 0
val id = emptyArray<Int>()
val pontuacoes = emptyArray<String>()


fun main(){

    var escolha: Int

    do {
        try {
            println(obterMenu())
            escolha = readln().toInt()

            when (escolha) {
                0 -> {
                    println("Ate logo!")
                }
                1 -> {
                    Thread.sleep(500) // Aguarda 2 segundos para visualizar o texto
                    limparConsole()

                    if (nomes.isEmpty())
                    {
                        println("Antes de iniciar o jogo tem de ler as personagens." + "(prima enter para voltar ao menu)")
                        readln()
                    }
                    else{
                        jogar()
                    }
                }
                2 -> {
                    if(lerPersonagens("personagens.txt")){
                        println(mostrarPersonagens())
                    }
                    else{
                        println("Não foi possível ler as personagens.\n Tente novamente!");
                    }

                    println(
                        "Personagens lidas com sucesso." +
                                "(prima enter para voltar ao menu)"
                    )
                    readln()
                }
                3 -> {

                }
                4 -> {
                    lerPontos("pontuacoes.txt")
                    println("(prima enter para voltar ao menu)")
                    readln()
                }
                5 -> {
                    gravarPontos(jogador, pontuacaoJogador, 0, tentativas, "pontuacoes.txt")
                    println("(prima enter para voltar ao menu)\n")
                    readln()
                }
            }
        } catch (e: Exception){
            println("Escolha uma opcao valida dentro do range informado no menu")
            escolha = 99;
        }
    } while (escolha!=0)
}

fun reset() {
    ids = emptyArray()
    nomes = emptyArray()
    generos = emptyArray()
    cabelo = emptyArray()
    cor = emptyArray()
    olhos = emptyArray()
    oculos = emptyArray()
    barba = emptyArray()
    chapeu = emptyArray()
    estaEmJogo = emptyArray()
}

fun limparConsole() {
    print("\u001b[H\u001b[2J")
    System.out.flush()
}

fun obterMenu(): String {
    val menu = "\n##### QUEM E QUEM!!! #####\n\n" +
            "Escolha uma opcao:\n" +
            "1 - Jogar\n" +
            "2 - Ler personagens\n" +
            "3 - Definir personagens\n" +
            "4 - Ver pontos\n" +
            "5 - Gravar pontos\n" +
            "0 - Sair\n"

    return menu
}

fun obterMenuJogo(): String {
    val menuJogo = "\nEscolha uma opcao:\n" +
            "adivinhar   - para adivinhar a personagem\n" +
            "perguntar   - para perguntar retirando personagens do jogo\n" +
            "personagens - para consultar as personagens em jogo\n" +
            "sair        - para sair do jogo\n"

    return menuJogo
}

fun lerPersonagens(nome: String): Boolean {

    // ler de ficheiro
    // se ficheiro não existir retornar false

    val ficheiro = File(nome)

    if (!ficheiro.exists()) {
        return false
    }

    // colocar os dados do ficheiro em memória

    val linhas = ficheiro.readLines()

    val numeroLinhas = linhas[0].toInt()

    ids = Array(numeroLinhas) { -1 }
    nomes = Array(numeroLinhas) { "" }
    generos = Array(numeroLinhas) { "" }
    cabelo = Array(numeroLinhas) { "" }
    cor = Array(numeroLinhas) { "" }
    olhos = Array(numeroLinhas) { "" }
    oculos = Array(numeroLinhas) { "" }
    barba = Array(numeroLinhas) { "" }
    chapeu = Array(numeroLinhas) { "" }
    estaEmJogo = Array(numeroLinhas) { false }

    var indice = 0

    for (i in 2 until linhas.size) {

        val linha = linhas[i]

        val valores = linha.split(":")

        ids[indice] = valores[0].toInt()
        nomes[indice] = valores[1]
        generos[indice] = valores[2]
        cabelo[indice] = valores[3]
        cor[indice] = valores[4]
        olhos[indice] = valores[5]
        oculos[indice] = valores[6]
        barba[indice] = valores[7]
        chapeu[indice] = valores[8]
        estaEmJogo[indice] = true

        indice++

    }

    return true
}

fun lerPontos(nome: String): Boolean {

    val ficheiro = File(nome)

    if (!ficheiro.exists()) return false

    val linhas = ficheiro.readLines()

    val numeroLinhas = linhas[0].toInt()

    if (numeroLinhas < 0) {
        return false
    }

    for (i in 1 until linhas.size) {
        val linha = linhas[i]

        val valores = linha.split("|")

        val nome = valores[0]
        val pontuacaoAtual = valores[1]
        val perguntas = valores[2]
        val tentativas = valores[3]

        println("$nome | $pontuacaoAtual | $perguntas | $tentativas")
    }

    return true
}

fun gravarPontos(
    nomeDoJogador: String, pontuacao: Int, numerosPergutasValidas: Int,
    numTentativas: Int, nomeFich: String,
) {
    if (jogador.isEmpty()) return;

    val ficheiro = File(nomeFich)

    val linhas = ficheiro.readLines().toMutableList()

    val numeroLinhas = linhas[0].toInt() + 1
    linhas[0] = numeroLinhas.toString()

    linhas.add("$jogador|$pontuacaoJogador|$numerosPergutasValidas|$tentativas")
    ficheiro.writeText(linhas.joinToString("\n"))
    jogador = ""
    println("Pontos gravados com sucesso.")
}

fun jogar() {

    limparConsole()
    validarNomeJogador()

    println(
        "Ben-vindo ${jogador}. Eu selecionei uma personagem." +
                " Tente adivinhar quem é fazendo perguntas sobre  suas caracteristicas."
    )

    println(obterMenuJogo()) //Mostra o menu

    do {
        println("O que pretende fazer?")
        var escolha = readln()

        when (escolha) {
            "adivinhar" -> {
                println("Tente adivinhar quem e:")
                val nome = readln()
                val id = gerarPersonagem()
                if (!adivinhar(nome, id)) {
                    tentativas++
                    println("Infelizmente, o seu palpite esta errado. Tente novamente ou faca mais perguntas. \n")
                    pegarRelatorio()
                }
                else{
                    pontuacaoJogador += 20
                    pegarRelatorio()
                    println("Parabens $jogador! Adivinhou. A personagem e o/a $nome. \n")

                    println("Para gravar a sua pontuacao selecione a opcao 5.")
                    println("(prima enter para voltar ao menu)\n")
                    var escolha1 = readln()

                    if (escolha1 == "5") {
                        gravarPontos(jogador, pontuacaoJogador, 0, tentativas, "pontuacoes.txt")
                        println("(prima enter para voltar ao menu)\n")
                        escolha1 = readln()
                    }

                    if (escolha1.isEmpty()) break
                }
            }
            "perguntar" -> {
                println("Identifique o atributo e asua designacao, por exemplo \" genero masculino \"");
                val pergunta = readln()

                perguntar(pergunta, 1)
            }
            "personagens" -> {
                if(lerPersonagens("personagens.txt")){
                    println(mostrarPersonagens())
                }
            }
            "sair" -> {
                println("ATÉ LOGO!!")
            }
        }
    } while (escolha != "sair")
}

fun pegarRelatorio() {
    println("$jogador | $pontuacaoJogador | 0 | $tentativas")
}

fun validarNomeJogador() {
    var nomeJogador: Array<String>

    do {
        println("Introduza o nome do jogador:")
        jogador = readln()

        nomeJogador = jogador.trim().split(" ").toTypedArray()

        if (nomeJogador.size != 2)
            println("Nome invalido, tente novamente.")

    } while (nomeJogador.size != 2)
}


fun mostrarPersonagens(): String {

    var resultado = "Personagens em jogo:\n"

    var nomeMax = "Nome".length
    var generoMax = "Genero".length
    var cabeloMax = "Cabelo".length
    var corMax = "Cor".length
    var olhosMax = "Olhos".length
    val oculosMax = "Oculos".length
    val barbaMax = "Barba".length
    val chapeuMax = "Chapeu".length

    for (indice in nomes.indices) {

        if (!estaEmJogo[indice]) {
            continue
        }

        if (nomes[indice].length > nomeMax) {
            nomeMax = nomes[indice].length
        }

        if (generos[indice].length > generoMax) {
            generoMax = generos[indice].length
        }

        if (cabelo[indice].length > cabeloMax) {
            cabeloMax = cabelo[indice].length
        }

        if (cor[indice].length > corMax) {
            corMax = cor[indice].length
        }

        if (olhos[indice].length > olhosMax) {
            olhosMax = olhos[indice].length
        }
    }

    resultado += "# " + "Nome".padEnd(nomeMax) + " | " +
            "Genero".padEnd(generoMax) + " | " +
            "Cabelo".padEnd(cabeloMax) + " | " +
            "Cor".padEnd(corMax) + " | " +
            "Olhos".padEnd(olhosMax) + " | " +
            "Oculos".padEnd(oculosMax) + " | " +
            "Barba".padEnd(barbaMax) + " | " +
            "Chapeu".padEnd(chapeuMax) + "\n"

    var count = 0

    for (indice in nomes.indices) {

        if (estaEmJogo[indice]) {

            resultado += count.toString() + " " +
                    nomes[indice].padEnd(nomeMax) + " | " +
                    generos[indice].padEnd(generoMax) + " | " +
                    cabelo[indice].padEnd(cabeloMax) + " | " +
                    cor[indice].padEnd(corMax) + " | " +
                    olhos[indice].padEnd(olhosMax) + " | " +
                    oculos[indice].padEnd(oculosMax) + " | " +
                    barba[indice].padEnd(barbaMax) + " | " +
                    chapeu[indice].padEnd(chapeuMax) +
                    "\n"

            count++

        }

    }

    return resultado
}


fun gerarPersonagem(): Int {
    return ids.random()
}

/*  val linhas = ficheiro.readLines()
  while (count<linhas.size){
  }

  val linha = linhas[count]
  val partes = linha.split("|")

  return true


 */


fun obterPontos(): String {
    return ""
}

fun perguntar(pergunta: String, id: Int): Int {

    val argumentos = pergunta.split(" ")

    if (argumentos.size != 0 && argumentos.size != 1 && argumentos.size != 2 && argumentos.size != 3) {
        return -1
    }

    if (id != 1 && id != 2 && id != 3) {
        return -1
    }
    val atributo = argumentos[0]
    if (atributo != "genero" && atributo != "cabelo" && atributo !=
        "cor" && atributo != "olhos" //&& atributo != "oculos" && atributo != "barba" && atributo != "chapeu"
    ) {
        return -1

    }
    val indice = 2
    val caraterista = when (atributo) {
        "genero" -> generos[indice]
        "cabelo" -> cabelo[indice]
        "cor" -> cor[indice]
        "olhos" -> olhos[indice]
        "oculos" -> oculos[indice]
        "barba" -> barba[indice]
        "chapeu" -> chapeu[indice]

        else -> " "

    }

    return -1

}

fun serNomeValido(nomeDoJogador: String): Boolean {
    return false
}

fun retirarDeJogo(idretiraDeJogo: Int) {

    var indice = 0
    for (id in ids) {

        if (id == idretiraDeJogo) {
            break
        }

        indice++

    }

    estaEmJogo[indice] = false

}

fun adivinhar(nome: String, id: Int): Boolean {
    return nomes.contains(nome)
}

fun encontrarElementoERetirar(lista: Array<String>, elemento: String) {
    var retirados = 0;
    val mensagem = "Foram retiradas do jogo ${retirados} personagens"
    for (posicao in 0 until lista.size) {
        if (lista[posicao] == elemento) {
            retirados++
            retirarDeJogo(posicao)
        }

    }
    println(mensagem);
    println(mostrarPersonagens())

}
