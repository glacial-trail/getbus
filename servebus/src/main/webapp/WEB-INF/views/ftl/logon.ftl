<#include "_html_top.ftl">

<div class="wrap">
    <#include "header.ftl">
    <!--Контент-->
    <div class="content">
        <section class="container">
            <div class="login">
                <h1>Войти в личный кабинет</h1>

                <form>
                    <p><input type="text" name="login" value="" placeholder="Логин"></p>

                    <p><input type="password" name="password" value="" placeholder="Пароль"></p>

                    <p class="remember-me">
                        <label>
                            <input type="checkbox" name="remember-me" id="remember-me">
                            Запомнить меня
                        </label>
                    </p>

                    <p class="submit"><input type="submit" name="commit" value="Войти"></p>

                    <div class="login-help">
                        <a href="">Забыли пароль?</a> | <a href="index registration.html">Регистрация</a>
                    </div>
                </form>
            </div>
        </section>
    </div>
    <!--Подвал-->
    <div class="footer">
        <p class="copyright">Copyright © 2016. All Rights Reserved</p>
    </div>
</div>

<#include "_html_bottom.ftl">