// aluno.js

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
                    <button class="btn btn-secondary btn-editar" data-id="${aluno.id}" data-bs-toggle="modal" data-bs-target="#modalAluno">Editar</button>
                    <button class="btn btn-danger btn-deletar" data-id="${aluno.id}">Deletar</button>
                </div>
            </div>
        </div>
    `;
    alunosContainer.insertAdjacentHTML('beforeend', alunoCard);
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

document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwtToken');
    fetchAlunoLogado(token);

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
