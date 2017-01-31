<#macro common_page pageTitle>
    <#include "_html_top.ftl">
        <#nested>
        <#include "footer.ftl">
    <#include "_html_bottom.ftl">
</#macro>

<#macro nav_bar>
    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                        aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <div class="logo">
                    <a href="/">
                        <img src="/img/logo.jpg" height="60" alt="logo">
                    </a>
                </div>
            </div>
            <div id="navbar" class="collapse navbar-collapse">
                <div class="right-menu">
                    <ul>
                        <li><a href="?lang=uk_UA">ua</a> | <a href="?lang=ru_RU">ru</a>| <a href="?lang=en_US">en</a></li>
                        <#nested/>
                    </ul>
                </div>
            </div>
        </div>
    </nav>
</#macro>