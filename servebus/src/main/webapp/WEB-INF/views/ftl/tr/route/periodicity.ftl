<#-- @ftlvariable name="periodicity" type="info.getbus.servebus.web.dto.route.PeriodicityPairDTO" -->
<#-- @ftlvariable name="countries" type="java.util.List<String>" -->
<#-- @ftlvariable name="tzList" type="java.util.List<String>" -->

<#import "../../macro/forms.ftl" as form>

<script type="text/javascript">
    function switchToDaily() {
        $(".js-daily-strategy input").prop( "disabled", false);
        $(".js-week-days-strategy").hide();
        $(".js-daily-strategy").show();
        $(".js-week-days-strategy input").prop( "disabled", true);
    }
    function switchToWeekDays() {
        $(".js-daily-strategy input").prop( "disabled", true);
        $(".js-daily-strategy").hide();
        $(".js-week-days-strategy").show();
        $(".js-week-days-strategy input").prop( "disabled", false);
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

<#--TODO move to DB or to i18n-->
<#assign days = ["mon", "tue", "wed", "thu", "fri", "sat", "sun"] >

<form name="periodicity" method="post">
    <fieldset>
        <input name="strategy" type="radio" value="DAILY" ${('DAILY' == strategy)?then('checked','')} onchange="switchToDaily()">
        daily interval
        <input name="strategy" type="radio" value="WEEK_DAYS" ${('WEEK_DAYS' == strategy)?then('checked','')} onchange="switchToWeekDays()">
        week days
    </fieldset>

    <br/><br/>

    <input name="routeId" value="${periodicity.routeId}" type="hidden">

    <div>
        <input name="forward.id" value="${forward.id!''}" type="hidden"/>
        <input name="reverse.id" value="${reverse.id!''}" type="hidden"/>

        <div>
            <span class="js-week-days-strategy js-toggle-strategy" <@hideIf 'WEEK_DAYS' != strategy/> >
                <label for="f-start">start date</label>
            </span>
            <span class="js-daily-strategy js-toggle-strategy" <@hideIf 'DAILY' != strategy/> >
                <label for="f-start">forward start date</label>
            </span>
            <input id="f-start" name="forward.start" value="${forward.start!''}" type="date">
        </div>
        <div class="js-daily-strategy js-toggle-strategy" <@hideIf 'DAILY' != strategy/> >
            <label for="r-start">reverse start date</label>
            <input id="r-start" name="reverse.start" value="${reverse.start!''}" type="date"
                <@disableIf 'DAILY' != strategy/> >
        </div>

        <div class="js-daily-strategy js-toggle-strategy" <@hideIf 'DAILY' != strategy/> >
            <label for="interval">interval</label>
            <input id="interval" name="forward.interval" value="${forward.interval!'0'}" type="number"
                <@disableIf 'DAILY' != strategy/> >
        </div>

        <div class="js-week-days-strategy js-toggle-strategy" <@hideIf 'WEEK_DAYS' != strategy/> >
            <label for="f-w-days">forward</label>
            <fieldset id="f-w-days">
                <@drawWeekDays directionName="forward" weekDays=forward.weekDays />
            </fieldset>
        </div>

        <div class="js-week-days-strategy js-toggle-strategy" <@hideIf 'WEEK_DAYS' != strategy/> >
            <label for="r-w-days">forward</label>
            <fieldset id="r-w-days">
                <@drawWeekDays directionName="reverse" weekDays=reverse.weekDays />
            </fieldset>
        </div>
    </div>

    <button formaction="periodicity/cancel">cancel</button>
    <input type="submit" value="save"/>
</form>


<#macro drawWeekDays directionName weekDays=[]>
    <#list days as day>
        ${day} <input name="${directionName}.weekDays[${day?index+1}]" type="checkbox"
                    <@drawSelected weekDays=weekDays idx=day?index+1 />
                    <@disableIf 'WEEK_DAYS' != strategy/>
                >
    </#list>
</#macro>

<#macro drawSelected weekDays=[] idx=0 >
    <#if weekDays?? && weekDays[idx]?? && weekDays[idx]>
        checked
    </#if>
</#macro>

<#macro hideIf condition>
    <#if condition>
        style="display: none"
    </#if>
</#macro>

<#macro disableIf condition>
    <#if condition>
        disabled
    </#if>
</#macro>
