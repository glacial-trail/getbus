<#assign pageTitle><@spring.message "logon.page_title"/></#assign>

<@gbc.common_page pageTitle >
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
</@gbc.common_page>