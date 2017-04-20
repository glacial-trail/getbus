<#import "../../macro/forms.ftl" as form>

<@spring.bind "route" />

<script type="text/javascript">
    function addRoutePoint(button) {
        var rowWithButton = $(button).parent().parent();
        rowWithButton.after($("#route-point-template tr:nth-child(2)").clone());
        rowWithButton.after($("#route-point-template tr:first-child").clone());
        reindexRoutePoints();
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
                elem.find("input").each(function () {
                    var input = $(this);
                    var newName = input.attr("name").replace(/(routePoints\[)(-?\d+)(\])/, "$1"+idx+"$3");
                    input.attr("name", newName);
                })
            }
        });
    }
</script>

<#assign isReverseRoute = !route.forward />

<table style="display: none;" id="route-point-template">
    <@routepoint idx=-1 restrictedit=isReverseRoute />
</table>

<form name="route" action="create-route" method="post" id="data">
    <input name="id" type="hidden" value="${route.id!''}"/>
    <input name="direction" value="${route.direction}" type="hidden"/>
    <label for="name">Please, enter route name</label>
    <input name="name" value="${route.name!''}" id="name" class="route_name"/>
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
            restrictedit=isReverseRoute
            add=!isReverseRoute && !routePoint?is_last
            remove=!isReverseRoute && !(routePoint?is_first||routePoint?is_last)
        />
    <#else>
        <@routepoint idx="0" restrictedit=isReverseRoute remove=false />
        <@routepoint idx="1" restrictedit=isReverseRoute add=false remove=false />
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
        <input name="routePoints[${idx}].id" value="${rp.id!''}" type="hidden"/>

        <td class="col-xs-2">
            <#--<input name="routePoints[${idx}].countryCode" value="${rp.countryCode!''}" ${restrictedit?then('readonly','')} type="text"/>-->
            <#--TODO fix country selector-->
            <select id="country" form="data" required>
                <option value="ukraine">Украина</option>
                <option value="russia">Россия</option>
                <option value="moldova">Молдова</option>
                <option value="belarus">Белорусь</option>
                <option value="poland">Польша</option>
                <option value="estonia">Эстония</option>
                <option value="latvia">Латвия</option>
                <option value="lithuania">Литва</option>
                <option value="romania">Румыния</option>
                <option value="bulgaria">Болгария</option>
                <option value="czech republic">Чехия</option>
                <option value="slovakia">Словакия</option>
                <option value="austria">Австрия</option>
                <option value="germany">Германия</option>
                <option value="france">Франция</option>
                <option value="italy">Италия</option>
                <option value="portugal">Португалия</option>
            </select>
        <#--<@form.showFieldErrors 'routePoints[${idx}].countryCode' 'error'/>-->
        </td>
        <td class="col-xs-2">
            <input name="routePoints[${idx}].name" value="${rp.name!''}" ${restrictedit?then('readonly','')} type="text" id="station"/>
            <@form.showFieldErrors 'routePoints[${idx}].name' 'error'/>
        </td>
        <td class="col-xs-2">
            <input name="routePoints[${idx}].address" value="${rp.address!''}" ${restrictedit?then('readonly','')} type="text"/>
            <@form.showFieldErrors 'routePoints[${idx}].address' 'error'/>
        </td>
        <td class="col-xs-2"><input name="routePoints[${idx}].arrival" value="${rp.arrival!''}" type="text" class="time" data-format="HH:mm" data-template="HH : mm"></td>
        <td class="col-xs-2"><input name="routePoints[${idx}].departure" value="${rp.departure!''}" type="text" class="time" data-format="HH:mm" data-template="HH : mm"></td>
        <td class="col-xs-1"><input name="routePoints[${idx}].tripTime" value="${rp.tripTime!''}" type="text" /></td>
        <td class="col-xs-1"><input name="routePoints[${idx}].distance" value="${rp.distance!''}" type="text" /></td>
    </tr>
    <tr>
        <#if add || remove>
            <td colspan="7" class="col-xs-12">
        </#if>
        <#if add>
                <a id="qwer" href="#" onclick="addRoutePoint(this)"><@spring.message "cabinet.partner.route.addStation"/></a>
        </#if>
        <#if remove>
                <a id="trr-${idx}" href="#" onclick="removeRoutePoint(this)"><@spring.message "cabinet.partner.route.removeStation"/></a>
        </#if>
        <#if add || remove>
            </td>
        </#if>
    </tr>
</#macro>
