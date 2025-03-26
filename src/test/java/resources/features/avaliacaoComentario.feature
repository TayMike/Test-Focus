#language: pt

Funcionalidade: Avaliações e Comentários

  Cenário: Deixar uma avaliação e comentário sobre o restaurante
    Dado que o usuário fez uma visita ao restaurante
    Quando o usuário avaliar o restaurante de 1 a 5 estrelas
    E deixar um comentário sobre a sua experiência
    Então a avaliação e o comentário serão registrados no sistema
    E outros usuários poderão visualizar a avaliação