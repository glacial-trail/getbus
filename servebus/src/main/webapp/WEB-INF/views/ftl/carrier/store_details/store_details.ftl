<#-- @ftlvariable name="storeDetails" type="info.getbus.servebus.web.dto.store.StoreDetailsDTO" -->
<#import "../../macro/forms.ftl" as form>

<@spring.bind "storeDetails" />
<#assign viewMode = storeDetails.id?? />

            <div>
<#if !viewMode>
                <form method="post" action="save">
</#if>
                <@lfield name="domain" value="${storeDetails.domain}" /> <br/>
                <@lfield name="paymentDetailsPhone" value="${storeDetails.paymentDetailsPhone}" /> <br/>
                <@lfield name="paymentDetailsAccount" value="${storeDetails.paymentDetailsAccount}" /> <br/>
                <@lfield name="paymentDetailsMfo" value="${storeDetails.paymentDetailsMfo}" /> <br/>
                <@lfield name="paymentDetailsOkpo" value="${storeDetails.paymentDetailsOkpo}" /> <br/>

<#if !viewMode>
                    <button type="submit">submit</button>
                </form>
</#if>
            </div>


<#macro lfield name value class="">
    <label for="${name}">${name}</label>
    <@text name name value viewMode class />
    <@form.showFieldErrors 'account' 'error' />
</#macro>



<#macro hidden name value class="" id="">
    <@datafield id=id name=name value=value type="hidden" class=class />
</#macro>

<#macro text id name value ro=false class="">
    <@datafield id=id name=name value=value ro=ro class=class />
</#macro>

<#macro datafield id name value type="text" ro=false class="">
    <#assign iclass = class?trim />
    <#if viewMode>
        <#if type != "hidden">
            <@span id=id class=class>${value}</@>
        </#if>
    <#else>
        <@input id=id type=type name=name value=value ro=ro class=iclass />
    </#if>
</#macro>

<#macro input id name value class type="text" ro=false>
<input
    <#if id?has_content>id="${id}"</#if>
    name="${name}"
    value="${value}"
    type="${type}"
    <#if class?has_content>class="${class}"</#if>
    <#if ro>readonly</#if>
/>
</#macro>

<#macro span id class>
<span <#if id?has_content>id="${id}"</#if> <#if class?has_content>class="${class}"</#if>><#nested>&nbsp;</span>
</#macro>
