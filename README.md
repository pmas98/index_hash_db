# Implementação de Índice Hash em Java

Este projeto implementa um índice hash para gerenciar grandes volumes de dados.

## 📋 Resumo do Projeto

O objetivo é implementar um índice HASH utilizando Programação Orientada a Objetos (POO) para gerenciar um grande volume de dados. 

### ✅ Requisitos Implementados

- **Estruturas de Dados e Entidades**:
  - ✅ **Tupla**: Representa uma linha da tabela, contendo o valor da chave de busca e os dados da linha
  - ✅ **Tabela**: Armazena todas as tuplas carregadas a partir do arquivo de dados
  - ✅ **Página**: Estrutura que representa a alocação física da tabela na mídia de armazenamento
  - ✅ **Bucket**: Mapeia as chaves de busca para os endereços das páginas
  - ✅ **Função Hash**: Mapeia uma chave de busca para um endereço de bucket

- **Funcionalidades da Aplicação**:
  - ✅ **Construção do Índice**: Carregar o arquivo de dados e construir o índice hash

- **Cálculos e Estatísticas**:
  - ✅ **Número de Buckets (NB)**: Calculado com base na fórmula NB > NR / FR
  - ✅ **Taxa de Colisões**: Calculada e exibida durante a construção do índice
  - ✅ **Taxa de Overflows**: Calculada e exibida para transbordamento dos buckets
  - ✅ **Custo da Busca (Índice)**: Estimado em número de acessos a disco
  - ✅ **Custo do Table Scan**: Calculado em páginas lidas para busca sequencial

## 🏗️ Estrutura do Projeto

```
src/main/java/hashindex/
├── core/           # Estruturas de dados principais
│   ├── Tuple.java      # Representa uma linha da tabela
│   ├── Table.java      # Armazena todas as tuplas
│   ├── Page.java       # Alocação física na mídia
│   ├── Bucket.java     # Mapeia chaves para páginas
│   └── HashIndex.java  # Classe principal do índice
├── functions/      # Implementações de funções hash
│   ├── HashFunction.java           # Interface para funções hash
│   ├── SimpleHashFunction.java     # Usa hashCode do Java
│   ├── DivisionHashFunction.java   # Hashing por divisão
│   └── MultiplicationHashFunction.java # Hashing por multiplicação
├── utils/          # Classes utilitárias
│   └── DataLoader.java  # Carregamento de dados de arquivos
└── demo/           # Aplicações de demonstração
    ├── HashIndexDemo.java           # Demo básico com dados de exemplo
    └── HashIndexDemoWithFile.java   # Demo com arquivos reais
```

### Classes Principais (`hashindex.core`)

1. **Tuple** - Representa uma linha da tabela contendo a chave de busca e os dados da linha
2. **Table** - Armazena todas as tuplas carregadas do arquivo de dados
3. **Page** - Representa a alocação física da tabela na mídia de armazenamento
4. **Bucket** - Mapeia chaves de busca para endereços de páginas
5. **HashIndex** - Classe principal que gerencia a funcionalidade do índice hash

### Implementações de Funções Hash (`hashindex.functions`)

- **SimpleHashFunction** - Usa o método hashCode integrado do Java
- **DivisionHashFunction** - Implementa hashing baseado em divisão
- **MultiplicationHashFunction** - Implementa hashing baseado em multiplicação usando a razão áurea

### Classes Utilitárias (`hashindex.utils`)

- **DataLoader** - Utilitário para carregar dados de arquivos

### Aplicações de Demonstração (`hashindex.demo`)

- **HashIndexDemo** - Demonstração básica com dados de exemplo
- **HashIndexDemoWithFile** - Demonstração aprimorada que pode trabalhar com arquivos de dados reais

## 🔧 Funcionalidades

### Funcionalidades Principais
- **Construção do Índice**: Carregar dados e construir o índice hash
- **Resolução de Colisões**: Sondagem linear para buckets de overflow
- **Estatísticas**: Acompanhar taxas de colisão, overflow e custos de busca

### Estatísticas Calculadas
- Número de Buckets (NB > NR / FR)
- Taxa de Colisões
- Taxa de Overflows
- Custo da Busca (páginas lidas) tanto para índice quanto para table scan

## 🚀 Como Usar

### Uso Básico

```java
// Criar dados de exemplo
List<Tuple> tuples = HashIndexDemo.createSampleData();

// Escolher função hash
HashFunction hashFunction = new SimpleHashFunction();

// Criar índice hash
HashIndex hashIndex = new HashIndex(pageSize, tuplesPerBucket, hashFunction);

// Construir índice
hashIndex.buildIndex(tuples);
```

### Trocando Funções Hash

```java
// Função hash simples
HashFunction hashFunction = new SimpleHashFunction();

// Função hash por divisão
HashFunction hashFunction = new DivisionHashFunction();

// Função hash por multiplicação
HashFunction hashFunction = new MultiplicationHashFunction();
```

### Carregando Dados de Arquivo

```java
// Carregar todas as palavras do arquivo
List<Tuple> tuples = DataLoader.loadWordsFromFile("words.txt");

// Carregar subconjunto de palavras
List<Tuple> tuples = DataLoader.loadWordsFromFile("words.txt", 1000);
```

## 🏃‍♂️ Executando as Demonstrações

### Usando Make (Linux/macOS)

```bash
# Compile and run basic demo
make demo

# Compile and run file-based demo
make demo-file

# Run GUI frontend
make frontend

# Show project structure
make tree

# Clean build files
make clean
```

### Windows (cmd/PowerShell)

```bat
:: Compile
make.cmd compile

:: Run basic demo
make.cmd demo

:: Run file-based demo
make.cmd demo-file

:: Run GUI frontend
make.cmd frontend

:: Show project structure
make.cmd tree

:: Clean build files
make.cmd clean
```

### Manual Compilation

```bash
# Compile all files
javac -cp . src/main/java/hashindex/**/*.java

# Run basic demo
java -cp src/main/java hashindex.demo.HashIndexDemo

# Run file-based demo
java -cp src/main/java hashindex.demo.HashIndexDemoWithFile
```

## 🔧 Parâmetros de Configuração

- **Page Size**: Número de tuplas por página
- **Tuples per Bucket**: Número de tuplas que podem ser armazenadas em cada bucket
- **Hash Function**: Escolha entre implementações disponíveis ou crie a sua própria

## 📈 Extendendo a Implementação

### Adicionando Novas Funções Hash

1. Crie uma nova classe em `src/main/java/hashindex/functions/`:

```java
package hashindex.functions;

public class MyHashFunction implements HashFunction {
    @Override
    public int hash(String key, int numberOfBuckets) {
        // Sua implementação de hash aqui
        return hashValue;
    }
}
```

2. Use-o em seu código:

```java
import hashindex.functions.MyHashFunction;
HashFunction hashFunction = new MyHashFunction();
```

### Carregamento de Dados Personalizado

Modifique a classe `DataLoader` ou crie sua própria lógica de carregamento de dados para trabalhar com diferentes formatos de arquivo ou fontes de dados.

## 📋 Requisitos Atendidos

✅ **Implementação POO**: Todas as classes seguem os princípios de POO
✅ **Facilidade de Troca de Funções Hash**: Design baseado em interface
✅ **Gerenciamento de Tuplas**: Representação completa de tuplas
✅ **Gerenciamento de Páginas**: Alocação física de armazenamento
✅ **Gerenciamento de Buckets**: Mapeamento de chaves para páginas
✅ **Resolução de Colisões**: Implementação de sondagem linear
✅ **Estatísticas**: Métricas de rastreamento abrangentes
✅ **Carregamento de Arquivos**: Suporte para arquivos de dados grandes

## 🎯 Próximos Passos

Esta implementação fornece uma base sólida para o índice hash. A próxima fase seria adicionar:
- Interface gráfica para visualização
- Exibição de estatísticas em tempo real
- Otimização de desempenho
- Estratégias de resolução de colisões mais sofisticadas
