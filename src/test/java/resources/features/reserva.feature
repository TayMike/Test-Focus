#language: pt

Funcionalidade: Reserva de Mesas

  Cenário: Realizar uma reserva de mesa com sucesso
    Dado o restaurante tem mesas disponíveis
    E que o usuário está na página de reservas de um restaurante
    Quando o usuário escolher a data, o horário e o número de pessoas
    Então a reserva será confirmada com sucesso
    E o restaurante será notificado sobre a reserva