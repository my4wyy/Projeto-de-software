const API_URL = 'http://localhost:8080/api/empresas-parceiras';

async function fetchEmpresaLogada(token) {
    try {
        const response = await fetch(`${API_URL}/me`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar dados da empresa');
        }

        const empresa = await response.json();
        renderEmpresa(empresa);
    } catch (error) {
        console.error('Erro ao buscar empresa logada:', error);
        alert('Falha ao buscar dados da empresa. Tente novamente.');
    }
}

async function renderEmpresa(empresa) {
    const empresasContainer = document.getElementById('empresas');
    empresasContainer.innerHTML = '';

    const vantagens = await fetchVantagens();

    const empresaCard = `
        <div class="col-md-4 mb-4">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">${empresa.nome}</h5>
                    <p class="card-text">Endereço: ${empresa.endereco}</p>
                    <p class="card-text">Email: ${empresa.email}</p>
                    <p class="card-text">Senha: ${empresa.senha}</p>
                    <p class="card-text">Vantagens: ${vantagens.map(v => v.descricao).join(', ')}</p>
                    <button class="btn btn-secondary btn-editar" data-id="${empresa.id}" data-bs-toggle="modal" data-bs-target="#modalEmpresa">Editar</button>
                    <button class="btn btn-danger btn-deletar" data-id="${empresa.id}">Deletar</button>
                </div>
            </div>
        </div>
    `;
    empresasContainer.insertAdjacentHTML('beforeend', empresaCard);
}

async function fetchVantagens() {
    try {
        const response = await fetch(`${API_URL}/me/vantagens`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar vantagens');
        }

        return await response.json();
    } catch (error) {
        console.error('Erro ao buscar vantagens:', error);
        return [];
    }
}

async function saveEmpresa(empresa) {
    const method = empresa.id ? 'PUT' : 'POST';
    const url = empresa.id ? `${API_URL}/${empresa.id}` : API_URL;

    const vantagensUnicas = Array.from(new Set(empresa.vantagens.map(v => v.descricao)))
        .map(descricao => ({ descricao }));

    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: empresa.id,
                nome: empresa.nome,
                endereco: empresa.endereco,
                email: empresa.email,
                senha: empresa.senha,
                vantagens: vantagensUnicas
            }),
        });

        if (!response.ok) {
            const errorData = await response.json();
            console.error('Erro do backend:', errorData);
            throw new Error(`Erro ao salvar empresa: ${errorData.message || 'Erro desconhecido'}`);
        }

        alert('Empresa salva com sucesso!');
        fetchEmpresaLogada(localStorage.getItem('jwtToken'));
    } catch (error) {
        console.error('Erro ao salvar empresa:', error);
        alert(`Erro ao salvar a empresa. Por favor, tente novamente. Detalhes: ${error.message}`);
    }
}

async function deleteEmpresa(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao deletar empresa');
        }

        alert('Empresa deletada com sucesso!');
        window.location.href = 'EmpresaCreate.html';
        fetchEmpresaLogada(localStorage.getItem('jwtToken'));
    } catch (error) {
        console.error('Erro ao deletar a empresa:', error);
        alert('Erro ao deletar a empresa. Por favor, tente novamente.');
    }
}

async function populateFormWithEmpresa(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar dados da empresa');
        }

        const empresa = await response.json();

        document.getElementById('empresaId').value = empresa.id;
        document.getElementById('empresaNome').value = empresa.nome;
        document.getElementById('empresaEndereco').value = empresa.endereco;
        document.getElementById('empresaEmail').value = empresa.email;
        document.getElementById('empresaSenha').value = empresa.senha;
        document.getElementById('empresaVantagens').value = empresa.vantagens.map(v => v.descricao).join(', ');
    } catch (error) {
        console.error('Erro ao buscar empresa:', error);
        alert('Erro ao carregar os dados da empresa. Tente novamente.');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwtToken');
    fetchEmpresaLogada(token);

    document.getElementById('formEmpresa').addEventListener('submit', (event) => {
        event.preventDefault();
        const empresa = {
            id: document.getElementById('empresaId').value,
            nome: document.getElementById('empresaNome').value,
            endereco: document.getElementById('empresaEndereco').value,
            email: document.getElementById('empresaEmail').value,
            senha: document.getElementById('empresaSenha').value,
            vantagens: document.getElementById('empresaVantagens').value.split(',').map(v => ({ descricao: v.trim() })),
        };
        saveEmpresa(empresa);
        event.target.reset();

        const modal = bootstrap.Modal.getInstance(document.getElementById('modalEmpresa'));
        modal.hide();
    });

    document.getElementById('empresas').addEventListener('click', (event) => {
        if (event.target.classList.contains('btn-editar')) {
            const id = event.target.dataset.id;
            populateFormWithEmpresa(id).then(() => {
                const modal = new bootstrap.Modal(document.getElementById('modalEmpresa'));
                modal.show();
            });
        } else if (event.target.classList.contains('btn-deletar')) {
            const id = event.target.dataset.id;
            deleteEmpresa(id);
        }
    });
});
