# Histórias de usuário 

**Gerar currículos**

Como secretaria,
Quero gerar currículos para os alunos,
Para que eu possa fornecer um documento com as disciplinas que o aluno cursou e suas notas.

Critérios de Aceitação:

A secretaria deve poder gerar um currículo para cada aluno
O currículo deve refletir as informações mais atualizadas do sistema de matrículas.
A secretaria deve ter a capacidade de revisar e corrigir informações no currículo.

**Gerenciar Alunos**

Como secretaria,
Quero gerenciar o cadastro de alunos no sistema,
Para que eu possa acessar, atualizar e remover informações sobre os alunos conforme necessário.

Critérios de Aceitação:

Criar: A secretaria deve poder adicionar novos alunos ao sistema com informações como nome, matrícula, curso, e informações de contato.
Ler: A secretaria deve poder visualizar informações detalhadas dos alunos cadastrados no sistema.
Atualizar: A secretaria deve poder atualizar informações dos alunos, como curso, dados de contato e status de matrícula.
Deletar: A secretaria deve poder remover alunos do sistema, garantindo que todas as informações relacionadas sejam devidamente excluídas.
O sistema deve verificar se a matrícula do aluno já existe antes de concluir o cadastro.

**Gerenciar Professores**

Como secretaria,
Quero gerenciar o cadastro de professores no sistema,
Para que eu possa acessar, atualizar e remover informações sobre os docentes conforme necessário.

Critérios de Aceitação:

Criar: A secretaria deve poder adicionar novos professores ao sistema com informações como nome, matrícula, departamento e informações de contato.
Ler: A secretaria deve poder visualizar informações detalhadas dos professores cadastrados no sistema.
Atualizar: A secretaria deve poder atualizar informações dos professores, como departamento e dados de contato.
Deletar: A secretaria deve poder remover professores do sistema, garantindo que todas as informações relacionadas sejam devidamente excluídas.
O sistema deve verificar se a matrícula do professor já existe antes de concluir o cadastro.


**Gerenciar Disciplinas**

Como secretaria,
Quero gerenciar o cadastro de disciplinas no sistema,
Para que eu possa acessar, atualizar e remover informações sobre as disciplinas conforme necessário.

Critérios de Aceitação:

Criar: A secretaria deve poder adicionar novas disciplinas ao sistema com informações como código da disciplina, nome, créditos e pré-requisitos.
Ler: A secretaria deve poder visualizar informações detalhadas das disciplinas cadastradas no sistema.
Atualizar: A secretaria deve poder atualizar informações das disciplinas, como nome, créditos e pré-requisitos.
Deletar: A secretaria deve poder remover disciplinas do sistema, garantindo que todas as informações relacionadas sejam devidamente excluídas.
O sistema deve verificar se o código da disciplina já existe antes de concluir o cadastro.

**Escolher Disciplinas**

Como um aluno,
Quero me matricular em até 4 disciplinas obrigatórias e 2 optativas,
Para que eu possa cumprir com as exigências do meu curso.

Critérios de Aceitação:

O sistema deve verificar se o aluno já está matriculado em 4 disciplinas obrigatórias antes de permitir a matrícula em mais disciplinas.
O sistema deve exibir a lista de disciplinas disponíveis e seu status (se a matrícula está aberta ou fechada).
O aluno deve receber uma confirmação de matrícula bem-sucedida.


**Cancelar Matrícula na disciplina**

Como um aluno,
Quero cancelar uma ou mais matrículas,
Para que eu possa ajustar meu plano de estudos de acordo com minhas necessidades.

Critérios de Aceitação:

O aluno pode cancelar matrículas em disciplinas até o final do período de matrículas.
O sistema deve atualizar a lista de disciplinas em que o aluno está matriculado e permitir a matrícula em outras disciplinas se houver vagas.
O sistema deve notificar o aluno sobre a confirmação do cancelamento.

**Verificar Vagas nas Disciplinas**

Como um aluno,
Quero verificar a disponibilidade de vagas nas disciplinas,
Para que eu possa me matricular em disciplinas que ainda têm vagas abertas.

Critérios de Aceitação:

O sistema deve exibir a quantidade atual de vagas disponíveis para cada disciplina.
O sistema deve atualizar o status das disciplinas quando o número máximo de inscrições é atingido.

**Consultar Alunos matriculados**

Como um professor,
Quero consultar a lista de alunos matriculados em minhas disciplinas,
Para que eu possa me preparar para as aulas e conhecer meus alunos.

Critérios de Aceitação:

O professor deve acessar uma lista de disciplinas e ver os alunos matriculados em cada uma delas.

**Notificar inscrição do aluno**

Como secretaria,
Quero notificar o sistema de cobranças sobre as disciplinas em que os alunos estão matriculados,
Para que os alunos possam ser cobrados de acordo com as disciplinas selecionadas.

Critérios de Aceitação:

O sistema de matrículas deve enviar uma notificação ao sistema de cobranças ao final do período de matrículas.
A notificação deve incluir detalhes sobre as disciplinas em que os alunos estão matriculados e suas respectivas taxas.

**Gerenciamento de Acesso**

Como um usuário do sistema,
Quero fazer login com meu nome de usuário e senha,
Para que eu possa acessar minhas informações e funcionalidades do sistema de forma segura.

Critérios de Aceitação:

O sistema deve autenticar o usuário com base no nome de usuário e senha.
O sistema deve garantir que o acesso às informações seja restrito de acordo com o papel do usuário.

**Encerrar Inscrições para Disciplinas**

Como secretária,
Quero encerrar as inscrições para disciplinas,
Para que eu possa controlar o processo de matrícula e garantir que as inscrições sejam feitas dentro do prazo estabelecido.

Critérios de Aceitação:

A secretária deve poder encerrar inscrições para todas as disciplinas em um determinado semestre.
O sistema deve impedir que novos alunos se matriculem nas disciplinas após o encerramento das inscrições.

**Inscrever-se no Semestre**

Como aluno,
Quero me inscrever para o semestre,
Para que eu possa começar a selecionar disciplinas e planejar meu cronograma acadêmico.

Critérios de Aceitação:

O aluno deve poder se inscrever para um novo semestre antes de escolher suas disciplinas.
O sistema deve confirmar a inscrição do aluno para o semestre atual.

**Cobrar Disciplinas do Semestre**

Como representante do sistema de cobrança,
Quero cobrar os alunos pelas disciplinas em que estão matriculados,
Para que os alunos paguem as taxas de matrícula de acordo com suas seleções de disciplinas.

Critérios de Aceitação:

O sistema de cobrança deve receber notificações sobre as disciplinas em que cada aluno está matriculado.
O sistema deve gerar faturas com base nas taxas associadas a cada disciplina.
Os alunos devem ser notificados sobre suas faturas e datas de pagamento.

**Acessar o Sistema**

Como usuáriao,
Quero acessar o sistema,
Para que eu possa utilizar suas funcionalidades de acordo com minhas permissões.

Critérios de Aceitação:

O sistema deve permitir que o usuário acesse suas funcionalidades após autenticar com sucesso.
