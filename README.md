# Provedor de serviços de vendas
## 1 - Informações de acesso

O provedor de serviços de vendas encontra-se hospedado no seguinte endereço:

https://sales-provider.appspot.com



## 2 - Autenticação e token de acesso

Todos os serviços dessa aplicação utilizam o mecanismo de autenticação OAuth 2.0.

Para obter o token de acesso, a aplicação cliente deve fazer uma requisição ao servidor com as seguintes informações:

**Método:** POST

**URL:** http://sales-provider.appspot.com/oauth/token

**Cabeçalhos:** 

* Content-Type: application/x-www-form-urlencoded


* Authorization: Basic c2llY29sYTptYXRpbGRl

**Corpo da mensagem:**

```
grant_type=password&username=matilde@siecola.com.br&password=matilde
```

Os valores dos campos **username** e **password** devem ser trocados para as credenciais de acesso do usuário que deseja obter o token.

A resposta a essa autenticação é o token de acesso no seguinte formato:

```Son
{
    "access_token": "13968c9d-e3ef-4b32-b119-b6d93f59963f",
    "token_type": "bearer",
    "refresh_token": "e567d8b8-def6-4f37-8b6d-d532a47a08ac",
    "expires_in": 3599,
    "scope": "read write"
}
```



> **Usuário com papel ADMIN
>
> O provedor de serviço de vendas já possui um usuário com papel de administrador, que não pode ser alterado nem apagado. Seu login é `matilde@siecola.com.br` e sua senha é `matilde`.



## 3 - Serviço de gerenciamento de usuários

### a) Listar todos os usuários

**Método:** GET

**URL:** https://sales-provider.appspot.com/api/users

**Permissão de acesso:** somente usuário com papel ADMIN

**Exemplo de resposta:**

```json
[
    {
        "id": 5741031244955648,
        "email": "doralice@siecola.com.br",
        "password": "doralice",
        "gcmRegId": null,
        "lastLogin": null,
        "lastGCMRegister": null,
        "role": "USER",
        "enabled": true
    },
    {
        "id": 5668600916475904,
        "email": "matilde@siecola.com.br",
        "password": "matilde",
        "gcmRegId": null,
        "lastLogin": null,
        "lastGCMRegister": null,
        "role": "ADMIN",
        "enabled": true
    }
]
```



### b) Criar um usuário

**Método:** POST

**URL:** https://sales-provider.appspot.com/api/users

**Permissão de acesso:** somente usuário com papel ADMIN

**Exemplo de corpo de requisição:**

```json
{
    "email": "doralice@siecola.com.br",
    "password": "doralice",
    "gcmRegId": null,
    "lastLogin": null,
    "lastGCMRegister": null,
    "role": "USER",
    "enabled": true
}
```

**Exemplo de mensagem de resposta:**

```json
{
    "id": 5629499534213120,
    "email": "doralice@siecola.com.br",
    "password": "doralice",
    "gcmRegId": null,
    "lastLogin": null,
    "lastGCMRegister": null,
    "role": "USER",
    "enabled": true
}
```



### c) Alterar um usuário pelo e-mail

**Método:** PUT

**URL:** https://sales-provider.appspot.com/api/users/byemail?email=doralice@siecola.com.br

**Permissão de acesso:** somente usuário com papel ADMIN ou o próprio usuário alvo da operação

**Exemplo de corpo de requisição:**

```json
{
    "id": 5629499534213120,
    "email": "doralice@siecola.com.br",
    "password": "doralice",
    "gcmRegId": null,
    "lastLogin": null,
    "lastGCMRegister": null,
    "role": "USER",
    "enabled": true
}
```

**Exemplo de mensagem de resposta:**

```json
{
    "id": 5629499534213120,
    "email": "doralice@siecola.com.br",
    "password": "doralice",
    "gcmRegId": null,
    "lastLogin": null,
    "lastGCMRegister": null,
    "role": "USER",
    "enabled": true
}
```



### d) Buscar um usuário pelo e-mail

**Método:** GET

**URL:** https://sales-provider.appspot.com/api/users/byemail?email=doralice@siecola.com.br

**Permissão de acesso:** somente usuário com papel ADMIN ou o próprio usuário alvo da operação

**Exemplo de mensagem de resposta:**

```json
{
    "id": 5629499534213120,
    "email": "doralice@siecola.com.br",
    "password": "doralice",
    "gcmRegId": null,
    "lastLogin": null,
    "lastGCMRegister": null,
    "role": "USER",
    "enabled": true
}
```



### e) Apagar um usuário pelo e-mail

**Método:** DELETE

**URL:** https://sales-provider.appspot.com/api/users/byemail?email=doralice@siecola.com.br

**Permissão de acesso:** somente usuário com papel ADMIN ou o próprio usuário alvo da operação

**Exemplo de mensagem de resposta:**

```json
{
    "id": 5629499534213120,
    "email": "doralice@siecola.com.br",
    "password": "doralice",
    "gcmRegId": null,
    "lastLogin": null,
    "lastGCMRegister": null,
    "role": "USER",
    "enabled": true
}
```



## 4 - Serviço de gerenciamento de produtos



## 5 - Serviço de gerenciamento de pedidos

