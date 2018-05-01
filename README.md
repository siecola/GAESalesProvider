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

```xml
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



## 3 - Serviço de gerenciamento de usuários



## 4 - Serviço de gerenciamento de produtos



## 5 - Serviço de gerenciamento de pedidos

