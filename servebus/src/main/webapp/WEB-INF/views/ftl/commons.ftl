<#macro common_page pageTitle>
    <#include "_html_top.ftl">
        <div class="wrap">
            <#include "header.ftl">
                <#nested>
            <#include "footer.ftl">
        </div>
    <#include "_html_bottom.ftl">
</#macro>