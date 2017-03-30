<#macro common_page pageTitle>
    <#include "_html_top.ftl">
        <#nested>
        <#include "footer.ftl">
    <#include "_html_bottom.ftl">
</#macro>


<#macro nav_logon>
    <@nav_bar>
        <#nested>
        <@lng />
        <@reg_partner_btn />
    </@nav_bar>
</#macro>

<#macro nav_logged>
    <@nav_bar>
        <#nested>
        <@lng />
        <@logout_btn />
    </@nav_bar>
</#macro>

<#macro nav_bar>
    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                        aria-expanded="false" aria-controls="navbar">
                    <#--<span class="sr-only">Toggle navigation</span>-->
                    <#--<span class="icon-bar"></span>-->
                    <#--<span class="icon-bar"></span>-->
                    <#--<span class="icon-bar"></span>-->
                    <span class="name-button">Меню</span>
                </button>
                <div class="logo">
                    <a href="/">
                        <img src="/img/getBusLogo2.svg" height="50" alt="logo">
                    </a>
                </div>
            </div>
            <div id="navbar" class="collapse navbar-collapse">
                <div class="right-menu">
                    <#nested/>
                </div>
            </div>
        </div>
    </nav>
</#macro>

<#macro lng>
    <#--TODO remove language menu for post only urls and for edit mode-->
    <ul>
        <li><a href="?lang=uk_UA">ua</a> | <a href="?lang=ru_RU">ru</a>| <a href="?lang=en_US">en</a></li>
    </ul>
</#macro>

<#macro reg_partner_btn>
    <form method="get">
        <button type="submit" formaction="/register-partner" class="btn btn-warning partners"><span
                class="glyphicon glyphicon-user"></span>
            <@spring.message "button_for_partners"/>
        </button>
    </form>
</#macro>

<#macro logout_btn>
    <form method="get">
        <button type="submit" formaction="/logout" class="btn btn-warning exit"><span class="glyphicon glyphicon-log-out"></span>
            <@spring.message "exit"/>
        </button>
    </form>
</#macro>
