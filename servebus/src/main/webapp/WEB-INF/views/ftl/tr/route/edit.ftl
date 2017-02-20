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
    <input name="name" value="${route.name!''}"/>
    <br/>
    <br/>
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
</form>

<#macro routepoint idx rp={} restrictedit=true add=true remove=true >
    <div>
        <fieldset class="route-point" data-rp-idx="${idx}">
            <input name="routePoints[${idx}].id" value="${rp.id!''}" type="hidden"/>
            <input name="routePoints[${idx}].name" value="${rp.name!''}" ${restrictedit?then('readonly','')}/>
            <input name="routePoints[${idx}].address" value="${rp.address!''}" ${restrictedit?then('readonly','')}/>
            <input name="routePoints[${idx}].arrival" value="${rp.arrival!''}"/>
            <input name="routePoints[${idx}].departure" value="${rp.departure!''}"/>
            <input name="routePoints[${idx}].tripTime" value="${rp.tripTime!''}"/>
            <input name="routePoints[${idx}].distance" value="${rp.distance!''}"/>
        </fieldset>
        <#if add>
            <a href="#" onclick="addRoutePoint(this)"><@spring.message "cabinet.partner.route.addStation"/></a>
        </#if>
        <#if remove>
            <a href="#" onclick="removeRoutePoint(this)"><@spring.message "cabinet.partner.route.removeStation"/></a>
        </#if>
    </div>
</#macro>