<#assign pageTitle><@spring.message "logon.page_title"/></#assign>

<@gbc.common_page pageTitle >
    <div class="background-img background-img-logon"></div>
    <#include "nav_logon.ftl" />
    <div class="login-container">
        <div class="row">
            <div class="login">
                <h1><@spring.message "logon.login_to_acc"/></h1>
                <form>
                    <p><input class="input-log" type="text" name="login" value="" placeholder="<@spring.message "login"/>"></p>
                    <p><input class="input-log" type="password" name="password" value="" placeholder="<@spring.message "password"/>"></p>
                    <p class="remember-me">
                        <label>
                            <input class="input-log" type="checkbox" name="remember-me" id="remember-me">
                            <@spring.message "remember_me"/>
                        </label>
                    </p>
                    <p class="submit"><input class="input-log" type="submit" name="commit" value="<@spring.message "enter"/>"></p>
                    <div class="login-help">
                        <a href="#"><@spring.message "logon.forgot_pass"/></a> | <a href="#"><@spring.message "logon.register"/></a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</@gbc.common_page>