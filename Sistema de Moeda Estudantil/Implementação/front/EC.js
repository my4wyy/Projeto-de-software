document.addEventListener('DOMContentLoaded', function () {
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
        .then(() => {
            alert('Empresa cadastrada com sucesso!');
        })
        .catch(error => {
            console.error('Erro ao cadastrar empresa:', error.message);
            alert('Erro: ' + error.message);
        });
    });
});
