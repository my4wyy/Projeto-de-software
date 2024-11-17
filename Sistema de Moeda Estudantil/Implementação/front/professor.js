const API_URL = 'http://localhost:8080/api/professores';
const API_ALUNOS_URL = 'http://localhost:8080/api/alunos';

// Obtém o token JWT armazenado localmente
function getToken() {
    return localStorage.getItem('jwtToken');
}

// Fetch para buscar o professor logado
async function fetchProfessorLogado() {
    const token = getToken();
    if (!token) {
        alert('Token de autenticação não encontrado. Faça login novamente.');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/me`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar dados do professor');
        }

        const professor = await response.json();
        renderProfessor(professor);
    } catch (error) {
        console.error('Erro:', error);
        alert('Falha ao buscar dados do professor. Tente novamente.');
    }
}

// Renderiza as informações do professor logado na interface
function renderProfessor(professor) {
    const professoresContainer = document.getElementById('professores');
    professoresContainer.innerHTML = `
        <div class="col-md-4 mb-4">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">${professor.nome}</h5>
                    <p class="card-text">Email: ${professor.email}</p>
                    <p class="card-text">Departamento: ${professor.departamento}</p>
                </div>
            </div>
        </div>
    `;
}

// Mostra o modal para enviar moedas
function abrirModalEnviarMoedas(alunoId) {
    window.alunoIdParaEnviar = alunoId; // Define o alunoId globalmente para envio
    const modalEnviarMoedas = new bootstrap.Modal(document.getElementById('modalEnviarMoedas'));
    modalEnviarMoedas.show();
}

document.getElementById('formEnviarMoedas').addEventListener('submit', async function (event) {
    event.preventDefault();

    const motivo = document.getElementById('motivo').value;
    const valor = parseFloat(document.getElementById('valor').value); // Use parseFloat para valores numéricos com decimais
    const token = getToken();

    if (!token) {
        alert('Token de autenticação não encontrado. Faça login novamente.');
        return;
    }

    // Verifica se o valor é válido
    if (isNaN(valor) || valor <= 0) { // Adicione a verificação de <= 0
        alert('Por favor, insira um valor maior que zero para enviar moedas.');
        return;
    }

    try {
        // Chama a API para enviar moedas
        const response = await fetch(`http://localhost:8080/api/transacoes/enviar-moedas?quantidade=${valor}&motivo=${encodeURIComponent(motivo)}&alunoId=${window.alunoIdParaEnviar}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });

        if (!response.ok) {
            // Lança um erro se a transação falhar
            const errorMessage = await response.text(); // Obtém mensagem de erro do backend
            throw new Error(errorMessage || 'Erro ao enviar moedas');
        }

        alert('Moedas enviadas com sucesso!');
        document.getElementById('formEnviarMoedas').reset();

        const modalEnviarMoedas = bootstrap.Modal.getInstance(document.getElementById('modalEnviarMoedas'));
        modalEnviarMoedas.hide();
    } catch (error) {
        console.error('Erro ao enviar moedas:', error);
        alert(`Falha ao enviar moedas: ${error.message}`);
    }
});


function atualizarSaldos(contaProfessor, contaAluno, quantidade) {
    if (contaProfessor.saldo < quantidade) {
        console.error("Saldo insuficiente para realizar a transação.");
        return false;
    }

    // Atualiza os saldos
    contaProfessor.saldo -= quantidade;
    contaAluno.saldo += quantidade;

    console.log(`Saldo atualizado com sucesso!`);
    console.log(`Novo saldo do professor: ${contaProfessor.saldo}`);
    console.log(`Novo saldo do aluno: ${contaAluno.saldo}`);
    return true;
}


// Lista os alunos e adiciona o botão para enviar moedas
async function listarAlunos() {
    const token = getToken();
    if (!token) {
        alert('Token de autenticação não encontrado. Faça login novamente.');
        return;
    }

    try {
        const response = await fetch(API_ALUNOS_URL, {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });

        if (!response.ok) throw new Error('Erro ao buscar alunos');

        const alunos = await response.json();
        const listaAlunos = document.getElementById('listaAlunos');
        listaAlunos.innerHTML = '';

        alunos.forEach((aluno) => {
            const item = `
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    ${aluno.nome}
                    <button class="btn btn-secondary btn-sm" onclick="abrirModalEnviarMoedas(${aluno.id})">Enviar moedas</button>
                </li>
            `;
            listaAlunos.insertAdjacentHTML('beforeend', item);
        });
    } catch (error) {
        console.error('Erro ao buscar alunos:', error);
        alert('Erro ao carregar alunos. Tente novamente.');
    }
}

// Obtém o ID do professor logado
async function getProfessorLogadoId() {
    const token = getToken();
    if (!token) {
        alert('Token de autenticação não encontrado. Faça login novamente.');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/me`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar dados do professor');
        }

        const professor = await response.json();
        return professor.id;
    } catch (error) {
        console.error('Erro ao buscar ID do professor:', error);
        alert('Erro ao buscar ID do professor.');
    }
}

// Inicializa a página ao carregar
document.addEventListener('DOMContentLoaded', () => {
    fetchProfessorLogado();

    const modalAlunos = document.getElementById('modalAlunos');
    modalAlunos.addEventListener('show.bs.modal', listarAlunos);

    
});
// Consulta o saldo do professor e renderiza no final do modal de extrato
async function consultarSaldoNoExtrato() {
    const token = getToken();
    if (!token) {
        alert('Token de autenticação não encontrado. Faça login novamente.');
        return;
    }

    try {
        const professorId = await getProfessorLogadoId();
        const url = `http://localhost:8080/api/professores/${professorId}/conta/saldo`;

        const response = await fetch(url, {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });

        if (!response.ok) throw new Error('Erro ao consultar saldo');

        const saldo = await response.json();
        return saldo;  // Retorna o saldo, para ser usado na renderização das transações
    } catch (error) {
        console.error('Erro ao consultar saldo:', error);
        alert('Erro ao consultar saldo. Tente novamente.');
    }
}

// Modifica a renderização das transações para incluir o saldo no final
async function renderTransacoes(transacoes) {
    const listaExtrato = document.getElementById('listaExtrato');
    listaExtrato.innerHTML = ''; // Limpa a lista

    if (transacoes.length === 0) {
        listaExtrato.innerHTML = '<li class="list-group-item">Nenhuma transação encontrada</li>';
        return;
    }

    // Renderiza as transações
    transacoes.forEach((transacao) => {
        const item = `
            <li class="list-group-item">
                <strong>Aluno:</strong> ${transacao.alunoNome} <br>
                <strong>Motivo:</strong> ${transacao.descricao} <br>
                <strong>Quantidade:</strong> ${transacao.quantidade} moedas
            </li>
        `;
        listaExtrato.insertAdjacentHTML('beforeend', item);
    });

    // Obtém o saldo total e adiciona ao final da lista de transações
    const saldo = await consultarSaldoNoExtrato();
    const saldoItem = `
        <li class="list-group-item">
            <strong>Saldo Total:</strong> ${saldo} Moedas
        </li>
    `;
    listaExtrato.insertAdjacentHTML('beforeend', saldoItem);
}

// Função para listar transações
async function listarTransacoes() {
    const token = getToken();
    if (!token) {
        alert('Token de autenticação não encontrado. Faça login novamente.');
        return;
    }

    try {
        // Obtem o ID do professor logado
        const professorId = await getProfessorLogadoId();

        // Busca todas as transações
        const response = await fetch('http://localhost:8080/api/transacoes/listar', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar transações');
        }

        const transacoes = await response.json();

        // Filtra as transações pelo ID do professor logado (comparando com o ID de origem)
        const transacoesProfessor = transacoes.filter(transacao => transacao.origem && transacao.origem.id === professorId);

        renderTransacoes(transacoesProfessor);
    } catch (error) {
        console.error('Erro ao listar transações:', error);
        alert('Erro ao carregar transações. Tente novamente.');
    }
}

// Inicializa a página ao carregar
document.addEventListener('DOMContentLoaded', () => {
    fetchProfessorLogado();

    const modalExtrato = document.getElementById('modalExtrato');
    modalExtrato.addEventListener('show.bs.modal', listarTransacoes);
});
