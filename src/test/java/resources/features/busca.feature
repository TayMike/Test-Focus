#language: pt

Funcionalidade: Busca de Restaurantes

  Cenário: Buscar restaurante pelo nome
    Dado que o usuário está na página de busca de restaurantes pelo nome
    Quando ele digitar o nome do restaurante
    Então ele verá os resultados que correspondem ao nome fornecido

  Cenário: Buscar restaurante pela localização
    Dado que o usuário está na página de busca de restaurantes pela localizacao
    Quando ele fornecer a localização desejada
    Então ele verá os restaurantes disponíveis naquela localização

  Cenário: Buscar restaurante pelo tipo de cozinha
    Dado que o usuário está na página de busca de restaurantes pelo tipo cozinha
    Quando ele selecionar o tipo de cozinha desejado
    Então ele verá os restaurantes que oferecem aquele tipo de cozinha