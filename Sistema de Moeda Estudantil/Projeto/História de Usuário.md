**Cadastro de Aluno**

Como: aluno,

Quero: realizar o meu cadastro no sistema de mérito,

Para que: eu possa participar do sistema e receber moedas.


Critérios de Aceitação:


O aluno deve poder se cadastrar com as seguintes informações: nome, email, CPF, RG, endereço, instituição de ensino e curso.


**Cadastro de Empresa Parceira**

Como: empresa parceira,

Quero: me cadastrar no sistema,

Para que: eu possa oferecer vantagens aos alunos em troca de moedas.


Critérios de Aceitação:


A empresa parceira deve poder se cadastrar com informações como nome da empresa, CNPJ e endereço.


**Envio de Moedas**

Como: professor,

Quero: enviar moedas aos alunos,

Para que: eu possa reconhecer e premiar seu desempenho em sala de aula.


Critérios de Aceitação:


O professor deve poder selecionar um aluno e enviar moedas a ele.

O sistema deve verificar se o professor possui saldo suficiente antes de enviar as moedas.

O proefessor deve poder enviar uma mensagem de reconhecimento


**Consulta de Extrato (Aluno)**

Como: aluno,

Quero: consultar meu extrato de moedas,

Para que: eu possa ver quantas moedas tenho e as transações que realizei.

Critérios de Aceitação:


O aluno deve poder visualizar o saldo de moedas atual.


**Consulta de Extrato (Professor)**

Como: professor,

Quero: consultar meu extrato de moedas,

Para que: eu possa verificar quantas moedas distribuí e quanto saldo ainda tenho.

Critérios de Aceitação:


O professor deve poder visualizar o saldo de moedas atual.

O professor deve ver uma lista de transações, incluindo os alunos que receberam moedas e a quantidade enviada.


**Resgate de Vantagem**

Como: aluno,

Quero: trocar minhas moedas por uma vantagem,

Para que: eu possa usufruir dos benefícios oferecidos pelas empresas parceiras.

Critérios de Aceitação:


O aluno deve poder visualizar uma lista de vantagens disponíveis e seus custos em moedas.

O aluno deve poder selecionar uma vantagem e trocá-la por moedas, desde que tenha saldo suficiente.

Deve ser gerado um codigo de troca para o aluno 


**Gerenciamento de Saldo de Moedas (Professor)**

Como: professor,

Quero: receber mil moedas a cada semestre,

Para que: eu possa distribuir entre os alunos como reconhecimento.

Critérios de Aceitação:


O professor deve receber mil moedas no início de cada semestre.

Se o professor não utilizar todas as moedas, o saldo não utilizado deve ser acumulado para o semestre seguinte.


**Login no Sistema**

Como: usuário (aluno, professor ou empresa parceira),

Quero: fazer login no sistema,

Para que: eu possa acessar minhas funcionalidades de acordo com meu perfil.

Critérios de Aceitação:


O sistema deve autenticar o usuário com base no nome de usuário e senha.


**Cadastro de Vantagem (Empresa Parceira)**

Como: empresa parceira,

Quero: cadastrar vantagens no sistema,

Para que: os alunos possam trocá-las por moedas.

Critérios de Aceitação:


A empresa parceira deve poder cadastrar uma nova vantagem com os seguintes detalhes: descrição, foto e custo em moedas.


**Notificação**

Como: aluno,

Quero: ser notificado quando receber moedas ou realizar uma troca,

Para que: eu possa acompanhar minhas transações e cupons.

Como: empresa parceira,

Quero: ser notificada quando um aluno resgatar uma vantagem,

Para que: eu possa validar o código no momento da troca.

Critérios de Aceitação:


O aluno deve ser notificado ao ser reconhecido com moedas por um professor.

O aluno deve ser notificado ao realizar a troca de moedas por uma vantagem, com o cupom e código de troca.

A empresa parceira deve ser notificada sobre a troca com os detalhes e o código gerado para conferência.
