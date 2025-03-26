#language: pt
Funcionalidade: Cadastro de Restaurantes

  Cenário: Cadastro de um restaurante com sucesso
    Dado que o restaurante deseja se cadastrar
    Quando ele fornecer as informações de nome, localização, tipo de cozinha, horários de funcionamento e capacidade
    Então o restaurante será cadastrado com sucesso no sistema
    E as informações do restaurante serão armazenadas corretamente