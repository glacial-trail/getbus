<#assign pageTitle><@spring.message "transporter.dashboard.page_title"/></#assign>

<@gbc.common_page pageTitle >
    <#--<div class="background-img backgroung-img-dashboard"></div>-->
    <@gbc.nav_logged>
        <#include "left_menu.ftl" />
    </@gbc.nav_logged>
    <section class="content">
        <div class="inner-wrap">
            <#include dashboardPageName />
        </div>
    </section>
</@gbc.common_page>