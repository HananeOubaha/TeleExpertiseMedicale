<%-- dans src/main/webapp/login.jsp --%>
<form method="POST" action="doLogin">

    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />

    <div>
        <label for="email">Email :</label>
        <input type="email" id="email" name="email" required>
    </div>
    <br>
    <div>
        <label for="password">Mot de passe :</label>
        <input type="password" id="password" name="password" required>
    </div>
    <br>
    <button type="submit">Se connecter</button>
</form>