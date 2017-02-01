<#macro showFieldErrors field cssClass>
    <#if spring.status.errors.hasFieldErrors(field)>
        <#list spring.status.errors.getFieldErrors(field) as error>
            <span class="${cssClass}"><@spring.message error.defaultMessage /></span>
        </#list>
    </#if>
</#macro>
