<#assign pageTitle><@spring.message "logon.page_title"/></#assign>

<@gbc.common_page pageTitle >
    <div class="background-img background-img-logon"></div>
    <#include "nav_logon.ftl" />
    <div class="login-container">
        <div class="row">
            <div class="login">
                <h1><@spring.message "logon.login_to_acc"/></h1>
                <#if RequestParameters.fail??>
                    <span class="error-message"><@spring.message "logon.wrongPassOrLogin"/></span>
                </#if>
                <#if RequestParameters.invalid_session??>
                    <span class="error-message"><@spring.message "logon.invalidSession"/></span>
                </#if>
                <form action="/security_check" method="POST">
                    <#--<input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>-->
                    <p><input class="input-log" type="text" name="logonid" value="" placeholder="<@spring.message "login"/>"autofocus></p>
                    <p><input class="input-log" type="password" name="password" value="" placeholder="<@spring.message "password"/>"></p>
                    <p class="remember-me">
                        <label>
                            <input class="input-log" type="checkbox" name="remember-me" id="remember-me">
                            <@spring.message "remember_me"/>
                        </label>
                    </p>
                    <p class="submit"><input class="input-log" type="submit" value="<@spring.message "enter"/>"></p>
                    <div class="login-help">
                        <a href="#"><@spring.message "logon.forgot_pass"/></a> | <a href="#"><@spring.message "logon.register"/></a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</@gbc.common_page>