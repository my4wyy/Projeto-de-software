const API_URL = 'http://localhost:8080/api/alunos';



async function fetchAlunoLogado(token) {
    try {
        const response = await fetch(`${API_URL}/me`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar dados do aluno');
        }

        const aluno = await response.json();
        renderAluno(aluno);
    } catch (error) {
        console.error('Erro:', error);
        alert('Falha ao buscar dados do aluno. Tente novamente.');
    }
}

function renderAluno(aluno) {
    const alunosContainer = document.getElementById('alunos');
    alunosContainer.innerHTML = '';

    const alunoCard = `
        <div class="col-md-4 mb-4">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">${aluno.nome}</h5>
                    <p class="card-text">Endereço: ${aluno.endereco}</p>
                    <p class="card-text">Email: ${aluno.email}</p>
                    <p class="card-text">CPF: ${aluno.cpf}</p>
                    <p class="card-text">RG: ${aluno.rg}</p>
                    <p class="card-text">Curso: ${aluno.curso}</p>
                    <button class="btn btn-primary btn-editar" data-id="${aluno.id}" data-bs-toggle="modal" data-bs-target="#modalAluno"><i class="fas fa-edit"></i></button>
                    <button class="btn btn-primary btn-deletar" data-id="${aluno.id}"><i class="fas fa-trash-alt"></i></button>
                </div>
            </div>
        </div>
    `;
    alunosContainer.insertAdjacentHTML('beforeend', alunoCard);
}

function getToken() {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        alert('Token não encontrado. Faça login novamente.');
    }
    return token;
}


async function saveAluno(aluno) {
    const method = aluno.id ? 'PUT' : 'POST';
    const url = aluno.id ? `${API_URL}/${aluno.id}` : API_URL;

    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(aluno),
        });

        if (!response.ok) {
            throw new Error('Erro ao salvar aluno');
        }

        alert('Aluno salvo com sucesso!');
        fetchAlunoLogado(localStorage.getItem('jwtToken'));
    } catch (error) {
        console.error(error);
        alert('Erro ao salvar o aluno. Por favor, tente novamente.');
    }
}

async function deleteAluno(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao deletar aluno');
        }

        alert('Aluno deletado com sucesso!');
        window.location.href = 'AlunoCreate.html';
    } catch (error) {
        console.error(error);
        alert('Erro ao deletar o aluno. Por favor, tente novamente.');
    }
}

async function populateFormWithAluno(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
            },
        });

        const aluno = await response.json();
        document.getElementById('alunoId').value = aluno.id;
        document.getElementById('alunoNome').value = aluno.nome;
        document.getElementById('alunoEndereco').value = aluno.endereco;
        document.getElementById('alunoEmail').value = aluno.email;
        document.getElementById('alunoSenha').value = aluno.senha;
        document.getElementById('alunoCpf').value = aluno.cpf;
        document.getElementById('alunoRg').value = aluno.rg;
        document.getElementById('alunoCurso').value = aluno.curso;
    } catch (error) {
        console.error('Erro ao buscar aluno:', error);
        alert('Erro ao carregar os dados do aluno. Tente novamente.');
    }
}

async function listarVantagens() {
    try {
        const response = await fetch('http://localhost:8080/api/vantagens', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
            },
        });

        if (!response.ok) throw new Error('Erro ao buscar vantagens');

        const vantagens = await response.json();
        const listaVantagens = document.getElementById('listaVantagens');
        listaVantagens.innerHTML = '';

        vantagens.forEach((vantagem) => {
            const item = `
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    <div class="d-flex align-items-center">
                        <img src="${vantagem.foto}" alt="Imagem da Vantagem" 
                             class="img-thumbnail me-3" style="width: 60px; height: 60px; object-fit: cover; border-radius: 8px;">
                        <div>
                            <p class="mb-1" style="font-size: 1rem; font-weight: bold;">${vantagem.descricao}</p>
                            <p class="mb-0 text-muted" style="font-size: 0.9rem;">Custo: <strong>${vantagem.custo}</strong> moedas</p>
                        </div>
                    </div>
                    <button class="btn btn-primary btn-sm btn-resgatar" data-id="${vantagem.id}">Resgatar</button>
                </li>
            `;
            listaVantagens.insertAdjacentHTML('beforeend', item);
        });

        document.querySelectorAll('.btn-resgatar').forEach(button => {
            button.addEventListener('click', async (event) => {
                const vantagemId = event.target.getAttribute('data-id');
                await resgatarVantagem(vantagemId);
            });
        });
    } catch (error) {
        console.error('Erro ao buscar vantagens:', error);
        alert('Erro ao carregar vantagens. Tente novamente.');
    }
}


async function getAlunoLogadoId() {
    const token = localStorage.getItem('jwtToken');
    const response = await fetch(`${API_URL}/me`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
        throw new Error('Erro ao buscar dados do aluno');
    }

    const aluno = await response.json();
    return aluno.id;
}

async function resgatarVantagem(vantagemId) {
    const alunoId = await getAlunoLogadoId();  
    const token = getToken();
    if (!token) {
        alert('Token não encontrado. Faça login novamente.');
        return;
    }

    try {
        const response = await fetch('http://localhost:8080/api/transacoes/resgatar-vantagem', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ alunoId, vantagemId }),
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText);
        }

        const message = await response.text();
        alert(message);
    } catch (error) {
        console.error('Erro ao resgatar vantagem:', error);
        alert(`Erro: ${error.message}`);
    }
}


document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwtToken');
    fetchAlunoLogado(token);

    const modalExtrato = document.getElementById('modalExtrato');
    modalExtrato.addEventListener('show.bs.modal', listarTransacoes);

    const modalVantagens = document.getElementById('modalVantagens');
    modalVantagens.addEventListener('show.bs.modal', listarVantagens);

    document.getElementById('formAluno').addEventListener('submit', (event) => {
        event.preventDefault();
        const aluno = {
            id: document.getElementById('alunoId').value,
            nome: document.getElementById('alunoNome').value,
            endereco: document.getElementById('alunoEndereco').value,
            email: document.getElementById('alunoEmail').value,
            senha: document.getElementById('alunoSenha').value,
            cpf: document.getElementById('alunoCpf').value,
            rg: document.getElementById('alunoRg').value,
            curso: document.getElementById('alunoCurso').value,
        };
        saveAluno(aluno);
        event.target.reset();

        const modal = bootstrap.Modal.getInstance(document.getElementById('modalAluno'));
        modal.hide();
    });

    document.getElementById('alunos').addEventListener('click', (event) => {
        if (event.target.classList.contains('btn-editar')) {
            const alunoId = event.target.getAttribute('data-id');
            populateFormWithAluno(alunoId);
        } else if (event.target.classList.contains('btn-deletar')) {
            const alunoId = event.target.getAttribute('data-id');
            deleteAluno(alunoId);
        }
    });
});
async function consultarSaldoNoExtrato() {
    const token = getToken();
    if (!token) {
        alert('Token de autenticação não encontrado. Faça login novamente.');
        return;
    }

    try {
        const alunoId = await getAlunoLogadoId();
        const url = `http://localhost:8080/api/alunos/${alunoId}/conta/saldo`;

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

async function buscarProfessorPorId(id) {
    console.log('ID recebido em buscarProfessorPorId:', id); 

    if (!id || typeof id !== 'string' && typeof id !== 'number') {
        console.error('ID inválido:', id);
        return null;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/professores/${id}`);
        if (!response.ok) {
            throw new Error('Erro ao buscar professor');
        }
        return await response.json();
    } catch (error) {
        console.error('Erro ao buscar professor:', error);
        return null;
    }
}


async function renderTransacoes(transacoes) {
    const listaExtrato = document.getElementById('listaExtrato');
    listaExtrato.innerHTML = '';

    if (transacoes.length === 0) {
        listaExtrato.innerHTML = '<li class="list-group-item">Nenhuma transação encontrada</li>';
        return;
    }

    for (const transacao of transacoes) {
        const origem = transacao.origem?.id;
        console.log('Origem da transação:', origem);
        const professor = origem ? await buscarProfessorPorId(origem) : null;
        const professorNome = professor ? professor.nome : 'Professor não encontrado';

        const dataTransacao = new Date(transacao.data);
        const dataFormatada = dataTransacao.toLocaleDateString('pt-BR');
        const horarioFormatado = dataTransacao.toLocaleTimeString('pt-BR', {
            hour: '2-digit',
            minute: '2-digit'
        });

        const item = `
            <li class="list-group-item d-flex justify-content-between align-items-center">
                <div>
                    <strong>${dataFormatada} às ${horarioFormatado}</strong> <br>
                    <span>Professor: ${professorNome}</span> <br>
                    <span>Motivo: ${transacao.descricao}</span>
                </div>
                <span class="text-end text-success fw-bold" style="font-size: 1.2rem;">+${transacao.quantidade} moedas</span>
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

async function renderCompras(compras) {
    const listaVantagensResgatadas = document.getElementById('listaVantagensResgatadas');
    listaVantagensResgatadas.innerHTML = '';

    if (compras.length === 0) {
        listaVantagensResgatadas.innerHTML = '<li class="list-group-item">Nenhuma compra encontrada</li>';
        return;
    }

    for (const compra of compras) {
        const dataCompra = new Date(compra.data);
        const dataFormatada = dataCompra.toLocaleDateString('pt-BR');
        const horarioFormatado = dataCompra.toLocaleTimeString('pt-BR', {
            hour: '2-digit',
            minute: '2-digit',
        });

        const item = `
            <li class="list-group-item d-flex justify-content-between align-items-center">
                <div>
                    <strong>${dataFormatada} às ${horarioFormatado}</strong><br>
                    <span>${compra.descricao}</span>
                </div>
                <span class="fw-bold text-danger" style="font-size: 1.2rem;">-${compra.quantidade} moedas</span>
            </li>
        `;
        listaVantagensResgatadas.insertAdjacentHTML('beforeend', item);
    }
}


async function listarTransacoes() {
    const token = getToken();
    if (!token) {
        alert('Token de autenticação não encontrado. Faça login novamente.');
        return;
    }

    try {
        const alunoID = await getAlunoLogadoId();

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
        console.log('Transações recebidas:', transacoes);

        const transacoesAluno = transacoes.filter(transacao => transacao.destino && transacao.destino.id === alunoID);
        const comprasAluno = transacoes.filter(transacao => transacao.origem && transacao.origem.id === alunoID);

        renderTransacoes(transacoesAluno);
        renderCompras(comprasAluno);
    } catch (error) {
        console.error('Erro ao listar transações:', error);
        alert('Erro ao carregar transações. Tente novamente.');
    }
}

document.getElementById('logoutButton').addEventListener('click', () => {
    localStorage.removeItem('jwtToken'); 
    window.location.href = 'index.html'; 
});



