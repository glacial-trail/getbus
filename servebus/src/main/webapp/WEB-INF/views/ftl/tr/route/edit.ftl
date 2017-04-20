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

<form name="route" action="create-route" method="post">
    <input name="id" type="hidden" value="${route.id!''}"/>
    <input name="direction" value="${route.direction}" type="hidden"/>
    <label for="name">Please, enter route name</label>
    <input name="name" value="${route.name!''}" id="name" class="route_name" placeholder="enter route name field"/>
    <@form.showFieldErrors 'name' 'error' />
    <br/>
    <br/>
    <table class="table table-bordered">
        <tr>
            <th><@spring.message "route.create.addstation"/></th>
            <th><@spring.message "route.create.Country"/></th>
            <th><@spring.message "route.create.address"/></th>
            <th><@spring.message "route.create.depttime"/></th>
            <th><@spring.message "route.create.arrtime"/></th>
            <th><@spring.message "route.create.triptime"/></th>
            <th><@spring.message "route.create.distance"/></th>
        </tr>

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
        <td colspan="7">
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
        <td>
            <input name="routePoints[${idx}].name" value="${rp.name!''}" ${restrictedit?then('readonly','')} type="text" id="station" placeholder="Start station name"/>
            <@form.showFieldErrors 'routePoints[${idx}].name' 'error'/>
        </td>
        <td>
            <input name="routePoints[${idx}].countryCode" value="${rp.countryCode!''}" ${restrictedit?then('readonly','')} type="text"/>
            <@form.showFieldErrors 'routePoints[${idx}].countryCode' 'error'/>
        </td>
        <td>
            <input name="routePoints[${idx}].address" value="${rp.address!''}" ${restrictedit?then('readonly','')} type="text" placeholder="Address"/>
            <@form.showFieldErrors 'routePoints[${idx}].address' 'error'/>
        </td>
        <td><input name="routePoints[${idx}].arrival" value="${rp.departure!''}" type="text" class="time" data-format="HH:mm" data-template="HH : mm"></td>
        <td><input name="routePoints[${idx}].departure" value="${rp.arrival!''}" type="text" class="time" data-format="HH:mm" data-template="HH : mm"></td>
        <td><input name="routePoints[${idx}].tripTime" value="${rp.tripTime!''}" type="text" placeholder="Trip time"/></td>
        <td><input name="routePoints[${idx}].distance" value="${rp.distance!''}" type="text" placeholder="Distance"/></td>
    </tr>
    <tr>
        <#if add || remove>
            <td colspan="7">
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
