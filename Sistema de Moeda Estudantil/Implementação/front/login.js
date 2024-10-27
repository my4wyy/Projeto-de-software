document.getElementById("login-form").addEventListener("submit", async function(event) {
    event.preventDefault();

    const username = document.getElementById("user").value;
    const password = document.getElementById("password").value;

    const loginRequest = {
        username: username,
        password: password
    };

    try {
        const response = await fetch("http://localhost:8080/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(loginRequest)
        });

        if (response.ok) {
            const token = await response.text();

            const payloadBase64 = token.split('.')[1];
            const decodedPayload = JSON.parse(atob(payloadBase64));

            localStorage.setItem("jwtToken", token);

            if (decodedPayload.role === "ALUNO") {
                window.location.href = "aluno.html";
            } else if (decodedPayload.role === "EMPRESA") {
                window.location.href = "empresa.html";
            } else {
                alert("Tipo de usuário desconhecido.");
            }
        } else {
            alert("Usuário ou senha inválidos.");
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        alert("Ocorreu um erro. Tente novamente mais tarde.");
    }
});
