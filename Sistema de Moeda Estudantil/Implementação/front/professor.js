const API_URL = 'http://localhost:8080/api/professores';
const API_ALUNOS_URL = 'http://localhost:8080/api/alunos';

async function fetchProfessorLogado(token) {
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

function renderProfessor(professor) {
    const professoresContainer = document.getElementById('professores');
    professoresContainer.innerHTML = '';

    const professorCard = `
        <div class="col-md-4 mb-4">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">${professor.nome}</h5>
                    <p class="card-text">Email: ${professor.email}</p>
                    <button class="btn btn-secondary btn-editar" data-id="${professor.id}" data-bs-toggle="modal" data-bs-target="#modalProfessor">Editar</button>
                    <button class="btn btn-danger btn-deletar" data-id="${professor.id}">Deletar</button>
                </div>
            </div>
        </div>
    `;
    professoresContainer.insertAdjacentHTML('beforeend', professorCard);
}

async function listarAlunos() {
    try {
        const response = await fetch(API_ALUNOS_URL, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
            },
        });

        if (!response.ok) throw new Error('Erro ao buscar alunos');

        const alunos = await response.json();
        const listaAlunos = document.getElementById('listaAlunos');
        listaAlunos.innerHTML = '';

        alunos.forEach((aluno) => {
            const item = `
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    ${aluno.nome} - ${aluno.email}
                    <button class="btn btn-secondary btn-sm">Detalhes</button>
                </li>
            `;
            listaAlunos.insertAdjacentHTML('beforeend', item);
        });
    } catch (error) {
        console.error('Erro ao buscar alunos:', error);
        alert('Erro ao carregar alunos. Tente novamente.');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwtToken');
    fetchProfessorLogado(token);

    const modalAlunos = document.getElementById('modalAlunos');
    modalAlunos.addEventListener('show.bs.modal', listarAlunos);

    document.getElementById('formProfessor').addEventListener('submit', (event) => {
        event.preventDefault();
        const professor = {
            id: document.getElementById('professorId').value,
            nome: document.getElementById('professorNome').value,
            email: document.getElementById('professorEmail').value,
        };
        saveProfessor(professor);
        event.target.reset();

        const modal = bootstrap.Modal.getInstance(document.getElementById('modalProfessor'));
        modal.hide();
    });

    document.getElementById('professores').addEventListener('click', (event) => {
        if (event.target.classList.contains('btn-editar')) {
            const professorId = event.target.getAttribute('data-id');
            populateFormWithProfessor(professorId);
        } else if (event.target.classList.contains('btn-deletar')) {
            const professorId = event.target.getAttribute('data-id');
            deleteProfessor(professorId);
        }
    });
});
