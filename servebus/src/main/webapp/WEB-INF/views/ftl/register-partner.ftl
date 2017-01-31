<#assign pageTitle><@spring.message "register.partner.page_title"/></#assign>

<@spring.bind "user" />

<@gbc.common_page pageTitle >
    <div class="background-img background-img-transporter-reg"></div>
    <@gbc.nav_bar/>
    <div class="register-container container">
        <div class="row">
            <div class="register">
                <form method="post">
                    <h2><@spring.message "register.partner.registration_title"/></h2>
                    <label for="firstname"><@spring.message "register.partner.label.name"/></label>
                    <input type="text" id="firstname" name="firstname" placeholder="<@spring.message "register.partner.enter_name"/>" value="${user.firstname!''}">
                    <#if spring.status.errors.hasFieldErrors('firstname')>
                        <span>Please provide valid input</span>
                    </#if>
                    <label for="lastname"><@spring.message "register.partner.label.Lastname"/></label>
                    <input type="text" id="lastname" name="lastname" placeholder="<@spring.message "register.partner.enter_lastname"/>" value="${user.lastname!''}">
                    <#if spring.status.errors.hasFieldErrors('lastname')>
                        <span>Please provide valid input</span>
                    </#if>
                    <label for="e-mail">E-mail</label>
                    <input type="text" id="e-mail" name="email" placeholder="<@spring.message "register.partner.enter_email"/>" value="${user.email!''}">
                    <#if spring.status.errors.hasFieldErrors('email')>
                        <span>Please provide valid input</span>
                    </#if>
                    <label for="phone"><@spring.message "register.partner.label.Phone"/></label>
                    <input type="tel" id="phone" name="phone" placeholder="<@spring.message "register.partner.enter_phone"/>" value="${user.phone!''}">
                    <#if spring.status.errors.hasFieldErrors('phone')>
                        <span>Please provide valid input</span>
                    </#if>
                    <label for="password"><@spring.message "register.partner.label.Password"/></label>
                    <input type="password" id="password" name="password" placeholder="<@spring.message "register.partner.Password"/>">
                    <#if spring.status.errors.hasFieldErrors('password')>
                        <span>Please provide valid input</span>
                    </#if>
                    <label for="re-password"><@spring.message "register.partner.label.confirm_password"/></label>
                    <input type="password" id="re-password" name="repassword" placeholder="<@spring.message "register.partner.confirm_password"/>">
                    <#if spring.status.errors.hasFieldErrors('repassword')>
                        <span>Please provide valid input</span>
                    </#if>
                    <button type="submit"><@spring.message "register.partner.submit_registration"/></button>
                </form>
            </div>
        </div>
    </div>
</@gbc.common_page>