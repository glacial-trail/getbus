<#assign pageTitle><@spring.message "register.partner.page_title"/></#assign>

<@gbc.common_page pageTitle >
    <div class="background-img background-img-transporter-reg"></div>
    <@gbc.nav_bar/>
    <div class="register-container container">
        <div class="row">
            <div class="register">
                <form action="" method="post">
                    <h2>Регистрация</h2>
                    <label for="firstname">Имя</label>
                    <input type="text" id="firstname" name="firstname" placeholder="Введите Ваше имя...">
                    <label for="lastname">Фамилия</label>
                    <input type="text" id="lastname" name="lastname" placeholder="Введите Вашу фамилию...">
                    <label for="e-mail">E-mail</label>
                    <input type="text" id="e-mail" name="e-mail" placeholder="Введите Ваш e-mail...">
                    <label for="password">Пароль</label>
                    <input type="password" id="password" name="password" placeholder="Введите пароль...">
                    <button type="submit">ЗАРЕГИСТРИРОВАТЬСЯ</button>
                </form>
            </div>
        </div>
    </div>
</@gbc.common_page>