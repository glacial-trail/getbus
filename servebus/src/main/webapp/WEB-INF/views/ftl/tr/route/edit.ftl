<script type="text/javascript">
    function addRoutePoint(button) {
        $(button).parent().after($("#route-point-template div").clone());
        reindexRoutePoints();
        return false;
    }

    function removeRoutePoint(button) {
        $(button).parent().remove();
        reindexRoutePoints();
        return false;
    }

    function reindexRoutePoints() {
        $("form[name=route] fieldset.route-point").each(function (idx) {
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

<#assign isReverseRoute = 'R' == route.direction />

<div style="display: none;" id="route-point-template">
    <@routepoint idx=-1 restrictedit=isReverseRoute />
</div>

<form name="route" action="create-route" method="post">
    <input name="id" type="hidden" value="${route.id!''}"/>
    <input name="direction" value="${route.direction}" type="hidden"/>
    <label for="name">Please, enter route name</label>
    <input name="name" value="${route.name!''}" id="name" class="route_name" placeholder="enter route name field"/>
    <#--<input type="text" id="time" data-format="HH:mm" data-template="HH : mm" name="datetime">-->
    <br/>
    <br/>
    <table class="table table-bordered">
        <tr>
            <th>Добавить станцию</th>
            <th>Адресс</th>
            <th>Время отправления</th>
            <th>Время прибытия</th>
            <th>Время в пути</th>
            <th>Дистанция</th>
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

    <br/>
    <button type="reset">reset</button>
    <button formaction="cancel">cancel</button>
    <#if isReverseRoute>
        <button type="submit" formaction="back">back</button>
    </#if>
    <button type="submit" formaction="save">${isReverseRoute?then('finish','next')}</button>
    </table>
</form>

<#macro routepoint idx rp={} restrictedit=true add=true remove=true >
<tr>
    <div>
        <fieldset class="route-point" data-rp-idx="${idx}">
            <input name="routePoints[${idx}].id" value="${rp.id!''}" type="hidden"/>

                <#--<label for="station">Добавить станцию</label>-->
            <td><input name="routePoints[${idx}].name" value="${rp.name!''}" ${restrictedit?then('readonly','')} type="text" id="station" placeholder="Start station name"/>
            </td>
            <td>
            <input name="routePoints[${idx}].address" value="${rp.address!''}" ${restrictedit?then('readonly','')} type="text" placeholder="Address"/>
            </td>
            <#--<input name="routePoints[${idx}].arrival" value="${rp.arrival!''}"/>-->
            <#--<input name="routePoints[${idx}].departure" value="${rp.departure!''}"/>-->
            <td><input type="text" class="time" data-format="HH:mm" data-template="HH : mm" name="datetime"></td>
            <td><input type="text" class="time" data-format="HH:mm" data-template="HH : mm" name="datetime"></td>
            <td><input name="routePoints[${idx}].tripTime" value="${rp.tripTime!''}" type="text" placeholder="Trip time"/></td>
            <td><input name="routePoints[${idx}].distance" value="${rp.distance!''}" type="text" placeholder="Distance"/></td>
        </fieldset>
        <#if add>
            <a href="#" onclick="addRoutePoint(this)"><@spring.message "cabinet.partner.route.addStation"/></a>
        </#if>
        <#if remove>
            <a href="#" onclick="removeRoutePoint(this)"><@spring.message "cabinet.partner.route.removeStation"/></a>
        </#if>
    </div>
</tr>
</#macro>
