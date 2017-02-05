<#assign pageTitle><@spring.message "transporter.cabinet.page_title"/></#assign>

<@gbc.common_page pageTitle >
    <div class="background-img backgroung-img-cabinet"></div>
    <#include "/nav_logged.ftl" />
    <div class="sidebar">
        <#include "left_menu.ftl" />
    </div>
    <div class="content">
        <#include cabPageName />
    </div>
</@gbc.common_page>