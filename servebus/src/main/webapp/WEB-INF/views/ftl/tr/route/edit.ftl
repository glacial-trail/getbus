<#-- @ftlvariable name="countries" type="java.util.List<String>" -->
<#-- @ftlvariable name="route" type="info.getbus.servebus.model.route.Route" -->
<#-- @ftlvariable name="viewMode" type="boolean" -->

<#import "../../macro/forms.ftl" as form>

<@spring.bind "route" />

<script type="text/javascript">
    var combodateConf = {
        format: 'HH:mm',
        template: 'HH : mm',
        value:'00:00',
        firstItem: 'none',
        minuteStep: 1
    };

    $(function() {
        $('.time:not(#route-point-template .time)').combodate(combodateConf);
    });

    function addRoutePoint(button) {
        var rowWithButton = $(button).parent().parent();
        var routePointTemplate = $("#route-point-template");
        rowWithButton.after(routePointTemplate.find("tr:nth-child(2)").clone());
        var inputsRow = routePointTemplate.find("tr:first-child").clone();
        rowWithButton.after(inputsRow);
        reindexRoutePoints();
        inputsRow.find('.time').combodate(combodateConf);
        return false;
    }

    function removeRoutePoint(button) {
        $(button).parent().parent().prev().remove();
        $(button).parent().parent().remove();
        reindexRoutePoints();
        return false;
    }

    function reindexRoutePoints() {
        $("form[name=route] tr.route-point").each(function (idx) {
            var elem = $(this);
            if (elem.data("rp-idx") !== idx) {
                elem.attr("data-rp-idx", idx);
                elem.find(".route-data:input").each(function () {
                    var input = $(this);
                    var newName = input.attr("name").replace(/(routePoints\[)(-?\d+)(\])/, "$1"+idx+"$3");
                    input.attr("name", newName);
                })
            }
        });
    }
</script>

<#--<#assign readonlyMode = false />-->
<#assign isReverseRoute = route.direction == 'R' />
<#--<#assign restrictedit = route.direction == 'R' />-->

<#if !isReverseRoute && !viewMode>
    <table style="display: none;" id="route-point-template">
        <@routepoint idx=-1 />
    </table>
</#if>

<form name="route" action="create-route" method="post" id="data">
    <@hidden name="id" value="${route.id?c!''}" />
    <@hidden name="direction" value="${route.direction}" />
    <label for="name">Please, enter route name</label>
    <@text name="name" value="${route.name!''}" id="name" class="route_name"/>
    <@form.showFieldErrors 'name' 'error' />
    <br/>
    <label for="base-price">base price</label>
    <@text name="basePrice" value="${route.basePrice!''}" id="base-price"/>
    <label for="base-seats-qty">base seats qty</label>
    <@number name="baseSeatsQty" value="${route.baseSeatsQty!''}" id="base-seats-qty"/>
    <label for="start-sales">start sales(voyage generation)</label>
    <@date name="startSales" value="${route.startSales!''}" id="start-sales"/>
    <label for="sales-depth">sales(voyage) depth</label>
    <@number name="salesDepth" value="${route.salesDepth!''}" id="sales-depth"/>
    <#--<input type="text" value="end sales(voyage generation, sales depth)">-->

    <br/>
    <br/>
    <table class="table table-bordered table-fixed">
        <thead>
            <tr>
                <th class="col-xs-2"><@spring.message "route.create.Country"/></th>
                <th class="col-xs-2"><@spring.message "route.create.addstation"/></th>
                <th class="col-xs-2"><@spring.message "route.create.address"/></th>
                <th class="col-xs-2"><@spring.message "route.create.depttime"/></th>
                <th class="col-xs-2"><@spring.message "route.create.arrtime"/></th>
                <th class="col-xs-1"><@spring.message "route.create.triptime"/></th>
                <th class="col-xs-1"><@spring.message "route.create.distance"/></th>
            </tr>
        </thead>
    <#list route.routePoints as routePoint>
        <@routepoint idx=routePoint?index rp=routePoint
            add=!routePoint?is_last
            remove=!(routePoint?is_first||routePoint?is_last)
        />
    <#else>
        <@routepoint idx="0" remove=false />
        <@routepoint idx="1" add=false remove=false />
    </#list>
    <tr>
        <td colspan="7" class="col-xs-12">
        <br/>
        <#if !viewMode>
            <button type="reset">reset</button>
            <button formaction="cancel">cancel</button>
        </#if>
        <#if viewMode>
            <#if isReverseRoute>
                <a href="${route.id?c}?direction=F">back</a>
            <#else>
                <a href="${route.id?c}?direction=R">next</a>
            </#if>
        <#else>
            <#if isReverseRoute>
                <button type="submit" formaction="back">back</button>
            <#else>
                <button type="submit" formaction="save">next</button>
            </#if>
            <button type="submit" formaction="save?finish=true">finish</button>
        </#if>
        </td>
    </tr>
    </table>
</form>

<#macro routepoint idx rp={} add=true remove=true >
    <tr class="route-point" data-rp-idx="${idx}">
        <input name="routePoints[${idx}].id" value="${(rp.id?c)!''}" type="hidden" class="route-data"/>

        <td class="col-xs-2">
                <#if viewMode>
                    <span><@spring.message 'country.'+rp.countryCode /></span>
                <#elseif isReverseRoute>
                    <input name="routePoints[${idx}].countryCode" value="${rp.countryCode!''}" type="hidden" class="route-data"/>
                    <input value="<@spring.message 'country.'+rp.countryCode />" readonly type="text"/>
                <#else>
                    <select name="routePoints[${idx}].countryCode" class="route-data" id="country">
                        <#list countries as code>
                            <option ${(rp.countryCode?? && code == rp.countryCode)?then('selected','')} value="${code}"><@spring.message 'country.'+code /></option>
                        </#list>
                    </select>
                </#if>
        <#--<@form.showFieldErrors 'routePoints[${idx}].countryCode' 'error'/>-->
        </td>
        <td class="col-xs-2">
            <@text name="routePoints[${idx}].name" value=rp.name!"" ro=isReverseRoute <#--id="station"-->/>
            <@form.showFieldErrors 'routePoints[${idx}].name' 'error'/>
        </td>
        <td class="col-xs-2">
            <@text name="routePoints[${idx}].address" value="${rp.address!''}" ro=isReverseRoute />
            <@form.showFieldErrors 'routePoints[${idx}].address' 'error'/>
        </td>
        <td class="col-xs-2"><@time name="routePoints[${idx}].arrival" value="${rp.arrival!''}" /></td>
        <td class="col-xs-2"><@time name="routePoints[${idx}].departure" value="${rp.departure!''}" /></td>
        <td class="col-xs-1"><@text name="routePoints[${idx}].tripTime" value="${rp.tripTime!''}" /></td>
        <td class="col-xs-1"><@text name="routePoints[${idx}].distance" value="${rp.distance!''}" /></td>
    </tr>
    <tr>
        <#if !isReverseRoute && !viewMode>
            <#if add || remove>
            <td colspan="7" class="col-xs-12">
            </#if>
            <#if add>
                <a href="#" onclick="addRoutePoint(this)"><@spring.message "cabinet.partner.route.addStation"/></a>
            </#if>
            <#if remove>
                <a href="#" onclick="removeRoutePoint(this)"><@spring.message "cabinet.partner.route.removeStation"/></a>
            </#if>
            <#if add || remove>
            </td>
            </#if>
        </#if>
    </tr>
</#macro>

<#macro hidden name value class="" id="">
    <@routedatafield id=id name=name value=value type="hidden" class=class />
</#macro>

<#macro text name value ro=false class="" id="">
    <@routedatafield id=id name=name value=value ro=ro class=class />
</#macro>

<#macro number name value ro=false class="" id="">
    <@routedatafield id=id name=name value=value type="number" ro=ro class=class />
</#macro>

<#macro date name value ro=false class="" id="">
    <@routedatafield id=id name=name value=value type="date" ro=ro class=class />
</#macro>

<#macro time name value ro=false class="" id="">
    <#assign iclass = ("time " + class)?trim />
    <@routedatafield id=id name=name value=value ro=ro class=iclass />
</#macro>

<#macro routedatafield id name value type="text" ro=false class="">
    <#assign iclass = ("route-data " + class)?trim />
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
