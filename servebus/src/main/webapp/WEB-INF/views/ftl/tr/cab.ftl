<#assign pageTitle><@spring.message "transporter.cabinet.page_title"/></#assign>

<@gbc.common_page pageTitle >
    <div id="backgroung-img-cabinet"></div>
    <#include "/nav_logged.ftl" />
    <div class="sidebar">
        <ul class="sidebar-nav">
            <li class="sidebar-brand">
                <a href="#">Menu</a>
            </li>
            <li>
                <a href="#">Routes</a>
            </li>
            <li>
                <a href="#">Voyages</a>
            </li>
            <li>
                <a href="#">About</a>
            </li>
            <li>
                <a href="/logout">Logout</a>
            </li>
        </ul>
    </div>
    <div class="content"></div>
</@gbc.common_page>