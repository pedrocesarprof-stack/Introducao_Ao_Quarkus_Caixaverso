# Coleção Bruno - Caixaverso API

## Como importar no Bruno

1. Abra o Bruno
2. Clique em **Open Collection**
3. Selecione esta pasta: `docs/bruno`

---

## Ambiente

Selecione o ambiente **Local** antes de executar os requests.
Ele define `base_url = http://localhost:8080`.

---

## Usuários disponíveis (Basic Auth)

| Usuário   | Senha        | Roles              |
|-----------|--------------|--------------------|
| `gerente` | `gerente123` | Gerente + Usuario  |
| `usuario` | `usuario123` | Usuario            |

---

## Fluxo de teste recomendado

### 1. Cadastrar um cliente
- **POST** `/clientes` → copie o `id` UUID retornado no response

### 2. Salvar o `cliente_id`
- No Bruno, vá em **Environments → Local** e adicione a variável `cliente_id` com o UUID copiado

### 3. Contratar um empréstimo
- **POST** `/emprestimos` com `tipoAmortizacao: "SAC"` ou `"PRICE"`
- Copie o `id` UUID do empréstimo retornado

### 4. Salvar o `emprestimo_id`
- Adicione `emprestimo_id` no ambiente Local

### 5. Listar e cancelar
- **GET** `/emprestimos` → lista todos
- **DELETE** `/emprestimos/{{emprestimo_id}}` → apenas com usuário `gerente`

---

## Estrutura da coleção

```
📁 Caixaverso API
├── 📁 Clientes
│   ├── Cadastrar Cliente                    POST   /clientes
│   ├── Listar Todos Clientes               GET    /clientes            (Gerente)
│   ├── Buscar Cliente por Documento        GET    /clientes/{doc}
│   └── Cadastrar Cliente - Erro Validacao  POST   /clientes            (400)
├── 📁 Emprestimos
│   ├── Contratar Emprestimo - SAC          POST   /emprestimos
│   ├── Contratar Emprestimo - PRICE        POST   /emprestimos
│   ├── Listar Todos Emprestimos            GET    /emprestimos
│   ├── Cancelar Emprestimo                 DELETE /emprestimos/{id}    (Gerente)
│   ├── Cancelar Emprestimo - Erro 403      DELETE /emprestimos/{id}    (403)
│   └── Contratar Emprestimo - Erro Validacao POST  /emprestimos        (400)
└── 📁 Reguladora
    └── Simular Taxa                        POST   /reguladora/taxas
```

