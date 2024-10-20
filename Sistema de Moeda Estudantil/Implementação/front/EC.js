document.addEventListener('DOMContentLoaded', function () {
    const vantagensContainer = document.getElementById('vantagens-container');
    const adicionarVantagemBtn = document.getElementById('adicionar-vantagem');
    const formCadastroVantagens = document.getElementById('form-cadastro-vantagens');
    const salvarVantagensBtn = document.getElementById('salvar-vantagens');
    let empresaId = null;

    function criarCamposVantagem() {
        const vantagemItem = document.createElement('div');
        vantagemItem.classList.add('vantagem-item', 'mb-3');
        vantagemItem.innerHTML = `
            <input type="text" class="form-control" placeholder="Descrição da Vantagem" required>
            <input type="number" class="form-control" placeholder="Custo da Vantagem" required>
            <input type="text" class="form-control" placeholder="URL da Foto" required>
        `;
        vantagensContainer.appendChild(vantagemItem);
    }

    adicionarVantagemBtn.addEventListener('click', criarCamposVantagem);

    document.getElementById('form-cadastro-empresa').addEventListener('submit', function(event) {
        event.preventDefault();

        const empresa = {
            nome: document.getElementById('nome').value,
            endereco: document.getElementById('endereco').value,
            email: document.getElementById('email').value,
            senha: document.getElementById('senha').value
        };

        fetch('http://localhost:8080/api/empresas-parceiras', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(empresa)
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text || 'Erro ao cadastrar empresa');
                });
            }
            return response.json();
        })
        .then(data => {
            empresaId = data.id; // Pega o ID da empresa cadastrada
            formCadastroVantagens.style.display = 'block'; // Libera o formulário de vantagens
            salvarVantagensBtn.style.display = 'block'; // Exibe o botão de salvar vantagens
            alert('Empresa cadastrada com sucesso! Agora, cadastre as vantagens.');
        })
        .catch(error => {
            console.error('Erro ao cadastrar empresa:', error.message);
            alert('Erro: ' + error.message);
        });
    });

    salvarVantagensBtn.addEventListener('click', function() {
        const vantagens = Array.from(vantagensContainer.getElementsByClassName('vantagem-item')).map(item => {
            const inputs = item.getElementsByTagName('input');
            return {
                descricao: inputs[0].value,
                custo: inputs[1].value,
                foto: inputs[2].value,
                empresaParceira: { id: empresaId } // Adiciona o ID da empresa
            };
        });

        const vantagemPromises = vantagens.map(vantagem => {
            return fetch('http://localhost:8080/api/vantagens', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(vantagem)
            });
        });

        Promise.all(vantagemPromises)
        .then(() => {
            alert('Vantagens cadastradas com sucesso!');
        })
        .catch(error => {
            console.error('Erro ao cadastrar vantagens:', error.message);
            alert('Erro: ' + error.message);
        });
    });
});
