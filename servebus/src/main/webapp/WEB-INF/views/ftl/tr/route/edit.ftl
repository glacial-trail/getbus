<#-- @ftlvariable name="countries" type="java.util.List<String>" -->
<#-- @ftlvariable name="route" type="info.getbus.servebus.model.route.Route" -->

<#import "../../macro/forms.ftl" as form>

<@spring.bind "route" />

<script type="text/javascript">
    var combodateConf = {
        value:'00:00',
        firstItem: 'none',
        minuteStep: 1
    };

    $(function() {
        $('.time:not(#route-point-template .time)').combodate(combodateConf);
    });

    function addRoutePoint(button) {
        var rowWithButton = $(button).parent().parent();
        rowWithButton.after($("#route-point-template tr:nth-child(2)").clone());
        var inputsRow = $("#route-point-template tr:first-child").clone();
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
            if (elem.data("rp-idx") != idx) {
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

<#assign isReverseRoute = !route.forward />

<#if !isReverseRoute>
    <table style="display: none;" id="route-point-template">
        <@routepoint idx=-1 restrictedit=isReverseRoute />
    </table>
</#if>

<form name="route" action="create-route" method="post" id="data">
    <input name="id" type="hidden" value="${route.id!''}" class="route-data"/>
    <input name="direction" value="${route.direction}" type="hidden" class="route-data"/>
    <label for="name">Please, enter route name</label>
    <input name="name" value="${route.name!''}" id="name" class="route-data route_name"/>
    <@form.showFieldErrors 'name' 'error' />
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
            restrictedit=!route.forward
            add=route.forward && !routePoint?is_last
            remove=route.forward && !(routePoint?is_first||routePoint?is_last)
        />
    <#else>
        <@routepoint idx="0" restrictedit=!route.forward remove=false />
        <@routepoint idx="1" restrictedit=!route.forward add=false remove=false />
    </#list>
    <tr>
        <td colspan="7" class="col-xs-12">
        <br/>
        <button type="reset">reset</button>
        <button formaction="cancel">cancel</button>
        <#if isReverseRoute>
            <button type="submit" formaction="back">back</button>
        </#if>
        <button type="submit" formaction="save">${isReverseRoute?then('finish','next')}</button>
    </td>
</tr>
    </table>
</form>

<#macro routepoint idx rp={} restrictedit=true add=true remove=true >
    <tr class="route-point" data-rp-idx="${idx}">
        <input name="routePoints[${idx}].id" value="${rp.id!''}" type="hidden" class="route-data"/>

        <td class="col-xs-2">
                <#if restrictedit>
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
            <input name="routePoints[${idx}].name" value="${rp.name!''}" ${restrictedit?then('readonly','')} type="text" class="route-data" id="station"/>
            <@form.showFieldErrors 'routePoints[${idx}].name' 'error'/>
        </td>
        <td class="col-xs-2">
            <input name="routePoints[${idx}].address" value="${rp.address!''}" ${restrictedit?then('readonly','')} type="text" class="route-data"/>
            <@form.showFieldErrors 'routePoints[${idx}].address' 'error'/>
        </td>
        <td class="col-xs-2"><input name="routePoints[${idx}].arrival" value="${rp.arrival!''}" type="text" class="route-data time" data-format="HH:mm" data-template="HH : mm"></td>
        <td class="col-xs-2"><input name="routePoints[${idx}].departure" value="${rp.departure!''}" type="text" class="route-data time" data-format="HH:mm" data-template="HH : mm"></td>
        <td class="col-xs-1"><input name="routePoints[${idx}].tripTime" value="${rp.tripTime!''}" type="text" class="route-data" /></td>
        <td class="col-xs-1"><input name="routePoints[${idx}].distance" value="${rp.distance!''}" type="text" class="route-data" /></td>
    </tr>
    <tr>
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
    </tr>
</#macro>
