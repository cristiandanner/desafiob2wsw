# DESAFIO API STAR WARS - B2W

## Índice

 <ol>
  <li><a href="#Desafio">Desafio</a></li>
  <li><a href="#Tecnologias">Tecnologias</a></li>
  <li><a href="#Executar">Executar</a></li>
  <li><a href="#Testes">Testes</a></li>
  <li><a href="#Funcionalidades">Funcionalidades</a>
    <ol>
      <li><a href="#Inserir">Inserir planeta</a></li>
      <li><a href="#Listar">Listar Planetas</a></li>
      <li><a href="#BuscarPorId">Buscar Planeta por Id</a></li>
      <li><a href="#ApagarPorId">Apagar Planeta por Id</a></li>
      <li><a href="#AtualizarPorNome">Atualizar Planeta por Nome</a></li>
    </ol>
  </li>
  <li><a href="#Consideracoes">Considerações</a>
</ol>

### <a name="Desafio">1. Desafio</a> 

O desafio proposto é de fazer uma API capaz de armazenar informações de planetas de Star Wars (nome, clima e terreno) em um banco de dados NoSQL. Nela pode-se excluir o planeta pelo id, obter o planeta pelo id, nome ou a coleção completa de planetas (respeitando a paginação). Além disso, essa API consulta a [SWAPI](https://swapi.co/api/) para obter a quantidades de filmes em que o planeta em questão aparece.

### <a name="Tecnologias">2. Tecnologias</a> 

Para o desafio foi utilizado Java, o framework Spring Boot (Spring Tools Suite) obtido pelo Marketplace da IDE Eclipse, como gerenciador de dependências foi utilizado o Maven. 
Para a persistência dos dados foi utilizado o banco de dados NoSQL MongoDB. Para testes da API automatizados foi utilizado JUnit e para testes manuais o Postman.

### <a name="Executar">3. Executar</a>  

Para executar o desafio será necessario ter instalado o [Java SDK 8](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html), o [Eclipse](https://www.eclipse.org/downloads/), o [MongoDB](https://www.mongodb.com/download-center?jmp=nav#community).
Além disso, obter através do Markteplace do Eclipse, o Spring Boot (STS) e o Maven se necessário.

Após tudo baixado e configurado:

<ol>
  <li>Abrir a aplicação do MongoDB</li>
  <li>Importar o desafio (clonado) no Eclipse: File -> Open Projects From File System... -> Directory -> Select Folder -> Finish</li>
  <li>Botão direito na classe Aplications: Run As -> Java Apllication</li>  
</ol>

A porta escolhida para rodar a aplicação foi 8102, caso queira trocar a porta, mudar no arquivo application.properties.

### <a name="Testes">4. Testes</a>

Para executar os testes:
<ol>
  <li>Abrir a aplicação do MongoDB</li>
  <li>Ir em src/test/java do projeto no Eclipse</li>
  <li>Botão direito na classe PlanetControllerTest: Run As -> JUnit Test</li>  
</ol>

 O sistema efetuará os testes, retornando verde se tudo deu certo ou vermelho se algo deu errado.

### <a name="Funcionalidades">5. Funcionalidades</a>

#### <a name="Inserir">I. Inserir planeta:</a>

### POST api/planets/

**Descrição:** Insere o planeta na base de dados.  

**Request:** Planeta contendo nome, clima e terreno em formato JSON.  

Ex: 
```
{
  "name": "Kamino",
  "climate": "temperate",
  "terrain": "ocean"
}
```  

**Status Codes:** 
- 201 (Created) - Inserido com sucesso.
- 404 (Not found) - Nome do planeta não encontrado na SWAPI.
- 409 (Conflict) - Planeta com mesmo nome já salvo anteriormente.

**Response:** Planeta em formato JSON.  

Ex: `{"id":"5e56886ccd1c150b18a2eb41","name":"Kamino","climate":"temperate":"ocean","filmsCount":1}`

#### <a name="Listar">II. Listar Planetas:</a>

### GET api/planets?name=Kamino&page=2&per_page=40

**Descrição:** Lista todos os planetas salvos. Os parâmetros `name`, `page` e `per_page` não são obrigatórios. Caso `page` não seja informado, o valor padrão de `page` é 1 e `per_page` 50. Se `name` não for informado ele lista todos os registros do banco de forma paginada.

**Parâmetros Request:** 
- `name` (não obrigatório): procura pelo nome do planeta.
- `page` (não obrigatório): indica em que página será procurado o planeta.
- `per_page` (não obrigatório): indica quantos registros por página será procurado o planeta.

**Status Codes:** 
- 200 (OK)  
- 404 (Not found) - Nome do planeta não encontrado na base de dados.

**Response:** Lista de planeta paginados ou o planeta filtrado por nome em formato JSON.

#### <a name="BuscarPorId">III. Buscar Planeta por Id:</a>

### GET api/planets/{id}

**Descrição:** Busca o planeta pelo Id. 

**Parâmetros Request:** 
- `id`: busca planeta pelo Id.

**Status Codes:** 
- 200 (OK)  
- 404 (Not found) - Id do planeta não encontrado na base de dados.

**Response:** Planeta em formato JSON.

#### <a name="ApagarPorId">IV. Apagar Planeta por Id:</a>

### DELETE api/planets/{id}

**Descrição:** Apaga o planeta pelo Id.  

**Parâmetros Request:**
- `id`: busca planeta pelo Id. 

**Status Codes:** 
- 204 (No Content) - Sucesso
- 404 (Not found) - Id do planeta não encontrado na base de dados.

**Response:** ```sem conteúdo```.  

#### <a name="AtualizarPorNome">V. Atualizar Planeta por Nome:</a>

### PUT api/planets/

**Descrição:** Atualiza o clima e o terreno pelo nome do Planeta. Apesar de não estar no escopo do desafio, adicionei para fechar o CRUD.  

**Request:** Json contendo o nome do planeta, o clima e o terreno.

Ex: 
```
{
  "name": "Kamino",
  "climate": "rock, desert, mountain, barren",
  "terrain": "temperate, arid"
}
```  

**Status Codes:** 
- 200 (OK) - Atualizado com sucesso.
- 404 (Not found) - Nome do planeta não encontrado na base de dados.

**Response:** Planeta em formato JSON.  

Ex: `{"id":"5e56886ccd1c150b18a2eb41","name":"Kamino","climate":"rock, desert, mountain, barren":"temperate, arid","filmsCount":1}`

### <a name="Consideracoes">6. Considerações</a>

No bate-papo que tive quando fui no BIT, foi exposto a importância do DevOps, então após meu commit inicial vou tentar configurar o Docker para facilitar o teste de vocês. Abraços!
