# salesreport
SalesReport

===================================

###Objetivo da prova:

O objetivo da prova é testarmos suas habilidades em desenvolvimento de software. 
Iremos avaliar mais do que o funcionamento da solução proposta, avaliaremos a sua abordagem, a
sua capacidade analítica, boas práticas de engenharia de software, performance e
escalabilidade da solução.

### Descrição:
Criar um sistema de análise de dados de venda que irá importar lotes de arquivos e produzir
um relatório baseado em informações presentes no mesmo.
Existem 3 tipos de dados dentro dos arquivos e eles podem ser distinguidos pelo seu
identificador que estará presente na primeira coluna de cada linha, onde o separador de
colunas é o caractere “ç”.

###Dados do vendedor

Os dados do vendedor possuem o identificador 001 e seguem o seguinte formato:

001çCPFçNameçSalary

### Dados do cliente

Os dados do cliente possuem o identificador 002 e seguem o seguinte formato:

002çCNPJçNameçBusiness Area

### Dados de venda

Os dados de venda possuem o identificador 003 e seguem o seguinte formato:

003çSale IDç[Item ID-Item Quantity-Item Price]çSalesman name

### Exemplo de conteúdo total do arquivo:

001ç1234567891234çPedroç50000

001ç3245678865434çPauloç40000.99

002ç2345675434544345çJose da SilvaçRural

002ç2345675433444345çEduardo PereiraçRural

003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro

003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo

===================================
### Regras de negócio que foram implementadas:

-> O sistema somente importa dados que estejam no formato correto;

-> Todos os dados ficam armazenados em uma base MongoDB em memória;

-> Uma venda é válida somente se o vendedor informado existir no arquivo ou já estiver cadastrado na base de dados;

### Testes:

-> Foi criado o teste unitário da rota do fluxo principal do sistema executado com sucesso;

### Versão do Java: 

Oracle Java JDK 8.x 64bits

### Principais tecnologias e frameworks utilizados: 

Java 8, Spring Boot, Apache Camel, MongoDB, Junit, Mockito.

### Execução a partir dos fontes:

-> Pasta inicial "salesreport";

```sh
./gradlew clean test bootJar

cd build/libs/

java -jar salesreport-2.0.0-SNAPSHOT.jar
```

