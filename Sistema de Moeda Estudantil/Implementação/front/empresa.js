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

async function criarVantagem(vantagem) {
    try {
        const response = await fetch(`${API_URL}/me/vantagens`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(vantagem),
        });

        if (!response.ok) {
            const errorData = await response.json();
            console.error('Erro ao criar vantagem:', errorData);
            throw new Error(`Erro ao criar vantagem: ${errorData.message || 'Erro desconhecido'}`);
        }

        alert('Vantagem criada com sucesso!');
        fetchEmpresaLogada(localStorage.getItem('jwtToken'));
    } catch (error) {
        console.error('Erro ao criar vantagem:', error);
        alert(`Erro ao criar a vantagem. Por favor, tente novamente. Detalhes: ${error.message}`);
    }
}

async function saveEmpresa(empresa) {
    const method = empresa.id ? 'PUT' : 'POST';
    const url = empresa.id ? `${API_URL}/${empresa.id}` : API_URL;

    const vantagensCompletas = empresa.vantagens.map(v => ({
        descricao: v.descricao,
        custo: parseFloat(v.custo) || 0,
        foto: v.foto || '' // Correção para garantir que o nome do campo seja "foto"
    }));

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
                vantagens: vantagensCompletas,
            }),
        });

        if (!response.ok) {
            const errorData = await response.json();
            console.error('Erro ao salvar empresa:', errorData);
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
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao deletar a empresa');
        }

        alert('Empresa deletada com sucesso!');
        window.location.href = 'EmpresaCreate.html';
    } catch (error) {
        console.error('Erro ao deletar a empresa:', error);
        alert('Falha ao deletar a empresa. Por favor, tente novamente.');
    }
}


async function populateFormWithEmpresa(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar dados da empresa para edição');
        }

        const empresa = await response.json();

        document.getElementById('empresaId').value = empresa.id;
        document.getElementById('empresaNome').value = empresa.nome;
        document.getElementById('empresaEndereco').value = empresa.endereco;
        document.getElementById('empresaEmail').value = empresa.email;
        document.getElementById('empresaSenha').value = '';
        document.getElementById('vantagens-container').innerHTML = empresa.vantagens.map(v => `
            <div class="vantagem-item mb-3">
                <input type="text" class="form-control" value="${v.descricao}" required />
                <input type="number" class="form-control" value="${v.custo}" required />
                <input type="text" class="form-control" value="${v.foto}" required />
                <button type="button" class="btn btn-danger btn-remove-vantagem">Remover</button>
            </div>
        `).join('') + '<button type="button" class="btn btn-success btn-add-vantagem">Adicionar Vantagem</button>';
    } catch (error) {
        console.error('Erro ao carregar dados da empresa para edição:', error);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwtToken');
    fetchEmpresaLogada(token);

    document.getElementById('vantagens-container').addEventListener('click', (event) => {
        if (event.target.classList.contains('btn-add-vantagem')) {
            const vantagemItem = document.createElement('div');
            vantagemItem.classList.add('vantagem-item', 'mb-3');
            vantagemItem.innerHTML = `
                <input type="text" class="form-control" placeholder="Descrição da Vantagem" required>
                <input type="number" class="form-control" placeholder="Custo da Vantagem" required>
                <input type="text" class="form-control" placeholder="URL da Foto" required>
                <button type="button" class="btn btn-danger btn-remove-vantagem">Remover</button>
            `;
            document.getElementById('vantagens-container').insertBefore(vantagemItem, event.target);
        }
    });

    document.getElementById('vantagens-container').addEventListener('click', (event) => {
        if (event.target.classList.contains('btn-remove-vantagem')) {
            event.target.parentElement.remove();
        }
    });

    document.getElementById('formEmpresa').addEventListener('submit', (event) => {
        event.preventDefault();
        const empresa = {
            id: document.getElementById('empresaId').value,
            nome: document.getElementById('empresaNome').value,
            endereco: document.getElementById('empresaEndereco').value,
            email: document.getElementById('empresaEmail').value,
            senha: document.getElementById('empresaSenha').value,
            vantagens: Array.from(document.querySelectorAll('.vantagem-item')).map(v => ({
                descricao: v.children[0].value,
                custo: v.children[1].value,
                foto: v.children[2].value, // Corrigido para usar "foto"
            })),
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
