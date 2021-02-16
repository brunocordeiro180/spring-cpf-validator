## Validação de CPF 

### Faça o cadastro através do endereço:

http://localhost:8080/signup

Passando como parâmentros o seu username e senha

### Faça o login através do endereço:

http://localhost:8080/login

### Verifique um CPF ou CNPJ através da url:

http://localhost:8080/validate

Passando como argumentos:

* value : Número que deseja validar

* tipo : 'CPF' para validar CPFs e 'CNPJ' para validar CNPJs 

### Verifique o total de requisições por mês:

http://localhost:8080/requisitions

Onde a resposta contêm o nome do usuário seguido pelo custo total de requisições a cada mês

Exemplo:

   ```JS
   {
    "larissa": {
        "1": "0.4"
    },
    "bruno": {
        "0": "0.1", 
        "1": "0.3"
    }
}
   ```

No exemplo acima o usuário "larissa" teve um custo de 0.40 centavos no mês 1 (Fevereito) e o usuário "bruno" teve um custo de 0.10 centavos no mês 0 (Janeiro)
