<#-- @ftlvariable name="countries" type="java.util.List<String>" -->
<#-- @ftlvariable name="tzList" type="java.util.List<String>" -->

<#import "../../macro/forms.ftl" as form>

<script type="text/javascript">
    function toggleStrategy() {
        $(".js-toggle-strategy").prop( "disabled", function( i, val ) {
            return !val;
        });
        $(".js-toggle-strategy:disabled").hide();
        $(".js-toggle-strategy:enabled").show();
    }
</script>

<@spring.bind "periodicity" />

<#assign forward = periodicity.forward />
<#assign reverse = periodicity.reverse />
<#if periodicity.strategy?? >
    <#assign strategy = periodicity.strategy />
<#else>
    <#assign strategy = 'DAILY' /><#--TODO add defaultStrategy to DTO-->
</#if>

<#--TODO move to DB-->
<#assign days = ["mon", "tue", "wed", "thu", "fri", "sat", "sun"] >

<form name="periodicity" method="post">
    <fieldset>
        <input name="strategy" type="radio" value="DAILY" ${('DAILY' == strategy)?then('checked','')} onchange="toggleStrategy()">
        daily interval
        <input name="strategy" type="radio" value="WEEK_DAYS" ${('WEEK_DAYS' == strategy)?then('checked','')} onchange="toggleStrategy()">
        week days
    </fieldset>

    <br/><br/>

    <input name="routeId" value="${routeId}" type="hidden">

    <div style="float: left">
        <input name="forward.id" value="${forward.id!''}" type="hidden"/>
        <input name="forward.start" value="${forward.start!''}" type="date">
        <select name="forward.TZ">
        <#list tzList as tz>
            <option ${(forward.TZ?? && tz == forward.TZ)?then('selected','')} value="${tz}">${tz}</option>
        </#list>
        </select>

        <br/><br/>

        <input class="js-daily-strategy js-toggle-strategy" name="forward.interval" value="${forward.interval!'0'}" type="number"
            <#if 'DAILY' != strategy>
                   disabled style="display: none"
            </#if>
        >
        <br/><br/>

        <fieldset class="js-week-days-strategy js-toggle-strategy"
            <#if 'WEEK_DAYS' != strategy>
                      disabled style="display: none"
            </#if>
        >
            <@drawWeekDays directionName="forward" weekDays=forward.weekDays />
        </fieldset>
    </div>

    <div>
        <input name="reverse.id" value="${reverse.id!''}" type="hidden"/>
        <input class="js-week-days-strategy js-toggle-strategy" name="reverse.start" value="${reverse.start!''}" type="date"
            <#if 'WEEK_DAYS' = strategy>
                   disabled style="display: none"
            </#if>
        >
        <select class="js-week-days-strategy js-toggle-strategy" name="reverse.TZ"
            <#if 'WEEK_DAYS' == strategy>
                    disabled style="display: none"
            </#if>
        >
        <#list tzList as tz>
            <option ${(reverse.TZ?? && tz == reverse.TZ)?then('selected','')} value="${tz}">${tz}</option>
        </#list>
        </select>
        <br/><br/>
        <br/><br/>

        <fieldset class="js-week-days-strategy js-toggle-strategy"
            <#if 'WEEK_DAYS' != strategy>
                  disabled style="display: none"
            </#if>
        >
            <@drawWeekDays directionName="reverse" weekDays=reverse.weekDays />
        </fieldset>
    </div>

    <br/>
    <button formaction="periodicity/cancel">cancel</button>
    <input type="submit" value="save"/>
</form>


<#macro drawWeekDays directionName weekDays=[]>
    <#list days as day>
        ${day} <input name="${directionName}.weekDays[${day?index}]" type="checkbox"
                    <@drawSelected weekDays=weekDays idx=day?index />
                >
    </#list>
</#macro>

<#macro drawSelected weekDays=[] idx=0 >
    <#if weekDays?? && weekDays[idx]?? && weekDays[idx]>
        checked
    </#if>
</#macro>
