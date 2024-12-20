const API_URL = 'http://localhost:8080/api/professores';
const API_ALUNOS_URL = 'http://localhost:8080/api/alunos';

function getToken() {
    return localStorage.getItem('jwtToken');
}

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

        if (!response.ok) throw new Error('Erro ao buscar dados do professor');
        const professor = await response.json();
        renderProfessor(professor);
    } catch (error) {
        console.error('Erro:', error);
        alert('Falha ao buscar dados do professor. Tente novamente.');
    }
}

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

function abrirModalEnviarMoedas(alunoId) {
    window.alunoIdParaEnviar = alunoId;
    const modalEnviarMoedas = new bootstrap.Modal(document.getElementById('modalEnviarMoedas'));
    modalEnviarMoedas.show();
}

document.getElementById('formEnviarMoedas').addEventListener('submit', async function (event) {
    event.preventDefault();

    const motivo = document.getElementById('motivo').value;
    const valor = parseFloat(document.getElementById('valor').value);
    const token = getToken();

    if (!token) {
        alert('Token de autenticação não encontrado. Faça login novamente.');
        return;
    }

    if (isNaN(valor) || valor <= 0) {
        alert('Por favor, insira um valor maior que zero para enviar moedas.');
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/transacoes/enviar-moedas?quantidade=${valor}&motivo=${encodeURIComponent(motivo)}&alunoId=${window.alunoIdParaEnviar}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });

        if (!response.ok) {
            const errorMessage = await response.text();
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
                    <button class="btn btn-primary btn-sm" onclick="abrirModalEnviarMoedas(${aluno.id})">Enviar moedas</button>
                </li>
            `;
            listaAlunos.insertAdjacentHTML('beforeend', item);
        });
    } catch (error) {
        console.error('Erro ao buscar alunos:', error);
        alert('Erro ao carregar alunos. Tente novamente.');
    }
}

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

        if (!response.ok) throw new Error('Erro ao buscar dados do professor');
        const professor = await response.json();
        return professor.id;
    } catch (error) {
        console.error('Erro ao buscar ID do professor:', error);
        alert('Erro ao buscar ID do professor.');
    }
}

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
        return saldo;
    } catch (error) {
        console.error('Erro ao consultar saldo:', error);
        alert('Erro ao consultar saldo. Tente novamente.');
    }
}

async function buscarAlunoPorId(alunoId) {
    const token = getToken();
    if (!token) {
        alert('Token de autenticação não encontrado. Faça login novamente.');
        return null;
    }

    try {
        const response = await fetch(`${API_ALUNOS_URL}/${alunoId}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) return null;
        return await response.json();
    } catch (error) {
        console.error('Erro ao buscar aluno:', error);
        return null;
    }
}

async function renderTransacoes(transacoes) {
    const listaExtrato = document.getElementById('listaExtrato');
    listaExtrato.innerHTML = '';

    if (transacoes.length === 0) {
        listaExtrato.innerHTML = '<li class="list-group-item text-center">Nenhuma transação encontrada</li>';
        return;
    }

    for (const transacao of transacoes) {
        const destino = transacao.destino;
        const aluno = destino ? await buscarAlunoPorId(destino.id) : null;
        const alunoNome = aluno ? aluno.nome : 'Aluno não encontrado';

        const dataTransacao = new Date(transacao.data);
        const dataFormatada = dataTransacao.toLocaleDateString('pt-BR');
        const horarioFormatado = dataTransacao.toLocaleTimeString('pt-BR', {
            hour: '2-digit',
            minute: '2-digit',
        });

        const item = `
            <li class="list-group-item d-flex justify-content-between align-items-center">
                <div>
                    <strong>${dataFormatada} às ${horarioFormatado}</strong><br>
                    <span>Aluno: ${alunoNome}</span><br>
                    <span>Motivo: ${transacao.descricao}</span>
                </div>
                <span class="fw-bold text-danger" style="font-size: 1.2rem;">-${transacao.quantidade} moedas</span>
            </li>
        `;
        listaExtrato.insertAdjacentHTML('beforeend', item);
    }

    const saldo = await consultarSaldoNoExtrato();
    const saldoItem = `
        <li class="list-group-item text-center fw-bold bg-light mt-3">
            <span style="font-size: 1.5rem;">Saldo Total:</span>
            <span style="font-size: 1.8rem;" class="text-success">${saldo} Moedas</span>
        </li>
    `;
    listaExtrato.insertAdjacentHTML('beforeend', saldoItem);
}


async function listarTransacoes() {
    const token = getToken();
    if (!token) {
        alert('Token de autenticação não encontrado. Faça login novamente.');
        return;
    }

    try {
        const professorId = await getProfessorLogadoId(); 
        const response = await fetch('http://localhost:8080/api/transacoes/listar', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) throw new Error('Erro ao buscar transações');
        const transacoes = await response.json();

        const transacoesProfessor = transacoes.filter(transacao => transacao.origem && transacao.origem.id === professorId);
        renderTransacoes(transacoesProfessor);
    } catch (error) {
        console.error('Erro ao listar transações:', error);
        alert('Erro ao carregar transações. Tente novamente.');
    }
}


document.addEventListener('DOMContentLoaded', () => {
    fetchProfessorLogado();

    const modalAlunos = document.getElementById('modalAlunos');
    modalAlunos.addEventListener('show.bs.modal', listarAlunos);

    const modalExtrato = document.getElementById('modalExtrato');
    modalExtrato.addEventListener('show.bs.modal', listarTransacoes);
});
document.getElementById('logoutButton').addEventListener('click', () => {
    localStorage.removeItem('jwtToken'); 
    window.location.href = 'index.html'; 
});