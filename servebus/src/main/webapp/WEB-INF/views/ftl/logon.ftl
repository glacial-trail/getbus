<#include "_html_top.ftl">

<div class="wrap">
    <#include "header.ftl">
    <div class="content">
        <section class="container">
            <div class="login">
                <h1><@spring.message "logon.login_to_acc"/></h1>

                <form action="/security_check" method="POST">
                    <#--<input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>-->
                    <p><input type="text" name="logonid" value="" placeholder="<@spring.message "login"/>" autofocus></p>

                    <p><input type="password" name="password" value="" placeholder="<@spring.message "password"/>"></p>

                    <p class="remember-me">
                        <label>
                            <input type="checkbox" name="remember-me" id="remember-me">
                            <@spring.message "remember_me"/>
                        </label>
                    </p>

                    <p class="submit"><input type="submit" value="<@spring.message "enter"/>"></p>

                    <div class="login-help">
                        <a href="#"><@spring.message "logon.forgot_pass"/></a> | <a href="#"><@spring.message "logon.register"/></a>
                    </div>
                </form>
            </div>
        </section>
    </div>
    <#include "footer.ftl">
</div>

<#include "_html_bottom.ftl">