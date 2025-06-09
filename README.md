# HotelManagement

Sistema de Gerenciamento de Hotel desenvolvido em Java, utilizando Swing para interface gráfica e SQLite como banco de dados local.

## Funcionalidades

- Cadastro e autenticação de usuários (admin e staff)
- Gerenciamento de quartos (cadastro, edição, exclusão)
- Cadastro de hóspedes
- Reservas de quartos
- Interface gráfica moderna com FlatLaf

## Tecnologias Utilizadas

- Java 17
- Swing (FlatLaf)
- SQLite (sqlite-jdbc)
- Maven

## Estrutura do Projeto

```
hotelmanagement/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/starlight/hotelmanagement/
│       │       ├── gui/
│       │       ├── model/
│       │       ├── util/
│       │       └── Hotelmanagement.java
│       └── resources/
│           └── db/
│               └── hotel.db
├── pom.xml
```

## Como Executar

### 1. Compilar e gerar o JAR

No terminal, execute:

```sh
mvn clean package
```

O arquivo JAR será gerado em `target/hotelmanagement-1.0-SNAPSHOT-jar-with-dependencies.jar`.

### 2. Executar o sistema

No terminal, execute:

```sh
java -jar target/hotelmanagement-1.0-SNAPSHOT-jar-with-dependencies.jar
```

> **Dica:**  
> Se quiser abrir sem terminal, crie um script `.bat` (Windows) ou `.command` (Mac/Linux) com o comando acima.

### 3. Requisitos

- Java 17 ou superior instalado  
- Não é necessário instalar SQLite, pois o driver já está incluso no JAR

## Observações

- O banco de dados (`hotel.db`) é criado automaticamente na primeira execução, caso não exista.
- O sistema é multiplataforma (Windows, Mac, Linux).

## Créditos

Desenvolvido por Bruna e colaboradores.

---