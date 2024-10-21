document.addEventListener('DOMContentLoaded', function () {
    fetch('http://localhost:8080/api/instituicoes', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao buscar instituições');
        }
        return response.json();
    })
    .then(instituicoes => {
        const selectInstituicao = document.getElementById('instituicao');
        selectInstituicao.innerHTML = '<option value="" disabled selected>Selecione a Instituição</option>';
        instituicoes.forEach(instituicao => {
            const option = document.createElement('option');
            option.value = instituicao.id; 
            option.textContent = instituicao.nome;
            selectInstituicao.appendChild(option);
        });
    })
    .catch(error => {
        console.error('Erro ao carregar instituições:', error.message);
        alert('Erro ao carregar as instituições.');
    });
});

document.getElementById('form-cadastro-aluno').addEventListener('submit', function(event) {
    event.preventDefault();

    const aluno = {
        nome: document.getElementById('nome').value,
        endereco: document.getElementById('endereco').value,
        email: document.getElementById('email').value,
        senha: document.getElementById('senha').value,
        cpf: document.getElementById('cpf').value,
        rg: document.getElementById('rg').value,
        curso: document.getElementById('curso').value,
        instituicao: { 
            id: document.getElementById('instituicao').value 
        }
    };

    console.log(JSON.stringify(aluno)); 

    fetch('http://localhost:8080/api/alunos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(aluno) 
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { 
                throw new Error(text || 'Erro ao cadastrar aluno');
            });
        }
        return response.json();
    })
    .then(data => {
        alert('Aluno cadastrado com sucesso!');
    })
    .catch(error => {
        console.error('Erro ao cadastrar aluno:', error.message);
        alert('Erro: ' + error.message);
    });
});
