#language: pt

Funcionalidade: Gerenciamento de Reservas

  Cenário: Visualizar reservas de mesas
    Dado que o restaurante tem reservas para o dia
    Quando o restaurante acessar a lista de reservas
    Então ele verá todas as reservas para a data e horário específicos

  Cenário: Atualizar o status de uma reserva
    Dado que o restaurante tem uma reserva confirmada
    Quando ele mudar o status da reserva para Confirmada ou Cancelada
    Então o status da reserva será atualizado no sistema