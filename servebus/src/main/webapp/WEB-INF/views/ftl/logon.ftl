<#include "_html_top.ftl">

<div class="wrap">
    <#include "header.ftl">
    <div class="content">
        <section class="container">
            <div class="login">
                <h1><@spring.message "logon.login_to_acc"/></h1>

                <form>
                    <p><input type="text" name="login" value="" placeholder="<@spring.message "login"/>"></p>

                    <p><input type="password" name="password" value="" placeholder="<@spring.message "password"/>"></p>

                    <p class="remember-me">
                        <label>
                            <input type="checkbox" name="remember-me" id="remember-me">
                            <@spring.message "remember_me"/>
                        </label>
                    </p>

                    <p class="submit"><input type="submit" name="commit" value="<@spring.message "enter"/>"></p>

                    <div class="login-help">
                        <a href=""><@spring.message "logon.forgot_pass"/></a> | <a href="index registration.html"><@spring.message "logon.register"/></a>
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